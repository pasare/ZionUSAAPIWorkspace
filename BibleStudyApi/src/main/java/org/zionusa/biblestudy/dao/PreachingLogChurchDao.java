package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface PreachingLogChurchDao extends JpaRepository<PreachingLog.Church, String> {

    List<PreachingLog.Church> getByDateBetween(String startDate, String endDate);

    List<PreachingLog.Church> getByDateBetweenAndId(String startDate, String endDate, Integer churchId);

}
