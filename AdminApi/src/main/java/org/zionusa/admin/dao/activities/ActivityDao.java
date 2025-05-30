package org.zionusa.admin.dao.activities;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ActivityDao extends BaseDao<Activity, Integer> {
    List<Activity> getActivitiesByApprovedTrue();

    List<Activity> getActivitiesByApprovedIsNull();

    List<Activity> getActivitiesByApprovedIsFalse();

    List<Activity> getActivitiesByCategoryId(Integer id);

    boolean existsActivitiesByIdAndApprovedIsTrue(Integer id);

    boolean existsActivityByIdAndEditorIdIsNotNull(Integer id);

    List<Activity> getActivitiesByApprovedIsTrueAndPublishedIsFalse();

    List<Activity> getActivitiesByCategoryIdAndPublishedAndUseForNotifications(Integer category, Boolean published, Boolean useForNotifications);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Activity a set a.approved = true, a.approverId = :approverId, a.approverName = :approverName where a.id = :id")
    void setActivityApproved(@Param("id") Integer id, @Param("approverId") Integer approverId, @Param("approverName") String approverName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Activity a set a.published = true, a.editorId = :editorId, a.editorName = :editorName, a.publishedDate = :publishedDate where a.id = :id")
    void setActivityPublished(@Param("id") Integer id, @Param("editorId") Integer editorId, @Param("editorName") String editorName, @Param("publishedDate") String publishedDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Activity a set a.approved = false, a.approverId = :approverId, a.approverName = :approverName, a.feedback = :feedback where a.id = :id")
    void setActivityUnapproved(@Param("id") Integer id, @Param("approverId") Integer approverId, @Param("approverName") String approverName, @Param("feedback") String feedback);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Activity a set a.published = false, a.editorId = null, a.editorName = null, a.publishedDate = null, a.notificationDateAndTime = null where a.id = :id")
    void setActivityUnpublished(@Param("id") Integer id);
}
