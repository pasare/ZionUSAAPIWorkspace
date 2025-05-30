package org.zionusa.management.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.management.util.TestConfigForMail;
import org.zionusa.management.util.WithMockCustomUser;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
public class AssociationControllerTest {

    //    private final Gson gson = new Gson();
//    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
//        MediaType.APPLICATION_JSON.getSubtype(),
//        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockCustomUser(access = "Member")
    public void getAllAccesses() throws Exception {
//        mockMvc.perform(withHeaders(get("/associations")
//                .header("X-APPLICATION-ID", "")))
//            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))))
//            .andExpect(status().isOk());
    }
}
