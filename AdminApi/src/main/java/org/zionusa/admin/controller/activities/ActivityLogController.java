package org.zionusa.admin.controller.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.CalendarEvent;
import org.zionusa.admin.domain.activities.ActivityLog;
import org.zionusa.admin.domain.activities.ActivityReportGroup;
import org.zionusa.admin.domain.activities.ActivityReportMember;
import org.zionusa.admin.service.activities.ActivityLogService;
import org.zionusa.base.controller.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity-logs")
public class ActivityLogController extends BaseController<ActivityLog, Integer> {

    private final ActivityLogService service;

    @Autowired
    public ActivityLogController(ActivityLogService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/{startDate}/{endDate}")
    public List<ActivityLog> getActivityLogsByDateBetween(@PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogsByDateBetween(startDate, endDate);
    }

    @GetMapping("/user/{userId}")
    public List<ActivityLog> getActivityLogsByUserId(@PathVariable Integer userId) {
        return service.getActivityLogsByUserId(userId);
    }

    @GetMapping("/user/{userId}/{startDate}/{endDate}")
    public List<ActivityLog> getActivityLogsByUserIdAndDateBetween(@PathVariable Integer userId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogsByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @GetMapping("/user-activity/{userId}/{activityId}/{startDate}/{endDate}")
    public List<ActivityLog> getActivityLogsByUserIdAndActivityIdAndDateBetween(@PathVariable Integer userId, @PathVariable Integer activityId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogsByUserIdAndActivityIdAndDateBetween(userId, activityId, startDate, endDate);
    }

    @GetMapping("/church/{churchId}")
    public List<ActivityLog> getActivityLogsByChurchId(@PathVariable Integer churchId) {
        return service.getActivityLogsByChurchId(churchId);
    }

    @GetMapping("/church/{churchId}/{startDate}/{endDate}")
    public List<ActivityLog> getActivityLogsByChurchIdAndDateBetween(@PathVariable Integer churchId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogsByChurchIdAndDateBetween(churchId, startDate, endDate);
    }

    @GetMapping("/group/{groupId}")
    public List<ActivityLog> getActivityLogsByGroupId(@PathVariable Integer groupId) {
        return service.getActivityLogsByGroupId(groupId);
    }

    @GetMapping("/group/{groupId}/{startDate}/{endDate}")
    public List<ActivityLog> getActivityLogsByGroupIdAndDateBetween(@PathVariable Integer groupId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogsByGroupIdAndDateBetween(groupId, startDate, endDate);
    }

    @GetMapping("/team/{teamId}")
    public List<ActivityLog> getActivityLogsByTeamId(@PathVariable Integer teamId) {
        return service.getActivityLogsByTeamId(teamId);
    }

    @GetMapping("/team/{teamId}/{startDate}/{endDate}")
    public List<ActivityLog> getActivityLogsByTeamIdAndDateBetween(@PathVariable Integer teamId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogsByTeamIdAndDateBetween(teamId, startDate, endDate);
    }

    @GetMapping("/map/{userId}/{startDate}/{endDate}")
    public Map<String, List<CalendarEvent>> getActivityLogMap(@PathVariable Integer userId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getActivityLogMap(userId, startDate, endDate);
    }

    @GetMapping("/reports/church/{churchId}/{startDate}/{endDate}")
    public List<ActivityReportGroup> getChurchActivityReport(@PathVariable Integer churchId, @PathVariable String startDate, @PathVariable String endDate, HttpServletRequest request) {
        return service.getChurchActivityReport(request, churchId, startDate, endDate);
    }

    @GetMapping("/reports/group/{groupId}/{startDate}/{endDate}")
    public List<ActivityReportMember> getGroupActivityReport(@PathVariable Integer groupId, @PathVariable String startDate, @PathVariable String endDate, HttpServletRequest request) {
        return service.getGroupActivityReport(request, groupId, startDate, endDate);
    }

    @GetMapping("/reports/rankings/{startDate}/{endDate}")
    public List<ActivityReportMember> getRankingActivityReport(@PathVariable String startDate, @PathVariable String endDate, HttpServletRequest request) {
        return service.getMemberRankings(request, startDate, endDate);
    }

    @GetMapping("/reports/member/{userId}/{startDate}/{endDate}")
    public ActivityReportMember getMemberActivityReport(@PathVariable Integer userId, @PathVariable String startDate, @PathVariable String endDate) {
        return service.getMemberActivityReport(userId, startDate, endDate);
    }

    @GetMapping("/reports/clear-caches")
    public void forceCacheClear() {
        service.clearCaches();
    }
}
