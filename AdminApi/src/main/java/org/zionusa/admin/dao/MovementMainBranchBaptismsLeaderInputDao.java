package org.zionusa.admin.dao;

import org.zionusa.admin.domain.movement.Movement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementMainBranchBaptismsLeaderInputDao extends BaseDao<Movement.MainBranchBaptismsLeaderInput, Integer> {

    List<Movement.MainBranchBaptismsLeaderInput> getByDateBetween(String startDate, String endDate);

    Movement.MainBranchBaptismsLeaderInput getByMovementIdAndBranchIdAndDate(Integer movementId, Integer branchId, String date);

    List<Movement.MainBranchBaptismsLeaderInput> getAllByBranchIdAndMovementIdAndDateGreaterThanEqualAndDateLessThanEqual(Integer branchId, Integer movementId, String startDate, String endDate);
}
