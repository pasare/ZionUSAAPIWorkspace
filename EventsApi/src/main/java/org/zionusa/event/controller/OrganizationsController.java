package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.Organization;
import org.zionusa.event.service.OrganizationsService;

@RestController
@RequestMapping("/organizations")
public class OrganizationsController extends BaseController<Organization, Integer> {

    public OrganizationsController(OrganizationsService organizationsService) {
        super(organizationsService);
    }

}
