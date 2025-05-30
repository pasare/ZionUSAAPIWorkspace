package org.zionusa.admin.controller.activities;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.admin.domain.MemberRecentSearch;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.admin.service.AzureBlobStorageService;
import org.zionusa.admin.service.activities.ActivityService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/activities")
@Api(value = "Activities")
public class ActivityController extends BaseController<Activity, Integer> {

    private final AzureBlobStorageService azureBlobStorageService;
    private final ActivityService service;

    @Autowired
    public ActivityController(ActivityService service, AzureBlobStorageService azureBlobStorageService) {
        super(service);
        this.service = service;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("userId") Integer userId,
                         @RequestParam("name") String name,
                         @RequestParam(value = "file") MultipartFile file) {

        return azureBlobStorageService.uploadImage(userId, file, name, "activities");
    }

    @Override
    @GetMapping()
    public List<Activity> getAll(@RequestParam(value = "archived", required = false) Boolean archived) {
        return service.getActivities(archived);
    }

    @GetMapping("/member-search")
    public MemberRecentSearch getSearchActivities() {
        return service.getMemberSearchActivities();
    }

    @PutMapping("/archive")
    public void archiveActivity(Integer activityId) {
        service.archive(activityId);
    }

    @GetMapping("/approved")
    public List<Activity> getApprovedActivities() {
        return service.getApprovedActivities();
    }

    @GetMapping("/pending")
    public List<Activity> getPendingActivities() {
        return service.getPendingActivities();
    }

    @GetMapping("/unapproved")
    public List<Activity> getUnapprovedActivities() {
        return this.service.getUnapprovedActivities();
    }

    @PutMapping("/{id}/approve")
    public Activity approveActivity(@PathVariable Integer id) {
        return service.approveActivity(id);
    }

    @PutMapping("/{id}/deny")
    public void denyActivity(@PathVariable Integer id, @RequestBody String feedback) {
        service.denyActivity(id, feedback);
    }

    @PutMapping("/{id}/publish")
    public Activity publishActivity(@PathVariable Integer id) {
        return service.publishActivity(id);
    }

    @PutMapping("/{id}/un-publish")
    public void unPublishActivity(@PathVariable Integer id) {
        service.unPublishActivity(id);
    }

    @GetMapping("/categories/{id}")
    public List<Activity> getActivitiesByCategoryId(@PathVariable Integer id) {
        return service.getActivitiesByCategoryId(id);
    }

    @GetMapping("/categories")
    public List<ActivityCategory> getActivityCategories() {
        return service.getActivityCategories();
    }

    @PostMapping("/prayer-time")
    public void manualSendPrayerTimeNotification(@RequestBody String deliveryTimeOfDay) {
        service.manualSendPrayerTimeNotification(deliveryTimeOfDay);
    }
}
