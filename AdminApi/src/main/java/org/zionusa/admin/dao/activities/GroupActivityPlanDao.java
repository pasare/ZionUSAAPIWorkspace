package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.GroupActivityPlan;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface GroupActivityPlanDao extends BaseDao<GroupActivityPlan, Integer> {

    List<GroupActivityPlan> findAllByGroupIdAndDateIsBetween(Integer groupId, String startDate, String endDate);

}
