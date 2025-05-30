package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.UserPreference;

import java.util.List;

public interface UserPreferencesDao extends BaseDao<UserPreference, Integer> {

    List<UserPreference> getAllByUserId(Integer userId);
}
