package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.EventMedia;

import java.util.List;

public interface EventMediaDao extends BaseDao<EventMedia, Integer> {

    List<EventMedia> findAllByEventProposalIdAndArchived(Integer eventProposalId, Boolean archived);

    List<EventMedia> findAllByResultsSurveyIdAndArchived(Integer resultsSurveyId, Boolean archived);

}
