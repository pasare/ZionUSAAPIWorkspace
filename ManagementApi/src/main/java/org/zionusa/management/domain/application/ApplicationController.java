package org.zionusa.management.domain.application;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/applications")
@Api(value = "Applications")
public class ApplicationController extends BaseController<Application, Integer> {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        super(applicationService);
        this.applicationService = applicationService;
    }

    @PutMapping(value = "/{id}/disable")
    @ApiOperation(value = "Disable an application from receiving data via the API")
    public void disableApplication(@PathVariable Integer id) {
        applicationService.enable(id, false);
    }

    @PutMapping(value = "/{id}/enable")
    @ApiOperation(value = "Enable an application to receive data via the API")
    public void enableApplication(@PathVariable Integer id) {
        applicationService.enable(id, true);
    }

    @PutMapping(value = "/{xApplicationId}/latest-version")
    @ApiOperation(value = "Get latest application version")
    public String getLatestVersion(@PathVariable String xApplicationId) {
        return applicationService.getLatestVersion(xApplicationId);
    }
}
