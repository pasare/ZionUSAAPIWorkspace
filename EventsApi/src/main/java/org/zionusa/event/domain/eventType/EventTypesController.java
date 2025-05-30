package org.zionusa.event.domain.eventType;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.service.BaseService;

@RestController
@RequestMapping("/event-types")
public class EventTypesController extends BaseController<EventType, Integer> {

    public EventTypesController(EventTypesService eventTypesService) {
        super(eventTypesService);
    }
}

