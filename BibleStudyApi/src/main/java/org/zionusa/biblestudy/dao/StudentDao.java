package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.Student;

import java.util.List;

public interface StudentDao extends BaseDao<Student, Integer> {

    List<Student> getStudentsByBaptismDateBetween(String startDate, String endDate);

    List<Student> getAllByLostSheepAndBaptismDateLessThanEqual(Boolean lostSheep, String date);


    @Query("from Student where lostSheep = :lostSheep and (userId1 = :userId1 or userId2 = :userId2 or userId3 = :userId3) and baptismDate <= :date")
    List<Student> getAllByUserId1OrUserId2OrUserId3AndLostSheepAndBaptismDateLessThanEqual(
        @Param("lostSheep") Boolean lostSheep, @Param("userId1") Integer userId1,
        @Param("userId2") Integer userId2, @Param("userId3") Integer userId3, @Param("date") String date);

    List<Student> findByChurchIdAndArchivedIsFalse(Integer churchId);

    List<Student> findByChurchIdAndArchivedIsFalseAndLostSheep(Integer churchId, Boolean lostSheep);

    List<Student> findAllByUserId1OrUserId2OrUserId3(Integer user1Id, Integer user2Id, Integer user3Id);

    List<Student> findByChurchIdOrUserId1OrUserId2OrUserId3AndArchivedIsFalse(
        Integer churchId, Integer user1Id, Integer user2Id, Integer user3Id);
}
