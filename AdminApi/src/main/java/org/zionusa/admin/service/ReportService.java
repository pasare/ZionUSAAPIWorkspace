package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.PermissionDao;
import org.zionusa.admin.dao.ReportDao;
import org.zionusa.admin.domain.Permission;
import org.zionusa.admin.domain.Report;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService extends BaseService<Report, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ReportDao reportDao;
    private final PermissionService permissionService;
    private final PermissionDao permissionDao;

    @Autowired
    public ReportService(ReportDao reportDao, PermissionService permissionService, PermissionDao permissionDao) {
        super(reportDao, logger, Report.class);
        this.reportDao = reportDao;
        this.permissionService = permissionService;
        this.permissionDao = permissionDao;
    }

    @Override
    public List<Report> getAll(Boolean archived) {
        List<Report> reports = super.getAll(archived);

        for (Report report : reports) {
            Permission permission = permissionDao.findPermissionByReferenceAndReferenceId(Permission.Reference.REPORT, report.getId());
            report.setPermission(permission);
        }

        return permissionService.filterReportListByPermission(reports);
    }

    @Override
    public Report getById(Integer id) throws NotFoundException {
        Optional<Report> reportOptional = reportDao.findById(id);

        if (!reportOptional.isPresent())
            throw new NotFoundException("The Form could not be found in the system");

        //get the form permission
        Report report = reportOptional.get();
        Permission permission = permissionDao.findPermissionByReferenceAndReferenceId(Permission.Reference.REPORT, id);

        report.setPermission(permission);
        if (permissionService.checkPermission(reportOptional.get().getPermission()))
            return report;

        return null;
    }

    @Override
    public Report save(Report report) {
        Report returnedReport = reportDao.save(report);

        if (report.getPermission() != null) {
            Permission permission = report.getPermission();
            permission.setReferenceId(returnedReport.getId());
            permission.setReference(Permission.Reference.REPORT);
            permissionDao.save(permission);
        }

        return returnedReport;
    }
}
