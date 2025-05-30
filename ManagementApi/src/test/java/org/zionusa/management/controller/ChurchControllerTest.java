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
public class ChurchControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getAllChurchesAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void getAllChurchesAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/4"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void getAllChurchesAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void getAllChurchesAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getAllChurchesAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void getAllDisplayChurchesAsUnauthorized() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/display"))
//                .andExpect(status().isOk());
//    }

    @Test
    @WithMockCustomUser(access = "Member")
    public void getAllDisplayChurchesAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/display"))
//            .andExpect(status().isOk());
    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getAllChurchInformationAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//
//        mockMvc.perform(getWithHeaders("/churches/1/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void getAllChurchInformationAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//
//        mockMvc.perform(getWithHeaders("/churches/1/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void getAllChurchInformationAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void getAllChurchInformationAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//
//        mockMvc.perform(getWithHeaders("/churches/1/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllChurchInformationAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//
//        mockMvc.perform(getWithHeaders("/churches/1/information"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getBranchesAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/1/branches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/branches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void getBranchesAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/1/branches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/churches/2/branches"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/churches/4/branches"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void getBranchesAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/1/branches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/branches"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void getBranchesAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/1/branches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/branches"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getBranchesAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/churches/1/branches"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/branches"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getGroupsAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/church-group"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/church-group"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getGroupsAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/church-group"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/churches/2/groups"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/churches/2/church-group"))
//                .andExpect(status().isOk());
//
//        //other church
//        mockMvc.perform(getWithHeaders("/churches/4/groups"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/4/church-group"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void getGroupsAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/church-group"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2/groups"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/2/church-group"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", churchId = "1")
//    public void getGroupsAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/church-group"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2/groups"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/2/church-group"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void getGroupsAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/church-group"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/churches/2/groups"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/2/church-group"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin", churchId = "1")
//    public void cleanGroupsAsAdmin() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/1/groups/clean"))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2/groups/clean"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void cleanGroupsAsChurch() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/1/groups/clean"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(deleteWithHeaders("/churches/2/groups/clean"))
//                .andExpect(status().isNoContent());
//
//        //different church
//        mockMvc.perform(deleteWithHeaders("/churches/4/groups/clean"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void cleanGroupsAsGroup() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/1/groups/clean"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2/groups/clean"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", churchId = "1")
//    public void cleanGroupsAsTeam() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/1/groups/clean"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2/groups/clean"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void cleanGroupsAsMember() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/1/groups/clean"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2/groups/clean"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin", churchId = "1")
//    public void getMembersAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/1/members/M"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members/F"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getMembersAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/churches/2/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        //different church
//        mockMvc.perform(getWithHeaders("/churches/4/members"))
//                .andExpect(status().isForbidden());
//
//
//        mockMvc.perform(getWithHeaders("/churches/1/members/M"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/churches/2/members/F"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        //different church
//        mockMvc.perform(getWithHeaders("/churches/4/members/M"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void getMembersAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1/members/M"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members/F"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @WithMockCustomUser(access = "Team", churchId = "1")
//    public void getMembersAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1/members/M"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members/F"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void getMembersAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/churches/1/members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/churches/1/members/M"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/churches/2/members/F"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin", churchId = "1")
//    public void saveChurchAsAdmin() throws Exception {
//        Church church = new Church();
//        church.setAssociationId(1);
//        church.setTypeId(1);
//        church.setName("Mock Church");
//        church.setStateId(1);
//
//        mockMvc.perform(withHeaders(post("/churches")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        church.setId(1);
//        mockMvc.perform(withHeaders(put("/churches/1")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void saveChurchAsChurch() throws Exception {
//        Church church = new Church();
//        church.setAssociationId(1);
//        church.setId(1);
//        church.setTypeId(1);
//        church.setName("Mock Church");
//        church.setStateId(1);
//
//        //modify branch
//        church.setId(2);
//        mockMvc.perform(withHeaders(put("/churches/2")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(2)));
//
//        //modify different church
//        church.setId(4);
//        mockMvc.perform(withHeaders(put("/churches/4")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //create branch
//        church.setId(null);
//        church.setParentChurchId(1);
//        mockMvc.perform(withHeaders(post("/churches")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //create new church not a branch
//        church.setId(null);
//        church.setParentChurchId(null);
//        mockMvc.perform(withHeaders(post("/churches")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //modify own church
//        church.setId(1);
//        mockMvc.perform(withHeaders(put("/churches/1")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void saveChurchAsGroup() throws Exception {
//        Church church = new Church();
//        church.setId(1);
//        church.setTypeId(1);
//        church.setName("Mock Church");
//        church.setStateId(1);
//
//        mockMvc.perform(withHeaders(put("/churches/1")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/churches/2")
//                .content(gson.toJson(church))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void archiveChurchAsAdmin() throws Exception {
//
//        mockMvc.perform(putWithHeaders("/churches/1/archive"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(putWithHeaders("/churches/2/archive"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void archiveChurchAsChurch() throws Exception {
//
//        //archive own church
//        mockMvc.perform(putWithHeaders("/churches/1/archive"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/churches/2/archive"))
//                .andExpect(status().isOk());
//
//        //different church
//        mockMvc.perform(putWithHeaders("/churches/4/archive"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void archiveChurchAsGroup() throws Exception {
//
//        mockMvc.perform(putWithHeaders("/churches/1/archive"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/churches/2/archive"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void archiveChurchAsTeam() throws Exception {
//
//        mockMvc.perform(putWithHeaders("/churches/1/archive"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/churches/2/archive"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void archiveChurchAsMember() throws Exception {
//
//        mockMvc.perform(putWithHeaders("/churches/1/archive"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/churches/2/archive"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin", churchId = "1")
//    public void deleteChurchAsAdmin() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/1"))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void deleteChurchAsChurch() throws Exception {
//
//        //delete other church
//        mockMvc.perform(deleteWithHeaders("/churches/4"))
//                .andExpect(status().isForbidden());
//
//        //delete branch church
//        mockMvc.perform(deleteWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//
//        //delete own church
//        mockMvc.perform(deleteWithHeaders("/churches/1"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1")
//    public void deleteChurchAsGroup() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/1"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", churchId = "1")
//    public void deleteChurchAsTeam() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/1"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void deleteChurchAsMember() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/2"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/churches/1"))
//                .andExpect(status().isForbidden());
//    }
}
