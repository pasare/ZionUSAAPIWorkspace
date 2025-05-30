package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.HelpDao;
import org.zionusa.event.domain.Help;

@Service
@CacheConfig(cacheNames = "helps-cache")
public class HelpService extends BaseService<Help, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(HelpService.class);

    @Autowired
    public HelpService(HelpDao helpDao) {
        super(helpDao, logger, Help.class);
    }
}
