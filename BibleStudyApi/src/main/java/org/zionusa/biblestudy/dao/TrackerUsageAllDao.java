package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.TrackerUsageAll;

public interface TrackerUsageAllDao extends JpaRepository<TrackerUsageAll, Integer>  {
}
