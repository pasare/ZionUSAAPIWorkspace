package org.zionusa.event.domain.eventPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;


@Service
public class EventPresenterService extends BaseService<EventPresenter, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventPresenterService.class);

    @Autowired
    public EventPresenterService(EventPresenterDao dao) {
        super(dao, logger, EventPresenter.class);
    }
}
