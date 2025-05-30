package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.domain.Association;
import org.zionusa.base.domain.Church;
import org.zionusa.base.domain.Member;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.biblestudy.dao.*;
import org.zionusa.biblestudy.domain.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PreachingLogService extends BaseService<PreachingLog, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(PreachingLogService.class);
    private final PreachingLogAssociationDao preachingLogAssociationDao;
    private final PreachingLogApaReportWeek1Dao preachingLogApaReportWeek1Dao;
    private final PreachingLogApaReportWeek2Dao preachingLogApaReportWeek2Dao;
    private final PreachingLogApaReportWeek3Dao preachingLogApaReportWeek3Dao;
    private final PreachingLogApaReportWeek4Dao preachingLogApaReportWeek4Dao;
    private final PreachingLogChurchDao preachingLogChurchDao;
    private final PreachingLogRegionDao preachingLogRegionDao;
    private final PreachingLogDao preachingLogDao;
    private final PreachingLogOverseerDao preachingLogOverseerDao;
    private final PreachingLogTotalsDao preachingLogTotalsDao;
    private final PreachingLogUserReportDao preachingLogUserReportDao;
    private final PreachingLogUserShortTermReportDao preachingLogUserShortTermReportDao;
    private final MovementChurchGoalDao movementChurchGoalDao;
    private final ShortTermPreachingViewDao shortTermPreachingViewDao;
    private final TrackerUsageAllDao trackerUsageAllDao;
    private final TrackerUsageChurchDao trackerUsageChurchDao;
    private final RestService restService;

    @Autowired
    public PreachingLogService(PreachingLogAssociationDao preachingLogAssociationDao,
                               PreachingLogApaReportWeek1Dao preachingLogApaReportWeek1Dao,
                               PreachingLogApaReportWeek2Dao preachingLogApaReportWeek2Dao,
                               PreachingLogApaReportWeek3Dao preachingLogApaReportWeek3Dao,
                               PreachingLogApaReportWeek4Dao preachingLogApaReportWeek4Dao,
                               PreachingLogChurchDao preachingLogChurchDao,
                               PreachingLogRegionDao preachingLogRegionDao,
                               PreachingLogDao preachingLogDao,
                               PreachingLogOverseerDao preachingLogOverseerDao,
                               PreachingLogTotalsDao preachingLogTotalsDao,
                               PreachingLogUserReportDao preachingLogUserReportDao,
                               PreachingLogUserShortTermReportDao preachingLogUserShortTermReportDao,
                               MovementChurchGoalDao movementChurchGoalDao,
                               ShortTermPreachingViewDao shortTermPreachingViewDao,
                               TrackerUsageAllDao trackerUsageAllDao,
                               TrackerUsageChurchDao trackerUsageChurchDao,
                               RestService restService) {
        super(preachingLogDao, logger, PreachingLog.class);
        this.preachingLogAssociationDao = preachingLogAssociationDao;
        this.preachingLogApaReportWeek1Dao = preachingLogApaReportWeek1Dao;
        this.preachingLogApaReportWeek2Dao = preachingLogApaReportWeek2Dao;
        this.preachingLogApaReportWeek3Dao = preachingLogApaReportWeek3Dao;
        this.preachingLogApaReportWeek4Dao = preachingLogApaReportWeek4Dao;
        this.preachingLogChurchDao = preachingLogChurchDao;
        this.preachingLogRegionDao = preachingLogRegionDao;
        this.preachingLogDao = preachingLogDao;
        this.preachingLogOverseerDao = preachingLogOverseerDao;
        this.preachingLogTotalsDao = preachingLogTotalsDao;
        this.preachingLogUserReportDao = preachingLogUserReportDao;
        this.preachingLogUserShortTermReportDao = preachingLogUserShortTermReportDao;
        this.movementChurchGoalDao = movementChurchGoalDao;
        this.shortTermPreachingViewDao = shortTermPreachingViewDao;
        this.trackerUsageAllDao = trackerUsageAllDao;
        this.trackerUsageChurchDao = trackerUsageChurchDao;
        this.restService = restService;
    }

    public List<PreachingLogApaReportWeek1> getApaRankingsWeek1Report(String sort) {
        if (sort.equalsIgnoreCase("activity")) {
            return preachingLogApaReportWeek1Dao.findTop300ByOrderByActivityPointsDesc();
        }
        if (sort.equalsIgnoreCase("preaching")) {
            return preachingLogApaReportWeek1Dao.findTop300ByOrderByPreachingPointsDesc();
        }
        if (sort.equalsIgnoreCase("teaching")) {
            return preachingLogApaReportWeek1Dao.findTop300ByOrderByTeachingPointsDesc();
        }
        if (sort.equalsIgnoreCase("total")) {
            return preachingLogApaReportWeek1Dao.findTop300ByOrderByTotalPointsDesc();
        }
        return new ArrayList<>();
    }

    public List<PreachingLogApaReportWeek1> getChurchApaRankingsWeek1Report(Integer churchId, String sort) {
        if (sort.equalsIgnoreCase("activity")) {
            return preachingLogApaReportWeek1Dao.findAllByChurchIdOrderByActivityPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("preaching")) {
            return preachingLogApaReportWeek1Dao.findAllByChurchIdOrderByPreachingPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("teaching")) {
            return preachingLogApaReportWeek1Dao.findAllByChurchIdOrderByTeachingPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("total")) {
            return preachingLogApaReportWeek1Dao.findAllByChurchIdOrderByTotalPointsDesc(churchId);
        }
        return new ArrayList<>();
    }

    public List<PreachingLogApaReportWeek2> getApaRankingsWeek2Report(String sort) {
        if (sort.equalsIgnoreCase("activity")) {
            return preachingLogApaReportWeek2Dao.findTop300ByOrderByActivityPointsDesc();
        }
        if (sort.equalsIgnoreCase("preaching")) {
            return preachingLogApaReportWeek2Dao.findTop300ByOrderByPreachingPointsDesc();
        }
        if (sort.equalsIgnoreCase("teaching")) {
            return preachingLogApaReportWeek2Dao.findTop300ByOrderByTeachingPointsDesc();
        }
        if (sort.equalsIgnoreCase("total")) {
            return preachingLogApaReportWeek2Dao.findTop300ByOrderByTotalPointsDesc();
        }
        return new ArrayList<>();
    }

    public List<PreachingLogApaReportWeek2> getChurchApaRankingsWeek2Report(Integer churchId, String sort) {
        if (sort.equalsIgnoreCase("activity")) {
            return preachingLogApaReportWeek2Dao.findAllByChurchIdOrderByActivityPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("preaching")) {
            return preachingLogApaReportWeek2Dao.findAllByChurchIdOrderByPreachingPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("teaching")) {
            return preachingLogApaReportWeek2Dao.findAllByChurchIdOrderByTeachingPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("total")) {
            return preachingLogApaReportWeek2Dao.findAllByChurchIdOrderByTotalPointsDesc(churchId);
        }
        return new ArrayList<>();
    }

    public List<PreachingLogApaReportWeek3> getApaRankingsWeek3Report(String sort) {
        if (sort.equalsIgnoreCase("activity")) {
            return preachingLogApaReportWeek3Dao.findTop300ByOrderByActivityPointsDesc();
        }
        if (sort.equalsIgnoreCase("preaching")) {
            return preachingLogApaReportWeek3Dao.findTop300ByOrderByPreachingPointsDesc();
        }
        if (sort.equalsIgnoreCase("teaching")) {
            return preachingLogApaReportWeek3Dao.findTop300ByOrderByTeachingPointsDesc();
        }
        if (sort.equalsIgnoreCase("total")) {
            return preachingLogApaReportWeek3Dao.findTop300ByOrderByTotalPointsDesc();
        }
        return new ArrayList<>();
    }

    public List<PreachingLogApaReportWeek3> getChurchApaRankingsWeek3Report(Integer churchId, String sort) {
        if (sort.equalsIgnoreCase("activity")) {
            return preachingLogApaReportWeek3Dao.findAllByChurchIdOrderByActivityPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("preaching")) {
            return preachingLogApaReportWeek3Dao.findAllByChurchIdOrderByPreachingPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("teaching")) {
            return preachingLogApaReportWeek3Dao.findAllByChurchIdOrderByTeachingPointsDesc(churchId);
        }
        if (sort.equalsIgnoreCase("total")) {
            return preachingLogApaReportWeek3Dao.findAllByChurchIdOrderByTotalPointsDesc(churchId);
        }
        return new ArrayList<>();
    }

    @Override
    public PreachingLog save(PreachingLog item) {
        if (item.hasPreaching()) {
            // Prevent preaching logs from being saved in the future
            LocalDateTime parsedDateTime = LocalDate.parse(item.getDate()).atStartOfDay();
            LocalDateTime currentDateTime = LocalDate.now().atStartOfDay();

            // the date was in the past mark approved
            if (parsedDateTime.isAfter(currentDateTime)) {
                logger.warn("Cannot save preaching logs for a future date ({}) for user id: {}", item.getDate(), item.getUserId1());
                return new PreachingLog();
            }

            // Prevent duplicate logs for the same date
            Integer preachingLogId = item.getId() == null ? 0 : item.getId();
            List<String> preachingLogs = preachingLogDao
                .getByUserId1AndDateAndIdIsNot(item.getUserId1(), item.getDate(), preachingLogId)
                .stream()
                .map(preachingLog -> preachingLog.getId().toString())
                .collect(Collectors.toList());
            if (!preachingLogs.isEmpty()) {
                String logs = String.join(",", preachingLogs);
                logger.warn("Removing duplicate preaching logs ({}) for user id: {}", logs, item.getUserId1());
                super.deleteMultiple(logs);
            }
            return super.save(item);
        }
        // Don't save it if there is no preaching data
        if (item.getId() != null) {
            // Delete it
            super.delete(item.getId());
        }
        return new PreachingLog();
    }

    public List<PreachingLog> getAllByAssociationIdAndDateBetween(HttpServletRequest request, Integer associationId, String startDate, String endDate) {
        List<PreachingLog> preachingLogs = preachingLogDao.getByDateBetween(startDate, endDate);

        Member[] members = restService.getAllDisplayAssociationMembers(request, associationId);

        return mapMembersToPreachingLogs(members, preachingLogs);
    }

    public List<PreachingLog> getAllByChurchIdAndDateBetween(HttpServletRequest request, Integer churchId, String startDate, String endDate) {
        List<PreachingLog> preachingLogs = preachingLogDao.getByDateBetween(startDate, endDate);

        Member[] members = restService.getAllDisplayChurchMembers(request, churchId);

        return mapMembersToPreachingLogs(members, preachingLogs);
    }

    public List<PreachingLog> getAllByGroupIdAndDateBetween(HttpServletRequest request, Integer groupId, String startDate, String endDate) {
        List<PreachingLog> preachingLogs = preachingLogDao.getByDateBetween(startDate, endDate);

        Member[] members = restService.getAllDisplayGroupMembers(request, groupId);

        return mapMembersToPreachingLogs(members, preachingLogs);
    }

    public List<PreachingLog> getAllByUserIdAndDateBetween(Integer userId, String startDate, String endDate) {
        return preachingLogDao.getByDateBetweenAndUserId1OrUserId2OrUserId3(startDate, endDate, userId, userId, userId);
    }

    /**
     * Only use this in 2021 and beyond
     */
    @Transactional
    @PreAuthorize("hasAuthority('Admin')")
    public void postAllByDateBetweenOnePerDay(String startDate, String endDate) {
        List<PreachingLog> preachingLogs = preachingLogDao.getByDateBetween(startDate, endDate);

        Map<Integer, List<PreachingLog>> preachingLogMap = new HashMap<>();

        // Group logs by each user
        preachingLogs.forEach(preachingLog -> {
            // Create list for each user
            // In 2021 the preaching is not grouped, so you only need to check userId 1
            if (!preachingLogMap.containsKey(preachingLog.getUserId1())) {
                preachingLogMap.put(preachingLog.getUserId1(), new ArrayList<>());
            }

            // Add preaching log
            preachingLogMap.get(preachingLog.getUserId1()).add(preachingLog);
        });

        // Limit to one per day per user
        preachingLogMap.values().forEach(this::limitPreachingLogsToOnePerDay);
    }

    /**
     * Only use this in 2021 and beyond
     */
    @Transactional
    @PreAuthorize("hasAuthority('Admin')")
    public void postAllByUserIdAndDateBetweenOnePerDay(Integer userId, String startDate, String endDate) {
        List<PreachingLog> preachingLogs = preachingLogDao.getByDateBetweenAndUserId1OrUserId2OrUserId3(startDate, endDate, userId, userId, userId);
        limitPreachingLogsToOnePerDay(preachingLogs);
    }

    private void limitPreachingLogsToOnePerDay(List<PreachingLog> preachingLogs) {
        Map<String, PreachingLog> preachingLogMap = new HashMap<>();
        List<PreachingLog> deletePreachingLogs = new ArrayList<>();

        preachingLogs.forEach(preachingLog -> {
            if (preachingLogMap.containsKey(preachingLog.getDate())) {
                final Integer simple = preachingLogMap.get(preachingLog.getDate()).getSimple();
                final Integer meaningful = preachingLogMap.get(preachingLog.getDate()).getMeaningful();
                final Integer fruit = preachingLogMap.get(preachingLog.getDate()).getFruit();

                // Keep Max
                if (preachingLog.getSimple() != null) {
                    preachingLogMap.get(preachingLog.getDate()).setSimple(Math.max(simple, preachingLog.getSimple()));
                }
                if (preachingLog.getMeaningful() != null) {
                    preachingLogMap.get(preachingLog.getDate()).setMeaningful(Math.max(meaningful, preachingLog.getMeaningful()));
                }
                if (preachingLog.getFruit() != null) {
                    preachingLogMap.get(preachingLog.getDate()).setFruit(Math.max(fruit, preachingLog.getFruit()));
                }

                // Delete duplicates
                deletePreachingLogs.add(preachingLog);
            } else {
                preachingLogMap.put(preachingLog.getDate(), preachingLog);
                // Set nulls to zero
                if (preachingLogMap.get(preachingLog.getDate()).getSimple() == null) {
                    preachingLogMap.get(preachingLog.getDate()).setSimple(0);
                }
                if (preachingLogMap.get(preachingLog.getDate()).getMeaningful() == null) {
                    preachingLogMap.get(preachingLog.getDate()).setMeaningful(0);
                }
                if (preachingLogMap.get(preachingLog.getDate()).getFruit() == null) {
                    preachingLogMap.get(preachingLog.getDate()).setFruit(0);
                }
            }
        });

        // Save
        saveMultiple(new ArrayList<>(preachingLogMap.values()));

        // Delete duplicates
        preachingLogDao.deleteInBatch(deletePreachingLogs);
    }

    public List<PreachingLog.Association> getTotalsByAssociation(HttpServletRequest request, String startDate, String endDate) {
        // Request data
        List<PreachingLog.Association> preachingLogs = this.preachingLogAssociationDao.getByDateBetween(startDate, endDate);
        Association[] associations = restService.getEnabledAssociations(request);

        // Generate Report
        HashMap<Integer, PreachingLog.Association> hashMap = new HashMap<>();
        Arrays.stream(associations).forEach(association -> {
            PreachingLog.Association initialLog = new PreachingLog.Association();
            initialLog.setId(association.getId());
            initialLog.setName(association.getName());
            hashMap.put(association.getId(), initialLog);
        });
        preachingLogs.forEach(preachingLog -> {
            if (hashMap.containsKey(preachingLog.getId())) {
                PreachingLog.Association previousSum = hashMap.get(preachingLog.getId());
                PreachingLog.Association nextSum = PreachingLog.Association.add(previousSum, preachingLog);
                hashMap.put(preachingLog.getId(), nextSum);
            }
        });

        return new ArrayList<>(hashMap.values());
    }

    public List<PreachingLog.Church> getTotalsByChurch(String startDate, String endDate) {
        // Request data
        List<PreachingLog.Church> preachingLogs = this.preachingLogChurchDao.getByDateBetween(startDate, endDate);

        // Generate Report
        HashMap<Integer, PreachingLog.Church> hashMap = new HashMap<>();
        preachingLogs.forEach(preachingLog -> {
            if (hashMap.containsKey(preachingLog.getId())) {
                PreachingLog.Church previousSum = hashMap.get(preachingLog.getId());
                PreachingLog.Church nextSum = PreachingLog.Church.add(previousSum, preachingLog);
                hashMap.put(preachingLog.getId(), nextSum);
            } else {
                hashMap.put(preachingLog.getId(), preachingLog);
            }
        });

        return new ArrayList<>(hashMap.values());
    }

    public List<PreachingLog.Overseer> getTotalsByOverseer(HttpServletRequest request, String startDate, String endDate) {
        // Request data
        List<PreachingLog.Overseer> preachingLogs = this.preachingLogOverseerDao.getByDateBetween(startDate, endDate);
        Church[] churches = restService.getDisplayChurches(request);

        // Generate Report
        HashMap<Integer, PreachingLog.Overseer> hashMap = new HashMap<>();

        // Overseer churches have no parent church
        Arrays.stream(churches).filter(church -> church.getParentChurchId() == null).forEach(church -> {
            PreachingLog.Overseer initialLog = new PreachingLog.Overseer();
            initialLog.setAssociationId(church.getAssociationId());
            initialLog.setCity(church.getCity());
            initialLog.setId(church.getId());
            initialLog.setName(church.getName());
            initialLog.setStateAbbrv(church.getStateAbbrv());
            initialLog.setStateName(church.getStateName());
            hashMap.put(church.getId(), initialLog);
        });
        preachingLogs.forEach(preachingLog -> {
            if (hashMap.containsKey(preachingLog.getId())) {
                PreachingLog.Overseer previousSum = hashMap.get(preachingLog.getId());
                PreachingLog.Overseer nextSum = PreachingLog.Overseer.add(previousSum, preachingLog);
                hashMap.put(preachingLog.getId(), nextSum);
            }
        });

        return new ArrayList<>(hashMap.values());
    }

    public PreachingLogSegments getTotalsBySegment(HttpServletRequest request, String startDate, String endDate) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Database queries
        List<PreachingLog> allPreachingLogs = preachingLogDao.getByDateBetween(startDate, endDate);
        String activeUserCount = restService.getCountActiveUsers(request);
        Association[] associations = restService.getEnabledAssociations(request);

        // Arrange results
        PreachingLogSegments preachingLogSegments = new PreachingLogSegments();
        preachingLogSegments.setAll(sumPreachingLogs("All Associations", allPreachingLogs, Integer.parseInt(activeUserCount)));

        Member[] associationMembers = restService.getAllDisplayAssociationMembers(request, authenticatedUser.getAssociationId());
        List<PreachingLog> associationPreachingLogs = mapMembersToPreachingLogs(associationMembers, allPreachingLogs);
        List<Association> association = Arrays.stream(associations).filter(a -> a.getId().equals(authenticatedUser.getAssociationId())).collect(Collectors.toList());
        String associationName = association.isEmpty() ? "" : association.get(0).getName();
        preachingLogSegments.setAssociation(sumPreachingLogs(associationName, associationPreachingLogs, associationMembers.length));

        // If the mainChurchId is null, the churchId is the mainChurchId
        Integer mainChurchId = authenticatedUser.getMainChurchId() == null ? authenticatedUser.getChurchId() : authenticatedUser.getMainChurchId();
        List<Member> mainChurchMembers = Arrays.stream(associationMembers).filter(e -> (e.getMainChurchId().equals(mainChurchId) || e.getChurchId().equals(mainChurchId))).collect(Collectors.toList());
        List<PreachingLog> mainChurchPreachingLogs = mapMembersToPreachingLogs(mainChurchMembers, associationPreachingLogs);
        preachingLogSegments.setMainChurch(sumPreachingLogs("Main Church", mainChurchPreachingLogs, mainChurchMembers.size()));

        List<Member> churchMembers = mainChurchMembers.stream().filter(e -> e.getChurchId().equals(authenticatedUser.getChurchId())).collect(Collectors.toList());
        List<PreachingLog> churchPreachingLogs = mapMembersToPreachingLogs(churchMembers, mainChurchPreachingLogs);
        String churchName = churchMembers.isEmpty() ? "" : churchMembers.get(0).getChurchName();
        preachingLogSegments.setChurch(sumPreachingLogs(churchName, churchPreachingLogs, churchMembers.size()));

        List<Member> groupMembers = churchMembers.stream().filter(e -> e.getGroupId().equals(authenticatedUser.getGroupId())).collect(Collectors.toList());
        List<PreachingLog> groupPreachingLogs = mapMembersToPreachingLogs(groupMembers, churchPreachingLogs);
        String groupName = groupMembers.isEmpty() ? "" : groupMembers.get(0).getGroupName();
        preachingLogSegments.setGroup(sumPreachingLogs(groupName, groupPreachingLogs, groupMembers.size()));

        List<Member> teamMembers = groupMembers.stream().filter(e -> e.getTeamId().equals(authenticatedUser.getTeamId())).collect(Collectors.toList());
        List<PreachingLog> teamPreachingLogs = mapMembersToPreachingLogs(teamMembers, groupPreachingLogs);
        String teamName = teamMembers.isEmpty() ? "" : teamMembers.get(0).getTeamName();
        preachingLogSegments.setTeam(sumPreachingLogs(teamName, teamPreachingLogs, teamMembers.size()));

        List<Member> user = teamMembers.stream().filter(e -> e.getId().equals(authenticatedUser.getId())).collect(Collectors.toList());
        List<PreachingLog> userPreachingLogs = mapMembersToPreachingLogs(user, teamPreachingLogs);
        preachingLogSegments.setUser(sumPreachingLogs(authenticatedUser.getChurchName(), userPreachingLogs, 1));

        return preachingLogSegments;
    }

    public List<PreachingLogUserReport> getAllUserReportsByGoalsAndDateBetween(
        String startDate,
        String endDate,
        Integer simpleGoal,
        Integer meaningfulGoal,
        Integer fruitGoal
    ) {
        if (simpleGoal == null) {
            simpleGoal = 0;
        }

        if (meaningfulGoal == null) {
            meaningfulGoal = 0;
        }

        if (fruitGoal == null) {
            fruitGoal = 0;
        }

        return preachingLogUserReportDao.getByDateBetweenAndSimpleIsGreaterThanEqualAndMeaningfulIsGreaterThanEqualAndFruitIsGreaterThanEqual(
            startDate, endDate, simpleGoal, meaningfulGoal, fruitGoal
        );
    }

    public List<PreachingLogUserReport> getUserRankingsByCategoryAndDateBetween(
        String startDate,
        String endDate,
        String categoryName,
        Integer offset,
        Integer limit
    ) throws IllegalArgumentException {
        final int searchLimit = limit == null ? PreachingLogUserReport.DEFAULT_LIMIT : limit;
        final int searchSkip = offset == null ? 0 : (offset * searchLimit);

        if (categoryName.equals(PreachingLogReportType.FRUIT)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getFruitRankingsByDateBetween(startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        if (categoryName.equals(PreachingLogReportType.MEANINGFUL)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getMeaningfulRankingsByDateBetween(startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        if (categoryName.equals(PreachingLogReportType.SIMPLE)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getSimpleRankingsByDateBetween(startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        throw new IllegalArgumentException(PreachingLogReportType.ERROR);
    }

    public List<PreachingLogUserReport> getUserShortTermRankingsByCategoryAndDateBetween(
        Integer shortTermId,
        String categoryName,
        Integer offset,
        Integer limit
    ) throws IllegalArgumentException {
        Optional<ShortTermPreachingView> shortTermPreachingOptional = shortTermPreachingViewDao.findById(shortTermId);

        if (shortTermPreachingOptional.isPresent()) {
            ShortTermPreachingView shortTermPreaching = shortTermPreachingOptional.get();

            final int searchLimit = limit == null ? PreachingLogUserReport.DEFAULT_LIMIT : limit;
            final int searchSkip = offset == null ? 0 : (offset * searchLimit);

            if (categoryName.equals(PreachingLogReportType.FRUIT)) {
                List<List<Object>> rankingData = preachingLogUserShortTermReportDao.getFruitRankingsByDateBetween(
                    shortTermId,
                    shortTermPreaching.getStartDate(),
                    shortTermPreaching.getEndDate()
                );
                return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
            }

            if (categoryName.equals(PreachingLogReportType.MEANINGFUL)) {
                List<List<Object>> rankingData = preachingLogUserShortTermReportDao.getMeaningfulRankingsByDateBetween(
                    shortTermId,
                    shortTermPreaching.getStartDate(),
                    shortTermPreaching.getEndDate()
                );
                return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
            }

            if (categoryName.equals(PreachingLogReportType.SIMPLE)) {
                List<List<Object>> rankingData = preachingLogUserShortTermReportDao.getSimpleRankingsByDateBetween(
                    shortTermId,
                    shortTermPreaching.getStartDate(),
                    shortTermPreaching.getEndDate()
                );
                return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
            }

            throw new IllegalArgumentException(PreachingLogReportType.ERROR);
        }

        throw new NotFoundException();
    }

    public List<PreachingLogUserReport> getChurchUserRankingsByCategoryAndDateBetween(
        Integer churchId,
        String startDate,
        String endDate,
        String categoryName,
        Integer offset,
        Integer limit
    ) throws IllegalArgumentException {
        final int searchLimit = limit == null ? PreachingLogUserReport.DEFAULT_LIMIT : limit;
        final int searchSkip = offset == null ? 0 : (offset * searchLimit);

        if (categoryName.equals(PreachingLogReportType.FRUIT)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getUserFruitRankingsByChurchAndDateBetween(churchId, startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        if (categoryName.equals(PreachingLogReportType.MEANINGFUL)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getUserMeaningfulRankingsByChurchAndDateBetween(churchId, startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        if (categoryName.equals(PreachingLogReportType.SIMPLE)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getUserSimpleRankingsByChurchAndDateBetween(churchId, startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        throw new IllegalArgumentException(PreachingLogReportType.ERROR);
    }

    public List<PreachingLogUserReport> getOverseerUserRankingsByCategoryAndDateBetween(
        Integer mainChurchId,
        String startDate,
        String endDate,
        String categoryName,
        Integer offset,
        Integer limit
    ) throws IllegalArgumentException {
        final int searchLimit = limit == null ? PreachingLogUserReport.DEFAULT_LIMIT : limit;
        final int searchSkip = offset == null ? 0 : (offset * searchLimit);

        if (categoryName.equals(PreachingLogReportType.FRUIT)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getUserFruitRankingsByOverseerAndDateBetween(mainChurchId, startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        if (categoryName.equals(PreachingLogReportType.MEANINGFUL)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getUserMeaningfulRankingsByOverseerAndDateBetween(mainChurchId, startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        if (categoryName.equals(PreachingLogReportType.SIMPLE)) {
            List<List<Object>> rankingData = preachingLogUserReportDao.getUserSimpleRankingsByOverseerAndDateBetween(mainChurchId, startDate, endDate);
            return reduceRankingsObject(rankingData, categoryName, searchSkip, searchLimit);
        }

        throw new IllegalArgumentException(PreachingLogReportType.ERROR);
    }

    public PreachingLogUserRank getUserChurchRankByDateBetween(
        Integer userId,
        Integer churchId,
        String startDate,
        String endDate
    ) {
        List<PreachingLogUserReport> reports = preachingLogUserReportDao.getChurchRankingsByDateBetween(churchId, startDate, endDate)
            .stream()
            .map(this::mapRankingsObject)
            .collect(Collectors.toList());

        return getUserRankFromPreachingLogReports(reports, userId);
    }

    public PreachingLogUserRank getUserOverseerRankByDateBetween(
        Integer userId,
        Integer churchId,
        String startDate,
        String endDate
    ) {
        List<PreachingLogUserReport> reports = preachingLogUserReportDao.getOverseerRankingsByDateBetween(churchId, startDate, endDate)
            .stream()
            .map(this::mapRankingsObject)
            .collect(Collectors.toList());

        return getUserRankFromPreachingLogReports(reports, userId);
    }

    // TODO: This takes the longest
    public PreachingLogUserRank getUserRankByDateBetween(
        Integer userId,
        String startDate,
        String endDate
    ) {
        List<PreachingLogUserReport> reports = preachingLogUserReportDao.getRankingsByDateBetween(startDate, endDate)
            .stream()
            .map(this::mapRankingsObject)
            .collect(Collectors.toList());

        return getUserRankFromPreachingLogReports(reports, userId);
    }

    public PreachingLogUserReport getUserTotalsByDateBetween(
        Integer userId,
        String startDate,
        String endDate
    ) {
        List<PreachingLogUserReport> reports = preachingLogUserReportDao.getUserTotalsByDateBetween(userId, startDate, endDate)
            .stream()
            .map(this::mapRankingsObject)
            .collect(Collectors.toList());

        if (reports.isEmpty()) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            PreachingLogUserReport report = new PreachingLogUserReport();
            report.setChurchName(authenticatedUser.getChurchName());
            report.setChurchId(authenticatedUser.getChurchId());
            report.setUserDisplayName(authenticatedUser.getDisplayName());
            report.setUserId(authenticatedUser.getId());
            report.setUserPictureUrl(authenticatedUser.getProfilePictureUrl());
            report.setSimple(0);
            report.setMeaningful(0);
            report.setFruit(0);
            return report;
        }

        return reports.get(0);
    }

    public PreachingLogUserRank getShortTermUserRankByDateBetween(
        Integer shortTermId,
        Integer userId
    ) {
        Optional<ShortTermPreachingView> shortTermPreachingOptional = shortTermPreachingViewDao.findById(shortTermId);

        if (shortTermPreachingOptional.isPresent()) {
            ShortTermPreachingView shortTermPreaching = shortTermPreachingOptional.get();

            // Make view for short terms to query
            List<PreachingLogUserReport> reports = preachingLogUserShortTermReportDao.getRankingsByDateBetween(
                    shortTermId,
                    shortTermPreaching.getStartDate(),
                    shortTermPreaching.getEndDate()
                )
                .stream()
                .map(this::mapRankingsObject)
                .collect(Collectors.toList());

            return getUserRankFromPreachingLogReports(reports, userId);
        }

        throw new NotFoundException();
    }

    public List<PreachingLog.ChurchWithGoals> getChurchRankingsByCategoryAndDateBetween(String startDate,
                                                                                        String endDate,
                                                                                        String categoryName) {
        HashMap<Integer, PreachingLog.ChurchWithGoals> hashMap = getChurchWithGoalsByCategoryAndDateBetween(startDate, endDate);

        return getRankedChurchPreachingByCategory(hashMap, categoryName);
    }

    public List<PreachingLog.RegionWithChurches> getChurchesByRegion(String startDate,
                                                                     String endDate,
                                                                     String categoryName) {
        // church id/churchWithGoals
        HashMap<Integer, PreachingLog.ChurchWithGoals> hashMapByChuchId =
            getChurchWithGoalsByCategoryAndDateBetween(startDate, endDate);

        // get the hashmap with region id/churchWithGoals
        HashMap<Integer, List<PreachingLog.ChurchWithGoals>> hashMapByRegionId = new HashMap<>();
        hashMapByChuchId.forEach((integer, churchWithGoals) -> {
            if (churchWithGoals.getRegionId() != null) {
                List<PreachingLog.ChurchWithGoals> churchWithGoalsList = new ArrayList<>();
                if (hashMapByRegionId.containsKey(churchWithGoals.getRegionId())) {
                    churchWithGoalsList =
                        hashMapByRegionId.get(churchWithGoals.getRegionId());
                }
                churchWithGoalsList.add(churchWithGoals);
                hashMapByRegionId.put(churchWithGoals.getRegionId(), churchWithGoalsList);
            }
        });

        List<PreachingLog.RegionWithGoals> regionWithGoals = getRegionRankingsByCategoryAndDateBetween(startDate,
            endDate, categoryName);

        // combine the hashmap with regions total
        List<PreachingLog.RegionWithGoals> regionsWithGoals =
            getRankedRegionPreachingByCategory(getRegionsWithGoals(startDate, endDate), categoryName);
        List<PreachingLog.RegionWithChurches> regionsWithChurches = new ArrayList<>();

        regionsWithGoals.forEach((regionWithGoal) -> {
            PreachingLog.RegionWithChurches regionWithChurch = new PreachingLog.RegionWithChurches();
            regionWithChurch.setUniqueId(regionWithGoal.getUniqueId());
            regionWithChurch.setRegionId(regionWithGoal.getRegionId());
            regionWithChurch.setDisplayName(regionWithGoal.getDisplayName());
            regionWithChurch.setRegionName(regionWithGoal.getRegionName());
            regionWithChurch.setSimple(regionWithGoal.getSimple());
            regionWithChurch.setSimpleGoal(regionWithGoal.getSimpleGoal());
            regionWithChurch.setMeaningful(regionWithGoal.getMeaningful());
            regionWithChurch.setMeaningfulGoal(regionWithGoal.getMeaningfulGoal());
            regionWithChurch.setFruit(regionWithGoal.getFruit());
            regionWithChurch.setFruitGoal(regionWithGoal.getFruitGoal());
            regionWithChurch.setRank(regionWithGoal.getRank());
            if (hashMapByRegionId.containsKey(regionWithGoal.getRegionId())) {
                regionWithChurch.setChurches(hashMapByRegionId.get(regionWithGoal.getRegionId()));
            }
            regionsWithChurches.add(regionWithChurch);
        });

        return regionsWithChurches;

    }

    public HashMap<Integer, PreachingLog.ChurchWithGoals> getChurchWithGoalsByCategoryAndDateBetween(String startDate,
                                                                                                     String endDate) {
        List<MovementChurchGoal> movementChurchGoals = this.movementChurchGoalDao.findAll();
        HashMap<Integer, MovementChurchGoal> goalMap = new HashMap<>();

        movementChurchGoals.forEach(goal -> {
            if (!goalMap.containsKey(goal.getReferenceId())) {
                goalMap.put(goal.getReferenceId(), goal);
            }
        });

        // Request data
        List<PreachingLog.Church> preachingLogs = this.preachingLogChurchDao.getByDateBetween(startDate, endDate);

        // Generate Report
        HashMap<Integer, PreachingLog.ChurchWithGoals> hashMap = new HashMap<>();
        preachingLogs.forEach(preachingLog -> {
            if (hashMap.containsKey(preachingLog.getId())) {
                PreachingLog.ChurchWithGoals previousSum = hashMap.get(preachingLog.getId());
                PreachingLog.ChurchWithGoals nextSum = PreachingLog.ChurchWithGoals.add(previousSum, preachingLog);

                hashMap.put(preachingLog.getId(), nextSum);
            } else {
                PreachingLog.ChurchWithGoals churchWithGoals =
                    PreachingLog.ChurchWithGoals.convertToChurchWithGoals(preachingLog);

                if (goalMap.containsKey(preachingLog.getId())) {

                    //simple
                    int simpleGoal = goalMap.get(preachingLog.getId()).getSimple();
                    churchWithGoals.setSimpleGoal(simpleGoal);

                    //meaningful
                    int meaningfulGoal = goalMap.get(preachingLog.getId()).getMeaningful();
                    churchWithGoals.setMeaningfulGoal(meaningfulGoal);

                    //fruit
                    int fruitGoal = goalMap.get(preachingLog.getId()).getFruit();
                    churchWithGoals.setFruitGoal(fruitGoal);
                }
                hashMap.put(preachingLog.getId(), churchWithGoals);
            }
        });
        return hashMap;
    }

    public List<PreachingLog.RegionWithGoals> getRegionRankingsByCategoryAndDateBetween(String startDate,
                                                                                        String endDate,
                                                                                        String categoryName) {
        HashMap<Integer, PreachingLog.RegionWithGoals> hashMap = getRegionsWithGoals(startDate, endDate);

        return getRankedRegionPreachingByCategory(hashMap, categoryName);
    }

    public HashMap<Integer, PreachingLog.RegionWithGoals> getRegionsWithGoals(String startDate, String endDate) {
        List<MovementChurchGoal> movementChurchGoals = this.movementChurchGoalDao.getAllByRegionIdIsNotNull();
        HashMap<Integer, MovementChurchGoal> goalMap = new HashMap<>();

        movementChurchGoals.forEach(goal -> {
            if (!goalMap.containsKey(goal.getRegionId())) {
                goalMap.put(goal.getRegionId(), goal);
            } else {
                MovementChurchGoal currentRegionGoal = goalMap.get(goal.getRegionId());
                MovementChurchGoal.addForRegion(currentRegionGoal, goal);
            }
        });

        // Request data
        List<PreachingLog.Region> preachingLogs = this.preachingLogRegionDao.getByDateBetween(startDate, endDate);

        // Generate Report
        HashMap<Integer, PreachingLog.RegionWithGoals> hashMap = new HashMap<>();
        preachingLogs.forEach(preachingLog -> {
            if (hashMap.containsKey(preachingLog.getRegionId())) {
                PreachingLog.RegionWithGoals previousSum = hashMap.get(preachingLog.getRegionId());
                PreachingLog.RegionWithGoals nextSum = PreachingLog.RegionWithGoals.add(previousSum, preachingLog);

                hashMap.put(preachingLog.getRegionId(), nextSum);
            } else {
                PreachingLog.RegionWithGoals regionWithGoals =
                    PreachingLog.Region.convertToRegionWithGoals(preachingLog);

                if (goalMap.containsKey(preachingLog.getRegionId())) {

                    //simple
                    int simpleGoal = goalMap.get(preachingLog.getRegionId()).getSimple();
                    regionWithGoals.setSimpleGoal(simpleGoal);

                    //meaningful
                    int meaningfulGoal = goalMap.get(preachingLog.getRegionId()).getMeaningful();
                    regionWithGoals.setMeaningfulGoal(meaningfulGoal);

                    //fruit
                    int fruitGoal = goalMap.get(preachingLog.getRegionId()).getFruit();
                    regionWithGoals.setFruitGoal(fruitGoal);
                }
                hashMap.put(preachingLog.getRegionId(), regionWithGoals);
            }
        });
        return hashMap;
    }

    public HashMap<String, Integer> getChurchRankDateBetween(Integer churchId, String startDate, String endDate) {
        List<MovementChurchGoal> movementChurchGoals = this.movementChurchGoalDao.findAll();
        HashMap<Integer, MovementChurchGoal> goalMap = new HashMap<>();

        movementChurchGoals.forEach(goal -> {
            if (!goalMap.containsKey(goal.getReferenceId())) {
                goalMap.put(goal.getReferenceId(), goal);
            }
        });

        // Request data
        List<PreachingLog.Church> preachingLogs = this.preachingLogChurchDao.getByDateBetween(startDate, endDate);

        // Generate Report
        HashMap<Integer, PreachingLog.ChurchWithGoals> hashMap = new HashMap<>();
        preachingLogs.forEach(preachingLog -> {
            if (hashMap.containsKey(preachingLog.getId())) {
                PreachingLog.ChurchWithGoals previousSum = hashMap.get(preachingLog.getId());
                PreachingLog.ChurchWithGoals nextSum = PreachingLog.ChurchWithGoals.add(previousSum, preachingLog);

                hashMap.put(preachingLog.getId(), nextSum);
            } else {
                PreachingLog.ChurchWithGoals churchWithGoals =
                    PreachingLog.ChurchWithGoals.convertToChurchWithGoals(preachingLog);

                if (goalMap.containsKey(preachingLog.getId())) {

                    //simple
                    int simpleGoal = goalMap.get(preachingLog.getId()).getSimple();
                    churchWithGoals.setSimpleGoal(simpleGoal);

                    //meaningful
                    int meaningfulGoal = goalMap.get(preachingLog.getId()).getMeaningful();
                    churchWithGoals.setMeaningfulGoal(meaningfulGoal);

                    //fruit
                    int fruitGoal = goalMap.get(preachingLog.getId()).getFruit();
                    churchWithGoals.setFruitGoal(fruitGoal);
                }
                hashMap.put(preachingLog.getId(), churchWithGoals);
            }
        });

        HashMap<Integer, PreachingLog.ChurchWithGoals> simpleRanking = getChurchRankByCategory(hashMap, churchId, "simple");
        Map.Entry<Integer, PreachingLog.ChurchWithGoals> simpleEntry = simpleRanking.entrySet().iterator().next();
        HashMap<Integer, PreachingLog.ChurchWithGoals> meaningfulRanking = getChurchRankByCategory(hashMap, churchId, "meaningful");
        Map.Entry<Integer, PreachingLog.ChurchWithGoals> meaningfulEntry = meaningfulRanking.entrySet().iterator().next();
        HashMap<Integer, PreachingLog.ChurchWithGoals> fruitRanking = getChurchRankByCategory(hashMap, churchId, "fruit");
        Map.Entry<Integer, PreachingLog.ChurchWithGoals> fruitEntry = fruitRanking.entrySet().iterator().next();

        HashMap<String, Integer> ranks = new HashMap<>();
        ranks.put("simpleRank", simpleEntry.getKey());
        ranks.put("simpleGoal", simpleEntry.getValue().getSimpleGoal());
        ranks.put("simple", simpleEntry.getValue().getSimple());
        ranks.put("meaningfulRank", meaningfulEntry.getKey());
        ranks.put("meaningfulGoal", meaningfulEntry.getValue().getMeaningfulGoal());
        ranks.put("meaningful", meaningfulEntry.getValue().getMeaningful());
        ranks.put("fruitRank", fruitEntry.getKey());
        ranks.put("fruitGoal", fruitEntry.getValue().getFruitGoal());
        ranks.put("fruit", fruitEntry.getValue().getFruit());

        return ranks;
    }

    public List<TrackerUsageAllReport> getTrackerUsageAll() {
        List<MovementChurchGoal> movementChurchGoals = movementChurchGoalDao.findAll();
        int totalParticipants = movementChurchGoals.stream().mapToInt(MovementChurchGoal::getParticipantCount).sum();
        List<TrackerUsageAll> trackerUsageAllList = trackerUsageAllDao.findAll();
        List<TrackerUsageAllReport> trackerUsageAllReports = new ArrayList<>();
        for (TrackerUsageAll trackerUsageAll : trackerUsageAllList) {
            TrackerUsageAllReport trackerUsageAllReport = new TrackerUsageAllReport();
            trackerUsageAllReport.setMonth(trackerUsageAll.getMonth());
            trackerUsageAllReport.setYear(trackerUsageAll.getYear());
            trackerUsageAllReport.setNumOfActiveUsers(trackerUsageAll.getNumOfActiveUsers());
            trackerUsageAllReport.setParticipantGoal(totalParticipants);
            trackerUsageAllReports.add(trackerUsageAllReport);
        }

        return trackerUsageAllReports;
    }

    public List<TrackerUsageChurchReport> getTrackerUsageByChurch() {
        List<MovementChurchGoal> movementChurchGoals = movementChurchGoalDao.findAll();
        HashMap<Integer, Integer> goals = new HashMap<>();
        movementChurchGoals.forEach(movementChurchGoal -> {
            goals.putIfAbsent(movementChurchGoal.getReferenceId(), movementChurchGoal.getParticipantCount());
        });

        List<TrackerUsageChurch> trackerUsageChurchList = trackerUsageChurchDao.findAll();
        List<TrackerUsageChurchReport> trackerUsageAllReports = new ArrayList<>();
        trackerUsageChurchList.forEach(trackerUsageChurch -> {
            TrackerUsageChurchReport trackerUsageChurchReport = new TrackerUsageChurchReport();
            trackerUsageChurchReport.setChurchId(trackerUsageChurch.getChurchId());
            trackerUsageChurchReport.setChurchName(trackerUsageChurch.getChurchName());
            trackerUsageChurchReport.setMonth(trackerUsageChurch.getMonth());
            trackerUsageChurchReport.setYear(trackerUsageChurch.getYear());
            trackerUsageChurchReport.setNumOfActiveUsers(trackerUsageChurch.getNumOfActiveUsers());
            if (goals.containsKey(trackerUsageChurch.getChurchId())) {
                trackerUsageChurchReport.setParticipantGoal(goals.get(trackerUsageChurch.getChurchId()));
                double percentage =
                    Math.round((double) trackerUsageChurch.getNumOfActiveUsers() * 100 / goals.get(trackerUsageChurch.getChurchId()) * 100) / 100.0;
                trackerUsageChurchReport.setPercentage(percentage);
            }
            trackerUsageAllReports.add(trackerUsageChurchReport);
        });

        return trackerUsageAllReports;
    }

    public HashMap<Integer, PreachingLog.ChurchWithGoals> getChurchRankByCategory(HashMap<Integer, PreachingLog.ChurchWithGoals> hashMap, int churchId,
                                                                                  String category) {
        HashMap<Integer, PreachingLog.ChurchWithGoals> sortedHashMap = sortByCategory(hashMap, category);
        HashMap<Integer, PreachingLog.ChurchWithGoals> rankHashMap = new HashMap<>();
        // Getting an iterator
        Iterator hmIterator = sortedHashMap.entrySet().iterator();
        int rank = 0;
        double previousValue = -1;
        while (hmIterator.hasNext()) {
            Map.Entry<Integer, PreachingLog.ChurchWithGoals> mapElement = (Map.Entry<Integer, PreachingLog.ChurchWithGoals>) hmIterator.next();
            double value = 0;
            if (Objects.equals(category, "meaningful")) {
                if (mapElement.getValue().getMeaningfulGoal() > 0) {
                    value =
                        (double) mapElement.getValue().getMeaningful() * 100 / mapElement.getValue().getMeaningfulGoal();

                }
            } else if (Objects.equals(category, "fruit")) {
                if (mapElement.getValue().getFruitGoal() > 0) {
                    value = (double) mapElement.getValue().getFruit() * 100 / mapElement.getValue().getFruitGoal();
                }
            } else if (Objects.equals(category, "simple")) {
                if (mapElement.getValue().getSimpleGoal() > 0) {
                    value = (double) mapElement.getValue().getSimple() * 100 / mapElement.getValue().getSimpleGoal();
                }
            }

            if (value != previousValue) {
                previousValue = value;
                rank++;
            }
            if (churchId == mapElement.getValue().getId()) {
                rankHashMap.put(rank, mapElement.getValue());
                return rankHashMap;
            }
        }
        rankHashMap.put(rank, new PreachingLog.ChurchWithGoals());
        return rankHashMap;
    }

    public List<PreachingLog.ChurchWithGoals> getRankedChurchPreachingByCategory(HashMap<Integer,
        PreachingLog.ChurchWithGoals> hashMap, String category) {
        HashMap<Integer, PreachingLog.ChurchWithGoals> sortedHashMap = sortByCategory(hashMap, category);

        int rank = 0;
        double previousValue = -1;
        List<PreachingLog.ChurchWithGoals> rankedChurchWithGoals = new ArrayList<>();
        // Getting an iterator
        Iterator hmIterator = sortedHashMap.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry<Integer, PreachingLog.ChurchWithGoals> mapElement = (Map.Entry<Integer, PreachingLog.ChurchWithGoals>) hmIterator.next();
            double value = 0;
            if (category.equals("meaningful")) {
                if (mapElement.getValue().getMeaningfulGoal() > 0) {
                    value =
                        (double) mapElement.getValue().getMeaningful() * 100 / mapElement.getValue().getMeaningfulGoal();

                }
            } else if (category.equals("fruit")) {
                if (mapElement.getValue().getFruitGoal() > 0) {
                    value = (double) mapElement.getValue().getFruit() * 100 / mapElement.getValue().getFruitGoal();
                }
            } else if (category.equals("simple")) {
                if (mapElement.getValue().getSimpleGoal() > 0) {
                    value = (double) mapElement.getValue().getSimple() * 100 / mapElement.getValue().getSimpleGoal();
                }
            }

            if (value != previousValue) {
                previousValue = value;
                rank++;
            }
            PreachingLog.ChurchWithGoals newChurchWithGoals = mapElement.getValue();
            newChurchWithGoals.setRank(rank);
            rankedChurchWithGoals.add(newChurchWithGoals);
        }
        return rankedChurchWithGoals;
    }

    public List<PreachingLog.RegionWithGoals> getRankedRegionPreachingByCategory(HashMap<Integer,
        PreachingLog.RegionWithGoals> hashMap, String category) {
        HashMap<Integer, PreachingLog.RegionWithGoals> sortedHashMap = sortByCategoryByRegion(hashMap, category);

        int rank = 0;
        double previousValue = -1;
        List<PreachingLog.RegionWithGoals> rankedRegionWithGoals = new ArrayList<>();
        // Getting an iterator
        Iterator hmIterator = sortedHashMap.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry<Integer, PreachingLog.RegionWithGoals> mapElement = (Map.Entry<Integer, PreachingLog.RegionWithGoals>) hmIterator.next();
            double value = 0;
            if (category.equals("meaningful")) {
                if (mapElement.getValue().getMeaningfulGoal() > 0) {
                    value =
                        (double) mapElement.getValue().getMeaningful() * 100 / mapElement.getValue().getMeaningfulGoal();

                }
            } else if (category.equals("fruit")) {
                if (mapElement.getValue().getFruitGoal() > 0) {
                    value = (double) mapElement.getValue().getFruit() * 100 / mapElement.getValue().getFruitGoal();
                }
            } else if (category.equals("simple")) {
                if (mapElement.getValue().getSimpleGoal() > 0) {
                    value = (double) mapElement.getValue().getSimple() * 100 / mapElement.getValue().getSimpleGoal();
                }
            }

            if (value != previousValue) {
                previousValue = value;
                rank++;
            }
            PreachingLog.RegionWithGoals newRegionWithGoals = mapElement.getValue();
            newRegionWithGoals.setRank(rank);
            rankedRegionWithGoals.add(newRegionWithGoals);
        }
        return rankedRegionWithGoals;
    }


    // function to sort hashmap by values
    public HashMap<Integer, PreachingLog.ChurchWithGoals> sortByCategory(HashMap<Integer,
        PreachingLog.ChurchWithGoals> hm, String category) {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, PreachingLog.ChurchWithGoals>> list =
            new LinkedList<Map.Entry<Integer, PreachingLog.ChurchWithGoals>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, PreachingLog.ChurchWithGoals>>() {
            public int compare(Map.Entry<Integer, PreachingLog.ChurchWithGoals> o1,
                               Map.Entry<Integer, PreachingLog.ChurchWithGoals> o2) {
                switch (category) {
                    case "simple":
                        double o1SimplePercentage = 0;
                        if (o1.getValue().getSimpleGoal() > 0) {
                            o1SimplePercentage = (double) o1.getValue().getSimple() / o1.getValue().getSimpleGoal();
                        }

                        double o2SimplePercentage = 0;
                        if (o2.getValue().getSimpleGoal() > 0) {
                            o2SimplePercentage = (double) o2.getValue().getSimple() / o2.getValue().getSimpleGoal();
                        }

                        return o1SimplePercentage < o2SimplePercentage ? 1 : -1;
                    case "meaningful":
                        double o1MeaningfulPercentage = 0;
                        if (o1.getValue().getMeaningfulGoal() > 0) {
                            o1MeaningfulPercentage =
                                (double) o1.getValue().getMeaningful() / o1.getValue().getMeaningfulGoal();
                        }

                        double o2MeaningfulPercentage = 0;
                        if (o2.getValue().getSimpleGoal() > 0) {
                            o2MeaningfulPercentage =
                                (double) o2.getValue().getMeaningful() / o2.getValue().getMeaningfulGoal();
                        }

                        return o1MeaningfulPercentage < o2MeaningfulPercentage ? 1 : -1;
                    case "fruit":
                        double o1FruitPercentage = 0;
                        if (o1.getValue().getFruitGoal() > 0) {
                            o1FruitPercentage =
                                (double) o1.getValue().getFruit() / o1.getValue().getFruitGoal();
                        }

                        double o2FruitPercentage = 0;
                        if (o2.getValue().getSimpleGoal() > 0) {
                            o2FruitPercentage =
                                (double) o2.getValue().getFruit() / o2.getValue().getFruitGoal();
                        }

                        return o1FruitPercentage < o2FruitPercentage ? 1 : -1;
                    default:
                        return 0;
                }
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, PreachingLog.ChurchWithGoals> temp = new LinkedHashMap<Integer, PreachingLog.ChurchWithGoals>();
        for (Map.Entry<Integer, PreachingLog.ChurchWithGoals> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public HashMap<Integer, PreachingLog.RegionWithGoals> sortByCategoryByRegion(HashMap<Integer,
        PreachingLog.RegionWithGoals> hm, String category) {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, PreachingLog.RegionWithGoals>> list =
            new LinkedList<Map.Entry<Integer, PreachingLog.RegionWithGoals>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, PreachingLog.RegionWithGoals>>() {
            public int compare(Map.Entry<Integer, PreachingLog.RegionWithGoals> o1,
                               Map.Entry<Integer, PreachingLog.RegionWithGoals> o2) {
                switch (category) {
                    case "simple":
                        double o1SimplePercentage = 0;
                        if (o1.getValue().getSimpleGoal() > 0) {
                            o1SimplePercentage = (double) o1.getValue().getSimple() / o1.getValue().getSimpleGoal();
                        }

                        double o2SimplePercentage = 0;
                        if (o2.getValue().getSimpleGoal() > 0) {
                            o2SimplePercentage = (double) o2.getValue().getSimple() / o2.getValue().getSimpleGoal();
                        }

                        return o1SimplePercentage < o2SimplePercentage ? 1 : -1;
                    case "meaningful":
                        double o1MeaningfulPercentage = 0;
                        if (o1.getValue().getMeaningfulGoal() > 0) {
                            o1MeaningfulPercentage =
                                (double) o1.getValue().getMeaningful() / o1.getValue().getMeaningfulGoal();
                        }

                        double o2MeaningfulPercentage = 0;
                        if (o2.getValue().getSimpleGoal() > 0) {
                            o2MeaningfulPercentage =
                                (double) o2.getValue().getMeaningful() / o2.getValue().getMeaningfulGoal();
                        }

                        return o1MeaningfulPercentage < o2MeaningfulPercentage ? 1 : -1;
                    case "fruit":
                        double o1FruitPercentage = 0;
                        if (o1.getValue().getFruitGoal() > 0) {
                            o1FruitPercentage =
                                (double) o1.getValue().getFruit() / o1.getValue().getFruitGoal();
                        }

                        double o2FruitPercentage = 0;
                        if (o2.getValue().getSimpleGoal() > 0) {
                            o2FruitPercentage =
                                (double) o2.getValue().getFruit() / o2.getValue().getFruitGoal();
                        }

                        return o1FruitPercentage < o2FruitPercentage ? 1 : -1;
                    default:
                        return 0;
                }
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, PreachingLog.RegionWithGoals> temp = new LinkedHashMap<Integer, PreachingLog.RegionWithGoals>();
        for (Map.Entry<Integer, PreachingLog.RegionWithGoals> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public PreachingLogUserRank getUserRankFromPreachingLogReports(List<PreachingLogUserReport> reports, Integer userId) {
        PreachingLogUserRank results = new PreachingLogUserRank();

        if ((long) reports.size() > 0) {
            // Fruit
            Map<Integer, PreachingLogUserReport> fruitRankMap = getUserRankByCategory(reports, userId, PreachingType.Fruit);
            try {
                Map.Entry<Integer, PreachingLogUserReport> fruitRankEntry = fruitRankMap.entrySet().iterator().next();
                int fruitRank = fruitRankEntry.getKey();
                PreachingLogUserReport fruitPreachingLog = fruitRankEntry.getValue();
                results.setFruitPercent((float) fruitPreachingLog.getFruit() * 10);
                results.setFruitRank((float) fruitRank);
                results.setFruitResult((float) fruitPreachingLog.getFruit());
            } catch (Exception ignore) {
                // This will fail if the user has no preaching data, so we can ignore it.
            }

            // Meaningful
            Map<Integer, PreachingLogUserReport> meaningfulRankMap = getUserRankByCategory(reports, userId, PreachingType.Meaningful);
            try {
                Map.Entry<Integer, PreachingLogUserReport> meaningfulRankEntry = meaningfulRankMap.entrySet().iterator().next();
                int meaningfulRank = meaningfulRankEntry.getKey();
                PreachingLogUserReport meaningfulPreachingLog = meaningfulRankEntry.getValue();
                results.setMeaningfulPercent((float) meaningfulPreachingLog.getMeaningful());
                results.setMeaningfulRank((float) meaningfulRank);
                results.setMeaningfulResult((float) meaningfulPreachingLog.getMeaningful());
            } catch (Exception ignore) {
                // This will fail if the user has no preaching data, so we can ignore it.
            }

            // Simple
            Map<Integer, PreachingLogUserReport> simpleRankMap = getUserRankByCategory(reports, userId, PreachingType.Simple);
            try {
                Map.Entry<Integer, PreachingLogUserReport> simpleRankEntry = simpleRankMap.entrySet().iterator().next();
                int simpleRank = simpleRankEntry.getKey();
                PreachingLogUserReport simplePreachingLog = simpleRankEntry.getValue();
                results.setSimplePercent((float) simplePreachingLog.getSimple() / 10);
                results.setSimpleRank((float) simpleRank);
                results.setSimpleResult((float) simplePreachingLog.getSimple());
            } catch (Exception ignore) {
                // This will fail if the user has no preaching data, so we can ignore it.
            }
        }

        return results;
    }

    public Map<Integer, PreachingLogUserReport> getUserRankByCategory(List<PreachingLogUserReport> userReports, Integer userId, String categoryName) {
        List<PreachingLogUserReport> sortedUserReports = userReports
            .stream()
            .sorted((PreachingLogUserReport a, PreachingLogUserReport b) -> sortRankedPreachingLog(a, b, categoryName))
            .collect(Collectors.toList());

        Map<Integer, PreachingLogUserReport> rankMap = new HashMap<>();
        int rank = 0;
        int previousResult = -1;
        for (PreachingLogUserReport userReport : sortedUserReports) {
            rank += 1;
            // Allow members with the same result to be ranked the same
            if (categoryName.equals(PreachingType.Fruit)) {
                if (previousResult == userReport.getFruit()) {
                    rank -= 1;
                }
                previousResult = userReport.getFruit();
            } else if (categoryName.equals(PreachingType.Meaningful)) {
                if (previousResult == userReport.getMeaningful()) {
                    rank -= 1;
                }
                previousResult = userReport.getMeaningful();
            } else if (categoryName.equals(PreachingType.Simple)) {
                if (previousResult == userReport.getSimple()) {
                    rank -= 1;
                }
                previousResult = userReport.getSimple();
            }
            // Return the rank of the user if found
            if (userReport.getUserId().equals(userId)) {
                rankMap.put(rank, userReport);
                return rankMap;
            }
        }
        return null;
    }

    public PreachingLogUserReport mapRankingsObject(List<Object> data) {
        PreachingLogUserReport report = new PreachingLogUserReport();
        // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE DAO
        report.setFruit(data.get(0) != null ? (int) (long) data.get(0) : 0);
        report.setMeaningful(data.get(1) != null ? (int) (long) data.get(1) : 0);
        report.setSimple(data.get(2) != null ? (int) (long) data.get(2) : 0);
        report.setChurchId(data.get(3) != null ? (int) data.get(3) : 0);
        report.setChurchName((String) data.get(4));
        report.setMainChurchId(data.get(5) != null ? (int) data.get(5) : 0);
        report.setUserDisplayName((String) data.get(6));
        report.setUserPictureUrl(data.get(7) != null ? (String) data.get(7) : null);
        report.setUserId(data.get(8) != null ? (int) data.get(8) : 0);
        return report;
    }

    public List<PreachingLogUserReport> reduceRankingsObject(
        List<List<Object>> reportDataList,
        String rankField,
        int searchSkip,
        int searchLimit
    ) {
        final List<PreachingLogUserReport> preachingLogUserReports = new ArrayList<>();
        final int max = searchSkip + searchLimit;

        int rankData = -1;
        int rankValue = -1;
        int i = 0;

        for (List<Object> data : reportDataList) {
            // Run calculations up to the lowest rank returned, then break out of the loop
            if (i > max) {
                break;
            }

            // Map to preaching report
            PreachingLogUserReport report = mapRankingsObject(data);

            // Get value to be used for ranking for the current user
            int currentRankData = -1;
            switch (rankField) {
                case PreachingLogReportType.FRUIT:
                    currentRankData = report.getFruit();
                    break;
                case PreachingLogReportType.MEANINGFUL:
                    currentRankData = report.getMeaningful();
                    break;
                case PreachingLogReportType.SIMPLE:
                    currentRankData = report.getSimple();
                    break;
                default:
                    break;
            }

            // Do not return rankings for data less than or equal to zero
            if (currentRankData <= 0) {
                break;
            }

            // Calculate rankings
            if (rankValue == -1) {
                // First place
                rankValue = 1;
            } else if (rankData != currentRankData) {
                // Rank is lower by one
                rankValue++;
            }

            // Only add logs for reports within the min and max
            if (i >= searchSkip && i < max) {
                report.setRank(rankValue);
                preachingLogUserReports.add(report);
            }

            // Iterate for the next item
            rankData = currentRankData;
            i++;
        }

        return preachingLogUserReports;
    }

    public int sortRankedPreachingLog(PreachingLogUserReport a, PreachingLogUserReport b, String property) {
        switch (property) {
            case PreachingLogReportType.FRUIT:
                if (b.getFruit().equals(a.getFruit())) {
                    return a.getUserDisplayName().compareTo(b.getUserDisplayName());
                }
                return b.getFruit().compareTo(a.getFruit());
            case PreachingLogReportType.MEANINGFUL:
                if (b.getMeaningful().equals(a.getMeaningful())) {
                    return a.getUserDisplayName().compareTo(b.getUserDisplayName());
                }
                return b.getMeaningful().compareTo(a.getMeaningful());
            case PreachingLogReportType.SIMPLE:
                if (b.getSimple().equals(a.getSimple())) {
                    return a.getUserDisplayName().compareTo(b.getUserDisplayName());
                }
                return b.getSimple().compareTo(a.getSimple());
            default:
                return a.getUserDisplayName().compareTo(b.getUserDisplayName());
        }
    }

    public List<PreachingLog.PreachingLogTotals> getTotalsDateBetween(String startDate, String endDate) {
        return preachingLogTotalsDao.getByDateBetween(startDate, endDate);
    }

    private List<PreachingLog> mapMembersToPreachingLogs(Member[] members, List<PreachingLog> preachingLogs) {
        // Create hash maps of the students and the signature reports
        Map<Integer, Boolean> memberIdsMap = new HashMap<>();

        for (Member member : members) {
            memberIdsMap.put(member.getId(), true);
        }

        return preachingLogs
            .stream()
            .filter(preachingLog -> (
                memberIdsMap.containsKey(preachingLog.getUserId1()) ||
                    memberIdsMap.containsKey(preachingLog.getUserId2()) ||
                    memberIdsMap.containsKey(preachingLog.getUserId3()))
            )
            .collect(Collectors.toList());
    }

    private List<PreachingLog> mapMembersToPreachingLogs(List<Member> members, List<PreachingLog> preachingLogs) {
        // Create hash maps of the students and the signature reports
        Map<Integer, Boolean> memberIdsMap = new HashMap<>();

        for (Member member : members) {
            memberIdsMap.put(member.getId(), true);
        }

        return preachingLogs
            .stream()
            .filter(preachingLog -> (
                memberIdsMap.containsKey(preachingLog.getUserId1()) ||
                    memberIdsMap.containsKey(preachingLog.getUserId2()) ||
                    memberIdsMap.containsKey(preachingLog.getUserId3()))
            )
            .collect(Collectors.toList());
    }

    public PreachingLog.PreachingLogTotals sumPreachingLogs(String name, List<PreachingLog> preachingLogs, int members) {
        Integer acquaintances = 0;
        Integer coWorkers = 0;
        Integer family = 0;
        Integer friends = 0;
        Integer neighbors = 0;
        Integer simple = 0;

        for (PreachingLog preachingLog : preachingLogs) {
            acquaintances += preachingLog.getAcquaintances();
            coWorkers += preachingLog.getCoWorkers();
            family += preachingLog.getFamily();
            friends += preachingLog.getFriends();
            neighbors += preachingLog.getNeighbors();
            simple += preachingLog.getSimple();
        }

        PreachingLog.PreachingLogTotals preachingLogTotals = new PreachingLog.PreachingLogTotals();
        preachingLogTotals.setName(name);
        preachingLogTotals.setAcquaintances(acquaintances);
        preachingLogTotals.setCoWorkers(coWorkers);
        preachingLogTotals.setFamily(family);
        preachingLogTotals.setFriends(friends);
        preachingLogTotals.setNeighbors(neighbors);
        preachingLogTotals.setSimple(simple);
        preachingLogTotals.setNumLogs(preachingLogs.size());

        preachingLogTotals.setMembers(members);

        return preachingLogTotals;
    }
}
