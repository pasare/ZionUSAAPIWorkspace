package org.zionusa.management.domain.access;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccessServiceTest {

    @InjectMocks
    private AccessService service;

    private List<Access> mockAccesses;

    @Mock
    private AccessDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockAccessesPath = "src/test/resources/accesses.json";

        byte[] accessesData = Files.readAllBytes(Paths.get(mockAccessesPath));
        mockAccesses = mapper.readValue(accessesData, new TypeReference<List<Access>>() {
        });

        when(dao.findAll()).thenReturn(mockAccesses);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockAccesses.get(0)));
    }

//    @Test
//    public void getAllAccesses() {
//        List<Access> accesses = service.getAll(null);
//
//        assertThat(accesses).isNotNull();
//        assertThat(accesses.size()).isEqualTo(6);
//        assertThat(mockAccesses).containsAll(accesses);
//    }

//    @Test
//    public void getAccessById() throws javassist.NotFoundException {
////        Access access = service.getById(1);
////
////        assertThat(access).isNotNull();
////        assertThat(access.getId()).isEqualTo(mockAccesses.get(0).getId());
////        assertThat(access.getName()).isEqualTo(mockAccesses.get(0).getName());
//
//    }

//    @Test
//    public void saveAccess() {
//
//        when(dao.save(any(Access.class))).thenReturn(mockAccesses.get(0));
//
//        Access returnedAccess = service.save(mockAccesses.get(0));
//
//        assertThat(returnedAccess).isNotNull();
//        assertThat(returnedAccess.getId()).isEqualTo(mockAccesses.get(0).getId());
//        assertThat(returnedAccess.getName()).isEqualTo(mockAccesses.get(0).getName());
//    }

//    @Test
//    public void deleteAccess() {
////        doNothing().when(dao).delete(any(Access.class));
////
////        service.delete(1);
////        verify(dao, times(1)).delete(any(Access.class));
//    }

//    @Test(expected = NotFoundException.class)
//    public void deleteAccessThatDoesNotExist() {
////        when(dao.findById(any(Integer.class))).thenReturn(Optional.empty());
////
////        service.delete(1);
////        verify(dao, times(0)).delete(any(Access.class));
//    }

}
