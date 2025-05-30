package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoalResult;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGoalResultOverseerDao extends BaseDao<MovementGoalResult.Overseer, Integer> {

    List<MovementGoalResult.Overseer> getAllByMovementId(Integer movementId);

    MovementGoalResult.Overseer getAllByMovementIdAndReferenceId(Integer movementId, Integer referenceId);

}
