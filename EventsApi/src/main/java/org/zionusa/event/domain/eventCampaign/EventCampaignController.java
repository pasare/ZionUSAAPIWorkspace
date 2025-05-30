package org.zionusa.event.domain.eventCampaign;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.eventType.EventType;
import org.zionusa.event.domain.eventType.EventTypesService;

@RestController
@RequestMapping("/event-campaign")
public class EventCampaignController extends BaseController<EventCampaign, Integer> {

    public EventCampaignController(EventCampaignService eventCampaignService) {
        super(eventCampaignService);
    }
}

