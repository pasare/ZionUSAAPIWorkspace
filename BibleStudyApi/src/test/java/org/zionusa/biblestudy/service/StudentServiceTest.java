package org.zionusa.biblestudy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.biblestudy.dao.StudentDao;
import org.zionusa.biblestudy.dao.StudentStepDao;
import org.zionusa.biblestudy.domain.Student;
import org.zionusa.biblestudy.domain.StudentStep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StudentServiceTest {

    @InjectMocks
    private StudentService service;

    private List<Student> mockStudents;

    private List<StudentStep> mockStudentSteps;

    @Mock
    private StudentDao dao;

    @Mock
    private StudentStepDao studentStepDao;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockStudentsPath = "src/test/resources/students.json";
        String mockStudentStepsPath = "src/test/resources/student-steps.json";

        byte[] studentsData = Files.readAllBytes(Paths.get(mockStudentsPath));
        byte[] studentStepsData = Files.readAllBytes(Paths.get(mockStudentStepsPath));
        mockStudents = mapper.readValue(studentsData, new TypeReference<List<Student>>() {
        });
        mockStudentSteps = mapper.readValue(studentStepsData, new TypeReference<List<StudentStep>>() {
        });

        when(dao.findAll()).thenReturn(mockStudents);
        when(studentStepDao.findAll()).thenReturn(mockStudentSteps);
        when(dao.findById(ArgumentMatchers.any(Integer.class))).thenReturn(Optional.of(mockStudents.get(0)));
        when(studentStepDao.findById(ArgumentMatchers.any(Integer.class))).thenReturn(Optional.of(mockStudentSteps.get(0)));
    }

    @Test
    public void getAllStudents() {
        List<Student> students = service.getAll(null);

        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(5);
        assertThat(students).containsAll(mockStudents);
    }

//    @Test
//    public void getAllStudentStepByStudentId() {
//        // Arrange
//        when(studentStepDao.findById(anyInt())).thenReturn(mockStudentSteps.get(0));
//        // Act
//        StudentStep studentStep = service.getStudentStepByStudentId(1);
//        // Assert
//        assertThat(studentStep).isNotNull();
////        assertThat(studentSteps.size()).isEqualTo(1);
////        assertThat(studentSteps).containsAll(expected);
//    }
//
//    @Test
//    public void getAllStudentStepsByUserId() {
//        // Arrange
//        when(studentStepDao.getAllByUserId1OrUserId2OrUserId3(anyInt(),anyInt(),anyInt())).thenReturn(mockStudentSteps.get(0));
//        // Act
//        List<StudentStep> studentSteps = service.getStudentStepByUserId(1);
//        // Assert
//        List<StudentStep> expected = new ArrayList<>(mockStudentSteps);
//        assertThat(studentSteps).isNotNull();
//        assertThat(studentSteps.size()).isEqualTo(1);
//        assertThat(studentSteps).containsAll(expected);
//    }
}
