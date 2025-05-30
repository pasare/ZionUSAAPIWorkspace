package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoal;
import org.zionusa.base.dao.BaseDao;

public interface MovementGoalShortTermPreachingDao extends BaseDao<MovementGoal.ShortTermPreaching, Integer> {

    MovementGoal.ShortTermPreaching getByReferenceId(Integer referenceId);

}
