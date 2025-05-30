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
public class UserControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getAllUsersAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(12))));
//    }

//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void getAllUsersAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/users"))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void getAllUsersAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/users"))
//                .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockCustomUser(access = "Team")
    public void getAllUsersAsTeam() throws Exception {
//        mockMvc.perform(withHeaders(get("/users")
//                .header("X-APPLICATION-ID", "")))
//            .andExpect(status().isOk());
    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllUsersAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
//    }
//
//    @Test
//    public void getAllUsersAsUnauthenticated() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/display"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllDisplayUsersAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/display"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getAllUsersByGenderAsAdmin() throws Exception {
////        mockMvc.perform(getWithHeaders("/users/gender/M"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(9))));
//
//        mockMvc.perform(getWithHeaders("/users/gender/F"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church")
//    public void getAllUsersByGenderAsChurch() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/gender/M"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/gender/F"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group")
//    public void getAllUsersByGenderAsGroup() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/gender/M"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/gender/F"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team")
//    public void getAllUsersByGenderAsTeam() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/gender/M"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/gender/F"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllUsersByGenderAsMember() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/gender/M"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/gender/F"))
//                .andExpect(status().isForbidden());
//    }
//
////    @Test
////    @WithMockCustomUser(access = "Admin")
////    public void getUserByIdAsAdmin() throws Exception {
////
////        mockMvc.perform(getWithHeaders("/users/1"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id", notNullValue()));
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getUserByIdAsChurch() throws Exception {
//
//        //branch church
//        mockMvc.perform(getWithHeaders("/users/9"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(getWithHeaders("/users/11"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //different church
//        mockMvc.perform(getWithHeaders("/users/6"))
//                .andExpect(status().isForbidden());
//
//        //same church
//        mockMvc.perform(getWithHeaders("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
////    @Test
////    @WithMockCustomUser(access = "Group", groupId = "1")
////    public void getUserByIdAsGroup() throws Exception {
////
////        //different group
////        mockMvc.perform(getWithHeaders("/users/9"))
////                .andExpect(status().isForbidden());
////
////        //same group
////        mockMvc.perform(getWithHeaders("/users/1"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id", notNullValue()));
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getUserByIdAsTeam() throws Exception {
//
//        //different group
//        mockMvc.perform(getWithHeaders("/users/9"))
//                .andExpect(status().isForbidden());
//
//        //same group
//        mockMvc.perform(getWithHeaders("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getUserByIdAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/users/9"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveUserAsAdmin() throws Exception {
//        User user = createUser();
//
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        user.setUsername("randomname@random.com");
//        mockMvc.perform(withHeaders(put("/users/1")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void saveUserAsChurch() throws Exception {
//        User user = createUser();
//
//        //save user to branch church
//        user.setTeamId(5);
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        user.setUsername("Random name2");
//        mockMvc.perform(withHeaders(put("/users/12")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //save user to different church
//        user.setTeamId(10);
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/users/4")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //save user to same church
//        user.setTeamId(1);
//        user.setUsername("test@teser.com");
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        user.setUsername("test12@tester.com");
//        mockMvc.perform(withHeaders(put("/users/1")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void saveUserAsGroup() throws Exception {
//        User user = createUser();
//
//        //different group
//        user.setTeamId(8);
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/users/10")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //same group
//        user.setTeamId(1);
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isCreated());
//
//        user.setUsername("randomname1@random.com");
//        mockMvc.perform(withHeaders(put("/users/1")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void saveUserAsTeam() throws Exception {
//        User user = createUser();
//
//        //different team
////        user.setTeamId(8);
////        mockMvc.perform(withHeaders(post("/users")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/users/10")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //same team
//        user.setTeamId(1);
//        user.setUsername("randomname@name.com");
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isCreated());
//
//        user.setUsername("test@test.com1");
//        mockMvc.perform(withHeaders(put("/users/1")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "2")
//    public void saveUserAsMember() throws Exception {
//        User user = createUser();
//
//        //different user
//        user.setTeamId(8);
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/users/10")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //same user
//        user.setId(null);
//        mockMvc.perform(withHeaders(post("/users")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        user.setId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin", id = "2", groupId = "2")
//    public void attemptToChangeUserSecurityAsAdmin() throws Exception {
//        User user = createUser();
//        user.setId(2);
//
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        //change access to admin
//        user.setAccessId(1);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        //change access to church
//        user.setAccessId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", role = "Church Leader", id = "2", groupId = "2")
//    public void attemptToChangeUserAccessAsChurch() throws Exception {
//        User user = createUser();
//        user.setId(2);
//
//        //set access to group
////        user.setAccessId(4);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isOk());
//
//        //change access to admin
//        user.setAccessId(1);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to church, same as mine
//        user.setAccessId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        //change access to group
//        user.setAccessId(3);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
////    @Test
////    @WithMockCustomUser(access = "Church", role = "Church Leader", id = "2", groupId = "2")
////    public void attemptToChangeUserRoleAsChurch() throws Exception {
////        User user = createUser();
////        user.setId(2);
////
////        //set role to group
////        user.setRoleId(4);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isOk());
////
////        //change role to overseer
////        user.setRoleId(1);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////
////        //change role to church leader
////        user.setRoleId(2);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////
////        //change access to group leader
////        user.setRoleId(3);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isOk());
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", role = "Group Leader", id = "2", groupId = "2")
//    public void attemptToChangeUserAccessAsGroup() throws Exception {
//        User user = createUser();
//        user.setId(2);
//
//        //set access to group
////        user.setAccessId(4);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isOk());
//
//        //change access to admin
//        user.setAccessId(1);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to church
//        user.setAccessId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to group, same as mine
//        user.setAccessId(3);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
////    @Test
////    @WithMockCustomUser(access = "Group", role = "Group Leader", id = "2", groupId = "2")
////    public void attemptToChangeUserRoleAsGroup() throws Exception {
////        User user = createUser();
////        user.setId(1);
////
////        //set role to group
////        user.setRoleId(4);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isOk());
////
////        //change role to overseer
////        user.setRoleId(1);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////
////        //change role to church leader
////        user.setRoleId(2);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////
////        //change role to group leader
////        user.setRoleId(3);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
////    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", role = "Team Leader", id = "2", teamId = "2")
//    public void attemptToChangeUserAccessAsTeam() throws Exception {
//        User user = createUser();
//        user.setId(2);
//        user.setTeamId(2);
//
//        //set access to member
//        user.setAccessId(5);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        //change access to admin
//        user.setAccessId(1);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to church
//        user.setAccessId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to group
//        user.setAccessId(3);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to team, same as mine
//        user.setAccessId(4);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", role = "Team Leader", id = "1", teamId = "2")
//    public void attemptToChangeUserRoleAsTeam() throws Exception {
//        User user = createUser();
//        user.setId(2);
//        user.setTeamId(2);
//
//        //set role to member
//        user.setRoleId(5);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        //change role to overseer
//        user.setRoleId(1);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to church leader
//        user.setRoleId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to group leader
//        user.setRoleId(3);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to team leader
//        user.setRoleId(4);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", role = "Member", id = "2", teamId = "2")
//    public void attemptToChangeUserAccessAsMember() throws Exception {
//        User user = createUser();
//        user.setId(2);
//        user.setTeamId(2);
//
//        //change access to admin
////        user.setAccessId(1);
////        mockMvc.perform(withHeaders(put("/users/2")
////                .content(gson.toJson(user))
////                .contentType(contentType)))
////                .andExpect(status().isForbidden());
//
//        //change access to church
//        user.setAccessId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to group
//        user.setAccessId(3);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change access to team
//        user.setAccessId(4);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", role = "Member", id = "1", teamId = "2")
//    public void attemptToChangeUserRoleAsMember() throws Exception {
//        User user = createUser();
//        user.setId(2);
//        user.setTeamId(2);
//
//        //set role to member
//        user.setRoleId(5);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to overseer
//        user.setRoleId(1);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to church leader
//        user.setRoleId(2);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to group leader
//        user.setRoleId(3);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        //change role to team leader
//        user.setRoleId(4);
//        mockMvc.perform(withHeaders(put("/users/2")
//                .content(gson.toJson(user))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void archiveUserAsAdmin() throws Exception {
//        mockMvc.perform(putWithHeaders("/users/8/archive"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(putWithHeaders("/users/9/archive"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void archiveUserAsChurch() throws Exception {
//
//        //branch church user
////        mockMvc.perform(putWithHeaders("/users/4/archive"))
////                .andExpect(status().isOk());
//
//        //different church
//        mockMvc.perform(putWithHeaders("/users/6/archive"))
//                .andExpect(status().isForbidden());
//
//        //same church
//        mockMvc.perform(putWithHeaders("/users/1/archive"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void archiveUserAsGroup() throws Exception {
//
//        //different group
//        mockMvc.perform(putWithHeaders("/users/9/archive"))
//                .andExpect(status().isForbidden());
//
//        //same group
//        mockMvc.perform(putWithHeaders("/users/1/archive"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void archiveUserAsTeam() throws Exception {
//
//        //different team
//        mockMvc.perform(putWithHeaders("/users/9/archive"))
//                .andExpect(status().isForbidden());
//
//        //same team
//        mockMvc.perform(putWithHeaders("/users/1/archive"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void archiveUserAsMember() throws Exception {
//
//        //different user
//        mockMvc.perform(putWithHeaders("/users/9/archive"))
//                .andExpect(status().isForbidden());
//
//        //same user
//        mockMvc.perform(putWithHeaders("/users/1/archive"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void deleteUserAsAdmin() throws Exception {
//
//        mockMvc.perform(deleteWithHeaders("/users/10"))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/users/5"))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(deleteWithHeaders("/users/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void deleteUserAsChurch() throws Exception {
//
//        //branch church user
//        mockMvc.perform(deleteWithHeaders("/users/4"))
//                .andExpect(status().isForbidden());
//
//        //different church
//        mockMvc.perform(deleteWithHeaders("/users/6"))
//                .andExpect(status().isForbidden());
//
//        //same church
//        mockMvc.perform(deleteWithHeaders("/users/1"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void deleteUserAsGroup() throws Exception {
//
//        //different group
//        mockMvc.perform(deleteWithHeaders("/users/9"))
//                .andExpect(status().isForbidden());
//
//        //same group
//        mockMvc.perform(deleteWithHeaders("/users/1"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void deleteUserAsTeam() throws Exception {
//
//        //different team
//        mockMvc.perform(deleteWithHeaders("/users/9"))
//                .andExpect(status().isForbidden());
//
//        //same team
//        mockMvc.perform(deleteWithHeaders("/users/1"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void deleteUserAsMember() throws Exception {
//
//        //different user
//        mockMvc.perform(deleteWithHeaders("/users/9"))
//                .andExpect(status().isForbidden());
//
//        //same user
//        mockMvc.perform(deleteWithHeaders("/users/1"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void checkUsernameAvailableAsAdmin() throws Exception {
//        String username = "test@test.com";
//
//        mockMvc.perform(withHeaders(post("/users/checkusername")
//                .content(gson.toJson(username))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", equalTo(true)));
//
//        username = "patrick.asare@zionusa.org";
//        mockMvc.perform(withHeaders(post("/users/checkusername")
//                .content(gson.toJson(username))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", equalTo(true)));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void activateUserAsAdmin() throws Exception {
//        mockMvc.perform(putWithHeaders("/users/1/activate"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.archived", equalTo(false)));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void activateUserAsChurch() throws Exception {
//
//        //user in different church
//        mockMvc.perform(putWithHeaders("/users/6/activate"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/users/4/activate"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.archived", equalTo(false)));
//
//        //same church
//        mockMvc.perform(putWithHeaders("/users/1/activate"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.archived", equalTo(false)));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void activeUserAsGroup() throws Exception {
//
//        //different group
//        mockMvc.perform(putWithHeaders("/users/9/activate"))
//                .andExpect(status().isForbidden());
//
//        //same group
//        mockMvc.perform(putWithHeaders("/users/1/activate"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.archived", equalTo(false)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void activateUserAsTeam() throws Exception {
//
//        //different team
//        mockMvc.perform(putWithHeaders("/users/9/activate"))
//                .andExpect(status().isForbidden());
//
//        //same team
//        mockMvc.perform(putWithHeaders("/users/1/activate"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.archived", equalTo(false)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void activateUserAsMember() throws Exception {
//
//        //different user
//        mockMvc.perform(putWithHeaders("/users/9/activate"))
//                .andExpect(status().isForbidden());
//
//        //same user
//        mockMvc.perform(putWithHeaders("/users/1/activate"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void enableUserAsAdmin() throws Exception {
//        mockMvc.perform(putWithHeaders("/users/1/enable"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void enableUserAsChurch() throws Exception {
//
//        //user in different church
//        mockMvc.perform(putWithHeaders("/users/6/enable"))
//                .andExpect(status().isForbidden());
//
//        //branch church
//        mockMvc.perform(putWithHeaders("/users/4/enable"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//
//        //same church
//        mockMvc.perform(putWithHeaders("/users/1/enable"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void enableUserAsGroup() throws Exception {
//
//        //different group
//        mockMvc.perform(putWithHeaders("/users/9/enable"))
//                .andExpect(status().isForbidden());
//
//        //same group
//        mockMvc.perform(putWithHeaders("/users/1/enable"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void enableUserAsTeam() throws Exception {
//
//        //different team
//        mockMvc.perform(putWithHeaders("/users/9/enable"))
//                .andExpect(status().isForbidden());
//
//        //same team
//        mockMvc.perform(putWithHeaders("/users/1/enable"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void enableUserAsMember() throws Exception {
//
//        //different user
//        mockMvc.perform(putWithHeaders("/users/9/enable"))
//                .andExpect(status().isForbidden());
//
//        //same user
//        mockMvc.perform(putWithHeaders("/users/1/enable"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.enabled", equalTo(true)));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getDeactivatedListAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/users/deactivated"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/users/disabled"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getDeactivatedListAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/users/deactivated"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/disabled"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getDeactivatedListAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/users/deactivated"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/disabled"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getDeactivatedListAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/users/deactivated"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/disabled"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getDeactivatedListAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/users/deactivated"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/users/disabled"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void expireCacheAsAdmin() throws Exception {
//        mockMvc.perform(getWithHeaders("/users/expire"))
//                .andExpect(status().isOk());
//    }
//
//    private User createUser() {
//        User user = new User();
//        user.setTeamId(4);
//        user.setAccessId(5);
//        user.setRoleId(5);
//        user.setTitleId(8);
//        user.setFirstName("Mock User");
//        user.setGender("M");
//        user.setUsername("Mock.user@user.com");
//        user.setArchived(false);
//        user.setEnabled(true);
//        user.setGaGrader(false);
//        user.setTeacher(false);
//        user.setReadyGrader(false);
//        return user;
//    }

}
