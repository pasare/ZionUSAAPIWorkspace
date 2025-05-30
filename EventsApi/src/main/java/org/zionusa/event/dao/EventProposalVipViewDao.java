package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.EventProposalVipView;

import java.util.List;

public interface EventProposalVipViewDao extends BaseDao<EventProposalVipView, Integer> {

    List<EventProposalVipView> getAllByEventProposalId(Integer eventProposalId);

}
