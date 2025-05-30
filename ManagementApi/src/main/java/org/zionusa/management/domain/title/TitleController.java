package org.zionusa.management.domain.title;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/titles")
public class TitleController extends BaseController<Title, Integer> {
    @Autowired
    public TitleController(TitleService titleService) {
        super(titleService);
    }
}
