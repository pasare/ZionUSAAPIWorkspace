package org.zionusa.event.domain.eventCategory;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.eventCategory.EventCategory;

import java.util.List;

public interface EventCategoryDao extends BaseDao<EventCategory, Integer> {

    List<EventCategory> findAllByActive(Boolean active);

    EventCategory findEventCategoryByTitle(String title);

}
