package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.ActivityLog;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ActivityLogDao extends BaseDao<ActivityLog, Integer> {

    List<ActivityLog> getActivityLogsByDateBetween(String startDate, String endDate);

    List<ActivityLog> getActivityLogsByUserId(Integer userId);

    List<ActivityLog> getActivityLogsByUserIdAndDateBetweenOrderByDateDescIdDesc(Integer userId, String startDate, String endDate);

    List<ActivityLog> getActivityLogsByUserIdAndActivityIdAndDateBetween(Integer userId, Integer activityId, String startDate, String endDate);

    List<ActivityLog> getActivityLogsByChurchId(Integer churchId);

    List<ActivityLog> getActivityLogsByChurchIdAndDateBetween(Integer churchId, String startDate, String endDate);

    List<ActivityLog> getActivityLogsByGroupId(Integer groupId);

    List<ActivityLog> getActivityLogsByGroupIdAndDateBetween(Integer groupId, String startDate, String endDate);

    List<ActivityLog> getActivityLogsByTeamId(Integer teamId);

    List<ActivityLog> getActivityLogsByTeamIdAndDateBetween(Integer teamId, String startDate, String endDate);

    List<ActivityLog> getActivityLogsByActivityId(Integer activityId);

    List<ActivityLog> getActivityLogsByChallengeLogId(Integer challengeLogId);

    List<ActivityLog> getActivityLogsByGoalLogId(Integer goalId);

    Integer countActivityLogsByUserIdAndDateIsBetween(Integer userId, String startDate, String endDate);

    Integer countActivityLogsByGroupIdAndDateIsBetween(Integer groupId, String startDate, String endDate);
}
