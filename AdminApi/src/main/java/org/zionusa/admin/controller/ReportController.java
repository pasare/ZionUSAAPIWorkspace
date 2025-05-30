package org.zionusa.admin.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.Report;
import org.zionusa.admin.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping()
    public List<Report> getAll() {
        return reportService.getAll(null);
    }

    @GetMapping(value = "/{id}")
    public Report getById(@PathVariable Integer id) throws NotFoundException {
        return reportService.getById(id);
    }

    @PostMapping()
    public Report save(@RequestBody Report report) {
        return reportService.save(report);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) throws NotFoundException {
        reportService.delete(id);
    }
}
