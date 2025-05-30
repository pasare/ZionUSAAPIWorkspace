package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.ChurchActivityPlan;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ChurchActivityPlanDao extends BaseDao<ChurchActivityPlan, Integer> {

    List<ChurchActivityPlan> findAllByChurchIdAndDateIsBetween(Integer churchId, String startDate, String endDate);

}
