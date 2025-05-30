package org.zionusa.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.ShortTermPreaching;
import org.zionusa.management.service.ShortTermPreachingService;

@RestController
@RequestMapping("/short-term-preaching")
public class ShortTermPreachingController extends BaseController<ShortTermPreaching, Integer> {

    @Autowired
    public ShortTermPreachingController(ShortTermPreachingService shortTermPreachingService) {
        super(shortTermPreachingService);
    }
}
