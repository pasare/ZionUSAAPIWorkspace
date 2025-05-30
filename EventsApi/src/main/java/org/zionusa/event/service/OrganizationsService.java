package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.OrganizationsDao;
import org.zionusa.event.domain.Organization;

@Service
@CacheConfig(cacheNames = "organizations")
public class OrganizationsService extends BaseService<Organization, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationsService.class);

    @Autowired
    public OrganizationsService(OrganizationsDao organizationsDao) {
        super(organizationsDao, logger, Organization.class);
    }
}
