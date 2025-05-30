package org.zionusa.management.domain.report;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/branch/{branchId}/user-activity-email")
    public void branchUserActivityEmail(
        @PathVariable Integer branchId,
        @RequestParam(value = "emailTo", required = false) String emailTo,
        @RequestParam(value = "monthsActive", required = false) Integer monthsActive) {
        service.branchUserActivityEmail(branchId, emailTo, monthsActive);
    }
}
