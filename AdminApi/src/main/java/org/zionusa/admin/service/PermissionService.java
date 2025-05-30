package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.admin.domain.Application;
import org.zionusa.admin.domain.Form;
import org.zionusa.admin.domain.Permission;
import org.zionusa.admin.domain.Report;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService extends BaseService<Permission, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    public PermissionService(BaseDao<Permission, Integer> dao) {
        super(dao, logger, Permission.class);
    }

    public boolean checkPermission(Permission permission) {

        //If no permission has been set then everybody can see the file
        if (permission == null)
            return true;

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticatedUser.getAccess().equalsIgnoreCase(Permission.Access.ADMIN.name())) {
            return true;
        }
        else {

            // If user does not meet the basic access requirement then skip the file
            if (permission.getAccess() == null || Permission.Access.valueOf(authenticatedUser.getAccess().toUpperCase()).compareTo(permission.getAccess()) > 0)
                return false;
            else {
                //User meets basic access requirements. If there is no SecurityGroup requirement then return the file
                if (permission.getOrganizationLevel() == null) return true;
            }

            //for all other cases do direct matching
            switch (permission.getOrganizationLevel()) {
                case CHURCH:
                    if (checkChurchPermission(authenticatedUser, permission)) return true;
                case GROUP:
                    if (checkGroupPermission(authenticatedUser, permission)) return true;
                case TEAM:
                    if (checkTeamPermission(authenticatedUser, permission)) return true;
                case USER:
                    if (checkUserPermission(authenticatedUser, permission)) return true;
            }
        }
        return false;
    }

    public List<Application> filterAppListByPermission(List<Application> applications) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //admin case return everything
        if (authenticatedUser.getAccess().equalsIgnoreCase(Permission.Access.ADMIN.name())) {
            return applications;
        }

        return applications
                .stream()
                .filter(application -> checkPermission(application.getPermission()))
                .collect(Collectors.toList());
    }

    public List<Form> filterFormListByPermission(List<Form> forms) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //admin case return everything
        if (authenticatedUser.getAccess().equalsIgnoreCase(Permission.Access.ADMIN.name())) {
            return forms;
        }

        return forms
                .stream()
                .filter(form -> checkPermission(form.getPermission()))
                .collect(Collectors.toList());
    }

    public List<Report> filterReportListByPermission(List<Report> reports) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //admin case return everything
        if (authenticatedUser.getAccess().equalsIgnoreCase(Permission.Access.ADMIN.name())) {
            return reports;
        }

        return reports
                .stream()
                .filter(form -> checkPermission(form.getPermission()))
                .collect(Collectors.toList());
    }

    private boolean checkChurchPermission(AuthenticatedUser authenticatedUser, Permission permission) {
        if (permission.getOrganizationLevelIds() != null) {
            String[] churchIds = permission.getOrganizationLevelIds().split(",");
            boolean contains = Arrays.asList(churchIds).contains(authenticatedUser.getChurchId().toString());
            if (contains) {
                return checkUserPermission(authenticatedUser, permission);
            }
        }
        return false;
    }

    private boolean checkGroupPermission(AuthenticatedUser authenticatedUser, Permission permission) {
        if (permission.getOrganizationLevelIds() != null) {
            String[] groupIds = permission.getOrganizationLevelIds().split(",");
            boolean contains = Arrays.asList(groupIds).contains(authenticatedUser.getGroupId().toString());
            if (contains) {
                return checkUserPermission(authenticatedUser, permission);
            }
        }
        return false;
    }

    private boolean checkTeamPermission(AuthenticatedUser authenticatedUser, Permission permission) {
        if (permission.getOrganizationLevelIds() != null) {
            String[] teamIds = permission.getOrganizationLevelIds().split(",");
            boolean contains = Arrays.asList(teamIds).contains(authenticatedUser.getTeamId().toString());
            if (contains) {
                return checkUserPermission(authenticatedUser, permission);
            }
        }
        return false;
    }

    private boolean checkUserPermission(AuthenticatedUser authenticatedUser, Permission permission) {
        if (permission.getUserIds() == null || permission.getUserIds().equals(""))
            return true;

        String[] userIds = permission.getUserIds().split(",");

        return Arrays.asList(userIds).contains(authenticatedUser.getId().toString());
    }
}
