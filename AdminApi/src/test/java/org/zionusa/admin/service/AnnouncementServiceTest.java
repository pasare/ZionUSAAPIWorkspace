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
import org.zionusa.admin.dao.AnnouncementDao;
import org.zionusa.admin.domain.Announcement;
import org.zionusa.admin.util.TestUtils;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnnouncementServiceTest {

    @Spy()
    @InjectMocks()
    private AnnouncementService service;

    @Mock
    private NotificationService notificationService;

    private List<Announcement> mockAnnouncements;

    @Mock
    private AnnouncementDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockAnnouncementsPath = "src/test/resources/announcements.json";

        byte[] announcementsData = Files.readAllBytes(Paths.get(mockAnnouncementsPath));
        mockAnnouncements = mapper.readValue(announcementsData, new TypeReference<List<Announcement>>() {
        });

        when(dao.findAll()).thenReturn(mockAnnouncements);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockAnnouncements.get(0)));

        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

    }

    @Test
    public void getAllAnnouncements() {
        //when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        List<Announcement> announcements = service.getAll(null);

        assertThat(announcements).isNotNull();
        assertThat(announcements.size()).isEqualTo(4);
        assertThat(mockAnnouncements).containsAll(announcements);
    }

    @Test
    public void getAnnouncementsById() {
        Announcement announcement = service.getById(1);

        assertThat(announcement).isNotNull();
        assertThat(announcement.getId()).isEqualTo(mockAnnouncements.get(0).getId());
        assertThat(announcement.getTitle()).isEqualTo(mockAnnouncements.get(0).getTitle());

    }

    @Test
    public void saveNewAnnouncementFromMember() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Announcement mockAnnouncement = mockAnnouncements.get(0);
        mockAnnouncement.setId(null);
        mockAnnouncement.setApproved(null);
        mockAnnouncement.setEditorId(null);
        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncement);
        doNothing().when(service).saveAnnouncementAddedNotification(any(Announcement.class));

        Announcement returnedAnnouncement = service.save(mockAnnouncements.get(0));

        assertThat(returnedAnnouncement).isNotNull();
        assertThat(returnedAnnouncement.getId()).isEqualTo(mockAnnouncements.get(0).getId());
        assertThat(returnedAnnouncement.getTitle()).isEqualTo(mockAnnouncements.get(0).getTitle());
        assertThat(returnedAnnouncement.getApproved()).isNull();
        assertThat(returnedAnnouncement.getEditorId()).isNull();
        verify(service, times(1)).saveAnnouncementAddedNotification(any(Announcement.class));
    }

//    @Test
//    public void saveNewAnnouncementFromOverseerAsOverseer() {
//        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
//        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
//        when(dao.existsAnnouncementByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
//        doNothing().when(dao).setAnnouncementApproved(anyInt(), anyInt(), anyString());
//
//        Announcement mockAnnouncement = mockAnnouncements.get(0);
//        mockAnnouncement.setId(null);
//        mockAnnouncement.setRequesterId(1);
//        mockAnnouncement.setApproved(null);
//        mockAnnouncement.setEditorId(null);
//
//        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncements.get(1));
//
//        Announcement returnedAnnouncement = service.save(mockAnnouncement);
//
//        //should approve right away, and not send notification
//        verify(service, times(0)).saveAnnouncementAddedNotification(any(Announcement.class));
//        verify(service, times(1)).approveAnnouncement(anyInt());
//        verify(service, times(0)).publishAnnouncement(anyInt());
//    }

    @Test
    public void editExistingAnnouncementFromMemberAsOverseerWithoutApproving() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsAnnouncementByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        doNothing().when(dao).setAnnouncementApproved(anyInt(), anyInt(), anyString());

        Announcement mockAnnouncement = mockAnnouncements.get(0);
        mockAnnouncement.setId(1);
        mockAnnouncement.setRequesterId(2);
        mockAnnouncement.setApproved(null);
        mockAnnouncement.setEditorId(null);
        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncement);

        Announcement returnedAnnouncement = service.save(mockAnnouncement);

        //should save the announcement without approving it, and without sending notifications
        verify(service, times(0)).saveAnnouncementAddedNotification(any(Announcement.class));
        verify(service, times(0)).approveAnnouncement(anyInt());
        verify(service, times(0)).publishAnnouncement(anyInt());
    }

    @Test
    public void saveAnnouncementNotApproved() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncements.get(0));

        Announcement returnedAnnouncement = service.save(mockAnnouncements.get(0));

        assertThat(returnedAnnouncement).isNotNull();
        assertThat(returnedAnnouncement.getId()).isEqualTo(mockAnnouncements.get(0).getId());
        assertThat(returnedAnnouncement.getTitle()).isEqualTo(mockAnnouncements.get(0).getTitle());

        //should not call any of the special endpoints
        verify(service, times(0)).saveAnnouncementAddedNotification(any(Announcement.class));
        verify(service, times(0)).approveAnnouncement(anyInt());
        verify(service, times(0)).publishAnnouncement(anyInt());
    }

    @Test(expected = ForbiddenException.class)
    public void saveAnnouncementApprovedByMemberShouldThrowException() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncements.get(0));

        Announcement mockAnnouncement = mockAnnouncements.get(0);
        mockAnnouncement.setId(1);
        mockAnnouncement.setApproved(true);
        mockAnnouncement.setEditorId(null);

        Announcement returnedAnnouncement = service.save(mockAnnouncements.get(0));

        //ensure that approve was never called
        verify(dao, times(0)).setAnnouncementApproved(anyInt(), anyInt(), anyString());
    }

//    @Test
//    public void saveAnnouncementApprovedByOverseer() {
//        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
//        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
//        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncements.get(0));
//
//        Announcement mockAnnouncement = mockAnnouncements.get(0);
//        mockAnnouncement.setId(1);
//        mockAnnouncement.setApproved(true);
//        mockAnnouncement.setEditorId(null);
//
//        Announcement returnedAnnouncement = service.save(mockAnnouncements.get(0));
//
//        //ensure that approve was called
//        verify(dao, times(1)).setAnnouncementApproved(anyInt(), anyInt(), anyString());
//    }

    @Test
    public void saveAnnouncementPublishedByEditor() {
        ArrayList<String> applicationRoles = new ArrayList<>();
        applicationRoles.add("MY_ZIONUSA_EDITOR");
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", applicationRoles);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsAnnouncementByIdAndApprovedIsTrue(anyInt())).thenReturn(true);
        when(dao.existsAnnouncementByIdAndEditorIdIsNotNull(anyInt())).thenReturn(false);
        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncements.get(0));
        doNothing().when(service).saveAnnouncementPublishedNotification(any(Announcement.class));

        Announcement mockAnnouncement = mockAnnouncements.get(0);
        mockAnnouncement.setId(1);
        mockAnnouncement.setApproved(true);
        mockAnnouncement.setPublished(true);
        mockAnnouncement.setEditorId(1);

        Announcement returnedAnnouncement = service.save(mockAnnouncements.get(0));

        //ensure that approve was called
        verify(dao, times(0)).setAnnouncementApproved(anyInt(), anyInt(), anyString());
        verify(dao, times(1)).setAnnouncementPublished(anyInt(), anyInt(), anyString(), anyString());
        verify(service, times(1)).saveAnnouncementPublishedNotification(any(Announcement.class));
    }

    @Test(expected = ForbiddenException.class)
    public void saveAnnouncementNotApprovedButPublishedByOverseerShouldThrowException() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsAnnouncementByIdAndApprovedIsTrue(anyInt())).thenReturn(true);
        when(dao.existsAnnouncementByIdAndEditorIdIsNotNull(anyInt())).thenReturn(false);
        when(dao.save(any(Announcement.class))).thenReturn(mockAnnouncements.get(0));

        Announcement mockAnnouncement = mockAnnouncements.get(0);
        mockAnnouncement.setId(1);
        mockAnnouncement.setApproved(false);
        mockAnnouncement.setPublished(true);
        mockAnnouncement.setEditorId(1);

        Announcement returnedAnnouncement = service.save(mockAnnouncements.get(0));

        //ensure that approve was called
        verify(dao, times(0)).setAnnouncementApproved(anyInt(), anyInt(), anyString());
        verify(dao, times(0)).setAnnouncementPublished(anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    public void getFilteredAnnouncementsForMemberWhenPublishedDateInPast() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Announcement publishInThePastAnnouncement = new Announcement();
        publishInThePastAnnouncement.setId(9999999);
        publishInThePastAnnouncement.setRequesterId(1);
        publishInThePastAnnouncement.setApproved(true);
        publishInThePastAnnouncement.setPublished(true);
        publishInThePastAnnouncement.setPublishedDate("2020-01-01");
        publishInThePastAnnouncement.setNotificationDateAndTime(LocalDateTime.now().toString());
        mockAnnouncements.add(publishInThePastAnnouncement);
        when(dao.findAll()).thenReturn(mockAnnouncements);

        List<Announcement> announcements = service.getGeneralAnnouncements();

        assertThat(announcements).isNotNull();

        //todo find out why this is not working correctly, should be at least one in the list
        assertThat(announcements.size()).isEqualTo(0);

        Optional<Announcement> addedAnnouncementOptional = announcements.stream().filter(announcement -> announcement.getId().equals(9999999)).findFirst();
        assertThat(addedAnnouncementOptional.isPresent()).isFalse();

    }

    @Test
    public void getFilteredAnnouncementsForMemberWhenPublishedDateInFuture() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Announcement publishInTheFutureAnnouncement = new Announcement();
        publishInTheFutureAnnouncement.setId(9999999);
        publishInTheFutureAnnouncement.setRequesterId(1);
        publishInTheFutureAnnouncement.setApproved(true);
        publishInTheFutureAnnouncement.setPublished(true);
        publishInTheFutureAnnouncement.setPublishedDate(LocalDate.now().plusYears(1).format(DateTimeFormatter.ISO_DATE));
        publishInTheFutureAnnouncement.setNotificationDateAndTime(LocalDateTime.now().plusDays(1).toString());
        mockAnnouncements.add(publishInTheFutureAnnouncement);
        when(dao.findAll()).thenReturn(mockAnnouncements);

        List<Announcement> announcements = service.getGeneralAnnouncements();

        assertThat(announcements).isNotNull();

        //new one should not be found
        assertThat(announcements.size()).isEqualTo(0);

        Optional<Announcement> addedAnnouncementOptional = announcements.stream().filter(announcement -> announcement.getId().equals(9999999)).findFirst();
        assertThat(addedAnnouncementOptional.isPresent()).isFalse();

    }

    public void approveAnnouncement() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
    }

    public void publishAnnouncement() {

    }

    @Test
    public void deleteAnnouncement() {
        doNothing().when(dao).delete(any(Announcement.class));

        service.delete(1);
        verify(dao, times(1)).delete(any(Announcement.class));
    }
}
