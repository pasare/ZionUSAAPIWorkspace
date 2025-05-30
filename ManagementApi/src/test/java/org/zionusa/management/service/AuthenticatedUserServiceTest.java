package org.zionusa.management.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.access.Access;
import org.zionusa.management.domain.role.Role;
import org.zionusa.management.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticatedUserServiceTest {

    @InjectMocks
    private AuthenticatedUserService service;

    @Mock
    private ChurchDao churchDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private TeamDao teamDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TransferRequestDao transferRequestDao;

    @Mock
    private MessageSource messages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadByUsername() {
        when(userDao.getUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(createUser(1, 1, 1, 1, new Access(1, "mock access"))));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, new Access(1, "mock access")));
        ManagementAuthenticatedUser returnedUser = service.loadUserByUsername("test@test.com");

        assertThat(authenticatedUser.getId()).isEqualTo(returnedUser.getId());
        assertThat(authenticatedUser.getRole()).isEqualTo(returnedUser.getRole());
        assertThat(authenticatedUser.getTeamId()).isEqualTo(returnedUser.getTeamId());
        assertThat(authenticatedUser.getGroupId()).isEqualTo(returnedUser.getGroupId());
        assertThat(authenticatedUser.getChurchId()).isEqualTo(returnedUser.getChurchId());
        assertThat(authenticatedUser.getChurchName()).isEqualTo(returnedUser.getChurchName());
//        assertThat(authenticatedUser.getGroupName()).isEqualTo(returnedUser.getGroupName());
        assertThat(authenticatedUser.getGroupEffectiveDate()).isEqualTo(returnedUser.getGroupEffectiveDate());
//        assertThat(authenticatedUser.getTeamName()).isEqualTo(returnedUser.getTeamName());
        assertThat(authenticatedUser.getTeamEffectiveDate()).isEqualTo(returnedUser.getTeamEffectiveDate());
        assertThat(authenticatedUser.getAccess()).isEqualTo(returnedUser.getAccess());


        verify(userDao, times(1)).getUserByUsernameIgnoreCase(anyString());

    }

    @Test
    public void loadUserByActiveDirectoryId() {
        when(userDao.getUserByActiveDirectoryIdIgnoreCase(anyString())).thenReturn(Optional.of(createUser(1, 1, 1, 1, new Access(1, "mock access"))));

        service.loadUserByActiveDirectoryId("123-456-76");

        verify(userDao, times(1)).getUserByActiveDirectoryIdIgnoreCase(anyString());
    }

    @Test
    public void setAuthenticatedUser() {
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, new Access(1, "mock access")));
        service.setAuthenticatedUser(authenticatedUser);
    }

    @Test
    public void canAccessChurchByAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));
        boolean result = service.canAccessChurch(authenticatedUser, 1);
        assertThat(result).isTrue();

    }

    @Test
    public void canAccessChurchBySameChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        User mockUser = createUser(1, 1, 1, 1, access);
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(mockUser);
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam().getGroup().getChurch()));

        boolean result = service.canAccessChurch(authenticatedUser, 1);
        assertThat(result).isTrue();

    }

    @Test
    public void canAccessChurchByDifferentChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        User mockUser = createUser(1, 1, 1, 1, access);
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(mockUser);
        mockUser.getTeam().getGroup().getChurch().setId(2);
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam().getGroup().getChurch()));

        boolean result = service.canAccessChurch(authenticatedUser, 2);
        assertThat(result).isFalse();

    }

    @Test
    public void canAccessChurchBySameGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        User mockUser = createUser(1, 1, 1, 1, access);
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(mockUser);
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam().getGroup().getChurch()));

        boolean result = service.canAccessChurch(authenticatedUser, 1);
        assertThat(result).isTrue();

    }

    @Test
    public void canAccessChurchByDifferentGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        User mockUser = createUser(1, 1, 2, 2, access);
        Church mockChurch = mockUser.getTeam().getGroup().getChurch();
        mockChurch.setId(1);
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(mockUser);
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(mockChurch));

        boolean result = service.canAccessChurch(authenticatedUser, 1);
        assertThat(result).isFalse();

    }

    @Test(expected = NotFoundException.class)
    public void canAccessChurchThatDoesNotExist() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 2, 2, access));

        boolean result = service.canAccessChurch(authenticatedUser, 1);
        assertThat(result).isFalse();
    }

    @Test
    public void canAccessChurchByMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        User mockUser = createUser(1, 1, 1, 1, access);
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(mockUser);
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam().getGroup().getChurch()));

        boolean result = service.canAccessChurch(authenticatedUser, 1);
        assertThat(result).isTrue();

    }

    @Test
    public void canModifyChurchByAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 2, 1, access));

        User mockUser = createUser(2, 1, 1, 1, null);

        boolean result = service.canModifyChurch(authenticatedUser, mockUser.getTeam().getGroup().getChurch());
        assertThat(result).isTrue();

    }

    @Test
    public void canModifyChurchBySameChurch() {

        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        User mockUser = createUser(2, 1, 1, 1, null);
        Church mockChurch = mockUser.getTeam().getGroup().getChurch();

        //existing church
        boolean result = service.canModifyChurch(authenticatedUser, mockChurch);
        assertThat(result).isTrue();

        //new church
        mockChurch.setId(null);
        boolean result2 = service.canModifyChurch(authenticatedUser, mockChurch);
        assertThat(result).isTrue();
    }

    @Test
    public void canModifyChurchByDifferentChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 2, access));

        User mockUser = createUser(2, 1, 1, 1, null);

        boolean result = service.canModifyChurch(authenticatedUser, mockUser.getTeam().getGroup().getChurch());
        assertThat(result).isFalse();

    }

    @Test
    public void canModifyChurchByGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        User mockUser = createUser(2, 1, 1, 1, null);

        boolean result = service.canModifyChurch(authenticatedUser, mockUser.getTeam().getGroup().getChurch());
        assertThat(result).isFalse();

    }

    @Test
    public void canModifyChurchByMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        User mockUser = createUser(2, 1, 1, 1, null);

        boolean result = service.canModifyChurch(authenticatedUser, mockUser.getTeam().getGroup().getChurch());
        assertThat(result).isFalse();

    }

    @Test
    public void canAccessGroupByAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 1);
        assertThat(result).isTrue();
    }

    @Test
    public void canAccessGroupBySameChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 1);
        assertThat(result).isTrue();
    }

    @Test
    public void canAccessGroupByDifferentChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 3, 12, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 1);
        assertThat(result).isFalse();
    }

    @Test
    public void canAccessGroupBySameGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 1);
        assertThat(result).isTrue();
    }

    @Test
    public void canAccessGroupByDifferentGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 5, 1, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 5);
        assertThat(result).isFalse();
    }

    @Test
    public void canAccessGroupBySameTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 11, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 1);
        assertThat(result).isTrue();
    }

    @Test
    public void canAccessGroupByDifferentTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        when(groupDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 10, 3, 12, null).getTeam().getGroup()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessGroup(authenticatedUser, 1);
        assertThat(result).isFalse();
    }

    @Test
    public void canModifyGroupByAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));
        Group group = createUser(1, 10, 3, 12, null).getTeam().getGroup();

        boolean result = service.canModifyGroup(authenticatedUser, group);
        assertThat(result).isTrue();
    }

    @Test
    public void canModifyGroupBySameChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));
        Group group = createUser(1, 1, 1, 1, null).getTeam().getGroup();

        boolean result = service.canModifyGroup(authenticatedUser, group);
        assertThat(result).isTrue();
    }

    @Test
    public void canModifyGroupByDifferentChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 4, 12, access));
        Group group = createUser(1, 1, 1, 1, null).getTeam().getGroup();

        boolean result = service.canModifyGroup(authenticatedUser, group);
        assertThat(result).isFalse();
    }

    @Test
    public void canModifyGroupBySameGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));
        Group group = createUser(1, 1, 1, 1, null).getTeam().getGroup();

        boolean result = service.canModifyGroup(authenticatedUser, group);
        assertThat(result).isTrue();
    }

    @Test
    public void canModifyGroupByDifferentGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 3, 5, 1, access));
        Group group = createUser(1, 1, 1, 1, null).getTeam().getGroup();

        boolean result = service.canModifyGroup(authenticatedUser, group);
        assertThat(result).isFalse();
    }

    @Test
    public void canModifyGroupByMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));
        Group group = createUser(1, 1, 1, 1, null).getTeam().getGroup();

        boolean result = service.canModifyGroup(authenticatedUser, group);
        assertThat(result).isFalse();
    }

    @Test
    public void canAccessUserByAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isTrue();

    }

    @Test
    public void canAccessUserBySameChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isTrue();

    }

    @Test
    public void canAccessUserByDifferentChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 5, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isFalse();

    }

    @Test
    public void canAccessUserBySameGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isTrue();

    }

    @Test
    public void canAccessUserByDifferentGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 5, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isFalse();
    }

    @Test
    public void canAccessUserBySameTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isTrue();
    }

    @Test
    public void canAccessUserByDifferentTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 3, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isFalse();
    }

    @Test
    public void canAccessUserBySameMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(1, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 1);

        assertThat(result).isTrue();
    }

    @Test
    public void canAccessUserByDifferentMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        when(userDao.findById(anyInt())).thenReturn(Optional.of(createUser(2, 1, 1, 1, null)));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canAccessUser(authenticatedUser, 3);

        assertThat(result).isFalse();
    }

    @Test
    public void canModifyUserByAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());
        User mockUser = createUser(1, 1, 1, 1, null);
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isTrue();
    }

    @Test
    public void canModifyUserBySameChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        User mockUser = createUser(1, 1, 1, 1, null);
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isTrue();
    }

    @Test
    public void canModifyUserByDifferentChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        User mockUser = createUser(1, 1, 1, 2, null);

        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isFalse();
    }

    @Test
    public void canModifyUserBySameGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        User mockUser = createUser(1, 1, 1, 1, null);
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));

        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isTrue();
    }

    @Test
    public void canModifyUserByDifferentGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        User mockUser = createUser(2, 1, 4, 1, null);
        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isFalse();
    }

    @Test
    public void canModifyUserBySameTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        User mockUser = createUser(2, 1, 1, 1, null);

        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isTrue();
    }

    @Test
    public void canModifyUserByDifferentTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        User mockUser = createUser(2, 5, 1, 1, null);

        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isFalse();
    }

    @Test
    public void canModifyUserBySameMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        User mockUser = createUser(1, 1, 1, 1, null);

        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isTrue();
    }

    @Test
    public void canModifyUserByDifferentMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        User mockUser = createUser(2, 1, 1, 1, null);

        when(teamDao.findById(anyInt())).thenReturn(Optional.of(mockUser.getTeam()));
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        boolean result = service.canModifyUser(authenticatedUser, mockUser);

        assertThat(result).isFalse();
    }

    @Test
    public void canProcessTransferAsAdmin() {
        Access access = new Access(1, EUserAccess.ADMIN.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        when(transferRequestDao.findById(anyInt())).thenReturn(Optional.of(createTransferRequest()));

        boolean result = service.canProcessTransfer(authenticatedUser, 1);

        assertThat(result).isTrue();
    }

    @Test
    public void canProcessTransferAsChurch() {
        Access access = new Access(1, EUserAccess.CHURCH.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        when(transferRequestDao.findById(anyInt())).thenReturn(Optional.of(createTransferRequest()));
        boolean result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isTrue();

        //different church
        authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 2, access));
        result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isTrue();
    }

    @Test
    public void canProcessTransferAsGroup() {
        Access access = new Access(1, EUserAccess.GROUP.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        when(transferRequestDao.findById(anyInt())).thenReturn(Optional.of(createTransferRequest()));
        boolean result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isTrue();

        //different church
        authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 2, 1, access));
        result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isFalse();
    }

    @Test
    public void canProcessTransferAsTeam() {
        Access access = new Access(1, EUserAccess.TEAM.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        when(transferRequestDao.findById(anyInt())).thenReturn(Optional.of(createTransferRequest()));
        boolean result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isTrue();

        //different church
        authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 2, 1, 1, access));
        result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isFalse();
    }

    @Test
    public void canProcessTransferAsMember() {
        Access access = new Access(1, EUserAccess.MEMBER.getValue());
        ManagementAuthenticatedUser authenticatedUser = new ManagementAuthenticatedUser(createUser(1, 1, 1, 1, access));

        when(transferRequestDao.findById(anyInt())).thenReturn(Optional.of(createTransferRequest()));
        boolean result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isFalse();

        //different church
        authenticatedUser = new ManagementAuthenticatedUser(createUser(2, 1, 1, 1, access));
        result = service.canProcessTransfer(authenticatedUser, 1);
        assertThat(result).isFalse();

    }

    private User createUser(Integer userId, Integer teamId, Integer groupId, Integer churchId, Access access) {
        User user = new User();
        Team team = new Team();
        Group group = new Group();
        Church church = new Church();

        user.setRole(new Role(1, "fake role"));

        user.setUsername("test@test.com");
        user.setActiveDirectoryId("123-456-76");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAccess(access);

        user.setId(userId);
        user.setTeamId(teamId);
        user.setTeam(team);

        team.setId(teamId);
        team.setGroupId(groupId);
        team.setGroup(group);

        group.setId(groupId);
        group.setChurchId(churchId);
        group.setChurch(church);
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        group.setTeams(teams);

        church.setId(churchId);

        return user;
    }

    private TransferRequest createTransferRequest() {
        TransferRequest transferRequest = new TransferRequest();

        transferRequest.setUserId(1);
        transferRequest.setNewTeamId(1);
        transferRequest.setNewGroupId(1);
        transferRequest.setNewChurchId(1);

        return transferRequest;
    }

}
