package org.zionusa.biblestudy.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.Study;

import java.util.List;

public interface StudyDao extends BaseDao<Study, Integer> {

    Study findByTitle(String title);

    List<Study> findByChapterNumber(Integer chapterNumber);

    List<Study> findByBookNumber(Integer bookNumber);

    List<Study> findByStudyTypeId(Integer sermonTypeId);

    List<Study> findByStudyCategoryId(Integer studyCategoryId);
}
