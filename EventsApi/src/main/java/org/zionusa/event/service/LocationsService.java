package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.LocationsDao;
import org.zionusa.event.domain.Location;

@Service
public class LocationsService extends BaseService<Location, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(LocationsService.class);

    @Autowired
    public LocationsService(LocationsDao locationsDao) {
        super(locationsDao, logger, Location.class);
    }
}
