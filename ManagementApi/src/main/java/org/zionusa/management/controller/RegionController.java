package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.Region;
import org.zionusa.management.domain.RegionsChurches;
import org.zionusa.management.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/regions")
@Api(value = "Regions")
public class RegionController extends BaseController<Region, Integer> {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        super(regionService);
        this.regionService = regionService;
    }

    @GetMapping(value = "regions/{movementId}")
    @ApiOperation(value = "View a list of regions", response = List.class)
    public List<Region> getAllByMovementId(@PathVariable Integer movementId) {
        return regionService.getAllByMovementId(movementId);
    }

    @GetMapping(value = "{id}/churches")
    @ApiOperation(value = "View a list of churches belonging to a region", response = List.class)
    public List<RegionsChurches> getAllByRegionsChurchesId(@PathVariable Integer id) {
        return regionService.getAllByRegionsChurchesId(id);
    }

}
