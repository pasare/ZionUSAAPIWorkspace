package org.zionusa.event.domain.resultsSurvey;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ResultsSurveyViewDao extends BaseDao<ResultsSurveyView, Integer> {

    List<ResultsSurveyView> getAllByArchive(Boolean archive);

    List<ResultsSurveyView> getAllByArchiveAndBranchId(Boolean archive, Integer branchId);

    List<ResultsSurveyView> getAllById(Integer id);

    ResultsSurveyView getByEventProposalId(Integer id);

}
