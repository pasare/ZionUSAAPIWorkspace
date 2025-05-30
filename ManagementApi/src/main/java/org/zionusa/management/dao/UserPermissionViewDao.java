package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseUserPermissionDao;
import org.zionusa.management.domain.UserPermissionView;

import java.util.List;

public interface UserPermissionViewDao extends BaseUserPermissionDao<UserPermissionView, Integer> {

    List<UserPermissionView> getAllByUserId(Integer userId);

}
