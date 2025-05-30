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
public class ChurchTypeControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockCustomUser(access = "Member")
    public void getAllChurchTypes() throws Exception {

//        mockMvc.perform(getWithHeaders("/church-types"))
//            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
//            .andExpect(status().isOk());

    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveChurchTypeAsAdmin() throws Exception {
//        ChurchType churchType = new ChurchType(1, "Test Church Type");
//
//        mockMvc.perform(withHeaders(post("/church-types")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
//            .andExpect(status().isCreated());
//
//        mockMvc.perform(withHeaders(put("/church-types/1")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
//            .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void saveChurchTypeAsChurch() throws Exception {
//        ChurchType churchType = new ChurchType(null, "Test Church Type");
//
//        mockMvc.perform(withHeaders(post("/church-types")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/church-types/1")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void saveChurchTypeAsGroup() throws Exception {
//        ChurchType churchType = new ChurchType(null, "Test Church Type");
//
//        mockMvc.perform(withHeaders(post("/church-types")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/church-types/1")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void saveChurchTypeAsTeam() throws Exception {
//        ChurchType churchType = new ChurchType(null, "Test Church Type");
//
//        mockMvc.perform(withHeaders(post("/church-types")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/church-types/1")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void saveChurchTypeAsMember() throws Exception {
//        ChurchType churchType = new ChurchType(null, "Test Church Type");
//
//        mockMvc.perform(withHeaders(post("/church-types")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/church-types/1")
//                .content(gson.toJson(churchType))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void deleteChurchTypeAsAdmin() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/church-types/1"))
//                .andExpect(status().isNoContent());
//    }

    @Test
    @WithMockCustomUser(access = "Church")
    public void deleteChurchTypeAsChurch() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/church-types/1"))
//            .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void deleteChurchTypeAsGroup() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/church-types/1"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void deleteChurchTypeAsTeam() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/church-types/1"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void deleteChurchTypeAsMember() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/church-types/1"))
//            .andExpect(status().isForbidden());
//    }
}
