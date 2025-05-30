package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementChurchDateActivity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MovementChurchDateActivityDao extends BaseDao<MovementChurchDateActivity, String> {

    List<MovementChurchDateActivity> getMovementChurchDateActivitiesByMovementIdAndChurchIdAndActivityDateBetween(Integer movementId, Integer churchId, String startDate, String endDate);
}
