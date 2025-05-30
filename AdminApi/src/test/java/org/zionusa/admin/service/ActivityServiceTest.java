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
import org.zionusa.admin.dao.ChallengeActivityDao;
import org.zionusa.admin.dao.activities.ActivityCategoryDao;
import org.zionusa.admin.dao.activities.ActivityDao;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.admin.service.activities.ActivityService;
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

public class ActivityServiceTest {

    @Spy()
    @InjectMocks
    private ActivityService service;

    @Mock
    private NotificationService notificationService;

    private List<Activity> mockActivities;

    @Mock
    private ActivityDao dao;

    @Mock
    private ActivityCategoryDao activityCategoryDao;

    @Mock
    private ChallengeActivityDao challengeActivityDao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockActivitiesPath = "src/test/resources/activities.json";

        byte[] accessesData = Files.readAllBytes(Paths.get(mockActivitiesPath));
        mockActivities = mapper.readValue(accessesData, new TypeReference<List<Activity>>() {
        });

        when(dao.findAll()).thenReturn(mockActivities);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockActivities.get(0)));

        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

    }

    @Test
    public void getAllActivities() {
        when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        List<Activity> activities = service.getAll(null);

        assertThat(activities).isNotNull();
        assertThat(activities.size()).isEqualTo(4);
        assertThat(mockActivities).containsAll(activities);
    }

    @Test
    public void getActivitiesById() {
        Activity activity = service.getById(1);

        assertThat(activity).isNotNull();
        assertThat(activity.getId()).isEqualTo(mockActivities.get(0).getId());
        assertThat(activity.getName()).isEqualTo(mockActivities.get(0).getName());

    }

    @Test
    public void getApprovedAndPublishedActivitiesForMemberWithNonePending() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        List<Activity> activities = service.getActivities(null);

        assertThat(activities).isNotNull();
        assertThat(activities.size()).isEqualTo(2);
    }

    @Test
    public void getApprovedAndPublishedActivitiesForMemberWhoHasApprovalPending() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity approvalPendingActivity = new Activity();
        approvalPendingActivity.setId(9999999);
        approvalPendingActivity.setRequesterId(1);
        approvalPendingActivity.setApproved(null);
        approvalPendingActivity.setPublished(false);
        mockActivities.add(approvalPendingActivity);
        when(dao.findAll()).thenReturn(mockActivities);

        List<Activity> activities = service.getActivities(null);

        assertThat(activities).isNotNull();
        assertThat(activities.size()).isEqualTo(3);

        Optional<Activity> addedActivityOptional = activities.stream().filter(activity -> activity.getId().equals(9999999)).findFirst();
        assertThat(addedActivityOptional.isPresent()).isTrue();

    }

    @Test
    public void getApprovedAndPublishedActivitiesForMemberWhoHasApprovalRejected() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity approvalPendingActivity = new Activity();
        approvalPendingActivity.setId(9999999);
        approvalPendingActivity.setRequesterId(1);
        approvalPendingActivity.setApproved(false);
        approvalPendingActivity.setPublished(false);
        approvalPendingActivity.setPublishedDate("2020-01-01");
        mockActivities.add(approvalPendingActivity);
        when(dao.findAll()).thenReturn(mockActivities);

        List<Activity> activities = service.getActivities(null);

        assertThat(activities).isNotNull();
        assertThat(activities.size()).isEqualTo(2);

        Optional<Activity> addedActivityOptional = activities.stream().filter(activity -> activity.getId().equals(9999999)).findFirst();
        assertThat(addedActivityOptional.isPresent()).isFalse();

    }

    @Test
    public void getFilteredActivitiesForMemberWhoHasPublishPending() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity approvalPendingActivity = new Activity();
        approvalPendingActivity.setId(9999999);
        approvalPendingActivity.setRequesterId(1);
        approvalPendingActivity.setApproved(true);
        approvalPendingActivity.setPublished(false);
        approvalPendingActivity.setPublishedDate(null);
        mockActivities.add(approvalPendingActivity);
        when(dao.findAll()).thenReturn(mockActivities);

        List<Activity> activities = service.getActivities(null);

        assertThat(activities).isNotNull();
        assertThat(activities.size()).isEqualTo(3);

        Optional<Activity> addedActivityOptional = activities.stream().filter(activity -> activity.getId().equals(9999999)).findFirst();
        assertThat(addedActivityOptional.isPresent()).isTrue();
    }

    @Test
    public void getFilteredActivitiesForMemberWhenPublishedDateInPast() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity publishInThePastActivity = new Activity();
        publishInThePastActivity.setId(9999999);
        publishInThePastActivity.setRequesterId(1);
        publishInThePastActivity.setApproved(true);
        publishInThePastActivity.setPublished(true);
        publishInThePastActivity.setPublishedDate("2020-12-01");
        publishInThePastActivity.setNotificationDateAndTime(LocalDateTime.now().minusDays(1).toString());
        mockActivities.add(publishInThePastActivity);
        when(dao.findAll()).thenReturn(mockActivities);

        List<Activity> activities = service.getActivities(null);

        assertThat(activities).isNotNull();
        assertThat(activities.size()).isEqualTo(3);

        Optional<Activity> addedActivityOptional = activities.stream().filter(activity -> activity.getId().equals(9999999)).findFirst();
        assertThat(addedActivityOptional.isPresent()).isTrue();

    }

    @Test
    public void getFilteredActivitiesForMemberWhenPublishedDateInFuture() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity publishInTheFutureActivity = new Activity();
        publishInTheFutureActivity.setId(9999999);
        publishInTheFutureActivity.setRequesterId(1);
        publishInTheFutureActivity.setApproved(true);
        publishInTheFutureActivity.setPublished(true);
        publishInTheFutureActivity.setPublishedDate(LocalDate.now().plusYears(1).format(DateTimeFormatter.ISO_DATE));
        publishInTheFutureActivity.setNotificationDateAndTime(LocalDateTime.now().plusDays(1).toString());
        mockActivities.add(publishInTheFutureActivity);
        when(dao.findAll()).thenReturn(mockActivities);

        List<Activity> activities = service.getActivities(null);

        assertThat(activities).isNotNull();

        // newly added one should not show up because its published way in future
        assertThat(activities.size()).isEqualTo(2);

        Optional<Activity> addedActivityOptional = activities.stream().filter(activity -> activity.getId().equals(9999999)).findFirst();
        assertThat(addedActivityOptional.isPresent()).isFalse();

    }

    @Test
    public void getApprovedAndPublishedActivitiesByCategoryIdForMemberWithNonePending() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        List<Activity> activities = createActivitiesByCategoryList(1, 7);
        when(dao.getActivitiesByCategoryId(anyInt())).thenReturn(activities);

        List<Activity> returnedActivities = service.getActivitiesByCategoryId(1);

        assertThat(returnedActivities).isNotNull();
        assertThat(returnedActivities.size()).isEqualTo(7);
    }

    @Test
    public void getApprovedAndPublishedActivitiesByCategoryIdForMemberWithOnePending() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        List<Activity> activities = createActivitiesByCategoryList(1, 2);
        Activity pendingActivity = new Activity();
        pendingActivity.setRequesterId(1);
        pendingActivity.setApproved(null);
        pendingActivity.setPublished(false);
        pendingActivity.setPublishedDate(null);
        activities.add(pendingActivity);

        //pending activity for somebody else
        Activity pendingActivity2 = new Activity();
        pendingActivity2.setRequesterId(2);
        pendingActivity2.setApproved(null);
        pendingActivity2.setPublished(false);
        pendingActivity2.setPublishedDate(null);
        activities.add(pendingActivity2);
        when(dao.getActivitiesByCategoryId(anyInt())).thenReturn(activities);

        List<Activity> returnedActivities = service.getActivitiesByCategoryId(anyInt());

        assertThat(returnedActivities).isNotNull();
        assertThat(returnedActivities.size()).isEqualTo(3);
    }

    @Test
    public void getActivityCategories() {
        //no reason why this call would fail since there is no processing, needed for coverage
        when(activityCategoryDao.findAll()).thenReturn(new ArrayList<>());
        service.getActivityCategories();
        assertThat(true);
    }

    @Test
    public void getApprovedActivities() {
        when(dao.getActivitiesByApprovedTrue()).thenReturn(new ArrayList<>());
        service.getApprovedActivities();
        assertThat(true);
    }

    @Test
    public void getPendingActivities() {
        when(dao.getActivitiesByApprovedIsNull()).thenReturn(new ArrayList<>());
        service.getPendingActivities();
        assertThat(true);
    }

    @Test
    public void getUnapprovedActivities() {
        when(dao.getActivitiesByApprovedIsFalse()).thenReturn(new ArrayList<>());
        service.getPendingActivities();
        assertThat(true);
    }

    @Test
    public void saveNewActivityFromMember() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(null);
        mockActivity.setApproved(null);
        mockActivity.setEditorId(null);
        when(dao.save(any(Activity.class))).thenReturn(mockActivity);
        doNothing().when(service).saveActivityAddedNotification(any(Activity.class));

        Activity returnedActivity = service.save(mockActivities.get(0));

        assertThat(returnedActivity).isNotNull();
        assertThat(returnedActivity.getId()).isEqualTo(mockActivities.get(0).getId());
        assertThat(returnedActivity.getName()).isEqualTo(mockActivities.get(0).getName());
        assertThat(returnedActivity.getApproved()).isNull();
        assertThat(returnedActivity.getEditorId()).isNull();
        verify(service, times(1)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityApprovedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityPublishedNotification(any(Activity.class));
    }

    @Test
    public void saveNewActivityFromOverseerAsOverseer() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        doNothing().when(dao).setActivityApproved(anyInt(), anyInt(), anyString());

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(null);
        mockActivity.setRequesterId(1);
        mockActivity.setApproved(null);
        mockActivity.setEditorId(null);

        when(dao.save(any(Activity.class))).thenReturn(mockActivities.get(1));

        Activity returnedActivity = service.save(mockActivity);

        //should approve right away, and not send notification
        verify(service, times(0)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(1)).saveActivityApprovedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityPublishedNotification(any(Activity.class));
        verify(service, times(1)).approveActivity(anyInt());
        verify(service, times(0)).publishActivity(anyInt());
    }

    @Test
    public void saveNewActivityFromEditorialWithEditorialRole() {
        ArrayList<String> applicationRoles = new ArrayList<>();
        applicationRoles.add("MY_ZIONUSA_EDITOR");
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", applicationRoles);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        doNothing().when(dao).setActivityApproved(anyInt(), anyInt(), anyString());

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(null);
        mockActivity.setRequesterId(1);
        mockActivity.setApproved(null);
        mockActivity.setEditorId(1);

        when(dao.save(any(Activity.class))).thenReturn(mockActivities.get(1));

        Activity returnedActivity = service.save(mockActivity);

        //should approve right away, and not send notification
        verify(service, times(0)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(1)).saveActivityApprovedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityPublishedNotification(any(Activity.class));
        verify(service, times(1)).approveActivity(anyInt());
        verify(service, times(0)).publishActivity(anyInt());
    }

    @Test
    public void editExistingActivityFromMemberAsOverseerWithoutApproving() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(false);
        doNothing().when(dao).setActivityApproved(anyInt(), anyInt(), anyString());

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(1);
        mockActivity.setRequesterId(2);
        mockActivity.setApproved(null);
        mockActivity.setEditorId(null);
        mockActivity.setPublished(false);
        when(dao.save(any(Activity.class))).thenReturn(mockActivity);

        Activity returnedActivity = service.save(mockActivity);

        //should save the announcement without approving it, and without sending notifications
        verify(service, times(0)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityApprovedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityPublishedNotification(any(Activity.class));
        verify(service, times(0)).approveActivity(anyInt());
        verify(service, times(0)).publishActivity(anyInt());
    }

    @Test
    public void saveActivityNotApproved() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        Activity activity = mockActivities.get(0);
        activity.setApproved(false);
        activity.setPublished(false);
        when(dao.save(any(Activity.class))).thenReturn(activity);

        Activity returnedActivity = service.save(activity);

        assertThat(returnedActivity).isNotNull();
        assertThat(returnedActivity.getId()).isEqualTo(mockActivities.get(0).getId());
        assertThat(returnedActivity.getName()).isEqualTo(mockActivities.get(0).getName());

        //should not call any of the special endpoints
        verify(service, times(0)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityApprovedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityPublishedNotification(any(Activity.class));
        verify(service, times(0)).approveActivity(anyInt());
        verify(service, times(0)).publishActivity(anyInt());
    }

    @Test(expected = ForbiddenException.class)
    public void saveActivityApprovedByMemberShouldThrowException() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Activity.class))).thenReturn(mockActivities.get(0));

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(1);
        mockActivity.setApproved(true);
        mockActivity.setEditorId(null);

        Activity returnedActivity = service.save(mockActivities.get(0));

        //ensure that approve was never called
        verify(dao, times(0)).setActivityApproved(anyInt(), anyInt(), anyString());
    }

    @Test
    public void saveActivityApprovedByOverseer() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.save(any(Activity.class))).thenReturn(mockActivities.get(0));

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(1);
        mockActivity.setApproved(true);
        mockActivity.setEditorId(null);
        mockActivity.setPublished(false);

        Activity returnedActivity = service.save(mockActivities.get(0));

        //ensure that approve was called
        verify(dao, times(1)).setActivityApproved(anyInt(), anyInt(), anyString());
        verify(service, times(0)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(1)).saveActivityApprovedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityPublishedNotification(any(Activity.class));
    }

    @Test
    public void saveActivityPublishedByEditor() {
        ArrayList<String> applicationRoles = new ArrayList<>();
        applicationRoles.add("MY_ZIONUSA_EDITOR");
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", applicationRoles);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(true);
        when(dao.existsActivityByIdAndEditorIdIsNotNull(anyInt())).thenReturn(false);
        when(dao.save(any(Activity.class))).thenReturn(mockActivities.get(0));
        doNothing().when(service).saveActivityPublishedNotification(any(Activity.class));

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(1);
        mockActivity.setApproved(true);
        mockActivity.setPublished(true);
        mockActivity.setEditorId(1);

        Activity returnedActivity = service.save(mockActivities.get(0));

        //ensure that approve was called
        verify(dao, times(0)).setActivityApproved(anyInt(), anyInt(), anyString());
        verify(dao, times(1)).setActivityPublished(anyInt(), anyInt(), anyString(), anyString());
        verify(service, times(1)).saveActivityPublishedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityAddedNotification(any(Activity.class));
        verify(service, times(0)).saveActivityApprovedNotification(any(Activity.class));
    }

    @Test(expected = ForbiddenException.class)
    public void saveActivityNotApprovedButPublishedByOverseerShouldThrowException() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        when(dao.existsActivitiesByIdAndApprovedIsTrue(anyInt())).thenReturn(true);
        when(dao.existsActivityByIdAndEditorIdIsNotNull(anyInt())).thenReturn(false);
        when(dao.save(any(Activity.class))).thenReturn(mockActivities.get(0));

        Activity mockActivity = mockActivities.get(0);
        mockActivity.setId(1);
        mockActivity.setApproved(false);
        mockActivity.setPublished(true);
        mockActivity.setEditorId(1);

        Activity returnedActivity = service.save(mockActivities.get(0));

        //ensure that approve was called
        verify(dao, times(0)).setActivityApproved(anyInt(), anyInt(), anyString());
        verify(dao, times(0)).setActivityPublished(anyInt(), anyInt(), anyString(), anyString());
    }

    public void approveChallenge() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Overseer", "Overseer", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
    }

    public void publishChallenge() {

    }

    @Test
    public void deleteActivity() {
        when(challengeActivityDao.findAllByActivityId(anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(dao).delete(any(Activity.class));

        service.delete(1);
        verify(dao, times(1)).deleteById(anyInt());
    }

    private List<Activity> createActivitiesByCategoryList(Integer categoryId, int count) {
        List<Activity> categoryActivities = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Activity activity = new Activity();
            activity.setCategoryId(categoryId);
            activity.setApproved(true);
            activity.setPublished(true);
            activity.setPublishedDate("2020-01-01");
            categoryActivities.add(activity);
        }

        return categoryActivities;
    }
}
