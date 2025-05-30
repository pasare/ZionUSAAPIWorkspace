package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.Notification;
import org.zionusa.biblestudy.service.NotificationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController extends BaseController<Notification, Integer> {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        super(notificationService);
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/parent-church/{churchId}")
    List<Notification> getAllNotificationsByParentChurch(@PathVariable Integer churchId) {
        return notificationService.getAllByParentChurch(churchId);
    }

    @GetMapping(value = "/church/{churchId}")
    List<Notification> getAllNotificationsByChurch(@PathVariable Integer churchId) {
        return notificationService.getAllByChurch(churchId);
    }

    @GetMapping(value = "/sermon-recordings")
    List<Notification> getAllSermonRecordingNotifications() {
        return notificationService.getBySource(Notification.SERMON_RECORDING_SOURCE);
    }

    @GetMapping(value = "/sermon-recordings/church/parent/{parentChurchId}")
    List<Notification> getSermonRecordingNotificationsByParentChurch(@PathVariable Integer parentChurchId) {
        return notificationService.getBySourceAndParentChurch(Notification.SERMON_RECORDING_SOURCE, parentChurchId);
    }


    @GetMapping(value = "/sermon-recordings/group/{groupId}")
    List<Notification> getSermonRecordingNotificationsByGroup(@PathVariable Integer groupId) {
        return notificationService.getBySourceAndGroup(Notification.SERMON_RECORDING_SOURCE, groupId);
    }

    @GetMapping(value = "/sermon-recordings/team/{teamId}")
    List<Notification> getSermonRecordingNotificationsByTeam(@PathVariable Integer teamId) {
        return notificationService.getBySourceAndTeam(Notification.SERMON_RECORDING_SOURCE, teamId);
    }

    @GetMapping(value = "/sermon-recordings/date/{startDate}/{endDate}")
    List<Notification> getSermonRecordingNotificationsByDate(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return notificationService.getBySourceAndDate(Notification.SERMON_RECORDING_SOURCE, startDate, endDate);
    }

    List<Notification> getSermonRecordingNotificationsByChurchAndDate() {
        return null;
    }

    @GetMapping(value = "/bible-studies")
    List<Notification> getAllBibleStudyNotifications() {
        return notificationService.getBySource(Notification.BIBLE_STUDY_SOURCE);
    }

    @GetMapping(value = "/bible-studies/group/{groupId}")
    List<Notification> getBibleStudyNotificationsByGroup(@PathVariable Integer groupId) {
        return notificationService.getBySourceAndGroup(Notification.BIBLE_STUDY_SOURCE, groupId);
    }

    @GetMapping(value = "/bible-studies/team/{teamId}")
    List<Notification> getBibleStudyNotificationsByTeam(@PathVariable Integer teamId) {
        return notificationService.getBySourceAndTeam(Notification.BIBLE_STUDY_SOURCE, teamId);
    }
}
