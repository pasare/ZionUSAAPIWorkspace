package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.SubjectDao;
import org.zionusa.admin.domain.Subject;
import org.zionusa.base.service.BaseService;

@Service
public class SubjectService extends BaseService<Subject, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementService.class);

    @Autowired
    public SubjectService(SubjectDao subjectDao) {
        super(subjectDao, logger, Subject.class);
    }


}
