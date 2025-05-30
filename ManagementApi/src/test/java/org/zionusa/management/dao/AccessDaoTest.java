package org.zionusa.management.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.zionusa.management.domain.access.Access;
import org.zionusa.management.domain.access.AccessDao;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class AccessDaoTest {

    @Spy
    @Autowired
    private AccessDao dao;

    private List<Access> mockAccesses;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        ObjectMapper mapper = new ObjectMapper();

        String mockAccessesPath = "src/test/resources/accesses.json";

        byte[] accessesData = Files.readAllBytes(Paths.get(mockAccessesPath));

        mockAccesses = mapper.readValue(accessesData, new TypeReference<List<Access>>() {
        });

        doReturn(mockAccesses).when(dao).findAll();
        doReturn(mockAccesses.get(0)).when(dao).findById(any(Integer.class));
    }

    //nothing to test
}
