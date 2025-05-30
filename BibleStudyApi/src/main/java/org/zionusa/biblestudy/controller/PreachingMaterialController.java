package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.PreachingMaterial;
import org.zionusa.biblestudy.service.PreachingMaterialService;

@RestController
@RequestMapping("/preaching-materials")
public class PreachingMaterialController extends BaseController<PreachingMaterial, Integer> {

    @Autowired
    public PreachingMaterialController(PreachingMaterialService preachingMaterialService) {
        super(preachingMaterialService);
    }
}
