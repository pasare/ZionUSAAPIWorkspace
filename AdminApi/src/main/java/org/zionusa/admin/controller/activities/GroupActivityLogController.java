package org.zionusa.admin.controller.activities;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.activities.GroupActivityLog;
import org.zionusa.admin.service.activities.GroupActivityLogService;
import org.zionusa.base.controller.BaseController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group-activity-logs")
@Api(value = "Group Activity Logs")
public class GroupActivityLogController extends BaseController<GroupActivityLog, Integer> {
    private final GroupActivityLogService service;

    @Autowired
    public GroupActivityLogController(GroupActivityLogService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/daily/{groupId}/{startDate}/{endDate}")
    public List<Map<String, Object>> getDailyGroupActivities(
            @PathVariable Integer groupId,
            @PathVariable String startDate,
            @PathVariable String endDate,
            @RequestBody List<String> columns) {
        return service.getDailyGroupActivities(groupId, startDate, endDate, columns);
    }
}
