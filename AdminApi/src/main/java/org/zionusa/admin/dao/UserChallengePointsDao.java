package org.zionusa.admin.dao;

import org.zionusa.admin.domain.UserMonthlyChallengePoints;
import org.zionusa.base.dao.BaseDao;

public interface UserChallengePointsDao extends BaseDao<UserMonthlyChallengePoints, Integer> {

    UserMonthlyChallengePoints getUserMonthlyChallengePointsByUserIdAndMonthAndYear(int userId, int month, int year);
}
