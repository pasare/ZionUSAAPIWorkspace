package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.MovementChurchGoal;

import java.util.List;

public interface MovementChurchGoalDao extends JpaRepository<MovementChurchGoal, Integer> {

    List<MovementChurchGoal> getAllByRegionIdIsNotNull();


}
