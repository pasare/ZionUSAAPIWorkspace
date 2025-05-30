package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.EventFundraising;
import org.zionusa.event.service.EventFundraisingService;
import org.zionusa.event.service.WorkflowService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event-fundraising")
public class EventFundraisingController extends BaseController<EventFundraising, Integer> {

    private final EventFundraisingService eventFundraisingService;
    private final WorkflowService workflowService;

    public EventFundraisingController(EventFundraisingService eventFundraisingService, WorkflowService workflowService) {
        super(eventFundraisingService);
        this.eventFundraisingService = eventFundraisingService;
        this.workflowService = workflowService;
    }

    @PutMapping(value = "/{id}/approve")
    public EventFundraising approve(HttpServletRequest request, @PathVariable Integer id) {
        EventFundraising eventFundraising = eventFundraisingService.getById(id);
        return workflowService.processEventFundraisingApproval(request, eventFundraising);
    }

    @PutMapping(value = "/{id}/deny")
    public EventFundraising deny(HttpServletRequest request, @PathVariable Integer id) {
        EventFundraising eventFundraising = eventFundraisingService.getById(id);
        return workflowService.processEventFundraisingDenial(request, eventFundraising);
    }

    @Override
    @PostMapping(value = "/display")
    public List<Map<String, Object>> getAllDisplay(
        @RequestBody List<String> columns,
        @RequestParam(value = "archived", required = false) Boolean archived) {
        return eventFundraisingService.getAllDisplayProtected(columns, archived);
    }

//    @PostMapping(value = "/display/event/{eventProposalId}")
//    public List<Map<String, Object>> getAllEventFundraisingDisplayByEventProposalId(@PathVariable Integer eventProposalId, @RequestBody List<String> columns) {
//        return eventFundraisingService.getAllDisplayByEventId(eventProposalId, columns);
//    }

    @PostMapping(value = "/display/user/{userId}")
    public List<Map<String, Object>> getAllEventFundraisingDisplayByUserId(@PathVariable Integer userId, @RequestBody List<String> columns) {
        return eventFundraisingService.getAllDisplayByUserId(userId, columns);
    }

    @GetMapping(value = "/event/{eventProposalId}/user/{userId}")
    public EventFundraising getEventFundraisingByEventProposalIdAndUserId(@PathVariable Integer eventProposalId, @PathVariable Integer userId) {
        return eventFundraisingService.getByEventIdUserId(eventProposalId, userId);
    }
}
