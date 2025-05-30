package org.zionusa.management.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.role.Role;
import org.zionusa.management.domain.title.TitleDao;
import org.zionusa.management.exception.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserDao dao;

    @Mock
    private TeamDao teamDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private ChurchDao churchDao;

    @Mock
    private UserApplicationRoleDao userApplicationRoleDao;

    @Mock
    private UserPreferencesDao userPreferencesDao;

    @Mock
    private TitleDao titleDao;

    @Mock
    private UserPictureDao userPictureDao;

    private List<User> mockUsers;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockUsersPath = "src/test/resources/users.json";

        byte[] usersData = Files.readAllBytes(Paths.get(mockUsersPath));
        mockUsers = mapper.readValue(usersData, new TypeReference<List<User>>() {
        });

        when(dao.findAll()).thenReturn(mockUsers);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockUsers.get(0)));
    }

    @Test
    public void getAll() {
        List<User> users = service.getAll(null);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(mockUsers.size());

    }

    @Test
    public void getByGenderExists() {
        when(dao.getUsersByGenderOrderByFirstNameAsc(anyString())).thenReturn(mockUsers);
        List<User> users = service.getByGender("M");

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(mockUsers.size());
    }

    @Test
    public void getByGenderNotExists() {
        when(dao.getUsersByGenderOrderByFirstNameAsc(anyString())).thenReturn(null);
        List<User> users = service.getByGender("F");

        assertThat(users).isEmpty();
    }

    @Test
    public void getByUserName() {
        when(dao.getUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(mockUsers.get(0)));

        User user = service.getByUsername("test@test.com");

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(mockUsers.get(0).getId());
    }

    @Test(expected = NotFoundException.class)
    public void getByUserNameThatDoesNotExist() {
        when(dao.getUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        service.getByUsername("test@test.com");
    }

    @Test
    public void getById() throws javassist.NotFoundException {
        User user = service.getById(1);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(mockUsers.get(0).getId());
        assertThat(user.getTeamId()).isEqualTo(mockUsers.get(0).getTeamId());
        assertThat(user.getTeam()).isEqualTo(mockUsers.get(0).getTeam());
        assertThat(user.getFirstName()).isEqualTo(mockUsers.get(0).getFirstName());
        assertThat(user.getLastName()).isEqualTo(mockUsers.get(0).getLastName());
        assertThat(user.getMiddleName()).isEqualTo(mockUsers.get(0).getMiddleName());
        assertThat(user.getFirstName()).isEqualTo(mockUsers.get(0).getFirstName());
        assertThat(user.getActiveDirectoryId()).isEqualTo(mockUsers.get(0).getActiveDirectoryId());
        assertThat(user.getDisplayName()).isEqualTo(mockUsers.get(0).getDisplayName());
        assertThat(user.getGender()).isEqualTo(mockUsers.get(0).getGender());
        assertThat(user.getUsername()).isEqualTo(mockUsers.get(0).getUsername());
        assertThat(user.getAccessId()).isEqualTo(mockUsers.get(0).getAccessId());
        assertThat(user.getAccess()).isEqualTo(mockUsers.get(0).getAccess());
        assertThat(user.getRoleId()).isEqualTo(mockUsers.get(0).getRoleId());
        assertThat(user.getRole()).isEqualTo(mockUsers.get(0).getRole());
        assertThat(user.getTitleId()).isEqualTo(mockUsers.get(0).getTitleId());
        assertThat(user.getTitle()).isEqualTo(mockUsers.get(0).getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void getUserThatDoesNotExistById() throws javassist.NotFoundException {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.getById(0);

    }

//    @Test
//    public void saveUser() {
//        when(dao.save(any(User.class))).thenReturn(mockUsers.get(0));
//        when(titleDao.getOne(anyInt())).thenReturn(new Title(1, "Mr.", "Mister", 11));
//        when(userApplicationRoleDao.findByUserId(anyInt())).thenReturn(new ArrayList<>());
//
//        Church mockChurch = new Church();
//        Group mockGroup = new Group();
//        Team mockTeam = new Team();
//
//        mockTeam.setGroupId(1);
//        mockGroup.setChurchId(1);
//
//        when(churchDao.findById(anyInt())).thenReturn(Optional.of(mockChurch));
//        when(groupDao.findById(anyInt())).thenReturn(Optional.of(mockGroup));
//        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockTeam));
//
//        User user = service.save(new User());
//
//        assertThat(user).isNotNull();
//        //assertThat(user.getId()).isEqualTo(mockUsers.get(0).getId());
//    }

    @Test
    public void archiveMember() {
        Team userTeam = new Team();
        userTeam.setGroupId(1);
        Group userGroup = new Group();
        userGroup.setChurchId(1);
        User user = createUser(new Role(1, "Member"));

        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
        when(dao.save(any(User.class))).thenReturn(user);
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));

        service.archive(1);

        verify(teamDao, never()).getTeamByLeaderId(null);
        verify(groupDao, never()).getGroupByLeaderId(null);
        verify(churchDao, never()).getChurchByLeaderId(null);
    }

//    @Test
//    public void archiveTeamLeader() {
//        User user = createUser(new Role(1, "Team Leader"));
//        Team userTeam = new Team();
//        userTeam.setId(1);
//        userTeam.setGroupId(1);
//        Group userGroup = new Group();
//        userGroup.setChurchId(1);
//
//        when(teamDao.getTeamByLeaderId(null)).thenReturn(Optional.of(new Team()));
//        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
//        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
//        when(dao.save(any(User.class))).thenReturn(user);
//        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
//        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
//        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));
//
//        service.archive(1);
//
//        verify(teamDao, times(1)).getTeamByLeaderId(null);
//        verify(teamDao, times(1)).save(any(Team.class));
//        verify(groupDao, never()).getGroupByLeaderId(null);
//        verify(groupDao, never()).save(any(Group.class));
//        verify(churchDao, never()).getChurchByLeaderId(null);
//        verify(churchDao, never()).save(any(Church.class));
//    }

//    @Test(expected = NotFoundException.class)
//    public void archiveTeamLeaderWhereTeamDoesNotExist() {
//        User user = createUser(new Role(1, "Team Leader"));
//        when(teamDao.getTeamByLeaderId(null)).thenReturn(Optional.empty());
//        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
//        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
//        when(dao.save(any(User.class))).thenReturn(user);
//
//        service.archive(1);
//
//    }

//    @Test
//    public void archiveGroupLeader() {
//        Team userTeam = new Team();
//        userTeam.setGroupId(1);
//        Group userGroup = new Group();
//        userGroup.setChurchId(1);
//        User user = createUser(new Role(1, "Group Leader"));
//
//        when(groupDao.getGroupByLeaderId(null)).thenReturn(Optional.of(new Group()));
//        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
//        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
//        when(dao.save(any(User.class))).thenReturn(user);
//        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
//        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
//        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));
//
//        service.archive(1);
//
//        verify(teamDao, never()).getTeamByLeaderId(null);
//        verify(teamDao, never()).save(any(Team.class));
//        verify(groupDao, times(1)).getGroupByLeaderId(null);
//        verify(groupDao, times(1)).save(any(Group.class));
//        verify(churchDao, never()).getChurchByLeaderId(null);
//        verify(churchDao, never()).save(any(Church.class));
//    }

//    @Test(expected = NotFoundException.class)
//    public void archiveGroupLeaderWhereTeamDoesNotExist() {
//        User user = createUser(new Role(1, "Group Leader"));
//        when(groupDao.getGroupByLeaderId(null)).thenReturn(Optional.empty());
//        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
//        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
//        when(dao.save(any(User.class))).thenReturn(user);
//
//        service.archive(1);
//
//    }

//    @Test
//    public void archiveChurchLeader() {
//        Team userTeam = new Team();
//        userTeam.setGroupId(1);
//        Group userGroup = new Group();
//        userGroup.setChurchId(1);
//        User user = createUser(new Role(1, "Church Leader"));
//
//        when(churchDao.getChurchByLeaderId(null)).thenReturn(Optional.of(new Church()));
//        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
//        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
//        when(dao.save(any(User.class))).thenReturn(user);
//        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
//        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
//        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));
//
//        service.archive(1);
//
//        verify(teamDao, never()).getTeamByLeaderId(null);
//        verify(teamDao, never()).save(any(Team.class));
//        verify(groupDao, never()).getGroupByLeaderId(null);
//        verify(churchDao, times(1)).save(any(Church.class));
//    }

//    @Test(expected = NotFoundException.class)
//    public void archiveChurchLeaderWhereTeamDoesNotExist() {
//        User user = createUser(new Role(1, "Church Leader"));
//        when(churchDao.getChurchByLeaderId(null)).thenReturn(Optional.empty());
//        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(user.getTeam()));
//        when(dao.findById(anyInt())).thenReturn(Optional.of(user));
//        when(dao.save(any(User.class))).thenReturn(user);
//
//        service.archive(1);
//
//    }

    @Test
    public void deleteUser() {
        User user = createUser(new Role(1, "Church Leader"));
        user.setApplicationRoles(new ArrayList<>());

        when(userPreferencesDao.getAllByUserId(anyInt())).thenReturn(new ArrayList<>());
        when(userPictureDao.findByUserId(anyInt())).thenReturn(null);
        when(teamDao.getTeamByLeaderId(anyInt())).thenReturn(Optional.empty());
        when(groupDao.getGroupByLeaderId(anyInt())).thenReturn(Optional.empty());
        when(churchDao.getChurchByLeaderId(anyInt())).thenReturn(Optional.empty());
        when(dao.findById(anyInt())).thenReturn(Optional.of(user));

        service.delete(1);

        verify(dao, times(1)).delete(any(User.class));
    }

    @Test
    public void deleteUserAlreadyRemoved() {
        when(userPreferencesDao.getAllByUserId(anyInt())).thenReturn(new ArrayList<>());
        when(userPictureDao.findByUserId(anyInt())).thenReturn(null);
        when(teamDao.getTeamByLeaderId(anyInt())).thenReturn(Optional.empty());
        when(groupDao.getGroupByLeaderId(anyInt())).thenReturn(Optional.empty());
        when(churchDao.getChurchByLeaderId(anyInt())).thenReturn(Optional.empty());
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.delete(1);

        verify(dao, times(0)).delete(any(User.class));
    }

    @Test
    public void CheckNotAvailableUserName() {
        when(dao.getUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(mockUsers.get(1)));

        boolean result = service.isUsernameAvailable("jim.jones@zionusa.org");

        assertThat(result).isFalse();
    }

    @Test
    public void CheckAvailableUserName() {
        when(dao.getUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());

        boolean result = service.isUsernameAvailable("test@test");

        assertThat(result).isTrue();
    }

    @Test
    public void reactivateUser() {
        Team userTeam = new Team();
        userTeam.setGroupId(1);
        Group userGroup = new Group();
        userGroup.setChurchId(1);

        when(dao.save(any(User.class))).thenReturn(mockUsers.get(0));
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));

        User result = service.reactivateUser(1);

        assertThat(result.isArchived()).isFalse();
    }

    @Test(expected = NotFoundException.class)
    public void reactivateUserThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.reactivateUser(1);

    }

    @Test
    public void enableUser() {
        Team userTeam = new Team();
        userTeam.setGroupId(1);
        Group userGroup = new Group();
        userGroup.setChurchId(1);
        when(dao.save(any(User.class))).thenReturn(mockUsers.get(0));
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));

        User result = service.enableUser(1);

        assertThat(result.isEnabled()).isTrue();
    }

    @Test(expected = NotFoundException.class)
    public void enableUserThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.enableUser(1);

    }

    @Test
    public void disableUser() {
        Team userTeam = new Team();
        userTeam.setGroupId(1);
        Group userGroup = new Group();
        userGroup.setChurchId(1);

        when(dao.save(any(User.class))).thenReturn(mockUsers.get(0));
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(userTeam));
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(userGroup));
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(new Church()));

        User result = service.disableUser(1);

        assertThat(result.isEnabled()).isFalse();
    }

    @Test(expected = NotFoundException.class)
    public void disableUserThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.disableUser(1);

    }

    @Test
    public void getDeactivatedUsers() {
        List<User> users = new ArrayList<>();
        users.add(mockUsers.get(1));

        when(dao.getUsersByArchivedTrue()).thenReturn(users);

        List<User> deactivatedUsers = service.getDeactivatedUsers();

        assertThat(deactivatedUsers).isNotNull();
        assertThat(deactivatedUsers).isNotEmpty();

    }

    @Test
    public void getEmptyDeactivatedUsers() {

        List<User> deactivatedUsers = service.getDeactivatedUsers();

        assertThat(deactivatedUsers).isNotNull();
        assertThat(deactivatedUsers).isEmpty();
    }


    @Test
    public void getDisabledUsers() {
        List<User> users = new ArrayList<>();
        users.add(mockUsers.get(1));

        when(dao.getUsersByEnabledFalse()).thenReturn(users);

        List<User> deactivatedUsers = service.getDisabledUsers();

        assertThat(deactivatedUsers).isNotNull();
        assertThat(deactivatedUsers).isNotEmpty();
    }

    @Test
    public void getEmptyDisabledUsers() {
        List<User> deactivatedUsers = service.getDisabledUsers();

        assertThat(deactivatedUsers).isNotNull();
        assertThat(deactivatedUsers).isEmpty();
    }


    @Test
    public void expireCache() {
        service.expireCache();
    }


    private User createUser(Role role) {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRoleId(1);
        user.setRole(role);
        user.setTitleId(1);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setAccountNonExpired(true);

        Team team = new Team();
        team.setId(1);
        team.setGroupId(1);

        user.setTeam(team);

        return user;
    }

}
