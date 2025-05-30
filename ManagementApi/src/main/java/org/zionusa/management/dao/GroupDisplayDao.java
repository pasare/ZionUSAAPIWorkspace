package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.Group;

import java.util.List;

public interface GroupDisplayDao extends JpaRepository<Group.DisplayGroup, Integer> {

	List<Group.DisplayGroup> getAllByChurchIdAndArchivedFalse(Integer churchId);
}
