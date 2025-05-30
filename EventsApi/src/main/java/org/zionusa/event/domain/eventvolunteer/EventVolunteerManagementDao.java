package org.zionusa.event.domain.eventvolunteer;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface EventVolunteerManagementDao extends BaseDao<EventVolunteerManagement, Integer> {

    List<EventVolunteerManagement> getEventVolunteerManagementsByEventProposalId(Integer eventProposalId);

    List<EventVolunteerManagement> getEventVolunteerManagementsByEventProposalIdAndIsForResultsSurvey(Integer eventProposalId, Boolean isForResultsSurvey);

}
