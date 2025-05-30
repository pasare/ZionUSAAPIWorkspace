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
public class TitleControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockCustomUser(access = "Member")
    public void getAllTitles() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/titles"))
//            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(7))))
//            .andExpect(status().isOk());

    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveTitleAsAdmin() throws Exception {
//        Title title = new Title(null, "Test Title", "T.");
//
//        mockMvc.perform(withHeaders(post("/titles")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
//                .andExpect(status().isCreated());
//
//        title = new Title(null, "Test Title1", "T1.");
//        mockMvc.perform(withHeaders(put("/titles/1")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void saveTitleAsChurch() throws Exception {
//        Title title = new Title(null, "Test Title", "T.");
//
//        mockMvc.perform(withHeaders(post("/titles")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/titles/1")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void saveTitleAsGroup() throws Exception {
//        Title title = new Title(null, "Test Title", "T.");
//
//        mockMvc.perform(withHeaders(post("/titles")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/titles/1", "T.")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void saveTitleAsTeam() throws Exception {
//        Title title = new Title(null, "Test Title", "T.");
//
//        mockMvc.perform(withHeaders(post("/titles")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/titles/1")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void saveTitleAsMember() throws Exception {
//        Title title = new Title(null, "Test Title", "T.");
//
//        mockMvc.perform(withHeaders(post("/titles")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/titles/1")
//                .content(gson.toJson(title))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void deleteTitleAsAdmin() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/titles/1"))
//            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(access = "Church")
    public void deleteTitleAsChurch() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/titles/1"))
//            .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void deleteTitleAsGroup() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/titles/1"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void deleteTitleAsTeam() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/titles/1"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void deleteTitleAsMember() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/titles/1"))
//            .andExpect(status().isForbidden());
//    }
}
