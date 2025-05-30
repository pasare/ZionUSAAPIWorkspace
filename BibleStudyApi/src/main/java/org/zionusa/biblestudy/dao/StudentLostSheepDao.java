package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.Student;

import java.util.List;

public interface StudentLostSheepDao extends JpaRepository<Student.LostSheep, Integer> {

    List<Student.LostSheep> findByChurchId(Integer churchId);

}
