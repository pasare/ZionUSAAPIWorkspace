package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.Team;

import java.util.List;

public interface TeamChurchTeamDao extends JpaRepository<Team.ChurchTeam, Integer> {

    List<Team.ChurchTeam> getAllByAssociationId(Integer associationId);

    List<Team.ChurchTeam> getAllByMainChurchId(Integer mainChurchId);

}
