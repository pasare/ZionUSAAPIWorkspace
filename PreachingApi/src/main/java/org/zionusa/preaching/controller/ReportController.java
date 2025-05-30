package org.zionusa.preaching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.preaching.domain.Report;
import org.zionusa.preaching.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController extends BaseController<Report> {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        super(reportService);
        this.reportService = reportService;
    }

    @GetMapping(value = "/user/{userId}/{startDate}/{endDate}")
    List<Report> getReportByUserIdAndDate(@PathVariable Integer userId,
                              @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String startDate,
                              @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String endDate) {
        return reportService.getByUserIdAndDate(userId, startDate, endDate);
    }

    @GetMapping(value = "/team/{teamId}/{startDate}/{endDate}")
    List<Report> getReportByTeamIdAndDate(@PathVariable Integer teamId,
                                    @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String startDate,
                                    @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String endDate) {
        return reportService.getByTeamIdAndDate(teamId, startDate, endDate);
    }

    @GetMapping(value = "/group/{groupId}/{startDate}/{endDate}")
    List<Report> getReportByGroupIdAndDate(@PathVariable Integer groupId,
                                           @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String startDate,
                                           @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String endDate){
        return reportService.getByGroupIdAndDate(groupId, startDate, endDate);
    }

    @GetMapping(value = "/church/{churchId}/{startDate}/{endDate}")
    List<Report> getReportByChurchIdAndDate(@PathVariable Integer churchId,
                                            @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String startDate,
                                            @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String endDate){
        return reportService.getByChurchIdAndDate(churchId, startDate, endDate);
    }
}
