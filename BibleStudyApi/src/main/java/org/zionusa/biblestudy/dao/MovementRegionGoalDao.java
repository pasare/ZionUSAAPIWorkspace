package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.MovementRegionGoal;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface MovementRegionGoalDao extends JpaRepository<MovementRegionGoal, Integer> {

    List<PreachingLog.Region> getByDateBetween(String startDate, String endDate);


}
