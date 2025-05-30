package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao extends BaseDao<Team, Integer> {

    Optional<Team> getTeamByGroupIdAndChurchTeamTrue(Integer id);

    Optional<Team> getTeamByLeaderId(Integer id);

    List<Team> getTeamsByLeaderId(Integer id);

    List<Team> getTeamsByGroupIdAndArchivedIsFalse(Integer groupId);

    List<Team> getTeamsByGroupId(Integer groupId);

    Team getTeamByGroupIdAndChurchTeamIsTrue(Integer groupId);

    List<Team> getTeamByArchivedIsFalseAndChurchTeamIsTrue();
}
