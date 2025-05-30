package org.zionusa.admin.dao;

import org.zionusa.admin.domain.movement.Movement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGroupBaptismsLeaderInputDao extends BaseDao<Movement.GroupBaptismsLeaderInput, Integer>  {
    List<Movement.GroupBaptismsLeaderInput> getByDateBetween(String startDate, String endDate);

    Movement.GroupBaptismsLeaderInput getByMovementIdAndGroupIdAndDate(Integer movementId, Integer groupId,
                                                                      String date);

}
