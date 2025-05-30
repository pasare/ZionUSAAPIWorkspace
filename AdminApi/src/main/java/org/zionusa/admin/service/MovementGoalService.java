package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.*;
import org.zionusa.admin.domain.MovementGoal;
import org.zionusa.base.service.BaseService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovementGoalService extends BaseService<MovementGoal, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(MovementGoalService.class);

    private final MovementGoalAssociationDao movementGoalAssociationDao;
    private final MovementGoalChurchDao movementGoalChurchDao;
    private final MovementGoalOverseerDao movementGoalOverseerDao;
    private final MovementGoalShortTermPreachingDao movementGoalShortTermPreachingDao;

    public MovementGoalService(MovementGoalAssociationDao movementGoalAssociationDao,
                               MovementGoalChurchDao movementGoalChurchDao,
                               MovementGoalOverseerDao movementGoalOverseerDao,
                               MovementGoalDao movementGoalDao,
                               MovementGoalShortTermPreachingDao movementGoalShortTermPreachingDao) {
        super(movementGoalDao, logger, MovementGoal.class);
        this.movementGoalAssociationDao = movementGoalAssociationDao;
        this.movementGoalChurchDao = movementGoalChurchDao;
        this.movementGoalOverseerDao = movementGoalOverseerDao;
        this.movementGoalShortTermPreachingDao = movementGoalShortTermPreachingDao;
    }

    public MovementGoal.Association getAssociationByMovementIdAndReferenceId(Integer movementId, Integer associationId) {
        try {
            return movementGoalAssociationDao.getAllByMovementIdAndReferenceId(movementId, associationId);
        } catch (Exception e) {
            return new MovementGoal.Association(associationId, movementId);
        }
    }

    public List<MovementGoal.Association> getAllAssociationsByMovementId(Integer movementId) {
        try {
            return movementGoalAssociationDao.getAllByMovementId(movementId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public MovementGoal.Church getChurchByMovementIdAndReferenceId(Integer movementId, Integer churchId) {
        try {
            return movementGoalChurchDao.getAllByMovementIdAndReferenceId(movementId, churchId);
        } catch (Exception e) {
            return new MovementGoal.Church(churchId, movementId);
        }
    }

    public List<MovementGoal.Church> getAllChurchesByMovementId(Integer movementId) {
        try {
            return movementGoalChurchDao.getAllByMovementId(movementId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public MovementGoal.Overseer getOverseerByMovementIdAndReferenceId(Integer movementId, Integer overseerId) {
        try {
            return movementGoalOverseerDao.getAllByMovementIdAndReferenceId(movementId, overseerId);
        } catch (Exception e) {
            return new MovementGoal.Overseer(overseerId, movementId);
        }
    }

    public List<MovementGoal.Overseer> getAllOverseersByMovementId(Integer movementId) {
        try {
            return movementGoalOverseerDao.getAllByMovementId(movementId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public MovementGoal.ShortTermPreaching geShortTermPreachingByShortTermId(Integer shortTermId) {
        try {
            return movementGoalShortTermPreachingDao.getByReferenceId(shortTermId);
        } catch (Exception e) {
            return new MovementGoal.ShortTermPreaching(shortTermId);
        }
    }
}
