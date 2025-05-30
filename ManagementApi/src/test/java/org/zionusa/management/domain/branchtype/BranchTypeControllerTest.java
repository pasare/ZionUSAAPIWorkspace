package org.zionusa.management.domain.branchtype;

import com.google.gson.Gson;
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
import org.zionusa.base.enums.EBranchType;
import org.zionusa.base.util.auth.BaseApplicationRequestFilter;
import org.zionusa.management.util.TestConfigForMail;
import org.zionusa.management.util.WithMockCustomUser;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zionusa.management.util.TestUtils.withHeaders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigForMail.class)
@AutoConfigureMockMvc
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BranchTypeControllerTest {
    private static final String basePath = "/branch-types";
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
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_04_GetAllArchivedOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath + "?archived=true")))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_05_GetAllNotArchivedOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath + "?archived=false")))
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_06_GetByIdOkForAllUsers() throws Exception {
        mockMvc.perform(withHeaders(get(basePath + "/OFFICE")))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void test_07_PatchIsForbidden() throws Exception {
        // Arrange
        EBranchType id = EBranchType.DEFAULT;
        BranchType item = new BranchType(id, "Test");
        // Act
        mockMvc.perform(withHeaders(patch(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void test_08_PostIsForbidden() throws Exception {
        // Arrange
        EBranchType id = EBranchType.DEFAULT;
        BranchType item = new BranchType(id, "Test");
        // Act
        mockMvc.perform(withHeaders(post(basePath)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void test_09_PtIsForbidden() throws Exception {
        // Arrange
        EBranchType id = EBranchType.DEFAULT;
        BranchType item = new BranchType(id, "Test");
        // Act
        mockMvc.perform(withHeaders(put(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void test_10_DeleteIsForbidden() throws Exception {
        // Act
        mockMvc.perform(withHeaders(delete(basePath + "/OFFICE")))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(access = "Admin")
    public void test_11_DeleteMultipleIsForbiddenForNonAdmin() throws Exception {
        // Act
        mockMvc.perform(withHeaders(delete(basePath + "/multiple?ids=OFFICE,HOUSE")))
            .andExpect(status().isForbidden());
    }
}
