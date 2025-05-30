package org.zionusa.management.service;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.dao.ChurchDao;
import org.zionusa.management.dao.GroupDao;
import org.zionusa.management.dao.TeamDao;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.Group;
import org.zionusa.management.domain.Team;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.access.AccessDao;
import org.zionusa.management.domain.role.RoleDao;
import org.zionusa.management.exception.ConflictException;
import org.zionusa.management.exception.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ChurchServiceTest {

    @InjectMocks
    private ChurchService service;

    @Mock
    private ChurchDao dao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TeamDao teamDao;

    @Mock
    private RoleDao roleDao;

    @Mock
    private AccessDao accessDao;

    private List<Church> mockChurches;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockChurchesPath = "src/test/resources/churches.json";

        byte[] churchesData = Files.readAllBytes(Paths.get(mockChurchesPath));
        mockChurches = mapper.readValue(churchesData, new TypeReference<List<Church>>() {
        });

        when(dao.findAll()).thenReturn(mockChurches);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(0)));
    }

    @Test
    public void getAllChurches() {
        when(dao.getAllByArchivedFalse()).thenReturn(mockChurches);

        List<Church> churches = service.getAll(null);

        assertThat(churches).isNotNull();
        assertThat(churches.size()).isEqualTo(4);

    }

    @Test
    public void getChurchById() {
        Church church = service.getById(1);

        assertThat(church).isNotNull();
        assertThat(church.getId()).isNotNull();
        assertThat(church.getId()).isEqualTo(mockChurches.get(0).getId());
        assertThat(church.getParentChurchId()).isEqualTo(mockChurches.get(0).getParentChurchId());
        assertThat(church.getLeaderId()).isEqualTo(mockChurches.get(0).getLeaderId());
        assertThat(church.getName()).isEqualTo(mockChurches.get(0).getName());
        assertThat(church.getAddress()).isEqualTo(mockChurches.get(0).getAddress());
        assertThat(church.getCity()).isEqualTo(mockChurches.get(0).getCity());
        assertThat(church.getPostalCode()).isEqualTo(mockChurches.get(0).getPostalCode());
        assertThat(church.getState()).isEqualTo(mockChurches.get(0).getState());
        assertThat(church.getEmail()).isEqualTo(mockChurches.get(0).getEmail());
        assertThat(church.getPhone()).isEqualTo(mockChurches.get(0).getPhone());
        assertThat(church.getLeaderId()).isEqualTo(mockChurches.get(0).getLeaderId());
        assertThat(church.getTypeId()).isEqualTo(mockChurches.get(0).getTypeId());
        assertThat(church.getType()).isEqualTo(mockChurches.get(0).getType());
        assertThat(church.getBranches()).isEqualTo(mockChurches.get(0).getBranches());

    }

    @Test(expected = NotFoundException.class)
    public void getChurchByIdThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.getById(1);
    }

    @Test
    public void getLeader() {
        User leader = service.getLeader(1);

        assertThat(leader).isNotNull();
        assertThat(leader.getId()).isEqualTo(mockChurches.get(0).getLeader().getId());
    }

    @Test
    public void getLeaderThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(1)));

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
    public void getGroups() {
        when(groupDao.getGroupsByChurchIdAndArchivedIsFalse(anyInt())).thenReturn(mockChurches.get(0).getGroups());
        List<Group> groups = service.getGroups(0);

        assertThat(groups).isNotEmpty();
        assertThat(groups.size()).isEqualTo(mockChurches.get(0).getGroups().size());
        assertThat(groups.get(0).getId()).isEqualTo(mockChurches.get(0).getGroups().get(0).getId());
    }

    @Test
    public void getGroupsThatDoNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(1)));

        List<Group> groups = service.getGroups(2);

        assertThat(groups).isEmpty();
    }

    @Test
    public void getMembers() {
        List<User> members = service.getMembers(1);
        List<User> mockMembers = new ArrayList<>();

        for (Team team : mockChurches.get(0).getGroups().get(0).getTeams()) {
            mockMembers.addAll(team.getMembers());
        }

        assertThat(members).isNotEmpty();
        assertThat(members.size()).isEqualTo(mockMembers.size());
        assertThat(mockMembers).containsAll(members);
    }

    @Test
    public void getMembersThatDoNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(1)));

        List<User> members = service.getMembers(1);

        assertThat(members).isEmpty();
    }

    @Test
    public void getMembersByGender() {
        List<User> members = service.getMembersByGender(1, "M");
        List<User> mockMembers = new ArrayList<>();

        for (Team team : mockChurches.get(0).getGroups().get(0).getTeams()) {
            mockMembers.addAll(team.getMembers());
        }

        assertThat(members).isNotEmpty();
        assertThat(members.size()).isEqualTo(mockMembers.size());
        assertThat(mockMembers).containsAll(members);
    }

    @Test
    public void getMembersByGenderThatDoNotExist() {
        List<User> members = service.getMembersByGender(1, "F");

        assertThat(members).isEmpty();
    }

    @Test
    public void getBranchChurches() {
        when(dao.getByParentChurchId(anyInt())).thenReturn(mockChurches.get(0).getBranches());
        List<Church> branchChurches = service.getBranchChurches(1);
        List<Church> mockBranchChurches = mockChurches.get(0).getBranches();

        assertThat(branchChurches).isNotEmpty();
        assertThat(branchChurches.size()).isEqualTo(mockBranchChurches.size());
        assertThat(mockBranchChurches).containsAll(branchChurches);
    }

    @Test
    public void getBranchChurchesThatDoNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(2)));

        List<Church> branchChurches = service.getBranchChurches(2);

        assertThat(branchChurches).isEmpty();
    }

    @Test
    public void checkGroupMembership() {
        boolean membershipCheck1 = service.checkGroupMembership(1, 3);
        boolean membershipCheck2 = service.checkGroupMembership(1, 2);
        boolean membershipCheck3 = service.checkGroupMembership(1, 47);


        assertThat(membershipCheck1).isTrue();
        assertThat(membershipCheck2).isFalse();
        assertThat(membershipCheck3).isFalse();
    }

    @Test
    public void checkGroupMembershipOfChurchThatDoesNotExist() {

        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        boolean membershipCheck1 = service.checkGroupMembership(2, 3);
        boolean membershipCheck2 = service.checkGroupMembership(2, 2);
        boolean membershipCheck3 = service.checkGroupMembership(2, 47);


        assertThat(membershipCheck1).isFalse();
        assertThat(membershipCheck2).isFalse();
        assertThat(membershipCheck3).isFalse();
    }

    @Test
    public void checkUserMembership() {
        boolean membershipCheck1 = service.checkUserMembership(1, 1);
        boolean membershipCheck2 = service.checkUserMembership(1, 2);

        assertThat(membershipCheck1).isFalse();
        assertThat(membershipCheck2).isTrue();
    }

    @Test
    public void checkUserMembershipOfChurchThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        boolean membershipCheck1 = service.checkUserMembership(43, 1);
        boolean membershipCheck2 = service.checkUserMembership(12, 1);

        assertThat(membershipCheck1).isFalse();
        assertThat(membershipCheck2).isFalse();
    }

    @Test
    public void cleanUpGroup() {
        when(groupDao.getGroupsByChurchId(anyInt())).thenReturn(mockChurches.get(2).getGroups());

        service.cleanUpGroups(1);

        verify(groupDao, times(1)).save(any(Group.class));
    }

    @Test
    public void saveChurch() throws Exception {
        when(dao.save(any(Church.class))).thenReturn(mockChurches.get(0));
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(any(Integer.class))).thenReturn(Optional.empty());
        when(groupDao.save(any(Group.class))).thenReturn(mockChurches.get(0).getGroups().get(0));
        when(teamDao.save(any(Team.class))).thenReturn(null);

        try {
            Church returnedChurch = service.saveChurch(mockChurches.get(0));

            assertThat(returnedChurch).isNotNull();
            assertThat(returnedChurch.getId()).isEqualTo(mockChurches.get(0).getId());

            verify(groupDao, times(1)).getGroupByChurchIdAndChurchGroupTrue(any(Integer.class));
            verify(groupDao, times(1)).save(any(Group.class));
            verify(teamDao, times(1)).save(any(Team.class));
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void saveChurchThatExists() throws Exception {
        when(dao.save(any(Church.class))).thenReturn(mockChurches.get(0));
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(mockChurches.get(0).getGroups().get(0)));
        when(userDao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(0).getGroups().get(0).getTeams().get(1).getMembers().get(0)));

        try {
            Church returnedChurch = service.saveChurch(mockChurches.get(0));

            assertThat(returnedChurch).isNotNull();
            assertThat(returnedChurch.getId()).isEqualTo(mockChurches.get(0).getId());

            verify(groupDao, times(1)).getGroupByChurchIdAndChurchGroupTrue(any(Integer.class));
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test()
    public void SaveChurchThatDoesNotExist() throws Exception {
        try {
            service.saveChurch(null);

            fail("Should throw a ConflictException");
        } catch (ConflictException e) {
            assertThat(e.getMessage()).isEqualTo("Cannot save an empty church!");
        }
    }

    @Test
    public void deleteChurch() {
        doNothing().when(dao).delete(any(Church.class));

        service.deleteChurch(1);

        verify(dao, times(1)).delete(any(Church.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteChurchThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.deleteChurch(2);

        verify(dao, times(0)).delete(any(Church.class));
    }


    /*@Test
    public void moveLeader() {
        Role role = new Role(1,"Church Leader");
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        Access access = new Access(1, "Church");
        List<Access> accessList = new ArrayList<>();
        accessList.add(access);
        Church mockChurch = mockChurches.get(0);

        when(userDao.findById(anyInt())).thenReturn(Optional.of(mockChurch.getLeader()));
        when(groupDao.getGroupByChurchIdAndChurchGroupTrue(anyInt())).thenReturn(Optional.of(mockChurch.getGroups().get(0)));
        when(teamDao.getTeamByGroupIdAndChurchTeamTrue(anyInt())).thenReturn(Optional.of(mockChurch.getGroups().get(0).getTeams().get(0)));
        when(roleDao.findAll()).thenReturn(roleList);
        when(accessDao.findAll()).thenReturn(accessList);
        when(dao.save(any(Church.class))).thenReturn(null);
        when(userDao.save(any(User.class))).thenReturn(null);

        service.moveLeader(1,1,mockChurch);

        verify(dao,times(2)).save(any(Church.class));
        verify(dao,times(1)).findById(anyInt());
        verify(userDao, times(1)).save(any(User.class));
        verify(userDao, times(1)).findById(anyInt());
        verify(roleDao, times(1)).findAll();
        verify(accessDao, times(1)).findAll();
    } */

    /*@Test(expected = NotFoundException.class)
    public void moveLeaderThatDoesNotExist() {
        when(userDao.findById(anyInt())).thenReturn(Optional.empty());

        service.moveLeader(1,1,mockChurches.get(0));
    } */

    /*@Test(expected = NotFoundException.class)
    public void MoveLeaderOldChurchDoesNotExist() {
        when(userDao.findById(anyInt())).thenReturn(Optional.of(mockChurches.get(0).getLeader()));
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.moveLeader(1,1,mockChurches.get(0));
        verify(userDao, times(1)).findById(anyInt());
    }*/

}
