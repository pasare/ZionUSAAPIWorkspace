package org.zionusa.base.dao;

import org.zionusa.base.domain.BaseDomain;

import java.util.List;
import java.util.Optional;

public interface BaseApplicationRolePermissionDao<T extends BaseDomain<K>, K> extends BaseDao<T, K> {

    Optional<T> getAllByPermissionNameAndApplicationRoleNameIn(String permissionName, List<String> applicationNames);

}
