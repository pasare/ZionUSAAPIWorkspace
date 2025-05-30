package org.zionusa.management.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.userprofile.UserProfile;
import org.zionusa.management.domain.userprofile.UserProfileSearchView;
import org.zionusa.management.service.MigrationService;
import org.zionusa.management.service.UserPermissionService;
import org.zionusa.management.service.UserProfileService;
import org.zionusa.management.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, Integer> {

    private final UserService userService;
    private final UserPermissionService userPermissionService;
    private final UserProfileService userProfileService;
    private final MigrationService migrationService;

    @Autowired
    public UserController(UserService userService,
                          UserPermissionService userPermissionService,
                          UserProfileService userProfileService,
                          MigrationService migrationService) {
        super(userService);
        this.userService = userService;
        this.userPermissionService = userPermissionService;
        this.userProfileService = userProfileService;
        this.migrationService = migrationService;
    }

    @GetMapping()
    @Override
    public List<User> getAll(Boolean archived) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get list of user that the person is authorized to see
        final String access = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(access) || EUserAccess.OVERSEER.is(access) || EUserAccess.CHURCH.is(access)) {
            return userService.getAll(archived);
        } else if (EUserAccess.TEAM.is(access)) {
            return userService.getByTeamId(authenticatedUser.getTeamId());
        }
        return new ArrayList<>();
    }

    @GetMapping(value = "/accessToken")
    public String getUserAccessToken() {
        return userPermissionService.getUserAccessToken();
    }

    @GetMapping(value = "/count/active")
    public Integer countAllActive() {
        return userService.countAllActive();
    }

    @GetMapping(value = "/display")
    public List<User.DisplayUser> getAllDisplayUsers(@RequestParam(value = "associationId", required = false) Integer associationId) {
        return userService.getDisplayUsers(associationId);
    }

    @GetMapping(value = "/display/active")
    public List<User.DisplayUser> getAllActiveDisplayUsers(@RequestParam(value = "associationId", required = false) Integer associationId) {
        return userService.getActiveDisplayUsers(associationId);
    }

    @GetMapping(value = "/application-roles/{applicationRoleId}")
    public List<User.ApplicationRole> getAllByApplicationRole(@PathVariable Integer applicationRoleId) {
        return userService.getByApplicationRoleContains(applicationRoleId);
    }

    @PostMapping(value = "/migrations/user-application-roles")
    public void migrateApplicationRoles() {
        migrationService.adminTriggerMigrateUserApplicationRoles();
    }

    @PostMapping(value = "/{userId}/application-roles")
    public User saveUserApplicationRoles(@PathVariable Integer userId, @RequestBody List<UserApplicationRole> userApplicationRoles) {
        return userService.saveUserApplicationRoles(userId, userApplicationRoles);
    }

    @GetMapping(value = "/{id}/display")
    public User.DisplayUser getDisplayByUserId(@PathVariable Integer id) {
        return userService.getDisplayByUserId(id);
    }

    @PostMapping(value = "/display/{id}")
    public Map<String, Object> getDisplayByUserIdWithColumns(@PathVariable Integer id, @RequestBody List<String> columns) {
        return userService.getDisplayByUserIdWithColumns(columns, id);
    }

    @GetMapping(value = "/access/{id}")
    public List<User.DisplayUser> getAllByAccess(@PathVariable Integer id) {
        return userService.getByAccessId(id);
    }

    @GetMapping(value = "/role/{id}")
    public List<User.DisplayUser> getAllByRole(@PathVariable Integer id) {
        return userService.getByRoleId(id);
    }

    @GetMapping(value = "/profiles")
    public List<UserProfileSearchView> getUsersWithProfiles() {
        return userProfileService.getAll();
    }

    @PatchMapping(value = "/profiles/{userId}")
    public UserProfile patchProfileById(@PathVariable Integer userId, @RequestBody Map<String, Object> fields) throws NotFoundException {
        return userProfileService.patchById(userId, fields);
    }

    @PostMapping(value = "/profiles")
    public List<Map<String, Object>> getDisplayUserProfiles(@RequestParam(value = "associationId", required = false) Integer associationId,
                                                            @RequestParam(value = "churchId", required = false) Integer churchId,
                                                            @RequestParam(value = "groupId", required = false) Integer groupId,
                                                            @RequestParam(value = "teamId", required = false) Integer teamId,
                                                            @RequestBody List<String> columns) {
        return userProfileService.getAllDisplay(columns, associationId, churchId, groupId, teamId);
    }

    @GetMapping(value = "/gender/{gender}")
    public List<User> getUsersByGender(@PathVariable String gender) {
        return userService.getByGender(gender);
    }

    @GetMapping(value = "/parent-church/{id}")
    public List<User.DisplayUser> getByParentChurchId(@PathVariable Integer id) {
        return userService.getByParentChurchId(id);
    }

    @GetMapping(value = "/church/{id}")
    public List<User.DisplayUser> getByChurchId(@PathVariable Integer id) {
        return userService.getByChurchId(id);
    }

    @GetMapping(value = "/church/{id}/display")
    public List<User.DisplayUser> getDisplayByChurchId(@PathVariable Integer id) {
        return userService.getDisplayByChurchId(id);
    }

    @GetMapping(value = "/church/{id}/{accessId}")
    public List<User.DisplayUser> getByChurchIdAndAccessId(@PathVariable Integer id, @PathVariable Integer accessId) {
        return userService.getByChurchIdAndAccessId(id, accessId);
    }

    @GetMapping(value = "/churches/{ids}")
    public List<User.DisplayUser> getByChurchIds(@PathVariable String ids) {
        return userService.getByChurchIds(ids);
    }

    @GetMapping(value = "/churches/{ids}/{accessId}")
    public List<User.DisplayUser> getByChurchIdsAndAccessId(@PathVariable String ids, @PathVariable Integer accessId) {
        return userService.getByChurchIdsAndAccessId(ids, accessId);
    }

    @GetMapping(value = "/group/{id}")
    public List<User.DisplayUser> getByGroupId(@PathVariable Integer id) {
        return userService.getByGroupId(id);
    }

    @GetMapping(value = "/group/{id}/display")
    public List<User.DisplayUser> getDisplayByGroupId(@PathVariable Integer id) {
        return userService.getDisplayByGroupId(id);
    }

    @GetMapping(value = "/groups/{ids}")
    public List<User.DisplayUser> getByGroupIds(@PathVariable String ids) {
        return userService.getByGroupIds(ids);
    }

    @GetMapping(value = "/team/{id}")
    public List<User> getByTeamId(@PathVariable Integer id) {
        return userService.getByTeamId(id);
    }

    @GetMapping(value = "/team/{id}/display")
    public List<User.DisplayUser> getDisplayByTeamId(@PathVariable Integer id) {
        return userService.getDisplayByTeamId(id);
    }

    @GetMapping(value = "/teams/{ids}")
    public List<User> getByTeamIds(@PathVariable String ids) {
        return userService.getByTeamIds(ids);
    }

    @GetMapping(value = "/token")
    @ApiOperation(value = "Get user permissions token")
    public String getUserToken() {
        return userService.getUserToken();
    }

    // Use for user profiles
    @GetMapping(value = "/profiles/{id}")
    public User.ProfileUser getUserWithProfileById(@PathVariable Integer id) {
        return userProfileService.getByUserId(id);
    }

    // Use for user profiles
    @PostMapping(value = "/profiles/{id}")
    public User.ProfileUser postUsersWithProfileById(@RequestBody User user) {
        return userService.saveProfile(user);
    }

    @GetMapping(value = "/profiles/branch/{branchId}")
    public List<UserProfileSearchView> findProfileUsersByBranchId(@PathVariable Integer branchId) {
        return userProfileService.findProfileUsersByBranchId(branchId);
    }

    @Deprecated
    @GetMapping(value = "/profiles/church/{churchId}")
    public List<UserProfileSearchView> findProfileUsersByChurchId(@PathVariable Integer churchId) {
        return userProfileService.findProfileUsersByBranchId(churchId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}/archive")
    public void archiveUser(@PathVariable Integer id) {
        userService.archive(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}/hide")
    public void hideUser(@PathVariable Integer id) {
        userService.hide(id);
    }

    @PostMapping(value = "/checkusername")
    public Boolean isUsernameAvailable(@RequestBody String username) {
        return userService.isUsernameAvailable(username);
    }

    @PostMapping(value = "/check-ad-user")
    public Boolean checkActiveDirectoryUser(@RequestBody String username) {
        return userService.checkActiveDirectoryUser(username);
    }

    @PutMapping(value = "/{id}/activate")
    public User reactivateUser(@PathVariable Integer id) {
        return userService.reactivateUser(id);
    }

    @PutMapping(value = "/{id}/enable")
    public User enableUser(@PathVariable Integer id) {
        return userService.enableUser(id);
    }

    @PutMapping(value = "/{id}/gospel-worker")
    public User updateGospelWorkerStatus(@PathVariable Integer id, @RequestParam("status") Boolean status) {
        return userService.updateGospelWorkerStatus(id, status);
    }

    @GetMapping(value = "/deactivated")
    public List<User> getDeactivatedUsers() {
        return userService.getDeactivatedUsers();
    }

    @GetMapping(value = "/disabled")
    public List<User> getDisabledUsers() {
        return userService.getDisabledUsers();
    }

    @GetMapping(value = "/expire")
    public void expireCache() {
        userService.expireCache();
    }

    @GetMapping(value = "/events/{requesterId}")
    public EventManagementTeam getEventManagementTeam(@PathVariable Integer requesterId) {
        return userService.getEventManagementTeam(requesterId);
    }

    @GetMapping(value = "/events/church/{churchId}")
    public EventManagementTeam getEventManagementTeamByChurch(@PathVariable Integer churchId) {
        return userService.getEventManagementTeamByChurch(churchId);
    }

    @GetMapping(value = "/events/church")
    public List<EventManagementTeam> getAllEventManagementTeams() {
        return userService.getAllEventManagementTeams();
    }

    @PostMapping(value = "/password-reset")
    public UserPasswordReset saveUserPasswordReset(@RequestBody UserPasswordReset userPasswordReset) throws IOException {
        return userService.saveUserPasswordReset(userPasswordReset);
    }

    @PutMapping(value = "/password-reset/{id}/approve")
    public UserPasswordReset approveUserPasswordReset(@PathVariable Integer id) {
        return userService.processUserPasswordReset(id, true);
    }

    @PutMapping(value = "/password-reset/{id}/deny")
    public UserPasswordReset rejectUserPasswordReset(@PathVariable Integer id) {
        return userService.processUserPasswordReset(id, false);
    }

    @GetMapping(value = "/registrations/password-reset/{churchId}")
    public Map<String, List> getUserRegistrationsAndResetsByChurchId(@PathVariable Integer churchId) {
        return userService.getUserRegistrationsAndPasswordResetsByChurchId(churchId);
    }

    @GetMapping(value = "/registrations/password-reset/total/{churchId}")
    public Integer getTotalUserRegistrationsAndResetsByChurchId(@PathVariable Integer churchId) {
        return userService.getTotalRegistrationsAndResetsByChurchId(churchId);
    }

    @GetMapping(value = "/registrations")
    public List<UserRegistration> getUserRegistrations() {
        return userService.getUserRegistrations();
    }

    @GetMapping(value = "/registrations/{churchId}")
    public List<UserRegistration> getUserRegistrationsByChurch(@PathVariable Integer churchId) {
        return userService.getUserRegistrationsByChurchId(churchId);
    }

    @PostMapping(value = "/registrations")
    public UserRegistration saveUserRegistration(@RequestBody UserRegistration userRegistration) {
        return userService.saveUserRegistration(userRegistration);
    }

    @PutMapping(value = "/registrations/{id}")
    public UserRegistration updateUserRegistration(@PathVariable Integer id, @RequestBody UserRegistration userRegistration) {
        return userService.saveUserRegistration(userRegistration);
    }

    @DeleteMapping(value = "/registrations/{id}")
    public void deleteUserRegistration(@PathVariable Integer id) {
        userService.deleteUserRegistration(id);
    }

    @PutMapping(value = "/registrations/{id}/approve")
    public UserRegistration approveUserRegistration(@PathVariable Integer id) {
        return userService.processUserRegistration(id, true);
    }

    @PutMapping(value = "/registrations/{id}/deny")
    public UserRegistration rejectUserRegistration(@PathVariable Integer id) {
        return userService.processUserRegistration(id, false);
    }

    @GetMapping(value = "/ga-graders")
    @ApiOperation(value = "View all the GA graders")
    public List<User.ApplicationRole> getAllGaGraders() {
        return userService.getAllGaGraders();
    }

    @GetMapping(value = "/ga-graders/main-church/{mainChurchId}")
    @ApiOperation(value = "View the GA graders per main church. This always returns all the overseers.")
    public List<User.ApplicationRole> getAllGaGradersByRoleIdOrMainChurchId(@PathVariable Integer mainChurchId) {
        return userService.getAllGaGradersByRoleIdOrMainChurchId(mainChurchId);
    }

    @GetMapping(value = "/teachers")
    public List<User.ApplicationRole> getAllTeachersDisplay(@RequestParam(value = "associationId", required = false) Integer associationId) {
        return userService.getAllTeachersDisplay(associationId);
    }

    @GetMapping(value = "/teachers/church/{churchId}")
    public List<User.ApplicationRole> getDisplayByTeacherAndChurchId(@PathVariable Integer churchId) {
        return userService.getDisplayByTeacherAndChurchId(churchId);
    }

    @GetMapping(value = "/ready-graders")
    public List<User.ApplicationRole> getAllReadyGradersDisplay() {
        return userService.getAllReadyGradersDisplay();
    }

    @GetMapping(value = "/ready-graders/main-church/{churchId}")
    public List<User.ApplicationRole> getDisplayByReadyGraderAndMainChurchId(@PathVariable Integer churchId) {
        return userService.getDisplayByReadyGraderAndMainChurchId(churchId);
    }

    @GetMapping(value = "/search")
    public List<Map<String, Object>> searchUsers(
        @RequestParam(value = "query") String query,
        @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return userService.searchUsers(query, limit);
    }
}
