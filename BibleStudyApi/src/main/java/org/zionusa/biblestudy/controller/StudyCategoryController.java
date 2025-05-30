package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.StudyCategory;
import org.zionusa.biblestudy.service.StudyCategoryService;

@RestController
@RequestMapping("/study-categories")
public class StudyCategoryController extends BaseController<StudyCategory, Integer> {

    @Autowired
    public StudyCategoryController(StudyCategoryService studyCategoryService) {
        super(studyCategoryService);
    }
}
