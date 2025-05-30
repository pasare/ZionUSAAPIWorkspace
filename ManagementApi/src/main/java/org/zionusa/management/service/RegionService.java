package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.RegionDao;
import org.zionusa.management.dao.RegionsChurchesDao;
import org.zionusa.management.domain.Region;
import org.zionusa.management.domain.RegionsChurches;

import java.util.List;

@Service
public class RegionService extends BaseService<Region, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    private final RegionDao regionDao;

    private final RegionsChurchesDao regionsChurchesDao;

    @Autowired
    RegionService(RegionDao regionDao, RegionsChurchesDao regionsChurchesDao) {
        super(regionDao, logger, Region.class);
        this.regionDao = regionDao;
        this.regionsChurchesDao = regionsChurchesDao;
    }

    public List<Region> getAllByMovementId(Integer movementId) {
        return this.regionDao.getAllByMovementId(movementId);
    }

    public List<RegionsChurches> getAllByRegionsChurchesId(Integer id) {
        return this.regionsChurchesDao.getAllByRegionId(id);
    }

}
