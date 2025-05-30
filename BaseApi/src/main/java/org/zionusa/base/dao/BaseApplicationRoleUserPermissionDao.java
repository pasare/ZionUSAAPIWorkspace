package org.zionusa.base.dao;

import org.zionusa.base.domain.ApplicationRoleUserPermission;

import java.util.List;

public interface BaseApplicationRoleUserPermissionDao<T extends ApplicationRoleUserPermission> extends BaseDao<T, Integer> {

    List<T> getAllByUserPermissionName(String permissionName);

}
