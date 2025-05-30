package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.ReformerTypeDao;
import org.zionusa.biblestudy.domain.ReformerType;

@Service
public class ReformerTypeService extends BaseService<ReformerType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ReformerTypeService.class);

    @Autowired
    public ReformerTypeService(ReformerTypeDao reformerTypeDao) {
        super(reformerTypeDao, logger, ReformerType.class);
    }
}
