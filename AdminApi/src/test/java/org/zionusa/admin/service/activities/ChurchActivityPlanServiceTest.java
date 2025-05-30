package org.zionusa.admin.service.activities;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.zionusa.admin.dao.activities.ActivityCategoryDao;
import org.zionusa.admin.dao.activities.ChurchActivityReportViewDao;
import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.admin.domain.activities.ActivityCategoryReport;
import org.zionusa.admin.domain.activities.ChurchActivityReportView;
import org.zionusa.admin.service.SpecialActivityViewService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ChurchActivityPlanServiceTest {
    @Spy()
    @InjectMocks()
    private ChurchActivityPlanService service;

    private List<ChurchActivityReportView> mockItems;

    @Mock
    private ActivityCategoryDao activityCategoryDao;

    @Mock
    private ChurchActivityReportViewDao dao;

    @Mock
    private SpecialActivityViewService specialActivityViewService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        final ObjectMapper mapper = new ObjectMapper();

        final String mockItemsPath = "src/test/resources/church-activity-report-views.json";

        final byte[] data = Files.readAllBytes(Paths.get(mockItemsPath));
        mockItems = mapper.readValue(data, new TypeReference<List<ChurchActivityReportView>>() {
        });

        when(dao.findAll()).thenReturn(mockItems);
        when(activityCategoryDao.findByAbbreviation(anyString())).thenReturn(Optional.of(new ActivityCategory()));
    }

    @Test
    public void getActivityCategoryReportFromView() {
        ChurchActivityReportView view = new ChurchActivityReportView();
        view.setCategoryAbbreviation("CategoryAbbreviation");
        view.setCategoryBackgroundColor("#FEDCBA");
        view.setCategoryId(1234);
        view.setCategoryName("CategoryName");
        view.setCategoryIconUrl("http://category.icon.url");
        view.setCategoryTextColor("#123456");

        final ActivityCategoryReport actual = service.getActivityCategoryReportFromView(view);

        assertThat(actual.getAbbreviation()).isEqualTo(view.getCategoryAbbreviation());
        assertThat(actual.getBackgroundColor()).isEqualTo(view.getCategoryBackgroundColor());
        assertThat(actual.getCount()).isEqualTo(0);
        assertThat(actual.getId()).isEqualTo(view.getCategoryId());
        assertThat(actual.getIconUrl()).isEqualTo(view.getCategoryIconUrl());
        assertThat(actual.getName()).isEqualTo(view.getCategoryName());
        assertThat(actual.getParticipantCount()).isEqualTo(0);
        assertThat(actual.getTextColor()).isEqualTo(view.getCategoryTextColor());
    }

//    @Test
//    public void sumChurchActivityReportWithNoViews() {
//        ChurchActivityReport report = new ChurchActivityReport();
//        report.setMaxActivityCount(63);
//        report.setRequiredActivityCount(33);
//        report.setSpecialActivityCount(0);
//        report.setTotalDays(7);
//        report.setWorshipCount(4);
//
//        List<ChurchActivityReportView> views = new ArrayList<>();
//
//        Integer categoryId = 3;
//
//        final ChurchActivityReport actual = service.sumChurchActivityReport(report, views, categoryId);
//
//        assertThat(actual.getCategories().size()).isEqualTo(1);
//        assertThat(actual.getCategories().get(0).getAbbreviation()).isEqualTo("None");
//        assertThat(actual.getCategories().get(0).getAvgParticipationCount()).isEqualTo(0.0);
//        assertThat(actual.getCategories().get(0).getCount()).isEqualTo(33);
//        assertThat(actual.getCategories().get(0).getName()).isEqualTo("None");
//        assertThat(actual.getCategories().get(0).getPercentage()).isEqualTo("100%");
//        assertThat(actual.getCategories().get(0).getParticipantCount()).isEqualTo(0);
//    }

//    @Test
//    public void sumChurchActivityReportWithAllNonRequiredActivities() {
//        // Normal week without any special activities
//        final ChurchActivityReport report = new ChurchActivityReport();
//        report.setMaxActivityCount(63);
//        report.setRequiredActivityCount(33);
//        report.setSpecialActivityCount(0);
//        report.setTotalDays(7);
//        report.setWorshipCount(4);
//
//        final List<ChurchActivityReportView> views = new ArrayList<>();
//
//        ChurchActivityReportView view1 = new ChurchActivityReportView();
//        view1.setActivityCount(3);
//        view1.setActivityTimeOfDay("M");
//        view1.setActivityParticipantCount(6);
//        view1.setCategoryId(1);
//        view1.setDate("2021-02-01");
//        views.add(view1);
//
//        Integer categoryId = 1;
//
//        ChurchActivityReportView view2 = new ChurchActivityReportView();
//        view2.setActivityCount(1);
//        view2.setActivityTimeOfDay("A");
//        view2.setActivityParticipantCount(2);
//        view2.setCategoryId(categoryId);
//        view2.setDate("2021-02-01");
//        views.add(view2);
//
//        when(specialActivityViewService.isRequiredTime(anyString(), anyString())).thenReturn(false);
//
//        final ChurchActivityReport actual = service.sumChurchActivityReport(report, views, categoryId);
//
//        assertThat(actual.getCategories().size()).isEqualTo(2);
//        // No activity
//        assertThat(actual.getCategories().get(0).getAbbreviation()).isEqualTo("None");
//        assertThat(actual.getCategories().get(0).getAvgParticipationCount()).isEqualTo(0.0);
//        assertThat(actual.getCategories().get(0).getCount()).isEqualTo(33);
//        assertThat(actual.getCategories().get(0).getName()).isEqualTo("None");
//        assertThat(actual.getCategories().get(0).getPercentage()).isEqualTo("89%");
//        assertThat(actual.getCategories().get(0).getParticipantCount()).isEqualTo(0);
//        // Category
//        assertThat(actual.getCategories().get(1).getAvgParticipationCount()).isEqualTo(2.0);
//        assertThat(actual.getCategories().get(1).getCount()).isEqualTo(4);
//        assertThat(actual.getCategories().get(1).getPercentage()).isEqualTo("11%");
//        assertThat(actual.getCategories().get(1).getParticipantCount()).isEqualTo(8);
//    }

//    @Test
//    public void sumChurchActivityReportWithAllRequiredActivities() {
//        // Normal week without any special activities
//        final ChurchActivityReport report = new ChurchActivityReport();
//        report.setMaxActivityCount(63);
//        report.setRequiredActivityCount(33);
//        report.setSpecialActivityCount(0);
//        report.setTotalDays(7);
//        report.setWorshipCount(4);
//
//        final List<ChurchActivityReportView> views = new ArrayList<>();
//
//        Integer categoryId = 1;
//
//        ChurchActivityReportView view1 = new ChurchActivityReportView();
//        view1.setActivityCount(3);
//        view1.setActivityTimeOfDay("E");
//        view1.setActivityParticipantCount(6);
//        view1.setCategoryId(categoryId);
//        view1.setDate("2021-02-01");
//        views.add(view1);
//
//        ChurchActivityReportView view2 = new ChurchActivityReportView();
//        view2.setActivityCount(1);
//        view2.setActivityTimeOfDay("E");
//        view2.setActivityParticipantCount(2);
//        view2.setCategoryId(categoryId);
//        view2.setDate("2021-02-01");
//        views.add(view2);
//
//        when(specialActivityViewService.isRequiredTime(anyString(), anyString())).thenReturn(true);
//
//        final ChurchActivityReport actual = service.sumChurchActivityReport(report, views, categoryId);
//
//        assertThat(actual.getCategories().size()).isEqualTo(2);
//        // No activity
//        assertThat(actual.getCategories().get(0).getAbbreviation()).isEqualTo("None");
//        assertThat(actual.getCategories().get(0).getAvgParticipationCount()).isEqualTo(0.0);
//        assertThat(actual.getCategories().get(0).getCount()).isEqualTo(29);
//        assertThat(actual.getCategories().get(0).getName()).isEqualTo("None");
//        assertThat(actual.getCategories().get(0).getPercentage()).isEqualTo("88%");
//        assertThat(actual.getCategories().get(0).getParticipantCount()).isEqualTo(0);
//        // Category
//        assertThat(actual.getCategories().get(1).getAvgParticipationCount()).isEqualTo(2.0);
//        assertThat(actual.getCategories().get(1).getCount()).isEqualTo(4);
//        assertThat(actual.getCategories().get(1).getPercentage()).isEqualTo("12%");
//        assertThat(actual.getCategories().get(1).getParticipantCount()).isEqualTo(8);
//    }
}
