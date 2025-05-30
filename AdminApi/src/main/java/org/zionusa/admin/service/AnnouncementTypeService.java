package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.AnnouncementTypeDao;
import org.zionusa.admin.domain.AnnouncementType;
import org.zionusa.base.service.BaseService;

@Service
public class AnnouncementTypeService extends BaseService<AnnouncementType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementTypeService.class);

    public AnnouncementTypeService(AnnouncementTypeDao dao) {
        super(dao, logger, AnnouncementType.class);
    }
}
