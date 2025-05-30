package org.zionusa.admin.service.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.activities.ActivityShareTypeDao;
import org.zionusa.admin.domain.activities.ActivityShareType;
import org.zionusa.base.service.BaseService;

@Service
public class ActivityShareTypeService extends BaseService<ActivityShareType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ActivityShareTypeService.class);

    @Autowired
    public ActivityShareTypeService(ActivityShareTypeDao ActivityShareTypeDao) {
        super(ActivityShareTypeDao, logger, ActivityShareType.class);
    }

}
