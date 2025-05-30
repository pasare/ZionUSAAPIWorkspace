package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLogApaReportWeek3;

import java.util.List;

public interface PreachingLogApaReportWeek3Dao extends JpaRepository<PreachingLogApaReportWeek3, Integer> {
    List<PreachingLogApaReportWeek3> findTop300ByOrderByActivityPointsDesc();

    List<PreachingLogApaReportWeek3> findTop300ByOrderByPreachingPointsDesc();

    List<PreachingLogApaReportWeek3> findTop300ByOrderByTeachingPointsDesc();

    List<PreachingLogApaReportWeek3> findTop300ByOrderByTotalPointsDesc();

    // Church Reports
    List<PreachingLogApaReportWeek3> findAllByChurchIdOrderByActivityPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek3> findAllByChurchIdOrderByPreachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek3> findAllByChurchIdOrderByTeachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek3> findAllByChurchIdOrderByTotalPointsDesc(Integer churchId);
}
