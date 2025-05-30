package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.PreachingLog;

import java.util.List;

public interface PreachingLogDao extends BaseDao<PreachingLog, Integer> {

    List<PreachingLog> getByDateBetween(String startDate, String endDate);

    List<PreachingLog> getByUserId1AndDateAndIdIsNot(Integer userId1, String date, Integer id);

    List<PreachingLog> getByDateBetweenAndUserId1(String startDate, String endDate, Integer userId1);

    @Query("from PreachingLog where date >= :startDate and date <= :endDate and (userId1 = :userId1 or userId2 = :userId2 or userId3 = :userId3)")
    List<PreachingLog> getByDateBetweenAndUserId1OrUserId2OrUserId3(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("userId1") Integer userId1,
        @Param("userId2") Integer userId2,
        @Param("userId3") Integer userId3
    );

    // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE SERVICE
    @Query("SELECT SUM(p.acquaintances) AS acquaintances," +
        "SUM(p.contacts)   AS contacts," +
        "SUM(p.coWorkers)  AS coWorkers," +
        "SUM(p.family)     AS family," +
        "SUM(p.friends)    AS friends," +
        "SUM(p.fruit)      AS fruit," +
        "SUM(p.meaningful) AS meaningful," +
        "SUM(p.neighbors)  AS neighbors," +
        "SUM(p.simple)     AS simple," +
        "MAX(p.userDisplayName1) AS userDisplayName1," +
        "p.userId1 AS userId1 FROM PreachingLog p WHERE p.date >= :startDate AND p.date <= :endDate GROUP BY p.userId1")
    List<List<Object>> getRankingsByDateBetween(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
}
