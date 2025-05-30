package org.zionusa.admin.dao;

import org.zionusa.admin.domain.ChallengesActivity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ChallengeActivityDao extends BaseDao<ChallengesActivity, Integer> {
    List<ChallengesActivity> findAllByActivityId(Integer id);
}
