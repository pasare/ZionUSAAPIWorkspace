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
import org.zionusa.management.domain.TransferRequest;
import org.zionusa.management.util.TestConfigForMail;
import org.zionusa.management.util.WithMockCustomUser;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
public class TransferRequestControllerTest {

    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getAllTransferRequestsAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/approved"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/denied"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//    }

    @Test
    @WithMockCustomUser(access = "Church", churchId = "1")
    public void getAllTransferRequestsAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests"))
//            .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/pending"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/approved"))
//            .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/denied"))
//            .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getAllTransferRequestsAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getAllTransferRequestsAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getAllTransferRequestsAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getUserTransferRequestsAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/approved"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/denied"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getUserTransferRequestsAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/approved"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/denied"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getUserTransferRequestsAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/approved"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/denied"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getUserTransferRequestsAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/approved"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/denied"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getUserTransferRequestsAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/approved"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/1/denied"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/users/6/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getTeamTransferRequestsAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/denied"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getTeamTransferRequestsAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/pending"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/denied"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getTeamTransferRequestsAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/pending"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/denied"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getTeamTransferRequestsAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/pending"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/denied"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getTeamTransferRequestsAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/pending"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/pending"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/teams/10/denied"))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getGroupTransferRequestsAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/denied"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getGroupTransferRequestsAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getGroupTransferRequestsAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getGroupTransferRequestsAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/denied"))
//                .andExpect(status().isForbidden());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getGroupTransferRequestsAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/1/denied"))
//                .andExpect(status().isForbidden());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/groups/5/denied"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void getChurchTransferRequestsAsAdmin() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/denied"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void getChurchTransferRequestsAsChurch() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/pending"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/denied"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void getChurchTransferRequestsAsGroup() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/denied"))
//                .andExpect(status().isForbidden());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/denied"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void getChurchTransferRequestsAsTeam() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/pending"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/approved"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/denied"))
//                .andExpect(status().isOk());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/denied"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void getChurchTransferRequestsAsMember() throws Exception {
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/1/denied"))
//                .andExpect(status().isForbidden());
//
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/pending"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/approved"))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(getWithHeaders("/transfer-requests/churches/4/denied"))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void saveTransferRequestAsAdmin() throws Exception {
//
//        TransferRequest transferRequest = createTransferRequest();
//
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void saveTransferRequestAsChurch() throws Exception {
//
//        TransferRequest transferRequest = createTransferRequest();
//
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //different church
//        transferRequest.setNewChurchId(4);
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void saveTransferRequestAsGroup() throws Exception {
//
//        TransferRequest transferRequest = createTransferRequest();
//
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //different church
//        transferRequest.setNewGroupId(4);
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void saveTransferRequestAsTeam() throws Exception {
//
//        TransferRequest transferRequest = createTransferRequest();
//
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        // different church
//        transferRequest.setNewTeamId(4);
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void saveTransferRequestAsMember() throws Exception {
//
//        TransferRequest transferRequest = createTransferRequest();
//
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()));
//
//        //different church
//        transferRequest.setUserId(4);
//        mockMvc.perform(withHeaders(post("/transfer-requests")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1")
//                .content(gson.toJson(transferRequest))
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void processTransferRequestAsAdmin() throws Exception {
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/approve")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/deny")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Church", churchId = "1")
//    public void processTransferRequestAsChurch() throws Exception {
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/approve")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/deny")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/approve")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/deny")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Group", groupId = "1")
//    public void processTransferRequestAsGroup() throws Exception {
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/approve")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/deny")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/approve")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/deny")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Team", teamId = "1")
//    public void processTransferRequestAsTeam() throws Exception {
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/approve")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/deny")
//                .contentType(contentType)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/approve")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/deny")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", id = "1")
//    public void processTransferRequestAsMember() throws Exception {
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/approve")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/1/deny")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/approve")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(withHeaders(put("/transfer-requests/4/deny")
//                .contentType(contentType)))
//                .andExpect(status().isForbidden());
//    }

    private TransferRequest createTransferRequest() {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setUserId(1);
        transferRequest.setNewTeamId(1);
        transferRequest.setNewGroupId(1);
        transferRequest.setNewChurchId(1);
        transferRequest.setRequestStatus("Test");

        return transferRequest;
    }

}
