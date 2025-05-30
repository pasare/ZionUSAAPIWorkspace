package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface PreachingLogOverseerDao extends JpaRepository<PreachingLog.Overseer, String> {

    List<PreachingLog.Overseer> getByDateBetween(String startDate, String endDate);

    List<PreachingLog.Overseer> getByDateBetweenAndId(String startDate, String endDate, Integer churchId);

}
