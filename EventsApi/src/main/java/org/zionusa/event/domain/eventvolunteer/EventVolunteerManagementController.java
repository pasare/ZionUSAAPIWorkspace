package org.zionusa.event.domain.eventvolunteer;

import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.VolunteerType;

import java.util.List;

@RestController
@RequestMapping("/event-volunteer-management")
public class EventVolunteerManagementController extends BaseController<EventVolunteerManagement, Integer> {

    private final EventVolunteerManagementService eventVolunteerManagementService;

    public EventVolunteerManagementController(EventVolunteerManagementService eventVolunteerManagementService) {
        super(eventVolunteerManagementService);
        this.eventVolunteerManagementService = eventVolunteerManagementService;
    }

    @GetMapping(value = "/workflow-statuses")
    public VolunteerType[] getVolunteerTypes() {
        return VolunteerType.values();
    }

    @GetMapping(value = "/event-proposal/{eventProposalId}")
    public List<EventVolunteerManagement> getVolunteersByEventProposal(
        @PathVariable Integer eventProposalId,
        @RequestParam(required = false) Boolean isForResultsSurvey) {
        return eventVolunteerManagementService.getVolunteersByEventProposal(eventProposalId, isForResultsSurvey);
    }

    @GetMapping(value = "/event-proposal/totals/{eventProposalId}")
    public EventVolunteerManagementTotals getVolunteerTotalsByEventProposal(
        @PathVariable Integer eventProposalId,
        @RequestParam(required = false) Boolean isForResultsSurvey) {
        return eventVolunteerManagementService.getVolunteerTotalsByEventProposal(eventProposalId, isForResultsSurvey);
    }

    @GetMapping(value = "/results-survey/totals/{resultsSurveyId}")
    public EventVolunteerManagementTotals getVolunteerTotalsByResultsSurvey(
        @PathVariable Integer resultsSurveyId,
        @RequestParam(required = false) Boolean isForResultsSurvey) {
        return eventVolunteerManagementService.getVolunteerTotalsByResultsSurvey(resultsSurveyId, isForResultsSurvey);
    }
}
