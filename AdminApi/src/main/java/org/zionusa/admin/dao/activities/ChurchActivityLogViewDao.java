package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.ChurchActivityLogView;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ChurchActivityLogViewDao extends BaseDao<ChurchActivityLogView, Integer> {

    List<ChurchActivityLogView> findAllByPlanChurchIdAndPlanDateIsBetween(Integer churchId, String startDate, String endDate);

}
