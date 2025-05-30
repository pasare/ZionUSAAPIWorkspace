package org.zionusa.admin.service.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.activities.GroupActivityLogDao;
import org.zionusa.admin.dao.activities.GroupActivityLogViewDao;
import org.zionusa.admin.domain.activities.GroupActivityLog;
import org.zionusa.admin.domain.activities.GroupActivityLogView;
import org.zionusa.admin.domain.activities.SpecialActivityView;
import org.zionusa.admin.service.SpecialActivityViewService;
import org.zionusa.base.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupActivityLogService extends BaseService<GroupActivityLog, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GroupActivityLogService.class);
    private final GroupActivityLogViewDao viewDao;
    private final SpecialActivityViewService specialActivityViewService;

    @Autowired
    public GroupActivityLogService(GroupActivityLogDao dao,
                                   GroupActivityLogViewDao viewDao,
                                   SpecialActivityViewService specialActivityViewService) {
        super(dao, logger, GroupActivityLog.class);
        this.viewDao = viewDao;
        this.specialActivityViewService = specialActivityViewService;
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        logger.warn("Retrieving all display group activity logs");
        return getAllDisplayFromList(viewDao.findAll(), columns);
    }

    public List<Map<String, Object>> getDailyGroupActivities(Integer groupId, String startDate, String endDate, List<String> columns) {
        logger.warn("Retrieving display group activity logs for id {} from {} to {}", groupId, startDate, endDate);
        List<GroupActivityLogView> views = viewDao.findAllByPlanGroupIdAndPlanDateIsBetween(groupId, startDate, endDate);

        List<GroupActivityLogView> specialActivityViews = specialActivityViewService.getWorshipActivities(startDate, endDate)
            .stream()
            .map(v -> fromSpecialDayView(v, true))
            .collect(Collectors.toList());

        List<GroupActivityLogView> specialDays = specialActivityViewService.findAllByDateBetween(startDate, endDate)
            .stream()
            .map(v -> fromSpecialDayView(v, false))
            .collect(Collectors.toList());

        views.addAll(specialActivityViews);
        views.addAll(specialDays);

        return getAllDisplayFromList(views, columns);
    }

    private GroupActivityLogView fromSpecialDayView(SpecialActivityView specialActivityView, Boolean isWorship) {
        GroupActivityLogView groupActivityLogView = new GroupActivityLogView();
        groupActivityLogView.setActivityId(specialActivityView.getId());
        groupActivityLogView.setActivityName(specialActivityView.getName());
        groupActivityLogView.setCategoryBackgroundColor(specialActivityView.getCategoryBackgroundColor());
        groupActivityLogView.setCategoryIconUrl(specialActivityView.getCategoryIconUrl());
        groupActivityLogView.setCategoryTextColor(specialActivityView.getCategoryTextColor());
        groupActivityLogView.setCategoryName(specialActivityView.getCategoryName());
        groupActivityLogView.setCategoryId(specialActivityView.getId());
        groupActivityLogView.setId(specialActivityView.getId());
        // Worship goes in the middle slot = 2
        groupActivityLogView.setOrderId(isWorship && specialActivityView.getOrderId() == null ? 2 : (specialActivityView.getOrderId()));
        // participantCount
        groupActivityLogView.setPlanId(0);
        groupActivityLogView.setTimeOfDay(specialActivityView.getTimeOfDay());
        groupActivityLogView.setReadOnly(specialActivityView.getReadOnly());
        groupActivityLogView.setWeight(specialActivityView.getWeight());
        groupActivityLogView.setPlanDate(specialActivityView.getDate());
        groupActivityLogView.setReadOnly(true);
        return groupActivityLogView;
    }
}
