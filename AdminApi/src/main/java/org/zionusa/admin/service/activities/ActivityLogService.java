package org.zionusa.admin.service.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.ChallengeLogDao;
import org.zionusa.admin.dao.GoalLogDao;
import org.zionusa.admin.dao.GroupChallengePointsDao;
import org.zionusa.admin.dao.UserChallengePointsDao;
import org.zionusa.admin.dao.activities.ActivityLogDao;
import org.zionusa.admin.domain.CalendarEvent;
import org.zionusa.admin.domain.ChallengeLog;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.admin.domain.activities.ActivityLog;
import org.zionusa.admin.domain.activities.ActivityReportGroup;
import org.zionusa.admin.domain.activities.ActivityReportMember;
import org.zionusa.admin.service.RestService;
import org.zionusa.base.domain.Group;
import org.zionusa.base.domain.Member;
import org.zionusa.base.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityLogService extends BaseService<ActivityLog, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogService.class);

    private final ActivityLogDao dao;
    private final ChallengeLogDao challengeLogDao;
    private final UserChallengePointsDao userChallengePointsDao;
    private final GroupChallengePointsDao groupChallengePointsDao;
    private final GoalLogDao goalLogDao;

    private final RestService restService;

    @Autowired
    public ActivityLogService(ActivityLogDao dao, ChallengeLogDao challengeLogDao, UserChallengePointsDao userChallengePointsDao, GroupChallengePointsDao groupChallengePointsDao, GoalLogDao goalLogDao, RestService restService) {
        super(dao, logger, ActivityLog.class);
        this.dao = dao;
        this.challengeLogDao = challengeLogDao;
        this.userChallengePointsDao = userChallengePointsDao;
        this.groupChallengePointsDao = groupChallengePointsDao;
        this.goalLogDao = goalLogDao;
        this.restService = restService;
    }

    public List<ActivityLog> getActivityLogsByDateBetween(String startDate, String endDate) {
        return dao.getActivityLogsByDateBetween(startDate, endDate);
    }

    public List<ActivityLog> getActivityLogsByUserIdAndDateBetween(Integer userId, String startDate, String endDate) {
        logger.debug(" Getting activity logs for user {} from {} to {}", userId, startDate, endDate);
        return dao.getActivityLogsByUserIdAndDateBetweenOrderByDateDescIdDesc(userId, startDate, endDate);
    }

    public List<ActivityLog> getActivityLogsByUserIdAndActivityIdAndDateBetween(Integer userId, Integer activityId, String startDate, String endDate) {
        logger.debug("Getting activity logs for user {} from activity {} from {} to {}", userId, activityId, startDate, endDate);
        return dao.getActivityLogsByUserIdAndActivityIdAndDateBetween(userId, activityId, startDate, endDate);
    }

    public List<ActivityLog> getActivityLogsByUserId(Integer userId) {
        return dao.getActivityLogsByUserId(userId);
    }

    public List<ActivityLog> getRecentActivityLogByUserId(Integer userId, String startDate, String endDate) {
        return dao.getActivityLogsByUserIdAndDateBetweenOrderByDateDescIdDesc(userId, startDate, endDate);
    }

    public List<ActivityLog> getActivityLogsByChurchId(Integer churchId) {
        return dao.getActivityLogsByChurchId(churchId);
    }

    public List<ActivityLog> getActivityLogsByChurchIdAndDateBetween(Integer churchId, String startDate, String endDate) {
        logger.debug("Getting activity logs for church {} from {} to {}", churchId, startDate, endDate);
        return dao.getActivityLogsByChurchIdAndDateBetween(churchId, startDate, endDate);
    }

    public List<ActivityLog> getActivityLogsByGroupId(Integer groupId) {
        return dao.getActivityLogsByGroupId(groupId);
    }

    public List<ActivityLog> getActivityLogsByGroupIdAndDateBetween(Integer groupId, String startDate, String endDate) {
        logger.debug("Getting activity logs for group {} from {} to {}", groupId, startDate, endDate);
        return dao.getActivityLogsByGroupIdAndDateBetween(groupId, startDate, endDate);
    }

    public List<ActivityLog> getActivityLogsByTeamId(Integer teamId) {
        return dao.getActivityLogsByTeamId(teamId);
    }

    public List<ActivityLog> getActivityLogsByTeamIdAndDateBetween(Integer teamId, String startDate, String endDate) {
        logger.debug("Getting activity logs for team {} from {} to {}", teamId, startDate, endDate);
        return dao.getActivityLogsByTeamIdAndDateBetween(teamId, startDate, endDate);
    }

    public Map<String, List<CalendarEvent>> getActivityLogMap(Integer userId, String startDate, String endDate) {
        Map<String, List<CalendarEvent>> eventMap = new HashMap<>();

        for (LocalDate date = LocalDate.parse(startDate); date.isBefore(LocalDate.parse(endDate)); date = date.plusDays(1)) {
            String formattedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
            List<CalendarEvent> events = eventMap.get(date.toString());

            List<ActivityLog> activityLogs = getActivityLogsByUserIdAndDateBetween(userId, formattedDate, formattedDate);

            if (events == null) events = new ArrayList<>();

            for (ActivityLog activityLog : activityLogs) {
                events.add(new CalendarEvent(activityLog.getName(), true));
            }

            eventMap.put(formattedDate, events);
        }

        return eventMap;
    }

    public ActivityReportMember getMemberActivityReport(Integer memberId, String startDate, String endDate) {
        int month = LocalDate.parse(startDate).getMonthValue();
        int year = LocalDate.parse(startDate).getYear();
        ActivityReportMember activityReportMember = new ActivityReportMember();
        activityReportMember.setId(memberId);
        activityReportMember.setActivitiesCount(dao.countActivityLogsByUserIdAndDateIsBetween(memberId, startDate, endDate));
        activityReportMember.setChallenges(goalLogDao.getGoalLogsByUserIdAndStartDateBetween(memberId, startDate, endDate));
        activityReportMember.setUserMonthlyChallengePoints(userChallengePointsDao.getUserMonthlyChallengePointsByUserIdAndMonthAndYear(memberId, month, year));
        activityReportMember.setReportGenerationDate(LocalDateTime.now().toString());
        return activityReportMember;
    }

    @Cacheable(value = "rankings-reports-cache", key = "#startDate.concat('-ranking-').concat(#endDate)")
    public List<ActivityReportMember> getMemberRankings(HttpServletRequest request, String startDate, String endDate) {
        int month = LocalDate.parse(startDate).getMonthValue();
        int year = LocalDate.parse(startDate).getYear();
        List<ActivityReportMember> reports = new ArrayList<>();
        for (Member member : restService.getAllDisplayMembers(request)) {
            ActivityReportMember activityReportMember = new ActivityReportMember();
            activityReportMember.setId(member.getId());
            activityReportMember.setDisplayName(member.getDisplayName());
            activityReportMember.setProfilePictureUrl(member.getPictureUrl());
            activityReportMember.setActivitiesCount(dao.countActivityLogsByUserIdAndDateIsBetween(member.getId(), startDate, endDate));
            activityReportMember.setChallengesTakenCount(challengeLogDao.countChallengeLogsByUserIdAndDateIsBetween(member.getId(), startDate, endDate));
            activityReportMember.setChallengesCompletedCount(challengeLogDao.countChallengeLogsByUserIdAndDateIsBetweenAndCompletedTrue(member.getId(), startDate, endDate));
            activityReportMember.setUserMonthlyChallengePoints(userChallengePointsDao.getUserMonthlyChallengePointsByUserIdAndMonthAndYear(member.getId(), month, year));
            activityReportMember.setReportGenerationDate(LocalDateTime.now().toString());
            reports.add(activityReportMember);
        }
        return reports.stream().sorted((this::sortListByPoints)).collect(Collectors.toList());
    }

    // this one does not load user challenges and activities, just the counts
    @Cacheable(value = "group-reports-cache", key = "#startDate.concat('-group-').concat(#endDate).concat(#groupId)")
    public List<ActivityReportMember> getGroupActivityReport(HttpServletRequest request, Integer groupId, String startDate, String endDate) {
        int month = LocalDate.parse(startDate).getMonthValue();
        int year = LocalDate.parse(startDate).getYear();
        List<ActivityReportMember> reports = new ArrayList<>();
        for (Member member : restService.getGroupMembers(request, groupId)) {
            ActivityReportMember activityReportMember = new ActivityReportMember();
            activityReportMember.setId(member.getId());
            activityReportMember.setDisplayName(member.getDisplayName());
            activityReportMember.setProfilePictureUrl(member.getPictureUrl());
            activityReportMember.setActivitiesCount(dao.countActivityLogsByUserIdAndDateIsBetween(member.getId(), startDate, endDate));
            activityReportMember.setChallengesTakenCount(goalLogDao.countGoalLogsByUserIdAndStartDateIsBetween(member.getId(), startDate, endDate));
            activityReportMember.setChallengesCompletedCount(goalLogDao.countGoalLogsByUserIdAndStartDateIsBetweenAndCompletedTrue(member.getId(), startDate, endDate));
            activityReportMember.setUserMonthlyChallengePoints(userChallengePointsDao.getUserMonthlyChallengePointsByUserIdAndMonthAndYear(member.getId(), month, year));
            activityReportMember.setReportGenerationDate(LocalDateTime.now().toString());
            reports.add(activityReportMember);
        }

        return reports.stream().sorted((this::sortListByPoints)).collect(Collectors.toList());
    }

    @Cacheable(value = "church-reports-cache", key = "#startDate.concat('-church-').concat(#endDate).concat(#churchId)")
    public List<ActivityReportGroup> getChurchActivityReport(HttpServletRequest request, Integer churchId, String startDate, String endDate) {
        int month = LocalDate.parse(startDate).getMonthValue();
        int year = LocalDate.parse(startDate).getYear();
        List<ActivityReportGroup> reports = new ArrayList<>();
        for (Group group : restService.getChurchGroups(request, churchId)) {
            ActivityReportGroup activityReportGroup = new ActivityReportGroup();
            activityReportGroup.setId(group.getId());
            activityReportGroup.setDisplayName(group.getName());
            activityReportGroup.setActivitiesCount(dao.countActivityLogsByGroupIdAndDateIsBetween(group.getId(), startDate, endDate));
            activityReportGroup.setChallengesTakenCount(0);
            activityReportGroup.setChallengesCompletedCount(0);
            activityReportGroup.setGroupMonthlyChallengePoints(groupChallengePointsDao.getUserMonthlyChallengePointsByGroupIdAndMonthAndYear(group.getId(), month, year));
            activityReportGroup.setReportGenerationDate(LocalDateTime.now().toString());
            reports.add(activityReportGroup);
        }

        return reports;
    }

    @Override
    public ActivityLog save(ActivityLog activityLog) {
        ActivityLog savedActivityLog = super.save(activityLog);

        //update challenge log completion status
        if (activityLog.getChallengeLogId() != null) {
            determineChallengeCompletionStatus(activityLog.getChallengeLogId());
        }

        return savedActivityLog;
    }

    private void determineChallengeCompletionStatus(Integer challengeLogId) {
        Optional<ChallengeLog> challengeLogOptional = challengeLogDao.findById(challengeLogId);

        int challengeActivitiesCompleted = 0;

        if (challengeLogOptional.isPresent() && !challengeLogOptional.get().isCompleted()) {
            ChallengeLog challengeLog = challengeLogOptional.get();
            List<Activity> activities = challengeLog.getChallenge().getActivities();

            for (ActivityLog activityLog : challengeLog.getCompletedActivities()) {
                if (activities.stream().anyMatch((activity) -> activity.getId().equals(activityLog.getActivityId()))) {
                    challengeActivitiesCompleted++;
                }
            }

            if (challengeActivitiesCompleted >= activities.size()) {
                logger.warn("determined that challenge is complete");
                challengeLog.setCompleted(true);
                challengeLogDao.save(challengeLog);
            }
            logger.warn("determined challenge is not complete yet {}", challengeActivitiesCompleted);
        }
    }

    @CacheEvict(cacheNames = {"church-reports-cache", "group-reports-cache", "rankings-reports-cache"}, allEntries = true)
    public void clearCaches() {
        logger.warn("Clearing all reports caches");
    }

    int sortListByPoints(ActivityReportMember report1, ActivityReportMember report2) {
        if (report1.getUserMonthlyChallengePoints() == null && report2.getUserMonthlyChallengePoints() == null)
            return 0;
        if (report1.getUserMonthlyChallengePoints() == null) return 1;
        if (report2.getUserMonthlyChallengePoints() == null) return -1;
        return Double.compare(report2.getUserMonthlyChallengePoints().getPoints(), report1.getUserMonthlyChallengePoints().getPoints());
    }
}
