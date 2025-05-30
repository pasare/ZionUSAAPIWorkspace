package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.MovementGoalResult;
import org.zionusa.admin.service.MovementGoalResultService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/movement-goal-results")
public class MovementGoalResultController extends BaseController<MovementGoalResult, Integer> {
    private final MovementGoalResultService movementGoalResultService;

    @Autowired
    public MovementGoalResultController(MovementGoalResultService movementGoalResultService) {
        super(movementGoalResultService);
        this.movementGoalResultService = movementGoalResultService;
    }

    @GetMapping("/{movementId}/associations")
    List<MovementGoalResult.Association> getAssociationResultsByMovementId(@PathVariable Integer movementId) {
        return movementGoalResultService.getAssociationResultsByMovementId(movementId);
    }

    @GetMapping("/{movementId}/churches")
    List<MovementGoalResult.Church> getChurchResultsByMovementId(@PathVariable Integer movementId) {
        return movementGoalResultService.getChurchResultsByMovementId(movementId);
    }

    @GetMapping("/{movementId}/overseers")
    List<MovementGoalResult.Overseer> getOverseerResultsByMovementId(@PathVariable Integer movementId) {
        return movementGoalResultService.getOverseerResultsByMovementId(movementId);
    }
}
