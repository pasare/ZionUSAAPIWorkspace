package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.access.Access;
import org.zionusa.management.domain.access.AccessDao;
import org.zionusa.management.domain.role.Role;
import org.zionusa.management.domain.role.RoleDao;
import org.zionusa.management.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.zionusa.management.util.SecurityUtil.isAuthenticatedUserAccessHigher;
import static org.zionusa.management.util.SecurityUtil.isAuthenticatedUserRoleHigher;

@Service
@Transactional
public class AuthenticatedUserService implements UserDetailsService {

    public static final Logger log = LoggerFactory.getLogger(AuthenticatedUserService.class);

    private final UserDao userDao;
    private final TeamDao teamDao;
    private final GroupDao groupDao;
    private final ChurchDao churchDao;
    private final RoleDao roleDao;
    private final AccessDao accessDao;
    private final ChurchOrganizationDao churchOrganizationDao;
    private final TransferRequestDao transferRequestDao;

    @Autowired
    public AuthenticatedUserService(UserDao userDao, TeamDao teamDao, GroupDao groupDao, ChurchDao churchDao, RoleDao roleDao, AccessDao accessDao, ChurchOrganizationDao churchOrganizationDao, TransferRequestDao transferRequestDao) {
        this.userDao = userDao;
        this.teamDao = teamDao;
        this.groupDao = groupDao;
        this.churchDao = churchDao;
        this.roleDao = roleDao;
        this.accessDao = accessDao;
        this.churchOrganizationDao = churchOrganizationDao;
        this.transferRequestDao = transferRequestDao;
    }

    public ManagementAuthenticatedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.getUserByUsernameIgnoreCase(username);

        if (!userOptional.isPresent())
            throw new NotFoundException("The user: " + username + " does not have an account in the system");

        if (!userOptional.get().isArchived()) {
            User user = userOptional.get();
            user.setLastLoginDate(String.valueOf(LocalDateTime.now().atZone(EZoneId.NEW_YORK.getValue()).toEpochSecond()));

            userDao.save(user);
            return new ManagementAuthenticatedUser(userOptional.get());
        }

        return null;
    }

    public ManagementAuthenticatedUser loadUserByActiveDirectoryId(String activeDirectoryId) throws NotFoundException {
        Optional<User> userOptional = userDao.getUserByActiveDirectoryIdIgnoreCase(activeDirectoryId);

        if (userOptional.isPresent() && !userOptional.get().isArchived()) {
            return new ManagementAuthenticatedUser(userOptional.get());
        }
        return null;
    }

    public void setAuthenticatedUser(ManagementAuthenticatedUser authenticatedUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean canAccessChurches(ManagementAuthenticatedUser authenticatedUser, String churchIds) {
        for (String id : churchIds.split(",")) {
            int churchId;
            try {
                churchId = Integer.parseInt(id.trim());
                boolean churchAccess = canAccessChurch(authenticatedUser, churchId);
                if (!churchAccess) return false;
            } catch (Exception e) {
                throw new NumberFormatException("Cannot verify access to malformed id: " + id);
            }
        }

        return true;
    }

    public boolean canAccessChurch(ManagementAuthenticatedUser authenticatedUser, Integer churchId) {
        if (authenticatedUser != null) {
            final String userAccess = authenticatedUser.getAccess();
            if (EUserAccess.ADMIN.is(userAccess)) {
                return true;
            }

            Optional<Church> churchOptional = churchDao.findById(churchId);
            if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess)) {
                return churchOptional.map(church -> isChurchAdmin(authenticatedUser, church.getId()) || isBranchChurchAdmin(churchId, church.getId())).orElseThrow(NotFoundException::new);
            } else if (EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
                return churchOptional.map(church -> isChurchAdmin(authenticatedUser, church.getId())).orElseThrow(NotFoundException::new);
            } else if (EUserAccess.GROUP.is(userAccess) || EUserAccess.TEAM.is(userAccess) || EUserAccess.MEMBER.is(userAccess)) {
                return churchOptional.map(church -> authenticatedUser.getChurchId().equals(church.getId())).orElseThrow(NotFoundException::new);
            }
            log.error("attempt to ACCESS churchId: {} by AuthenticatedUser: {} failed due to lack of access rights ({})", churchId, authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        }
        log.error("attempt to ACCESS churchId: {} without authorization ({})", churchId, LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canModifyChurch(AuthenticatedUser authenticatedUser, Church church) {
        if (church != null) {
            final String userAccess = authenticatedUser.getAccess();
            if (EUserAccess.ADMIN.is(userAccess)) {
                return true;
            } else if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
                if (church.getId() == null) {
                    return authenticatedUser.getChurchId().equals(church.getParentChurchId());
                }
                return isChurchAdmin(authenticatedUser, church.getId());
            }
            log.error("attempt to MODIFY church with id {} by AuthenticatedUser: {} failed due to lack of access rights ({})", church != null ? church.getId() : null, authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        }

        return false;
    }

    public boolean canModifyChurchOrganization(AuthenticatedUser authenticatedUser, ChurchOrganization churchOrganization) {
        if (churchOrganization != null) {
            final String userAccess = authenticatedUser.getAccess();
            if (EUserAccess.ADMIN.is(userAccess)) {
                return true;
            } else if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
                return isChurchAdmin(authenticatedUser, churchOrganization.getChurchId());
            }
        }

        return false;
    }

    public boolean canDeleteChurch(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        } else if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return isBranchChurchAdmin(authenticatedUser.getChurchId(), id);
        }
        return false;
    }

    public boolean canDeleteChurchOrganization(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<ChurchOrganization> churchOrganizationOptional = churchOrganizationDao.getChurchOrganizationByChurchId(id);
        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return churchOrganizationOptional.map(organization -> isChurchAdmin(authenticatedUser, organization.getChurchId())).orElseThrow(NotFoundException::new);
        }
        return false;
    }

    public boolean canAccessGroups(AuthenticatedUser authenticatedUser, String groupIds) {
        for (String id : groupIds.split(",")) {
            int groupId;
            try {
                groupId = Integer.parseInt(id.trim());
                boolean groupAccess = canAccessGroup(authenticatedUser, groupId);
                if (!groupAccess) return false;
            } catch (Exception e) {
                throw new NumberFormatException("Cannot verify access to malformed id: " + id);
            }
        }

        return true;
    }

    public boolean canAccessGroup(AuthenticatedUser authenticatedUser, Integer groupId) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<Group> groupOptional = groupDao.findById(groupId);
        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return groupOptional.map(group -> isChurchAdmin(authenticatedUser, group.getChurchId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.GROUP.is(userAccess)) {
            return groupOptional.map(group -> isGroupAdmin(authenticatedUser, group.getId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.TEAM.is(userAccess)) {
            return groupOptional.map(group -> authenticatedUser.getGroupId().equals(group.getId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.MEMBER.is(userAccess)) {
            return groupOptional.map(group -> authenticatedUser.getGroupId().equals(group.getId())).orElseThrow(NotFoundException::new);
        }

        log.error("attempt to ACCESS group with id {} by AuthenticatedUser: {} failed due to lack of access rights ({})", groupId, authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canModifyGroup(AuthenticatedUser authenticatedUser, Group group) {
        if (group != null) {
            final String userAccess = authenticatedUser.getAccess();
            if (EUserAccess.ADMIN.is(userAccess)) {
                return true;
            } else if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
                return isChurchAdmin(authenticatedUser, group.getChurchId());
            } else if (EUserAccess.GROUP.is(userAccess)) {
                return isGroupAdmin(authenticatedUser, group.getId());
            }
        }
        log.error("attempt to MODIFY groupId: {} by AuthenticatedUser: {} failed due to lack of access rights ({})", group != null ? group.getId() : null, authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canDeleteGroup(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<Group> groupOptional = groupDao.findById(id);
        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return groupOptional.map(group -> isChurchAdmin(authenticatedUser, group.getChurchId())).orElseThrow(NotFoundException::new);
        }
        return false;
    }

    public boolean canAccessTeams(AuthenticatedUser authenticatedUser, String teamIds) {
        for (String id : teamIds.split(",")) {
            int teamId;
            try {
                teamId = Integer.parseInt(id.trim());
                boolean groupAccess = canAccessTeam(authenticatedUser, teamId);
                if (!groupAccess) return false;
            } catch (Exception e) {
                throw new NumberFormatException("Cannot verify access to malformed id: " + id);
            }
        }

        return true;
    }

    public boolean canAccessTeam(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<Team> teamOptional = teamDao.findById(id);
        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return teamOptional.map(team -> isChurchAdmin(authenticatedUser, team.getGroup().getChurchId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.GROUP.is(userAccess)) {
            return teamOptional.map(team -> isGroupAdmin(authenticatedUser, team.getGroupId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.TEAM.is(userAccess)) {
            return teamOptional.map(team -> isTeamAdmin(authenticatedUser, team.getId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.MEMBER.is(userAccess)) {
            return id.equals(authenticatedUser.getTeamId());
        }
        return false;
    }

    public boolean canModifyTeam(AuthenticatedUser authenticatedUser, Team team) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        } else if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            Optional<Group> groupOptional = groupDao.findById(team.getGroupId());
            return groupOptional.map(group -> isChurchAdmin(authenticatedUser, group.getChurchId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.GROUP.is(userAccess)) {
            return isGroupAdmin(authenticatedUser, team.getGroupId());
        } else if (EUserAccess.TEAM.is(userAccess)) {
            return isTeamAdmin(authenticatedUser, team.getId());
        }
        return false;
    }

    public boolean canDeleteTeam(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<Team> teamOptional = teamDao.findById(id);
        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return teamOptional.map(team -> isChurchAdmin(authenticatedUser, team.getGroup().getChurchId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.GROUP.is(userAccess)) {
            return teamOptional.map(team -> isGroupAdmin(authenticatedUser, team.getGroupId())).orElseThrow(NotFoundException::new);
        }
        return false;
    }

    public boolean canAccessUser(AuthenticatedUser authenticatedUser, Integer userId) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<User> userOptional = userDao.findById(userId);

        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return userOptional.map(user -> isChurchAdmin(authenticatedUser, user.getTeam().getGroup().getChurchId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.GROUP.is(userAccess)) {
            return userOptional.map(user -> isGroupAdmin(authenticatedUser, user.getTeam().getGroupId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.TEAM.is(userAccess)) {
            return userOptional.map(user -> isTeamAdmin(authenticatedUser, user.getTeamId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.MEMBER.is(userAccess)) {
            return userOptional.map(user -> authenticatedUser.getId().equals(user.getId())).orElseThrow(NotFoundException::new);
        }

        log.error("attempt to ACCESS userId: {} by AuthenticatedUser: {} failed due to lack of access rights ({})", userId, authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canModifyUser(AuthenticatedUser authenticatedUser, User user) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<Team> teamOptional = teamDao.findById(user.getTeamId());

        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            // first check if security is being modified
            if (canModifySecurity(authenticatedUser, user)) {
                return teamOptional.map(team -> isChurchAdmin(authenticatedUser, team.getGroup().getChurchId())).orElseThrow(NotFoundException::new);
            }
            return false;
        } else if (EUserAccess.GROUP.is(userAccess)) {
            if (canModifySecurity(authenticatedUser, user)) {
                return teamOptional.map(team -> isGroupAdmin(authenticatedUser, team.getGroupId())).orElseThrow(NotFoundException::new);
            }
            return false;
        } else if (EUserAccess.TEAM.is(userAccess)) {
            if (canModifySecurity(authenticatedUser, user)) {
                return teamOptional.map(team -> isTeamAdmin(authenticatedUser, team.getId())).orElseThrow(NotFoundException::new);
            }
            return false;
        } else if (EUserAccess.MEMBER.is(userAccess)) {
            if (canModifySecurity(authenticatedUser, user)) {
                return (authenticatedUser.getId().equals(user.getId()));
            }
        }
        log.error("attempt to MODIFY user: {} by AuthenticatedUser: {} failed due to lack of access rights ({})", user.getUsername(), authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canDeleteUser(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess)) {
            return true;
        }

        Optional<User> userOptional = userDao.findById(id);
        if (EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return userOptional.map(user -> isChurchAdmin(authenticatedUser, user.getTeam().getGroup().getChurchId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.GROUP.is(userAccess)) {
            return userOptional.map(user -> isGroupAdmin(authenticatedUser, user.getTeam().getGroupId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.TEAM.is(userAccess)) {
            return userOptional.map(user -> isTeamAdmin(authenticatedUser, user.getTeamId())).orElseThrow(NotFoundException::new);
        }

        log.error("attempt to DELETE user: {} by AuthenticatedUser: {} failed due to lack of access rights ({})", id, authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canModifySecurity(AuthenticatedUser authenticatedUser, User user) {
        //check if security is being updated in any way
        if (user.getId() != null) {
            int oldAccessId = userDao.getAccessIdByUserId(user.getId());
            int oldRoleId = userDao.getRoleIdByUserId(user.getId());

            //if access has changed confirm that user is allowed to make the modification
            if (oldAccessId != user.getAccessId()) {
                log.info("Authenticated User {} is attempting to modify access of user {} from {} to {}", authenticatedUser.getDisplayName(), user.getDisplayName(), oldAccessId, user.getAccessId());
                Optional<Access> newAccessOptional = accessDao.findById(user.getAccessId());
                if (!newAccessOptional.isPresent() || !isAuthenticatedUserAccessHigher(authenticatedUser.getAccess(), newAccessOptional.get().getName())) {
                    log.error("The authenticated user is NOT allowed to perform this operation");
                    return false;
                }

                log.info("The authenticated user is allowed to perform this operation");
            }

            //if role has changed confirm that user is allowed to make the modification
            if (oldRoleId != user.getRoleId()) {
                log.info("Authenticated User {} is attempting to modify role of user {} from {} to {}", authenticatedUser.getDisplayName(), user.getDisplayName(), oldRoleId, user.getRoleId());
                Optional<Role> newRoleOptional = roleDao.findById(user.getRoleId());
                if (!newRoleOptional.isPresent() || !isAuthenticatedUserRoleHigher(authenticatedUser.getAccess(), newRoleOptional.get().getName())) {
                    log.error("The authenticated user is NOT allowed to perform this operation");
                    return false;
                }

                log.info("The authenticated user is allowed to perform this operation");
            }
        }
        return true;
    }

    public boolean canModifyTransferRequest(AuthenticatedUser authenticatedUser, TransferRequest transferRequest) {
        //  Certain requests require permission
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess) || EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return true;
        } else if (EUserAccess.GROUP.is(userAccess)) {
            if (isGroupAdmin(authenticatedUser, transferRequest.getNewGroupId())) {
                return true;
            }
        } else if (EUserAccess.TEAM.is(userAccess)) {
            if (isTeamAdmin(authenticatedUser, transferRequest.getNewTeamId())) {
                return true;
            }
        }

        // But can always transfer yourself
        if (authenticatedUser.getId().equals(transferRequest.getUserId())) {
            log.info("{} (UseId: {}) is attempting a transfer request for themselves", authenticatedUser.getDisplayName(), authenticatedUser.getId());
            return true;
        }

        log.error("attempt to MODIFY transfer request: {} by AuthenticatedUser: {} failed due to lack of access rights ({})", authenticatedUser.getUsername(), authenticatedUser.getId(), LocalDate.now(EZoneId.NEW_YORK.getValue()));
        return false;
    }

    public boolean canProcessTransfer(AuthenticatedUser authenticatedUser, Integer id) {
        final String userAccess = authenticatedUser.getAccess();
        if (EUserAccess.ADMIN.is(userAccess) || EUserAccess.MAIN_BRANCH.is(userAccess) || EUserAccess.OVERSEER.is(userAccess) || EUserAccess.BRANCH.is(userAccess) || EUserAccess.CHURCH.is(userAccess)) {
            return true;
        }

        Optional<TransferRequest> transferRequestOptional = transferRequestDao.findById(id);

        if (EUserAccess.GROUP.is(userAccess)) {
            return transferRequestOptional.map(transferRequest -> isGroupAdmin(authenticatedUser, transferRequest.getNewGroupId())).orElseThrow(NotFoundException::new);
        } else if (EUserAccess.TEAM.is(userAccess)) {
            return transferRequestOptional.map(transferRequest -> isTeamAdmin(authenticatedUser, transferRequest.getNewTeamId())).orElseThrow(NotFoundException::new);
        }
        return false;
    }

    private boolean isChurchAdmin(AuthenticatedUser authenticatedUser, Integer churchId) {
        Integer userChurchId = authenticatedUser.getChurchId();
        return churchId != null && (churchId.equals(userChurchId) || isBranchChurchAdmin(userChurchId, churchId));
    }

    private boolean isBranchChurchAdmin(Integer churchId, Integer branchChurchId) {
        Optional<Church> churchOptional = churchDao.findById(branchChurchId);
        return churchOptional.filter(church -> churchId.equals(church.getParentChurchId())).isPresent();
    }

    private boolean isGroupAdmin(AuthenticatedUser authenticatedUser, Integer groupId) {
        return groupId != null && groupId.equals(authenticatedUser.getGroupId());
    }

    private boolean isTeamAdmin(AuthenticatedUser authenticatedUser, Integer teamId) {
        return teamId != null && teamId.equals(authenticatedUser.getTeamId());
    }
}
