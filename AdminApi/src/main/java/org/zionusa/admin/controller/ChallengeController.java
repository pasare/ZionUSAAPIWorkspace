package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.admin.domain.Challenge;
import org.zionusa.admin.domain.ChallengeCategory;
import org.zionusa.admin.service.AzureBlobStorageService;
import org.zionusa.admin.service.ChallengeService;
import org.zionusa.base.controller.BaseController;

import java.util.List;


@RestController
@RequestMapping("/challenges")
public class ChallengeController extends BaseController<Challenge, Integer> {

    private final ChallengeService service;
    private final AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public ChallengeController(ChallengeService service, AzureBlobStorageService azureBlobStorageService) {
        super(service);
        this.service = service;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("userId") Integer userId,
                         @RequestParam("name") String name,
                         @RequestParam(value = "file") MultipartFile file) {

        return azureBlobStorageService.uploadImage(userId, file, name, "challenges");
    }

    @Override
    @GetMapping()
    public List<Challenge> getAll(@RequestParam(value = "archived", required = false) Boolean archived) {
        return service.getChallenges(archived);
    }

    @PutMapping("/archive")
    public void archiveChallenge(Integer challengeId) {
        service.archive(challengeId);
    }

    @GetMapping("/approved")
    public List<Challenge> getApprovedChallenges() {
        return service.getApprovedChallenges();
    }

    @GetMapping("/approved/{churchId}")
    public List<Challenge> getApprovedChallengesByChurchId(@PathVariable Integer churchId) {
        return service.getApprovedChallengesByChurchId(churchId);
    }

    @GetMapping("/pending")
    public List<Challenge> getPendingChallenges() {
        return service.getPendingChallenges();
    }

    @GetMapping("/unapproved")
    public List<Challenge> getUnapprovedChallenges() {
        return this.service.getUnapprovedChallenges();
    }

    @PutMapping("/{id}/approve")
    public Challenge approveChallenge(@PathVariable Integer id) {
        return service.approveChallenge(id);
    }

    @PutMapping("/{id}/deny")
    public void denyChallenge(@PathVariable Integer id, @RequestBody String feedback) {
        service.denyChallenge(id, feedback);
    }

    @GetMapping("/categories/{id}")
    public List<Challenge> getChallengesByCategoryId(@PathVariable Integer id) {
        return service.getChallengesByCategoryId(id);
    }

    @GetMapping("/categories")
    public List<ChallengeCategory> getChallengeCategories() {
        return service.getChallengeCategories();
    }

    @GetMapping("/movements/{id}")
    public List<Challenge> getChallengesByMovementId(@PathVariable Integer id) {
        return service.getChallengesByMovementId(id);
    }

}
