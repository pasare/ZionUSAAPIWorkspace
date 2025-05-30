package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.StudyTypeDao;
import org.zionusa.biblestudy.domain.StudyType;

@Service
public class StudyTypeService extends BaseService<StudyType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(StudyTypeService.class);

    @Autowired
    public StudyTypeService(StudyTypeDao studyTypeDao) {
        super(studyTypeDao, logger, StudyType.class);
    }
}
