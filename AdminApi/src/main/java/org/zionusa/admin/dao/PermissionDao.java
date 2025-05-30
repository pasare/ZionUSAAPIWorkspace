package org.zionusa.admin.dao;

import org.zionusa.admin.domain.Permission;
import org.zionusa.base.dao.BaseDao;

public interface PermissionDao extends BaseDao<Permission, Integer> {

    Permission findPermissionByReferenceAndReferenceId(Permission.Reference reference, Integer referenceId);
}
