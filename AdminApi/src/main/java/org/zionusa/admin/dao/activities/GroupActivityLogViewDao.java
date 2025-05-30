package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.GroupActivityLogView;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface GroupActivityLogViewDao extends BaseDao<GroupActivityLogView, Integer> {

    List<GroupActivityLogView> findAllByPlanGroupIdAndPlanDateIsBetween(Integer groupId, String startDate, String endDate);
}
