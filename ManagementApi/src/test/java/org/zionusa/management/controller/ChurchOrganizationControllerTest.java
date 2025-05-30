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
import org.zionusa.management.domain.ChurchOrganization;
import org.zionusa.management.util.TestConfigForMail;
import org.zionusa.management.util.WithMockCustomUser;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
public class ChurchOrganizationControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @WithMockCustomUser(access = "Admin", churchId = "1")
//    public void getAllChurchOrganizationInfoAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/church-organizations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/church-organizations/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getAllChurchOrganizationInfoAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/church-organizations"))
//            .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/church-organizations/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/church-organizations/2"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1", groupId = "1")
//    public void getAllChurchOrganizationInfoAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/church-organizations"))
//            .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/church-organizations/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/church-organizations/2"))
//            .andExpect(status().isOk());
//    }

    @Test
    @WithMockCustomUser(access = "Team", churchId = "1", teamId = "1")
    public void getAllChurchOrganizationInfoAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/church-organizations"))
//            .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/church-organizations/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/church-organizations/2"))
//            .andExpect(status().isOk());
    }

//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1", id = "1")
//    public void getAllChurchOrganizationInfoAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/church-organizations"))
//            .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/church-organizations/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/church-organizations/2"))
//            .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveChurchOrganizationAsAdmin() throws Exception {
//
//        ChurchOrganization churchOrganization = getMockChurchOrganization();
//
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void saveChurchOrganizationAsChurch() throws Exception {
//
//        ChurchOrganization churchOrganization = getMockChurchOrganization();
//
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//
//        //branch church
//        churchOrganization.setChurchId(2);
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(2)));
//
//        //different church
//        churchOrganization.setChurchId(4);
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void saveChurchOrganizationAsGroup() throws Exception {
//
//        ChurchOrganization churchOrganization = getMockChurchOrganization();
//
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", churchId = "1")
//    public void saveChurchOrganizationAsTeam() throws Exception {
//
//        ChurchOrganization churchOrganization = getMockChurchOrganization();
//
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void saveChurchOrganizationAsMember() throws Exception {
//
//        ChurchOrganization churchOrganization = getMockChurchOrganization();
//
//        mockMvc.perform(withHeaders(post("/church-organizations")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        churchOrganization.setId(1);
//        mockMvc.perform(withHeaders(put("/church-organizations/1")
//                .content(gson.toJson(churchOrganization))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void deleteChurchOrganizationAsAdmin() throws Exception {
//        mockMvc.perform(deleteWithHeaders("/church-organizations/1"))
//            .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/2"))
//            .andExpect(status().isNoContent());
    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void deleteChurchOrganizationAsChurch() throws Exception {
//
//        //own church
//        mockMvc.perform(deleteWithHeaders("/church-organizations/1"))
//                .andExpect(status().isNoContent());
//
//        //branch church
//        mockMvc.perform(deleteWithHeaders("/church-organizations/2"))
//                .andExpect(status().isNoContent());
//
//        //Other church
//        mockMvc.perform(deleteWithHeaders("/church-organizations/4"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void deleteChurchOrganizationAsGroup() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/2"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", churchId = "1")
//    public void deleteChurchOrganizationAsTeam() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/2"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void deleteChurchOrganizationAsMember() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/church-organizations/2"))
//                .andExpect(status().isForbidden());
//
//    }

    private ChurchOrganization getMockChurchOrganization() {
        ChurchOrganization churchOrganization = new ChurchOrganization();
        churchOrganization.setChurchId(1);
        churchOrganization.setOrganizationData("fake Data");

        return churchOrganization;
    }

}
