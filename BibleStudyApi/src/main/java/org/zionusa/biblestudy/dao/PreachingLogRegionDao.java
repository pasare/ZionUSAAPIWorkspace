package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface PreachingLogRegionDao extends JpaRepository<PreachingLog.Region, String> {

    List<PreachingLog.Region> getByDateBetween(String startDate, String endDate);

    List<PreachingLog.Region> getByDateBetweenAndRegionId(String startDate, String endDate, Integer regionId);

}
