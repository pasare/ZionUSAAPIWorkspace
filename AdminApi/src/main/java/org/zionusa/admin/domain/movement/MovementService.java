package org.zionusa.admin.domain.movement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.*;
import org.zionusa.admin.domain.*;
import org.zionusa.admin.domain.branch.BranchView;
import org.zionusa.admin.domain.branch.BranchViewService;
import org.zionusa.admin.service.RestService;
import org.zionusa.base.domain.Member;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovementService extends BaseService<Movement, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MovementService.class);
    private final RestService restService;

    private final BranchViewService branchViewService;
    private final MovementDao dao;
    private final MovementUserActivityDao movementUserActivityDao;
    private final MovementGroupActivityDao movementGroupActivityDao;
    private final MovementChurchActivityDao movementChurchActivityDao;
    private final MovementChurchDateActivityDao movementChurchDateActivityDao;
    private final MovementEastCoastActivityDao movementEastCoastActivityDao;
    private final MovementUserCategoryCountDao movementEastCoastCategoryCountDao;
    private final MovementUserLevelDao movementUserLevelDao;
    private final MovementUserLevelCountDao movementUserLevelCountDao;
    private final MovementBranchBaptismsLeaderInputDao movementBranchBaptismsLeaderInputDao;
    private final MovementGroupBaptismsLeaderInputDao movementGroupBaptismsLeaderInputDao;
    private final MovementMainBranchBaptismsLeaderInputDao movementMainBranchBaptismsLeaderInputDao;
    private final MovementBranchBaptismsLeaderInputViewDao movementBranchBaptismsLeaderInputViewDao;
    private final MovementMainBranchBaptismsLeaderInputViewDao movementMainBranchBaptismsLeaderInputViewDao;

    @Autowired
    public MovementService(BranchViewService branchViewService,
                           MovementDao dao,
                           MovementUserActivityDao movementUserActivityDao,
                           MovementGroupActivityDao movementGroupActivityDao,
                           MovementChurchActivityDao movementChurchActivityDao,
                           MovementChurchDateActivityDao movementChurchDateActivityDao,
                           MovementUserCategoryCountDao movementEastCoastCategoryCountDao,
                           MovementEastCoastActivityDao movementEastCoastActivityDao,
                           MovementUserLevelDao movementUserLevelDao,
                           MovementUserLevelCountDao movementUserLevelCountDao,
                           RestService restService,
                           MovementBranchBaptismsLeaderInputDao movementBranchBaptismsLeaderInputDao,
                           MovementGroupBaptismsLeaderInputDao movementGroupBaptismsLeaderInputDao,
                           MovementMainBranchBaptismsLeaderInputDao movementMainBranchBaptismsLeaderInputDao,
                           MovementBranchBaptismsLeaderInputViewDao movementBranchBaptismsLeaderInputViewDao,
                           MovementMainBranchBaptismsLeaderInputViewDao movementMainBranchBaptismsLeaderInputViewDao) {
        super(dao, logger, Movement.class);
        this.branchViewService = branchViewService;
        this.dao = dao;
        this.movementUserActivityDao = movementUserActivityDao;
        this.movementGroupActivityDao = movementGroupActivityDao;
        this.movementChurchActivityDao = movementChurchActivityDao;
        this.movementChurchDateActivityDao = movementChurchDateActivityDao;
        this.movementEastCoastCategoryCountDao = movementEastCoastCategoryCountDao;
        this.movementEastCoastActivityDao = movementEastCoastActivityDao;
        this.movementUserLevelDao = movementUserLevelDao;
        this.movementUserLevelCountDao = movementUserLevelCountDao;
        this.restService = restService;
        this.movementBranchBaptismsLeaderInputDao = movementBranchBaptismsLeaderInputDao;
        this.movementGroupBaptismsLeaderInputDao = movementGroupBaptismsLeaderInputDao;
        this.movementMainBranchBaptismsLeaderInputDao = movementMainBranchBaptismsLeaderInputDao;
        this.movementBranchBaptismsLeaderInputViewDao = movementBranchBaptismsLeaderInputViewDao;
        this.movementMainBranchBaptismsLeaderInputViewDao = movementMainBranchBaptismsLeaderInputViewDao;
    }

    public Movement getCurrentMovement() {
        Movement movement = dao.getFirstByActiveIsTrue();
        movement.setParticipantCount(movementUserActivityDao.countDistinctByUserIdAndStartDateEqualsAndEndDateEquals(movement.getStartDate(), movement.getEndDate()));
        return dao.getFirstByActiveIsTrue();
    }

    public List<MovementUserActivity> getMovementUserActivity(Integer movementId, Integer userId) {
        return movementUserActivityDao.getMovementUserActivitiesByMovementIdAndUserId(movementId, userId);
    }

    public List<MovementGroupActivity> getMovementGroupActivity(Integer movementId, Integer groupId) {
        return movementGroupActivityDao.getMovementGroupActivitiesByMovementIdAndGroupId(movementId, groupId);
    }

    public List<MovementChurchActivity> getMovementChurchActivity(Integer movementId, Integer churchId) {
        return movementChurchActivityDao.getMovementChurchActivitiesByMovementIdAndChurchId(movementId, churchId);
    }

    public List<MovementChurchDateActivity> getMovementChurchActivityByDateBetween(Integer movementId, Integer churchId, String startDate, String endDate) {
        return movementChurchDateActivityDao.getMovementChurchDateActivitiesByMovementIdAndChurchIdAndActivityDateBetween(movementId, churchId, startDate, endDate);
    }

    public List<MovementEastCoastActivity> getMovementEastCoastActivity(Integer movementId) {
        return movementEastCoastActivityDao.getMovementEastCoastActivitiesByMovementId(movementId);
    }

    public MovementUserCategoryCount getEastCoastMovementCategoryCountReport(Integer movementId) {
        return movementEastCoastCategoryCountDao.getMovementUserCategoryCountById(movementId);
    }

    public List<MovementUserLevelCount> getUserMovementLevelCountEastCoastReport(Integer movementId) {
        // Get the full levels report for the movement
        return movementUserLevelCountDao.getMovementUserLevelByMovementId(movementId);
    }

    public List<MovementUserLevelReport> getUserMovementLevelEastCoastReport(HttpServletRequest request, Integer movementId) {
        // Get the full levels report for the movement
        List<MovementUserLevel> userLevels = movementUserLevelDao.getTop300MovementUserLevelByMovementIdOrderByLevelDescTotalCountDesc(movementId);

        // Get the all members of the church
        Member[] members = restService.getAllDisplayMembers(request);

        // Filter levels reports by members in the church
        return getMovementUserLevels(members, userLevels);
    }

    public List<MovementUserLevelReport> getUserMovementLevelChurchReport(HttpServletRequest request, Integer movementId, Integer churchId) {
        // Get the full levels report for the movement
        List<MovementUserLevel> userLevels = movementUserLevelDao.getMovementUserLevelByMovementId(movementId);

        // Get the current members of the church
        Member[] members = restService.getAllDisplayChurchMembers(request, churchId);

        // Filter levels reports by members in the church
        return getMovementUserLevels(members, userLevels);
    }

    public List<MovementUserLevelReport> getUserMovementLevelGroupReport(HttpServletRequest request, Integer movementId, Integer groupId) {
        // Get the full levels report for the movement
        List<MovementUserLevel> userLevels = movementUserLevelDao.getMovementUserLevelByMovementId(movementId);

        // Get the current members of the group
        Member[] members = restService.getAllDisplayGroupMembers(request, groupId);

        // Filter levels reports by members in the group
        return getMovementUserLevels(members, userLevels);
    }

    public List<MovementUserLevelReport> getUserMovementLevelTeamReport(HttpServletRequest request, Integer movementId, Integer teamId) {
        // Get the full levels report for the movement
        List<MovementUserLevel> userLevels = movementUserLevelDao.getMovementUserLevelByMovementId(movementId);

        // Get the current members of the team
        Member[] members = restService.getAllDisplayTeamMembers(request, teamId);
        logger.debug("Found {} members in group {}", members.length, teamId);

        // Filter levels reports by members in the team
        return getMovementUserLevels(members, userLevels);
    }

    public List<MovementUserLevelReport> getUserMovementLevelUserReport(HttpServletRequest request, Integer movementId, Integer userId) throws NotFoundException {
        // Get the full levels report for the movement
        List<MovementUserLevel> userLevels = movementUserLevelDao.getMovementUserLevelByMovementIdAndUserId(movementId, userId);

        // Get the current members of the team
        Member member = restService.getDisplayMember(request, userId);

        if (member == null) {
            throw new NotFoundException("No user level report exists.");
        }

        // Filter levels reports by members in the team
        return getMovementUserLevels(new Member[]{member}, userLevels);
    }

    private List<MovementUserLevelReport> getMovementUserLevels(Member[] members, List<MovementUserLevel> movementUserLevels) {
        Map<Integer, MovementUserLevel> movementUserLevelMap = new HashMap<>();

        for (MovementUserLevel movementUserLevel : movementUserLevels) {
            movementUserLevelMap.put(movementUserLevel.getUserId(), movementUserLevel);
        }

        List<MovementUserLevelReport> memberMovementUserLevels = new ArrayList<>();

        for (Member member : members) {
            // Only include members with a report in the response
            if (movementUserLevelMap.containsKey(member.getId())) {
                memberMovementUserLevels.add(new MovementUserLevelReport(movementUserLevelMap.get(member.getId()), member));
            }
        }

        return memberMovementUserLevels;
    }

    public List<Movement.BranchBaptismsLeaderInputView> getMovementsBranchBaptisms(Integer movementId) {
        return movementBranchBaptismsLeaderInputViewDao.getAllByMovementId(movementId);
    }

    public Integer getMovementsBranchBaptismCount(Integer movementId, Integer branchId) {
        Movement.BranchBaptismsLeaderInputView branchBaptismsLeaderInputView =
            movementBranchBaptismsLeaderInputViewDao.getByMovementIdAndBranchId(movementId, branchId);

        if (branchBaptismsLeaderInputView != null && branchBaptismsLeaderInputView.getBaptisms() != null) {
            return branchBaptismsLeaderInputView.getBaptisms();
        }

        return 0;
    }

    public List<Movement.MainBranchBaptismsLeaderInputView> getMovementsMainBranchBaptisms(Integer movementId) {
        return movementMainBranchBaptismsLeaderInputViewDao.getAllByMovementId(movementId);
    }

    public Integer getMovementsMainBranchBaptismCount(Integer movementId, Integer branchId) {
        Movement.MainBranchBaptismsLeaderInputView mainBranchBaptismsLeaderInputView =
            movementMainBranchBaptismsLeaderInputViewDao.getByMovementIdAndBranchId(movementId, branchId);

        if (mainBranchBaptismsLeaderInputView != null && mainBranchBaptismsLeaderInputView.getBaptisms() != null) {
            return mainBranchBaptismsLeaderInputView.getBaptisms();
        }

        return 0;
    }

    public int getTotalMovementBaptisms(Integer movementId) {
        if (movementId == null) {
            Movement movement = getCurrentMovement();
            movementId = movement.getId();
        }

        List<Movement.MainBranchBaptismsLeaderInputView> mainBranchBaptismsLeaderInputViews =
            getMovementsMainBranchBaptisms(movementId);

        int totalBaptisms = 0;
        for (Movement.MainBranchBaptismsLeaderInputView mainBranchBaptismsLeaderInputView :
            mainBranchBaptismsLeaderInputViews) {
            totalBaptisms += mainBranchBaptismsLeaderInputView.getBaptisms();
        }
        return totalBaptisms;
    }

    public List<Movement.GroupBaptismsLeaderInput> getTotalBaptismsByGroup(String startDate, String endDate) {
        List<Movement.GroupBaptismsLeaderInput> results =
            movementGroupBaptismsLeaderInputDao.getByDateBetween(startDate, endDate);
        if (results.isEmpty()) {
            return results;
        }
        Map<Integer, Movement.GroupBaptismsLeaderInput> groupBaptismsLeaderInputMap = new HashMap<>();

        for (Movement.GroupBaptismsLeaderInput result : results) {
            final Integer groupId = result.getGroupId();
            if (groupBaptismsLeaderInputMap.containsKey(groupId)) {
                final Integer baptisms = groupBaptismsLeaderInputMap.get(groupId).getBaptisms();
                groupBaptismsLeaderInputMap.get(groupId)
                    .setBaptisms(baptisms + (result.getBaptisms() == null ? 0 : result.getBaptisms()));
            } else {
                result.setBaptisms(result.getBaptisms() == null ? 0 : result.getBaptisms());
                result.setDate(null);
                result.setMovementId(null);
                groupBaptismsLeaderInputMap.put(groupId, result);
            }
        }

        return new ArrayList<>(groupBaptismsLeaderInputMap.values());
    }

    public List<Movement.BranchBaptismsLeaderInput> getTotalBaptismsByBranch(String startDate, String endDate) {
        List<Movement.BranchBaptismsLeaderInput> results = movementBranchBaptismsLeaderInputDao.getByDateBetween(startDate, endDate);

        if (results.isEmpty()) {
            return results;
        }

        // Get list of branches for results
        List<BranchView> branchViews = branchViewService.getAll(false);

        Map<Integer, BranchView> branchViewHashMap = new HashMap<>();

        for (BranchView branchView : branchViews) {
            branchViewHashMap.putIfAbsent(branchView.getId(), branchView);
        }

        Map<Integer, Movement.BranchBaptismsLeaderInput> branchBaptismsLeaderInputMap = new HashMap<>();

        for (Movement.BranchBaptismsLeaderInput result : results) {
            final Integer branchId = result.getBranchId();
            if (branchBaptismsLeaderInputMap.containsKey(branchId)) {
                final Integer baptisms = branchBaptismsLeaderInputMap.get(branchId).getBaptisms();
                branchBaptismsLeaderInputMap.get(result.getBranchId())
                    .setBaptisms(baptisms + (result.getBaptisms() == null ? 0 : result.getBaptisms()));
            } else {
                final BranchView branchView = branchViewHashMap.get(branchId);
                final Integer mainBranchId = branchView.getMainBranchId();
                final BranchView mainBranchView = mainBranchId == null
                    ? branchView
                    : branchViewHashMap.get(mainBranchId);
                // Update properties for report
                result.setBaptisms(result.getBaptisms() == null ? 0 : result.getBaptisms());
                result.setDate(null);
                result.setBranchName(branchView.getName());
                result.setMainBranchId(branchView.getMainBranchId());
                result.setMainBranchName(mainBranchView.getName());
                result.setMovementId(null);
                branchBaptismsLeaderInputMap.put(branchId, result);
            }
        }

        return new ArrayList<>(branchBaptismsLeaderInputMap.values());
    }

    public List<Movement.MainBranchBaptismsLeaderInput> getTotalBaptismsByMainBranch(String startDate, String endDate) {
        return movementMainBranchBaptismsLeaderInputDao.getByDateBetween(startDate, endDate);
    }

    public Movement.GroupBaptismsLeaderInput getMovementsGroupTodaysBaptisms(Integer movementId, Integer groupId,
                                                                          String date) {
        Movement.GroupBaptismsLeaderInput baptismData =
            movementGroupBaptismsLeaderInputDao.getByMovementIdAndGroupIdAndDate(movementId,
            groupId, date);

        if (baptismData == null) {
            Movement.GroupBaptismsLeaderInput emptyBaptism = new Movement.GroupBaptismsLeaderInput();
            emptyBaptism.setBaptisms(0);
            emptyBaptism.setGroupId(groupId);
            emptyBaptism.setDate(date);
            emptyBaptism.setMovementId(movementId);
            return emptyBaptism;
        }
        return baptismData;
    }

    public Movement.BranchBaptismsLeaderInput getMovementsBranchTodaysBaptisms(Integer movementId, Integer branchId, String date) {
        Movement.BranchBaptismsLeaderInput baptismData = movementBranchBaptismsLeaderInputDao.getByMovementIdAndBranchIdAndDate(movementId,
            branchId, date);

        if (baptismData == null) {
            Movement.BranchBaptismsLeaderInput emptyBaptism = new Movement.BranchBaptismsLeaderInput();
            emptyBaptism.setBaptisms(0);
            emptyBaptism.setBranchId(branchId);
            emptyBaptism.setDate(date);
            emptyBaptism.setMovementId(movementId);
            return emptyBaptism;
        }
        return baptismData;
    }

    public Movement.MainBranchBaptismsLeaderInput getMovementsMainBranchTodaysBaptisms(Integer movementId, Integer branchId, String date) {
        Movement.MainBranchBaptismsLeaderInput baptismData =
            movementMainBranchBaptismsLeaderInputDao.getByMovementIdAndBranchIdAndDate(movementId, branchId, date);

        if (baptismData == null) {
            Movement.MainBranchBaptismsLeaderInput emptyBaptism = new Movement.MainBranchBaptismsLeaderInput();
            emptyBaptism.setBaptisms(0);
            emptyBaptism.setBranchId(branchId);
            emptyBaptism.setDate(date);
            emptyBaptism.setMovementId(movementId);
            return emptyBaptism;
        }

        return baptismData;
    }

    public String getAssociation(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        switch (dayOfWeek) {
            case SUNDAY:
                return "Northeast Association";
            case MONDAY:
                return "Southeast Association";
            case TUESDAY:
                return "North Central Association";
            case WEDNESDAY:
                return "South Central Association";
            case THURSDAY:
                return "West Central Association";
            case FRIDAY:
                return "Northwest Association";
            case SATURDAY:
                return "Southwest Association";
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
    }

    public void saveGroupBaptismsLeaderInput(Movement.GroupBaptismsLeaderInput groupBaptismsLeaderInput) {
        // Find existing one to update baptism data
        Movement.GroupBaptismsLeaderInput previous =
            movementGroupBaptismsLeaderInputDao.getByMovementIdAndGroupIdAndDate(
                groupBaptismsLeaderInput.getMovementId(),
                groupBaptismsLeaderInput.getBranchId(),
                groupBaptismsLeaderInput.getDate());

        if (previous != null) {
            int newBaptismCount = groupBaptismsLeaderInput.getBaptisms();
            previous.setBaptisms(newBaptismCount);
            movementGroupBaptismsLeaderInputDao.save(previous);
        } else {
            movementGroupBaptismsLeaderInputDao.save(groupBaptismsLeaderInput);
        }
    }

    public void saveBranchBaptismsLeaderInput(Movement.BranchBaptismsLeaderInput branchBaptismsLeaderInput) {
        // Find existing one to update baptism data
        Movement.BranchBaptismsLeaderInput previous = movementBranchBaptismsLeaderInputDao.getByMovementIdAndBranchIdAndDate(
            branchBaptismsLeaderInput.getMovementId(),
            branchBaptismsLeaderInput.getBranchId(),
            branchBaptismsLeaderInput.getDate());

        if (previous != null) {
//            int previousBaptisms = previous.getBaptisms();
//            int newBaptismCount = branchBaptismsLeaderInput.getBaptisms() + previousBaptisms;
            int newBaptismCount = branchBaptismsLeaderInput.getBaptisms();

            previous.setBaptisms(newBaptismCount);
            movementBranchBaptismsLeaderInputDao.save(previous);
        } else {
            movementBranchBaptismsLeaderInputDao.save(branchBaptismsLeaderInput);
        }
    }

    public void saveMainBranchBaptismsLeaderInput(Movement.MainBranchBaptismsLeaderInput mainBranchBaptismsLeaderInput) {
        // Find existing one to update baptism data
        Movement.MainBranchBaptismsLeaderInput previous = movementMainBranchBaptismsLeaderInputDao.getByMovementIdAndBranchIdAndDate(
            mainBranchBaptismsLeaderInput.getMovementId(),
            mainBranchBaptismsLeaderInput.getBranchId(),
            mainBranchBaptismsLeaderInput.getDate());

        if (previous != null) {
            int previousBaptisms = previous.getBaptisms();
            int newBaptismCount = mainBranchBaptismsLeaderInput.getBaptisms() + previousBaptisms;
            previous.setBaptisms(newBaptismCount);
            movementMainBranchBaptismsLeaderInputDao.save(previous);
        } else {
            movementMainBranchBaptismsLeaderInputDao.save(mainBranchBaptismsLeaderInput);
        }
    }


}
