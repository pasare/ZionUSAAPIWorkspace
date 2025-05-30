package org.zionusa.admin.domain.specialdays;

import org.zionusa.admin.domain.specialdays.SpecialDay;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface SpecialDayDao extends BaseDao<SpecialDay, Integer> {

    List<SpecialDay> findAllByDateBetween(String startDate, String endDate);

}
