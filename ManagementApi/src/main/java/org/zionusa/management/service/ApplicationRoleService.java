package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.enums.EApplicationRolePrefix;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.Util;
import org.zionusa.management.dao.ApplicationRoleDao;
import org.zionusa.management.dao.ApplicationRolePermissionDao;
import org.zionusa.management.dao.UserApplicationRoleDao;
import org.zionusa.management.dao.UserPermissionDao;
import org.zionusa.management.domain.ApplicationRole;
import org.zionusa.management.domain.ApplicationRolePermission;
import org.zionusa.management.domain.UserApplicationRole;
import org.zionusa.management.domain.UserPermissionForManagement;
import org.zionusa.management.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApplicationRoleService extends BaseService<ApplicationRole, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRoleService.class);

    private final ApplicationRoleDao applicationRoleDao;
    private final ApplicationRolePermissionDao applicationRolePermissionDao;
    private final UserApplicationRoleDao userApplicationRoleDao;
    private final UserPermissionDao userPermissionDao;

    public ApplicationRoleService(ApplicationRoleDao applicationRoleDao,
                                  ApplicationRolePermissionDao applicationRolePermissionDao,
                                  UserApplicationRoleDao userApplicationRoleDao,
                                  UserPermissionDao userPermissionDao) {
        super(applicationRoleDao, logger, ApplicationRole.class);
        this.applicationRoleDao = applicationRoleDao;
        this.applicationRolePermissionDao = applicationRolePermissionDao;
        this.userApplicationRoleDao = userApplicationRoleDao;
        this.userPermissionDao = userPermissionDao;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public List<ApplicationRole> getDisabledApplicationRoles() {
        return applicationRoleDao.findAllByEnabled(false);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_USER_APPLICATION_ROLES + "')")
    public List<ApplicationRole> getAllByArchivedIsFalse() {
        return super.getAllByArchivedIsFalse();
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENTS_USER_APPLICATION_ROLES + "')")
    public List<ApplicationRole> getEventsApplicationRoles() {
        return applicationRoleDao.findAllByEnabledAndNameContains(true, "EVENT_");
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_USER_APPLICATION_ROLES + "')")
    public ApplicationRole getApplicationRoleByName(String name) {
        return this.applicationRoleDao.findByName(name);
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_USER_APPLICATION_ROLES + "')")
    public List<UserApplicationRole> getUserApplicationRoles(Integer userId) {
        return userApplicationRoleDao.findByUserId(userId);
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void deleteOneApplicationRolePermission(Integer id, Integer userPermissionId) {
        Optional<ApplicationRolePermission> applicationRolePermissionOptional = applicationRolePermissionDao.findApplicationRolePermissionByApplicationRoleIdAndPermissionId(id, userPermissionId);
        if (applicationRolePermissionOptional.isPresent()) {
            ApplicationRolePermission applicationRolePermission = applicationRolePermissionOptional.get();
            applicationRolePermissionDao.delete(applicationRolePermission);
        }
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_USER_APPLICATION_ROLES + "')")
    public void deleteOneUserApplicationRole(Integer id, Integer userId) {
        Optional<UserApplicationRole> userApplicationRoleOptional = userApplicationRoleDao.findByUserIdAndApplicationRoleId(userId, id);

        if (userApplicationRoleOptional.isPresent()) {
            UserApplicationRole userApplicationRole = userApplicationRoleOptional.get();
            userApplicationRoleDao.delete(userApplicationRole);
        }
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENTS_USER_APPLICATION_ROLES + "')")
    public void deleteOneEventsUserApplicationRole(Integer id, Integer userId) throws NotFoundException {
        deleteOneTypeOfEventsUserApplicationRole(id, userId, EApplicationRolePrefix.EVENT.getValue());
    }

    // PreAuthorize Public
    public void deleteOneStudyUserApplicationRole(Integer id, Integer userId) throws NotFoundException {
        deleteOneTypeOfEventsUserApplicationRole(id, userId, EApplicationRolePrefix.STUDY.getValue());
    }

    @PreAuthorize("hasAuthority('Admin')")
    public List<ApplicationRolePermission> getAllPermissionsByApplicationRoleId(Integer id) {
        return applicationRolePermissionDao.getAllByApplicationRoleId(id);
    }

    public List<Map<String, Object>> getAllPermissionsByApplicationRoleIdDisplay(Integer id, List<String> columns) {
        List<ApplicationRolePermission> applicationRolePermissions = applicationRolePermissionDao.getAllByApplicationRoleId(id);

        List<Map<String, Object>> displayItems = new ArrayList<>();

        applicationRolePermissions.forEach(item -> {
            Map<String, Object> displayContact = Util.getFieldsAndValues(columns, item);
            if (displayContact.size() > 0) displayItems.add(displayContact);
        });

        return displayItems;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void saveOneApplicationRolePermission(Integer id, Integer userPermissionId) {
        Optional<ApplicationRolePermission> applicationRolePermissionOptional = applicationRolePermissionDao.findApplicationRolePermissionByApplicationRoleIdAndPermissionId(id, userPermissionId);
        if (!applicationRolePermissionOptional.isPresent()) {
            Optional<ApplicationRole> applicationRoleOptional = applicationRoleDao.findById(id);
            Optional<UserPermissionForManagement> userPermissionOptional = userPermissionDao.findById(userPermissionId);
            if (applicationRoleOptional.isPresent() && userPermissionOptional.isPresent()) {
                ApplicationRole applicationRole = applicationRoleOptional.get();
                UserPermissionForManagement userPermission = userPermissionOptional.get();

                ApplicationRolePermission applicationRolePermission = new ApplicationRolePermission();
                applicationRolePermission.setApplicationRole(applicationRole);
                applicationRolePermission.setApplicationRoleId(applicationRole.getId());
                applicationRolePermission.setPermission(userPermission);
                applicationRolePermission.setPermissionId(userPermission.getId());

                applicationRolePermissionDao.save(applicationRolePermission);
            } else {
                throw new NotFoundException("Application role " + id + " or user permission " + userPermissionId + " not found");
            }
        }
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_USER_APPLICATION_ROLES + "')")
    public void saveOneUserApplicationRole(Integer id, Integer userId) {
        Optional<UserApplicationRole> userApplicationRoleOptional = userApplicationRoleDao.findByUserIdAndApplicationRoleId(userId, id);

        if (!userApplicationRoleOptional.isPresent()) {
            Optional<ApplicationRole> applicationRoleOptional = applicationRoleDao.findById(id);
            if (applicationRoleOptional.isPresent()) {
                ApplicationRole applicationRole = applicationRoleOptional.get();
                UserApplicationRole userApplicationRole = new UserApplicationRole(userId, applicationRole);
                userApplicationRoleDao.save(userApplicationRole);
            } else {
                logger.warn("Application role {} not found", id);
            }
        }
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENTS_USER_APPLICATION_ROLES + "')")
    public void saveOneEventsUserApplicationRole(Integer id, Integer userId) throws NotFoundException {
        saveOneTypeOfUserApplicationRole(id, userId, EApplicationRolePrefix.EVENT.getValue());
    }

    // PreAuthorize Public
    public void saveOneStudyUserApplicationRole(Integer id, Integer userId) throws NotFoundException {
        saveOneTypeOfUserApplicationRole(id, userId, EApplicationRolePrefix.STUDY.getValue());
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_USER_APPLICATION_ROLES + "')")
    public List<UserApplicationRole> updateUserApplicationRoles(Integer userId, List<ApplicationRole> applicationRoles) {

        List<UserApplicationRole> currentRoles = userApplicationRoleDao.findByUserId(userId);
        if (!currentRoles.isEmpty()) userApplicationRoleDao.deleteAll(currentRoles);

        List<UserApplicationRole> newRoles = new ArrayList<>();
        for (ApplicationRole applicationRole : applicationRoles) {
            UserApplicationRole userApplicationRole = new UserApplicationRole(userId, applicationRole);
            newRoles.add(userApplicationRole);
        }

        return userApplicationRoleDao.saveAll(newRoles);
    }

    public void deleteOneTypeOfEventsUserApplicationRole(Integer id, Integer userId, String prefix) throws NotFoundException {
        Optional<UserApplicationRole> userApplicationRoleOptional = userApplicationRoleDao.findByUserIdAndApplicationRoleId(userId, id);

        if (userApplicationRoleOptional.isPresent()) {
            UserApplicationRole userApplicationRole = userApplicationRoleOptional.get();
            if (userApplicationRole.getApplicationRole().getName().contains(prefix)) {
                userApplicationRoleDao.delete(userApplicationRole);
            } else {
                logger.warn("User cannot edit role {}", id);
            }
        } else {
            logger.warn("Application role {} not found", id);
        }
    }

    private void saveOneTypeOfUserApplicationRole(Integer id, Integer userId, String prefix) throws NotFoundException {
        Optional<UserApplicationRole> userApplicationRoleOptional = userApplicationRoleDao.findByUserIdAndApplicationRoleId(userId, id);

        if (!userApplicationRoleOptional.isPresent()) {
            Optional<ApplicationRole> applicationRoleOptional = applicationRoleDao.findById(id);
            if (applicationRoleOptional.isPresent()) {
                ApplicationRole applicationRole = applicationRoleOptional.get();
                if (applicationRole.getName().contains(prefix)) {
                    UserApplicationRole userApplicationRole = new UserApplicationRole(userId, applicationRole);
                    userApplicationRoleDao.save(userApplicationRole);
                } else {
                    logger.warn("User cannot edit role {}", id);
                }
            } else {
                logger.warn("Application role {} not found", id);
            }
        }
    }
}
