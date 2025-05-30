package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.zionusa.admin.dao.activities.ActivityCategoryDao;
import org.zionusa.admin.dao.activities.SpecialActivityViewDao;
import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.admin.domain.activities.SpecialActivityView;
import org.zionusa.base.service.BaseService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class SpecialActivityViewService extends BaseService<SpecialActivityView, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(SpecialActivityViewService.class);
    private final ActivityCategoryDao activityCategoryDao;
    private final SpecialActivityViewDao dao;

    public SpecialActivityViewService(ActivityCategoryDao activityCategoryDao, SpecialActivityViewDao dao) {
        super(dao, logger, SpecialActivityView.class);
        this.activityCategoryDao = activityCategoryDao;
        this.dao = dao;
    }

    public List<SpecialActivityView> findAllByDateBetween(String startDate, String endDate) {
        return dao.findAllByDateBetween(startDate, endDate);
    }

    public int getMaxActivityCount(String startDate, String endDate) {
        return getTotalDays(startDate, endDate) * 9;
    }

    public int getTotalDays(String startDate, String endDate) throws ResponseStatusException {
        final int days = Math.round(DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate).plus(1, DAYS)));

        if (days > 0) {
            return days;
        }

        final String reason = "Invalid date range from " + startDate + " to " + endDate;
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
    }

    public int getRequiredActivityCount(String startDate, String endDate) {
        final LocalDate start = LocalDate.parse(startDate);
        final LocalDate end = LocalDate.parse(endDate);
        int count = 0;

        for (LocalDate i = start; i.isBefore(end.plus(1, DAYS)); i = i.plus(1, DAYS)) {
            switch (i.getDayOfWeek()) {
                case SUNDAY:
                case SATURDAY:
                    count += 9;
                    break;
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY:
                    count += 3;
                    break;
                default:
                    break;
            }
        }

        return count;
    }

    public int getSpecialActivityNotInRequiredPlanTimeCount(List<SpecialActivityView> specialActivities) {
        int count = 0;

        for (SpecialActivityView specialActivity : specialActivities) {
            if (!isRequiredTime(specialActivity.getDate(), specialActivity.getTimeOfDay())) {
                count += 1;
            }
        }

        return count;
    }

    public int getSpecialActivityInRequiredPlanTimeCount(List<SpecialActivityView> specialActivities) {
        int count = 0;

        for (SpecialActivityView specialActivity : specialActivities) {
            if (isRequiredTime(specialActivity.getDate(), specialActivity.getTimeOfDay())) {
                count += 1;
            }
        }

        return count;
    }

    public List<SpecialActivityView> getWorshipActivities(String startDate, String endDate) {
        final LocalDate start = LocalDate.parse(startDate);
        final LocalDate end = LocalDate.parse(endDate);
        final List<SpecialActivityView> list = new ArrayList<>();

        final Optional<ActivityCategory> activityCategoryOptional = activityCategoryDao.findByAbbreviation("Worship");
        ActivityCategory worshipCategory = activityCategoryOptional.orElseGet(ActivityCategory::new);

        for (LocalDate i = start; i.isBefore(end.plus(1, DAYS)); i = i.plus(1, DAYS)) {
            switch (i.getDayOfWeek()) {
                case TUESDAY:
                    final SpecialActivityView thirdDay = new SpecialActivityView();
                    thirdDay.setCategoryAbbreviation(worshipCategory.getAbbreviation());
                    thirdDay.setCategoryBackgroundColor(worshipCategory.getBackgroundColor());
                    thirdDay.setCategoryIconUrl(worshipCategory.getIconUrl());
                    thirdDay.setCategoryId(worshipCategory.getId());
                    thirdDay.setCategoryName(worshipCategory.getName());
                    thirdDay.setCategoryTextColor(worshipCategory.getTextColor());
                    thirdDay.setDate(i.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    thirdDay.setId(0);
                    thirdDay.setName("Third Day Worship");
                    thirdDay.setOrderId(2);
                    thirdDay.setReadOnly(true);
                    thirdDay.setTimeOfDay("E");
                    thirdDay.setWeight(1);
                    list.add(thirdDay);
                    break;
                case SATURDAY:
                    final SpecialActivityView sabbathMorning = new SpecialActivityView();
                    sabbathMorning.setCategoryAbbreviation(worshipCategory.getAbbreviation());
                    sabbathMorning.setCategoryBackgroundColor(worshipCategory.getBackgroundColor());
                    sabbathMorning.setCategoryIconUrl(worshipCategory.getIconUrl());
                    sabbathMorning.setCategoryId(worshipCategory.getId());
                    sabbathMorning.setCategoryName(worshipCategory.getName());
                    sabbathMorning.setCategoryTextColor(worshipCategory.getTextColor());
                    sabbathMorning.setDate(i.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    sabbathMorning.setId(0);
                    sabbathMorning.setName("Sabbath Worship");
                    sabbathMorning.setOrderId(1);
                    sabbathMorning.setReadOnly(true);
                    sabbathMorning.setTimeOfDay("M");
                    sabbathMorning.setWeight(1);
                    list.add(sabbathMorning);

                    final SpecialActivityView sabbathAfternoon = new SpecialActivityView();
                    sabbathAfternoon.setCategoryAbbreviation(worshipCategory.getAbbreviation());
                    sabbathAfternoon.setCategoryBackgroundColor(worshipCategory.getBackgroundColor());
                    sabbathAfternoon.setCategoryIconUrl(worshipCategory.getIconUrl());
                    sabbathAfternoon.setCategoryId(worshipCategory.getId());
                    sabbathAfternoon.setCategoryName(worshipCategory.getName());
                    sabbathAfternoon.setCategoryTextColor(worshipCategory.getTextColor());
                    sabbathAfternoon.setDate(i.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    sabbathAfternoon.setId(0);
                    sabbathAfternoon.setName("Sabbath Worship");
                    sabbathAfternoon.setOrderId(2);
                    sabbathAfternoon.setReadOnly(true);
                    sabbathAfternoon.setTimeOfDay("A");
                    sabbathAfternoon.setWeight(1);
                    list.add(sabbathAfternoon);

                    final SpecialActivityView sabbathEvening = new SpecialActivityView();
                    sabbathEvening.setCategoryAbbreviation(worshipCategory.getAbbreviation());
                    sabbathEvening.setCategoryBackgroundColor(worshipCategory.getBackgroundColor());
                    sabbathEvening.setCategoryIconUrl(worshipCategory.getIconUrl());
                    sabbathEvening.setCategoryId(worshipCategory.getId());
                    sabbathEvening.setCategoryName(worshipCategory.getName());
                    sabbathEvening.setCategoryTextColor(worshipCategory.getTextColor());
                    sabbathEvening.setDate(i.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    sabbathEvening.setId(0);
                    sabbathEvening.setName("Sabbath Worship");
                    sabbathEvening.setOrderId(2);
                    sabbathEvening.setReadOnly(true);
                    sabbathEvening.setTimeOfDay("E");
                    sabbathEvening.setWeight(1);
                    list.add(sabbathEvening);
                    break;
                default:
                    break;
            }
        }

        return list;
    }

    public Integer getWorshipActivityCount(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        int count = 0;

        for (LocalDate i = start; i.isBefore(end.plus(1, DAYS)); i = i.plus(1, DAYS)) {
            switch (i.getDayOfWeek()) {
                case TUESDAY:
                    count += 1;
                    break;
                case SATURDAY:
                    count += 3;
                    break;
                default:
                    break;
            }
        }

        return count;
    }

    public Boolean isRequiredTime(String date, String timeOfDay) {
        LocalDate specialDate = LocalDate.parse(date);

        switch (specialDate.getDayOfWeek()) {
            case SUNDAY:
            case SATURDAY:
                // Weekends all day are required planning time
                return true;
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                // Weekday evenings are required planning time
                if (timeOfDay.equals("E")) {
                    return true;
                }
                break;
            default:
                break;
        }

        return false;
    }
}
