package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.UniversitySchoolCodeDao;
import org.zionusa.event.domain.UniversitySchoolCode;

@Service
public class UniversitySchoolCodeService extends BaseService<UniversitySchoolCode, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(UniversitySchoolCodeService.class);

    @Autowired
    public UniversitySchoolCodeService(UniversitySchoolCodeDao universitySchoolCodeDao) {
        super(universitySchoolCodeDao, logger, UniversitySchoolCode.class);
    }
}
