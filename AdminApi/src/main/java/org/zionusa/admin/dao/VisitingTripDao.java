package org.zionusa.admin.dao;

import org.zionusa.admin.domain.VisitingTrip;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface VisitingTripDao extends BaseDao<VisitingTrip, Integer> {
    List<VisitingTrip> getByHomeZionId(Integer id);
    List<VisitingTrip> getByVisitingZionId(Integer id);
}
