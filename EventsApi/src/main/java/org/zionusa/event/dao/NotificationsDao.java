package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.Notification;

import java.util.List;

public interface NotificationsDao extends BaseDao<Notification, Integer> {

    List<Notification> getAllByProcessedFalse();
}
