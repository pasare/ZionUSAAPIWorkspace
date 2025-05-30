package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.BibleStudyProgress;
import org.zionusa.biblestudy.service.BibleStudyProgressService;

@RestController
@RequestMapping("/bible-study-progress")
public class BibleStudyProgressController extends BaseController<BibleStudyProgress, Integer> {

    @Autowired
    public BibleStudyProgressController(BibleStudyProgressService bibleStudyProgressService) {
        super(bibleStudyProgressService);
    }

}
