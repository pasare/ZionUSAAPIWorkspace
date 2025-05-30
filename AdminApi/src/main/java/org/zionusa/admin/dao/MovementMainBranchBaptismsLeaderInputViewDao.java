package org.zionusa.admin.dao;

import org.zionusa.admin.domain.movement.Movement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementMainBranchBaptismsLeaderInputViewDao extends BaseDao<Movement.MainBranchBaptismsLeaderInputView,
    Integer> {

    List<Movement.MainBranchBaptismsLeaderInputView> getAllByMovementId(Integer movementId);

    Movement.MainBranchBaptismsLeaderInputView getByMovementIdAndBranchId(Integer movementId, Integer branchId);
}
