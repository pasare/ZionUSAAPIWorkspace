package org.zionusa.management.domain.specialDay;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface SpecialDayDao extends BaseDao<SpecialDay, Integer> {

    List<SpecialDay> findAllByDateBetween(String startDate, String endDate);

}
