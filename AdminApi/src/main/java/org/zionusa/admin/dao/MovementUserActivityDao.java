package org.zionusa.admin.dao;

import org.springframework.data.jpa.repository.Query;
import org.zionusa.admin.domain.MovementUserActivity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementUserActivityDao extends BaseDao<MovementUserActivity, String> {

    List<MovementUserActivity> getMovementUserActivitiesByMovementIdAndUserId(Integer movementId, Integer userId);

    @Query("SELECT count(DISTINCT userId) FROM MovementUserActivity where startDate = ?1 and endDate = ?2")
    int countDistinctByUserIdAndStartDateEqualsAndEndDateEquals(String startDate, String endDate);
}
