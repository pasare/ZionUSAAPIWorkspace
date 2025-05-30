package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.domain.ChurchStatusReport;

import java.util.Date;
import java.util.List;

public interface ChurchStatusReportDao extends JpaRepository<ChurchStatusReport, Integer> {

    List<ChurchStatusReport> findAllByDateBetween(String startDate, String endDate);

    List<ChurchStatusReport> findAllByChurchId(Integer churchId);

    List<ChurchStatusReport> findAllByChurchIdAndDateBetween(Integer churchId, String startDate, String endDate);

}
