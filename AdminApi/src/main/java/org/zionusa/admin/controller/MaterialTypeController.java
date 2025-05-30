package org.zionusa.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.MaterialType;
import org.zionusa.admin.service.MaterialTypeService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/material-types")
public class MaterialTypeController extends BaseController<MaterialType, Integer> {

    public MaterialTypeController(MaterialTypeService materialTypeService){
        super(materialTypeService);
    }
}
