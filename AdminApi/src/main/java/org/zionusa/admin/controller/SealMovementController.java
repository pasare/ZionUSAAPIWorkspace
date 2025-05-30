package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.SealMovement;
import org.zionusa.admin.service.SealMovementService;

import java.util.List;

@RestController
@RequestMapping("/seal-movement")
public class SealMovementController {

    private final SealMovementService service;

    @Autowired
    public SealMovementController(SealMovementService service) {
        this.service = service;
    }

    @GetMapping()
    public List<SealMovement> getAll() {
        return service.getAll();
    }

    @GetMapping("/user/{id}")
    public List<SealMovement> getSealMovementByUserId(@PathVariable Integer id) {
        return service.getByUserId(id);
    }

    @GetMapping("/challenge/{id}")
    public List<SealMovement> getSealMovementByChallengeId(@PathVariable Integer id) {
        return service.getByChallengeId(id);
    }

    @GetMapping("/user-challenge/{userId}/{challengeId}")
    public List<SealMovement> getSealMovementByUserId(@PathVariable Integer userId, @PathVariable Integer challengeId) {
        return service.getByChallengeIdAndUserId(userId, challengeId);
    }
}
