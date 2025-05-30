package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.AnnouncementType;
import org.zionusa.admin.service.AnnouncementTypeService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/announcement-types")
public class AnnouncementTypeController extends BaseController<AnnouncementType, Integer> {

    @Autowired
    public AnnouncementTypeController(AnnouncementTypeService service) {
        super(service);
    }

}
