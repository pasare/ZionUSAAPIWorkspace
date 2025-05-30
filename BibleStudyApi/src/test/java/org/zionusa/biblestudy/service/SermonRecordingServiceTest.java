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
import org.zionusa.biblestudy.dao.SermonRecordingDao;
import org.zionusa.biblestudy.dao.StudentDao;
import org.zionusa.biblestudy.dao.StudyDao;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.domain.SermonRecording;
import org.zionusa.biblestudy.domain.Student;
import org.zionusa.biblestudy.domain.Study;
import org.zionusa.biblestudy.utils.TestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class SermonRecordingServiceTest {

	@InjectMocks
	@Spy
	private SermonRecordingService service;

	private List<SermonRecording> mockSermonRecordings;

	@Mock
	private SermonRecordingDao dao;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);

		ObjectMapper mapper = new ObjectMapper();

		String mockSermonRecordingsPath = "src/test/resources/sermon-recordings.json";

		byte[] sermonRecordingsData = Files.readAllBytes(Paths.get(mockSermonRecordingsPath));
		mockSermonRecordings = mapper.readValue(sermonRecordingsData, new TypeReference<List<SermonRecording>>() {
		});

		when(dao.findAll()).thenReturn(mockSermonRecordings);
		when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockSermonRecordings.get(0)));

		//needed for methods that check user information in the method
		AuthenticatedUser applicationUser = TestUtils.mockAuthenticatedUser(1, 1, 1, 1, "Member", "Member", new ArrayList<>());
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

	}

	@Test
	public void saveExistingSermonRecordingShouldNotSendANotification() {
		SermonRecording sermonRecording = new SermonRecording();
		sermonRecording.setId(1);
		sermonRecording.setPreacherName("testerson");
		sermonRecording.setPreacherId(1);

		service.save(sermonRecording);

		verify(service, never()).saveSermonUploadedNotification(any(SermonRecording.class));
		verify(service, never()).saveExampleSermonNotification(any(SermonRecording.class));
		verify(service, never()).saveSermonGradedNotification(any(SermonRecording.class));
	}

	@Test
	public void saveNewSermonRecordingShouldSendAnUploadedNotification() {
		doNothing().when(service).saveSermonUploadedNotification(any(SermonRecording.class));

		SermonRecording sermonRecording = new SermonRecording();
		sermonRecording.setPreacherName("testerson");
		sermonRecording.setPreacherId(1);

		service.save(sermonRecording);

		verify(service, times(1)).saveSermonUploadedNotification(any(SermonRecording.class));
		verify(service, never()).saveExampleSermonNotification(any(SermonRecording.class));
		verify(service, never()).saveSermonGradedNotification(any(SermonRecording.class));

	}

	@Test
	public void gradeExampleSermonShouldSendExampleSermonNotificationAndGradedNotification() {
		doNothing().when(service).saveExampleSermonNotification(any(SermonRecording.class));
		doNothing().when(service).saveSermonGradedNotification(any(SermonRecording.class));

		SermonRecording sermonRecording = new SermonRecording();
		sermonRecording.setId(1);
		sermonRecording.setGrade(3);
		sermonRecording.setPreacherName("testerson");
		sermonRecording.setPreacherId(1);
		sermonRecording.setChurchId(1);
		sermonRecording.setGraderId(1);
		sermonRecording.setTeamId(1);
		sermonRecording.setExampleSermon(true);
		when(dao.save(any(SermonRecording.class))).thenReturn(sermonRecording);

		service.gradeSermon(sermonRecording);

		verify(service, never()).saveSermonUploadedNotification(any(SermonRecording.class));
		verify(service, times(1)).saveExampleSermonNotification(any(SermonRecording.class));
		verify(service, times(1)).saveSermonGradedNotification(any(SermonRecording.class));
	}

	@Test
	public void gradeSermonRecordingShouldSendGradedNotification() {
		doNothing().when(service).saveExampleSermonNotification(any(SermonRecording.class));
		doNothing().when(service).saveSermonGradedNotification(any(SermonRecording.class));

		SermonRecording sermonRecording = new SermonRecording();
		sermonRecording.setId(1);
		sermonRecording.setGrade(3);
		sermonRecording.setPreacherName("testerson");
		sermonRecording.setPreacherId(1);
		sermonRecording.setChurchId(1);
		sermonRecording.setGraderId(1);
		sermonRecording.setTeamId(1);
		sermonRecording.setExampleSermon(false);
		when(dao.save(any(SermonRecording.class))).thenReturn(sermonRecording);

		service.gradeSermon(sermonRecording);

		verify(service, never()).saveSermonUploadedNotification(any(SermonRecording.class));
		verify(service, never()).saveExampleSermonNotification(any(SermonRecording.class));
		verify(service, times(1)).saveSermonGradedNotification(any(SermonRecording.class));
	}

}
