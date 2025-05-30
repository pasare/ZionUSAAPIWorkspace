package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLogApaReportWeek1;

import java.util.List;

public interface PreachingLogApaReportWeek1Dao extends JpaRepository<PreachingLogApaReportWeek1, Integer> {
    List<PreachingLogApaReportWeek1> findTop300ByOrderByActivityPointsDesc();

    List<PreachingLogApaReportWeek1> findTop300ByOrderByPreachingPointsDesc();

    List<PreachingLogApaReportWeek1> findTop300ByOrderByTeachingPointsDesc();

    List<PreachingLogApaReportWeek1> findTop300ByOrderByTotalPointsDesc();

    // Church Reports
    List<PreachingLogApaReportWeek1> findAllByChurchIdOrderByActivityPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek1> findAllByChurchIdOrderByPreachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek1> findAllByChurchIdOrderByTeachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek1> findAllByChurchIdOrderByTotalPointsDesc(Integer churchId);
}
