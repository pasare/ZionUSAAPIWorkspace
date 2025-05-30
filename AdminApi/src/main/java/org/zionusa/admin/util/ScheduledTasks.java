package org.zionusa.admin.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zionusa.admin.dao.NotificationDao;
import org.zionusa.admin.domain.Notification;
import org.zionusa.admin.service.*;
import org.zionusa.admin.service.activities.ActivityLogService;
import org.zionusa.admin.service.activities.ActivityService;
import org.zionusa.base.enums.EZoneId;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import static org.zionusa.admin.domain.Announcement.*;
import static org.zionusa.admin.domain.Notification.*;

@Component
public class ScheduledTasks {

    public static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final ActivityService activityService;
    private final AnnouncementService announcementService;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;
    private final ChallengeService challengeService;
    private final NotificationDao notificationDao;
    private final GoalLogService goalLogService;
    private final MothersTeachingService mothersTeachingService;
    @Value("${app.environment}")
    private String environment;

    @Autowired
    ScheduledTasks(ActivityService activityService, AnnouncementService announcementService, NotificationService notificationService, ActivityLogService activityLogService, ChallengeService challengeService, NotificationDao notificationDao, GoalLogService goalLogService, MothersTeachingService mothersTeachingService) {
        this.activityService = activityService;
        this.announcementService = announcementService;
        this.notificationService = notificationService;
        this.activityLogService = activityLogService;
        this.challengeService = challengeService;
        this.notificationDao = notificationDao;
        this.goalLogService = goalLogService;
        this.mothersTeachingService = mothersTeachingService;
    }

    @Scheduled(cron = "${cron.report.expire}")
    public void ExpireReportsCache() {
        log.debug("Expiring Reports Caches");
        this.activityLogService.clearCaches();

    }

    @Scheduled(cron = "${cron.notifications}")
    public void checkForNotificationsToSend() throws UnknownHostException {
        // prevent notifications sending multiple times when people are running the production version of the app locally
        if (ensureNotSendingFromProductionUsingLocalhost()) {
            log.debug("Checking for notifications that need to be sent {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

            List<Notification> pendingNotifications = notificationDao.getAllByProcessedFalse();

            if (!pendingNotifications.isEmpty()) {

                int notificationCount = 0;
                for (Notification notification : pendingNotifications) {

                    //default in case time was never specified
                    boolean shouldSendNotification = true;


                    if (notification.getNotificationDateTime() != null) {
                        try {
                            LocalDateTime notificationSendDateTime = LocalDateTime.parse(notification.getNotificationDateTime());
                            log.info("The notification {} send date and time should be  {}", notification.getObjectId(), notificationSendDateTime);
                            log.info("The current date and time is {}", LocalDateTime.now());

                            shouldSendNotification = notificationSendDateTime.isBefore(LocalDateTime.now());

                            log.info("Should we send this notification? {}", shouldSendNotification);
                        } catch (DateTimeParseException e) {
                            log.info("invalid date could not be parsed, {} for id {}", notification.getNotificationDateTime(), notification.getId());
                        }
                    }


                    if (ACTIVITIES_SOURCE.equalsIgnoreCase(notification.getSource())) {

                        //sub source tells us who needs to receive the message
                        switch (notification.getSubSource()) {
                            case GENERAL_ANNOUNCEMENT_ADDED_SUB_SOURCE:
                            case FRAGRANCE_ANNOUNCEMENT_ADDED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    announcementService.sendAnnouncementApprovalNeededPushNotification(notification);
                                break;
                            case GENERAL_ANNOUNCEMENT_APPROVED_SUB_SOURCE:
                            case FRAGRANCE_ANNOUNCEMENT_APPROVED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    announcementService.sendAnnouncementApprovedPushNotification(notification);
                                break;
                            case GENERAL_ANNOUNCEMENT_NEW_SUB_SOURCE:
                            case FRAGRANCE_ANNOUNCEMENT_NEW_SUB_SOURCE:
                                if (shouldSendNotification)
                                    announcementService.sendAnnouncementNewPushNotification(notification);
                                break;
                            case ACTIVITY_ADDED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    activityService.sendActivityApprovalNeededPushNotification(notification);
                                break;
                            case ACTIVITY_APPROVED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    activityService.sendActivityApprovedPushNotification(notification);
                                break;
                            case ACTIVITY_NEW_SUB_SOURCE:
                                //activityService.sendActivityNewPushNotification(notification);
                                break;
                            case ACTIVITY_PUBLISHED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    activityService.sendActivityPublishedPushNotification(notification);
                            case CHALLENGE_ADDED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    challengeService.sendChallengeApprovalNeededPushNotification(notification);
                                break;
                            case CHALLENGE_APPROVED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    challengeService.sendChallengeApprovedPushNotification(notification);
                                break;
                            case CHALLENGE_NEW_SUB_SOURCE:
                                //challengeService.sendChallengeNewPushNotification(notification);
                                break;
                            case GROUP_GOAL_ADDED_SUB_SOURCE:
                                if (shouldSendNotification)
                                    goalLogService.sendGroupGoalNewPushNotification(notification);
                        }

                    }

                    if (BIBLE_STUDY_SOURCE.equalsIgnoreCase(notification.getSource())) {
                        notificationService.prepareBibleStudyPushNotification(notification);
                    }

                    if (PREACHING_SOURCE.equalsIgnoreCase(notification.getSource())) {
                        notificationService.preparePreachingPushNotification(notification);
                    }

                    if (EVENTS_SOURCE.equalsIgnoreCase(notification.getSource())) {
                        notificationService.prepareEventPushNotification(notification);
                    }

                    if (shouldSendNotification) {
                        notificationCount++;
                        // mark as processed for both dev and production. But dev will not send any notifications unless forced
                        Date processedDate = Date.from(LocalDateTime.now(EZoneId.NEW_YORK.getValue()).toInstant(ZoneOffset.MIN));
                        notification.setProcessed(true);
                        notification.setProcessedDate(processedDate);
                        notificationDao.save(notification);
                    }

                }
                log.debug("{} Notifications processed at this time {}", notificationCount, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            } else {
                log.debug("No notifications to send at this time {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            }
        }
    }

    @Scheduled(cron = "0 00 5 * * *")
    public void mothersTeachingNotifications() {
//        if (environment.equalsIgnoreCase("production")) {
//            log.debug("Temporarily not scheduling Mother's Teaching notification {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
//        } else {
//            log.debug("Scheduling Mother's Teaching notification {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
//            Random generator = new Random();
//
//            List<MothersTeaching> mothersTeachingList = mothersTeachingService.getAll();
//
//            MothersTeaching teaching = mothersTeachingList.get(generator.nextInt(mothersTeachingList.size()));
//            mothersTeachingService.sendMothersTeachingNotification(teaching);
//        }
    }

    @Scheduled(cron = "0 10 5 ? * SUN-FRI")
    public void afternoonPrayerNotification() {
//        if (environment.equalsIgnoreCase("production")) {
//            log.debug("Temporarily not scheduling afternoon prayer notifications {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
//        } else {
//            log.debug("Scheduling afternoon prayer notification {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
//            activityService.sendPrayerTimeNotification("2:25PM", true);
//        }
    }

    @Scheduled(cron = "0 05 5 ? * SUN-FRI")
    public void morningPrayerNotification() {
//        if (environment.equalsIgnoreCase("production")) {
//            log.debug("Temporarily not scheduling morning prayer notifications {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
//        } else {
//            log.debug("Scheduling morning prayer notification {}", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
//            activityService.sendPrayerTimeNotification("9:55AM", true);
//        }
    }

    private boolean ensureNotSendingFromProductionUsingLocalhost() throws UnknownHostException {
        return environment != null
                && (environment.equalsIgnoreCase("test") || environment.equalsIgnoreCase("production")
                && InetAddress.getLocalHost().getHostAddress().equalsIgnoreCase("10.0.0.4"));

    }
}
