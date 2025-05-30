package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.LREPamphlet;
import org.zionusa.biblestudy.service.LREPamphletService;

@RestController
@RequestMapping("/lre-pamphlets")
public class LREPamphletController extends BaseController<LREPamphlet, Integer> {

    @Autowired
    public LREPamphletController(LREPamphletService lrePamphletService) {
        super(lrePamphletService);
    }
}
