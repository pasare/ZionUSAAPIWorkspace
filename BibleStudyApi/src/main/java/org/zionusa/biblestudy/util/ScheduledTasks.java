package org.zionusa.biblestudy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.biblestudy.dao.NotificationDao;
import org.zionusa.biblestudy.domain.Notification;
import org.zionusa.biblestudy.service.BibleStudyService;
import org.zionusa.biblestudy.service.SermonRecordingService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import static org.zionusa.biblestudy.domain.BibleStudy.*;
import static org.zionusa.biblestudy.domain.Notification.BIBLE_STUDY_SOURCE;
import static org.zionusa.biblestudy.domain.Notification.SERMON_RECORDING_SOURCE;
import static org.zionusa.biblestudy.domain.SermonRecording.*;

@Component
public class ScheduledTasks {

    public static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final NotificationDao notificationDao;
    private final BibleStudyService bibleStudyService;
    private final SermonRecordingService sermonRecordingService;

    @Value("${app.environment}")
    private String environment;

    @Autowired
    public ScheduledTasks(NotificationDao notificationDao, BibleStudyService bibleStudyService, SermonRecordingService sermonRecordingService) {
        this.notificationDao = notificationDao;
        this.bibleStudyService = bibleStudyService;
        this.sermonRecordingService = sermonRecordingService;
    }

    @Scheduled(cron = "${cron.notifications}")
    public void checkForNotificationsToSend() throws UnknownHostException {

        //prevent notifications ending multiple times when people are running the production version of the app locally
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
                            log.debug("The notification {} send date and time should be  {}", notification.getObjectId(), notificationSendDateTime);
                            log.debug("The current date and time is {}", LocalDateTime.now());

                            shouldSendNotification = notificationSendDateTime.isBefore(LocalDateTime.now());

                            log.debug("Should we send this notification? {}", shouldSendNotification);
                        } catch (DateTimeParseException e) {
                            log.info("invalid date could not be parsed, {} for id {}", notification.getNotificationDateTime(), notification.getId());
                        }
                    }

                    if (shouldSendNotification && BIBLE_STUDY_SOURCE.equalsIgnoreCase(notification.getSource())) {
                        switch (notification.getSubSource()) {

                            case BIBLE_STUDY_ADDED_SUB_SOURCE:
                                bibleStudyService.sendBibleStudyAddedPushNotification(notification);
                                break;
                            case BIBLE_STUDY_TEACHER_ASSIGNED_SUB_SOURCE:
                                if (notification.getTeacherId() != null)
                                    bibleStudyService.sendBibleStudyTeachingAssignedPushNotification(notification);
                                break;
                            case BIBLE_STUDY_APPROVED_SUB_SOURCE:
                                bibleStudyService.sendBibleStudyApprovedPushNotification(notification);
                                break;
                            case BIBLE_STUDY_TEACHER_AVAILABLE_SUB_SOURCE:
                                bibleStudyService.sendBibleStudyTeacherAvailabilityPushNotification(notification);
                                break;
                        }
                    } else if (shouldSendNotification && SERMON_RECORDING_SOURCE.equalsIgnoreCase(notification.getSource())) {
                        switch (notification.getSubSource()) {

                            case SERMON_UPLOADED_SUB_SOURCE:
                                sermonRecordingService.sendSermonUploadedNotification(notification);
                                break;
                            case SERMON_GRADED_SUB_SOURCE:
                                sermonRecordingService.sendSermonGradedPushNotification(notification);
                                break;
                            case SERMON_EXAMPLE_SUB_SOURCE:
                                sermonRecordingService.sendExampleSermonPushNotification(notification);
                                break;
                        }
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

    private boolean ensureNotSendingFromProductionUsingLocalhost() throws UnknownHostException {
        return environment != null
                && (environment.equalsIgnoreCase("test") || environment.toLowerCase().equals("production")
                && InetAddress.getLocalHost().getHostAddress().equalsIgnoreCase("10.0.0.4"));

    }

}
