package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.ApplicationRole;
import org.zionusa.management.domain.ApplicationRolePermission;
import org.zionusa.management.domain.UserApplicationRole;
import org.zionusa.management.service.ApplicationRoleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/application-roles")
@Api(value = "User Application Roles")
public class ApplicationRoleController extends BaseController<ApplicationRole, Integer> {

    private final ApplicationRoleService applicationRoleService;

    @Autowired
    public ApplicationRoleController(ApplicationRoleService applicationRoleService) {
        super(applicationRoleService);
        this.applicationRoleService = applicationRoleService;
    }

    @DeleteMapping(value = "/{id}/user/{userId}")
    @ApiOperation(value = "Delete one application role from a user")
    public void deleteOneUserApplicationRole(@PathVariable Integer id, @PathVariable Integer userId) {
        applicationRoleService.deleteOneUserApplicationRole(id, userId);
    }

    @DeleteMapping(value = "/{id}/user/{userId}/events")
    @ApiOperation(value = "Delete one application role from an events user")
    public void deleteOneEventsUserApplicationRole(@PathVariable Integer id, @PathVariable Integer userId) {
        applicationRoleService.deleteOneEventsUserApplicationRole(id, userId);
    }

    @DeleteMapping(value = "/{id}/user/{userId}/study")
    @ApiOperation(value = "Delete one application role for studying")
    public void deleteOneStudyUserApplicationRole(@PathVariable Integer id, @PathVariable Integer userId) {
        applicationRoleService.deleteOneStudyUserApplicationRole(id, userId);
    }

    @PutMapping(value = "/{id}/user/{userId}")
    @ApiOperation(value = "Add one application role to a user")
    public void saveOneUserApplicationRole(@PathVariable Integer id, @PathVariable Integer userId) {
        applicationRoleService.saveOneUserApplicationRole(id, userId);
    }

    @PutMapping(value = "/{id}/user/{userId}/events")
    @ApiOperation(value = "Add one application role to an events user")
    public void saveOneEventsUserApplicationRole(@PathVariable Integer id, @PathVariable Integer userId) {
        applicationRoleService.saveOneEventsUserApplicationRole(id, userId);
    }

    @PutMapping(value = "/{id}/user/{userId}/study")
    @ApiOperation(value = "Add one application role for studying")
    public void saveOneStudyUserApplicationRole(@PathVariable Integer id, @PathVariable Integer userId) {
        applicationRoleService.saveOneStudyUserApplicationRole(id, userId);
    }

    @GetMapping(value = "/{id}/user-permissions")
    @ApiOperation(value = "Get all permissions for a user application role")
    public List<ApplicationRolePermission> getAllPermissionsByApplicationRoleId(@PathVariable Integer id) {
        return applicationRoleService.getAllPermissionsByApplicationRoleId(id);
    }

    @PostMapping(value = "/{id}/user-permissions")
    @ApiOperation(value = "Get all permissions for a user application role")
    public List<Map<String, Object>> getAllPermissionsByApplicationRoleIdDisplay(
        @PathVariable Integer id,
        @RequestBody List<String> columns) {
        return applicationRoleService.getAllPermissionsByApplicationRoleIdDisplay(id, columns);
    }

    @DeleteMapping(value = "/{id}/user-permission/{userPermissionId}")
    @ApiOperation(value = "Delete one permission from a user application role")
    public void deleteOneApplicationRolePermission(@PathVariable Integer id, @PathVariable Integer userPermissionId) {
        applicationRoleService.deleteOneApplicationRolePermission(id, userPermissionId);
    }

    @PutMapping(value = "/{id}/user-permission/{userPermissionId}")
    @ApiOperation(value = "Add one permission to a user application role")
    public void saveOneApplicationRolePermission(@PathVariable Integer id, @PathVariable Integer userPermissionId) {
        applicationRoleService.saveOneApplicationRolePermission(id, userPermissionId);
    }

    @GetMapping(value = "/disabled")
    @ApiOperation(value = "Read enabled application roles")
    public List<ApplicationRole> getDisabledApplicationRoles() {
        return applicationRoleService.getDisabledApplicationRoles();
    }

    @GetMapping(value = "/enabled/events")
    @ApiOperation(value = "Read events application roles")
    public List<ApplicationRole> getEventsApplicationRoles() {
        return applicationRoleService.getEventsApplicationRoles();
    }

    @GetMapping(value = "/user/{userId}")
    @ApiOperation(value = "Read application roles for a user")
    public List<UserApplicationRole> getUserApplicationRoles(@PathVariable Integer userId) {
        return applicationRoleService.getUserApplicationRoles(userId);
    }

    @PutMapping(value = "/user/{userId}")
    @ApiOperation(value = "Assign all application roles to a user")
    public List<UserApplicationRole> saveUserApplicationRoles(@PathVariable Integer userId, @RequestBody List<ApplicationRole> applicationRoles) {
        return applicationRoleService.updateUserApplicationRoles(userId, applicationRoles);
    }

}
