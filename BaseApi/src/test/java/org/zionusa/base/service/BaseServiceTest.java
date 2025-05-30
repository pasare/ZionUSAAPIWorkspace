package org.zionusa.base.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BaseServiceTest {

    @InjectMocks
    private BaseService<TestItem, Integer> service;

    @Mock
    private BaseDao<TestItem, Integer> dao;

    @Mock
    private Logger logger;

    private List<TestItem> mockBaseObjects;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockBaseObjectPath = "src/test/resources/baseService.json";

        byte[] baseData = Files.readAllBytes(Paths.get(mockBaseObjectPath));
        mockBaseObjects = mapper.readValue(baseData, new TypeReference<List<TestItem>>() {
        });
        when(dao.findAll()).thenReturn(mockBaseObjects);
    }

    @Test
    public void getAllSucceeds() {
        // Act
        List<TestItem> baseObjects = service.getAll(null);
        // Assert
        assertThat(baseObjects).isNotNull();
        assertThat(baseObjects.size()).isEqualTo(5);
    }

    @Test
    public void getByIdSucceeds() {
        // Arrange
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockBaseObjects.get(0)));
        // Act
        TestItem item = service.getById(1);
        // Assert
        assertThat(item.getEmail()).isEqualTo("Test1@gmail.com");
    }

    // TODO: Fix archive unit tests
//    @Test
//    public void getAllDisplayTest() {
//
//        List<String> mockColumns = new ArrayList<>();
//        mockColumns.add("id");
//        mockColumns.add("name");
//
//        List<Map<String, Object>> baseObjects = service.getAllDisplay(mockColumns, null);
//
//        assertThat(baseObjects).isNotNull();
//        assertThat(baseObjects.size()).isEqualTo(5);
//    }

    @Test(expected = NotFoundException.class)
    public void getByIdNotFound() throws NotFoundException {
        // Arrange
        when(dao.findById(any(Integer.class))).thenReturn(Optional.empty());
        // Act
        service.getById(6);
    }

    @Test
    public void saveSucceeds() {
        // Arrange
        when(dao.save(any(TestItem.class))).thenReturn(mockBaseObjects.get(0));
        // Act
        TestItem returnedBaseObject = service.save(mockBaseObjects.get(0));
        // Assert
        assertThat(returnedBaseObject).isNotNull();
        assertThat(returnedBaseObject.getId()).isEqualTo(mockBaseObjects.get(0).getId());
        assertThat(returnedBaseObject.getEmail()).isEqualTo(mockBaseObjects.get(0).getEmail());
    }

//    @Test
//    public void deleteSucceeds() throws NotFoundException {
//        // Arrange
//        when(dao.save(any(TestItem.class))).thenReturn(mockBaseObjects.get(0));
//        doNothing().when(dao).delete(any(TestItem.class));
//        // Act
//        service.delete(1);
//        // Assert
//        verify(dao, times(1)).delete(any(TestItem.class));
//    }

    @Test
    public void deleteItemThatDoesNotExistThrowsNoError() throws NotFoundException {
        // Arrange
        when(dao.findById(any(Integer.class))).thenReturn(Optional.empty());
        // Act
        service.delete(1);
        // Assert
        verify(dao, times(0)).delete(any(TestItem.class));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestItem implements BaseDomain<Integer> {
        private final boolean archived = false;
        private final boolean hidden = false;
        private Integer id;
        private String email;

        public String getEmail() {
            return email;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer value) {
            id = value;
        }
    }

}
