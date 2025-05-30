package org.zionusa.event.domain.eventCategory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.eventCategory.EventCategory;
import org.zionusa.event.domain.eventCategory.EventCategoryService;

@RestController
@RequestMapping("/event-categories")
public class EventCategoryController extends BaseController<EventCategory, Integer> {

    public EventCategoryController(EventCategoryService eventCategoryService) {
        super(eventCategoryService);
    }
}
