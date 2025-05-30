package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.ChurchStatusGoal;

import java.util.List;

public interface ChurchStatusGoalDao extends JpaRepository<ChurchStatusGoal, Integer>  {

    List<ChurchStatusGoal> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(String startDate, String endDate);

    List<ChurchStatusGoal> findAllByMovementId(Integer movementId);

    List<ChurchStatusGoal> findAllByChurchId(Integer churchId);

    List<ChurchStatusGoal> findAllByGroupId(Integer groupId);

    List<ChurchStatusGoal> findAllByParentChurchId(Integer churchId);

    List<ChurchStatusGoal> findAllByMovementIdAndChurchId(Integer movementId, Integer churchId);

    List<ChurchStatusGoal> findAllByChurchIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Integer churchId, String startDate, String endDate);
}
