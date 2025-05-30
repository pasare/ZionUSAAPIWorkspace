package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoalResult;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGoalResultChurchDao extends BaseDao<MovementGoalResult.Church, Integer> {

    List<MovementGoalResult.Church> getAllByMovementId(Integer movementId);

    MovementGoalResult.Church getAllByMovementIdAndReferenceId(Integer movementId, Integer referenceId);

}
