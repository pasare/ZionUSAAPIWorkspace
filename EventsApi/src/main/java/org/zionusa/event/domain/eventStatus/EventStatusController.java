package org.zionusa.event.domain.eventStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.WorkflowStatus;

@RestController
@RequestMapping("/event-statuses")
public class EventStatusController extends BaseController<EventStatus, Integer> {

    @Autowired
    public EventStatusController(EventStatusService eventStatusService) {
        super(eventStatusService);
    }

    @GetMapping(value = "/workflow-statuses")
    public WorkflowStatus[] getWorkflowStatuses() {
        return WorkflowStatus.values();
    }
}
