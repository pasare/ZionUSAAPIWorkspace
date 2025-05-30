package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementUserLevelCount;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementUserLevelCountDao extends BaseDao<MovementUserLevelCount, String> {
    List<MovementUserLevelCount> getMovementUserLevelByMovementId(Integer movementId);
}
