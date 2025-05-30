package org.zionusa.admin.dao;

import org.zionusa.admin.domain.Notification;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface NotificationDao extends BaseDao<Notification, Integer> {

    List<Notification> findByChurchId(Integer churchId);

    List<Notification> findByChurchIdAndDateBetween(Integer churchId, String startDate, String endDate);

    List<Notification> findByParentChurchId(Integer parentChurchId);

    List<Notification> findByParentChurchIdAndDateBetween(Integer parentChurchId, String startDate, String endDate);

    List<Notification> findBySource(String source);

    List<Notification> findBySourceAndParentChurchId(String source, Integer parentChurchId);

    List<Notification> findBySourceAndChurchId(String source, Integer churchId);

    List<Notification> findBySourceAndGroupId(String source, Integer groupId);

    List<Notification> findBySourceAndTeamId(String source, Integer teamId);

    List<Notification> findBySourceAndUserId(String source, Integer userId);

    List<Notification> findBySourceAndDateBetween(String source, String startDate, String endDate);

    List<Notification> getAllByProcessedFalse();
}
