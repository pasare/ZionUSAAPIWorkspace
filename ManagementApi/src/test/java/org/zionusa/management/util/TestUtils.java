package org.zionusa.management.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.zionusa.base.util.auth.SecurityConstants.X_APPLICATION_ID;

public class TestUtils {

    public static Object getTestData(String dataPath, List<Object> mockDataArray, Class dataType) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        String mockDataPath = dataPath;

        byte[] accessesData = Files.readAllBytes(Paths.get(mockDataPath));

        mockDataArray = mapper.readValue(accessesData, new TypeReference<List<Object>>() {
        });

        return mockDataArray;
    }

    public static RequestBuilder deleteWithHeaders(String urlTemplate, Object... uriVars) {
        return withHeaders(delete(urlTemplate, uriVars));
    }

    public static RequestBuilder getWithHeaders(String urlTemplate, Object... uriVars) {
        return withHeaders(get(urlTemplate, uriVars));
    }

    public static RequestBuilder postWithHeaders(String urlTemplate, Object... uriVars) {
        return withHeaders(post(urlTemplate, uriVars));
    }

    public static RequestBuilder putWithHeaders(String urlTemplate, Object... uriVars) {
        return withHeaders(put(urlTemplate, uriVars));
    }

    public static RequestBuilder withHeaders(MockHttpServletRequestBuilder request) {
        return request.header(X_APPLICATION_ID, X_APPLICATION_ID);
    }
}
