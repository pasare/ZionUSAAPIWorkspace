package org.zionusa.admin.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.admin.domain.Announcement;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface AnnouncementDao extends BaseDao<Announcement, Integer> {

    List<Announcement> getAnnouncementsByApprovedIsTrue();

    List<Announcement> getAnnouncementsByApprovedIsTrueAndPublishedIsTrue();

    List<Announcement> getAnnouncementsByTypeId(Integer typeId);

    List<Announcement> getAnnouncementsByTypeIdAndApprovedIsTrue(Integer typeId);

    List<Announcement> getAnnouncementsByTypeIdAndApprovedIsTrueAndPublishedIsTrue(Integer typeId);

    List<Announcement> getAnnouncementsByApprovedIsNull();

    List<Announcement> getAnnouncementsByApprovedIsFalse();

    List<Announcement> getAnnouncementsByTypeIdAndApprovedIsFalse(Integer typeId);

    List<Announcement> getAnnouncementsByTypeIdAndApprovedIsNull(Integer typeId);

    List<Announcement> getAnnouncementsByApprovedIsTrueAndPublishedIsFalse();

    boolean existsAnnouncementByIdAndApprovedIsTrue(Integer id);

    boolean existsAnnouncementByIdAndEditorIdIsNotNull(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Announcement a set a.approved = true, a.approverId = :approverId, a.approverName = :approverName where a.id = :id")
    void setAnnouncementApproved(@Param("id") Integer id, @Param("approverId") Integer approverId, @Param("approverName") String approverName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Announcement a set a.published = true, a.editorId = :editorId, a.editorName = :editorName, a.publishedDate = :publishedDate where a.id = :id")
    void setAnnouncementPublished(@Param("id") Integer id, @Param("editorId") Integer editorId, @Param("editorName") String editorName, @Param("publishedDate") String publishedDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Announcement a set a.approved = false, a.approverId = :approverId, a.approverName = :approverName, a.feedback = :feedback where a.id = :id")
    void setAnnouncementUnapproved(@Param("id") Integer id, @Param("approverId") Integer approverId, @Param("approverName") String approverName, @Param("feedback") String feedback);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Announcement a set a.published = false, a.editorId = null, a.editorName = null, a.publishedDate = null, a.notificationDateAndTime = null where a.id = :id")
    void setAnnouncementUnpublished(@Param("id") Integer id);
}
