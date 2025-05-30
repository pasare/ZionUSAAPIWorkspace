package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.RegionsChurches;

import java.util.List;

public interface RegionsChurchesDao extends JpaRepository<RegionsChurches, Integer> {

    List<RegionsChurches> getAllByRegionId(Integer id);
}
