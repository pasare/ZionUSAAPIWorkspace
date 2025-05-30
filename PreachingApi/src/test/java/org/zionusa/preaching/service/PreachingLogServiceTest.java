package org.zionusa.preaching.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.preaching.dao.PreachingLogDao;
import org.zionusa.preaching.dao.ReportDao;
import org.zionusa.preaching.domain.Partner;
import org.zionusa.preaching.domain.PreachingLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PreachingLogServiceTest {

    @InjectMocks
    private PreachingLogService service;

    private List<PreachingLog> mockPreachingLogs;

    @Mock
    PreachingLogDao dao;

    @Mock
    ReportDao reportDao;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockPreachingLogsPath = "src/test/resources/preaching-logs.json";

        byte[] preachingLogData = Files.readAllBytes(Paths.get(mockPreachingLogsPath));
        mockPreachingLogs = mapper.readValue(preachingLogData, new TypeReference<List<PreachingLog>>() {});

        when(dao.findAll()).thenReturn(mockPreachingLogs);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockPreachingLogs.get(0)));
    }

    @Test
    public void savePreachingLogWithNoExistingReportsTest() {
        PreachingLog previousPreachingLog = mockPreachingLogs.get(0);
        PreachingLog mockPreachingLog = new PreachingLog();
        mockPreachingLog.setDate("2019-01-01");

        mockPreachingLog.setPartner1(new Partner(1, 1, 1, 1, 1));
        mockPreachingLog.setPartner2(new Partner(2, 2, 2, 2, 2));
        mockPreachingLog.setPartner3(new Partner(3, 3, 3, 3, 3));

        when(reportDao.getReportByUserIdAndDate(anyInt(), anyString())).thenReturn(null);

        when(dao.save(any(PreachingLog.class))).thenReturn(mockPreachingLog);
        service.save(mockPreachingLog);

        assertThat(previousPreachingLog).isNotNull();
        assertThat(mockPreachingLog).isNotNull();
        //assertThat(p)
        verify(reportDao, times(3)).getReportByUserIdAndDate(anyInt(), anyString());
    }
}
