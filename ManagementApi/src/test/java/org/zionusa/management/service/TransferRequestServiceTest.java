package org.zionusa.management.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.zionusa.management.dao.ChurchDao;
import org.zionusa.management.dao.TransferRequestDao;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.access.Access;
import org.zionusa.management.enums.ETransferRequestStatus;
import org.zionusa.management.exception.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TransferRequestServiceTest {

    @InjectMocks
    private TransferRequestService service;

    @Mock
    private TransferRequestDao dao;

    @Mock
    private UserDao userDao;

    @Mock
    private ChurchDao churchDao;

    @Mock
    private MessageSource messages;

    @Mock
    private UserService userService;

    private List<TransferRequest> mockTransferRequests;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockTransferRequestsPath = "src/test/resources/transfer-requests.json";

        byte[] transferRequestData = Files.readAllBytes(Paths.get(mockTransferRequestsPath));
        mockTransferRequests = mapper.readValue(transferRequestData, new TypeReference<List<TransferRequest>>() {
        });


        when(dao.findAll()).thenReturn(mockTransferRequests);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockTransferRequests.get(0)));
        when(userService.internalSaveUser(any())).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void getAllTransferRequests() {

        List<TransferRequest> transferRequestsAll = service.getAllTransferRequests(null);
        assertThat(transferRequestsAll).hasSize(6);

        List<TransferRequest> transferRequestsPending = service.getAllTransferRequests(ETransferRequestStatus.PENDING.getValue());
        assertThat(transferRequestsPending).hasSize(3);

        List<TransferRequest> transferRequestsApproved = service.getAllTransferRequests(ETransferRequestStatus.APPROVED.getValue());
        assertThat(transferRequestsApproved).hasSize(1);

        List<TransferRequest> transferRequestsDenied = service.getAllTransferRequests(ETransferRequestStatus.DENIED.getValue());
        assertThat(transferRequestsDenied).hasSize(2);
    }

    @Test
    public void getChurchTransferRequests() {

        when(dao.getTransferRequestsByNewChurchId(anyInt())).thenReturn(mockTransferRequests);

        List<TransferRequest> transferRequestsAll = service.getBranchTransferRequests(1, null);
        assertThat(transferRequestsAll).hasSize(6);

        List<TransferRequest> transferRequestsPending = service.getBranchTransferRequests(1, ETransferRequestStatus.PENDING.getValue());
        assertThat(transferRequestsPending).hasSize(3);

        List<TransferRequest> transferRequestsApproved = service.getBranchTransferRequests(1, ETransferRequestStatus.APPROVED.getValue());
        assertThat(transferRequestsApproved).hasSize(1);

        List<TransferRequest> transferRequestsDenied = service.getBranchTransferRequests(1, ETransferRequestStatus.DENIED.getValue());
        assertThat(transferRequestsDenied).hasSize(2);

    }

    @Test
    public void getGroupTransferRequests() {

        when(dao.getTransferRequestsByNewGroupId(anyInt())).thenReturn(mockTransferRequests);

        List<TransferRequest> transferRequestsAll = service.getGroupTransferRequests(1, null);
        assertThat(transferRequestsAll).hasSize(6);

        List<TransferRequest> transferRequestsPending = service.getGroupTransferRequests(1, ETransferRequestStatus.PENDING.getValue());
        assertThat(transferRequestsPending).hasSize(3);

        List<TransferRequest> transferRequestsApproved = service.getGroupTransferRequests(1, ETransferRequestStatus.APPROVED.getValue());
        assertThat(transferRequestsApproved).hasSize(1);

        List<TransferRequest> transferRequestsDenied = service.getGroupTransferRequests(1, ETransferRequestStatus.DENIED.getValue());
        assertThat(transferRequestsDenied).hasSize(2);

    }

    @Test
    public void getTeamTransferRequests() {

        when(dao.getTransferRequestsByNewTeamId(anyInt())).thenReturn(mockTransferRequests);

        List<TransferRequest> transferRequestsAll = service.getTeamTransferRequests(1, null);
        assertThat(transferRequestsAll).hasSize(6);

        List<TransferRequest> transferRequestsPending = service.getTeamTransferRequests(1, ETransferRequestStatus.PENDING.getValue());
        assertThat(transferRequestsPending).hasSize(3);

        List<TransferRequest> transferRequestsApproved = service.getTeamTransferRequests(1, ETransferRequestStatus.APPROVED.getValue());
        assertThat(transferRequestsApproved).hasSize(1);

        List<TransferRequest> transferRequestsDenied = service.getTeamTransferRequests(1, ETransferRequestStatus.DENIED.getValue());
        assertThat(transferRequestsDenied).hasSize(2);

    }

    @Test
    public void getUserTransferRequests() {

        when(dao.getTransferRequestsByUserId(anyInt())).thenReturn(mockTransferRequests);

        List<TransferRequest> transferRequestsAll = service.getUserTransferRequests(1, null);
        assertThat(transferRequestsAll).hasSize(6);

        List<TransferRequest> transferRequestsPending = service.getUserTransferRequests(1, ETransferRequestStatus.PENDING.getValue());
        assertThat(transferRequestsPending).hasSize(3);

        List<TransferRequest> transferRequestsApproved = service.getUserTransferRequests(1, ETransferRequestStatus.APPROVED.getValue());
        assertThat(transferRequestsApproved).hasSize(1);

        List<TransferRequest> transferRequestsDenied = service.getUserTransferRequests(1, ETransferRequestStatus.DENIED.getValue());
        assertThat(transferRequestsDenied).hasSize(2);

    }

    @Test
    public void getTransferRequestsByDate() {

        when(dao.getTransferRequestsByRequestDateBetween(any(Date.class), any(Date.class))).thenReturn(mockTransferRequests);

        List<TransferRequest> transferRequests = service.getTransferRequestsBetweenDate(new Date(), new Date());

        assertThat(transferRequests).hasSize(6);

    }

    @Test
    public void save() {
        TransferRequest transferRequest = mockTransferRequests.get(0);

        //when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage(Session.getDefaultInstance(new Properties())));
        when(dao.save(any(TransferRequest.class))).thenReturn(transferRequest);
        when(churchDao.findById(anyInt())).thenReturn(Optional.of(createMockChurch()));
        when(userDao.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(userDao.save(any(User.class))).thenReturn(new User());

        TransferRequest result = service.save(transferRequest);

        assertThat(result.getId()).isEqualTo(transferRequest.getId());

        assertThat(result.getCurrentTeamId()).isEqualTo(transferRequest.getCurrentTeamId());
        assertThat(result.getCurrentTeamName()).isEqualTo(transferRequest.getCurrentTeamName());
        assertThat(result.getCurrentGroupId()).isEqualTo(transferRequest.getCurrentGroupId());
        assertThat(result.getCurrentGroupName()).isEqualTo(transferRequest.getCurrentGroupName());
        assertThat(result.getCurrentChurchId()).isEqualTo(transferRequest.getCurrentChurchId());
        assertThat(result.getCurrentChurchName()).isEqualTo(transferRequest.getCurrentChurchName());


        assertThat(result.getRequestStatus()).isEqualTo(transferRequest.getRequestStatus());
        assertThat(result.getUserId()).isEqualTo(transferRequest.getUserId());
        assertThat(result.getNewTeamId()).isEqualTo(transferRequest.getNewTeamId());
        assertThat(result.getNewTeamName()).isEqualTo(transferRequest.getNewTeamName());
        assertThat(result.getNewGroupId()).isEqualTo(transferRequest.getNewGroupId());
        assertThat(result.getNewGroupName()).isEqualTo(transferRequest.getNewGroupName());
        assertThat(result.getNewChurchId()).isEqualTo(transferRequest.getNewChurchId());
        assertThat(result.getNewChurchName()).isEqualTo(transferRequest.getNewChurchName());

        assertThat(result.getReviewerId()).isEqualTo(transferRequest.getReviewerId());
        assertThat(result.getReviewerName()).isEqualTo(transferRequest.getReviewerName());
        assertThat(result.getComment()).isEqualTo(transferRequest.getComment());

        verify(dao, times(2)).save(any(TransferRequest.class));

    }

    @Test(expected = NotFoundException.class)
    public void saveRequestToChurchThatDoesNotExist() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TransferRequest transferRequest = mockTransferRequests.get(0);

        //when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage(Session.getDefaultInstance(new Properties())));
        when(dao.save(any(TransferRequest.class))).thenReturn(transferRequest);
        when(churchDao.findById(anyInt())).thenReturn(Optional.empty());

        service.save(transferRequest);
    }


    @Test
    public void approve() {

        TransferRequest transferRequest = mockTransferRequests.get(0);
        User user = new User();
        user.setTeamId(1);

        when(dao.save(any(TransferRequest.class))).thenReturn(transferRequest);
        when(userDao.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(userDao.save(any(User.class))).thenReturn(user);

        boolean result = service.approve(1);

        assertThat(result).isTrue();
        verify(dao, times(1)).save(any(TransferRequest.class));
    }

    @Test
    public void approveTransferRequestWithError() {
        User user = new User();
        user.setId(1);
        when(userDao.findById(anyInt())).thenReturn(Optional.of(user));
        when(userService.internalSaveUser(any(User.class))).thenReturn(new User());

        boolean result = service.approve(1);

        assertThat(result).isFalse();
        verify(dao, never()).save(any(TransferRequest.class));
    }

    @Test(expected = NotFoundException.class)
    public void approveRequestThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.approve(1);
    }

    @Test(expected = NotFoundException.class)
    public void approveRequestWhereUserDoesNotExist() {

        when(userDao.findById(anyInt())).thenReturn(Optional.empty());

        service.approve(1);

    }

    @Test
    public void deny() {
        TransferRequest transferRequest = mockTransferRequests.get(0);

        when(dao.save(any(TransferRequest.class))).thenReturn(transferRequest);

        boolean result = service.deny(1);

        assertThat(result).isTrue();
    }

    @Test
    public void denyRequestWithError() {
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockTransferRequests.get(0)));

        boolean result = service.deny(1);

        assertThat(result).isTrue();
    }

    @Test(expected = NotFoundException.class)
    public void denyRequestThatDoesNotExist() {
        when(dao.findById(anyInt())).thenReturn(Optional.empty());

        service.deny(1);
    }

    private Church createMockChurch() {
        Church church = new Church();
        Group group = new Group();
        Team team = new Team();
        User user = new User();

        List<Group> groupList = new ArrayList<>();
        List<Team> teamList = new ArrayList<>();
        List<User> memberList = new ArrayList<>();

        groupList.add(group);
        teamList.add(team);
        memberList.add(user);

        church.setGroups(groupList);
        group.setTeams(teamList);
        team.setMembers(memberList);

        team.setId(1);

        user.setUsername("fake@fake.com");
        user.setAccess(new Access(1, "Team"));

        return church;
    }

}
