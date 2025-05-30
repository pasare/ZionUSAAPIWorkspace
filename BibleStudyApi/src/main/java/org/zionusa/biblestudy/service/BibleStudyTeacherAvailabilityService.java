package org.zionusa.biblestudy.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.BibleStudyTeacherAvailabilityDao;
import org.zionusa.biblestudy.domain.BibleStudyTeacherAvailability;

@Service
public class BibleStudyTeacherAvailabilityService extends BaseService<BibleStudyTeacherAvailability, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(BibleStudyTeacherAvailabilityService.class);

    @Autowired
    public BibleStudyTeacherAvailabilityService(BibleStudyTeacherAvailabilityDao bibleStudyTeacherAvailabilityDao) {
        super(bibleStudyTeacherAvailabilityDao, logger, BibleStudyTeacherAvailability.class);

    }

    public BibleStudyTeacherAvailability save(BibleStudyTeacherAvailability bibleStudyTeacherAvailability,
                                              BibleStudyTeacherAvailabilityDao bibleStudyTeacherAvailabilityDao) {

        return bibleStudyTeacherAvailabilityDao.save(bibleStudyTeacherAvailability);
    }
}
