package org.zionusa.event.domain.eventRegistration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/event-registrations")
public class EventRegistrationsController extends BaseController<EventRegistration, Integer> {

    private final EventRegistrationsService eventRegistrationsService;

    public EventRegistrationsController(EventRegistrationsService eventRegistrationsService) {
        super(eventRegistrationsService);
        this.eventRegistrationsService = eventRegistrationsService;
    }

    @GetMapping(value = "/event/{id}")
    public List<EventRegistration> getByEventId(@PathVariable Integer id) {
        return eventRegistrationsService.getByEventId(id);
    }

    @GetMapping(value = "/user/{id}")
    public List<EventRegistration> getByUserId(@PathVariable Integer id) {
        return eventRegistrationsService.getByUserId(id);
    }
}
