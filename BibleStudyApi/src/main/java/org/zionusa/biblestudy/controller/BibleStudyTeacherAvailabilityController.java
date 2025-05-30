package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.BibleStudyTeacherAvailability;
import org.zionusa.biblestudy.service.BibleStudyTeacherAvailabilityService;

@RestController
@RequestMapping("/bible-study-teacher-availability")
public class BibleStudyTeacherAvailabilityController extends BaseController<BibleStudyTeacherAvailability, Integer> {

    @Autowired
    public BibleStudyTeacherAvailabilityController(BibleStudyTeacherAvailabilityService bibleStudyTeacherAvailabilityService) {
        super(bibleStudyTeacherAvailabilityService);
    }

}
