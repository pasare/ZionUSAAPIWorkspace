package org.zionusa.biblestudy.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.zionusa.base.util.auth.AuthenticatedUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static Object getTestData(String dataPath, List<Object> mockDataArray, Class dataType) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        String mockDataPath = dataPath;

        byte[] accessesData = Files.readAllBytes(Paths.get(mockDataPath));

        mockDataArray = mapper.readValue(accessesData, new TypeReference<List<Object>>() {});

        return mockDataArray;
    }

    public static AuthenticatedUser mockAuthenticatedUser(Integer id, Integer teamId, Integer groupId, Integer churchId, String access, String role, List<String> applicationRoles) {
        AuthenticatedUser principal = new AuthenticatedUser("A", "User", Collections.emptyList());

        principal.setId(id);
        principal.setTeamId(teamId);
        principal.setGroupId(groupId);
        principal.setChurchId(churchId);
        principal.setDisplayName("A Test User");
        principal.setActiveDirectoryId("2121-adad-2332-adf");
        principal.setAccess(access);
        principal.setRole(role);
        principal.setTeamName("A Team");
        principal.setGroupName("A Group");
        principal.setChurchName("A Church");
        principal.setUserApplicationRoles(applicationRoles);

        return principal;
    }
}
