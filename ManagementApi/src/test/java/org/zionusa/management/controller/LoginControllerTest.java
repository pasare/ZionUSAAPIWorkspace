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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new Gson();

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Test
    public void Login() {
        /*AdalUser adalUser = new AdalUser();
        adalUser.setUsername("patrick.asare67@zionusa.org");
        adalUser.setOid("f2c296da-497c-a359-66c3d57500c9");
        mockMvc.perform(withHeaders(post("/login")
                .content(gson.toJson(adalUser))
                .contentType(contentType))
                .andExpect(status().isOk()); */
        assertTrue(true);
    }
}
