package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.SealMovementDao;
import org.zionusa.admin.domain.SealMovement;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class SealMovementService extends BaseService<SealMovement, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SealMovementService.class);

    private final SealMovementDao dao;

    public SealMovementService(SealMovementDao dao) {
        super(dao, logger, SealMovement.class);
        this.dao = dao;
    }

    public List<SealMovement> getAll() {return dao.findAll();}

    public List<SealMovement> getByUserId(Integer id) {
        return dao.findAllByUserId(id);
    }

    public List<SealMovement> getByChallengeId(Integer id) {
        return dao.findAllByChallengeId(id);
    }

    public List<SealMovement> getByChallengeIdAndUserId(Integer userId, Integer challengeId) {
        return dao.findAllByUserIdAndChallengeId(userId, challengeId);
    }

}
