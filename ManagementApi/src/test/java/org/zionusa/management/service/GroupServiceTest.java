package org.zionusa.management.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.dao.GroupDao;
import org.zionusa.management.dao.TeamDao;
import org.zionusa.management.dao.TransferRequestDao;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.domain.User;
import org.zionusa.management.exception.ConflictException;
import org.zionusa.management.exception.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

public class GroupServiceTest {

    @InjectMocks
    private GroupService service;

    @Mock
    private TeamService teamService;

    @Mock
    private GroupDao dao;

    @Mock
    private UserDao userDao;

    @Mock
    private TeamDao teamDao;

    @Mock
    private TransferRequestDao transferRequestDao;

    private List<Group> mockGroups;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockGroupsPath = "src/test/resources/groups.json";

        byte[] groupsData = Files.readAllBytes(Paths.get(mockGroupsPath));
        mockGroups = mapper.readValue(groupsData, new TypeReference<List<Group>>() {
        });

        when(dao.findAll()).thenReturn(mockGroups);
        when(dao.getGroupsByArchivedIsFalse()).thenReturn(mockGroups);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockGroups.get(0)));
        when(dao.getGroupByIdAndArchivedIsFalse(any(Integer.class))).thenReturn(mockGroups.get(0));
    }

    @Test
    public void getAllGroups() {
        List<Group> groups = service.getAll();

        assertThat(groups).isNotEmpty();
        assertThat(groups.size()).isEqualTo(mockGroups.size());
        assertThat(mockGroups).containsAll(groups);
    }

    @Test
    public void getGroupById() {
        Group group = service.getById(1);

        assertThat(group).isNotNull();
        assertThat(group.getId()).isNotNull();
        assertThat(group.getId()).isEqualTo(mockGroups.get(0).getId());
        assertThat(group.getLeaderId()).isEqualTo(mockGroups.get(0).getLeaderId());
        assertThat(group.getLeader()).isEqualTo(mockGroups.get(0).getLeader());
        assertThat(group.getChurchId()).isEqualTo(mockGroups.get(0).getChurchId());
        assertThat(group.getChurch()).isEqualTo(mockGroups.get(0).getChurch());
        assertThat(group.isArchived()).isEqualTo(mockGroups.get(0).isArchived());
    }

    @Test(expected = NotFoundException.class)
    public void getGroupThatDoesNotExistById() {
        when(dao.getGroupByIdAndArchivedIsFalse(anyInt())).thenReturn(null);

        service.getById(1);
    }

    @Test
    public void getChurchGroup() {
        when(dao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(mockGroups.get(1)));

        Group group = service.getChurchGroup(1);

        assertThat(group).isNotNull();
    }

    @Test(expected = NotFoundException.class)
    public void getChurchGroupThatDoesNotExist() {
        when(dao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.empty());

        service.getChurchGroup(1);
    }

    @Test
    public void getLeader() {
        User leader = service.getLeader(1);

        assertThat(leader).isNotNull();
        assertThat(leader.getId()).isNotNull();
        assertThat(leader.getId()).isEqualTo(mockGroups.get(0).getLeader().getId());
    }

    @Test
    public void getLeaderThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockGroups.get(2)));

        User leader = service.getLeader(1);

        assertThat(leader).isNull();
    }

    @Test
    public void getLeaderOfChurchThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        User leader = service.getLeader(1);

        assertThat(leader).isNull();
    }

    @Test
    public void getTeams() {
        List<Team> teams = service.getTeams(1);
        List<Team> mockTeams = mockGroups.get(0).getTeams();

        assertThat(teams).isNotEmpty();
        assertThat(teams.size()).isEqualTo(mockTeams.size());
        assertThat(mockTeams).containsAll(teams);
    }

    @Test
    public void getTeamsOfGroupThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        List<Team> teams = service.getTeams(1);

        assertThat(teams).isEmpty();
    }

    @Test
    public void getMembers() {
        List<User> members = service.getMembers(1);
        List<User> mockMembers = new ArrayList<>();

        for (Team team : mockGroups.get(0).getTeams()) {
            mockMembers.addAll(team.getMembers());
        }

        assertThat(members).isNotEmpty();
        assertThat(members.size()).isEqualTo(mockMembers.size());
        assertThat(mockMembers).containsAll(members);
    }

    @Test
    public void getMembersThatDoNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockGroups.get(1)));

        List<User> members = service.getMembers(1);

        assertThat(members).isEmpty();
    }

    @Test
    public void checkUserMembership() {
        boolean membershipCheck1 = service.checkUserMembership(1, 1);
        boolean membershipCheck2 = service.checkUserMembership(1, 2);

        assertThat(membershipCheck1).isFalse();
        assertThat(membershipCheck2).isTrue();
    }

    @Test
    public void checkUserMembershipOfGroupThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        boolean membershipCheck1 = service.checkUserMembership(43, 1);
        boolean membershipCheck2 = service.checkUserMembership(12, 1);

        assertThat(membershipCheck1).isFalse();
        assertThat(membershipCheck2).isFalse();
    }

    @Test
    public void saveGroup() {
        when(dao.save(any(Group.class))).thenReturn(mockGroups.get(0));
        when(teamDao.getTeamsByGroupId(anyInt())).thenReturn(new ArrayList<>());
        when(userDao.getOne(anyInt())).thenReturn(new User());

        Group group = mockGroups.get(0);

        Group returnedGroup = service.save(group);

        assertThat(returnedGroup).isNotNull();
        assertThat(returnedGroup.getId()).isEqualTo(group.getId());

        verify(dao, times(1)).save(any(Group.class));
    }

    @Test(expected = ConflictException.class)
    public void SaveGroupThatDoesNotExist() {
        service.save(null);

        verify(dao, never()).save(any(Group.class));
    }

    @Test
    public void archiveGroup() {
        service.archive(1);

        verify(teamService, times(2)).migrateToChurchTeam(any(Team.class));
        verify(dao, times(1)).save(any(Group.class));
        verify(dao, never()).delete(any(Group.class));
    }

    @Test()
    public void archiveGroupThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        try {
            service.archive(1);

            fail("Should throw ConflictException");
        } catch (ConflictException e) {
            verify(teamService, never()).migrateToChurchTeam(any(Team.class));
            verify(dao, never()).save(any(Group.class));
        }
    }

    @Test(expected = ConflictException.class)
    public void archiveChurchGroup() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockGroups.get(1)));

        service.archive(2);

        verify(teamService, never()).migrateToChurchTeam(any(Team.class));
        verify(dao, never()).save(any(Group.class));
    }

    @Test
    public void deleteGroup() {
        service.delete(1);

        verify(teamService, times(2)).migrateToChurchTeam(any(Team.class));
        verify(dao, times(1)).delete(any(Group.class));
    }

    @Test()
    public void deleteGroupThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.delete(1);

        verify(teamService, never()).migrateToChurchTeam(any(Team.class));
        verify(dao, never()).delete(any(Group.class));
    }

    @Test(expected = ConflictException.class)
    public void deleteChurchGroup() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockGroups.get(1)));
        service.delete(1);

        verify(teamService, never()).migrateToChurchTeam(any(Team.class));
        verify(dao, never()).delete(any(Group.class));
    }


}
