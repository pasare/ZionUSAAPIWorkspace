package org.zionusa.base.authorizer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zionusa.base.dao.BaseApplicationRoleUserPermissionDao;
import org.zionusa.base.dao.BaseUserPermissionDao;
import org.zionusa.base.domain.ApplicationRoleUserPermission;
import org.zionusa.base.domain.BasePermissionDomain;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.util.auth.AuthenticatedUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseAuthorizer<A extends ApplicationRoleUserPermission, U extends BasePermissionDomain<K>, K> {
    private final BaseApplicationRoleUserPermissionDao<A> baseApplicationRoleUserPermissionDao;
    private final BaseUserPermissionDao<U, K> baseUserPermissionDao;

    public BaseAuthorizer(BaseApplicationRoleUserPermissionDao<A> baseApplicationRoleUserPermissionDao,
                          BaseUserPermissionDao<U, K> baseUserPermissionDao) {
        this.baseApplicationRoleUserPermissionDao = baseApplicationRoleUserPermissionDao;
        this.baseUserPermissionDao = baseUserPermissionDao;
    }

    public List<Map<String, Boolean>> checkAllPermissions(List<String> roleNames) {
        List<U> userPermissions = baseUserPermissionDao.findAll();

        return userPermissions.stream()
            .filter(userPermission -> hasApplicationRole(roleNames, userPermission.getName()))
            .map(userPermission -> {
                Map<String, Boolean> userPermissionMap = new HashMap<>();
                userPermissionMap.put(userPermission.getName(), hasApplicationRole(roleNames, userPermission.getName()));
                return userPermissionMap;
            })
            .collect(Collectors.toList());
    }

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return (AuthenticatedUser) authentication.getPrincipal();
        }
        return null;
    }

    public boolean hasAdminAccess(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return EUserAccess.ADMIN.is(authenticatedUser.getAccess());
    }

    public boolean hasPermission(String permissionName) {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        List<String> userApplicationRoleUserPermissionForEvents =
            baseApplicationRoleUserPermissionDao.getAllByUserPermissionName(permissionName)
                .stream()
                .map(ApplicationRoleUserPermission::getUserApplicationRoleName)
                .collect(Collectors.toList());

        return hasAnyApplicationRole(
            authenticatedUser.getUserApplicationRoles(),
            userApplicationRoleUserPermissionForEvents
        );
    }

    public boolean hasAllApplicationRoles(List<String> userRoles, List<String> requiredRoles) {
        if (userRoles == null || requiredRoles == null) {
            return false;
        }
        boolean accessGranted = true;
        for (String requiredRole : requiredRoles) {
            accessGranted = !Boolean.FALSE.equals(hasApplicationRole(userRoles, requiredRole)) && accessGranted;
        }
        return accessGranted;
    }

    public boolean hasAnyApplicationRole(List<String> userApplicationRoles, List<String> requiredApplicationRoles) {
        if (userApplicationRoles == null || requiredApplicationRoles == null) {
            return false;
        }
        boolean accessGranted = false;
        for (String requiredRole : requiredApplicationRoles) {
            accessGranted = hasApplicationRole(userApplicationRoles, requiredRole) || accessGranted;
        }
        return accessGranted;
    }

    public boolean hasApplicationRole(List<String> userApplicationRoles, String requiredApplicationRole) {
        if (userApplicationRoles == null || requiredApplicationRole == null) {
            return false;
        }
        String found = userApplicationRoles.stream()
            .filter(r -> r.equals(requiredApplicationRole))
            .findAny()
            .orElse(null);
        return found != null;
    }
}
