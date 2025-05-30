package org.zionusa.management.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.dao.ChurchTypeDao;
import org.zionusa.management.domain.ChurchType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ChurchTypeServiceTest {

    @InjectMocks
    private ChurchTypeService service;

    @Mock
    private ChurchTypeDao dao;

    private List<ChurchType> mockChurchTypes;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockChurchTypesPath = "src/test/resources/church-types.json";

        byte[] churchesData = Files.readAllBytes(Paths.get(mockChurchTypesPath));
        mockChurchTypes = mapper.readValue(churchesData, new TypeReference<List<ChurchType>>() {
        });

        when(dao.findAll()).thenReturn(mockChurchTypes);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockChurchTypes.get(0)));
    }

    @Test
    public void getAll() {
        List<ChurchType> churchTypes = service.getAll(null);

        assertThat(churchTypes).isNotEmpty();
        assertThat(churchTypes.get(0).getId()).isEqualTo(mockChurchTypes.get(0).getId());
        assertThat(churchTypes.get(0).getName()).isEqualTo(mockChurchTypes.get(0).getName());
        assertThat(churchTypes.get(0).getSortOrder()).isEqualTo(mockChurchTypes.get(0).getSortOrder());
    }

    @Test
    public void getChurchTypeById() throws javassist.NotFoundException {
        ChurchType access = service.getById(1);

        assertThat(access).isNotNull();
        assertThat(access.getId()).isEqualTo(mockChurchTypes.get(0).getId());
        assertThat(access.getName()).isEqualTo(mockChurchTypes.get(0).getName());
        assertThat(access.getSortOrder()).isEqualTo(mockChurchTypes.get(0).getSortOrder());

    }

    @Test
    public void saveChurchType() {
        when(dao.save(any(ChurchType.class))).thenReturn(mockChurchTypes.get(0));

        ChurchType churchType = service.save(mockChurchTypes.get(0));

        assertThat(churchType).isNotNull();
        assertThat(churchType.getId()).isEqualTo(mockChurchTypes.get(0).getId());

        verify(dao, times(1)).save(any(ChurchType.class));
    }

}
