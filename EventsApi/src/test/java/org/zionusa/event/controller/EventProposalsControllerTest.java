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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zionusa.event.util.TestUtils.withHeaders;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@Transactional
public class EventProposalsControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void getAllUnpublishedEventProposalsForMember() throws Exception {
//        mockMvc.perform(withHeaders(get("/event-proposals")))
//            .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void publishEventProposalAsMember() throws Exception {
//        mockMvc.perform(withHeaders(put("/event-proposals/1/publish")))
//            .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void publishEventProposalAsAdmin() throws Exception {
//        mockMvc.perform(put("/event-proposals/1/publish"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", userApplicationRoles = {"MY_ZIONUSA_EDITOR"})
//    public void publishEventProposalAsEditorial() throws Exception {
//        mockMvc.perform(put("/event-proposals/1/publish"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockCustomUser(access = "Member")
//    public void unPublishEventProposalAsMember() throws Exception {
//        mockMvc.perform(withHeaders(put("/event-proposals/1/un-publish")))
//            .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockCustomUser(access = "Admin")
//    public void unPublishEventProposalAsAdmin() throws Exception {
//        mockMvc.perform(put("/event-proposals/1/un-publish"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockCustomUser(access = "Member", userApplicationRoles = {"MY_ZIONUSA_EDITOR"})
//    public void unPublishEventProposalAsEditorial() throws Exception {
//        mockMvc.perform(put("/event-proposals/1/un-publish"))
//                .andExpect(status().isOk());
//    }
}
