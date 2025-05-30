package org.zionusa.biblestudy.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.Reformer;

import java.util.List;

public interface ReformerDao extends BaseDao<Reformer, Integer> {

    Reformer findByUserIdAndStartDate(Integer userId, String startDate);

    List<Reformer> findAllByParentChurchId(Integer parentChurchId);

    List<Reformer> findAllByChurchId(Integer churchId);

    List<Reformer> findAllByGroupId(Integer groupId);

    List<Reformer> findAllByTeamId(Integer teamId);

    List<Reformer> findAllByStartDateBetween(String startDate, String endDate);

    List<Reformer> findAllByUserId(Integer userId);

    List<Reformer> findAllByUserIdAndStartDateBetween(Integer churchId, String startDate, String endDate);

    List<Reformer> findAllByChurchIdAndStartDateBetween(Integer churchId, String startDate, String endDate);

    List<Reformer> findAllByGroupIdAndStartDateBetween(Integer groupId, String startDate, String endDate);

    List<Reformer> findAllByTeamIdAndStartDateBetween(Integer teamId, String startDate, String endDate);
}
