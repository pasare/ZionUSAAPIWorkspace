package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.MovementGoalResultAssociationDao;
import org.zionusa.admin.dao.MovementGoalResultChurchDao;
import org.zionusa.admin.dao.MovementGoalResultDao;
import org.zionusa.admin.dao.MovementGoalResultOverseerDao;
import org.zionusa.admin.domain.MovementGoalResult;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class MovementGoalResultService extends BaseService<MovementGoalResult, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(MovementGoalResultService.class);

    private final MovementGoalResultAssociationDao movementGoalResultAssociationDao;
    private final MovementGoalResultChurchDao movementGoalResultChurchDao;
    private final MovementGoalResultOverseerDao movementGoalResultOverseerDao;

    public MovementGoalResultService(MovementGoalResultDao movementGoalResultDao,
                                     MovementGoalResultAssociationDao movementGoalResultAssociationDao,
                                     MovementGoalResultChurchDao movementGoalResultChurchDao,
                                     MovementGoalResultOverseerDao movementGoalResultOverseerDao) {
        super(movementGoalResultDao, logger, MovementGoalResult.class);
        this.movementGoalResultAssociationDao = movementGoalResultAssociationDao;
        this.movementGoalResultChurchDao = movementGoalResultChurchDao;
        this.movementGoalResultOverseerDao = movementGoalResultOverseerDao;
    }

    public List<MovementGoalResult.Association> getAssociationResultsByMovementId(Integer movementId) {
        return movementGoalResultAssociationDao.getAllByMovementId(movementId);
    }

    public List<MovementGoalResult.Church> getChurchResultsByMovementId(Integer movementId) {
        return movementGoalResultChurchDao.getAllByMovementId(movementId);
    }

    public List<MovementGoalResult.Overseer> getOverseerResultsByMovementId(Integer movementId) {
        return movementGoalResultOverseerDao.getAllByMovementId(movementId);
    }
}
