package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.EventProposalTableView;

import java.util.List;

public interface EventProposalTableViewDao extends BaseDao<EventProposalTableView, Integer> {

    List<EventProposalTableView> getEventProposalTableViewByProposedDateBetween(String startDate, String endDate);

    List<EventProposalTableView> getEventProposalTableViewByBranchId(Integer branchId);

    List<EventProposalTableView> getEventProposalTableViewByBranchIdAndProposedDateBetween(Integer branchId, String startDate, String endDate);

}
