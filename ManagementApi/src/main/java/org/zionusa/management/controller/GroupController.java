package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.service.GroupService;
import org.zionusa.management.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/groups")
@Api(value = "Groups")
public class GroupController extends BaseController<Group, Integer> {

    private final GroupService groupService;
    private final TeamService teamService;

    @Autowired
    public GroupController(GroupService groupService, TeamService teamService) {
        super(groupService);
        this.groupService = groupService;
        this.teamService = teamService;
    }

    @GetMapping(value = "/information")
    public List<Group> getAllGroupInformation() {
        return groupService.getAllGroupInformation();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}/archive")
    public void archiveGroup(@PathVariable Integer id) {
        groupService.archive(id);
    }

    @GetMapping(value = "{id}/teams")
    public List<Team> getTeamsByGroupId(@PathVariable Integer id) {
        return this.teamService.getByGroupId(id);
    }

    @GetMapping(value = "/{id}/teams/display")
    @ApiOperation(value = "View a minimal dataset of the teams of a group", response = List.class)
    public List<Team.DisplayTeam> getDisplayTeams(@PathVariable Integer id) {
        return teamService.getDisplayTeams(id);
    }

}
