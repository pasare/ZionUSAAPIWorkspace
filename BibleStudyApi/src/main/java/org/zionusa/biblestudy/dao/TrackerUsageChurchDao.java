package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.TrackerUsageChurch;

public interface TrackerUsageChurchDao extends JpaRepository<TrackerUsageChurch, Integer>  {
}
