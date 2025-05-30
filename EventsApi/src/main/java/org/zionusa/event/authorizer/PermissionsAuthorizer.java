package org.zionusa.event.authorizer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.authorizer.BaseAuthorizer;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.event.dao.UserApplicationRoleUserPermissionViewDao;
import org.zionusa.event.dao.UserPermissionDao;
import org.zionusa.event.domain.UserApplicationRoleUserPermissionForEvents;
import org.zionusa.event.domain.UserPermissionForEvents;

import java.util.Arrays;

@Service
@Transactional
public class PermissionsAuthorizer extends BaseAuthorizer<UserApplicationRoleUserPermissionForEvents, UserPermissionForEvents, Integer> {
    public PermissionsAuthorizer(
        UserApplicationRoleUserPermissionViewDao userApplicationRoleUserPermissionViewDao,
        UserPermissionDao userPermissionDao) {
        super(userApplicationRoleUserPermissionViewDao, userPermissionDao);
    }

    public boolean hasEventAdminEditAccess() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), Arrays.asList(
            // ADMIN
            EApplicationRole.ADMIN_ACCESS.getValue(),
            EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue()
        ));
    }

    public boolean hasEventAdminReadAccess() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), Arrays.asList(
            // ADMIN
            EApplicationRole.ADMIN_ACCESS.getValue(),
            EApplicationRole.EVENT_DEFAULT_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.getValue(),
            // MANAGER
            EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue(),
            // GA
            EApplicationRole.EVENT_DEFAULT_GA.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_GA.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.getValue(),
            // AUDIO_VISUAL
            EApplicationRole.EVENT_DEFAULT_AUDIO_VISUAL_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_AUDIO_VISUAL_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_AUDIO_VISUAL_ADMIN.getValue(),
            // EDITORIAL
            EApplicationRole.EVENT_DEFAULT_EDITORIAL_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_EDITORIAL_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_EDITORIAL_ADMIN.getValue(),
            // GRAPHIC_DESIGNER
            EApplicationRole.EVENT_DEFAULT_GRAPHIC_DESIGNER_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_GRAPHIC_DESIGNER_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_GRAPHIC_DESIGNER_ADMIN.getValue(),
            // PHOTOGRAPHER
            EApplicationRole.EVENT_DEFAULT_PHOTOGRAPHER_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_PHOTOGRAPHER_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_PHOTOGRAPHER_ADMIN.getValue(),
            // VIDEOGRAPHER
            EApplicationRole.EVENT_DEFAULT_VIDEOGRAPHER_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_VIDEOGRAPHER_ADMIN.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_VIDEOGRAPHER_ADMIN.getValue()
        ));
    }

    public boolean hasEventManagerReadAccess() {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null) {
            return false;
        }
        return hasAnyApplicationRole(authenticatedUser.getUserApplicationRoles(), Arrays.asList(
            // Manager
            EApplicationRole.EVENT_DEFAULT_MANAGER.getValue(),
            EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.getValue(),
            EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.getValue()
        ));
    }
}
