package org.zionusa.biblestudy.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.Study;
import org.zionusa.biblestudy.service.StudyService;

import java.util.List;

@RestController
@RequestMapping("/studies")
public class StudyController extends BaseController<Study, Integer> {

    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        super(studyService);
        this.studyService = studyService;
    }

    @PostMapping(value = "/categories")
    @ApiOperation(value = "Get studies by one or many categories")
    public List<Study> getByCategories(@RequestBody List<Integer> categoryIds) {
        return this.studyService.getByCategories(categoryIds);
    }

}
