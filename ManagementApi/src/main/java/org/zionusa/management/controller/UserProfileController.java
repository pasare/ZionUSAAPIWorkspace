package org.zionusa.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.UserProfileCategory;
import org.zionusa.management.domain.userprofile.UserProfile;
import org.zionusa.management.domain.userprofile.UserProfileSearchView;
import org.zionusa.management.service.UserProfileService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping()
    public List<UserProfileSearchView> getProfiles() {
        return userProfileService.getAll();
    }

    @GetMapping("/branch/{branchId}")
    public List<UserProfileSearchView> findProfileUsersByBranchId(@PathVariable Integer branchId) {
        return userProfileService.findProfileUsersByBranchId(branchId);
    }

    @Deprecated
    @GetMapping("/church/{churchId}")
    public List<UserProfileSearchView> findProfileUsersByChurchId(@PathVariable Integer churchId) {
        return userProfileService.findProfileUsersByBranchId(churchId);
    }

    @GetMapping("/group/{groupId}")
    public List<UserProfileSearchView> findUserProfilesByGroupId(@PathVariable Integer groupId) {
        return userProfileService.findUserProfilesByGroupId(groupId);
    }

    @GetMapping("/team/{teamId}")
    public List<UserProfileSearchView> findUserProfilesByTeamId(@PathVariable Integer teamId) {
        return userProfileService.findUserProfilesByTeamId(teamId);
    }

    @PatchMapping("/user/{userId}")
    public UserProfile patchById(@PathVariable Integer userId, @RequestBody Map<String, Object> fields) throws NotFoundException {
        return userProfileService.patchById(userId, fields);
    }

    @PostMapping()
    public UserProfile saveProfile(@RequestBody UserProfile profile) {
        return userProfileService.save(profile);
    }

    @GetMapping(value = "/user/{userId}")
    public User.ProfileUser getProfileByUserId(@PathVariable Integer userId) {
        return userProfileService.getByUserId(userId);
    }

    @GetMapping(value = "/categories")
    public List<UserProfileCategory> getCategories() {
        return userProfileService.getCategories();
    }

}
