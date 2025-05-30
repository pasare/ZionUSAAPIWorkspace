package org.zionusa.management.domain.userprofile;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface UserProfileSearchViewDao extends BaseDao<UserProfileSearchView, Integer> {
    List<UserProfileSearchView> findAllByBranchIdOrMainBranchIdOrParentBranchId(Integer branchId, Integer mainBranchId, Integer parentBranchId);

    List<UserProfileSearchView> findAllByGroupId(Integer groupId);

    List<UserProfileSearchView> findAllByTeamId(Integer teamId);

}
