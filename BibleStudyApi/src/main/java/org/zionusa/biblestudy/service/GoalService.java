package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.biblestudy.dao.ChurchStatusGoalDao;
import org.zionusa.biblestudy.domain.ChurchStatusGoal;

import java.util.List;
import java.util.Optional;

@Service
public class GoalService {
    private static final Logger logger = LoggerFactory.getLogger(GoalService.class);

    private final ChurchStatusGoalDao churchStatusGoalDao;

    @Autowired
    public GoalService(ChurchStatusGoalDao churchStatusGoalDao) {
        this.churchStatusGoalDao = churchStatusGoalDao;
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoals() {
        return churchStatusGoalDao.findAll();
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByDateBetween(String startDate, String endDate) {
        return churchStatusGoalDao.findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByChurchId(Integer churchId) {
        return churchStatusGoalDao.findAllByChurchId(churchId);
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByMovementId(Integer movementId) {
        return churchStatusGoalDao.findAllByMovementId(movementId);
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByMovementIdAndChurchId(Integer movementId, Integer churchId) {
        return churchStatusGoalDao.findAllByMovementIdAndChurchId(movementId, churchId);
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByGroupId(Integer groupId) {
        return churchStatusGoalDao.findAllByGroupId(groupId);
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByParentChurchId(Integer churchId) {
        return churchStatusGoalDao.findAllByParentChurchId(churchId);
    }

    public List<ChurchStatusGoal> findAllChurchStatusGoalsByChurchIdAndDateBetween(Integer churchId, String startDate, String endDate) {
        return churchStatusGoalDao.findAllByChurchIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(churchId, startDate, endDate);
    }

    public ChurchStatusGoal saveChurchStatusGoal(Integer churchId, ChurchStatusGoal churchStatusGoal) {
        return churchStatusGoalDao.save(churchStatusGoal);
    }

    public void deleteChurchStatusGoal(Integer churchId, Integer churchStatusGoalId) {
        Optional<ChurchStatusGoal> churchStatusGoalOptional = churchStatusGoalDao.findById(churchStatusGoalId);

        if (!churchStatusGoalOptional.isPresent())
            throw new NotFoundException("The church status goal was not found in the system");

        churchStatusGoalDao.delete(churchStatusGoalOptional.get());
    }
}
