package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementEastCoastActivity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementEastCoastActivityDao extends BaseDao<MovementEastCoastActivity, String> {

    List<MovementEastCoastActivity> getMovementEastCoastActivitiesByMovementId(Integer movementId);
}
