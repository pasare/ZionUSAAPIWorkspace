package org.zionusa.event.domain.eventMarketing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/event-marketing")
public class EventMarketingController extends BaseController<EventMarketing, Integer> {

    public EventMarketingController(EventMarketingService eventMarketingService) {
        super(eventMarketingService);
    }
}
