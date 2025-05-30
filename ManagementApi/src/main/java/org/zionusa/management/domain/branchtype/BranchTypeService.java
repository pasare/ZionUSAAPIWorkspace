package org.zionusa.management.domain.branchtype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.zionusa.base.enums.EBranchType;
import org.zionusa.base.service.BaseService;

import java.util.List;
import java.util.Map;

@Service
public class BranchTypeService extends BaseService<BranchType, EBranchType> {

    private static final Logger logger = LoggerFactory.getLogger(BranchTypeService.class);

    @Autowired
    BranchTypeService(BranchTypeDao dao) {
        super(dao, logger, BranchType.class);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public BranchType patchById(EBranchType id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public BranchType save(BranchType t) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<BranchType> saveMultiple(List<BranchType> tList) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(EBranchType id) throws HttpClientErrorException.Forbidden {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

}
