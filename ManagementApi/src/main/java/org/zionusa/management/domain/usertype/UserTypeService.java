package org.zionusa.management.domain.usertype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.zionusa.base.enums.EUserType;
import org.zionusa.base.service.BaseService;

import java.util.List;
import java.util.Map;

@Service
public class UserTypeService extends BaseService<UserType, EUserType> {

    private static final Logger logger = LoggerFactory.getLogger(UserTypeService.class);

    @Autowired
    UserTypeService(UserTypeDao dao) {
        super(dao, logger, UserType.class);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public UserType patchById(EUserType id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public UserType save(UserType t) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<UserType> saveMultiple(List<UserType> tList) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(EUserType id) throws HttpClientErrorException.Forbidden {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws org.zionusa.base.util.exceptions.ForbiddenException {
        throw new org.zionusa.base.util.exceptions.ForbiddenException();
    }

}
