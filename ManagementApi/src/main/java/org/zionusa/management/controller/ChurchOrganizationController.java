package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.ChurchOrganization;
import org.zionusa.management.service.ChurchOrganizationService;

@RestController
@RequestMapping("/church-organizations")
@Api(value = "Churches")
public class ChurchOrganizationController extends BaseController<ChurchOrganization, Integer> {

    @Autowired
    public ChurchOrganizationController(ChurchOrganizationService churchOrganizationService) {
        super(churchOrganizationService);
    }

}
