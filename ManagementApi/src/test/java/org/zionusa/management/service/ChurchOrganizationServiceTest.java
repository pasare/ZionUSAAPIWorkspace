package org.zionusa.management.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.dao.ChurchOrganizationDao;
import org.zionusa.management.domain.ChurchOrganization;
import org.zionusa.management.exception.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ChurchOrganizationServiceTest {

    @InjectMocks
    private ChurchOrganizationService service;

    private List<ChurchOrganization> mockChurchOrganizationInfo;

    @Mock
    private ChurchOrganizationDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockOrganizationPath = "src/test/resources/church-organization.json";

        byte[] accessesData = Files.readAllBytes(Paths.get(mockOrganizationPath));
        mockChurchOrganizationInfo = mapper.readValue(accessesData, new TypeReference<List<ChurchOrganization>>() {
        });

        when(dao.findAll()).thenReturn(mockChurchOrganizationInfo);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockChurchOrganizationInfo.get(0)));
    }

    @Test
    public void getAll() {
        List<ChurchOrganization> churchOrganizationInfo = service.getAll(null);

        assertThat(churchOrganizationInfo).isNotNull();
        assertThat(churchOrganizationInfo).hasSize(3);
        assertThat(mockChurchOrganizationInfo).containsAll(churchOrganizationInfo);
    }

    @Test
    public void getChurchOrganizationByChurchId() {
        ChurchOrganization mockChurchOrganization = mockChurchOrganizationInfo.get(0);
        when(dao.getChurchOrganizationByChurchId(anyInt())).thenReturn(Optional.of(mockChurchOrganization));

        ChurchOrganization churchOrganization = service.getChurchOrganizationByChurchId(1);

        assertThat(churchOrganization.getId()).isEqualTo(mockChurchOrganization.getId());
        assertThat(churchOrganization.getChurchId()).isEqualTo(mockChurchOrganization.getChurchId());
        assertThat(churchOrganization.getOrganizationData()).isEqualTo(mockChurchOrganization.getOrganizationData());

    }

    @Test(expected = NotFoundException.class)
    public void getChurchOrganizationThatDoesNotExistByChurchId() {
        when(dao.getChurchOrganizationByChurchId(anyInt())).thenReturn(Optional.empty());

        service.getChurchOrganizationByChurchId(1);
    }

    @Test
    public void save() {
        ChurchOrganization churchOrganization = mockChurchOrganizationInfo.get(0);

        when(dao.save(any(ChurchOrganization.class))).thenReturn(churchOrganization);

        ChurchOrganization result = service.save(churchOrganization);

        assertThat(churchOrganization.getId()).isEqualTo(result.getId());
        assertThat(churchOrganization.getChurchId()).isEqualTo(result.getChurchId());
        assertThat(churchOrganization.getOrganizationData()).isEqualTo(result.getOrganizationData());
    }

}
