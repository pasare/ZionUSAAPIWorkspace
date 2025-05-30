package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.SystemConfiguration;

import java.util.List;

public interface SystemConfigurationsDao extends BaseDao<SystemConfiguration, Integer> {

    List<SystemConfiguration> getAllByCategory(String category);
}
