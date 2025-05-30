package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoal;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGoalOverseerDao extends BaseDao<MovementGoal.Overseer, Integer> {

    List<MovementGoal.Overseer> getAllByMovementId(Integer movementId);

    MovementGoal.Overseer getAllByMovementIdAndReferenceId(Integer movementId, Integer referenceId);

}
