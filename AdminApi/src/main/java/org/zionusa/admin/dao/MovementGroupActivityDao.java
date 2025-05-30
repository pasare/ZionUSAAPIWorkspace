package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementGroupActivity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementGroupActivityDao extends BaseDao<MovementGroupActivity, String> {

    List<MovementGroupActivity> getMovementGroupActivitiesByMovementIdAndGroupId(Integer movementId, Integer groupId);
}
