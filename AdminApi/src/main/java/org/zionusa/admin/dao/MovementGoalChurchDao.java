package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoal;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGoalChurchDao extends BaseDao<MovementGoal.Church, Integer> {

    List<MovementGoal.Church> getAllByMovementId(Integer movementId);

    MovementGoal.Church getAllByMovementIdAndReferenceId(Integer movementId, Integer referenceId);

}
