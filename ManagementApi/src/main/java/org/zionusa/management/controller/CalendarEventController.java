package org.zionusa.management.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.CalendarEvent;
import org.zionusa.management.domain.CalendarEventCreateRequest;
import org.zionusa.management.service.CalendarEventService;

@RestController
@RequestMapping("/calendar-event")
@Api(value = "Calendar Events")
public class CalendarEventController extends BaseController<CalendarEvent, Integer> {

    private final CalendarEventService calendarEventService;

    @Autowired
    public CalendarEventController(CalendarEventService calendarEventService) {
        super(calendarEventService);
        this.calendarEventService = calendarEventService;
    }

    @PostMapping(value = "/microsoft")
    @ApiOperation(value = "Create a new event in O365")
    public CalendarEvent createOneForOffice365(@RequestBody CalendarEventCreateRequest calendarEventCreateRequest) {
        return calendarEventService.createOneForOffice365(calendarEventCreateRequest);
    }

}
