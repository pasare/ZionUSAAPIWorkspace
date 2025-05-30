package org.zionusa.management.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.domain.TransferRequest;
import org.zionusa.management.domain.User;
import org.zionusa.management.exception.ConflictException;
import org.zionusa.management.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService extends BaseService<Team, Integer> {

    public static final Logger logger = LoggerFactory.getLogger(TeamService.class);
    private final GroupDao groupDao;
    private final TeamChurchTeamDao teamChurchTeamDao;
    private final TeamDao teamDao;
    private final TransferRequestDao transferRequestDao;
    private final TeamDisplayDao teamDisplayDao;
    private final UserService userService;

    @Autowired
    TeamService(GroupDao groupDao,
                TeamChurchTeamDao teamChurchTeamDao,
                TeamDao teamDao,
                TransferRequestDao transferRequestDao,
                TeamDisplayDao teamDisplayDao,
                UserService userService) {
        super(teamDao, logger, Team.class);
        this.groupDao = groupDao;
        this.teamChurchTeamDao = teamChurchTeamDao;
        this.teamDao = teamDao;
        this.transferRequestDao = transferRequestDao;
        this.teamDisplayDao = teamDisplayDao;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public List<Team> getAll() {
        return teamDao.findAll();
    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canAccessTeam(principal, #id)")
    public Team getById(Integer id) {
        Optional<Team> teamOptional = teamDao.findById(id);

        if (!teamOptional.isPresent())
            throw new NotFoundException("The team could not be found");

        Team team = teamOptional.get();
        Hibernate.initialize(team.getMembers());

        return teamOptional.get();
    }

    public List<Team> getByGroupId(Integer groupId) {
        return teamDao.getTeamsByGroupIdAndArchivedIsFalse(groupId);
    }

    public List<Team.DisplayTeam> getDisplayTeams(Integer groupId) {
        return teamDisplayDao.getAllByGroupIdAndArchivedFalse(groupId);
    }

    public List<Team> getAllTeamInformation() {
        return teamDao.findAll();
    }

    public Team getChurchTeam(Integer churchId) {
        Optional<Group> groupOptional = groupDao.getGroupByChurchIdAndChurchGroupTrue(churchId);

        if (!groupOptional.isPresent())
            throw new NotFoundException("The church group was not found therefore there is no church team");

        Optional<Team> teamOptional = teamDao.getTeamByGroupIdAndChurchTeamTrue(groupOptional.get().getId());

        if (!teamOptional.isPresent())
            throw new NotFoundException("The church team was not found check the church id");

        return teamOptional.get();
    }

    public List<Team.ChurchTeam> getChurchTeamsByAssociation(Integer associationId) {
        return teamChurchTeamDao.getAllByAssociationId(associationId);
    }

    public List<Team.ChurchTeam> getChurchTeamsByMainBranch(Integer mainBranchId) {
        return teamChurchTeamDao.getAllByMainChurchId(mainBranchId);
    }

    @PreAuthorize("@authenticatedUserService.canAccessTeam(principal, #id)")
    public boolean migrateUsers(Integer teamId, List<User> users) {
        logger.info("started migrating users to group: {} ({})", teamId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        int migrateCounter = 0;

        for (User user : users) {
            // Put user in new team
            user.setTeamId(teamId);
            userService.internalSaveUser(user);
            migrateCounter++;
            logger.info("finished moving user: {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        }

        if (migrateCounter != users.size()) {
            logger.error("ERROR moving one or more users ({})", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            return false;
        }

        logger.info("Finished migrating users to team: {} ({})", teamId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return true;
    }

    @PreAuthorize("@authenticatedUserService.canModifyTeam(principal, #team)")
    @CacheEvict(cacheNames = {"all-churches-cache"}, allEntries = true)
    public Team save(Team team) {
        if (team == null)
            throw new ConflictException("Cannot save a null team");

        Team returnedTeam = teamDao.save(team);
        if (returnedTeam.getLeaderId() != null) {
            User leader = userService.getById(returnedTeam.getLeaderId());
            if (leader != null) {
                leader.setTeamId(team.getId());
                // Set role to team leader
                leader.setRoleId(4);
                // Increase team leader access
                if (leader.getAccessId() == 5) { // Member access and role
                    leader.setAccessId(4); // Team
                }
                userService.save(leader);
            }
        }
        Hibernate.initialize(returnedTeam.getMembers());

        return returnedTeam;
    }

    @PreAuthorize("@authenticatedUserService.canDeleteTeam(principal, #id)")
    @CacheEvict(cacheNames = {"all-churches-cache"}, allEntries = true)
    public void archive(Integer id) {
        Optional<Team> teamOptional = teamDao.findById(id);

        if (!teamOptional.isPresent())
            throw new NotFoundException("Cannot archive a team that does not exist");

        Team team = teamOptional.get();

        if (team.isChurchTeam()) {
            logger.warn("unable to archive the church team: {} ({})", team.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            throw new ConflictException("Cannot archive the church team");
        }
        //move all users to church team
        migrateToChurchTeam(team);
        team.setArchived(true);

        teamDao.save(team);
        logger.info("Finished archiving the team: {} ({})", team.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    @PreAuthorize("@authenticatedUserService.canDeleteTeam(principal, #id)")
    @CacheEvict(cacheNames = {"all-churches-cache"}, allEntries = true)
    public void delete(Integer id) {
        Optional<Team> teamOptional = teamDao.findById(id);

        if (!teamOptional.isPresent()) {
            // No need to throw an error since the job is already done.
            logger.warn("Unable to delete team (id: {}) that does not exist: ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            return;
        }

        Team team = teamOptional.get();

        //do not delete church team
        if (team.isChurchTeam()) {
            logger.warn("unable to delete the church team: {} ({})", team.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            throw new ConflictException("Cannot delete the church team");
        }

        //move all users to church team
        migrateToChurchTeam(team);

        //remove any pending transfer requests
        for (TransferRequest transferRequest : transferRequestDao.getTransferRequestsByNewTeamId(team.getId())) {
            transferRequestDao.delete(transferRequest);
        }

        teamDao.delete(team);
        logger.info("Finished deleting the team: {} ({})", team.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    @Transactional
    public void migrateToChurchTeam(Team team) {
        logger.info("begin migrating users from team ({}):{} to church team ({})", team.getId(), team.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        if (!team.getMembers().isEmpty()) {
            Optional<Group> churchGroupOptional = groupDao.getGroupByChurchIdAndChurchGroupTrue(team.getGroup().getChurchId());

            if (!churchGroupOptional.isPresent())
                throw new NotFoundException("The church group was not found, cannot migrate users");

            Group churchGroup = churchGroupOptional.get();
            Team churchTeam = null;

            //find the church team
            for (Team returnedTeam : churchGroup.getTeams()) {
                if (returnedTeam.isChurchTeam()) {
                    churchTeam = returnedTeam;
                    break;
                }
            }

            if (churchTeam != null) {
                for (User member : team.getMembers()) {
                    member.setTeamId(churchTeam.getId());
                    userService.internalSaveUser(member);
                }

                logger.info("Finished migrating users to church team with id {} ({})", churchTeam.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            }
        } else {
            logger.warn("The team had no members to migrate id {} ({})", team.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        }
    }
}
