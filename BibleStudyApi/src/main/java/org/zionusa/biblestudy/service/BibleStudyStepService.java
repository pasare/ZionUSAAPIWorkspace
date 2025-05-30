package org.zionusa.biblestudy.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.BibleStudyStepDao;
import org.zionusa.biblestudy.domain.BibleStudyStep;

@Service
public class BibleStudyStepService extends BaseService<BibleStudyStep, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(BibleStudyStepService.class);

    @Autowired
    public BibleStudyStepService(BibleStudyStepDao bibleStudyStepDao) {
        super(bibleStudyStepDao, logger, BibleStudyStep.class);

    }
}
