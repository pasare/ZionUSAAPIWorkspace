package org.zionusa.admin.service;

import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zionusa.admin.dao.GoalLogDao;
import org.zionusa.admin.domain.GoalLog;
import org.zionusa.admin.domain.Notification;
import org.zionusa.admin.domain.activities.ActivityShareType;
import org.zionusa.admin.service.activities.ActivityShareTypeService;
import org.zionusa.base.domain.Member;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.notifications.PushNotificationService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.zionusa.admin.domain.Announcement.GROUP_GOAL_ADDED_SUB_SOURCE;

@Service
public class GoalLogService extends BaseService<GoalLog, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(GoalLogService.class);
    private final ActivityShareTypeService activityShareTypeService;
    private final GoalLogDao dao;
    private final RestService restService;
    private final NotificationService notificationService;
    private final MessageSource messageSource;
    @Value("${notification.key}")
    private String notificationKey;
    @Value("${notification.app.id}")
    private String notificationAppId;

    @Autowired
    public GoalLogService(ActivityShareTypeService activityShareTypeService, GoalLogDao dao, RestService restService, NotificationService notificationService, MessageSource messageSource) {
        super(dao, logger, GoalLog.class);
        this.activityShareTypeService = activityShareTypeService;
        this.dao = dao;
        this.restService = restService;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
    }

    public List<GoalLog> getGoalLogsByDateBetween(String startDate, String endDate) {
        return dao.getGoalLogsByStartDateBetween(startDate, endDate);
    }

    public List<GoalLog> getGoalLogsByUserIdAndDateBetween(Integer userId, String startDate, String endDate) {
        return dao.getGoalLogsByUserIdAndStartDateBetween(userId, startDate, endDate);
    }

    public List<GoalLog> getGoalLogsByUserIdAndStartDateLessThanAndEndDateGreaterThan(Integer userId, String date) {
        return dao.getGoalLogsByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(userId, date, date);
    }

    public List<GoalLog> getGoalLogsByUserIdAndStartDateAfterAndEndDateAfter(Integer userId, String startDate, String endDate) {
        return dao.getGoalLogsByUserIdAndStartDateAfterAndEndDateAfter(userId, startDate, endDate);
    }

    public List<GoalLog> getGoalLogsByUserId(Integer userId) {
        return dao.getGoalLogsByUserId(userId);
    }

    @Transactional
    @Override
    public GoalLog save(GoalLog goalLog) {
        // Save activity board goal
        boolean isNew = goalLog.getId() == null;
        GoalLog authenticatedUserSavedGoalLog = dao.save(goalLog);

        // Check for sharing this activity board goal
        if (isNew && goalLog.getShareReferenceId() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Get the Activity Share Types
            List<ActivityShareType> activityShareTypes = activityShareTypeService.getAll(null);
            HashMap<Integer, String> activityShareTypeMap = new HashMap<>();

            for (ActivityShareType activityShareType : activityShareTypes) {
                activityShareTypeMap.put(activityShareType.getId(), activityShareType.getName());
            }
            String applicationRole = activityShareTypeMap.get(goalLog.getShareReferenceId());
            if (authenticatedUser.getUserApplicationRoles().contains(applicationRole)) {
                //
                Member[] members = new Member[0];
                if (applicationRole.equals(ActivityShareType.CHURCH_ACCESS)) {
                    members = restService.getAllDisplayChurchMembers(request, authenticatedUser.getChurchId());
                } else if (applicationRole.equals(ActivityShareType.GROUP_ACCESS)) {
                    members = restService.getAllDisplayGroupMembers(request, authenticatedUser.getGroupId());
                } else if (applicationRole.equals(ActivityShareType.BRANCH_ACCESS)) {
                    members = restService.getAllDisplayChurchMembers(request, authenticatedUser.getChurchId());
                } else if (applicationRole.equals(ActivityShareType.TEAM_ACCESS)) {
                    members = restService.getAllDisplayTeamMembers(request, authenticatedUser.getTeamId());
                }

                // TODO: Check this case
                if (members != null && members.length > 0) {
                    // Set the goal logs for all members except the authenticated user
                    List<GoalLog> goalLogs = Arrays.stream(members)
                        .filter(member -> !member.getId().equals(authenticatedUser.getId()))
                        .map(member -> {
                            GoalLog otherMemberGoalLog = new GoalLog();
                            otherMemberGoalLog.setChallenge(goalLog.getChallenge());
                            otherMemberGoalLog.setCompleted(false);
                            otherMemberGoalLog.setStartDate(goalLog.getStartDate());
                            otherMemberGoalLog.setEndDate(goalLog.getEndDate());
                            otherMemberGoalLog.setChallengeId(goalLog.getChallengeId());
                            otherMemberGoalLog.setUserId(member.getId());
                            return otherMemberGoalLog;
                        }).collect(Collectors.toList());

                    dao.saveAll(goalLogs);
                } else {
                    logger.warn("There are no members");
                }
            } else {
                logger.warn("The user does not have the right user application roles");
            }
        }

        return authenticatedUserSavedGoalLog;
    }

    private void saveGroupGoalLogNotification(GoalLog goalLog) {
        String date = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        String titleMessage = "goal.group.title";
        String subTitleMessage = "goal.group.subtitle";
        String contentMessage = "goal.group.content";
        String notificationSource = Notification.ACTIVITIES_SOURCE;
        String notificationSubSource = GROUP_GOAL_ADDED_SUB_SOURCE;

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        notificationService.saveSystemCreatedNotification(
            goalLog.getId(),
            authenticatedUser.getId(),
            authenticatedUser.getParentChurchId(),
            authenticatedUser.getChurchId(),
            authenticatedUser.getGroupId(),
            authenticatedUser.getTeamId(),
            authenticatedUser.getDisplayName(),
            authenticatedUser.getChurchName(),
            date,
            notificationSource,
            notificationSubSource,
            titleMessage,
            subTitleMessage,
            contentMessage,
            goalLog.getNotificationDateAndTime()
        );
    }

    public void sendGroupGoalNewPushNotification(Notification notification) {
        Optional<GoalLog> goalLogOptional = dao.findById(notification.getObjectId());

        if (goalLogOptional.isPresent()) {
            GoalLog goalLog = goalLogOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{goalLog.getChallenge().getName(), goalLog.getStartDate()};

            // send notification to the church
            List<Filter> filters = notificationService.createGroupFilters(notification.getGroupId(), notification.getUserId());
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams
            );
        }
    }
}
