package org.zionusa.management.controller;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.management.util.TestConfigForMail;
import org.zionusa.management.util.WithMockCustomUser;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
public class RoleControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockCustomUser(access = "Member")
    public void getAllRoles() throws Exception {

//        mockMvc.perform(getWithHeaders("/roles"))
//            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))))
//            .andExpect(status().isOk());

    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveRoleAsAdmin() throws Exception {
//        Role role = new Role(null, "Test Role");
//
//        mockMvc.perform(withHeaders(post("/roles")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
//                .andExpect(status().isCreated());
//
//        role = new Role(null, "Test Role1");
//        mockMvc.perform(withHeaders(put("/roles/1")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void saveRoleAsChurch() throws Exception {
//        Role role = new Role(null, "Test Role");
//
//        mockMvc.perform(withHeaders(post("/roles")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/roles/1")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void saveRoleAsGroup() throws Exception {
//        Role role = new Role(null, "Test Role");
//
//        mockMvc.perform(withHeaders(post("/roles")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/roles/1")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void saveRoleAsTeam() throws Exception {
//        Role role = new Role(null, "Test Role");
//
//        mockMvc.perform(withHeaders(post("/roles")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/roles/1")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void saveRoleAsMember() throws Exception {
//        Role role = new Role(null, "Test Role");
//
//        mockMvc.perform(withHeaders(post("/roles")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/roles/1")
//                .content(gson.toJson(role))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void deleteRoleAsAdmin() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/roles/1"))
//            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(access = "Church")
    public void deleteRoleAsChurch() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/roles/1"))
//            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Group")
    public void deleteRoleAsGroup() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/roles/1"))
//            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Team")
    public void deleteRoleAsTeam() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/roles/1"))
//            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Member")
    public void deleteRoleAsMember() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/roles/1"))
//            .andExpect(status().isForbidden());
    }
}
