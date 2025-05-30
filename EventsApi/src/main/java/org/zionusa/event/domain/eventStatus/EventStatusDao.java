package org.zionusa.event.domain.eventStatus;

import org.zionusa.base.dao.BaseDao;

import java.util.List;
import java.util.Optional;

public interface EventStatusDao extends BaseDao<EventStatus, Integer> {
    Optional<EventStatus> findByEventProposalId(Integer eventProposalId);

    List<EventStatus> findAllByStartedTrueAndFinalizedNull();

    List<EventStatus> findAllByManagerApprovedTrueAndAdminApprovedNull();

    List<EventStatus> findAllByAdminApprovedTrueAndGaApprovedNull();
}
