package org.zionusa.admin.controller.activities;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.activities.GroupActivityPlan;
import org.zionusa.admin.domain.activities.GroupActivityReport;
import org.zionusa.admin.service.activities.GroupActivityPlanService;
import org.zionusa.base.controller.BaseController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group-activity-plans")
@Api(value = "Group Activity Daily Plans")
public class GroupActivityPlanController extends BaseController<GroupActivityPlan, Integer> {
    private final GroupActivityPlanService service;

    @Autowired
    public GroupActivityPlanController(GroupActivityPlanService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/daily/{groupId}/{startDate}/{endDate}")
    public List<Map<String, Object>> getDailyGroupPlans(
        @PathVariable Integer groupId,
        @PathVariable String startDate,
        @PathVariable String endDate,
        @RequestBody List<String> columns) {
        return service.getDailyGroupPlans(groupId, startDate, endDate, columns);
    }

    @GetMapping("/report/{groupId}/{startDate}/{endDate}")
    public GroupActivityReport getGroupPlansReport(
        @PathVariable Integer groupId,
        @PathVariable String startDate,
        @PathVariable String endDate) {
        return service.getGroupPlansReport(groupId, startDate, endDate);
    }

    @GetMapping("/category-report/{categoryId}/{churchId}/{startDate}/{endDate}")
    public List<GroupActivityReport> getChurchPlansReportByCategory(
        @PathVariable Integer categoryId,
        @PathVariable Integer churchId,
        @PathVariable String startDate,
        @PathVariable String endDate) {
        return service.getGroupPlanReportByCategory(categoryId, churchId, startDate, endDate);
    }

    @GetMapping("/category-report/{churchId}/{startDate}/{endDate}")
    public List<GroupActivityReport> getChurchPlansReportByChurchId(
        @PathVariable Integer churchId,
        @PathVariable String startDate,
        @PathVariable String endDate) {
        return service.getGroupPlanReportByCategory(null, churchId, startDate, endDate);
    }
}
