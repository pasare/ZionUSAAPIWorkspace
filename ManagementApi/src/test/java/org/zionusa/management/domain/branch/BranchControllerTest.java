package org.zionusa.management.domain.branch;

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
public class BranchControllerTest {

    private static final String basePath = "/branches";
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
            .andExpect(jsonPath("$", hasSize(10)))
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
            .andExpect(jsonPath("$", hasSize(8)))
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
        Integer id = 10;
        Branch item = new Branch();
        item.setId(id);
        item.setName("Test");
        item.setMainBranchId(1);
        item.setStateId(1);
        // Act
        mockMvc.perform(withHeaders(patch(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_08_PatchIsAllowedForNonAdmin() throws Exception {
        // Arrange
        Integer id = 10;
        Branch item = new Branch();
        item.setId(id);
        item.setName("Test");
        item.setMainBranchId(1);
        item.setStateId(1);
        // Act
        mockMvc.perform(withHeaders(patch(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_09_PostIsAllowedForAdmin() throws Exception {
        // Arrange
        Integer id = 11;
        Branch item = new Branch();
        item.setId(id);
        item.setName("Test");
        item.setMainBranchId(1);
        item.setStateId(1);
        item.setTypeId(EBranchType.DEFAULT);
        // Act
        mockMvc.perform(withHeaders(post(basePath)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isCreated());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_10_PostIsAllowedForNonAdmin() throws Exception {
        // Arrange
        Integer id = 10;
        Branch item = new Branch();
        item.setId(id);
        item.setName("Test");
        item.setMainBranchId(1);
        item.setStateId(1);
        item.setTypeId(EBranchType.DEFAULT);
        // Act
        mockMvc.perform(withHeaders(post(basePath)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isCreated());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_11_PutIsAllowedForAdmin() throws Exception {
        // Arrange
        Integer id = 10;
        Branch item = new Branch();
        item.setId(id);
        item.setName("Test");
        item.setMainBranchId(1);
        item.setStateId(1);
        item.setTypeId(EBranchType.DEFAULT);
        // Act
        mockMvc.perform(withHeaders(put(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_12_PutIsAllowedForNonAdmin() throws Exception {
        // Arrange
        Integer id = 10;
        Branch item = new Branch();
        item.setId(id);
        item.setName("Test");
        item.setMainBranchId(1);
        item.setStateId(1);
        item.setTypeId(EBranchType.DEFAULT);

        // Act
        mockMvc.perform(withHeaders(put(basePath + "/" + id)
                .content(gson.toJson(item))
                .contentType(contentType)))
            .andExpect(status().isOk());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_13_DeleteIsForbiddenForNonAdmin() throws Exception {
        // Act
        mockMvc.perform(withHeaders(delete(basePath + "/9")))
            .andExpect(status().isForbidden());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_14_DeleteIsAllowedForAdmin() throws Exception {
        // Act
        mockMvc.perform(withHeaders(delete(basePath + "/9")))
            .andExpect(status().isNoContent());
    }

    @Test()
    @WithMockCustomUser(access = "Member")
    public void test_15_DeleteMultipleIsForbiddenForNonAdmin() throws Exception {
        // Act
        mockMvc.perform(withHeaders(delete(basePath + "/multiple?ids=7,8")))
            .andExpect(status().isForbidden());
    }

    @Test()
    @WithMockCustomUser(access = "Admin")
    public void test_16_DeleteMultipleIsAllowedForAdmin() throws Exception {
        // Act
        mockMvc.perform(withHeaders(delete(basePath + "/multiple?ids=7,8")))
            .andExpect(status().isNoContent());
    }
}
