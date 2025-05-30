package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGoal;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGoalAssociationDao extends BaseDao<MovementGoal.Association, Integer> {

    List<MovementGoal.Association> getAllByMovementId(Integer movementId);

    MovementGoal.Association getAllByMovementIdAndReferenceId(Integer movementId, Integer referenceId);

}
