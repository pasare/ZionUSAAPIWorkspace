package org.zionusa.admin.dao;

import org.zionusa.admin.domain.movement.Movement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementBranchBaptismsLeaderInputViewDao extends BaseDao<Movement.BranchBaptismsLeaderInputView, Integer> {

    List<Movement.BranchBaptismsLeaderInputView> getAllByMovementId(Integer movementId);

    Movement.BranchBaptismsLeaderInputView getByMovementIdAndBranchId(Integer movementId, Integer branchId);
}
