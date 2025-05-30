package org.zionusa.event.domain.eventProposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.EventProposalDisplay;
import org.zionusa.event.domain.EventProposalTableView;
import org.zionusa.event.domain.EventStatusNotes;
import org.zionusa.event.service.AzureBlobStorageService;
import org.zionusa.event.service.EventFilesService;
import org.zionusa.event.service.WorkflowService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event-proposals")
public class EventProposalsController extends BaseController<EventProposal, Integer> {

    private final EventProposalsService eventProposalsService;
    private final WorkflowService workflowService;
    private final AzureBlobStorageService azureBlobStorageService;
    private final EventFilesService eventFilesService;

    public EventProposalsController(EventProposalsService eventProposalsService, WorkflowService workflowService, AzureBlobStorageService azureBlobStorageService, EventFilesService eventFilesService) {
        super(eventProposalsService);
        this.eventProposalsService = eventProposalsService;
        this.workflowService = workflowService;
        this.azureBlobStorageService = azureBlobStorageService;
        this.eventFilesService = eventFilesService;
    }

    @GetMapping(value = "/display")
    public List<EventProposalDisplay> getAllDisplayPreset() {
        return eventProposalsService.getAllDisplayPreset();
    }

    @PutMapping(value = "/{id}/display")
    public EventProposalDisplay getDisplayById(@PathVariable Integer id) {
        return eventProposalsService.getDisplayById(id);
    }

    @PutMapping(value = "/{id}/finalize")
    public EventProposal finalizeById(HttpServletRequest request, @PathVariable Integer id) throws NotFoundException {
        EventProposal finalizedEvent = eventProposalsService.finalizeById(id);
        EventStatusNotes eventStatusNotes = new EventStatusNotes();
        return workflowService.processApproval(request, finalizedEvent, eventStatusNotes);
    }

    @PutMapping(value = "/{id}/approve")
    public EventProposal approveById(HttpServletRequest request, @PathVariable Integer id, @RequestBody EventStatusNotes eventStatusNotes) throws NotFoundException {
        EventProposal eventProposal = eventProposalsService.getById(id);
        return workflowService.processApproval(request, eventProposal, eventStatusNotes);
    }

    @PutMapping(value = "/{id}/deny")
    public EventProposal denyById(HttpServletRequest request, @PathVariable Integer id, @RequestBody EventStatusNotes eventStatusNotes) {
        EventProposal eventProposal = eventProposalsService.getById(id);
        return workflowService.processDenial(request, eventProposal, eventStatusNotes);
    }

    @PutMapping(value = "/{id}/archive")
    public EventProposal archiveById(@PathVariable Integer id) {
        eventProposalsService.archive(id);
        return eventProposalsService.getById(id);
    }

    @PutMapping("/{id}/publish")
    @ApiOperation(value = "Publish an event proposal so members can view it")
    public void publishEventProposal(@PathVariable Integer id) {
        eventProposalsService.publishEventProposal(id);
    }

    @PutMapping("/{id}/un-publish")
    @ApiOperation(value = "Un publish an event proposal, hiding it from the view of members")
    public void unPublishEventProposal(@PathVariable Integer id) {
        eventProposalsService.unPublishEventProposal(id);
    }

    @PostMapping(value = "/calendar/{id}/invite")
    public void createEventProposalCalendarInvite(HttpServletRequest request, @PathVariable Integer id) throws JsonProcessingException {
        eventProposalsService.createEventProposalCalendarInvite(request, id);
    }

    @GetMapping(value = "/upcoming")
    @ApiOperation(value = "View upcoming events that are published and not private")
    public List<EventProposalDisplay> getAllUpcoming() {
        return eventProposalsService.getAllUpcoming();
    }

    @GetMapping(value = "/upcoming/{eventProposalId}")
    @ApiOperation(value = "View upcoming event that is published and not private")
    public EventProposalDisplay getOneUpcoming(@PathVariable Integer eventProposalId) {
        return eventProposalsService.getOneUpcoming(eventProposalId);
    }

    @PostMapping(value = "/upcoming")
    @ApiOperation(value = "View upcoming events that are published and not private")
    public List<Map<String, Object>> getAllUpcoming(List<String> columns) {
        return eventProposalsService.getAllUpcoming(columns);
    }

    @PostMapping(value = "/upload")
    public String uploadEventImage(@RequestParam("userId") Integer userId,
                                   @RequestParam("eventProposalId") Integer eventProposalId,
                                   @RequestParam("name") String name,
                                   @RequestParam("type") String type,
                                   @RequestParam(value = "file") MultipartFile file) {
        String url = azureBlobStorageService.uploadEventFile(userId, eventProposalId, file, name, type);

        if (!type.toLowerCase().replace("\\s", "-").equals("additional-image")) {
            eventFilesService.saveEventFileFromEventProposalUpload(eventProposalId, url, name, type);
        }
        return url;
    }

    @GetMapping(value = "/table")
    public List<EventProposalTableView> getEventProposalTableViewBy() {
        return eventProposalsService.getEventProposalTableViewBy();
    }

    @GetMapping(value = "/table/{branchId}")
    public List<EventProposalTableView> getEventProposalTableViewByBranchId(@PathVariable Integer branchId) {
        return eventProposalsService.getEventProposalTableViewByBranchId(branchId);
    }

    @PostMapping(value = "/vips/{eventProposalId}")
    public List<Map<String, Object>> getAllVipsByEventProposalId(@PathVariable Integer eventProposalId, @RequestBody List<String> columns) {
        return eventProposalsService.getAllVipsByEventProposalId(eventProposalId, columns);
    }

    @DeleteMapping(value = "/vips/{eventProposalId}/contact/{contactId}")
    public void deleteVipToEventProposal(@PathVariable Integer eventProposalId, @PathVariable Integer contactId) {
        eventProposalsService.deleteVipToEventProposal(eventProposalId, contactId);
    }

    @PutMapping(value = "/vips/{eventProposalId}/contact/{contactId}")
    public void postVipToEventProposal(@PathVariable Integer eventProposalId, @PathVariable Integer contactId) {
        eventProposalsService.postVipToEventProposal(eventProposalId, contactId);
    }
}
