package org.zionusa.management.domain.application;

import org.zionusa.base.dao.BaseApplicationDao;

import java.util.Optional;

public interface ApplicationDao extends BaseApplicationDao<Application, Integer> {
    Optional<Application> findByUniqueId(String uniqueId);
}
