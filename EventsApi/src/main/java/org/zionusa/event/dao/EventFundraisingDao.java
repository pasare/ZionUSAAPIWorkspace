package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.EventFundraising;

import java.util.List;

public interface EventFundraisingDao extends BaseDao<EventFundraising, Integer> {

    List<EventFundraising> getEventFundraisingsByEventId(Integer eventProposalId);

    List<EventFundraising> getEventFundraisingsByUserId(Integer userId);

    EventFundraising getEventFundraisingByEventIdAndUserId(Integer eventProposalId, Integer UserId);
}
