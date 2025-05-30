package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.admin.domain.Announcement;
import org.zionusa.admin.service.AnnouncementService;
import org.zionusa.admin.service.AzureBlobStorageService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController extends BaseController<Announcement, Integer> {

    private final AnnouncementService service;
    private final AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public AnnouncementController(AnnouncementService service, AzureBlobStorageService azureBlobStorageService) {
        super(service);
        this.service = service;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @GetMapping("/general")
    public List<Announcement> getGeneralAnnouncements() {
        return service.getGeneralAnnouncements();
    }

    @GetMapping("/fragrances")
    public List<Announcement> getFragranceAnnouncements() {
        return service.getFragranceAnnouncements();
    }

    @GetMapping("/help-videos")
    public List<Announcement> getHelpVideos() { return service.getHelpVideos();
    }

    @GetMapping("/approved")
    public List<Announcement> getApprovedAnnouncements() {
        return service.getApprovedAndPublishedAnnouncements();
    }

    @GetMapping("/approved/general")
    public List<Announcement> getApprovedGeneralAnnouncements() {
        return service.getApprovedAndPublishedGeneralAnnouncements();
    }

    @GetMapping("/approved/fragrances")
    public List<Announcement> getApprovedFragranceAnnouncements() {
        return service.getApprovedAndPublishedFragranceAnnouncements();
    }

    @GetMapping("/pending")
    public List<Announcement> getPendingAnnouncements() {
        return service.getPendingAnnouncements();
    }

    @GetMapping("/pending/general")
    public List<Announcement> getPendingGeneral() {
        return service.getPendingGeneral();
    }

    @GetMapping("/pending/fragrances")
    public List<Announcement> getPendingFragrances() {
        return service.getPendingFragrances();
    }

    @GetMapping("/unapproved")
    public List<Announcement> getUnapprovedAnnouncements() {
        return this.service.getUnapprovedAnnouncements();
    }

    @GetMapping("/unapproved/fragrances")
    public List<Announcement> getUnapprovedFragrances() {
        return this.service.getUnapprovedFragrances();
    }

    @GetMapping("/unapproved/general")
    public List<Announcement> getUnapprovedGeneral() {
        return this.service.getUnapprovedGeneral();
    }

    @PostMapping( value = "/upload")
    public String upload(@RequestParam("userId") Integer userId,
                             @RequestParam("name") String name,
                             @RequestParam(value = "file") MultipartFile file) {

        return azureBlobStorageService.uploadImage(userId, file, name, "announcements");
    }

    @PutMapping("/{id}/approve")
    public Announcement approveAnnouncement(@PathVariable Integer id) {
        return service.approveAnnouncement(id);
    }

    @PutMapping("/{id}/deny")
    public void denyAnnouncement(@PathVariable Integer id, @RequestBody String feedback) {
        service.denyAnnouncement(id, feedback);
    }

    @PutMapping("/{id}/publish")
    public Announcement publishAnnouncement(@PathVariable Integer id) {
        return service.publishAnnouncement(id);
    }

    @PutMapping("/{id}/un-publish")
    public void unPublishAnnouncement(@PathVariable Integer id) {
        service.unPublishAnnouncement(id);
    }

}
