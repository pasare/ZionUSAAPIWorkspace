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
public class TeamControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

////    @Test
////    @WithMockCustomUser(access = "Admin")
////    public void getAllTeamsAsAdmin() throws Exception {
////        mockMvc.perform(getWithHeaders("/teams"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(12))));
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void getAllTeamsAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/teams"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void getAllTeamsAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/teams"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void getAllTeamsAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/teams"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllTeamsAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/teams"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getTeamByIdAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/teams/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/2"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/3"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getTeamByIdAsChurch() throws Exception {
//
//        //team in church
//        mockMvc.perform(getWithHeaders("/teams/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/teams/5"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/8"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        //a different church
//        mockMvc.perform(getWithHeaders("/teams/10"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getTeamByIdAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/teams/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/2"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/5"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/teams/8"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getTeamByIdAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/teams/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/2"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/teams/5"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/teams/8"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", teamId = "1")
//    public void getTeamByIdAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/teams/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/teams/2"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/teams/5"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/teams/8"))
//            .andExpect(status().isForbidden());
//    }
//
////    @Test
////    @WithMockCustomUser(access = "Admin")
////    public void saveTeamAsAdmin() throws Exception {
////        Team team = new Team();
////        team.setGroupId(4);
////        team.setName("Mock Team");
////        team.setChurchTeam(false);
////        team.setArchived(false);
////
////        mockMvc.perform(withHeaders(post("/teams")
////                .content(gson.toJson(team))
////                .contentType(contentType)))
////                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.id", notNullValue()));
////
////        mockMvc.perform(withHeaders(put("/teams/4")
////                .content(gson.toJson(team))
////                .contentType(contentType)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id", notNullValue()));
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void saveTeamAsChurch() throws Exception {
//        Team team = new Team();
//        team.setName("Mock Group");
//        team.setChurchTeam(false);
//        team.setArchived(false);
//
//        //save to branch church
//        team.setGroupId(4);
//        mockMvc.perform(withHeaders(post("/teams")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/teams/4")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        //save to different church
//        team.setGroupId(5);
//        mockMvc.perform(withHeaders(post("/teams")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/teams/12")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        //save to same church
//        team.setGroupId(1);
//        mockMvc.perform(withHeaders(post("/teams")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/teams/1")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
////    @Test
////    @WithMockCustomUser(access = "Group", groupId = "1")
////    public void saveTeamAsGroup() throws Exception {
////
////        Team team = new Team();
////        team.setName("Mock Team");
////        team.setChurchTeam(false);
////        team.setArchived(false);
////
////        //save to different group
////        team.setGroupId(4);
////        mockMvc.perform(withHeaders(post("/teams")
////                .content(gson.toJson(team))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////
////        mockMvc.perform(withHeaders(put("/teams/12")
////                .content(gson.toJson(team))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////
////        //save to same group
////        team.setGroupId(1);
////        mockMvc.perform(withHeaders(post("/teams")
////                .content(gson.toJson(team))
////                .contentType(contentType)))
////                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.id", notNullValue()));
////
////        mockMvc.perform(withHeaders(put("/teams/1")
////                .content(gson.toJson(team))
////                .contentType(contentType)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id", notNullValue()));
////        ;
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void saveTeamAsTeam() throws Exception {
//
//        Team team = new Team();
//        team.setName("Mock Team");
//        team.setChurchTeam(false);
//        team.setArchived(false);
//
//        //save new team
//        team.setGroupId(4);
//        mockMvc.perform(withHeaders(post("/teams")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        //modify different team
//        team.setId(12);
//        mockMvc.perform(withHeaders(put("/teams/12")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        //modify same team
//        team.setId(1);
//        mockMvc.perform(withHeaders(put("/teams/1")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", teamId = "1")
//    public void saveTeamAsMember() throws Exception {
//
//        Team team = new Team();
//        team.setName("Mock Team");
//        team.setChurchTeam(false);
//        team.setArchived(false);
//
//        team.setGroupId(4);
//        mockMvc.perform(withHeaders(post("/teams")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/teams/4")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        team.setGroupId(1);
//        mockMvc.perform(withHeaders(post("/teams")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/teams/1")
//                .content(gson.toJson(team))
//                .contentType(contentType)))
//            .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void archiveTeamAsAdmin() throws Exception {
//
//        mockMvc.perform(putWithHeaders("/teams/12/archive"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(putWithHeaders("/teams/6/archive"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(putWithHeaders("/teams/1/archive"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void archiveTeamAsChurch() throws Exception {
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/teams/7/archive"))
//            .andExpect(status().isOk());
//
//        //different church
//        mockMvc.perform(putWithHeaders("/teams/10/archive"))
//            .andExpect(status().isForbidden());
//
//        //same church
//        mockMvc.perform(putWithHeaders("/teams/1/archive"))
//            .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void archiveTeamAsGroup() throws Exception {
//
//        //different group
//        mockMvc.perform(putWithHeaders("/teams/4/archive"))
//                .andExpect(status().isForbidden());
//
//        //same group
//        mockMvc.perform(putWithHeaders("/teams/1/archive"))
//                .andExpect(status().isOk());
//
//    }

    @Test
    @WithMockCustomUser(access = "Team", teamId = "1")
    public void archiveTeamAsTeam() throws Exception {
//
//        //different team
//        mockMvc.perform(putWithHeaders("/teams/4/archive"))
//            .andExpect(status().isForbidden());
//
//        //same team
//        mockMvc.perform(putWithHeaders("/teams/1/archive"))
//            .andExpect(status().isForbidden());

    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", teamId = "1")
//    public void archiveTeamAsMember() throws Exception {
//
//        //different team
//        mockMvc.perform(putWithHeaders("/teams/4/archive"))
//            .andExpect(status().isForbidden());
//
//        //same team
//        mockMvc.perform(putWithHeaders("/teams/1/archive"))
//            .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void deleteTeamAsAdmin() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/teams/12"))
//            .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/teams/6"))
//            .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/teams/1"))
//            .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void deleteTeamAsChurch() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/teams/12"))
//            .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/teams/6"))
//            .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/teams/1"))
//            .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void deleteTeamAsGroup() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/teams/12"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/teams/6"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/teams/1"))
//            .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void deleteTeamAsTeam() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/teams/12"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/teams/6"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/teams/1"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1")
//    public void deleteTeamAsMember() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/teams/12"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/teams/6"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/teams/1"))
//            .andExpect(status().isForbidden());
//    }

}
