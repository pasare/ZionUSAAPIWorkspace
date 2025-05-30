package org.zionusa.event.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.event.dao.EventManagementTeamViewDao;
import org.zionusa.event.dao.EventMediaDao;
import org.zionusa.event.dao.ReminderEventResultSurveyTableViewDao;
import org.zionusa.event.domain.EventManagementTeamView;
import org.zionusa.event.domain.EventMedia;
import org.zionusa.event.domain.Notification;
import org.zionusa.event.domain.eventCategory.EventCategory;
import org.zionusa.event.domain.eventCategory.EventCategoryDao;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.eventProposal.EventProposalsDao;
import org.zionusa.event.domain.eventRegistration.EventRegistration;
import org.zionusa.event.domain.eventRegistration.EventRegistrationsDao;
import org.zionusa.event.domain.eventRegistration.EventRegistrationsService;
import org.zionusa.event.domain.eventStatus.EventStatus;
import org.zionusa.event.domain.eventStatus.EventStatusDao;
import org.zionusa.event.domain.eventTeam.EventTeam;
import org.zionusa.event.domain.eventTeam.EventTeamMemberView;
import org.zionusa.event.domain.eventTeam.EventTeamMemberViewDao;
import org.zionusa.event.domain.eventTeam.EventTeamsDao;
import org.zionusa.event.domain.resultsSurvey.ReminderEventResultSurveyTableView;
import org.zionusa.event.domain.resultsSurvey.ResultsSurveyViewDao;
import org.zionusa.event.enums.ENotificationCategory;
import org.zionusa.event.enums.ENotificationMessageSourceType;
import org.zionusa.event.service.NotificationsService;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ScheduledTasks {

    public static final String CHECKING_IF_THERE_ARE_ANY_EVENT_PROPOSALS_THAT_NEED_ADMIN_APPROVAL_REMINDER_NOTIFICATIONS = "Checking if there are any event proposals that need admin approval reminder notifications {}";
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private final NotificationsService notificationsService;
    private final CacheManager cacheManager;
    private final EventStatusDao eventStatusDao;
    private final MessageSource messageSource;
    private final EventManagementTeamViewDao eventManagementTeamViewDao;
    private final EventTeamMemberViewDao eventTeamMemberViewDao;
    private final EventProposalsDao eventProposalsDao;
    private final ReminderEventResultSurveyTableViewDao reminderEventResultSurveyTableViewDao;
    private final EventRegistrationsDao eventRegistrationsDao;
    private final EventRegistrationsService eventRegistrationsService;
    private final ResultsSurveyViewDao resultsSurveyViewDao;
    private final EventMediaDao eventMediaDao;
    private final EventCategoryDao eventCategoryDao;

    @Value("${app.ui.url}")
    private String eventsUrl;
    @Value("${microsoft.o365.domain}")
    private String domain;
    @Value("${event.category.default}")
    private String eventCategoryDefault;
    @Value("${event.category.title1}")
    private String eventCategoryTitle1;
    @Value("${event.category.title2}")
    private String eventCategoryTitle2;

    @Autowired
    public ScheduledTasks(NotificationsService notificationsService, CacheManager cacheManager, EventStatusDao eventStatusDao, MessageSource messageSource, EventManagementTeamViewDao eventManagementTeamViewDao, EventTeamsDao eventTeamsDao, EventTeamMemberViewDao eventTeamMemberViewDao, EventProposalsDao eventProposalsDao, ReminderEventResultSurveyTableViewDao reminderEventResultSurveyTableViewDao, EventRegistrationsDao eventRegistrationsDao, EventRegistrationsService eventRegistrationsService, ResultsSurveyViewDao resultsSurveyViewDao, EventMediaDao eventMediaDao, EventCategoryDao eventCategoryDao) {
        this.notificationsService = notificationsService;
        this.cacheManager = cacheManager;
        this.eventStatusDao = eventStatusDao;
        this.messageSource = messageSource;
        this.eventManagementTeamViewDao = eventManagementTeamViewDao;
        this.eventTeamMemberViewDao = eventTeamMemberViewDao;
        this.eventProposalsDao = eventProposalsDao;
        this.reminderEventResultSurveyTableViewDao = reminderEventResultSurveyTableViewDao;
        this.eventRegistrationsDao = eventRegistrationsDao;
        this.eventRegistrationsService = eventRegistrationsService;
        this.resultsSurveyViewDao = resultsSurveyViewDao;
        this.eventMediaDao = eventMediaDao;
        this.eventCategoryDao = eventCategoryDao;
    }

    // send the notifications for approvals
    @Scheduled(cron = "${cron.report.expire}")
    public void sendApprovalNotification() {
        logger.warn("Checking if there are any email notifications to send out {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        List<Notification> pendingNotifications = notificationsService.getPendingNotifications();

        int notificationCount = 0;

        for (Notification notification : pendingNotifications) {

            if (Notification.TYPE_EMAIL.equalsIgnoreCase(notification.getType())) {
                logger.warn("Sending Email Notifications: {}", notification);
                notificationsService.sendEmailNotification(notification);
            } else {
                notificationsService.sendPushNotification(notification);
            }
            // update the notification as processed
            notification.setProcessed(true);
            notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            notificationsService.save(notification);

            notificationCount++;
        }

        if (notificationCount > 0) {
            logger.warn("Sent notifications for {} events {}", notificationCount, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        } else {
            logger.warn("There were no notifications to send {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        }
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendUnfinishedEventProposalNotification() {
        logger.warn("Checking if there are any unfinished event proposals that need reminder notifications {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        List<EventStatus> eventStatuses = eventStatusDao.findAllByStartedTrueAndFinalizedNull();
        List<EventManagementTeamView> eventTeam = new ArrayList<>();

        logger.warn("Found {} event proposal(s) that have not been submitted yet", eventStatuses.size());

        int notificationCount = 0;
        if (!eventStatuses.isEmpty()) {

            String titleMessage = "notification.email.unfinished.reminder.title";
            String contentMessage = "notification.email.unfinished.reminder.message";

            for (EventStatus eventStatus : eventStatuses) {
                EventProposal eventProposal = eventProposalsDao.getEventProposalByIdAndArchived(eventStatus.getEventProposalId(), false);

                if (eventProposal != null && eventProposal.getArchived() != Boolean.TRUE && eventProposal.getRequesterEmail() != null) {

                    LocalDate currentDate = LocalDate.now();
                    LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate(), DateTimeFormatter.ISO_DATE);

                    if (eventDate.isAfter(currentDate)) {
                        logger.warn("Sending unfinished notification for event {}  submitted by {},  scheduled for {}.", eventProposal.getTitle(), eventProposal.getRequesterName(), eventProposal.getProposedDate());

                        eventTeam = getEventTeam(eventProposal, eventProposal.getEventCategory(), eventTeam);

                        StringBuilder recipientString = new StringBuilder();
                        String recipient = new String();
                        boolean isRequesterActive = false;
                        String[] contentParams = new String[]{eventProposal.getTitle(), LocalDate.parse(eventStatus.getStartedDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), LocalDate.parse(eventProposal.getProposedDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()};
                        isRequesterActive = isRequesterActive(eventTeam, recipientString, false, eventProposal.getRequesterEmail());
                        if (isRequesterActive) {
                            recipient = eventProposal.getRequesterEmail();
                        }
                        if (recipientString.length() > 0) {
                            recipient = recipientString.substring(0, recipientString.length() - 2);
                        }

                        Notification notification = new Notification();
                        notification.setEventProposalId(eventStatus.getEventProposalId());
                        notification.setCategory(Notification.EVENT_PROPOSAL);
                        notification.setSubCategory("unfinished-reminder");
                        notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                        notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                        notification.setType("text");
                        notification.setRecipients(recipient);

                        notificationsService.sendEmailNotification(notification);
                        notification.setProcessed(true);
                        notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                        notificationsService.save(notification);
                        notificationCount++;
                    }
                }
            }
        }
        logger.warn("Finished sending {} reminder notifications", notificationCount);

    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendDefaultAdminApprovalReminder() {
        logger.warn(CHECKING_IF_THERE_ARE_ANY_EVENT_PROPOSALS_THAT_NEED_ADMIN_APPROVAL_REMINDER_NOTIFICATIONS, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        List<EventStatus> eventStatuses = eventStatusDao.findAllByManagerApprovedTrueAndAdminApprovedNull();

        logger.warn("Found {} event proposal(s) that are awaiting admin approval", eventStatuses.size());
        int notificationCount = 0;
        if (!eventStatuses.isEmpty()) {

            String titleMessage = ENotificationMessageSourceType.ADMIN_APPROVAL_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.ADMIN_APPROVAL_MESSAGE.getValue();

            for (EventStatus eventStatus : eventStatuses) {

                EventProposal eventProposal = eventProposalsDao.getEventProposalByIdAndArchived(eventStatus.getEventProposalId(), false);


                if (eventProposal != null && eventProposal.getArchived() != Boolean.TRUE && eventProposal.getEventCategory().getTitle().equalsIgnoreCase(eventCategoryDefault)) {
                    LocalDate currentDate = LocalDate.now();

                    LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate(), DateTimeFormatter.ISO_DATE);
                    if (eventDate.isAfter(currentDate)) {
                        List<EventManagementTeamView> eventAdministrators = eventManagementTeamViewDao.getEventManagementTeamViewByApplicationRoleName(EApplicationRole.EVENT_DEFAULT_ADMIN.getValue());
                        logger.warn("Sending notification for event {}  submitted by {},  scheduled for {}.", eventProposal.getTitle(), eventProposal.getRequesterName(), eventProposal.getProposedDate());
                        if (eventAdministrators.isEmpty()) {
                            throw new NullPointerException("No event Administrator found for this branch");
                        }
                        String[] contentParams = new String[]{eventProposal.getBranchName(), eventCategoryDefault, eventProposal.getTitle(), LocalDate.parse(eventProposal.getProposedDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()};

                        Notification notification = new Notification();
                        notification.setEventProposalId(eventStatus.getEventProposalId());
                        notification.setCategory(Notification.EVENT_PROPOSAL);
                        notification.setSubCategory(ENotificationCategory.ADMIN_APPROVAL_REMINDER.getValue());
                        notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                        notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                        notification.setType(Notification.TYPE_EMAIL);

                        StringBuilder recipientString = new StringBuilder();
                        for (EventManagementTeamView recipient : eventAdministrators) {
                            if (recipient != null && recipient.getUsername() != null) {
                                recipientString.append(recipient.getUsername()).append(", ");
                            }
                        }
                        notificationsService.sendEmailNotification(notification);
                        notification.setProcessed(true);
                        notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                        notificationsService.save(notification);
                        notificationCount++;
                    }
                }
            }
        }
        logger.warn("Finished sending {} reminder notifications", notificationCount);
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendCategory1AdminApprovalReminder() {
        logger.warn("Checking if there are any event proposals that need admin approval reminder notifications {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        List<EventStatus> eventStatuses = eventStatusDao.findAllByManagerApprovedTrueAndAdminApprovedNull();

        logger.warn("Found {} event proposal(s) that are awaiting {} admin approval", eventCategoryTitle1, eventStatuses.size());
        int notificationCount = 0;
        if (!eventStatuses.isEmpty()) {
            String titleMessage = ENotificationMessageSourceType.ADMIN_APPROVAL_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.ADMIN_APPROVAL_MESSAGE.getValue();
            for (EventStatus eventStatus : eventStatuses) {
                EventProposal eventProposal = eventProposalsDao.getEventProposalByIdAndArchived(eventStatus.getEventProposalId(), false);

                if (eventProposal != null && eventProposal.getArchived() != Boolean.TRUE && eventProposal.getEventCategory().getTitle().equalsIgnoreCase(eventCategoryTitle1)) {


                    LocalDate currentDate = LocalDate.now();
                    LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate(), DateTimeFormatter.ISO_DATE);
                    if (eventDate.isAfter(currentDate)) {
                        logger.warn("Sending {} admin reminder notification for event {}  submitted by {},  scheduled for {}.", eventCategoryTitle1, eventProposal.getTitle(), eventProposal.getRequesterName(), eventProposal.getProposedDate());
                        List<EventManagementTeamView> eventAdministrators = eventManagementTeamViewDao.getEventManagementTeamViewByApplicationRoleName(EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue());

                        if (eventAdministrators.isEmpty()) {
                            throw new NullPointerException(" No event Administrator found for this branch");
                        }

                        String[] contentParams = new String[]{eventProposal.getBranchName(), eventCategoryTitle1, eventProposal.getTitle(), LocalDate.parse(eventProposal.getProposedDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()};

                        Notification notification = new Notification();
                        notification.setEventProposalId(eventStatus.getEventProposalId());
                        notification.setCategory(Notification.EVENT_PROPOSAL);
                        notification.setSubCategory(ENotificationCategory.ADMIN_APPROVAL_REMINDER.getValue());
                        notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                        notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                        notification.setType(Notification.TYPE_EMAIL);

                        StringBuilder recipientString = new StringBuilder();
                        for (EventManagementTeamView recipient : eventAdministrators) {
                            if (recipient != null && recipient.getUsername() != null) {
                                recipientString.append(recipient.getUsername()).append(", ");
                            }
                        }
                        notificationsService.sendEmailNotification(notification);
                        notification.setProcessed(true);
                        notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                        notificationsService.save(notification);
                        notificationCount++;
                    }
                }
            }
        }
        logger.warn("Finished sending {} reminder notifications", notificationCount);
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendCategory2AdminApprovalReminder() {
        logger.warn(CHECKING_IF_THERE_ARE_ANY_EVENT_PROPOSALS_THAT_NEED_ADMIN_APPROVAL_REMINDER_NOTIFICATIONS, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        List<EventStatus> eventStatuses = eventStatusDao.findAllByManagerApprovedTrueAndAdminApprovedNull();

        logger.warn("Found {} event proposal(s) that are awaiting {} admin approval", eventCategoryTitle2, eventStatuses.size());
        int notificationCount = 0;
        if (!eventStatuses.isEmpty()) {
            String titleMessage = ENotificationMessageSourceType.ADMIN_APPROVAL_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.ADMIN_APPROVAL_MESSAGE.getValue();

            for (EventStatus eventStatus : eventStatuses) {
                EventProposal eventProposal = eventProposalsDao.getEventProposalByIdAndArchived(eventStatus.getEventProposalId(), false);
                if (eventProposal != null && eventProposal.getArchived() != Boolean.TRUE && eventProposal.getEventCategory().getTitle().equalsIgnoreCase(eventCategoryTitle2)) {

                    LocalDate currentDate = LocalDate.now();

                    LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate(), DateTimeFormatter.ISO_DATE);
                    if (eventDate.isAfter(currentDate)) {
                        logger.warn("Sending {} Admin notification for event {}  submitted by {},  scheduled for {}.", eventCategoryTitle2, eventProposal.getTitle(), eventProposal.getRequesterName(), eventProposal.getProposedDate());

                        List<EventManagementTeamView> eventAdministrators = eventManagementTeamViewDao.getEventManagementTeamViewByApplicationRoleName(EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue());

                        if (eventAdministrators.isEmpty()) {
                            throw new NullPointerException(" No event Administrator found for this branch");
                        }
                        String[] contentParams = new String[]{eventProposal.getBranchName(), eventProposal.getTitle(), LocalDate.parse(eventProposal.getProposedDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()};

                        Notification notification = new Notification();
                        notification.setEventProposalId(eventStatus.getEventProposalId());
                        notification.setCategory(Notification.EVENT_PROPOSAL);
                        notification.setSubCategory(ENotificationCategory.ADMIN_APPROVAL_REMINDER.getValue());
                        notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                        notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                        notification.setType(Notification.TYPE_EMAIL);

                        StringBuilder recipientString = new StringBuilder();
                        for (EventManagementTeamView recipient : eventAdministrators) {
                            if (recipient != null && recipient.getUsername() != null) {
                                recipientString.append(recipient.getUsername()).append(", ");
                            }
                        }
                        notificationsService.sendEmailNotification(notification);
                        notification.setProcessed(true);
                        notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                        notificationsService.save(notification);
                        notificationCount++;
                    }
                }
            }
        }
        logger.warn("Finished sending {} reminder notifications", notificationCount);
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendResultSurveySubmissionNotification() {
        List<ReminderEventResultSurveyTableView> reminderEventResultSurveyTableView = reminderEventResultSurveyTableViewDao.findAll();
        reminderEventResultSurveyTableView.forEach(survey -> {
            String titleMessage = ENotificationMessageSourceType.RESULTS_SURVEY_REMINDER_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.RESULTS_SURVEY_REMINDER_MESSAGE.getValue();
            String recipient = "";

            LocalDate currentDate = LocalDate.now();
            LocalDate currentDateMinus60Days = currentDate.minusDays(60);
            LocalDate eventDate = LocalDate.parse(survey.getProposedEndDate(), DateTimeFormatter.ISO_DATE);

//            Send result survey notifications for events that happened no later than 60 days and before today.
            if (eventDate.isAfter(currentDateMinus60Days) && eventDate.isBefore(currentDate)) {
                if (survey.getId() != null) {
                    Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(survey.getId());
                    if (eventProposalOptional.isPresent()) {
                        EventProposal eventProposal = eventProposalOptional.get();
                        if (eventProposal.getEventCategoryId() != null) {
                            Optional<EventCategory> optionalEventCategory = eventCategoryDao.findById(eventProposal.getEventCategoryId());
                            recipient = getRecipient(survey, recipient, eventProposal, optionalEventCategory);

                            if (recipient == null || recipient.equals("")) {
                                recipient = survey.getRequesterEmail();
                            }
                        }
                        String[] contentParams = new String[]{survey.getEventTitle().toUpperCase(), LocalDate.parse(survey.getProposedEndDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + survey.getId()};

                        logger.warn("Sending notification for event {}  submitted by {},  scheduled for {}.", survey.getEventTitle(), survey.getRequesterName(), survey.getProposedEndDate());

                        Notification notification = new Notification();
                        notification.setEventProposalId(survey.getId());
                        notification.setCategory(Notification.EVENT_PROPOSAL);
                        notification.setSubCategory(ENotificationCategory.RESULTS_SURVEY_REMINDER.getValue());
                        notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                        notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                        notification.setRecipients(recipient);
                        notification.setType("text");

                        notificationsService.sendEmailNotification(notification);
                        notification.setProcessed(true);
                        notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                        notificationsService.save(notification);
                    }
                }
            }
        });
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendResultPhotoUploadNotification() {
        List<ReminderEventResultSurveyTableView> reminderEventResultSurveyTableView = reminderEventResultSurveyTableViewDao.findAll();
        reminderEventResultSurveyTableView.forEach(survey -> {
            String titleMessage = ENotificationMessageSourceType.PHOTO_SUBMISSION_REMINDER_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.PHOTO_SUBMISSION_REMINDER_MESSAGE.getValue();
            String recipient = "";

            LocalDate currentDate = LocalDate.now();
            LocalDate currentDateMinus60Days = currentDate.minusDays(60);
            LocalDate eventDate = LocalDate.parse(survey.getProposedEndDate(), DateTimeFormatter.ISO_DATE);

//            Send result survey notifications for events that happened no later than 60 days and before today.
            if (eventDate.isAfter(currentDateMinus60Days) && eventDate.isBefore(currentDate)) {
                List<EventMedia> eventMediaList = eventMediaDao.findAllByResultsSurveyIdAndArchived(survey.getId(), false);

                AtomicBoolean photoPosted = new AtomicBoolean();

                eventMediaList.forEach(eventMedia -> {
                    if (eventMedia.getType().contains("VIP_PHOTOS")) {
                        photoPosted.set(true);
                    }
                });
                if (!photoPosted.get()) {
                    if (survey.getId() != null) {
                        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(survey.getId());
                        if (eventProposalOptional.isPresent()) {
                            EventProposal eventProposal = eventProposalOptional.get();
                            if (eventProposal.getEventCategoryId() != null) {
                                Optional<EventCategory> optionalEventCategory = eventCategoryDao.findById(eventProposal.getEventCategoryId());
                                recipient = getPhotoRecipient(survey, recipient, eventProposal, optionalEventCategory);
                                if (recipient == null || recipient.equals("")) {
                                    recipient = survey.getRequesterEmail();
                                }
                            }
                            String[] contentParams = new String[]{survey.getEventTitle().toUpperCase(), LocalDate.parse(survey.getProposedEndDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + survey.getId()};

                            logger.warn("Sending Photo notification for event {}  submitted by {},  scheduled for {}.", survey.getEventTitle(), survey.getRequesterName(), survey.getProposedEndDate());

                            Notification notification = new Notification();
                            notification.setEventProposalId(survey.getId());
                            notification.setCategory(Notification.EVENT_PROPOSAL);
                            notification.setSubCategory(ENotificationCategory.PHOTO_SUBMISSION_REMINDER.getValue());
                            notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                            notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                            notification.setRecipients(recipient);
                            notification.setType("text");

                            notificationsService.sendEmailNotification(notification);
                            notification.setProcessed(true);
                            notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                            notificationsService.save(notification);
                        }
                    }
                }
            }
        });

    }

    private String getRecipient(ReminderEventResultSurveyTableView survey, String recipient, EventProposal eventProposal, Optional<EventCategory> optionalEventCategory) {
        if (optionalEventCategory.isPresent()) {
            EventCategory eventCategory = optionalEventCategory.get();
            List<EventManagementTeamView> eventTeam = new ArrayList<>();
            StringBuilder recipientString = new StringBuilder();
            eventTeam = getEventTeam(eventProposal, eventCategory, eventTeam);
            boolean isRequesterActive = false;
            isRequesterActive = isRequesterActive(eventTeam, recipientString, false, survey.getRequesterEmail());
            if (!isRequesterActive) {
                if (recipientString.length() > 0) {
                    recipient = recipientString.substring(0, recipientString.length() - 2);
                }
            } else {
                recipient = survey.getRequesterEmail();
            }
        }
        return recipient;
    }

    private String getPhotoRecipient(ReminderEventResultSurveyTableView survey, String recipient, EventProposal eventProposal, Optional<EventCategory> optionalEventCategory) {
        if (optionalEventCategory.isPresent()) {
            EventCategory eventCategory = optionalEventCategory.get();
            List<EventTeamMemberView> eventTeam = new ArrayList<>();
            List<EventTeam> eventTeam2 = new ArrayList<>();
            StringBuilder recipientString = new StringBuilder();
            eventTeam = getPhotoEventTeam(eventProposal, eventCategory, eventTeam);
            eventTeam.forEach(member ->
                recipientString.append(member.getUsername()).append(", "));
            if (recipientString.length() > 0) {
                recipient = recipientString.substring(0, recipientString.length() - 2);
            } else
                recipient = survey.getRequesterEmail();
        }
        return recipient;
    }

    private String getVideoRecipient(ReminderEventResultSurveyTableView survey, String recipient, EventProposal eventProposal, Optional<EventCategory> optionalEventCategory) {
        if (optionalEventCategory.isPresent()) {
            EventCategory eventCategory = optionalEventCategory.get();
            List<EventTeamMemberView> eventTeam = new ArrayList<>();
            List<EventTeam> eventTeam2 = new ArrayList<>();
            StringBuilder recipientString = new StringBuilder();
            eventTeam = getPhotoEventTeam(eventProposal, eventCategory, eventTeam);
            eventTeam.forEach(member ->
                recipientString.append(member.getUsername()).append(", "));
            if (recipientString.length() > 0) {
                recipient = recipientString.substring(0, recipientString.length() - 2);
            } else
                recipient = survey.getRequesterEmail();
        }
        return recipient;
    }

    private List<EventManagementTeamView> getEventTeam(EventProposal eventProposal, EventCategory eventCategory, List<EventManagementTeamView> eventTeam) {
        if (eventCategory.getSubCategories().contains("DEFAULT")) {
            eventTeam = eventManagementTeamViewDao.getEventManagementTeamViewByBranchIdAndApplicationRoleNameContaining(eventProposal.getBranchId(), "EVENT_DEFAULT_");
        } else if (eventCategory.getSubCategories().contains(("OPTION_1"))) {
            eventTeam = eventManagementTeamViewDao.getEventManagementTeamViewByBranchIdAndApplicationRoleNameContaining(eventProposal.getBranchId(), "EVENT_ASEZ_IUBA_");
        } else if (eventCategory.getSubCategories().contains(("OPTION_2"))) {
            eventTeam = eventManagementTeamViewDao.getEventManagementTeamViewByBranchIdAndApplicationRoleNameContaining(eventProposal.getBranchId(), "EVENT_ASEZ_WAO_IWBA_");
        }
        return eventTeam;
    }

    private List<EventTeamMemberView> getPhotoEventTeam(EventProposal eventProposal, EventCategory eventCategory, List<EventTeamMemberView> eventTeamMemberViews) {
        if (eventCategory.getSubCategories().contains("DEFAULT")) {
            eventTeamMemberViews = eventTeamMemberViewDao.getAllEventTeamMemberViewByApplicationRoleName("EVENT_DEFAULT_PHOTOGRAPHER_ADMIN");
            eventTeamMemberViews.addAll(eventTeamMemberViewDao.getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(eventProposal.getBranchId(), "EVENT_DEFAULT_PHOTOGRAPHER"));
            eventTeamMemberViews.addAll(eventTeamMemberViewDao.getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(eventProposal.getBranchId(), "EVENT_DEFAULT_REPRESENTATIVE"));
        } else if (eventCategory.getSubCategories().contains(("OPTION_1"))) {
            eventTeamMemberViews = eventTeamMemberViewDao.getAllEventTeamMemberViewByApplicationRoleName("EVENT_ASEZ_IUBA_PHOTOGRAPHER_ADMIN");
            eventTeamMemberViews.addAll(eventTeamMemberViewDao.getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(eventProposal.getBranchId(), "EVENT_ASEZ_IUBA_PHOTOGRAPHER"));
            eventTeamMemberViews.addAll(eventTeamMemberViewDao.getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(eventProposal.getBranchId(), "EVENT_ASEZ_IUBA_REPRESENTATIVE"));
        } else if (eventCategory.getSubCategories().contains(("OPTION_2"))) {
            eventTeamMemberViews = eventTeamMemberViewDao.getAllEventTeamMemberViewByApplicationRoleName("EVENT_ASEZ_WAO_IWBA_PHOTOGRAPHER_ADMIN");
            eventTeamMemberViews.addAll(eventTeamMemberViewDao.getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(eventProposal.getBranchId(), "EVENT_ASEZ_WAO_PHOTOGRAPHER"));
            eventTeamMemberViews.addAll(eventTeamMemberViewDao.getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(eventProposal.getBranchId(), "EVENT_ASEZ_WAO_REPRESENTATIVE"));
        }
        return eventTeamMemberViews;
    }

    private boolean isRequesterActive(List<EventManagementTeamView> eventTeam, StringBuilder recipientString, boolean isRequesterActive, String requesterEmail) {
        for (EventManagementTeamView member : eventTeam) {
            if (member.getUsername().equalsIgnoreCase(requesterEmail)) {
                isRequesterActive = true;
            }
            if (member.getApplicationRoleName().contains("REPRESENTATIVE") ||
                member.getApplicationRoleName().contains("MANAGER") ||
                member.getApplicationRoleName().contains("EVENT_DEFAULT_ADMIN") ||
                member.getApplicationRoleName().contains("EVENT_ASEZ_WAO_IWBA_ADMIN") ||
                member.getApplicationRoleName().contains("EVENT_ASEZ_IUBA_ADMIN")
            ) {
                recipientString.append(member.getUsername()).append(", ");
            }
        }
        return isRequesterActive;
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendMonthPostEventUpdate() {
        List<EventProposal> updateEventProposalList = eventProposalsDao.getAllByArchived(false);
        updateEventProposalList.forEach(event -> {
            String titleMessage = ENotificationMessageSourceType.MONTH_POST_EVENT_REMINDER_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.MONTH_POST_EVENT_REMINDER_MESSAGE.getValue();

            LocalDate currentDate = LocalDate.now();
            LocalDate currentDateMinus30Days = currentDate.minusDays(28);

            if (event.getProposedEndDate() == null || Objects.equals(event.getProposedEndDate(), "")) {
                event.setProposedEndDate(event.getProposedDate());
            }

            LocalDate eventDate = LocalDate.parse(event.getProposedEndDate(), DateTimeFormatter.ISO_DATE);

//            Send post event update notifications for events that took place a month ago
            if (eventDate.isEqual(currentDateMinus30Days)) {
                if (event.getRequesterEmail() != null) {

                    String[] contentParams = new String[]{event.getTitle().toUpperCase(), LocalDate.parse(event.getProposedEndDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), eventsUrl + "/admin/event-proposals/view/" + event.getId()};

                    Notification notification = new Notification();
                    notification.setEventProposalId(event.getId());
                    notification.setCategory(Notification.EVENT_PROPOSAL);
                    notification.setSubCategory(ENotificationCategory.POST_EVENT_UPDATE_REMINDER.getValue());
                    notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
                    notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
                    notification.setRecipients(event.getRequesterEmail());
                    notification.setType("text");

                    notificationsService.sendEmailNotification(notification);
                    notification.setProcessed(true);
                    notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                    notificationsService.save(notification);
                }
            }
        });
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendGAApprovalReminder() throws ParseException {
        logger.warn("Checking if there are any event proposals that need GA approval reminder notifications {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        List<EventStatus> eventStatuses = eventStatusDao.findAllByAdminApprovedTrueAndGaApprovedNull();

        ArrayList<String> eventList = new ArrayList<String>();
        eventList.add("<ul>");

        logger.warn("Found {} event proposal(s) that are awaiting GA approval", eventStatuses.size());

        int notificationCount = 0;
        if (!eventStatuses.isEmpty()) {
            String titleMessage = ENotificationMessageSourceType.GA_APPROVAL_REMINDER_TITLE.getValue();
            String contentMessage = ENotificationMessageSourceType.GA_APPROVAL_REMINDER_MESSAGE.getValue();

            for (EventStatus eventStatus : eventStatuses) {
                EventProposal eventProposal = eventProposalsDao.getEventProposalByIdAndArchived(eventStatus.getEventProposalId(), false);

                if (eventProposal != null && eventProposal.getArchived() != Boolean.TRUE) {

                    if (eventProposal.getId() != null && eventProposal.getTitle() != null && eventProposal.getBranchName() != null) {
                        String url = eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId();

                        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate);
                        String eventForNotification = "<li>  Branch: " + eventProposal.getBranchName() + "   Event: <a href=" + url + ">" + eventProposal.getTitle() + "</a>   Date: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate) + ". </li>";
                        eventList.add(eventForNotification);
                        notificationCount++;
                    }
                }
            }

            List<EventManagementTeamView> gaApprovers;

            gaApprovers = eventManagementTeamViewDao.getEventManagementTeamViewByApplicationRoleName(EApplicationRole.EVENT_DEFAULT_GA.getValue());
            logApprovers(EApplicationRole.EVENT_DEFAULT_GA, gaApprovers);

            if (gaApprovers.isEmpty()) {
                throw new NullPointerException(" No event GA Approvers found for this branch");
            }
            eventList.add("</ul>");
            String eventString = eventList.toString().replace(",", "");
            eventString = eventString.substring(1, eventString.length() - 1);
            String[] contentParams = new String[]{String.valueOf(notificationCount), eventString};

            Notification notification = new Notification();
            notification.setEventProposalId(0);
            notification.setCategory(Notification.EVENT_PROPOSAL);
            notification.setSubCategory(ENotificationCategory.ADMIN_APPROVAL_REMINDER.getValue());
            notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
            notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
            notification.setType(Notification.TYPE_EMAIL);

            StringBuilder recipientString = new StringBuilder();
            for (EventManagementTeamView recipient : gaApprovers) {
                if (recipient != null && recipient.getUsername() != null) {
                    recipientString.append(recipient.getUsername()).append(", ");
                }
            }
            if (recipientString.length() > 0) {
                notification.setRecipients(recipientString.substring(0, recipientString.length() - 2));
            } else {
                throw new IllegalStateException("A notification cannot be created without a recipient list");
            }

            notificationsService.sendEmailNotification(notification);
            notification.setProcessed(true);
            notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            logger.warn("Saving Notification {}: ", notification);
            notificationsService.save(notification);
        }
        logger.warn("Finished sending {} GA reminder notifications", notificationCount);
    }

    @Scheduled(cron = "${cron.daily.notification}")
    public void sendGuestsEventReminder() {
        String titleMessage = ENotificationMessageSourceType.GUESTS_EVENT_REMINDER_TITLE.getValue();
        String contentMessage = ENotificationMessageSourceType.GUESTS_EVENT_REMINDER_MESSAGE.getValue();

        LocalDate tmrDate = LocalDate.now();
        List<EventProposal> tomorrowsEvents = eventProposalsDao.getEventProposalByProposedDateAndWorkflowStatusAndArchivedFalse(tmrDate.toString(), "Admin Approved");

        for (EventProposal proposal : tomorrowsEvents) {
            List<EventRegistration> registrations = eventRegistrationsDao.getEventRegistrationsByEventProposalId(proposal.getId());
            StringBuilder recipientString = new StringBuilder();
            for (EventRegistration recipients : registrations) {
                recipientString.append(recipients.getParticipantEmail()).append(", ");
            }
            String[] contentParams = new String[]{proposal.getTitle(), LocalDate.parse(proposal.getProposedEndDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), proposal.getProposedTime(), proposal.getProposedEndTime(), proposal.getLocation().getLocationName() + ", " + proposal.getLocation().getAddress() + ", " + proposal.getLocation().getCity() + ", " + proposal.getLocation().getStateProvince() + " " + proposal.getLocation().getZipPostalCode(), proposal.getCallToAction().replaceAll("<[^>]*>", ""),};

            Notification notification = new Notification();
            notification.setEventProposalId(proposal.getId());
            notification.setCategory(Notification.EVENT_PROPOSAL);
            notification.setSubCategory(ENotificationCategory.GUEST_EVENT_REMINDER.getValue());
            notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
            notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
            notification.setType("text");
            if (recipientString.length() > 0) {
                notification.setRecipients(recipientString.substring(0, recipientString.length() - 2));
            } else {
                throw new IllegalStateException("A notification cannot be created without a recipient list");
            }
            notificationsService.sendEmailNotification(notification);
            notification.setProcessed(true);
            notification.setProcessTime(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            notificationsService.save(notification);
        }
    }

    //    @Scheduled(cron = "${cron.daily.notification}")
    public void sendRegistrationToList() throws IOException {
        List<EventRegistration> eventRegistrantList = eventRegistrationsDao.findAll();
//        System.out.println("All event registrants: "+ eventRegistrantList);
        List<EventRegistration> emailList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (EventRegistration eventRegistration : eventRegistrantList) {
            if (currentDate.isBefore(LocalDate.parse((eventRegistration.getEventDate()))) &&
                (eventRegistration.getQrCode().isEmpty()
                    || eventRegistration.getQrCode() == null
                    || eventRegistration.getQrImageUrl().isEmpty()
                    || eventRegistration.getQrImageUrl() == null
                )) {
                eventRegistrationsService.createQrCode(eventRegistration);
                eventRegistrationsService.sendRegistrationEmail(eventRegistration);
            }
        }
        System.out.println("Email list: " + emailList);
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void clearAllCaches() {
        logger.warn("Begin Daily Clearing of all Caches {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        for (String cache : cacheManager.getCacheNames()) {
            logger.warn("{}", cache);
            Objects.requireNonNull(cacheManager.getCache(cache)).clear();
            logger.warn("Cache {} was cleared {}", cache, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        }
        logger.warn("End Daily Clearing of all Caches {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    private void logApprovers(EApplicationRole role, List<EventManagementTeamView> approvers) {
        logger.warn("{} Approvers {}: ", role.getValue(), approvers);
    }
}
