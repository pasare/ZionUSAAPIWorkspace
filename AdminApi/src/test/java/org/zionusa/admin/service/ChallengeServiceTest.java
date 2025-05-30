package org.zionusa.admin.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zionusa.admin.dao.ChallengeDao;
import org.zionusa.admin.dao.ChallengeLogDao;
import org.zionusa.admin.dao.GoalLogDao;
import org.zionusa.admin.domain.Challenge;
import org.zionusa.admin.util.TestUtils;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChallengeServiceTest {

    @Spy()
    @InjectMocks()
    private ChallengeService service;

    @Mock
    private NotificationService notificationService;

    private List<Challenge> mockChallenges;

    @Mock
    private ChallengeDao dao;

    @Mock
    private ChallengeLogDao challengeLogDao;

    @Mock
    private GoalLogDao goalLogDao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockChallengesPath = "src/test/resources/challenges.json";

        byte[] challengesData = Files.readAllBytes(Paths.get(mockChallengesPath));
        mockChallenges = mapper.readValue(challengesData, new TypeReference<List<Challenge>>() {
        });

        when(dao.findAll()).thenReturn(mockChallenges);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockChallenges.get(0)));

        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

    }

    @Test
    public void getAllChallenges() {
        //when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        List<Challenge> announcements = service.getAll(null);

        assertThat(announcements).isNotNull();
        assertThat(announcements.size()).isEqualTo(4);
        assertThat(mockChallenges).containsAll(announcements);
    }

    @Test
    public void getChallengesById() {
        Challenge challenge = service.getById(1);

        assertThat(challenge).isNotNull();
        assertThat(challenge.getId()).isEqualTo(mockChallenges.get(0).getId());
        assertThat(challenge.getName()).isEqualTo(mockChallenges.get(0).getName());

    }

    @Test
    public void saveNewChallengeFromMember() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(null);
        mockChallenge.setApproved(null);
        mockChallenge.setEditorId(null);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenge);
        doNothing().when(service).saveChallengeAddedNotification(any(Challenge.class));

        Challenge returnedChallenge = service.save(mockChallenges.get(0));

        assertThat(returnedChallenge).isNotNull();
        assertThat(returnedChallenge.getId()).isEqualTo(mockChallenges.get(0).getId());
        assertThat(returnedChallenge.getName()).isEqualTo(mockChallenges.get(0).getName());
        assertThat(returnedChallenge.getApproved()).isNull();
        assertThat(returnedChallenge.getEditorId()).isNull();

        verify(service, times(0)).saveChallengeAddedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengeApprovedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengePublishedNotification(any(Challenge.class));
    }

    @Test
    public void saveNewChallengeFromOverseerAsOverseer() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsChallengeByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        doNothing().when(dao).setChallengeApproved(anyInt(), anyInt(), anyString());

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(null);
        mockChallenge.setRequesterId(1);
        mockChallenge.setApproved(null);
        mockChallenge.setEditorId(null);

        when(dao.save(any(Challenge.class))).thenReturn(mockChallenges.get(1));

        Challenge returnedChallenge = service.save(mockChallenge);

        //should approve right away, and not send notification
        verify(service, times(0)).saveChallengeAddedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengeApprovedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengePublishedNotification(any(Challenge.class));
        verify(service, times(1)).approveChallenge(anyInt());
        verify(service, times(0)).publishChallenge(anyInt());
    }

    @Test
    public void editExistingChallengeFromMemberAsOverseerWithoutApproving() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsChallengeByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        doNothing().when(dao).setChallengeApproved(anyInt(), anyInt(), anyString());

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(1);
        mockChallenge.setRequesterId(2);
        mockChallenge.setApproved(null);
        mockChallenge.setEditorId(null);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenge);

        Challenge returnedChallenge = service.save(mockChallenge);

        //should save the announcement without approving it, and without sending notifications
        verify(service, times(0)).saveChallengeAddedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengeApprovedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengePublishedNotification(any(Challenge.class));
        verify(service, times(1)).approveChallenge(anyInt());
        verify(service, times(1)).publishChallenge(anyInt());
    }

    @Test
    public void saveChallengeNotApproved() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenges.get(0));

        Challenge returnedChallenge = service.save(mockChallenges.get(0));

        assertThat(returnedChallenge).isNotNull();
        assertThat(returnedChallenge.getId()).isEqualTo(mockChallenges.get(0).getId());
        assertThat(returnedChallenge.getName()).isEqualTo(mockChallenges.get(0).getName());

        //should not call any of the special endpoints
        verify(service, times(0)).saveChallengeAddedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengeApprovedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengePublishedNotification(any(Challenge.class));
        verify(service, times(1)).approveChallenge(anyInt());
        verify(service, times(1)).publishChallenge(anyInt());
    }

    /*@Test(expected = ForbiddenException.class)
    public void saveChallengeApprovedByMemberShouldThrowException() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenges.get(0));

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(1);
        mockChallenge.setApproved(true);
        mockChallenge.setEditorId(null);

        Challenge returnedChallenge = service.save(mockChallenges.get(0));

        //ensure that approve was never called
        verify(dao, times(0)).setChallengeApproved(anyInt(), anyInt(), anyString());
    } */

    @Test
    public void saveChallengeApprovedByOverseer() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenges.get(0));

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(1);
        mockChallenge.setApproved(true);
        mockChallenge.setPublished(false);
        mockChallenge.setEditorId(null);

        Challenge returnedChallenge = service.save(mockChallenges.get(0));

        //ensure that approve was called
        verify(dao, times(1)).setChallengeApproved(anyInt(), anyInt(), anyString());
        verify(service, times(0)).saveChallengeAddedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengeApprovedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengePublishedNotification(any(Challenge.class));
    }

    @Test
    public void saveChallengePublishedByEditor() {
        ArrayList<String> applicationRoles = new ArrayList<>();
        applicationRoles.add("MY_ZIONUSA_EDITOR");
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", applicationRoles);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsChallengeByIdAndApprovedIsTrue(anyInt())).thenReturn(true);
        when(dao.existsChallengeByIdAndEditorIdIsNotNull(anyInt())).thenReturn(false);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenges.get(0));
        doNothing().when(service).saveChallengePublishedNotification(any(Challenge.class));

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(1);
        mockChallenge.setApproved(true);
        mockChallenge.setPublished(true);
        mockChallenge.setEditorId(1);

        Challenge returnedChallenge = service.save(mockChallenges.get(0));

        //ensure that approve was called
        verify(dao, times(0)).setChallengeApproved(anyInt(), anyInt(), anyString());
        verify(dao, times(1)).setChallengePublished(anyInt(), anyInt(), anyString(), anyString());

        verify(service, times(0)).saveChallengeAddedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengeApprovedNotification(any(Challenge.class));
        verify(service, times(0)).saveChallengePublishedNotification(any(Challenge.class));
    }

    @Test(expected = ForbiddenException.class)
    public void saveChallengeNotApprovedButPublishedByOverseerShouldThrowException() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsChallengeByIdAndApprovedIsTrue(anyInt())).thenReturn(true);
        when(dao.existsChallengeByIdAndEditorIdIsNotNull(anyInt())).thenReturn(false);
        when(dao.save(any(Challenge.class))).thenReturn(mockChallenges.get(0));

        Challenge mockChallenge = mockChallenges.get(0);
        mockChallenge.setId(1);
        mockChallenge.setApproved(false);
        mockChallenge.setPublished(true);
        mockChallenge.setEditorId(1);

        Challenge returnedChallenge = service.save(mockChallenges.get(0));

        //ensure that approve was called
        verify(dao, times(0)).setChallengeApproved(anyInt(), anyInt(), anyString());
        verify(dao, times(0)).setChallengePublished(anyInt(), anyInt(), anyString(), anyString());
    }

    public void approveChallenge() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
    }

    public void publishChallenge() {

    }

    @Test
    public void deleteChallenge() {
        doNothing().when(dao).delete(any(Challenge.class));
        when(challengeLogDao.findAllByChallengeId(anyInt())).thenReturn(new ArrayList<>());
        when(goalLogDao.findAllByChallengeId(anyInt())).thenReturn(new ArrayList<>());

        service.delete(1);
        verify(dao, times(1)).delete(any(Challenge.class));
        verify(challengeLogDao, times(1)).findAllByChallengeId(anyInt());
        verify(goalLogDao, times(1)).findAllByChallengeId(anyInt());
    }
}
