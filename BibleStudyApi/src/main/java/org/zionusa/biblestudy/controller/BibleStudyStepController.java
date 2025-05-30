package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.BibleStudyStep;
import org.zionusa.biblestudy.service.BibleStudyStepService;

@RestController
@RequestMapping("/bible-study-step")
public class BibleStudyStepController extends BaseController<BibleStudyStep, Integer> {

    @Autowired
    public BibleStudyStepController(BibleStudyStepService bibleStudyStepService) {
        super(bibleStudyStepService);
    }

}
