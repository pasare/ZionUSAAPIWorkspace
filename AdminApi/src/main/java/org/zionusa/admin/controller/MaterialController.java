package org.zionusa.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Material;
import org.zionusa.admin.service.MaterialService;
import org.zionusa.base.controller.BaseController;


@RestController
@RequestMapping("/materials")
public class MaterialController extends BaseController<Material, Integer> {

    public MaterialController(MaterialService materialService) {
        super(materialService);
    }
}
