package org.zionusa.admin.domain.branch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;

@Service
public class BranchViewService extends BaseService<BranchView, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(BranchViewService.class);

    @Autowired
    BranchViewService(BranchViewDao dao) {
        super(dao, logger, BranchView.class);
    }
}
