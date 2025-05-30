package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.MarketingMaterial;
import org.zionusa.event.service.MarketingMaterialsService;

@RestController
@RequestMapping("/marketing-materials")
public class MarketingMaterialsController extends BaseController<MarketingMaterial, Integer> {

    public MarketingMaterialsController(MarketingMaterialsService marketingMaterialsService) {
        super(marketingMaterialsService);
    }

}
