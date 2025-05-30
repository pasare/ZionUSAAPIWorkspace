package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.BibleStudyProgressDao;
import org.zionusa.biblestudy.domain.BibleStudyProgress;

@Service
public class BibleStudyProgressService extends BaseService<BibleStudyProgress, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(BibleStudyProgressService.class);

    @Autowired
    public BibleStudyProgressService(BibleStudyProgressDao bibleStudyProgressDao) {
        super(bibleStudyProgressDao, logger, BibleStudyProgress.class);
    }
}
