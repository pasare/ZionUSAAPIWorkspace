package org.zionusa.event.domain.eventAttendance;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/event-attendances")
public class EventAttendancesController extends BaseController<EventAttendance, Integer> {

    public EventAttendancesController(EventAttendanceService eventAttendanceService) {
        super(eventAttendanceService);
    }
}
