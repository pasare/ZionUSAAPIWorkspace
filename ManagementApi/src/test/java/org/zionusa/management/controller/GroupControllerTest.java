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
public class GroupControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getAllGroupsAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(6))));
//    }

    @Test
    @WithMockCustomUser(access = "Church")
    public void getAllGroupsAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups"))
//            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Group")
    public void getAllGroupsAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups"))
//            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Team")
    public void getAllGroupsAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups"))
//            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Member")
    public void getAllGroupsAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups"))
//            .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockCustomUser(access = "Admin", groupId = "1")
//    public void getGroupByIdAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1", groupId = "1")
//    public void getGroupByIdAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //group from branch church
//        mockMvc.perform(getWithHeaders("/groups/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/4"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //group from different church
//        mockMvc.perform(getWithHeaders("/groups/5"))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getGroupByIdAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/2"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/groups/3"))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Team", groupId = "1")
//    public void getGroupByIdAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/2"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/groups/3"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", groupId = "1")
//    public void getGroupByIdAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/groups/2"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/groups/3"))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveGroupAsAdmin() throws Exception {
//        Group group = new Group();
//        group.setChurchId(4);
//        group.setName("Mock Group");
//        group.setChurchGroup(false);
//        group.setArchived(false);
//
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/groups/4")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1", groupId = "1")
//    public void saveGroupAsChurch() throws Exception {
//        Group group = new Group();
//        group.setChurchId(4);
//        group.setName("Mock Group");
//        group.setChurchGroup(false);
//        group.setArchived(false);
//
//        //different church
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/groups/4")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //Branch church
//        group.setChurchId(2);
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/groups/2")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        group.setChurchId(3);
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/groups/3")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//
//        //same church
//        group.setChurchId(1);
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/groups/2")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }

//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1", groupId = "1")
//    public void saveGroupAsGroup() throws Exception {
//        Group group = new Group();
//        group.setChurchId(1);
//        group.setName("Mock Group");
//        group.setChurchGroup(false);
//        group.setArchived(false);
//
//        //different group
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        group.setId(4);
//        mockMvc.perform(withHeaders(put("/groups/4")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //same group
//        group.setId(1);
//        mockMvc.perform(withHeaders(put("/groups/1")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }

//    @Test
//    @WithMockCustomUser(access = "Team", churchId = "1", groupId = "1")
//    public void saveGroupAsTeam() throws Exception {
//        Group group = new Group();
//        group.setChurchId(1);
//        group.setName("Mock Group");
//        group.setChurchGroup(false);
//        group.setArchived(false);
//
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        group.setId(4);
//        mockMvc.perform(withHeaders(put("/groups/4")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }

//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1", groupId = "1")
//    public void saveGroupAsMember() throws Exception {
//        Group group = new Group();
//        group.setChurchId(1);
//        group.setName("Mock Group");
//        group.setChurchGroup(false);
//        group.setArchived(false);
//
//        mockMvc.perform(withHeaders(post("/groups")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        group.setId(4);
//        mockMvc.perform(withHeaders(put("/groups/4")
//                .content(gson.toJson(group))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void archiveGroupAsAdmin() throws Exception {
//
//        //try to archive church group
//        mockMvc.perform(putWithHeaders("/groups/4/archive"))
//                .andExpect(status().isConflict());
//
//        mockMvc.perform(putWithHeaders("/groups/3/archive"))
//                .andExpect(status().isOk());
//
//        /*mockMvc.perform(withHeaders(put("/groups/1/archive"))
//                .andExpect(status().isNoContent());*/
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void archiveGroupAsChurch() throws Exception {
//
//        //try to archive church group
//        mockMvc.perform(putWithHeaders("/groups/4/archive"))
//                .andExpect(status().isConflict());
//
//        mockMvc.perform(putWithHeaders("/groups/3/archive"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(putWithHeaders("/groups/1/archive"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void archiveGroupAsGroup() throws Exception {
//
//        //try to archive church group
//        mockMvc.perform(putWithHeaders("/groups/4/archive"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(putWithHeaders("/groups/3/archive"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(putWithHeaders("/groups/1/archive"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void archiveGroupAsTeam() throws Exception {
//
//        //try to archive church group
//        mockMvc.perform(putWithHeaders("/groups/4/archive"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(putWithHeaders("/groups/3/archive"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(putWithHeaders("/groups/1/archive"))
//            .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockCustomUser(access = "Member", id = "1")
    public void archiveGroupAsMember() throws Exception {

//        //try to archive church group
//        mockMvc.perform(putWithHeaders("/groups/4/archive"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(putWithHeaders("/groups/3/archive"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(putWithHeaders("/groups/1/archive"))
//            .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void deleteGroupAsAdmin() throws Exception {
//
//        //try to delete church group
//        mockMvc.perform(deleteWithHeaders("/groups/4"))
//                .andExpect(status().isConflict());
//
//        mockMvc.perform(deleteWithHeaders("/groups/3"))
//                .andExpect(status().isNoContent());
//
//        /*mockMvc.perform(deleteWithHeaders("/groups/1"))
//                .andExpect(status().isNoContent());*/
//    }

//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void deleteGroupAsChurch() throws Exception {
//
//        //try to delete group from different church
//        mockMvc.perform(deleteWithHeaders("/groups/5"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/groups/1"))
//                .andExpect(status().isNoContent());
//
//    }

//    @Test
//    @WithMockCustomUser(access = "Group", churchId = "1", groupId = "1")
//    public void deleteGroupAsGroup() throws Exception {
//
//        //try to delete group from different church
//        mockMvc.perform(deleteWithHeaders("/groups/3"))
//            .andExpect(status().isForbidden());
//
//        //try to delete church group
//        mockMvc.perform(deleteWithHeaders("/groups/2"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/groups/1"))
//            .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", churchId = "1", groupId = "1")
//    public void deleteGroupAsMember() throws Exception {
//
//        //try to delete group from different church
//        mockMvc.perform(deleteWithHeaders("/groups/3"))
//            .andExpect(status().isForbidden());
//
//        //try to delete church group
//        mockMvc.perform(deleteWithHeaders("/groups/2"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(deleteWithHeaders("/groups/1"))
//            .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    public void getGroupInformationAsUnauthenticated() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/information"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getGroupInformationAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/information"))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getGroupTeamsAsUnauthenticated() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1/teams"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getGroupTeamsAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1/teams"))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getGroupDisplayTeamsAsUnauthenticated() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1/teams/display"))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getGroupDisplayTeamsAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/groups/1/teams/display"))
//            .andExpect(status().isOk());
//    }
}
