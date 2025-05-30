package org.zionusa.base.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.util.auth.AuthenticatedUser;

import java.util.List;

public class BaseAuthenticatedUserService {
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
        if (EUserAccess.ADMIN.is(authenticatedUser.getAccess())) {
            return true;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.ADMIN_ACCESS.getValue()
        });
    }

    public boolean hasEditorialApplicationRole(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.EDITORIAL.getValue()
        });
    }

    public boolean isBranchLeader(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        if (EUserRole.CHURCH_LEADER.is(authenticatedUser.getRole())) {
            return true;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.BRANCH_LEADER.getValue()
        });
    }

    public boolean hasAllApplicationRoles(List<String> userRoles, String[] requiredRoles) {
        if (userRoles == null || requiredRoles == null) {
            return false;
        }
        boolean accessGranted = true;
        for (String requiredRole : requiredRoles) {
            accessGranted = !Boolean.FALSE.equals(hasApplicationRole(userRoles, requiredRole)) && accessGranted;
        }
        return accessGranted;
    }

    public boolean hasAnyApplicationRole(List<String> userRoles, String[] requiredRoles) {
        if (userRoles == null || requiredRoles == null) {
            return false;
        }
        boolean accessGranted = false;
        for (String requiredRole : requiredRoles) {
            accessGranted = hasApplicationRole(userRoles, requiredRole) || accessGranted;
        }
        return accessGranted;
    }

    public boolean hasApplicationRole(List<String> userRoles, String requiredRole) {
        if (userRoles == null || requiredRole == null) {
            return false;
        }
        String found = userRoles.stream()
                .filter(r -> r.equals(requiredRole))
                .findAny()
                .orElse(null);
        return found != null;
    }
}
