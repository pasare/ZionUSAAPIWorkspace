package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.EventProposalVip;

import java.util.List;

public interface EventProposalVipDao extends BaseDao<EventProposalVip, Integer> {

    List<EventProposalVip> getAllByContactId(Integer contactId);

    List<EventProposalVip> getAllByEventProposalId(Integer eventProposalId);

    EventProposalVip getAllByContactIdAndEventProposalId(Integer contactId, Integer eventProposalId);

}
