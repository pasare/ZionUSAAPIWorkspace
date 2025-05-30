package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.Location;
import org.zionusa.event.service.LocationsService;

@RestController
@RequestMapping("/locations")
public class LocationsController extends BaseController<Location, Integer> {

    public LocationsController(LocationsService locationsService) {
        super(locationsService);
    }
}
