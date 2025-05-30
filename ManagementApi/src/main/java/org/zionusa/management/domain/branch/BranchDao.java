package org.zionusa.management.domain.branch;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface BranchDao extends BaseDao<Branch, Integer> {

    List<Branch> getAllByParentBranchId(Integer parentBranchId);

}
