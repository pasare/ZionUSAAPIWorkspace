package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.ApplicationDao;
import org.zionusa.admin.domain.Application;
import org.zionusa.base.service.BaseService;

@Service
public class ApplicationService extends BaseService<Application, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    public ApplicationService(ApplicationDao dao) {
        super(dao, logger, Application.class);
    }
}
