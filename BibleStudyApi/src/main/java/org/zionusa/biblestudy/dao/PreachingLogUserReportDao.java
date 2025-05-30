package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.biblestudy.domain.PreachingLogUserReport;

import java.util.List;

public interface PreachingLogUserReportDao extends JpaRepository<PreachingLogUserReport, String> {

    List<PreachingLogUserReport> getByDateBetweenAndSimpleIsGreaterThanEqualAndMeaningfulIsGreaterThanEqualAndFruitIsGreaterThanEqual(String startDate,
                                                                                                                                      String endDate,
                                                                                                                                      Integer simpleGoal, Integer meaningfulGoal,
                                                                                                                                      Integer fruitGoal);

    // [WARNING] DO NOT CHANGE THE ORDER. IT MUST MATCH THE ORDER IN THE DAO
    @Query("SELECT SUM(p.fruit)     AS fruit," +
        "SUM(p.meaningful)      AS meaningful," +
        "SUM(p.simple)          AS simple," +
        "MAX(p.churchId)        AS churchId," +
        "MAX(p.churchName)      AS churchName," +
        "MAX(p.mainChurchId)    AS mainChurchId," +
        "MAX(p.userDisplayName) AS userDisplayName," +
        "MAX(p.userPictureUrl)  AS userPictureUrl," +
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate GROUP BY p.userId")
    List<List<Object>> getRankingsByDateBetween(
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.userId = :userId GROUP BY p.userId")
    List<List<Object>> getUserTotalsByDateBetween(
        @Param("userId") Integer userId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate GROUP BY p.userId ORDER BY SUM(p.fruit) DESC NULLS LAST")
    List<List<Object>> getFruitRankingsByDateBetween(
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate GROUP BY p.userId ORDER BY SUM(p.meaningful) DESC NULLS LAST")
    List<List<Object>> getMeaningfulRankingsByDateBetween(
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate GROUP BY p.userId ORDER BY SUM(p.simple) DESC NULLS LAST")
    List<List<Object>> getSimpleRankingsByDateBetween(
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.churchId = :churchId GROUP BY p.userId")
    List<List<Object>> getChurchRankingsByDateBetween(
        @Param("churchId") Integer churchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.churchId = :churchId GROUP BY p.userId ORDER BY SUM(p.fruit) DESC NULLS LAST")
    List<List<Object>> getUserFruitRankingsByChurchAndDateBetween(
        @Param("churchId") Integer churchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.churchId = :churchId GROUP BY p.userId ORDER BY SUM(p.meaningful) DESC NULLS LAST")
    List<List<Object>> getUserMeaningfulRankingsByChurchAndDateBetween(
        @Param("churchId") Integer churchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.churchId = :churchId GROUP BY p.userId ORDER BY SUM(p.simple) DESC NULLS LAST")
    List<List<Object>> getUserSimpleRankingsByChurchAndDateBetween(
        @Param("churchId") Integer churchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.mainChurchId = :mainChurchId GROUP BY p.userId")
    List<List<Object>> getOverseerRankingsByDateBetween(
        @Param("mainChurchId") Integer mainChurchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.mainChurchId = :mainChurchId GROUP BY p.userId ORDER BY SUM(p.fruit) DESC NULLS LAST")
    List<List<Object>> getUserFruitRankingsByOverseerAndDateBetween(
        @Param("mainChurchId") Integer mainChurchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.mainChurchId = :mainChurchId GROUP BY p.userId ORDER BY SUM(p.meaningful) DESC NULLS LAST")
    List<List<Object>> getUserMeaningfulRankingsByOverseerAndDateBetween(
        @Param("mainChurchId") Integer mainChurchId,
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
        "p.userId AS userId FROM PreachingLogUserReport p WHERE p.date >= :startDate AND p.date <= :endDate AND p.mainChurchId = :mainChurchId GROUP BY p.userId ORDER BY SUM(p.simple) DESC NULLS LAST")
    List<List<Object>> getUserSimpleRankingsByOverseerAndDateBetween(
        @Param("mainChurchId") Integer mainChurchId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

}
