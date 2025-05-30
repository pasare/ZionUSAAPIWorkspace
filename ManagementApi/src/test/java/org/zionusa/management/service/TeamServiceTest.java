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
import org.zionusa.management.domain.TransferRequest;
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

public class TeamServiceTest {

    @InjectMocks
    private TeamService service;

    private List<Team> mockTeams;

    @Mock
    private TeamDao dao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TransferRequestDao transferRequestDao;

    @Mock
    private UserService userService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockTeamsPath = "src/test/resources/teams.json";

        byte[] teamsData = Files.readAllBytes(Paths.get(mockTeamsPath));
        mockTeams = mapper.readValue(teamsData, new TypeReference<List<Team>>() {
        });

        when(dao.findAll()).thenReturn(mockTeams);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockTeams.get(0)));
        when(userService.internalSaveUser(any())).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void getAllTeams() {
        List<Team> teams = service.getAll();

        assertThat(teams).isNotNull();
        assertThat(teams.size()).isEqualTo(mockTeams.size());
        assertThat(mockTeams).containsAll(teams);
    }

    @Test
    public void getTeamById() throws javassist.NotFoundException {
        Team team = service.getById(1);

        assertThat(team).isNotNull();
        assertThat(team.getId()).isNotNull();
        assertThat(team.getGroupId()).isEqualTo(mockTeams.get(0).getGroupId());
        assertThat(team.getGroup()).isEqualTo(mockTeams.get(0).getGroup());
        assertThat(team.getLeaderId()).isEqualTo(mockTeams.get(0).getLeaderId());
        assertThat(team.getLeader()).isEqualTo(mockTeams.get(0).getLeader());
        assertThat(team.isArchived()).isEqualTo(mockTeams.get(0).isArchived());
        assertThat(team.getEffectiveDate()).isEqualTo(mockTeams.get(0).getEffectiveDate());
    }

    @Test(expected = NotFoundException.class)
    public void getTeamThatDoesNotExistById() throws javassist.NotFoundException {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.getById(1);
    }

    @Test
    public void getChurchTeam() {
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(new Group()));
        when(dao.getTeamByGroupIdAndChurchTeamTrue(null)).thenReturn(Optional.of(mockTeams.get(0)));

        Team team = service.getChurchTeam(1);

        assertThat(team).isNotNull();
    }

    @Test(expected = NotFoundException.class)
    public void getChurchTeamWhenChurchGroupDoesNotExist() {
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.empty());
        when(dao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(mockTeams.get(0)));

        service.getChurchTeam(1);
    }

    @Test(expected = NotFoundException.class)
    public void getChurchTeamThatDoesNotExist() {
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(new Group()));
        when(dao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(null);

        service.getChurchTeam(1);
    }

    @Test
    public void migrateUsers() {
        when(userDao.save(any(User.class))).thenReturn(null);

        service.migrateUsers(1, mockTeams.get(0).getMembers());
        service.migrateUsers(1, mockTeams.get(1).getMembers());
    }

    @Test
    public void saveTeam() {
        when(dao.save(any(Team.class))).thenReturn(mockTeams.get(1));

        Team returnedTeam = service.save(mockTeams.get(1));

        assertThat(returnedTeam).isNotNull();
        assertThat(returnedTeam.getId()).isEqualTo(mockTeams.get(1).getId());
        verify(dao, times(1)).save(any(Team.class));
    }

    @Test(expected = ConflictException.class)
    public void saveTeamThatDoesNotExist() {
        service.save(null);
    }

    @Test
    public void archiveTeam() {
        when(dao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(mockTeams.get(0)));

        Group mockGroup = new Group();
        mockGroup.setId(1);
        mockGroup.setTeams(mockTeams);

        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(mockGroup));
        service.archive(1);

        verify(groupDao, times(1)).getGroupByChurchIdAndChurchGroupTrue(anyInt());
//        verify(userDao,times(1)).save(any(User.class));
        verify(dao, times(1)).save(any(Team.class));
    }

    @Test(expected = NotFoundException.class)
    public void archiveTeamThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.archive(1);

        verify(groupDao, never()).getGroupByChurchIdAndChurchGroupTrue(anyInt());
        verify(userDao, never()).save(any(User.class));
        verify(dao, never()).getTeamByGroupIdAndChurchTeamTrue(anyInt());
        verify(dao, never()).save(any(Team.class));
    }

    @Test(expected = ConflictException.class)
    public void archiveChurchTeam() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockTeams.get(2)));

        service.archive(1);

        verify(groupDao, never()).getGroupByChurchIdAndChurchGroupTrue(anyInt());
        verify(userDao, never()).save(any(User.class));
        verify(dao, never()).getTeamByGroupIdAndChurchTeamTrue(anyInt());
        verify(dao, never()).save(any(Team.class));
    }

    @Test
    public void deleteTeam() {
        when(dao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(mockTeams.get(0)));

        Group mockGroup = new Group();
        mockGroup.setId(1);
        mockGroup.setTeams(mockTeams);

        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(mockGroup));
        service.delete(1);

        verify(groupDao, times(1)).getGroupByChurchIdAndChurchGroupTrue(anyInt());
//        verify(userDao,times(1)).save(any(User.class));
        verify(dao, times(1)).delete(any(Team.class));
    }

    @Test()
    public void deleteTeamThatDoesNotExist() throws NotFoundException {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.delete(1);

        verify(groupDao, never()).getGroupByChurchIdAndChurchGroupTrue(anyInt());
        verify(userDao, never()).save(any(User.class));
        verify(dao, never()).getTeamByGroupIdAndChurchTeamTrue(anyInt());
        verify(dao, never()).delete(any(Team.class));
    }

    @Test(expected = ConflictException.class)
    public void deleteChurchTeam() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockTeams.get(2)));

        service.delete(1);

        verify(groupDao, never()).getGroupByChurchIdAndChurchGroupTrue(anyInt());
        verify(userDao, never()).save(any(User.class));
        verify(dao, never()).getTeamByGroupIdAndChurchTeamTrue(anyInt());
        verify(dao, never()).delete(any(Team.class));
    }

    @Test
    public void migrateUsersToChurchTeam() {
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(createTestGroup()));
        when(dao.getTeamByGroupIdAndChurchTeamTrue(null)).thenReturn(Optional.of(mockTeams.get(2)));

        service.migrateToChurchTeam(mockTeams.get(0));

        verify(groupDao, times(1)).getGroupByChurchIdAndChurchGroupTrue(anyInt());
//        verify(userDao,times(1)).save(any(User.class));
    }

    @Test(expected = NotFoundException.class)
    public void migrateUsersToChurchTeamThatDoesNotExist() {
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.empty());

        service.migrateToChurchTeam(mockTeams.get(0));
        verify(groupDao, never()).getGroupByChurchIdAndChurchGroupTrue(anyInt());
        verify(userDao, never()).save(any(User.class));
        verify(dao, times(1)).getTeamByGroupIdAndChurchTeamTrue(null);
    }

    @Test(expected = NotFoundException.class)
    public void migrateUsersToChurchTeamWithGroupThatDoesNotExist() {
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.empty());
        when(dao.getTeamByGroupIdAndChurchTeamTrue(null)).thenReturn(Optional.empty());

        service.migrateToChurchTeam(mockTeams.get(0));
        verify(groupDao, never()).getGroupByChurchIdAndChurchGroupTrue(anyInt());
        verify(userDao, never()).save(any(User.class));
        verify(dao, never()).getTeamByGroupIdAndChurchTeamTrue(null);
    }


    private Group createTestGroup() {
        Group group = new Group();
        Team team = new Team();
        team.setGroupId(1);
        team.setId(1);
        team.setChurchTeam(true);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team);
        group.setTeams(teams);

        return group;
    }
}
