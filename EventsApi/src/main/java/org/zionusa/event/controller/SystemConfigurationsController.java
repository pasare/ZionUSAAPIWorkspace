package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.SystemConfiguration;
import org.zionusa.event.service.SystemConfigurationsService;

import java.util.List;

@RestController
@RequestMapping("/system-configurations")
public class SystemConfigurationsController extends BaseController<SystemConfiguration, Integer> {

    private final SystemConfigurationsService systemConfigurationsService;

    public SystemConfigurationsController(SystemConfigurationsService systemConfigurationsService) {
        super(systemConfigurationsService);
        this.systemConfigurationsService = systemConfigurationsService;
    }

    @GetMapping(value = "/category/{category}")
    public List<SystemConfiguration> getByCategory(@PathVariable String category) {
        return systemConfigurationsService.getByCategory(category);
    }

}
