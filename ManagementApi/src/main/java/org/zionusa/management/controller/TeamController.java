package org.zionusa.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.Team;
import org.zionusa.management.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController extends BaseController<Team, Integer> {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        super(teamService);
        this.teamService = teamService;
    }

    @GetMapping(value = "/information")
    public List<Team> getAllTeamInformation() {
        return teamService.getAllTeamInformation();
    }

    @GetMapping(value = "/church-team/{churchId}")
    public Team getChurchTeam(@PathVariable Integer churchId) {
        return teamService.getChurchTeam(churchId);
    }

    @GetMapping(value = "/church-team/association/{associationId}")
    public List<Team.ChurchTeam> getChurchTeamsByAssociation(@PathVariable Integer associationId) {
        return teamService.getChurchTeamsByAssociation(associationId);
    }

    @GetMapping(value = "/church-team/main-branch/{mainBranchId}")
    public List<Team.ChurchTeam> getChurchTeamsByMainBranch(@PathVariable Integer mainBranchId) {
        return teamService.getChurchTeamsByMainBranch(mainBranchId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}/archive")
    public void archiveTeam(@PathVariable Integer id) {
        teamService.archive(id);
    }

}
