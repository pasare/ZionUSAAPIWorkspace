package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.ApplicationRole;

import java.util.List;

public interface ApplicationRoleDao extends BaseDao<ApplicationRole, Integer> {

    ApplicationRole findByName(String name);

    List<ApplicationRole> findAllByEnabled(Boolean enabled);

    List<ApplicationRole> findAllByEnabledAndNameContains(Boolean enabled, String contains);

}
