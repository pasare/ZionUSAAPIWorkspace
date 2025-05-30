package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoalResult;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGoalResultAssociationDao extends BaseDao<MovementGoalResult.Association, Integer> {

    List<MovementGoalResult.Association> getAllByMovementId(Integer movementId);

    MovementGoalResult.Association getAllByMovementIdAndReferenceId(Integer movementId, Integer referenceId);

}
