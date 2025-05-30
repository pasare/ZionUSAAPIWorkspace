package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.Query;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.BibleStudy;

import java.util.List;

public interface BibleStudyDao extends BaseDao<BibleStudy, Integer> {

    List<BibleStudy> findAllByTeacherAvailableNull();

    List<BibleStudy> findByParentChurchIdAndArchivedAndDeniedIsFalse(Integer parentChurchId, Boolean archived);

    List<BibleStudy> findByChurchIdAndArchived(Integer churchId, Boolean archived);

    List<BibleStudy> findByStudentIdAndArchived(Integer studentId, Boolean archived);

    List<BibleStudy> findByStudentIdAndArchivedAndDeniedIsFalse(Integer studentId, Boolean archived);

    List<BibleStudy> findByStudentIdAndArchivedAndDeniedIsFalseAndApprovedIsTrueAndAttendedIsTrue(Integer studentId,
                                                                                                  Boolean archived);

    List<BibleStudy> findByArchivedAndDateBetweenAndDeniedIsFalse(Boolean archived, String startDate, String endDate);

    List<BibleStudy> findByChurchIdAndArchivedAndDateBetweenAndDeniedIsFalse(Integer churchId, Boolean archived, String startDate, String endDate);

//    List<BibleStudy> findByStudentIdAndArchivedAndDateBetweenAndDeniedIsFalse(Integer studentId, Boolean archived, String startDate, String endDate);

    List<BibleStudy> findByStudentIdAndArchivedAndDateBetweenAndDeniedIsFalseAndApprovedIsTrueAndAttendedIsTrue(Integer studentId, Boolean archived, String startDate, String endDate);

    @Query("select distinct count(studyId) from BibleStudy where studentId = ?1 and attended = ?2")
    Integer countDistinctByStudentIdAndAttended(Integer studentId, boolean attended);

}
