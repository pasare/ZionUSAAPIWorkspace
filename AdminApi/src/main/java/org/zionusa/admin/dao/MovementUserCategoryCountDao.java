package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MovementUserCategoryCount;
import org.zionusa.base.dao.BaseDao;

public interface MovementUserCategoryCountDao extends BaseDao<MovementUserCategoryCount, Integer> {
    MovementUserCategoryCount getMovementUserCategoryCountById(Integer id);
}
