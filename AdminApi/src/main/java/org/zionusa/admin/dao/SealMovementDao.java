package org.zionusa.admin.dao;

import org.zionusa.admin.domain.SealMovement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface SealMovementDao extends BaseDao<SealMovement, Integer> {

    List<SealMovement> findAllByUserId (Integer userId);

    List<SealMovement> findAllByChallengeId (Integer challengeId);

    List<SealMovement> findAllByUserIdAndChallengeId(Integer userId, Integer challengeId);

}
