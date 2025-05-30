package org.zionusa.management.domain.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseViewService;

@Service
public class StateViewService extends BaseViewService<StateView, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(StateViewService.class);

    @Autowired
    StateViewService(StateViewDao dao) {
        super(dao, logger);
    }
}
