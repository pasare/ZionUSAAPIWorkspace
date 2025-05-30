package org.zionusa.admin.controller.activities;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.activities.ChurchActivityLog;
import org.zionusa.admin.service.activities.ChurchActivityLogService;
import org.zionusa.base.controller.BaseController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/church-activity-logs")
@Api(value = "Church Activity Logs")
public class ChurchActivityLogController extends BaseController<ChurchActivityLog, Integer> {
    private final ChurchActivityLogService service;

    @Autowired
    public ChurchActivityLogController(ChurchActivityLogService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/daily/{churchId}/{startDate}/{endDate}")
    public List<Map<String, Object>> getDailyChurchActivities(
            @PathVariable Integer churchId,
            @PathVariable String startDate,
            @PathVariable String endDate,
            @RequestBody List<String> columns) {
        return service.getDailyChurchActivities(churchId, startDate, endDate, columns);
    }
}
