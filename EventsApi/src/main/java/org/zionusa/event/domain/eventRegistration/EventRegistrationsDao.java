package org.zionusa.event.domain.eventRegistration;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.eventRegistration.EventRegistration;

import java.util.List;

public interface EventRegistrationsDao extends BaseDao<EventRegistration, Integer> {

    List<EventRegistration> getEventRegistrationsByEventProposalId(Integer eventProposalId);

    List<EventRegistration> getEventRegistrationsByParticipantId(Integer participantId);

}
