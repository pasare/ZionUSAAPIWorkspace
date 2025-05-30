package org.zionusa.biblestudy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.LREUserPamphlet;
import org.zionusa.biblestudy.service.LREUserPamphletService;

import java.util.List;

@RestController
@RequestMapping("/lre-user-pamphlets")
public class LREUserPamphletController extends BaseController<LREUserPamphlet, Integer> {

    private final LREUserPamphletService lreUserPamphletService;

    @Autowired
    public LREUserPamphletController(LREUserPamphletService lreUserPamphletService) {
        super(lreUserPamphletService);
        this.lreUserPamphletService = lreUserPamphletService;
    }

    @GetMapping(value = "/user/{userId}")
    public List<LREUserPamphlet> getAllLREUserPamphletByUser(@PathVariable Integer userId) {
        return lreUserPamphletService.getByUser(userId);
    }

    @GetMapping(value = "/church/{churchId}")
    public List<LREUserPamphlet> getAllLREUserPamphletByChurch(@PathVariable Integer churchId) {
        return lreUserPamphletService.getByChurch(churchId);
    }

    @GetMapping(value = "/parent-church/{parentChurchId}")
    public List<LREUserPamphlet> getAllLREUserPamphletByParentChurch(@PathVariable Integer parentChurchId) {
        return lreUserPamphletService.getByParentChurch(parentChurchId);
    }

    @GetMapping(value = "/barcode/{barcode}")
    public LREUserPamphlet getLREUserPamphletByBarcode(@PathVariable String barcode) {
        return lreUserPamphletService.getByBarcode(barcode);
    }
}
