package org.zionusa.biblestudy.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.zionusa.biblestudy.domain.ChurchStatusReport;
import org.zionusa.biblestudy.domain.GoalReport;
import org.zionusa.biblestudy.domain.PreachingLog;
import org.zionusa.biblestudy.domain.PreachingLogSegments;
import org.zionusa.biblestudy.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/church-status")
    public List<ChurchStatusReport> getAllChurchStatusReports() {
        return reportService.findAllChurchStatusReports();
    }

    @GetMapping(value = "/church-status/{startDate}/{endDate}")
    public List<ChurchStatusReport> getChurchStatusReportsByDate(@PathVariable String startDate,
                                                          @PathVariable String endDate) {
        return reportService.findAllChurchStatusReportsByDateBetween(startDate, endDate);
    }

    @GetMapping(value = "/church-status/{churchId}")
    public List<ChurchStatusReport> getChurchStatusReportsByChurch(@PathVariable Integer churchId) {
        return reportService.findAllChurchStatusReportsByChurchId(churchId);
    }

    @GetMapping(value = "/church-status/{churchId}/{startDate}/{endDate}")
    public List<ChurchStatusReport> getChurchStatusReportsByChurchAndDate(@PathVariable Integer churchId,
                                                                   @PathVariable String startDate,
                                                                   @PathVariable String endDate) {
        return reportService.findAllChurchStatusReportsByChurchIdAndDateBetween(churchId, startDate, endDate);
    }

    @PostMapping(value = "/church-status/{churchId}")
    public ChurchStatusReport saveChurchStatusReportsByChurchAndDate(@PathVariable Integer churchId, @RequestBody ChurchStatusReport churchStatusReport) {
        return reportService.saveChurchStatusReport(churchId, churchStatusReport);
    }

    @DeleteMapping(value = "/church-status/{churchId}/{churchStatusReportId}")
    public void deleteChurchStatusReportsByChurchAndDate(@PathVariable Integer churchId, @PathVariable Integer churchStatusReportId) {
        reportService.deleteChurchStatusReport(churchId, churchStatusReportId);
    }

    @GetMapping(
            value = "/goal/fruit/{fruitGoalPerMember}/{fruitStartDate}/{fruitEndDate}/signatures/{signaturesGoalPerMember}/" +
                    "{signaturesStartDate}/{signaturesEndDate}/{referenceName}/{referenceId}"
    )
    @ApiOperation(value = "Goal based fruit and signature report within a date range.")
    public GoalReport getFruitSignaturesReport(
            HttpServletRequest request,
            @PathVariable Integer fruitGoalPerMember,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String fruitStartDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String fruitEndDate,
            @PathVariable Integer signaturesGoalPerMember,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String signaturesStartDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String signaturesEndDate,
            @PathVariable Integer referenceId,
            @PathVariable String referenceName) throws Exception {
        return reportService.getFruitSignaturesReport(request, fruitGoalPerMember, fruitStartDate, fruitEndDate,
                signaturesGoalPerMember, signaturesStartDate, signaturesEndDate, referenceName, referenceId);
    }

    @GetMapping(value = "/preaching/totals/{startDate}/{endDate}")
    @ApiOperation("View preaching log totals by date range.")
    public List<PreachingLog.PreachingLogTotals> getTotalsDateBetween(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                                               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return reportService.getPreachingTotalsDateBetween(startDate, endDate);
    }

    @GetMapping(value = "/preaching/totals/associations/{startDate}/{endDate}")
    @ApiOperation("View preaching log totals by association per date range.")
    public List<PreachingLog.Association> getPreachingTotalsByAssociation(
            HttpServletRequest request,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return reportService.getPreachingTotalsByAssociation(request, startDate, endDate);
    }

    @GetMapping(value = "/preaching/totals/churches/{startDate}/{endDate}")
    @ApiOperation("View preaching log totals by church per date range.")
    public List<PreachingLog.Church> getPreachingTotalsByChurch(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return reportService.getPreachingTotalsByChurch(startDate, endDate);
    }

    /*@GetMapping(value = "/preaching/totals/regions/{startDate}/{endDate}")
    @ApiOperation("View preaching log totals by region per date range.")
    public List<PreachingLog.Region> getPreachingTotalsByRegion(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return reportService.getPreachingTotalsByRegion(startDate, endDate);
    }*/

    @GetMapping(value = "/preaching/totals/overseers/{startDate}/{endDate}")
    @ApiOperation("View preaching log totals overseer per date range.")
    public List<PreachingLog.Overseer> getPreachingTotalsByOverseer(
            HttpServletRequest request,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        return reportService.getPreachingTotalsByOverseer(request, startDate, endDate);
    }

    @GetMapping(value = "/preaching/totals/segment/{startDate}/{endDate}")
    @ApiOperation("View preaching log totals by date range for all, church, group, team and user.")
    public PreachingLogSegments getPreachingTotalsBySegment(HttpServletRequest request,
                                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return reportService.getPreachingTotalsBySegment(request, startDate, endDate);
    }

    @GetMapping(value = "/preaching/total-count/{startDate}/{endDate}")
    @ApiOperation("View preaching total count by date range.")
    public Integer getTotalCountDateBetween(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return reportService.getTotalCountDateBetween(startDate, endDate);
    }
}
