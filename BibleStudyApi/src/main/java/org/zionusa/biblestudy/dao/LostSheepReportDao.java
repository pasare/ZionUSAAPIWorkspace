package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.Student;

public interface LostSheepReportDao extends JpaRepository<Student.LostSheepReport, Integer> {


}
