package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.Church;

import java.util.List;

@Deprecated
public interface ChurchDisplayDao extends JpaRepository<Church.DisplayChurch, Integer> {

    List<Church.DisplayChurch> getAllByArchivedFalse();

    List<Church.DisplayChurch> getAllByArchivedFalseAndAssociationId(Integer associationId);
}
