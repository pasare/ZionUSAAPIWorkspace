package org.zionusa.admin.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.VisitingTrip;
import org.zionusa.admin.domain.VisitingTripApprovalNotes;
import org.zionusa.admin.service.VisitingTripService;
import org.zionusa.admin.service.VisitingTripServiceWorkflow;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/visiting-trips")
public class VisitingTripController {

    private final VisitingTripService visitingTripService;
    private final VisitingTripServiceWorkflow visitingTripServiceWorkflow;

    @Autowired
    public VisitingTripController(VisitingTripService visitingTripService, VisitingTripServiceWorkflow visitingTripServiceWorkflow) {
        this.visitingTripService = visitingTripService;
        this.visitingTripServiceWorkflow = visitingTripServiceWorkflow;
    }

    @GetMapping()
    List<VisitingTrip> getAll() {
        return visitingTripService.getAll(null);
    }

    @GetMapping(value = "/{id}")
    VisitingTrip getById(@PathVariable Integer id) throws NotFoundException {
        return visitingTripService.getById(id);
    }

    @PostMapping()
    VisitingTrip save(@RequestBody VisitingTrip visitingTrip) {
        return visitingTripService.save(visitingTrip);
    }

    @PutMapping()
    VisitingTrip update(@RequestBody VisitingTrip visitingTrip) {
        return visitingTripService.save(visitingTrip);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable Integer id) throws NotFoundException {
        visitingTripService.delete(id);
    }

    @GetMapping(value = "/home-church/{id}")
    public List<VisitingTrip> getByHomeZionId(@PathVariable String id) {
        return visitingTripService.getByHomeZionId(id);
    }

    @GetMapping(value = "/home-churches/{ids}")
    public List<VisitingTrip> getByHomeZionIds(@PathVariable String ids) {
        return visitingTripService.getByHomeZionIds(ids);
    }

    @GetMapping(value = "/visiting-church/{id}")
    public List<VisitingTrip> getByVisitingZionId(@PathVariable String id) {
        return visitingTripService.getByVisitingZionId(id);
    }

    @GetMapping(value = "/visiting-churches/{ids}")
    public List<VisitingTrip> getByVisitingZionIds(@PathVariable String ids) {
        return visitingTripService.getByVisitingZionIds(ids);
    }

    @PutMapping(value = "/{id}/approve")
    VisitingTrip approve(HttpServletRequest request, @PathVariable Integer id, @RequestBody VisitingTripApprovalNotes visitingTripApprovalNotes) throws NotFoundException {
        VisitingTrip visitingTrip = visitingTripService.getById(id);
        return visitingTripServiceWorkflow.processApproval(request, visitingTrip, visitingTripApprovalNotes);
    }

    @PutMapping(value = "/{id}/deny")
    VisitingTrip deny(HttpServletRequest request, @PathVariable Integer id, @RequestBody VisitingTripApprovalNotes visitingTripApprovalNotes) throws NotFoundException {
        VisitingTrip visitingTrip = visitingTripService.getById(id);
        return visitingTripServiceWorkflow.processDenial(request, visitingTrip, visitingTripApprovalNotes);
    }

    @PostMapping(value = "/submit")
    VisitingTrip submit(HttpServletRequest request, @RequestBody VisitingTrip visitingTrip) {
        return visitingTripServiceWorkflow.processSubmission(request, visitingTrip);
    }
}
