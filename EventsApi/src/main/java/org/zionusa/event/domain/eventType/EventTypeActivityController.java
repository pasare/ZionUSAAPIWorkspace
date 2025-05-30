package org.zionusa.event.domain.eventType;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.service.BaseService;

@RestController
@RequestMapping("/event-type-activity")
public class EventTypeActivityController extends BaseController<EventTypeActivity, Integer> {


    public EventTypeActivityController(BaseService<EventTypeActivity, Integer> service) {
        super(service);
    }
}
