package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Application;
import org.zionusa.admin.service.ApplicationService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/applications")
public class ApplicationController extends BaseController<Application, Integer> {

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        super(applicationService);
    }
}
