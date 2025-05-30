package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.biblestudy.domain.PreachingLogUserShortTermReport;

import java.util.List;

public interface PreachingLogUserShortTermReportDao extends JpaRepository<PreachingLogUserShortTermReport, String> {
    // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE DAO
    @Query("SELECT SUM(p.fruit)     AS fruit," +
            "SUM(p.meaningful)      AS meaningful," +
            "SUM(p.simple)          AS simple," +
            "MAX(p.churchId)        AS churchId," +
            "MAX(p.churchName)      AS churchName," +
            "MAX(p.mainChurchId)    AS mainChurchId," +
            "MAX(p.userDisplayName) AS userDisplayName," +
            "MAX(p.userPictureUrl)  AS userPictureUrl," +
            "p.userId AS userId FROM PreachingLogUserShortTermReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.shortTermId = :shortTermId GROUP BY p.userId")
    List<List<Object>> getRankingsByDateBetween(
            @Param("shortTermId") Integer shortTermId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE DAO
    @Query("SELECT SUM(p.fruit)     AS fruit," +
            "SUM(p.meaningful)      AS meaningful," +
            "SUM(p.simple)          AS simple," +
            "MAX(p.churchId)        AS churchId," +
            "MAX(p.churchName)      AS churchName," +
            "MAX(p.mainChurchId)    AS mainChurchId," +
            "MAX(p.userDisplayName) AS userDisplayName," +
            "MAX(p.userPictureUrl)  AS userPictureUrl," +
            "p.userId AS userId FROM PreachingLogUserShortTermReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.shortTermId = :shortTermId GROUP BY p.userId ORDER BY SUM(p.fruit) DESC NULLS LAST")
    List<List<Object>> getFruitRankingsByDateBetween(
            @Param("shortTermId") Integer shortTermId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE DAO
    @Query("SELECT SUM(p.fruit)     AS fruit," +
            "SUM(p.meaningful)      AS meaningful," +
            "SUM(p.simple)          AS simple," +
            "MAX(p.churchId)        AS churchId," +
            "MAX(p.churchName)      AS churchName," +
            "MAX(p.mainChurchId)    AS mainChurchId," +
            "MAX(p.userDisplayName) AS userDisplayName," +
            "MAX(p.userPictureUrl)  AS userPictureUrl," +
            "p.userId AS userId FROM PreachingLogUserShortTermReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.shortTermId = :shortTermId GROUP BY p.userId ORDER BY SUM(p.meaningful) DESC NULLS LAST")
    List<List<Object>> getMeaningfulRankingsByDateBetween(
            @Param("shortTermId") Integer shortTermId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE DAO
    @Query("SELECT SUM(p.fruit)     AS fruit," +
            "SUM(p.meaningful)      AS meaningful," +
            "SUM(p.simple)          AS simple," +
            "MAX(p.churchId)        AS churchId," +
            "MAX(p.churchName)      AS churchName," +
            "MAX(p.mainChurchId)    AS mainChurchId," +
            "MAX(p.userDisplayName) AS userDisplayName," +
            "MAX(p.userPictureUrl)  AS userPictureUrl," +
            "p.userId AS userId FROM PreachingLogUserShortTermReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.shortTermId = :shortTermId GROUP BY p.userId ORDER BY SUM(p.simple) DESC NULLS LAST")
    List<List<Object>> getSimpleRankingsByDateBetween(
            @Param("shortTermId") Integer shortTermId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
