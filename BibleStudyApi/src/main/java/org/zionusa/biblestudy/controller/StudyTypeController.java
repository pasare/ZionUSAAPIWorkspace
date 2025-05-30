package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.StudyType;
import org.zionusa.biblestudy.service.StudyTypeService;

@RestController
@RequestMapping("/study-types")
public class StudyTypeController extends BaseController<StudyType, Integer> {

    @Autowired
    public StudyTypeController(StudyTypeService studyTypeService) {
        super(studyTypeService);
    }
}
