package org.zionusa.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.UniversitySchoolCode;
import org.zionusa.event.service.UniversitySchoolCodeService;

@RestController
@RequestMapping("/university-code")
public class UniversitySchoolCodeController extends BaseController<UniversitySchoolCode, Integer> {

    @Autowired
    public UniversitySchoolCodeController(UniversitySchoolCodeService universitySchoolCodeService) {
        super(universitySchoolCodeService);
    }
}
