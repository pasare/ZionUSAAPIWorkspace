package org.zionusa.admin.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.server.ResponseStatusException;
import org.zionusa.admin.dao.activities.SpecialActivityViewDao;
import org.zionusa.admin.domain.activities.SpecialActivityView;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class SpecialActivityViewServiceTest {
    @Spy()
    @InjectMocks()
    private SpecialActivityViewService service;

    private List<SpecialActivityView> mockItems;

    @Mock
    private SpecialActivityViewDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        final ObjectMapper mapper = new ObjectMapper();

        final String mockItemsPath = "src/test/resources/special-activities.json";

        final byte[] data = Files.readAllBytes(Paths.get(mockItemsPath));
        mockItems = mapper.readValue(data, new TypeReference<List<SpecialActivityView>>() {
        });

        when(dao.findAll()).thenReturn(mockItems);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockItems.get(0)));
    }

    @Test
    public void getAllItems() {
        List<SpecialActivityView> items = service.getAll(null);

        assertThat(items).isNotNull();
        assertThat(items.size()).isEqualTo(6);
        assertThat(mockItems).containsAll(items);
    }

    @Test
    public void getMaxActivityCountForOneDay() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-01";

        final int actual = service.getMaxActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(9);
    }

    @Test
    public void getMaxActivityCountForOneWeek() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-07";

        final int actual = service.getMaxActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(63);
    }

    @Test
    public void getMaxActivityCountForInvalidRange() {
        String startDate = "2021-02-01";
        String endDate = "2021-01-31";

        try {
            service.getMaxActivityCount(startDate, endDate);
            Assert.fail();
        } catch (Exception e) {
            assertThat(e instanceof ResponseStatusException).isEqualTo(true);
        }
    }

    @Test
    public void getTotalDaysForOneDay() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-01";

        final int actual = service.getTotalDays(startDate, endDate);

        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void getTotalDaysForOneWeek() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-07";

        final int actual = service.getTotalDays(startDate, endDate);

        assertThat(actual).isEqualTo(7);
    }

    @Test
    public void getRequiredActivityCountForOneWeek() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-07";

        final int actual = service.getRequiredActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(33);
    }

    @Test
    public void getRequiredActivityCountForWeekdays() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-05";

        final int actual = service.getRequiredActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(15);
    }

    @Test
    public void getRequiredActivityCountForWeekends() {
        String startDate = "2021-02-06";
        String endDate = "2021-02-07";

        final int actual = service.getRequiredActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(18);
    }

    @Test
    public void getSpecialActivityInRequiredPlanTimeCount() {
        final int actual = service.getSpecialActivityInRequiredPlanTimeCount(mockItems);

        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void getSpecialActivityNotInRequiredPlanTimeCount() {
        final int actual = service.getSpecialActivityNotInRequiredPlanTimeCount(mockItems);

        assertThat(actual).isEqualTo(4);
    }

    @Test
    public void getTotalDaysForInvalidRange() {
        String startDate = "2021-02-01";
        String endDate = "2021-01-31";

        try {
            service.getTotalDays(startDate, endDate);
            Assert.fail();
        } catch (Exception e) {
            assertThat(e instanceof ResponseStatusException).isEqualTo(true);
        }
    }

    @Test
    public void getWorshipActivityCountNoWorshipDays() {
        String startDate = "2021-02-03";
        String endDate = "2021-02-05";

        final Integer actual = service.getWorshipActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void getWorshipActivityCountThirdDay() {
        String startDate = "2021-02-01";
        String endDate = "2021-02-03";

        final Integer actual = service.getWorshipActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void getWorshipActivityCountSabbathDay() {
        String startDate = "2021-02-06";
        String endDate = "2021-02-07";

        final Integer actual = service.getWorshipActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    public void getWorshipActivityCountFeb2021() {
        String startDate = "2021-02-02";
        String endDate = "2021-02-27";

        final Integer actual = service.getWorshipActivityCount(startDate, endDate);

        assertThat(actual).isEqualTo(16);
    }

    @Test
    public void isRequiredTimeOnWeekdayEvening() {
        String date = "2021-02-04";
        String timeOfDay = "E";

        final Boolean actual = service.isRequiredTime(date, timeOfDay);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void isRequiredTimeOnWeekdayNotEvening() {
        String date = "2021-02-05";
        String timeOfDay = "A";

        final Boolean actual = service.isRequiredTime(date, timeOfDay);

        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void isRequiredTimeOnWeekend() {
        String date = "2021-02-06";
        String timeOfDay = "M";

        final Boolean actual = service.isRequiredTime(date, timeOfDay);

        assertThat(actual).isEqualTo(true);
    }
}
