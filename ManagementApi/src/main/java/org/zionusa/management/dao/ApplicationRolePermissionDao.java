package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseApplicationRolePermissionDao;
import org.zionusa.management.domain.ApplicationRolePermission;

import java.util.List;
import java.util.Optional;

public interface ApplicationRolePermissionDao extends BaseApplicationRolePermissionDao<ApplicationRolePermission, Integer> {

    Optional<ApplicationRolePermission> findApplicationRolePermissionByApplicationRoleIdAndPermissionId(Integer applicationRoleId, Integer permissionId);

    List<ApplicationRolePermission> getAllByApplicationRoleId(Integer id);
}
