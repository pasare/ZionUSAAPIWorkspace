package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.ChurchType;
import org.zionusa.management.service.ChurchTypeService;

@Deprecated
@RestController
@RequestMapping("/church-types")
@Api(value = "Churches")
public class ChurchTypeController extends BaseController<ChurchType, Integer> {

    @Autowired
    public ChurchTypeController(ChurchTypeService churchTypeService) {
        super(churchTypeService);
    }
}
