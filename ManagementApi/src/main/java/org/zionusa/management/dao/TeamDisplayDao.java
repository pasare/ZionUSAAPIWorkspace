package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.Team;

import java.util.List;

public interface TeamDisplayDao extends JpaRepository<Team.DisplayTeam, Integer> {

	List<Team.DisplayTeam> getAllByGroupIdAndArchivedFalse(Integer groupId);
}
