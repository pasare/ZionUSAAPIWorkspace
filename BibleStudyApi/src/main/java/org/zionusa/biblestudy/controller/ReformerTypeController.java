package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.ReformerType;
import org.zionusa.biblestudy.service.ReformerTypeService;

@RestController
@RequestMapping("/reformer-types")
public class ReformerTypeController extends BaseController<ReformerType, Integer> {

    @Autowired
    public ReformerTypeController(ReformerTypeService reformerTypeService) {
        super(reformerTypeService);
    }
}
