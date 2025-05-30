package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.ShortTermPreaching;

import java.util.List;

public interface ShortTermPreachingDao extends BaseDao<ShortTermPreaching, Integer> {

    List<ShortTermPreaching> getAllByEnabled(Boolean enabled);
}
