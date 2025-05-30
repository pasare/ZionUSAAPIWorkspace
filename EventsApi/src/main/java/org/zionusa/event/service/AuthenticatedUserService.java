package org.zionusa.event.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.service.BaseAuthenticatedUserService;
import org.zionusa.base.util.auth.AuthenticatedUser;

@Service
@Transactional
public class AuthenticatedUserService extends BaseAuthenticatedUserService {
    public boolean canPublish() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        return hasAdminAccess(authenticatedUser) || hasEditorialApplicationRole(authenticatedUser);
    }

    public boolean canViewEvents() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        return hasEventRepresentativeAccess(authenticatedUser);
    }

    public boolean hasEventAccess(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.BRANCH_ACCESS.getValue(),
                EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
                EApplicationRole.EVENT_DEFAULT_GA.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_GA.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.getValue(),
                EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue(),
                EApplicationRole.EVENT_DEFAULT_REPRESENTATIVE.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_REPRESENTATIVE.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_REPRESENTATIVE.getValue()
        });
    }

    public boolean hasEventAdminAccess(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue()
        });
    }

    public boolean hasEventGaAccess(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
                EApplicationRole.EVENT_DEFAULT_GA.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_GA.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.getValue()
        });
    }

    public boolean hasEventManagerAccess(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
                EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue()
        });
    }

    public boolean hasEventRepresentativeAccess(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), new String[]{
                EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
                EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue(),
                EApplicationRole.EVENT_DEFAULT_REPRESENTATIVE.getValue(),
                EApplicationRole.EVENT_ASEZ_IUBA_REPRESENTATIVE.getValue(),
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_REPRESENTATIVE.getValue()
        });
    }
}
