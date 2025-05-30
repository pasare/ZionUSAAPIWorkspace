package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.User;
import org.zionusa.management.service.ChurchService;
import org.zionusa.management.service.GroupService;

import java.util.List;
import java.util.Map;

@Deprecated
@RestController
@RequestMapping("/churches")
@Api(value = "Churches")
public class ChurchController {

    private final ChurchService churchService;
    private final GroupService groupService;

    @Autowired
    public ChurchController(ChurchService churchService, GroupService groupService) {
        this.churchService = churchService;
        this.groupService = groupService;
    }

    @GetMapping()
    @ApiOperation(value = "View a list of churches, full hierarchy", response = List.class)
    public List<Church> getAll(@RequestParam(value = "loadFull", required = false) Boolean loadFull) {
        return churchService.getAll(loadFull);
    }

    @GetMapping(value = "/display")
    public List<Church.DisplayChurch> getAllDisplayChurches(
        @RequestParam(value = "associationId", required = false) Integer associationId
    ) {
        return churchService.getDisplayChurches(associationId);
    }

    @PostMapping(value = "/display")
    public List<Map<String, Object>> getAllDisplayChurches(@RequestBody List<String> columns) {
        return churchService.getAllDisplay(columns, false);
    }

    @GetMapping(value = "/information")
    @ApiOperation(value = "View a list of churches without any group or user information", response = List.class)
    public List<Church> getChurchInformationList() {
        return churchService.getAllChurchInformation();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "View a church, full hierarchy", response = Church.class)
    public Church getById(@PathVariable Integer id) {
        return churchService.getById(id);
    }

    @GetMapping(value = "/{id}/display")
    @ApiOperation(value = "View a church, with minimal group information", response = Church.class)
    public Church.DisplayChurch getChurchDisplay(@PathVariable Integer id) {
        return churchService.getDisplayById(id);
    }

    @GetMapping(value = "/{id}/information")
    @ApiOperation(value = "View a church, without any group or user information", response = Church.class)
    public Church getChurchInformation(@PathVariable Integer id) {
        return churchService.getInformationById(id);
    }

    @GetMapping(value = "parent-church/{id}")
    @ApiOperation(value = "View a list of churches who's parent matches the provided id", response = Church.class)
    public List<Church> getByParentChurch(@PathVariable Integer id) {
        return churchService.getByParentChurchId(id);
    }

    @GetMapping(value = "/{id}/branches")
    @ApiOperation(value = "View a list of churches which are a branch of the provided id", response = List.class)
    public List<Church> getBranchChurches(@PathVariable Integer id) {
        return churchService.getBranchChurches(id);
    }

    @GetMapping(value = "/{id}/church-group")
    @ApiOperation(value = "View the primary group of a church, every church should have exactly one", response = Group.class)
    public Group getChurchGroup(@PathVariable Integer id) {
        return groupService.getChurchGroup(id);
    }

    @GetMapping(value = "/{id}/groups")
    @ApiOperation(value = "View the groups of a church", response = List.class)
    public List<Group> getGroups(@PathVariable Integer id) {
        return churchService.getGroups(id);
    }

    @GetMapping(value = "/{id}/groups/display")
    @ApiOperation(value = "View a minimal dataset of the groups of a church", response = List.class)
    public List<Group.DisplayGroup> getDisplayGroups(@PathVariable Integer id) {
        return churchService.getDisplayGroups(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}/groups/clean")
    @ApiOperation(value = "Clean up any orphaned groups that may still be in the system")
    public void cleanGroups(@PathVariable Integer id) {
        churchService.cleanUpGroups(id);
    }

    @GetMapping(value = "/{id}/leaders")
    @ApiOperation(value = "View a list of church leaders which belong to the provided church id", response = List.class)
    public List<User> getChurchLeaders(@PathVariable Integer id) {
        return churchService.getLeaders(id);
    }

    @GetMapping(value = "/{id}/members")
    @ApiOperation(value = "View a list of members who belong to the provided church id", response = List.class)
    public List<User> getMembers(@PathVariable Integer id) {
        return churchService.getMembers(id);
    }

    @GetMapping(value = "/{id}/members/{gender}")
    @ApiOperation(value = "View a list of members who belong to the provided church id, filtered by gender", response = List.class)
    public List<User> getMembers(@PathVariable Integer id, @PathVariable String gender) {
        return churchService.getMembersByGender(id, gender);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @ApiOperation(value = "Create a new church", response = Church.class)
    public Church saveChurch(@RequestBody Church church) throws Exception {
        return churchService.saveChurch(church);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Modify an existing church", response = Church.class)
    public Church updateChurch(@RequestBody Church church) throws Exception {
        return churchService.saveChurch(church);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}/archive")
    @ApiOperation(value = "Archive an existing church", response = Church.class)
    public void archiveChurch(@PathVariable Integer id) {
        churchService.archiveChurch(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Remove a church", response = Church.class)
    public void deleteChurch(@PathVariable Integer id) {
        churchService.deleteChurch(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/expire")
    @PreAuthorize("hasAuthority('Admin')")
    public void expireCache() {
        churchService.expireCache();
    }
}
