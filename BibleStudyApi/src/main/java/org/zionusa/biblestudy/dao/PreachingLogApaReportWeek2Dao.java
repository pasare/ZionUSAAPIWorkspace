package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLogApaReportWeek2;

import java.util.List;

public interface PreachingLogApaReportWeek2Dao extends JpaRepository<PreachingLogApaReportWeek2, Integer> {
    List<PreachingLogApaReportWeek2> findTop300ByOrderByActivityPointsDesc();

    List<PreachingLogApaReportWeek2> findTop300ByOrderByPreachingPointsDesc();

    List<PreachingLogApaReportWeek2> findTop300ByOrderByTeachingPointsDesc();

    List<PreachingLogApaReportWeek2> findTop300ByOrderByTotalPointsDesc();

    // Church Reports
    List<PreachingLogApaReportWeek2> findAllByChurchIdOrderByActivityPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek2> findAllByChurchIdOrderByPreachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek2> findAllByChurchIdOrderByTeachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek2> findAllByChurchIdOrderByTotalPointsDesc(Integer churchId);
}
