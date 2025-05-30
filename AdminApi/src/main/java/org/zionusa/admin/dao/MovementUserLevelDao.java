package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementUserLevel;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementUserLevelDao extends BaseDao<MovementUserLevel, String> {
    List<MovementUserLevel> getMovementUserLevelByMovementId(Integer movementId);

    List<MovementUserLevel> getMovementUserLevelByMovementIdAndUserId(Integer movementId, Integer userId);

    List<MovementUserLevel> getTop300MovementUserLevelByMovementIdOrderByLevelDescTotalCountDesc(Integer movementId);
}
