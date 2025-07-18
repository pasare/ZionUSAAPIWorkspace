package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.SermonDao;
import org.zionusa.admin.domain.Sermon;
import org.zionusa.base.service.BaseService;

@Service
public class SermonService extends BaseService<Sermon, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SermonService.class);

    @Autowired
    public SermonService(SermonDao sermonDao) {
        super(sermonDao, logger, Sermon.class);
    }

}
