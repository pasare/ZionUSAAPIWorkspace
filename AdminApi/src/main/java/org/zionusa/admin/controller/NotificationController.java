package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Notification;
import org.zionusa.admin.service.NotificationService;
import org.zionusa.base.controller.BaseController;

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

    @GetMapping(value = "/preaching")
    List<Notification> getAllPreachingNotifications() {
        return notificationService.getBySource(Notification.PREACHING_SOURCE);
    }

    @GetMapping(value = "/events")
    List<Notification> getAllEventsNotifications() {
        return notificationService.getBySource(Notification.EVENTS_SOURCE);
    }

    @GetMapping(value = "/bible-studies")
    List<Notification> getAllBibleStudiesNotifications() {
        return notificationService.getBySource(Notification.BIBLE_STUDY_SOURCE);
    }

    @GetMapping(value = "/activities")
    List<Notification> getAllActivitiesNotifications() {
        return notificationService.getBySource(Notification.ACTIVITIES_SOURCE);
    }

}
