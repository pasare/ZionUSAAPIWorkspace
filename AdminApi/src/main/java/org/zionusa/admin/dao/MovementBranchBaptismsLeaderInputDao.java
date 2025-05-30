package org.zionusa.admin.dao;

import org.zionusa.admin.domain.movement.Movement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementBranchBaptismsLeaderInputDao extends BaseDao<Movement.BranchBaptismsLeaderInput, Integer> {

    List<Movement.BranchBaptismsLeaderInput> getByDateBetween(String startDate, String endDate);

    Movement.BranchBaptismsLeaderInput getByMovementIdAndBranchIdAndDate(Integer movementId, Integer branchId, String date);

    List<Movement.BranchBaptismsLeaderInput> getAllByBranchIdAndMovementIdAndDateGreaterThanEqualAndDateLessThanEqual(Integer branchId, Integer movementId, String startDate, String endDate);
}
