package org.zionusa.event.domain.eventMarketing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;

@Service
public class EventMarketingService extends BaseService<EventMarketing, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventMarketingService.class);

    @Autowired
    public EventMarketingService(EventMarketingDao eventMarketingDao) {
        super(eventMarketingDao, logger, EventMarketing.class);
    }
}
