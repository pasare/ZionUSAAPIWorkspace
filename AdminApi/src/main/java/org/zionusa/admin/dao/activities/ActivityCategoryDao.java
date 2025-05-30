package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.ActivityCategory;
import org.zionusa.base.dao.BaseDao;

import java.util.Optional;

public interface ActivityCategoryDao extends BaseDao<ActivityCategory, Integer> {

    Optional<ActivityCategory> findByAbbreviation(String abbreviation);

}
