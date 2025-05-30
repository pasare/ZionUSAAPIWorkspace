package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementChurchActivity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementChurchActivityDao extends BaseDao<MovementChurchActivity, String> {

    List<MovementChurchActivity> getMovementChurchActivitiesByMovementIdAndChurchId(Integer movementId, Integer churchId);
}
