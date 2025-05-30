package org.zionusa.management.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.GroupDao;
import org.zionusa.management.dao.TeamDao;
import org.zionusa.management.dao.TransferRequestDao;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.domain.TransferRequest;
import org.zionusa.management.domain.User;
import org.zionusa.management.exception.ConflictException;
import org.zionusa.management.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService extends BaseService<Group, Integer> {

    public static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final GroupDao groupDao;
    private final UserDao userDao;
    private final TeamService teamService;
    private final TeamDao teamDao;
    private final TransferRequestDao transferRequestDao;
    private final UserService userService;

    @Autowired
    public GroupService(GroupDao groupDao,
                        UserDao userDao,
                        TeamService teamService,
                        TeamDao teamDao,
                        TransferRequestDao transferRequestDao,
                        UserService userService
    ) {
        super(groupDao, logger, Group.class);
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.teamService = teamService;
        this.teamDao = teamDao;
        this.transferRequestDao = transferRequestDao;
        this.userService = userService;
    }

    @Cacheable("all-groups-cache")
    @PreAuthorize("hasAuthority('Admin')")
    public List<Group> getAll() {
        logger.info("getting all groups ({})", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        return groupDao.getGroupsByArchivedIsFalse();
    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canAccessGroup(principal, #id)")
    public Group getById(Integer id) {
        Group group = groupDao.getGroupByIdAndArchivedIsFalse(id);

        if (group == null)
            throw new NotFoundException("The group was not found with the requested id");

        Hibernate.initialize(group.getTeams());
        return group;
    }

    @Cacheable("all-groups-cache")
    public List<Group> getAllGroupInformation() {
        return groupDao.findAll();
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal, #churchId)")
    public Group getChurchGroup(Integer churchId) {
        Optional<Group> groupOptional = groupDao.getGroupByChurchIdAndChurchGroupTrue(churchId);

        if (!groupOptional.isPresent())
            throw new NotFoundException("The church group was not found, check the church id");

        return groupOptional.get();
    }

    public User getLeader(Integer id) {
        logger.info("getting leader of groupId: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Optional<Group> groupOptional = groupDao.findById(id);
        return groupOptional.map(Group::getLeader).orElse(null);
    }

    public List<Team> getTeams(Integer groupId) {
        Optional<Group> groupOptional = groupDao.findById(groupId);

        return groupOptional.map(Group::getTeams).orElse(new ArrayList<>());
    }

    public List<User> getMembers(Integer groupId) {
        Optional<Group> groupOptional = groupDao.findById(groupId);
        List<User> users = new ArrayList<>();

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();

            if (group.getTeams() != null) {
                for (Team team : group.getTeams()) {
                    users.addAll(team.getMembers());
                }
            }
        }

        return users;
    }

    @PreAuthorize("@authenticatedUserService.canAccessGroup(principal, #id)")
    public Boolean checkUserMembership(Integer id, Integer userId) {
        logger.info("checking user {}'s membership to church: {} ({})", userId, id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Optional<Group> groupOptional = groupDao.findById(id);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            User user = new User();
            user.setId(userId);
            for (Team team : group.getTeams())
                if (team.getMembers().contains(user)) {
                    return true;
                }
        }

        return false;
    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canModifyGroup(principal, #group)")
    @CacheEvict(cacheNames = {"all-churches-cache", "all-groups-cache"}, allEntries = true)
    public Group save(Group group) {

        if (group == null) {
            throw new ConflictException("Cannot save an empty group!");
        }

        logger.info("started saving group: {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        if (group.getId() == null && group.getEffectiveDate() == null) {
            group.setEffectiveDate(new Date());
        }

        Group returnedGroup = groupDao.save(group);

        //create base team of group if it does not exist
        Team returnedTeam = createBaseTeam(group);

        //check if the leader needs to be moved
        User leader = handleGroupLeaderMove(returnedGroup, returnedTeam);
        returnedGroup.setLeader(leader);

        logger.info("finished saving group: {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        return returnedGroup;
    }

    @PreAuthorize("@authenticatedUserService.canDeleteGroup(principal, #id)")
    @CacheEvict(cacheNames = {"all-churches-cache", "all-groups-cache"}, allEntries = true)
    public void archive(Integer id) {
        logger.info("started archiving group with id: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        Optional<Group> groupOptional = groupDao.findById(id);

        if (!groupOptional.isPresent()) {
            logger.warn("Unable to archive group that does not exist: ({})", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            throw new ConflictException("Cannot delete a group that does not exist");
        }

        Group group = groupOptional.get();

        //do not delete church group
        if (group.isChurchGroup()) {
            logger.warn("Unable to archive the church group: {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            throw new ConflictException("Unable to archive the church group");
        }

        //first move any remaining members to church team
        for (Team team : group.getTeams()) {
            teamService.migrateToChurchTeam(team);
        }

        logger.info("Finished archiving group: {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        group.setArchived(true);
        groupDao.save(group);

    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canDeleteGroup(principal, #id)")
    @CacheEvict(cacheNames = {"all-churches-cache", "all-groups-cache"}, allEntries = true)
    public void delete(Integer id) {
        logger.info("Started deleting group with id: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        Optional<Group> groupOptional = groupDao.findById(id);

        if (!groupOptional.isPresent()) {
            // No need to throw an error since the job is already done.
            logger.warn("Unable to delete group (id: {}) that does not exist: ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            return;
        }

        Group group = groupOptional.get();

        if (group.isChurchGroup()) {
            logger.warn("Unable to delete the church group: {} ({})", group.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            throw new ConflictException("Cannot delete the church group");
        }

        for (Team team : group.getTeams()) {
            teamService.migrateToChurchTeam(team);

            //remove any pending transfer requests
            for (TransferRequest transferRequest : transferRequestDao.getTransferRequestsByNewTeamId(team.getId())) {
                transferRequestDao.delete(transferRequest);
            }
        }

        //remove any pending transfers
        for (TransferRequest transferRequest : transferRequestDao.getTransferRequestsByNewGroupId(group.getId())) {
            transferRequestDao.delete(transferRequest);
        }

        //finally delete the group
        groupDao.delete(group);
        logger.info("Finished deleting group: {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    private Team createBaseTeam(Group group) {
        Team team = teamDao.getTeamByGroupIdAndChurchTeamIsTrue(group.getId());

        //the base team already exists, check if leader needs to be moved there
        if (team == null) {
            logger.info("Creating the base team for the group: {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            team = new Team();
            team.setGroupId(group.getId());
            team.setChurchTeam(true);
            team.setName(group.getName());
            team.setEffectiveDate(group.getEffectiveDate());
            return teamDao.save(team);
        }

        return team;
    }

    private User handleGroupLeaderMove(Group group, Team team) {
        if (group.getLeaderId() != null) {
            Optional<User> leaderOptional = userDao.findById(group.getLeaderId());

            if (!leaderOptional.isPresent())
                throw new NotFoundException("Cannot move the leader because they cannot be found in the system");

            User leader = leaderOptional.get();
            if (leader.getTeamId() != team.getId()) {
                leader.setTeamId(team.getId());

                // Increase access
                switch (leader.getAccessId()) {
                    case 4: // Team
                    case 5: // Member
                        leader.setAccessId(3); // Group
                        break;
                    default:
                        break;
                }
                // Set role to group leader
                leader.setRoleId(3);

                return userService.internalSaveUser(leader);
            }

            return leaderOptional.get();
        }
        return null;
    }
}
