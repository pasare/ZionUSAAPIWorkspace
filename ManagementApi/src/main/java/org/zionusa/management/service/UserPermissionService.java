package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.management.authorizer.PermissionsAuthorizer;
import org.zionusa.management.dao.UserPermissionDao;
import org.zionusa.management.dao.UserPermissionViewDao;
import org.zionusa.management.domain.UserPermissionForManagement;
import org.zionusa.management.domain.UserPermissionView;
import org.zionusa.management.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserPermissionService extends BaseService<UserPermissionForManagement, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(UserPermissionService.class);

    private final PermissionsAuthorizer permissionsAuthorizer;
    private final UserPermissionViewDao userPermissionViewDao;

    @Autowired
    public UserPermissionService(PermissionsAuthorizer permissionsAuthorizer,
                                 UserPermissionViewDao userPermissionViewDao,
                                 UserPermissionDao dao) {
        super(dao, logger, UserPermissionForManagement.class);
        this.permissionsAuthorizer = permissionsAuthorizer;
        this.userPermissionViewDao = userPermissionViewDao;
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<UserPermissionForManagement> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Page<UserPermissionForManagement> getAllByPage(Pageable pageable) throws NotFoundException {
        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) throws NotFoundException {
        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public UserPermissionForManagement getById(Integer id) throws NotFoundException {
        throw new NotFoundException();
    }

    public List<String> getByUserId(Integer userId) throws NotFoundException {
        AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();

        if (permissionsAuthorizer.hasAdminAccess(authenticatedUser) || authenticatedUser.getId().equals(userId)) {
            final List<UserPermissionView> userPermissionViews = userPermissionViewDao.getAllByUserId(userId);

            return mapPermissionNames(userPermissionViews);
        }

        throw new NotFoundException();
    }

    public String getUserAccessToken() {
        AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();

        final List<UserPermissionView> userPermissionViews = userPermissionViewDao.getAllByUserId(authenticatedUser.getId());

        return this.permissionsAuthorizer.getPermissionsToken(authenticatedUser.getId(), mapPermissionNames(userPermissionViews));
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public UserPermissionForManagement patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public UserPermissionForManagement save(UserPermissionForManagement t) throws NotFoundException {
        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<UserPermissionForManagement> saveMultiple(List<UserPermissionForManagement> tList) throws NotFoundException {
        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws NotFoundException {
        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws NotFoundException {
        throw new NotFoundException();
    }

    private List<String> mapPermissionNames(List<UserPermissionView> userPermissionViews) {
        Map<String, String> userPermissionViewMap = new HashMap<>();

        for (UserPermissionView userPermissionView : userPermissionViews) {
            final String permissionName = userPermissionView.getUserPermissionName();
            userPermissionViewMap.putIfAbsent(permissionName, permissionName);
        }

        return new ArrayList<>(userPermissionViewMap.values());
    }
}
