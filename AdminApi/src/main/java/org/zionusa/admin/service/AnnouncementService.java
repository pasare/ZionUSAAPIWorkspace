package org.zionusa.admin.service;

import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.AnnouncementDao;
import org.zionusa.admin.dao.AnnouncementTypeDao;
import org.zionusa.admin.domain.Announcement;
import org.zionusa.admin.domain.AnnouncementType;
import org.zionusa.admin.domain.Notification;
import org.zionusa.admin.enums.EAnnouncementType;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.base.util.notifications.PushNotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.zionusa.admin.domain.Announcement.*;


@Service
public class AnnouncementService extends BaseService<Announcement, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(AnnouncementService.class);

    @Value("${notification.key}")
    private String notificationKey;

    @Value("${notification.app.id}")
    private String notificationAppId;

    private final MessageSource messageSource;

    private final NotificationService notificationService;

    private final AnnouncementDao dao;

    private final AnnouncementTypeDao announcementTypeDao;

    @Autowired
    public AnnouncementService(MessageSource messageSource, NotificationService notificationService, AnnouncementDao dao, AnnouncementTypeDao announcementTypeDao) {
        super(dao, logger, Announcement.class);
        this.messageSource = messageSource;
        this.notificationService = notificationService;
        this.dao = dao;
        this.announcementTypeDao = announcementTypeDao;
    }

    public List<Announcement> getGeneralAnnouncements() {
        logger.debug("Getting all approved, published and pending general announcements ");
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Announcement> announcements = dao.getAnnouncementsByTypeId(EAnnouncementType.GENERAL.getValue());
        return filterAnnouncementsByPublishedOrPendingApprovalOrPublishing(announcements, authenticatedUser);
    }

    public List<Announcement> getFragranceAnnouncements() {
        logger.debug("Getting all approved, published and pending fragrances");
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Announcement> fragrances = dao.getAnnouncementsByTypeId(EAnnouncementType.FRAGRANCE.getValue());
        return filterAnnouncementsByPublishedOrPendingApprovalOrPublishing(fragrances, authenticatedUser);
    }

    public List<Announcement> getApprovedAndPublishedAnnouncements() {
        return dao.getAnnouncementsByApprovedIsTrueAndPublishedIsTrue();
    }

    public List<Announcement> getHelpVideos() {
        return dao.getAnnouncementsByTypeId(EAnnouncementType.HELP_VIDEO.getValue());
    }

    public List<Announcement> getApprovedAndPublishedGeneralAnnouncements() {
        return dao.getAnnouncementsByTypeIdAndApprovedIsTrueAndPublishedIsTrue(EAnnouncementType.GENERAL.getValue());
    }

    public List<Announcement> getApprovedAndPublishedFragranceAnnouncements() {
        return dao.getAnnouncementsByTypeIdAndApprovedIsTrueAndPublishedIsTrue(EAnnouncementType.FRAGRANCE.getValue());
    }

    public List<Announcement> getPendingAnnouncements() {
        return dao.getAnnouncementsByApprovedIsNull();
    }

    public List<Announcement> getPendingGeneral() {
        return dao.getAnnouncementsByTypeIdAndApprovedIsNull(EAnnouncementType.GENERAL.getValue());
    }

    public List<Announcement> getPendingFragrances() {
        return dao.getAnnouncementsByTypeIdAndApprovedIsNull(EAnnouncementType.FRAGRANCE.getValue());
    }

    public List<Announcement> getUnapprovedAnnouncements() {
        return dao.getAnnouncementsByApprovedIsFalse();
    }

    public List<Announcement> getUnapprovedGeneral() {
        return dao.getAnnouncementsByTypeIdAndApprovedIsFalse(EAnnouncementType.GENERAL.getValue());
    }

    public List<Announcement> getUnapprovedFragrances() {
        return dao.getAnnouncementsByTypeIdAndApprovedIsFalse(EAnnouncementType.FRAGRANCE.getValue());
    }

    @Override
    public Announcement save(Announcement announcement) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean sendNotification = announcement.getId() == null;
        boolean shouldApprove = announcement.getApproved() != null && announcement.getApproved();
        boolean shouldPublish = announcement.isPublished();

        //if submitted by overseer, admin, or editorial it will be automatically approved and no notification will be sent
        final String access = authenticatedUser.getAccess();
        if (announcement.getRequesterId().equals(authenticatedUser.getId()) && (
                EUserAccess.OVERSEER.is(access) || EUserAccess.ADMIN.is(access)
                        || (authenticatedUser.getUserApplicationRoles() != null
                        && authenticatedUser.getUserApplicationRoles().contains(EApplicationRole.EDITORIAL.getValue()))
        )) {
            shouldApprove = true;
            sendNotification = false;
        }

        Announcement savedAnnouncement = dao.save(announcement);

        // handle approval, if previously approved do not approve again
        if (savedAnnouncement.getId() != null && shouldApprove) {
            boolean alreadyApproved = dao.existsAnnouncementByIdAndApprovedIsTrue(savedAnnouncement.getId());
            if (!alreadyApproved) {
                savedAnnouncement = approveAnnouncement(savedAnnouncement.getId());
            }
        }

        // handle publish, if previously published should not publish again
        if (savedAnnouncement.getId() != null && shouldPublish) {
            boolean alreadyPublished = dao.existsAnnouncementByIdAndEditorIdIsNotNull(savedAnnouncement.getId());
            if (!alreadyPublished) {
                savedAnnouncement = publishAnnouncement(savedAnnouncement.getId());
            }
        }

        //create notification that announcement is ready to be approved
        if (sendNotification) saveAnnouncementAddedNotification(savedAnnouncement);

        return savedAnnouncement;
    }


    public Announcement approveAnnouncement(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Announcement> announcementOptional = dao.findById(id);

        if (!announcementOptional.isPresent())
            throw new NotFoundException("Cannot approve an announcement that does not exist");

        Announcement announcement = announcementOptional.get();

        //For security only certain roles can actually do approvals
        final String access = authenticatedUser.getAccess();
        if (EUserAccess.OVERSEER.is(access) || EUserAccess.ADMIN.is(access)) {
            dao.setAnnouncementApproved(id, authenticatedUser.getId(), authenticatedUser.getDisplayName());
            announcement.setApproved(true);
            announcement.setApproverId(authenticatedUser.getId());
            announcement.setApproverName(authenticatedUser.getDisplayName());
            saveAnnouncementApprovedNotification(announcement);
        } else {
            throw new ForbiddenException("That operation is not allowed");
        }

        return announcement;
    }

    public void denyAnnouncement(Integer id, String feedback) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Announcement> announcementOptional = dao.findById(id);

        if (!announcementOptional.isPresent())
            throw new NotFoundException("Cannot deny an announcement that does not exist");

        Announcement announcement = announcementOptional.get();

        //For security only certain roles can actually do approvals
        final String access = authenticatedUser.getAccess();
        if (EUserAccess.OVERSEER.is(access) || EUserAccess.ADMIN.is(access)) {
            dao.setAnnouncementUnapproved(id, authenticatedUser.getId(), authenticatedUser.getDisplayName(), feedback);
            announcement.setApproved(false);
            announcement.setApproverId(authenticatedUser.getId());
            announcement.setApproverName(authenticatedUser.getDisplayName());
        } else {
            throw new ForbiddenException("That operation is not allowed");
        }
    }

    public Announcement publishAnnouncement(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Announcement> announcementOptional = dao.findById(id);

        if (!announcementOptional.isPresent())
            throw new NotFoundException("Cannot publish an announcement that does not exist");

        Announcement announcement = announcementOptional.get();

        //For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
                || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {

            if (announcement.getApproved() == null || !announcement.getApproved()) {
                approveAnnouncement(id);
            }

            String publishedDate = announcement.getNotificationDateAndTime() != null
                    ? LocalDateTime.parse(announcement.getNotificationDateAndTime(), DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ISO_DATE)
                    : LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);

            dao.setAnnouncementPublished(id, authenticatedUser.getId(), authenticatedUser.getDisplayName(), publishedDate);
            announcement.setEditorId(authenticatedUser.getId());
            announcement.setEditorName(authenticatedUser.getDisplayName());
            announcement.setPublishedDate(publishedDate);
            saveAnnouncementPublishedNotification(announcement);
        } else {
            throw new ForbiddenException("Cannot perform the operation");
        }

        return announcement;
    }

    public void unPublishAnnouncement(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Announcement> announcementOptional = dao.findById(id);

        if (!announcementOptional.isPresent())
            throw new NotFoundException("Cannot un-publish an announcement that does not exist");

        Announcement announcement = announcementOptional.get();

        //For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
                || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {
            dao.setAnnouncementUnpublished(id);
            announcement.setEditorId(null);
            announcement.setEditorName(null);
            announcement.setPublishedDate(null);
        }
    }

    private List<Announcement> filterAnnouncementsByPublishedOrPendingApprovalOrPublishing(List<Announcement> announcements, AuthenticatedUser authenticatedUser) {
        return announcements.stream().filter(announcement ->
                (announcement.getApproved() != null
                        && announcement.getApproved()
                        && announcement.isPublished() && (announcement.getNotificationDateAndTime() == null || LocalDateTime.parse(announcement.getNotificationDateAndTime()).isBefore(LocalDateTime.now())))
                        || ((announcement.getApproved() == null || announcement.getPublishedDate() == null)
                        && announcement.getRequesterId() != null
                        && announcement.getRequesterId().equals(authenticatedUser.getId()))).collect(Collectors.toList());
    }

    public void saveAnnouncementAddedNotification(Announcement announcement) {

        String titleMessage = "";
        String subTitleMessage = "";
        String contentMessage = "";
        String notificationSubSource = "";
        String notificationSource = Notification.ACTIVITIES_SOURCE;

        Optional<AnnouncementType> typeOptional = announcementTypeDao.findById(announcement.getTypeId());
        if (typeOptional.isPresent()) {
            AnnouncementType type = typeOptional.get();
            if (type.getTitle().toLowerCase().contains("general")) {
                titleMessage = "announcement.general.added.title";
                subTitleMessage = "announcement.general.added.subtitle";
                contentMessage = "announcement.general.added.content";
                notificationSubSource = GENERAL_ANNOUNCEMENT_ADDED_SUB_SOURCE;
            } else if (type.getTitle().toLowerCase().contains("fragrance")) {
                titleMessage = "announcement.fragrance.added.title";
                subTitleMessage = "announcement.fragrance.added.subtitle";
                contentMessage = "announcement.fragrance.added.content";
                notificationSubSource = FRAGRANCE_ANNOUNCEMENT_ADDED_SUB_SOURCE;
            }
        }

        notificationService.saveSystemCreatedNotification(
                announcement.getId(),
                announcement.getRequesterId(),
                null,
                announcement.getChurchId(),
                announcement.getGroupId(),
                announcement.getTeamId(),
                announcement.getRequesterName(),
                announcement.getChurchName(),
                announcement.getDate(),
                notificationSource,
                notificationSubSource,
                titleMessage,
                subTitleMessage,
                contentMessage,
                announcement.getNotificationDateAndTime()
        );

    }

    public void saveAnnouncementApprovedNotification(Announcement announcement) {

        String titleMessage = "";
        String subTitleMessage = "";
        String contentMessage = "";
        String notificationSubSource = "";

        String notificationSource = Notification.ACTIVITIES_SOURCE;

        // notification for editors to review the announcement
        if (announcement.getType() != null) {
            if (announcement.getType().getTitle().toLowerCase().contains("general")) {
                titleMessage = "announcement.general.approved.title";
                subTitleMessage = "announcement.general.approved.subtitle";
                contentMessage = "announcement.general.approved.content";
                notificationSubSource = GENERAL_ANNOUNCEMENT_APPROVED_SUB_SOURCE;
            } else if (announcement.getType().getTitle().toLowerCase().contains("fragrance")) {
                titleMessage = "announcement.fragrance.approved.title";
                subTitleMessage = "announcement.fragrance.approved.subtitle";
                contentMessage = "announcement.fragrance.approved.content";
                notificationSubSource = FRAGRANCE_ANNOUNCEMENT_APPROVED_SUB_SOURCE;
            }

            notificationService.saveSystemCreatedNotification(
                    announcement.getId(),
                    announcement.getRequesterId(),
                    null,
                    announcement.getChurchId(),
                    announcement.getGroupId(),
                    announcement.getTeamId(),
                    announcement.getRequesterName(),
                    announcement.getChurchName(),
                    announcement.getDate(),
                    notificationSource,
                    notificationSubSource,
                    titleMessage,
                    subTitleMessage,
                    contentMessage,
                    announcement.getNotificationDateAndTime()
            );
        }

    }

    public void saveAnnouncementPublishedNotification(Announcement announcement) {
        String churchTitleMessage = "";
        String churchSubTitleMessage = "";
        String churchContentMessage = "";
        String churchNotificationSubSource = "";
        String notificationSource = Notification.ACTIVITIES_SOURCE;


        // notification for all church members that there is a new post,
        if (announcement.getType() != null) {
            if (announcement.getType().getTitle().toLowerCase().contains("general")) {
                churchTitleMessage = "announcement.general.new.title";
                churchSubTitleMessage = "announcement.general.new.subtitle";
                churchContentMessage = "announcement.general.new.content";
                churchNotificationSubSource = GENERAL_ANNOUNCEMENT_NEW_SUB_SOURCE;

            } else if (announcement.getType().getTitle().toLowerCase().contains("fragrance")) {
                churchTitleMessage = "announcement.fragrance.new.title";
                churchSubTitleMessage = "announcement.fragrance.new.subtitle";
                churchContentMessage = "announcement.fragrance.new.content";
                churchNotificationSubSource = FRAGRANCE_ANNOUNCEMENT_NEW_SUB_SOURCE;
            }

            notificationService.saveSystemCreatedNotification(
                    announcement.getId(),
                    announcement.getRequesterId(),
                    null,
                    announcement.getChurchId(),
                    announcement.getGroupId(),
                    announcement.getTeamId(),
                    announcement.getRequesterName(),
                    announcement.getChurchName(),
                    announcement.getDate(),
                    notificationSource,
                    churchNotificationSubSource,
                    churchTitleMessage,
                    churchSubTitleMessage,
                    churchContentMessage,
                    announcement.getNotificationDateAndTime()
            );
        }

    }


    public void sendAnnouncementApprovalNeededPushNotification(Notification notification) {
        Optional<Announcement> announcementOptional = dao.findById(notification.getObjectId());

        if (announcementOptional.isPresent()) {
            Announcement announcement = announcementOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{announcement.getSubTitle()};
            String[] contentParams = new String[]{announcement.getTitle(), notification.getUserName()};
            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("announcementId", announcement.getId().toString());
            additionalData.put("status", "ApprovalNeeded");
            additionalData.put("type", getNotificationType(announcement));

            Integer parentChurchId = notification.getParentChurchId();
            if (parentChurchId == null) parentChurchId = notification.getChurchId();

            // send notification to approvers
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

    public void sendAnnouncementApprovedPushNotification(Notification notification) {
        Optional<Announcement> announcementOptional = dao.findById(notification.getObjectId());

        if (announcementOptional.isPresent()) {
            Announcement announcement = announcementOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{announcement.getSubTitle()};
            String[] contentParams = new String[]{announcement.getTitle(), notification.getUserName()};
            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("announcementId", announcement.getId().toString());
            additionalData.put("status", "Approved");
            additionalData.put("type", getNotificationType(announcement));

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

    public void sendAnnouncementNewPushNotification(Notification notification) {
        Optional<Announcement> announcementOptional = dao.findById(notification.getObjectId());

        if (announcementOptional.isPresent()) {
            Announcement announcement = announcementOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{announcement.getSubTitle()};
            String[] contentParams = new String[]{announcement.getTitle(), notification.getUserName()};
            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("announcementId", announcement.getId().toString());
            additionalData.put("status", "Published");
            additionalData.put("type", getNotificationType(announcement));

            // send notification to the east coast
            List<Filter> filters = notificationService.createEastCoastFilters();
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

    private String getNotificationType(Announcement announcement) {
        if (announcement.getTypeId().equals(EAnnouncementType.FRAGRANCE.getValue())) {
            return "Fragrance";
        } else if (announcement.getTypeId().equals(EAnnouncementType.HELP_VIDEO.getValue())) {
            return "HelpVideo";
        }
        return "Announcement";
    }
}
