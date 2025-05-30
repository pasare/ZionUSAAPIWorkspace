package org.zionusa.admin.controller.activities;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.admin.service.activities.ActivityCategoryService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/activity-categories")
@Api(value = "Activity Categories")
public class ActivityCategoryController extends BaseController<ActivityCategory, Integer> {

    @Autowired
    public ActivityCategoryController(ActivityCategoryService service) {
        super(service);
    }
}
