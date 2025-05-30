package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.MovementGoal;
import org.zionusa.admin.service.MovementGoalService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/movement-goals")
public class MovementGoalController extends BaseController<MovementGoal, Integer> {

    private final MovementGoalService movementGoalService;

    @Autowired
    public MovementGoalController(MovementGoalService movementGoalService) {
        super(movementGoalService);
        this.movementGoalService = movementGoalService;
    }

    @GetMapping("/{movementId}/association/{associationId}")
    public MovementGoal.Association getAssociationByMovementIdAndReferenceId(@PathVariable Integer movementId, @PathVariable Integer associationId) {
        return movementGoalService.getAssociationByMovementIdAndReferenceId(movementId, associationId);
    }

    @GetMapping("/{movementId}/associations")
    public List<MovementGoal.Association> getAllAssociationsByMovementId(@PathVariable Integer movementId) {
        return movementGoalService.getAllAssociationsByMovementId(movementId);
    }

    @GetMapping("/{movementId}/church/{churchId}")
    public MovementGoal.Church getChurchByMovementIdAndReferenceId(@PathVariable Integer movementId, @PathVariable Integer churchId) {
        return movementGoalService.getChurchByMovementIdAndReferenceId(movementId, churchId);
    }

    @GetMapping("/{movementId}/churches")
    public List<MovementGoal.Church> getAllChurchesByMovementId(@PathVariable Integer movementId) {
        return movementGoalService.getAllChurchesByMovementId(movementId);
    }

    @GetMapping("/{movementId}/overseer/{churchId}")
    public MovementGoal.Overseer getOverseerByMovementIdAndReferenceId(@PathVariable Integer movementId, @PathVariable Integer churchId) {
        return movementGoalService.getOverseerByMovementIdAndReferenceId(movementId, churchId);
    }

    @GetMapping("/{movementId}/overseers")
    public List<MovementGoal.Overseer> getAllOverseersByMovementId(@PathVariable Integer movementId) {
        return movementGoalService.getAllOverseersByMovementId(movementId);
    }

    @GetMapping("/{shortTermId}/short-term")
    public MovementGoal.ShortTermPreaching geShortTermPreachingByShortTermId(@PathVariable Integer shortTermId) {
        return movementGoalService.geShortTermPreachingByShortTermId(shortTermId);
    }

}
