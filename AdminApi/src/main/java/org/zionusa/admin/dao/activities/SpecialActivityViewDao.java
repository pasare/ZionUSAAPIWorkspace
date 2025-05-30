package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.SpecialActivityView;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface SpecialActivityViewDao extends BaseDao<SpecialActivityView, Integer> {

    List<SpecialActivityView> findAllByDateBetween(String startDate, String endDate);

}
