package org.zionusa.admin.service;

import com.currencyfair.onesignal.model.notification.Field;
import com.currencyfair.onesignal.model.notification.Filter;
import com.currencyfair.onesignal.model.notification.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.admin.dao.NotificationDao;
import org.zionusa.admin.domain.Notification;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService extends BaseService<Notification, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationDao notificationDao;

    @Autowired
    public NotificationService(MessageSource messageSource, NotificationDao notificationDao) {
        super(notificationDao, logger, Notification.class);
        this.notificationDao = notificationDao;
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

    public List<Notification> getAllByChurchAndDate(Integer churchId, String startDate, String endDate) {

        List<Notification> churchNotifications = notificationDao.findByChurchIdAndDateBetween(churchId, startDate, endDate);

        //check the users security and remove notifications which do not pertain to them
        return filterNotifications(churchNotifications);
    }

    public List<Notification> getBySource(String source) {
        return notificationDao.findBySource(source);
    }

    public List<Notification> getBySourceAndParentChurch(String source, Integer parentChurchId) {
        return notificationDao.findBySourceAndParentChurchId(source, parentChurchId);
    }

    public List<Notification> getBySourceAndChurch(String source, Integer churchId, Integer parentChurchId) {

        return notificationDao.findBySourceAndChurchId(source, churchId);
    }

    public List<Notification> getBySourceAndGroup(String source, Integer groupId) {
        return notificationDao.findBySourceAndGroupId(source, groupId);
    }

    public List<Notification> getBySourceAndTeam(String source, Integer teamId) {
        return notificationDao.findBySourceAndTeamId(source, teamId);
    }

    public List<Notification> getBySourceAndUser(String source, Integer preacherId) {
        return notificationDao.findBySourceAndUserId(source, preacherId);
    }

    public List<Notification> getBySourceAndDate(String source, String startDate, String endDate) {

        return notificationDao.findBySourceAndDateBetween(source, startDate, endDate);
    }

    private List<Notification> filterNotifications(List<Notification> notifications) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final String access = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(access) || (EUserAccess.CHURCH.is(access) && EUserRole.OVERSEER.is(authenticatedUser.getRole()))) {
            return notifications;
        }
        return notifications
                .stream()
                .filter(notification -> notification.getChurchId().equals(authenticatedUser.getChurchId()))
                .collect(Collectors.toList());
    }

    public void prepareBibleStudyPushNotification(Notification notification) {

    }

    public void preparePreachingPushNotification(Notification notification) {

    }

    public void prepareEventPushNotification(Notification notification) {

    }

    @Transactional
    public void saveSystemCreatedNotification(Integer objectId, Integer userId, Integer parentChurchId, Integer churchId, Integer groupId, Integer teamId, String displayName, String churchName, String date, String source, String notificationSubSource, String titleMessage, String subTitleMessage, String contentMessage, String notificationDateTime) {
        Notification notification = new Notification();
        notification.setDate(date);
        notification.setUserId(userId);
        notification.setParentChurchId(parentChurchId);
        notification.setChurchId(churchId);
        notification.setGroupId(groupId);
        notification.setTeamId(teamId);
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

    public List<Filter> createOverseerFilters(Integer churchId) {
        List<Filter> overseerFilters = new ArrayList<>();
        Filter approveFilter = new Filter(Field.TAG, "access", Relation.EQUALS, EUserRole.OVERSEER.getValue());
        Filter churchFilter = new Filter(Field.TAG, "churchId", Relation.EQUALS, churchId.toString());
        overseerFilters.add(approveFilter);
        overseerFilters.add(churchFilter);
        return overseerFilters;
    }

    public List<Filter> createAdminFilters(Integer userId) {
        List<Filter> adminFilters = new ArrayList<>();
        Filter userFilter = new Filter(Field.TAG, "userId", Relation.NOT_EQUALS, userId.toString());
        Filter approveFilter = new Filter(Field.TAG, "access", Relation.EQUALS, EUserAccess.ADMIN.getValue());
        adminFilters.add(approveFilter);
        adminFilters.add(userFilter);
        return adminFilters;
    }

    public List<Filter> createEastCoastFilters() {
        List<Filter> eastCoastFilters = new ArrayList<>();
        Filter eastCoastFilter = new Filter(Field.TAG, "churchId", Relation.NOT_EQUALS, null);
        eastCoastFilters.add(eastCoastFilter);
        return eastCoastFilters;
    }

    public List<Filter> createChurchFilters(Integer churchId) {
        List<Filter> churchFilters = new ArrayList<>();
        Filter churchFilter = new Filter(Field.TAG, "churchId", Relation.EQUALS, churchId.toString());
        churchFilters.add(churchFilter);
        return churchFilters;
    }

    public List<Filter> createGroupFilters(Integer groupId, Integer userId) {
        Filter userFilter = new Filter(Field.TAG, "userId", Relation.NOT_EQUALS, userId.toString());
        List<Filter> groupFilters = new ArrayList<>();
        Filter groupFilter = new Filter(Field.TAG, "groupId", Relation.EQUALS, groupId.toString());
        groupFilters.add(groupFilter);
        groupFilters.add(userFilter);
        return groupFilters;
    }

    public List<Filter> createOwnerFilters(Integer userId) {
        List<Filter> ownerFilters = new ArrayList<>();
        Filter ownerFilter = new Filter(Field.TAG, "userId", Relation.EQUALS, userId.toString());
        ownerFilters.add(ownerFilter);
        return ownerFilters;
    }

    public List<Filter> createEditorFilters() {
        List<Filter> editorFilters = new ArrayList<>();
        Filter editorFilter = new Filter(Field.TAG, "editor", Relation.EQUALS, "true");
        editorFilters.add(editorFilter);
        return editorFilters;
    }


}
