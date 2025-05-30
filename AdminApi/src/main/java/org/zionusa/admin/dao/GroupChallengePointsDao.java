package org.zionusa.admin.dao;

import org.zionusa.admin.domain.GroupMonthlyChallengePoints;
import org.zionusa.base.dao.BaseDao;

public interface GroupChallengePointsDao extends BaseDao<GroupMonthlyChallengePoints, Integer> {

    GroupMonthlyChallengePoints getUserMonthlyChallengePointsByGroupIdAndMonthAndYear(int groupId, int month, int year);
}
