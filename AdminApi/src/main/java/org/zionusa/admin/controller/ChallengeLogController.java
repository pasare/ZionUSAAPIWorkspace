package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.ChallengeLog;
import org.zionusa.admin.service.ChallengeLogService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/challenge-logs")
public class ChallengeLogController extends BaseController<ChallengeLog, Integer> {

    private final ChallengeLogService service;

    @Autowired
    public ChallengeLogController(ChallengeLogService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/{startDate}/{endDate}")
    public List<ChallengeLog> getActivityGroupLogsByDateBetween(@PathVariable String startDate, @PathVariable String endDate) {
        return service.getChallengeLogsByDateBetween(startDate, endDate);
    }

    @GetMapping("/{userId}/{startDate}/{endDate}")
    public List<ChallengeLog> getActivityGroupLogsByUserIdAndDateBetween(@PathVariable Integer userId, @PathVariable String startDate, @PathVariable String endDate){
        return service.getChallengeLogsByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @GetMapping("/user/{userId}")
    public List<ChallengeLog> getActivityGroupLogsByUserId(@PathVariable Integer userId){
        return service.getChallengeLogsByUserId(userId);
    }
}
