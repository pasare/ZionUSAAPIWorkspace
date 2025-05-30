package org.zionusa.admin.controller.activities;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.activities.ChurchActivityPlan;
import org.zionusa.admin.domain.activities.ChurchActivityReport;
import org.zionusa.admin.service.activities.ChurchActivityPlanService;
import org.zionusa.base.controller.BaseController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/church-activity-plans")
@Api(value = "Church Activity Daily Plans")
public class ChurchActivityPlanController extends BaseController<ChurchActivityPlan, Integer> {
    private final ChurchActivityPlanService service;

    @Autowired
    public ChurchActivityPlanController(ChurchActivityPlanService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/daily/{churchId}/{startDate}/{endDate}")
    public List<Map<String, Object>> getDailyChurchPlans(
            @PathVariable Integer churchId,
            @PathVariable String startDate,
            @PathVariable String endDate,
            @RequestBody List<String> columns) {
        return service.getDailyChurchPlans(churchId, startDate, endDate, columns);
    }

    @GetMapping("/report/{churchId}/{startDate}/{endDate}")
    public ChurchActivityReport getChurchPlansReport(
            @PathVariable Integer churchId,
            @PathVariable String startDate,
            @PathVariable String endDate) {
        return service.getChurchPlansReport(churchId, startDate, endDate);
    }

    @GetMapping("/category-report/{categoryId}/{churchTypeId}/{startDate}/{endDate}")
    public List<ChurchActivityReport> getChurchPlansReportByCategory(
            @PathVariable Integer categoryId,
            @PathVariable Integer churchTypeId,
            @PathVariable String startDate,
            @PathVariable String endDate) {
        return service.getChurchPlanReportByCategory(categoryId, churchTypeId, startDate, endDate);
    }

    @GetMapping("/category-report/{churchTypeId}/{startDate}/{endDate}")
    public List<ChurchActivityReport> getChurchPlansReportByDate(
        @PathVariable Integer churchTypeId,
        @PathVariable String startDate,
        @PathVariable String endDate) {
        return service.getChurchPlanReportByCategory(null, churchTypeId, startDate, endDate);
    }
}
