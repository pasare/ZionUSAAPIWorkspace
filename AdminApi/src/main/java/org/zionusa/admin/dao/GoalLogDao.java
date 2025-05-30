package org.zionusa.admin.dao;

import org.zionusa.admin.domain.GoalLog;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface GoalLogDao extends BaseDao<GoalLog, Integer> {

    List<GoalLog> findAllByChallengeId(Integer challengeId);

    List<GoalLog> getGoalLogsByStartDateBetween(String startDate, String endDate);

    List<GoalLog> getGoalLogsByUserIdAndStartDateBetween(Integer userId, String startDate, String endDate);

    List<GoalLog> getGoalLogsByUserIdAndStartDateAfterAndEndDateAfter(Integer userId, String startDate, String endDate);

    List<GoalLog> getGoalLogsByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer userId, String startDate, String endDate);

    List<GoalLog> getGoalLogsByUserId(Integer userId);

    Integer countGoalLogsByUserIdAndStartDateIsBetween(Integer userId, String startDate, String endDate);

    Integer countGoalLogsByUserIdAndStartDateIsBetweenAndCompletedTrue(Integer userId, String startDate, String endDate);
}
