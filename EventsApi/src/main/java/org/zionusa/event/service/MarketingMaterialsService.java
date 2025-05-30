package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.MarketingMaterialsDao;
import org.zionusa.event.domain.MarketingMaterial;

@Service
public class MarketingMaterialsService extends BaseService<MarketingMaterial, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MarketingMaterialsService.class);

    @Autowired
    public MarketingMaterialsService(MarketingMaterialsDao marketingMaterialsDao) {
        super(marketingMaterialsDao, logger, MarketingMaterial.class);
    }
}
