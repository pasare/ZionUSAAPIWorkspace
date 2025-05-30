package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface PreachingLogAssociationDao extends JpaRepository<PreachingLog.Association, String> {

    List<PreachingLog.Association> getByDateBetween(String startDate, String endDate);

    List<PreachingLog.Association> getByDateBetweenAndId(String startDate, String endDate, Integer associationId);

}
