package org.zionusa.management.domain.roles;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.domain.role.Role;
import org.zionusa.management.domain.role.RoleDao;
import org.zionusa.management.domain.role.RoleService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RoleServiceTest {

    @InjectMocks
    private RoleService service;

    private List<Role> mockRoles;

    @Mock
    private RoleDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockRolesPath = "src/test/resources/roles.json";

        byte[] rolesData = Files.readAllBytes(Paths.get(mockRolesPath));
        mockRoles = mapper.readValue(rolesData, new TypeReference<List<Role>>() {
        });

        when(dao.findAll()).thenReturn(mockRoles);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockRoles.get(0)));
    }

    @Test
    public void getAllRoles() {
        List<Role> roles = service.getAll(null);

        assertThat(roles).isNotNull();
        assertThat(roles.size()).isEqualTo(7);
    }

    @Test
    public void getRoleById() throws javassist.NotFoundException {
        Role role = service.getById(1);

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(mockRoles.get(0).getId());
        assertThat(role.getName()).isEqualTo(mockRoles.get(0).getName());
        assertThat(role.getSortOrder()).isEqualTo(mockRoles.get(0).getSortOrder());
    }

    @Test
    public void saveRole() {

        when(dao.save(any(Role.class))).thenReturn(mockRoles.get(0));

        Role returnedRole = service.save(new Role(4, "Mock Role"));

        assertThat(returnedRole).isNotNull();
        assertThat(returnedRole.getId()).isEqualTo(mockRoles.get(0).getId());
        assertThat(returnedRole.getName()).isEqualTo(mockRoles.get(0).getName());
    }

}
