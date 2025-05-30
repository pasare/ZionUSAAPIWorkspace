package org.zionusa.management.domain.access;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.util.auth.BaseApplicationRequestFilter;
import org.zionusa.management.util.TestConfigForMail;
import org.zionusa.management.util.WithMockCustomUser;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zionusa.management.util.TestUtils.deleteWithHeaders;
import static org.zionusa.management.util.TestUtils.withHeaders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessControllerTest {

    private static final String basePath = "/accesses";
    private final Gson gson = new Gson();
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

    @Test(expected = BaseApplicationRequestFilter.UnknownException.class)
    public void test_01_GetAllForbiddenWithoutXApplicationId() throws Exception {
        mockMvc.perform(get(basePath));
    }

    @Test()
    public void test_02_GetAllForbiddenWithoutAuthenticatedUser() throws Exception {
        mockMvc.perform(withHeaders(get(basePath)))
            .andExpect(status().isForbidden());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_03_GetAllOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath)))
            .andExpect(jsonPath("$", hasSize(8)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_04_GetAllArchivedOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath + "?archived=true")))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_05_GetAllNotArchivedOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath + "?archived=false")))
            .andExpect(jsonPath("$", hasSize(6)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_06_GetByIdOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath + "/1")))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_07_PatchIsAllowedForAdmin() throws Exception {
        // Arrange
        Integer id = 8;
        Access item = new Access();
        item.setId(id);
        item.setName("Test");
        // Act
        mockMvc.perform(withHeaders(patch(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_08_PatchIsForbiddenForNonAdmin() throws Exception {
        // Arrange
        Integer id = 8;
        Access item = new Access();
        item.setId(id);
        item.setName("Test");
        // Act
        mockMvc.perform(withHeaders(patch(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_09_PostIsAllowedForAdmin() throws Exception {
        // Arrange
        Integer id = 9;
        Access item = new Access();
        item.setId(id);
        item.setName("Test");
        // Act
        mockMvc.perform(withHeaders(post(basePath)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isCreated());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_10_PostIsForbiddenForNonAdmin() throws Exception {
        // Arrange
        Integer id = 8;
        Access item = new Access();
        item.setId(id);
        item.setName("Test");
        // Act
        mockMvc.perform(withHeaders(post(basePath)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_11_PutIsAllowedForAdmin() throws Exception {
        // Arrange
        Integer id = 8;
        Access item = new Access();
        item.setId(id);
        item.setName("Test");
        // Act
        mockMvc.perform(withHeaders(put(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_12_PutIsForbiddenForNonAdmin() throws Exception {
        // Arrange
        Integer id = 8;
        Access item = new Access();
        item.setId(id);
        item.setName("Test");
        // Act
        mockMvc.perform(withHeaders(put(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    // Old tests below -------------------------------------------------------------------------------------------------

    @Test
    @WithMockCustomUser(access = "Admin")
    public void saveAccessAsAdmin() throws Exception {
        Access access = new Access(null, "Test Access");

        mockMvc.perform(withHeaders(post("/accesses")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
            .andExpect(status().isCreated());

        access = new Access(null, "Test Access2");
        mockMvc.perform(withHeaders(put("/accesses/1")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(access = "Church")
    public void saveAccessAsChurch() throws Exception {
        Access access = new Access(null, "Test Access");

        mockMvc.perform(withHeaders(post("/accesses")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());

        mockMvc.perform(withHeaders(put("/accesses/1")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Group")
    public void saveAccessAsGroup() throws Exception {
        Access access = new Access(null, "Test Access");

        mockMvc.perform(withHeaders(post("/accesses")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());

        mockMvc.perform(withHeaders(put("/accesses/1")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Team")
    public void saveAccessAsTeam() throws Exception {
        Access access = new Access(null, "Test Access");

        mockMvc.perform(withHeaders(post("/accesses")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());

        mockMvc.perform(withHeaders(put("/accesses/1")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Member")
    public void saveAccessAsMember() throws Exception {
        Access access = new Access(null, "Test Access");

        mockMvc.perform(withHeaders(post("/accesses")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());

        mockMvc.perform(withHeaders(put("/accesses/1")
                .content(gson.toJson(access))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void test_13_DeleteAccessAsAdmin() throws Exception {
        mockMvc.perform(deleteWithHeaders("/accesses/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser(access = "Church")
    public void test_14_DeleteAccessAsChurch() throws Exception {
        mockMvc.perform(deleteWithHeaders("/accesses/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Group")
    public void test_15_DeleteAccessAsGroup() throws Exception {
        mockMvc.perform(deleteWithHeaders("/accesses/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Team")
    public void test_16_DeleteAccessAsTeam() throws Exception {
        mockMvc.perform(deleteWithHeaders("/accesses/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Member")
    public void test_17_DeleteAccessAsMember() throws Exception {
        mockMvc.perform(deleteWithHeaders("/accesses/1"))
            .andExpect(status().isForbidden());
    }
}
