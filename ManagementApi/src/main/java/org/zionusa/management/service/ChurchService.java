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
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.domain.User;
import org.zionusa.management.exception.ConflictException;
import org.zionusa.management.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated
@Service
public class ChurchService extends BaseService<Church, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ChurchService.class);

    private final ChurchDao churchDao;
    private final ChurchDisplayDao churchDisplayDao;
    private final UserDao userDao;
    private final GroupDao groupDao;
    private final TeamDao teamDao;
    private final GroupDisplayDao groupDisplayDao;
    private final UserService userService;

    @Autowired
    public ChurchService(ChurchDao churchDao,
                         ChurchDisplayDao churchDisplayDao, UserDao userDao,
                         GroupDao groupDao,
                         TeamDao teamDao,
                         GroupDisplayDao groupDisplayDao, UserService userService) {
        super(churchDao, logger, Church.class);
        this.churchDao = churchDao;
        this.churchDisplayDao = churchDisplayDao;
        this.userDao = userDao;
        this.groupDao = groupDao;
        this.teamDao = teamDao;
        this.groupDisplayDao = groupDisplayDao;
        this.userService = userService;
    }

    @Cacheable("all-churches-cache")
    @PreAuthorize("hasAuthority('Admin')")
    public List<Church> getAll(Boolean loadFull) {
        logger.info("getting all churches ({})", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        List<Church> churches = churchDao.getAllByArchivedFalse();
        churches.sort(Comparator.comparing(Church::getName));
        return churches;
    }

    public List<Church.DisplayChurch> getDisplayChurches(Integer associationId) {
        if (associationId == null) {
            return churchDisplayDao.getAllByArchivedFalse();
        }
        return churchDisplayDao.getAllByArchivedFalseAndAssociationId(associationId);
    }

    @Cacheable("churches-cache")
    public List<Church> getAllChurchInformation() {
        List<Church> churches = churchDao.getAllByArchivedFalse();
        churches.sort(Comparator.comparing(Church::getName));

        return churches;
    }

    @Override
    @Transactional
    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal, #id)")
    public Church getById(Integer id) {
        logger.info("getting church with id: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        Optional<Church> churchOptional = churchDao.findById(id);

        if (!churchOptional.isPresent())
            throw new NotFoundException("The church was not found");

        loadFullChurch(churchOptional.get());

        return churchOptional.get();
    }

    public Church.DisplayChurch getDisplayById(Integer id) {
        Optional<Church.DisplayChurch> churchOptional = churchDisplayDao.findById(id);

        if (!churchOptional.isPresent())
            throw new NotFoundException("The church was not found");

        return churchOptional.get();
    }

    public Church getInformationById(Integer id) {
        Optional<Church> churchOptional = churchDao.findById(id);

        if (!churchOptional.isPresent())
            throw new NotFoundException("The church was not found");

        return churchOptional.get();
    }

    public List<Church> getByParentChurchId(Integer id) {
        Optional<Church> churchOptional = churchDao.findById(id);

        if (!churchOptional.isPresent())
            throw new NotFoundException("The church was not found");

        List<Church> churches = churchDao.getByParentChurchId(id);
        churches.add(churchOptional.get());

        return churches;
    }

    public User getLeader(Integer churchId) {
        logger.info("getting church {}'s leader by id: ({})", churchId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Optional<Church> churchOptional = churchDao.findById(churchId);

        return churchOptional.map(Church::getLeader).orElse(null);
    }

    @Transactional
    public List<User> getLeaders(Integer churchId) {
        logger.info("getting church leaders for church id: {} ({})", churchId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Optional<Church> churchOptional = churchDao.findById(churchId);

        if (!churchOptional.isPresent())
            throw new NotFoundException("Cannot get leaders for a church that does not exist");

        List<User> leaders = new ArrayList<>();
        for (Group group : churchOptional.get().getGroups()) {
            for (Team team : group.getTeams()) {
                for (User member : team.getMembers()) {
                    if (EUserRole.CHURCH_LEADER.is(member.getRole().getName())) {
                        leaders.add(member);
                    }
                }
            }
        }

        return leaders;
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal, #id)")
    public List<Group> getGroups(Integer id) {
        return groupDao.getGroupsByChurchIdAndArchivedIsFalse(id);
    }

    @Transactional
    public List<Group.DisplayGroup> getDisplayGroups(Integer churchId) {
        return groupDisplayDao.getAllByChurchIdAndArchivedFalse(churchId);
    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal, #id)")
    public List<User> getMembers(Integer id) {
        Optional<Church> churchOptional = churchDao.findById(id);
        List<User> users = new ArrayList<>();

        if (churchOptional.isPresent()) {
            Church church = churchOptional.get();

            for (Group group : church.getGroups()) {
                for (Team team : group.getTeams()) {
                    users.addAll(team.getMembers());
                }
            }
        }
        return users;
    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal, #id)")
    public List<User> getMembersByGender(Integer id, String gender) {
        Optional<Church> churchOptional = churchDao.findById(id);
        List<User> users = new ArrayList<>();

        if (churchOptional.isPresent()) {
            Church church = churchOptional.get();
            for (Group group : church.getGroups()) {
                for (Team team : group.getTeams()) {
                    if (team != null && !team.getMembers().isEmpty() &&
                        team.getMembers().get(0).getGender().equals(gender)) {
                        users.addAll(team.getMembers());
                    }
                }
            }
        }
        return users;
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal, #parentChurchId)")
    public List<Church> getBranchChurches(Integer parentChurchId) {
        logger.info("getting branch churches for church {} ({})", parentChurchId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        return churchDao.getByParentChurchId(parentChurchId);
    }

    public Boolean checkGroupMembership(Integer id, Integer groupId) {
        logger.info("checking groups {}'s membership to church: {} ({})", groupId, id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Optional<Church> church = churchDao.findById(id);
        if (church.isPresent()) {
            for (Group group : church.get().getGroups()) {
                if (group.getId().equals(groupId))
                    return true;
            }
        }
        return false;
    }

    public Boolean checkUserMembership(Integer id, Integer userId) {
        logger.info("checking user {}'s membership to church: {} ({})", userId, id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Optional<Church> church = churchDao.findById(id);
        if (church.isPresent()) {
            User user = new User();
            user.setId(userId);
            for (Group group : church.get().getGroups()) {
                for (Team team : group.getTeams()) {
                    if (team.getMembers().contains(user))
                        return true;
                }
            }
        }
        return false;
    }

    @PreAuthorize("@authenticatedUserService.canDeleteChurch(principal, #churchId)")
    public void cleanUpGroups(Integer churchId) {
        List<Group> groups = groupDao.getGroupsByChurchId(churchId);
        for (Group group : groups) {
            if (!group.isChurchGroup() && group.getTeams().isEmpty()) {
                logger.warn("No more members in group: {} so deleting it ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
                group.setArchived(true);
                groupDao.save(group);
            }
        }

    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canModifyChurch(principal, #church)")
    @CacheEvict(cacheNames = {"all-churches-cache", "churches-cache", "all-groups-cache"}, allEntries = true)
    public Church saveChurch(Church church) throws NotFoundException {

        if (church == null) {
            throw new ConflictException("Cannot save an empty church!");
        }

        logger.info("begin saving church {} ({})", church.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        Church returnedChurch = churchDao.save(church);

        Optional<Group> churchGroupOptional = groupDao.getGroupByChurchIdAndChurchGroupTrue(returnedChurch.getId());

        //If group does not already exist for this church then create it otherwise return existing group
        Group churchGroup = churchGroupOptional.orElseGet(() -> createChurchGroup(returnedChurch));

        //check if the church leader needs to be moved from another church
        if (church.getLeaderId() != null) {
            Optional<Team> churchTeamOptional = churchGroup.getTeams().stream().filter(Team::isChurchTeam).findFirst();
            if (churchTeamOptional.isPresent()) {
                moveLeader(church.getLeaderId(), returnedChurch, churchTeamOptional.get());
            } else {
                throw new NotFoundException("The church id " + church.getId() + " does not have a church team in the system");
            }
        }

        logger.info("finished saving church {} ({})", church.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        expireCache();
        return returnedChurch;
    }

    @PreAuthorize("@authenticatedUserService.canDeleteChurch(principal, #id)")
    @CacheEvict(cacheNames = {"all-churches-cache", "churches-cache", "all-groups-cache"}, allEntries = true)
    public void archiveChurch(Integer id) {
        Optional<Church> churchOptional = churchDao.findById(id);

        if (!churchOptional.isPresent()) {
            // No need to throw an error since the job is already done.
            logger.warn("Unable to delete church (id: {}) that does not exist: ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            return;
        }

        Church church = churchOptional.get();
        church.setArchived(true);
        churchDao.save(church);
        expireCache();
    }


    @PreAuthorize("hasAuthority('Admin')")
    @CacheEvict(cacheNames = {"all-churches-cache", "churches-cache", "all-groups-cache"}, allEntries = true)
    public void deleteChurch(Integer id) {
        Optional<Church> churchOptional = churchDao.findById(id);

        if (!churchOptional.isPresent())
            throw new NotFoundException("Cannot delete a church that does not exist");

        Church church = churchOptional.get();

        logger.info("deleting church: {} with id: {} ({})", church.getName(), church.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        churchDao.delete(church);
        expireCache();
    }

    @PreAuthorize("@authenticatedUserService.canModifyChurch(principal, #newChurch.id)")
    public void moveLeader(Integer leaderId, Church newChurch, Team newChurchTeam) {

        if (leaderId != null) {
            //get leader
            Optional<User> leaderOptional = userDao.findById(leaderId);
            if (!leaderOptional.isPresent()) throw new NotFoundException("A leader does not exist with that id");

            User leader = leaderOptional.get();

            //check if the leader is valid to move
            if (!newChurchTeam.getId().equals(leader.getTeamId())) {

                //remove the church leader from old church if applicable
                Church oldChurch = leader.getTeam().getGroup().getChurch();
                logger.info("removing church leader {} from {} ({})", leader.getDisplayName(), oldChurch.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

                if (oldChurch.getLeader() != null && oldChurch.getLeader().getId().equals(leaderId)) {
                    oldChurch.setLeaderId(null);
                    churchDao.save(oldChurch);
                    logger.info("finished removing church leader {} from {} ({})", leader.getDisplayName(), oldChurch.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
                }

                // Move the church leader to new church
                logger.info("moving leader {} to new team {} from old team {} ({})", leader.getDisplayName(), newChurchTeam.getId(), leader.getTeamId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
                newChurch.setLeaderId(leaderId);
                churchDao.save(newChurch);

                // Then move the leader into the church team
                leader.setTeamId(newChurchTeam.getId());

                // Increase access
                switch (leader.getAccessId()) {
                    case 3: // Group
                    case 4: // Team
                    case 5: // Member
                        leader.setAccessId(2); // Church
                        break;
                    default:
                        break;
                }
                // Set role to church leader
                leader.setRoleId(2);

                //need to also update the users structure information now because its flat in the tree
                userService.internalSaveUser(leader);
                logger.info("finished moving leader {} to new team {} ({})", leader.getDisplayName(), newChurchTeam.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            }
        }
    }

    @CacheEvict(cacheNames = {"all-churches-cache", "churches-cache", "all-groups-cache"}, allEntries = true)
    public void expireCache() {
        // Left empty to just expire cache only
    }

    private void loadFullChurch(Church church) {

        loadChurchCollections(church);

        if (church.getBranches() != null) {
            // Filter out archived branches
            church.setBranches(church.getBranches().stream().filter(branch -> !branch.getArchived()).collect(Collectors.toList()));
            for (Church branch : church.getBranches()) {
                loadChurchCollections(branch);
            }
        }
    }

    private void loadChurchCollections(Church church) {
        if (church.getGroups() != null) {
            Hibernate.initialize(church.getGroups());
            for (Group group : church.getGroups()) {
                group.setChurch(null);

                if (group.getTeams() != null) {
                    Hibernate.initialize(group.getTeams());

                    for (Team team : group.getTeams()) {
                        if (team != null) team.setGroup(null);

                        if (team != null && team.getMembers() != null) {
                            Hibernate.initialize(team.getMembers());
                            for (User user : team.getMembers()) {

                                //prevent circular dependencies
                                user.setTeam(null);
                            }
                        }
                    }
                }
            }
        }
    }

    private Group createChurchGroup(Church church) {

        Group group = new Group();
        group.setChurchId(church.getId());
        group.setName(church.getName());
        group.setChurchGroup(true);
        group.setEffectiveDate(new Date());
        Group returnedGroup = groupDao.save(group);
        logger.info("finished saving the new church group {} ({})", group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        //now must save the church team within that group
        Team team = new Team();
        team.setGroupId(returnedGroup.getId());
        team.setName(returnedGroup.getName());
        team.setChurchTeam(true);
        team.setEffectiveDate(returnedGroup.getEffectiveDate());
        Team churchTeam = teamDao.save(team);

        List<Team> teams = new ArrayList<>();
        List<Group> groups = new ArrayList<>();

        teams.add(churchTeam);
        groups.add(returnedGroup);

        returnedGroup.setTeams(teams);
        church.setGroups(groups);
        logger.info("finished saving the new church team {} ({})", team.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        return returnedGroup;
    }
}
