package org.zionusa.biblestudy.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.*;
import org.zionusa.biblestudy.service.PreachingLogService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/preaching-log")
public class PreachingLogController extends BaseController<PreachingLog, Integer> {

    private final PreachingLogService preachingLogService;

    @Autowired
    public PreachingLogController(PreachingLogService preachingLogService) {
        super(preachingLogService);
        this.preachingLogService = preachingLogService;
    }

    @PostMapping(value = "/all/{startDate}/{endDate}/one-per-day")
    @ApiOperation("Set all preaching logs per user within a date range to one per day.")
    public void postAllByDateBetweenOnePerDay(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        preachingLogService.postAllByDateBetweenOnePerDay(startDate, endDate);
    }

    @GetMapping(value = "/all/user-rankings/apa/week/1")
    @ApiOperation("APA Movement user rankings in week 1")
    public List<PreachingLogApaReportWeek1> getApaRankingsWeek1Report(
        @RequestParam(value = "sort", required = false) String sort
    ) {
        return preachingLogService.getApaRankingsWeek1Report(sort);
    }

    @GetMapping(value = "/church/user-rankings/apa/week/1")
    @ApiOperation("APA Movement user rankings in week 1")
    public List<PreachingLogApaReportWeek1> getChurchApaRankingsWeek1Report(
        @RequestParam(value = "churchId") Integer churchId,
        @RequestParam(value = "sort") String sort
    ) {
        return preachingLogService.getChurchApaRankingsWeek1Report(churchId, sort);
    }

    @GetMapping(value = "/all/user-rankings/apa/week/2")
    @ApiOperation("APA Movement user rankings in week 2")
    public List<PreachingLogApaReportWeek2> getApaRankingsWeek2Report(
        @RequestParam(value = "sort", required = false) String sort
    ) {
        return preachingLogService.getApaRankingsWeek2Report(sort);
    }

    @GetMapping(value = "/church/user-rankings/apa/week/2")
    @ApiOperation("APA Movement user rankings in week 2")
    public List<PreachingLogApaReportWeek2> getChurchApaRankingsWeek2Report(
        @RequestParam(value = "churchId") Integer churchId,
        @RequestParam(value = "sort") String sort
    ) {
        return preachingLogService.getChurchApaRankingsWeek2Report(churchId, sort);
    }

    @GetMapping(value = "/all/user-rankings/apa/week/3")
    @ApiOperation("APA Movement user rankings in week 3")
    public List<PreachingLogApaReportWeek3> getApaRankingsWeek3Report(
        @RequestParam(value = "sort", required = false) String sort
    ) {
        return preachingLogService.getApaRankingsWeek3Report(sort);
    }

    @GetMapping(value = "/church/user-rankings/apa/week/3")
    @ApiOperation("APA Movement user rankings in week 3")
    public List<PreachingLogApaReportWeek3> getChurchApaRankingsWeek3Report(
        @RequestParam(value = "churchId") Integer churchId,
        @RequestParam(value = "sort") String sort
    ) {
        return preachingLogService.getChurchApaRankingsWeek3Report(churchId, sort);
    }

    @GetMapping(value = "/association/{associationId}/{startDate}/{endDate}")
    @ApiOperation("View preaching logs per association within a date range.")
    public List<PreachingLog> getAllByAssociationIdAndDateBetween(HttpServletRequest request,
                                                                  @PathVariable Integer associationId,
                                                                  @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                                                  @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return preachingLogService.getAllByAssociationIdAndDateBetween(request, associationId, startDate, endDate);
    }

    @GetMapping(value = "/church/{churchId}/{startDate}/{endDate}")
    @ApiOperation("View preaching logs per church within a date range.")
    public List<PreachingLog> getAllByChurchIdAndDateBetween(HttpServletRequest request,
                                                             @PathVariable Integer churchId,
                                                             @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                                             @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return preachingLogService.getAllByChurchIdAndDateBetween(request, churchId, startDate, endDate);
    }

    @GetMapping(value = "/group/{groupId}/{startDate}/{endDate}")
    @ApiOperation("View preaching logs per group within a date range.")
    public List<PreachingLog> getAllByGroupIdAndDateBetween(HttpServletRequest request,
                                                            @PathVariable Integer groupId,
                                                            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                                            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return preachingLogService.getAllByGroupIdAndDateBetween(request, groupId, startDate, endDate);
    }

    @GetMapping(value = "/user/{userId}/{startDate}/{endDate}")
    @ApiOperation("View preaching logs per user within a date range.")
    public List<PreachingLog> getAllByUserIdAndDateBetween(
        @PathVariable Integer userId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return preachingLogService.getAllByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @PostMapping(value = "/user/{userId}/{startDate}/{endDate}/one-per-day")
    @ApiOperation("Set preaching logs per user within a date range to one per day.")
    public void postAllByUserIdAndDateBetweenOnePerDay(
        @PathVariable Integer userId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        preachingLogService.postAllByUserIdAndDateBetweenOnePerDay(userId, startDate, endDate);
    }

    // for daily preaching
    @GetMapping(value = "/users/all/{startDate}/{endDate}/{simpleGoal}/{meaningfulGoal}/{fruitGoal}")
    @ApiOperation("Ranked users preaching by category for all users.")
    public List<PreachingLogUserReport> getUserReportsByGoalsAndDateBetween(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
        @PathVariable Integer simpleGoal,
        @PathVariable Integer meaningfulGoal,
        @PathVariable Integer fruitGoal
    ) throws IllegalArgumentException {
        return preachingLogService.getAllUserReportsByGoalsAndDateBetween(startDate, endDate, simpleGoal, meaningfulGoal, fruitGoal);
    }

    @GetMapping(value = "/user-rankings/all/{startDate}/{endDate}/{categoryName}")
    @ApiOperation("Ranked users preaching by category for all users.")
    public List<PreachingLogUserReport> getUserRankingsByCategoryAndDateBetween(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
        @PathVariable String categoryName,
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "limit", required = false) Integer limit
    ) throws IllegalArgumentException {
        return preachingLogService.getUserRankingsByCategoryAndDateBetween(startDate, endDate, categoryName, offset, limit);
    }

    @GetMapping(value = "/user-rankings/short-term/{shortTermId}/{categoryName}")
    @ApiOperation("Ranked users preaching by category for all users during short term.")
    public List<PreachingLogUserReport> getUserShortTermRankingsByCategoryAndDateBetween(
        @PathVariable Integer shortTermId,
        @PathVariable String categoryName,
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "limit", required = false) Integer limit
    ) throws IllegalArgumentException {
        return preachingLogService.getUserShortTermRankingsByCategoryAndDateBetween(shortTermId, categoryName, offset, limit);
    }

    @GetMapping(value = "/user-rankings/church/{churchId}/{startDate}/{endDate}/{categoryName}")
    @ApiOperation("Ranked users preaching by category for all users in a church.")
    public List<PreachingLogUserReport> getChurchUserRankingsByCategoryAndDateBetween(
        @PathVariable Integer churchId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
        @PathVariable String categoryName,
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "limit", required = false) Integer limit
    ) throws IllegalArgumentException {
        return preachingLogService.getChurchUserRankingsByCategoryAndDateBetween(churchId, startDate, endDate, categoryName, offset, limit);
    }

    @GetMapping(value = "/user-rankings/overseer/{churchId}/{startDate}/{endDate}/{categoryName}")
    @ApiOperation("Ranked users preaching by category for all users in an overseer church.")
    public List<PreachingLogUserReport> getOverseerUserRankingsByCategoryAndDateBetween(
        @PathVariable Integer churchId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
        @PathVariable String categoryName,
        @RequestParam(value = "offset", required = false) Integer offset,
        @RequestParam(value = "limit", required = false) Integer limit
    ) throws IllegalArgumentException {
        return preachingLogService.getOverseerUserRankingsByCategoryAndDateBetween(churchId, startDate, endDate, categoryName, offset, limit);
    }

    @GetMapping(value = "/church-rankings/{startDate}/{endDate}/{categoryName}")
    @ApiOperation("Ranked church preaching by category for all churches")
    public List<PreachingLog.ChurchWithGoals> getChurchRankingsByCategoryAndDateBetween(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
        @PathVariable String categoryName
    ) throws IllegalArgumentException {
        return preachingLogService.getChurchRankingsByCategoryAndDateBetween(startDate, endDate,
            categoryName);
    }

    @GetMapping(value = "/region-rankings/{startDate}/{endDate}/{categoryName}")
    @ApiOperation("Ranked church preaching by category for all churches")
    public List<PreachingLog.RegionWithGoals> getRegionRankingsByCategoryAndDateBetween(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
        @PathVariable String categoryName
    ) throws IllegalArgumentException {
        return preachingLogService.getRegionRankingsByCategoryAndDateBetween(startDate, endDate,
            categoryName);
    }

    @GetMapping(value = "/user-rank/all/{userId}/{startDate}/{endDate}")
    @ApiOperation("Single user preaching rank by category.")
    public PreachingLogUserRank getUserRankByDateBetween(
        @PathVariable Integer userId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return preachingLogService.getUserRankByDateBetween(userId, startDate, endDate);
    }

    @GetMapping(value = "/user-rank/short-term/{shortTermId}/{userId}")
    @ApiOperation("Single user preaching rank by category during short term peaching.")
    public PreachingLogUserRank getUserShortTermRankByDateBetween(
        @PathVariable Integer shortTermId,
        @PathVariable Integer userId
    ) {
        return preachingLogService.getShortTermUserRankByDateBetween(shortTermId, userId);
    }

    @GetMapping(value = "/user-rank/church/{userId}/{churchId}/{startDate}/{endDate}")
    @ApiOperation("Single user preaching rank by church and category.")
    public PreachingLogUserRank getUserChurchRankByDateBetween(
        @PathVariable Integer userId,
        @PathVariable Integer churchId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return preachingLogService.getUserChurchRankByDateBetween(userId, churchId, startDate, endDate);
    }

    @GetMapping(value = "/user-rank/overseer/{userId}/{churchId}/{startDate}/{endDate}")
    @ApiOperation("Single user preaching rank by church and category.")
    public PreachingLogUserRank getUserOverseerRankByDateBetween(
        @PathVariable Integer userId,
        @PathVariable Integer churchId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return preachingLogService.getUserOverseerRankByDateBetween(userId, churchId, startDate, endDate);
    }

    @GetMapping(value = "/user-totals/{userId}/{startDate}/{endDate}")
    @ApiOperation("Single user preaching totals for any date range.")
    public PreachingLogUserReport getUserTotalsByDateBetween(
        @PathVariable Integer userId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return preachingLogService.getUserTotalsByDateBetween(userId, startDate, endDate);
    }

    //getChurchRank
    @GetMapping(value = "/church-rank/{churchId}/{startDate}/{endDate}")
    @ApiOperation("Single user preaching rank by church and category.")
    public Map<String, Integer> getChurchRankByDateBetween(
        @PathVariable Integer churchId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return preachingLogService.getChurchRankDateBetween(churchId, startDate, endDate);
    }

    @GetMapping(value = "/usage-report/all")
    @ApiOperation("Get monthly tracker usage for all users")
    public List<TrackerUsageAllReport> getTrackerUsageAll() {
        return preachingLogService.getTrackerUsageAll();
    }

    @GetMapping(value = "/usage-report/church")
    @ApiOperation("Get monthly tracker usage by church")
    public List<TrackerUsageChurchReport> getTrackerUsageByChurch() {
        return preachingLogService.getTrackerUsageByChurch();
    }

    //getChurchesByRegion
    @GetMapping(value = "/region-rankings/church/{startDate}/{endDate}/{categoryName}")
    @ApiOperation("Get region rankings")
    public List<PreachingLog.RegionWithChurches> getRegionRankingsByChurch(@PathVariable String startDate,
                                                                           @PathVariable String endDate,
                                                                           @PathVariable String categoryName) {
        return preachingLogService.getChurchesByRegion(startDate, endDate, categoryName);
    }
}
