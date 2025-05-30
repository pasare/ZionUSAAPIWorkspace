package org.zionusa.admin.service.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.activities.ChurchActivityLogDao;
import org.zionusa.admin.dao.activities.ChurchActivityLogViewDao;
import org.zionusa.admin.domain.activities.ChurchActivityLog;
import org.zionusa.admin.domain.activities.ChurchActivityLogView;
import org.zionusa.admin.domain.activities.SpecialActivityView;
import org.zionusa.admin.service.SpecialActivityViewService;
import org.zionusa.base.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChurchActivityLogService extends BaseService<ChurchActivityLog, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(ChurchActivityLogService.class);
    private final ChurchActivityLogViewDao viewDao;
    private final SpecialActivityViewService specialActivityViewService;

    @Autowired
    public ChurchActivityLogService(ChurchActivityLogDao dao,
                                    ChurchActivityLogViewDao viewDao,
                                    SpecialActivityViewService specialActivityViewService) {
        super(dao, logger, ChurchActivityLog.class);
        this.viewDao = viewDao;
        this.specialActivityViewService = specialActivityViewService;
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        logger.warn("Retrieving all display church activity logs");
        return getAllDisplayFromList(viewDao.findAll(), columns);
    }

    public List<Map<String, Object>> getDailyChurchActivities(Integer churchId, String startDate, String endDate, List<String> columns) {
        logger.warn("Retrieving display church activity logs for id " + churchId + " from " + startDate + " to " + endDate);
        List<ChurchActivityLogView> views = viewDao.findAllByPlanChurchIdAndPlanDateIsBetween(churchId, startDate, endDate);

        List<ChurchActivityLogView> specialActivityViews = specialActivityViewService.getWorshipActivities(startDate, endDate)
            .stream()
            .map(v -> fromSpecialDayView(v, true))
            .collect(Collectors.toList());

        List<ChurchActivityLogView> specialDays = specialActivityViewService.findAllByDateBetween(startDate, endDate)
            .stream()
            .map(v -> fromSpecialDayView(v, false))
            .collect(Collectors.toList());

        views.addAll(specialActivityViews);
        views.addAll(specialDays);

        return getAllDisplayFromList(views, columns);
    }

    private ChurchActivityLogView fromSpecialDayView(SpecialActivityView specialActivityView, Boolean isWorship) {
        ChurchActivityLogView churchActivityLogView = new ChurchActivityLogView();
        churchActivityLogView.setActivityId(specialActivityView.getId());
        churchActivityLogView.setActivityName(specialActivityView.getName());
        churchActivityLogView.setCategoryBackgroundColor(specialActivityView.getCategoryBackgroundColor());
        churchActivityLogView.setCategoryIconUrl(specialActivityView.getCategoryIconUrl());
        churchActivityLogView.setCategoryTextColor(specialActivityView.getCategoryTextColor());
        churchActivityLogView.setCategoryName(specialActivityView.getCategoryName());
        churchActivityLogView.setCategoryId(specialActivityView.getCategoryId());
        churchActivityLogView.setId(specialActivityView.getId());
        // Worship goes in the middle slot = 2
        churchActivityLogView.setOrderId(isWorship && specialActivityView.getOrderId() == null ? 2 :
            (specialActivityView.getOrderId()));
        // participantCount
        churchActivityLogView.setPlanId(0);
        churchActivityLogView.setTimeOfDay(specialActivityView.getTimeOfDay());
        churchActivityLogView.setReadOnly(specialActivityView.getReadOnly());
        churchActivityLogView.setWeight(specialActivityView.getWeight());
        churchActivityLogView.setPlanDate(specialActivityView.getDate());
        churchActivityLogView.setReadOnly(true);
        return churchActivityLogView;
    }
}
