package org.zionusa.biblestudy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.biblestudy.dao.BibleStudyDao;
import org.zionusa.biblestudy.dao.StudentDao;
import org.zionusa.biblestudy.dao.StudyDao;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.domain.Student;
import org.zionusa.biblestudy.domain.Study;
import org.zionusa.biblestudy.utils.TestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BibleStudyServiceTest {


    @InjectMocks
    @Spy
    private BibleStudyService service;

    private List<BibleStudy> mockBibleStudies;

    @Mock
    private BibleStudyDao dao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private StudyDao studyDao;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockBibleStudiesPath = "src/test/resources/bible-studies.json";

        byte[] bibleStudiesData = Files.readAllBytes(Paths.get(mockBibleStudiesPath));
        mockBibleStudies = mapper.readValue(bibleStudiesData, new TypeReference<List<BibleStudy>>() {
        });

        when(dao.findAll()).thenReturn(mockBibleStudies);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockBibleStudies.get(0)));

        //needed for methods that check user information in the method
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

        when(studentDao.getOne(anyInt())).thenReturn(new Student());
        when(studyDao.getOne(anyInt())).thenReturn(new Study());
    }

    @Test
    public void getAllBibleStudies() {
        List<BibleStudy> bibleStudies = service.getAll(null);

        assertThat(bibleStudies).isNotNull();
        assertThat(bibleStudies.size()).isEqualTo(3);
        assertThat(bibleStudies).containsAll(mockBibleStudies);
    }

    @Test
    public void saveBibleStudyWithDateInThePastShouldBeAutoApprovedWithNoNotification() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);


        BibleStudy bibleStudy = new BibleStudy();
        bibleStudy.setDate(LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE));
        bibleStudy.setStudentId(1);
        bibleStudy.setStudyId(1);
        bibleStudy.setStudent(new Student());
        bibleStudy.setStudy(new Study());
        when(dao.save(any(BibleStudy.class))).thenReturn(bibleStudy);

        BibleStudy returnedBibleStudy = service.save(bibleStudy);

        assertThat(returnedBibleStudy.isApproved()).isTrue();
        assertThat(returnedBibleStudy.isAttended()).isTrue();

        verify(service, times(1)).updateStudentProgress(any(BibleStudy.class));
        verify(service, never()).saveBibleStudyAddedNotification(any(BibleStudy.class));

    }

    @Test
    public void saveBibleStudyWithSameDateAndTimeInThePastShouldBeAutoApprovedWithNoNotification() {
//        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
//        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
//
//
//        BibleStudy bibleStudy = new BibleStudy();
//        bibleStudy.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
//        bibleStudy.setTime(LocalTime.now().minusHours(1L).format(DateTimeFormatter.ofPattern("h:mm a")));
//        bibleStudy.setStudentId(1);
//        bibleStudy.setStudyId(1);
//        bibleStudy.setStudent(new Student());
//        bibleStudy.setStudy(new Study());
//        when(dao.save(any(BibleStudy.class))).thenReturn(bibleStudy);
//
//        BibleStudy returnedBibleStudy = service.save(bibleStudy);
//
//        assertThat(returnedBibleStudy.isApproved()).isTrue();
//        assertThat(returnedBibleStudy.isAttended()).isTrue();
//
//        verify(service, times(1)).updateStudentProgress(any(BibleStudy.class));
//        verify(service, never()).saveBibleStudyAddedNotification(any(BibleStudy.class));
    }

    @Test
    public void saveBibleStudyWithDateInTheFutureShouldRequireApprovalWithNotification() {

        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        doNothing().when(service).saveBibleStudyAddedNotification(any(BibleStudy.class));


        BibleStudy bibleStudy = new BibleStudy();
        bibleStudy.setDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));
        bibleStudy.setStudentId(1);
        bibleStudy.setStudyId(1);
        bibleStudy.setStudent(new Student());
        bibleStudy.setStudy(new Study());
        when(dao.save(any(BibleStudy.class))).thenReturn(bibleStudy);

        BibleStudy returnedBibleStudy = service.save(bibleStudy);

        assertThat(returnedBibleStudy.isApproved()).isFalse();
        assertThat(returnedBibleStudy.isAttended()).isFalse();

        verify(service, never()).updateStudentProgress(any(BibleStudy.class));
        verify(service, times(1)).saveBibleStudyAddedNotification(any(BibleStudy.class));

    }

    @Test
    public void saveBibleStudyWithSameDateAndTimeInTheFutureShouldRequireApproval() {
        AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
        doNothing().when(service).saveBibleStudyAddedNotification(any(BibleStudy.class));


        BibleStudy bibleStudy = new BibleStudy();
        bibleStudy.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        bibleStudy.setTime(LocalTime.now().plusMinutes(30L).format(DateTimeFormatter.ofPattern("h:mm a")));
        bibleStudy.setStudentId(1);
        bibleStudy.setStudyId(1);
        bibleStudy.setStudent(new Student());
        bibleStudy.setStudy(new Study());
        when(dao.save(any(BibleStudy.class))).thenReturn(bibleStudy);

        BibleStudy returnedBibleStudy = service.save(bibleStudy);

        assertThat(returnedBibleStudy.isApproved()).isFalse();
        assertThat(returnedBibleStudy.isAttended()).isFalse();

        verify(service, never()).updateStudentProgress(any(BibleStudy.class));
        verify(service, never()).saveCreatorBibleStudyApprovedNotification(any(BibleStudy.class));
        verify(service, times(1)).saveBibleStudyAddedNotification(any(BibleStudy.class));
    }

    @Test
    public void approveBibleStudyShouldCreateTeacherAndCreatorNotifications() {

        doNothing().when(service).saveTeacherAssignedNotification(any(BibleStudy.class));
        doNothing().when(service).saveCreatorBibleStudyApprovedNotification(any(BibleStudy.class));

        service.approve(anyInt());

        verify(service, never()).updateStudentProgress(any(BibleStudy.class));
        verify(service, never()).saveBibleStudyAddedNotification(any(BibleStudy.class));
        verify(service, times(1)).saveTeacherAssignedNotification(any(BibleStudy.class));
        verify(service, times(1)).saveCreatorBibleStudyApprovedNotification(any(BibleStudy.class));
    }

    @Test
    public void attendBibleStudyShouldUpdateStudentProgressAndSendNoNotifications() {
        //doNothing().when(service).updateStudentProgress(any(BibleStudy.class));

        service.attend(1);

        verify(service, times(1)).updateStudentProgress(any(BibleStudy.class));
        verify(service, never()).saveBibleStudyAddedNotification(any(BibleStudy.class));
        verify(service, never()).saveTeacherAssignedNotification(any(BibleStudy.class));
        verify(service, never()).saveCreatorBibleStudyApprovedNotification(any(BibleStudy.class));
    }

    @Test
    public void teacherAvailableShouldSendPushNotification() {
        doNothing().when(service).saveTeacherAvailabilityNotification(any(BibleStudy.class), anyString(), anyString(), anyString());

        when(dao.save(any(BibleStudy.class))).thenReturn(mockBibleStudies.get(0));

        BibleStudy returnedBibleStudy = service.teacherAvailable(anyInt());

        assertThat(returnedBibleStudy.getTeacherAvailable()).isEqualTo(true);

        verify(service, times(1)).saveTeacherAvailabilityNotification(any(BibleStudy.class), anyString(), anyString(), anyString());
        verify(service, never()).saveBibleStudyAddedNotification(any(BibleStudy.class));
        verify(service, never()).saveTeacherAssignedNotification(any(BibleStudy.class));
        verify(service, never()).saveCreatorBibleStudyApprovedNotification(any(BibleStudy.class));

    }

    @Test
    public void teacherUnAvailableShouldSendPushNotification() {
        doNothing().when(service).saveTeacherAvailabilityNotification(any(BibleStudy.class), anyString(), anyString(), anyString());

        when(dao.save(any(BibleStudy.class))).thenReturn(mockBibleStudies.get(0));

        BibleStudy returnedBibleStudy = service.teacherUnavailable(anyInt());

        assertThat(returnedBibleStudy.getTeacherAvailable()).isEqualTo(false);

        verify(service, times(1)).saveTeacherAvailabilityNotification(any(BibleStudy.class), anyString(), anyString(), anyString());
        verify(service, never()).saveBibleStudyAddedNotification(any(BibleStudy.class));
        verify(service, never()).saveTeacherAssignedNotification(any(BibleStudy.class));
        verify(service, never()).saveCreatorBibleStudyApprovedNotification(any(BibleStudy.class));

    }

    @Test
    public void updateStudentProgress() {
        Student student = new Student();
        student.setId(1);

        when(studentDao.findById(anyInt())).thenReturn(Optional.of(student));
        when(dao.countDistinctByStudentIdAndAttended(anyInt(), anyBoolean())).thenReturn(2);

        Student updatedStudent = service.updateStudentProgress(mockBibleStudies.get(0));

        assertThat(updatedStudent.getStudiesCompleted()).isEqualTo(2);

        // 2/50 should be 4%
        assertThat(updatedStudent.getStudiesPercentage()).isEqualTo(4);

        when(dao.countDistinctByStudentIdAndAttended(anyInt(), anyBoolean())).thenReturn(6);
        Student updatedStudent2 = service.updateStudentProgress(mockBibleStudies.get(0));
        // 6/50 should be 4%
        assertThat(updatedStudent.getStudiesPercentage()).isEqualTo(12);
    }


}
