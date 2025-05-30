package org.zionusa.preaching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.preaching.dao.ReportDao;
import org.zionusa.preaching.domain.Report;

import java.util.List;

@Service
public class ReportService extends BaseService<Report> {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ReportDao reportDao;


    @Autowired
    public ReportService(ReportDao reportDao) {
        super(reportDao, logger, Report.class);
        this.reportDao = reportDao;
    }

    public List<Report> getByUserIdAndDate(Integer userId, String startDate, String endDate) {
        return reportDao.getReportsByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public List<Report> getByTeamIdAndDate(Integer teamId, String startDate, String endDate) {
        return null;
    }

    public List<Report> getByGroupIdAndDate(Integer groupId, String startDate, String endDate) {
        return null;
    }

    public List<Report> getByChurchIdAndDate(Integer churchId, String startDate, String endDate) {
        return null;
    }
}
