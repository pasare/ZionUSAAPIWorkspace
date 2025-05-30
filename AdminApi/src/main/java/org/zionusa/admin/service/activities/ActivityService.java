package org.zionusa.admin.service.activities;

import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.admin.dao.ChallengeActivityDao;
import org.zionusa.admin.dao.ChallengeDao;
import org.zionusa.admin.dao.activities.ActivityCategoryDao;
import org.zionusa.admin.dao.activities.ActivityCategoryViewDao;
import org.zionusa.admin.dao.activities.ActivityDao;
import org.zionusa.admin.dao.activities.ActivityLogDao;
import org.zionusa.admin.domain.Challenge;
import org.zionusa.admin.domain.ChallengesActivity;
import org.zionusa.admin.domain.MemberRecentSearch;
import org.zionusa.admin.domain.Notification;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.admin.domain.activities.ActivityLog;
import org.zionusa.admin.service.NotificationService;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.base.util.notifications.PushNotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.zionusa.admin.domain.Announcement.*;

@Service
public class ActivityService extends BaseService<Activity, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);
    private final ActivityCategoryViewDao viewDao;
    private final ActivityDao dao;
    private final ChallengeDao challengeDao;
    private final ChallengeActivityDao challengeActivityDao;
    private final ActivityCategoryDao activityCategoryDao;
    private final NotificationService notificationService;
    private final MessageSource messageSource;
    private final ActivityLogDao activityLogDao;
    @Value("${notification.key}")
    private String notificationKey;
    @Value("${notification.app.id}")
    private String notificationAppId;

    @Autowired
    public ActivityService(ActivityCategoryViewDao viewDao,
                           ActivityDao dao,
                           ChallengeDao challengeDao,
                           ChallengeActivityDao challengeActivityDao,
                           ActivityCategoryDao activityCategoryDao,
                           ActivityLogDao activityLogDao,
                           NotificationService notificationService,
                           MessageSource messageSource) {
        super(dao, logger, Activity.class);
        this.viewDao = viewDao;
        this.dao = dao;
        this.challengeDao = challengeDao;
        this.challengeActivityDao = challengeActivityDao;
        this.activityCategoryDao = activityCategoryDao;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
        this.activityLogDao = activityLogDao;
    }

    public List<Activity> getActivities(Boolean archived) {
        logger.warn("Getting all approved and pending activities");
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Activity> activities = super.getAll(archived);
        return filterActivitiesByPublishedOrPendingApprovalOrPublishing(activities, authenticatedUser);
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        logger.warn("Retrieving display group activity logs");
        return getAllDisplayFromList(viewDao.findAll(), columns);
    }

    public MemberRecentSearch getMemberSearchActivities() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = today.plusDays(-30);

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // get the user activity logs in the past 30 days
        List<ActivityLog> activityLogs = this.activityLogDao.getActivityLogsByUserIdAndDateBetweenOrderByDateDescIdDesc(authenticatedUser.getId(), thirtyDaysAgo.toString(), today.toString());

        MemberRecentSearch memberRecentSearch = new MemberRecentSearch();

        List<Activity> recentActivities = new ArrayList<>(activityLogs.size());
        for (ActivityLog activityLog : activityLogs) {
            recentActivities.add(activityLog.getActivity());
        }

        memberRecentSearch.activitesFromPast30Days = recentActivities;
        memberRecentSearch.allActivities = this.getApprovedActivities();

        return memberRecentSearch;
    }

    public List<ActivityCategory> getActivityCategories() {
        return activityCategoryDao.findAll();
    }

    public List<Activity> getActivitiesByCategoryId(Integer id) {
        logger.warn("Getting all activities by category id");
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Activity> activities = dao.getActivitiesByCategoryId(id);
        return filterActivitiesByPublishedOrPendingApprovalOrPublishing(activities, authenticatedUser);
    }

    public List<Activity> getApprovedActivities() {
        return dao.getActivitiesByApprovedTrue();
    }

    public List<Activity> getPendingActivities() {
        return dao.getActivitiesByApprovedIsNull();
    }

    public List<Activity> getUnapprovedActivities() {
        return dao.getActivitiesByApprovedIsFalse();
    }

    @Override
    public Activity save(Activity activity) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean sendNotification = activity.getId() == null;
        boolean shouldApprove = activity.getApproved() != null && activity.getApproved();
        boolean shouldPublish = activity.isPublished();

        //if submitted by overseer, admin, or editorial it will be automatically approved and no notification will be sent
        if (activity.getRequesterId().equals(authenticatedUser.getId()) && (
            authenticatedUser.getAccess().equalsIgnoreCase("Overseer")
                || authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
                || (authenticatedUser.getUserApplicationRoles() != null
                && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))
        )) {
            shouldApprove = true;
            sendNotification = false;
        }

        Activity savedActivity = dao.save(activity);

        // handle approval, if previously approved do not approve again
        if (savedActivity.getId() != null && shouldApprove) {
            boolean alreadyApproved = dao.existsActivitiesByIdAndApprovedIsTrue(savedActivity.getId());
            if (!alreadyApproved) {
                savedActivity = approveActivity(savedActivity.getId());
            }
        }

        //handle publish, if previously published should not publish again
        if (savedActivity.getId() != null && shouldPublish) {
            boolean alreadyPublished = dao.existsActivityByIdAndEditorIdIsNotNull(savedActivity.getId());
            if (!alreadyPublished) {
                savedActivity = publishActivity(savedActivity.getId());
            }
        }

        //create notification that activity is ready to be approved
        if (sendNotification) saveActivityAddedNotification(savedActivity);

        return savedActivity;
    }

    @Transactional
    public void archive(Integer id) {
        Optional<Activity> activityOptional = dao.findById(id);

        if (!activityOptional.isPresent())
            throw new NotFoundException("Cannot archive an activity that does not exist");

        Activity activity = activityOptional.get();
        activity.setArchived(true);

        dao.save(activity);
    }

    @Override
    public void delete(Integer id) {
        List<ChallengesActivity> challengesActivities = challengeActivityDao.findAllByActivityId(id);

        for (ChallengesActivity challengeActivity : challengesActivities) {
            Optional<Challenge> challengeOptional = challengeDao.findById(challengeActivity.getChallengeId());

            if (!challengeOptional.isPresent())
                throw new NotFoundException("Cannot continue delete operation because the challenge does not exist");

            Challenge challenge = challengeOptional.get();
            List<Activity> activities = challenge.getActivities();
            activities.removeIf(targetActivity -> targetActivity.getId().equals(id));
            challengeDao.save(challenge);
        }
        dao.deleteById(id);
    }

    @Modifying
    public Activity approveActivity(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Activity> activityOptional = dao.findById(id);

        if (!activityOptional.isPresent())
            throw new NotFoundException("Cannot approve an activity that does not exist");

        Activity activity = activityOptional.get();

        //For security only certain roles can actually do approvals
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || authenticatedUser.getAccess().equalsIgnoreCase("OVERSEER")
            || authenticatedUser.getUserApplicationRoles() != null
            && (authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))
        ) {
            dao.setActivityApproved(id, authenticatedUser.getId(), authenticatedUser.getDisplayName());
            activity.setApproved(true);
            activity.setApproverId(authenticatedUser.getId());
            activity.setApproverName(authenticatedUser.getDisplayName());
            saveActivityApprovedNotification(activity);
        } else {
            throw new ForbiddenException("That operation is not allowed");
        }

        return activity;
    }

    @Modifying
    public void denyActivity(Integer id, String feedback) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Activity> activityOptional = dao.findById(id);

        if (!activityOptional.isPresent())
            throw new NotFoundException("Cannot deny an activity that does not exist");

        Activity activity = activityOptional.get();

        //For security only certain roles can actually do approvals
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || authenticatedUser.getAccess().equalsIgnoreCase("OVERSEER")) {
            dao.setActivityUnapproved(id, authenticatedUser.getId(), authenticatedUser.getDisplayName(), feedback);
            activity.setApproved(false);
            activity.setApproverId(authenticatedUser.getId());
            activity.setApproverName(authenticatedUser.getDisplayName());
        } else {
            throw new ForbiddenException("That operation is not allowed");
        }

    }

    public Activity publishActivity(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Activity> activityOptional = dao.findById(id);

        if (!activityOptional.isPresent())
            throw new NotFoundException("Cannot publish an activity that does not exist");

        Activity activity = activityOptional.get();

        //For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {

            if (activity.getApproved() == null || !activity.getApproved()) {
                approveActivity(id);
            }

            String publishedDate = activity.getNotificationDateAndTime() != null
                ? LocalDateTime.parse(activity.getNotificationDateAndTime(), DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ISO_DATE)
                : LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);

            dao.setActivityPublished(id, authenticatedUser.getId(), authenticatedUser.getDisplayName(), publishedDate);
            activity.setEditorId(authenticatedUser.getId());
            activity.setEditorName(authenticatedUser.getDisplayName());
            activity.setPublishedDate(publishedDate);
            saveActivityPublishedNotification(activity);
        } else {
            throw new ForbiddenException("Cannot perform the operation");
        }


        return activity;
    }

    public void unPublishActivity(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Activity> activityOptional = dao.findById(id);

        if (!activityOptional.isPresent())
            throw new NotFoundException("Cannot un-publish an activity that does not exist");

        Activity activity = activityOptional.get();

        //For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {
            dao.setActivityUnpublished(id);
            activity.setEditorId(null);
            activity.setEditorName(null);
            activity.setPublishedDate(null);
        }
    }

    private List<Activity> filterActivitiesByPublishedOrPendingApprovalOrPublishing(List<Activity> activities, AuthenticatedUser authenticatedUser) {
        return activities.stream().filter(activity ->
            (activity.getApproved() != null
                && activity.getApproved()
                && (activity.isPublished() && (activity.getNotificationDateAndTime() == null || LocalDateTime.parse(activity.getNotificationDateAndTime()).isBefore(LocalDateTime.now())))
                && !activity.isArchived())
                || ((activity.getApproved() == null || activity.getPublishedDate() == null)
                && activity.getRequesterId() != null
                && activity.getRequesterId().equals(authenticatedUser.getId()))).collect(Collectors.toList());
    }

    public void saveActivityAddedNotification(Activity activity) {

        String date = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        String titleMessage = "activity.added.title";
        String subTitleMessage = "activity.added.subtitle";
        String contentMessage = "activity.added.content";
        String notificationSource = Notification.ACTIVITIES_SOURCE;

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        notificationService.saveSystemCreatedNotification(
            activity.getId(),
            authenticatedUser.getId(),
            null,
            authenticatedUser.getChurchId(),
            authenticatedUser.getGroupId(),
            authenticatedUser.getTeamId(),
            authenticatedUser.getDisplayName(),
            authenticatedUser.getChurchName(),
            date,
            notificationSource,
            ACTIVITY_ADDED_SUB_SOURCE,
            titleMessage,
            subTitleMessage,
            contentMessage,
            activity.getNotificationDateAndTime()
        );

    }

    public void saveActivityApprovedNotification(Activity activity) {
        String date = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        String notificationSource = Notification.ACTIVITIES_SOURCE;

        // notification for editors to review the activity
        String titleMessage = "activity.approved.title";
        String subTitleMessage = "activity.approved.subtitle";
        String contentMessage = "activity.approved.content";

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        notificationService.saveSystemCreatedNotification(
            activity.getId(),
            authenticatedUser.getId(),
            null,
            authenticatedUser.getChurchId(),
            authenticatedUser.getGroupId(),
            authenticatedUser.getTeamId(),
            authenticatedUser.getDisplayName(),
            authenticatedUser.getChurchName(),
            date,
            notificationSource,
            ACTIVITY_APPROVED_SUB_SOURCE,
            titleMessage,
            subTitleMessage,
            contentMessage,
            activity.getNotificationDateAndTime()
        );
    }

    public void saveActivityPublishedNotification(Activity activity) {
        String churchTitleMessage = "activity.new.title";
        String churchSubTitleMessage = "activity.new.subtitle";
        String churchContentMessage = "activity.new.content";
        String notificationSource = Notification.ACTIVITIES_SOURCE;

        // notification for all church members that there is a new post
        notificationService.saveSystemCreatedNotification(
            activity.getId(),
            activity.getRequesterId(),
            null,
            activity.getChurchId(),
            activity.getGroupId(),
            activity.getTeamId(),
            activity.getRequesterName(),
            activity.getChurchName(),
            activity.getDate(),
            notificationSource,
            ACTIVITY_NEW_SUB_SOURCE,
            churchTitleMessage,
            churchSubTitleMessage,
            churchContentMessage,
            activity.getNotificationDateAndTime()
        );

        //notification for the creator that their activity was approved
        String requesterTitleMessage = "activity.published.title";
        String requesterSubTitleMessage = "activity.published.subtitle";
        String requesterContentMessage = "activity.published.content";

        // notification for all church members that there is a new post
        notificationService.saveSystemCreatedNotification(
            activity.getId(),
            activity.getRequesterId(),
            null,
            activity.getChurchId(),
            activity.getGroupId(),
            activity.getTeamId(),
            activity.getRequesterName(),
            activity.getChurchName(),
            activity.getDate(),
            notificationSource,
            ACTIVITY_PUBLISHED_SUB_SOURCE,
            requesterTitleMessage,
            requesterSubTitleMessage,
            requesterContentMessage,
            activity.getNotificationDateAndTime()
        );

    }

    public void sendActivityApprovalNeededPushNotification(Notification notification) {
        Optional<Activity> activityOptional = dao.findById(notification.getObjectId());

        if (activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{activity.getName(), notification.getUserName()};
            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("activityId", activity.getId().toString());
            additionalData.put("status", "ApprovalNeeded");
            additionalData.put("type", "Activity");

            Integer parentChurchId = notification.getParentChurchId();
            if (parentChurchId == null) parentChurchId = notification.getChurchId();

            // send notification to approver
            List<Filter> filters = notificationService.createOverseerFilters(parentChurchId);
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

            // send notification to all admins as well
            List<Filter> adminFilters = notificationService.createAdminFilters(notification.getUserId());
            pushNotificationService.createPushNotification(
                adminFilters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );
        }
    }

    public void sendActivityApprovedPushNotification(Notification notification) {
        Optional<Activity> activityOptional = dao.findById(notification.getObjectId());

        if (activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{activity.getName(), notification.getUserName()};
            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("activityId", activity.getId().toString());
            additionalData.put("status", "Approved");
            additionalData.put("type", "Activity");

            // send notification to the editors
            List<Filter> filters = notificationService.createEditorFilters();
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );
        }
    }

    public void sendActivityPublishedPushNotification(Notification notification) {
        Optional<Activity> activityOptional = dao.findById(notification.getObjectId());

        if (activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{activity.getName()};
            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("activityId", activity.getId().toString());
            additionalData.put("status", "Published");
            additionalData.put("type", "Activity");

            // send notification to the editors
            List<Filter> filters = notificationService.createOwnerFilters(notification.getUserId());
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );
        }
    }

    //# not used right so that members are not overwhelmed with alerts
    /* public void sendActivityNewPushNotification(Notification notification) {
        Optional<Activity> activityOptional = dao.findById(notification.getObjectId());

        if (activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{activity.getName(), notification.getUserName()};

            // send notification to the church
            List<Filter> filters = notificationService.createChurchFilters(notification.getChurchId(), notification.getUserId());
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
    } */

    @PreAuthorize("hasAuthority('Admin')")
    public void manualSendPrayerTimeNotification(String deliveryTimeOfDay) {
        sendPrayerTimeNotification(deliveryTimeOfDay, false);
    }

    @Transactional
    public void sendPrayerTimeNotification(String deliveryTimeOfDay, Boolean scheduled) {
        Random generator = new Random();
        Integer categoryId = 3;

        List<Activity> activities = dao.getActivitiesByCategoryIdAndPublishedAndUseForNotifications(categoryId, true, true);

        if (activities != null && activities.size() > 0) {

            Activity activity = activities.get(generator.nextInt(activities.size()));

            Notification notification = new Notification();
            notification.setTitle("activity.prayer.title");
            notification.setSubtitle("activity.prayer.subtitle");
            notification.setContent("activity.prayer.content");

            String[] titleParams = new String[]{(scheduled
                ? "Let us follow God's example and pray for others!"
                : "Test prayer time notification")};
            String[] contentParams = new String[]{(activity.getName())};
            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("activityId", activity.getId().toString());
            additionalData.put("status", "Published");
            additionalData.put("type", "Activity");

            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            // Send notification based on the user's timezone
            List<Filter> filters = notificationService.createEastCoastFilters();
            pushNotificationService.createPushNotificationBasedOnTimezone(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getContent(),
                contentParams,
                deliveryTimeOfDay,
                additionalData
            );
            logger.warn("Scheduled prayer time notification for {}.", deliveryTimeOfDay);
        } else {
            logger.warn("Could not find any prayer (id: {}) activities for prayer time.", categoryId);
        }
    }

}
