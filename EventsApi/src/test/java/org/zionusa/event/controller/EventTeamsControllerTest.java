package org.zionusa.event.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.event.util.WithMockCustomUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zionusa.event.util.TestUtils.withHeaders;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@Transactional
public class EventTeamsControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllEventTeamsForMember() throws Exception {
//        mockMvc.perform(withHeaders(get("/event-teams")))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllBranchEventTeamsForMember() throws Exception {
//        mockMvc.perform(withHeaders(get("/event-teams/branch/1")))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllBranchEventTeamsForMemberNotFound() throws Exception {
//        mockMvc.perform(withHeaders(get("/event-teams/branch/3")))
//            .andExpect(status().isNotFound());
//    }
}
