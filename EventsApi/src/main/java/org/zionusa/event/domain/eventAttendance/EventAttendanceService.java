package org.zionusa.event.domain.eventAttendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;

@Service
public class EventAttendanceService extends BaseService<EventAttendance, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventAttendanceService.class);

    @Autowired
    public EventAttendanceService(EventAttendanceDao eventAttendanceDao) {
        super(eventAttendanceDao, logger, EventAttendance.class);
    }
}
