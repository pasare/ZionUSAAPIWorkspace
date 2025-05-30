package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.GoalLog;
import org.zionusa.admin.service.GoalLogService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/goal-logs")
public class GoalLogController extends BaseController<GoalLog, Integer> {

    private final GoalLogService service;

    @Autowired
    public GoalLogController(GoalLogService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/{startDate}/{endDate}")
    public List<GoalLog> getActivityGroupLogsByDateBetween(@PathVariable String startDate, @PathVariable String endDate) {
        return service.getGoalLogsByDateBetween(startDate, endDate);
    }

    @GetMapping("/{userId}/{startDate}/{endDate}")
    public List<GoalLog> getActivityGroupLogsByUserIdAndDateBetween(@PathVariable Integer userId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getGoalLogsByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @GetMapping("/user/{userId}")
    public List<GoalLog> getActivityGroupLogsByUserId(@PathVariable Integer userId){
        return service.getGoalLogsByUserId(userId);
    }

    @GetMapping("/user/{userId}/{date}")
    public List<GoalLog> getGoalLogsByStartDateLessThanAndEndDateGreaterThan(@PathVariable Integer userId, @PathVariable String date) {
        return service.getGoalLogsByUserIdAndStartDateLessThanAndEndDateGreaterThan(userId, date);
    }

}
