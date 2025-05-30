package org.zionusa.biblestudy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.biblestudy.dao.StudyTypeDao;
import org.zionusa.biblestudy.domain.StudyType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StudyTypeServiceTest {

    @InjectMocks
    private StudyTypeService service;

    private List<StudyType> mockStudyTypes;

    @Mock
    private StudyTypeDao dao;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockStudyTypesPath = "src/test/resources/study-types.json";

        byte[] accessesData = Files.readAllBytes(Paths.get(mockStudyTypesPath));
        mockStudyTypes = mapper.readValue(accessesData, new TypeReference<List<StudyType>>() {
        });

        when(dao.findAll()).thenReturn(mockStudyTypes);
        when(dao.findById(ArgumentMatchers.any(Integer.class))).thenReturn(Optional.of(mockStudyTypes.get(0)));
    }

    @Test
    public void getAllStudyTypes() {
        List<StudyType> studyTypes = service.getAll(null);

        assertThat(studyTypes).isNotNull();
        assertThat(studyTypes.size()).isEqualTo(3);
        assertThat(studyTypes).containsAll(studyTypes);
    }
}
