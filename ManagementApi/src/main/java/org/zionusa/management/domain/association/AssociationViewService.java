package org.zionusa.management.domain.association;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseViewService;

@Service
public class AssociationViewService extends BaseViewService<AssociationView, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AssociationViewService.class);

    @Autowired
    AssociationViewService(AssociationViewDao associationViewDao) {
        super(associationViewDao, logger);
    }
}
