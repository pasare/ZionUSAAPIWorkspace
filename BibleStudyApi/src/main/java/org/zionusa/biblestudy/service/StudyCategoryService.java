package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.StudyCategoryDao;
import org.zionusa.biblestudy.domain.StudyCategory;

@Service
public class StudyCategoryService extends BaseService<StudyCategory, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(StudyCategoryService.class);

    @Autowired
    public StudyCategoryService(StudyCategoryDao studyCategoryDao) {
        super(studyCategoryDao, logger, StudyCategory.class);
    }
}
