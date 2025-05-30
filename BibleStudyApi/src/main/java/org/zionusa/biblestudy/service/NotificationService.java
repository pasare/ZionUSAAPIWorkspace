package org.zionusa.biblestudy.service;

import com.currencyfair.onesignal.model.notification.Field;
import com.currencyfair.onesignal.model.notification.Filter;
import com.currencyfair.onesignal.model.notification.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.biblestudy.dao.NotificationDao;
import org.zionusa.biblestudy.domain.Notification;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService extends BaseService<Notification, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationDao notificationDao;
    private final AuthenticatedUserService authenticatedUserService;

    @Autowired
    public NotificationService(NotificationDao notificationDao, AuthenticatedUserService authenticatedUserService) {
        super(notificationDao, logger, Notification.class);
        this.notificationDao = notificationDao;
        this.authenticatedUserService = authenticatedUserService;
    }

    public List<Notification> getAllByParentChurch(Integer churchId) {
        List<Notification> churchNotifications = notificationDao.findByChurchId(churchId);

        //Also add any sermon recording notifications that have been shared with this church
        churchNotifications.addAll(notificationDao.findByParentChurchId(churchId));

        //check the users security and remove notifications which do not pertain to them
        return churchNotifications;
    }

    public List<Notification> getAllByChurch(Integer churchId) {
        return notificationDao.findByChurchId(churchId);
    }

    public List<Notification> getBySource(String source) {
        return notificationDao.findBySource(source);
    }

    public List<Notification> getBySourceAndParentChurch(String source, Integer parentChurchId) {
        return notificationDao.findBySourceAndParentChurchId(source, parentChurchId);
    }

    public List<Notification> getBySourceAndGroup(String source, Integer groupId) {
        return notificationDao.findBySourceAndGroupId(source, groupId);
    }

    public List<Notification> getBySourceAndTeam(String source, Integer teamId) {
        return notificationDao.findBySourceAndTeamId(source, teamId);
    }

    public List<Notification> getBySourceAndDate(String source, LocalDate startDate, LocalDate endDate) {
        Instant startInstant = startDate.atStartOfDay().atZone(EZoneId.NEW_YORK.getValue()).toInstant();
        Instant endInstant = endDate.atStartOfDay().atZone(EZoneId.NEW_YORK.getValue()).toInstant();

        return notificationDao.findBySourceAndDateBetween(source, startDate.toString(), endDate.toString());
    }

    @Transactional
    public void saveSystemCreatedNotification(Integer objectId, Integer userId, Integer parentChurchId, Integer churchId, Integer groupId, Integer teamId, Integer teacherId, String teacherName, String displayName, String churchName, String date, String source, String notificationSubSource, String titleMessage, String subTitleMessage, String contentMessage, String notificationDateTime) {
        Notification notification = new Notification();
        notification.setDate(date);
        notification.setUserId(userId);
        notification.setParentChurchId(parentChurchId);
        notification.setChurchId(churchId);
        notification.setGroupId(groupId);
        notification.setTeamId(teamId);
        notification.setTeacherId(teacherId);
        notification.setTeacherName(teacherName);
        notification.setUserName(displayName);
        notification.setChurchName(churchName);
        notification.setSource(source);
        notification.setSubSource(notificationSubSource);
        notification.setTitle(titleMessage);
        notification.setSubtitle(subTitleMessage);
        notification.setContent(contentMessage);
        notification.setObjectId(objectId);
        notification.setIgnoreEnvironment(false);
        notification.setProcessed(false);
        notification.setNotificationDateTime(notificationDateTime);

        save(notification);
    }

    public List<Filter> createChurchLeaderFilters(Integer churchId) {
        List<Filter> churchLeaderFilters = new ArrayList<>();
        Filter leaderFilter = new Filter(Field.TAG, "role", Relation.EQUALS, EUserRole.CHURCH_LEADER.getValue());
        Filter churchFilter = new Filter(Field.TAG, "churchId", Relation.EQUALS, churchId.toString());
        churchLeaderFilters.add(churchFilter);
        churchLeaderFilters.add(leaderFilter);
        return churchLeaderFilters;
    }

    public List<Filter> createGroupLeaderFilters(Integer churchId) {
        List<Filter> groupLeaderFilters = new ArrayList<>();
        Filter groupFilter = new Filter(Field.TAG, "role", Relation.EQUALS, EUserRole.GROUP_LEADER.getValue());
        Filter churchFilter = new Filter(Field.TAG, "churchId", Relation.EQUALS, churchId.toString());
        groupLeaderFilters.add(churchFilter);
        groupLeaderFilters.add(groupFilter);
        return groupLeaderFilters;
    }

    public List<Filter> createChurchFilters(Integer churchId) {
        List<Filter> churchFilters = new ArrayList<>();
        Filter churchFilter = new Filter(Field.TAG, "churchId", Relation.EQUALS, churchId.toString());
        churchFilters.add(churchFilter);
        return churchFilters;
    }

    public List<Filter> createTeacherFilters(Integer teacherId) {
        List<Filter> teacherFilters = new ArrayList<>();
        Filter teacherFilter = new Filter(Field.TAG, "userId", Relation.EQUALS, teacherId.toString());
        teacherFilters.add(teacherFilter);
        return teacherFilters;
    }

    public List<Filter> createRequesterFilters(Integer userId) {
        List<Filter> requesterFilters = new ArrayList<>();
        Filter requesterFilter = new Filter(Field.TAG, "userId", Relation.EQUALS, userId.toString());
        requesterFilters.add(requesterFilter);
        return requesterFilters;
    }

    public List<Filter> createOverseerFilters(Integer parentChurchId, Integer userId) {
        List<Filter> overseerFilters = new ArrayList<>();
        Filter approveFilter = new Filter(Field.TAG, "access", Relation.EQUALS, EUserRole.OVERSEER.getValue());
        Filter churchFilter = new Filter(Field.TAG, "churchId", Relation.EQUALS, parentChurchId.toString());
        overseerFilters.add(approveFilter);
        overseerFilters.add(churchFilter);
        return overseerFilters;
    }

    public List<Filter> createAdminFilters(Integer userId) {
        List<Filter> adminFilters = new ArrayList<>();
        Filter userFilter = new Filter(Field.TAG, "userId", Relation.NOT_EQUALS, userId.toString());
        Filter approveFilter = new Filter(Field.TAG, "access", Relation.EQUALS, EUserRole.ADMIN.getValue());
        adminFilters.add(approveFilter);
        adminFilters.add(userFilter);
        return adminFilters;
    }

    public List<Filter> createEastCoastFilters() {
        List<Filter> eastCoastFilters = new ArrayList<>();
        Filter eastCoastFilter = new Filter(Field.TAG, "churchId", Relation.NOT_EQUALS, "");
        eastCoastFilters.add(eastCoastFilter);
        return eastCoastFilters;
    }


    private List<Notification> filterNotifications(List<Notification> notifications) {
        AuthenticatedUser authenticatedUser = this.authenticatedUserService.getAuthenticatedUser();

        switch (authenticatedUser.getAccess()) {
            case "Admin":
                return notifications;
            case "Church":
                if (authenticatedUser.getRole().equals("Overseer")) return notifications;
                return notifications.stream().filter(notification -> notification.getChurchId().equals(authenticatedUser.getChurchId())).collect(Collectors.toList());
            default:
                return notifications.stream().filter(notification ->
                    notification.getUserId().equals(authenticatedUser.getId()) || notification.getSource().equals(Notification.BIBLE_STUDY_SOURCE)).collect(Collectors.toList());
        }
    }


}
