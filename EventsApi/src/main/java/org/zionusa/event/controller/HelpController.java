package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.Help;
import org.zionusa.event.service.HelpService;

@RestController
@RequestMapping("/helps")
public class HelpController extends BaseController<Help, Integer> {

    public HelpController(HelpService helpService) {
        super(helpService);
    }
}
