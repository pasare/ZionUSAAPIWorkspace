package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.PreachingLogApaReportWeek4;

import java.util.List;

public interface PreachingLogApaReportWeek4Dao extends JpaRepository<PreachingLogApaReportWeek4, Integer> {
    List<PreachingLogApaReportWeek4> findTop300ByOrderByActivityPointsDesc();

    List<PreachingLogApaReportWeek4> findTop300ByOrderByPreachingPointsDesc();

    List<PreachingLogApaReportWeek4> findTop300ByOrderByTeachingPointsDesc();

    List<PreachingLogApaReportWeek4> findTop300ByOrderByTotalPointsDesc();

    // Church Reports
    List<PreachingLogApaReportWeek4> findAllByChurchIdOrderByActivityPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek4> findAllByChurchIdOrderByPreachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek4> findAllByChurchIdOrderByTeachingPointsDesc(Integer churchId);

    List<PreachingLogApaReportWeek4> findAllByChurchIdOrderByTotalPointsDesc(Integer churchId);
}
