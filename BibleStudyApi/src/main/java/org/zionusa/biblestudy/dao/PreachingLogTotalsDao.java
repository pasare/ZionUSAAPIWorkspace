package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface PreachingLogTotalsDao extends JpaRepository<PreachingLog.PreachingLogTotals, Integer> {

    List<PreachingLog.PreachingLogTotals> getByDateBetween(String startDate, String endDate);

}