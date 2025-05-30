package org.zionusa.admin.controller.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.activities.ActivityShareType;
import org.zionusa.admin.service.activities.ActivityShareTypeService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/activity-share-types")
public class ActivityShareTypeController extends BaseController<ActivityShareType, Integer> {

    @Autowired
    public ActivityShareTypeController(ActivityShareTypeService service) {
        super(service);
    }
}
