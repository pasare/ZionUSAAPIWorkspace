package org.zionusa.preaching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.preaching.domain.Report;

import java.util.List;

public interface ReportDao extends JpaRepository<Report, Integer> {

    Report getReportByUserIdAndDate(Integer userId, String date);

    List<Report> getReportsByUserIdAndDateBetween(Integer userId, String startDate, String endDate);
}
