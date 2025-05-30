package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.biblestudy.domain.ChurchStatusGoal;
import org.zionusa.biblestudy.service.GoalService;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping(value = "/church-status")
    List<ChurchStatusGoal> getAllChurchStatusGoals() {
        return goalService.findAllChurchStatusGoals();
    }

    @GetMapping(value = "/church-status/{startDate}/{endDate}")
    List<ChurchStatusGoal> getChurchStatusGoalsByDate(@PathVariable String startDate,
                                                          @PathVariable String endDate) {
        return goalService.findAllChurchStatusGoalsByDateBetween(startDate, endDate);
    }

    @GetMapping(value = "/church-status/{churchId}")
    List<ChurchStatusGoal> getChurchStatusGoalsByChurch(@PathVariable Integer churchId) {
        return goalService.findAllChurchStatusGoalsByChurchId(churchId);
    }

    @GetMapping(value = "/church-status/movement/{movementId}")
    List<ChurchStatusGoal> getChurchStatusGoalsByMovementId(@PathVariable Integer movementId) {
        return goalService.findAllChurchStatusGoalsByMovementId(movementId);
    }

    @GetMapping(value = "/church-status/movement/{movementId}/church/{churchId}")
    List<ChurchStatusGoal> getChurchStatusGoalsByChurch(@PathVariable Integer movementId, @PathVariable Integer churchId) {
        return goalService.findAllChurchStatusGoalsByMovementIdAndChurchId(movementId, churchId);
    }

    @GetMapping(value = "/church-status/group/{groupId}")
    List<ChurchStatusGoal> getChurchStatusGoalsByGroup(@PathVariable Integer groupId) {
        return goalService.findAllChurchStatusGoalsByGroupId(groupId);
    }

    @GetMapping(value = "/church-status/parent-church/{parentChurchId}")
    List<ChurchStatusGoal> getChurchStatusGoalsByParentChurch(@PathVariable Integer parentChurchId) {
        return goalService.findAllChurchStatusGoalsByParentChurchId(parentChurchId);
    }

    @GetMapping(value = "/church-status/{churchId}/{startDate}/{endDate}")
    List<ChurchStatusGoal> getChurchStatusGoalsByChurchAndDate(@PathVariable Integer churchId,
                                                                   @PathVariable String startDate,
                                                                   @PathVariable String endDate) {
        return goalService.findAllChurchStatusGoalsByChurchIdAndDateBetween(churchId, startDate, endDate);
    }

    @PostMapping(value = "/church-status/{churchId}")
    ChurchStatusGoal saveChurchStatusReportsByChurchAndDate(@PathVariable Integer churchId, @RequestBody ChurchStatusGoal churchStatusGoal) {
        return goalService.saveChurchStatusGoal(churchId, churchStatusGoal);
    }

    @DeleteMapping(value = "/church-status/{churchId}/{churchStatusGoalId}")
    void deleteChurchStatusGoalByChurchAndGoalId(@PathVariable Integer churchId, @PathVariable Integer churchStatusGoalId) {
        goalService.deleteChurchStatusGoal(churchId, churchStatusGoalId);
    }
}
