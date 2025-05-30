package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.StudentStep;

import java.util.List;

public interface StudentStepDao extends JpaRepository<StudentStep, Integer> {

    List<StudentStep> getAllByDevelopRelationshipNotNullAndUserId1OrUserId2OrUserId3(Integer userId1, Integer userId2, Integer userId3);

    List<StudentStep> getAllByDevelopRelationshipBetweenAndUserId1OrUserId2OrUserId3(
            String startDate, String endDate, Integer userId1, Integer userId2, Integer userId3);

}
