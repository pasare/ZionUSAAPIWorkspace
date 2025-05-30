package org.zionusa.admin.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.admin.domain.Challenge;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ChallengeDao extends BaseDao<Challenge, Integer> {
    List<Challenge> getChallengesByApprovedTrue();

    List<Challenge> getChallengesByApprovedTrueAndChurchIdOrType(Integer churchId, Character type);

    List<Challenge> getChallengesByApprovedIsNull();

    List<Challenge> getChallengesByApprovedIsFalse();

    List<Challenge> getChallengesByCategoryId(Integer id);

    List<Challenge> getChallengesByMovementId(Integer id);

    boolean existsChallengeByIdAndApprovedIsTrue(Integer id);

    boolean existsChallengeByIdAndEditorIdIsNotNull(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Challenge c set c.approved = true, c.approverId = :approverId, c.approverName = :approverName where c.id = :id")
    void setChallengeApproved(@Param("id") Integer id, @Param("approverId") Integer approverId, @Param("approverName") String approverName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Challenge c set c.published = true, c.editorId = :editorId, c.editorName = :editorName, c.publishedDate = :publishedDate where c.id = :id")
    void setChallengePublished(@Param("id") Integer id, @Param("editorId") Integer editorId, @Param("editorName") String editorName, @Param("publishedDate") String publishedDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Challenge c set c.approved = false, c.approverId = :approverId, c.approverName = :approverName, c.feedback = :feedback where c.id = :id")
    void setChallengeUnapproved(@Param("id") Integer id, @Param("approverId") Integer approverId, @Param("approverName") String approverName, @Param("feedback") String feedback);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Challenge c set cpublished = false, c.editorId = null, c.editorName = null, c.publishedDate = null where c.id = :id")
    void setChallengeUnpublished(@Param("id") Integer id);
}
