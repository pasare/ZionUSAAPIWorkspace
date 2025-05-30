package org.zionusa.event.domain.eventTeam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.domain.EventManagementBranch;

import java.util.List;

@RestController
@RequestMapping("/event-teams")
public class EventTeamsController extends BaseController<EventTeam, Integer> {
    private final EventTeamsService eventTeamsService;

    public EventTeamsController(EventTeamsService eventTeamsService) {
        super(eventTeamsService);
        this.eventTeamsService = eventTeamsService;
    }

    @GetMapping(value = "/all-teams")
    public List<EventManagementBranch> getEventAllEventTeams() throws NotFoundException {
        return eventTeamsService.getAllEventManagementTeam();
    }

    @GetMapping(value = "/branch/{branchId}")
    public EventManagementBranch getEventTeamByBranch(@PathVariable Integer branchId) throws NotFoundException {
        return eventTeamsService.getEventTeamByBranch(branchId);
    }

    @GetMapping(value = "/event-proposal/{eventProposalId}")
    public EventManagementBranch getEventTeamByEventProposal(@PathVariable Integer eventProposalId) throws NotFoundException {
        return eventTeamsService.getEventTeamByEventProposal(eventProposalId);
    }
}
