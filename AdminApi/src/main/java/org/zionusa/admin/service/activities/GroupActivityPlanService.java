package org.zionusa.admin.service.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.activities.ActivityCategoryDao;
import org.zionusa.admin.dao.activities.GroupActivityPlanDao;
import org.zionusa.admin.dao.activities.GroupActivityReportViewDao;
import org.zionusa.admin.domain.activities.*;
import org.zionusa.admin.service.SpecialActivityViewService;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupActivityPlanService extends BaseService<GroupActivityPlan, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupActivityPlanService.class);
    private final ActivityCategoryDao activityCategoryDao;
    private final GroupActivityPlanDao dao;
    private final GroupActivityReportViewDao reportDao;
    private final SpecialActivityViewService specialActivityViewService;

    @Autowired
    public GroupActivityPlanService(ActivityCategoryDao activityCategoryDao,
                                    GroupActivityPlanDao dao,
                                    GroupActivityReportViewDao reportDao,
                                    SpecialActivityViewService specialActivityViewService) {
        super(dao, logger, GroupActivityPlan.class);
        this.activityCategoryDao = activityCategoryDao;
        this.dao = dao;
        this.reportDao = reportDao;
        this.specialActivityViewService = specialActivityViewService;
    }

    public GroupActivityReport getGroupPlansReport(Integer groupId, String startDate, String endDate) throws NotFoundException {
        logger.warn("Retrieving group plans report for id {} from {} to {}", groupId, startDate, endDate);
        List<GroupActivityReportView> views = reportDao.findAllByGroupIdAndDateIsBetween(groupId, startDate, endDate);

        if (views.size() == 0) {
            throw new NotFoundException();
        }

        // ---------------------------
        // Third Days and Sabbath Days
        // ---------------------------

        final int maxActivityCount = specialActivityViewService.getMaxActivityCount(startDate, endDate);
        final int requiredActivityCount = specialActivityViewService.getRequiredActivityCount(startDate, endDate);
        final int worshipCount = specialActivityViewService.getWorshipActivityCount(startDate, endDate);

        // ------------
        // Special Days
        // ------------

        final List<SpecialActivityView> specialActivities = specialActivityViewService.findAllByDateBetween(startDate, endDate);
        final int specialActivityCount = specialActivities.size();
        final int specialRequiredActivityCount = specialActivityViewService.getSpecialActivityInRequiredPlanTimeCount(specialActivities);

        final GroupActivityReport report = new GroupActivityReport();
        report.setTotalDays(specialActivityViewService.getTotalDays(startDate, endDate));
        report.setChurchId(views.get(0).getChurchId());
        report.setId(views.get(0).getGroupId());
        report.setMaxActivityCount(maxActivityCount - specialActivityCount - worshipCount);
        report.setName(views.get(0).getGroupName());
        report.setRequiredActivityCount(requiredActivityCount - specialRequiredActivityCount - worshipCount);
        report.setSpecialActivityCount(specialActivityCount);
        report.setWorshipCount(worshipCount);

        return sumGroupActivityReport(report, views, null);
    }

    public List<GroupActivityReport> getGroupPlanReportByCategory(Integer categoryId,
                                                                    Integer churchId,
                                                                    String startDate,
                                                                    String endDate) throws NotFoundException {
        logger.warn("Retrieving group plans report for category id " + categoryId + " from church " + churchId + " from " + startDate + " to " + endDate);
        List<GroupActivityReportView> views = reportDao.findAllByChurchIdAndDateIsBetween(
                churchId,
                startDate,
                endDate
        );

        // ---------------------------
        // Third Days and Sabbath Days
        // ---------------------------

        final int maxActivityCount = specialActivityViewService.getMaxActivityCount(startDate, endDate);
        final int requiredActivityCount = specialActivityViewService.getRequiredActivityCount(startDate, endDate);
        final int worshipCount = specialActivityViewService.getWorshipActivityCount(startDate, endDate);

        // ------------
        // Special Days
        // ------------

        final List<SpecialActivityView> specialActivities = specialActivityViewService.findAllByDateBetween(startDate, endDate);
        final int specialActivityCount = specialActivities.size();
        final int specialRequiredActivityCount = specialActivityViewService.getSpecialActivityInRequiredPlanTimeCount(specialActivities);

        // Organize the plans by group
        Map<Integer, List<GroupActivityReportView>> groupActivityReportViewMap = new HashMap<>();
        for (GroupActivityReportView view : views) {
            groupActivityReportViewMap.putIfAbsent(view.getGroupId(), new ArrayList<>());
            groupActivityReportViewMap.get(view.getGroupId()).add(view);
        }

        return groupActivityReportViewMap.values().stream().map(viewsByGroup -> {
            final GroupActivityReport report = new GroupActivityReport();
            report.setTotalDays(specialActivityViewService.getTotalDays(startDate, endDate));
            report.setId(viewsByGroup.get(0).getGroupId());
            report.setChurchId(viewsByGroup.get(0).getChurchId());
            report.setMaxActivityCount(maxActivityCount - specialActivityCount - worshipCount);
            report.setName(viewsByGroup.get(0).getGroupName());
            report.setRequiredActivityCount(requiredActivityCount - specialRequiredActivityCount - worshipCount);
            report.setSpecialActivityCount(specialActivityCount);
            report.setWorshipCount(worshipCount);


            return sumGroupActivityReport(report, viewsByGroup, categoryId);
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getDailyGroupPlans(Integer groupId, String startDate, String endDate, List<String> columns) {
        logger.warn("Retrieving display group plans for id {} from {} to {}", groupId, startDate, endDate);
        return getAllDisplayFromList(dao.findAllByGroupIdAndDateIsBetween(groupId, startDate, endDate), columns);
    }

    private GroupActivityReport sumGroupActivityReport(GroupActivityReport report,
                                                       List<GroupActivityReportView> views, Integer categoryId) {
        HashMap<Integer, ActivityCategoryReport> categoryReportHashMap = new HashMap<>();
        Integer totalCount = 0;
        Integer totalRequiredCount = 0;

        for (GroupActivityReportView view : views) {
            if (!categoryReportHashMap.containsKey(view.getCategoryId())) {
                categoryReportHashMap.put(view.getCategoryId(), getActivityCategoryReportFromView(view));
            }

            // Increase the count
            Integer previousCount = categoryReportHashMap.get(view.getCategoryId()).getCount();
            totalCount += view.getActivityCount();
            categoryReportHashMap.get(view.getCategoryId()).setCount(view.getActivityCount() + previousCount);

            // Add required time count, if set
            if (specialActivityViewService.isRequiredTime(view.getDate(), view.getActivityTimeOfDay())) {
                totalRequiredCount += view.getActivityCount();
            }
            Integer previousParticipantCount = categoryReportHashMap.get(view.getCategoryId()).getParticipantCount();
            categoryReportHashMap.get(view.getCategoryId()).setParticipantCount(view.getActivityParticipantCount() + previousParticipantCount);
        }

        // Add empty activity spaces
        if (report.getRequiredActivityCount() > totalRequiredCount) {
            final Optional<ActivityCategory> noneCategoryOptional = activityCategoryDao.findByAbbreviation("None");
            final ActivityCategory noneCategory = noneCategoryOptional.orElseGet(ActivityCategory::new);
            final int missingActivityCount = report.getRequiredActivityCount() - totalRequiredCount;
            final ActivityCategoryReport missingActivities = new ActivityCategoryReport();

            // Add all metadata
            missingActivities.setAbbreviation("None");
            missingActivities.setBackgroundColor(noneCategory.getBackgroundColor());
            missingActivities.setId(-1);
            missingActivities.setIconUrl(noneCategory.getIconUrl());
            missingActivities.setName("None");
            missingActivities.setTextColor(noneCategory.getTextColor());

            // Set counts
            missingActivities.setCount(missingActivityCount);
            missingActivities.setParticipantCount(0);

            categoryReportHashMap.put(missingActivities.getId(), missingActivities);

            totalCount += missingActivityCount;
        }

        final double finalTotalCount = (double) totalCount;
        report.setCategories(new ArrayList<>(categoryReportHashMap.values())
                .stream()
                .peek(activityCategoryReport -> {
                    activityCategoryReport.setPercentage(Math.round((activityCategoryReport.getCount() * 100) / finalTotalCount) + "%");
                    double avgParticipationCount = (double)Math.round(( activityCategoryReport.getParticipantCount() * 10) / (double) activityCategoryReport.getCount()) / 10;
                    activityCategoryReport.setAvgParticipationCount(avgParticipationCount);
                })
            .filter(activityCategoryReport -> {
                if(categoryId == null) {
                    return true;
                }
                return activityCategoryReport.getId().equals(categoryId); })
                .sorted(this::sortActivityCategoryReport)
                .collect(Collectors.toList()));

        return report;
    }

    public int sortActivityCategoryReport(ActivityCategoryReport a, ActivityCategoryReport b) {
        if (a.getId().equals(b.getId())) {
            return 0;
        }
        return a.getId() < b.getId() ? -1 : 1;
    }

    public ActivityCategoryReport getActivityCategoryReportFromView(GroupActivityReportView view) {
        final ActivityCategoryReport activityCategoryReport = new ActivityCategoryReport();

        // Add all metadata
        activityCategoryReport.setAbbreviation(view.getCategoryAbbreviation());
        activityCategoryReport.setBackgroundColor(view.getCategoryBackgroundColor());
        activityCategoryReport.setId(view.getCategoryId());
        activityCategoryReport.setIconUrl(view.getCategoryIconUrl());
        activityCategoryReport.setName(view.getCategoryName());
        activityCategoryReport.setTextColor(view.getActivityTextColor());

        // Set count to zero
        activityCategoryReport.setCount(0);
        activityCategoryReport.setParticipantCount(0);

        return activityCategoryReport;
    }
}
