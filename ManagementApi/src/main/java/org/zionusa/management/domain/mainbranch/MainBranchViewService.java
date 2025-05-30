package org.zionusa.management.domain.mainbranch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseViewService;

@Service
public class MainBranchViewService extends BaseViewService<MainBranchView, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MainBranchViewService.class);

    @Autowired
    MainBranchViewService(MainBranchViewDao dao) {
        super(dao, logger);
    }
}
