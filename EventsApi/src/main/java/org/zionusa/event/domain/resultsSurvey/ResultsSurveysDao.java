package org.zionusa.event.domain.resultsSurvey;

import org.zionusa.base.dao.BaseDao;

public interface ResultsSurveysDao extends BaseDao<ResultsSurvey, Integer> {

    ResultsSurvey getResultsSurveyByEventProposalId(Integer proposalId);
}
