package org.zionusa.admin.service.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.activities.ActivityCategoryDao;
import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.base.service.BaseService;

@Service
public class ActivityCategoryService extends BaseService<ActivityCategory, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(ActivityCategoryService.class);

    @Autowired
    public ActivityCategoryService(ActivityCategoryDao dao) {
        super(dao, logger, ActivityCategory.class);
    }
}
