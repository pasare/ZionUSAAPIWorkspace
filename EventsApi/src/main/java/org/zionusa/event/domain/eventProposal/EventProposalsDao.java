package org.zionusa.event.domain.eventProposal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.eventProposal.EventProposal;

import java.util.List;

public interface EventProposalsDao extends BaseDao<EventProposal, Integer> {
    List<EventProposal> getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(boolean isPublished, boolean isPrivate, String proposedEndDate);

    List<EventProposal> getAllByArchived(Boolean archived);

    List<EventProposal> getAllByArchivedAndBranchIdOrWorkflowStatusContains(Boolean archived, Integer branchId, String workflowStatus);

    List<EventProposal> getAllByWorkflowStatus(String workflowStatus);

    EventProposal getEventProposalByIdAndArchived(Integer id, Boolean archived);

    List<EventProposal> getEventProposalByProposedDateAndWorkflowStatusAndArchivedFalse(String date, String workFlowStatus);


    @Modifying
    @Query("update EventProposal p set isPublished = true, publishedDate = :publishedDate, editorId = :editorId, editorName = :editorName where p.id = :id")
    void publishEventProposal(@Param("id") Integer id, @Param("publishedDate") String publishedDate, @Param("editorId") Integer editorId, @Param("editorName") String editorName);

}
