package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.Region;

import java.util.List;

public interface RegionDao extends BaseDao<Region, Integer> {

    List<Region> getAllByMovementId(Integer movementId);
}
