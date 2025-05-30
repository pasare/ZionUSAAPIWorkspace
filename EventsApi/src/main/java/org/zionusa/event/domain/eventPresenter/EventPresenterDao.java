package org.zionusa.event.domain.eventPresenter;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface EventPresenterDao extends BaseDao<EventPresenter, Integer> {

    List<EventPresenter> getAllByEventProposalId (Integer eventProposalId);

    void deleteAllByEventProposalId(Integer eventProposalId);
}
