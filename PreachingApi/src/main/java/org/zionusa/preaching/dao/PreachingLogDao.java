package org.zionusa.preaching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.preaching.domain.PreachingLog;

import java.util.List;

public interface PreachingLogDao extends JpaRepository<PreachingLog, Integer> {

    List<PreachingLog> getPreachingLogsByUserId1OrUserId2OrUserId3(Integer userId1, Integer userId2, Integer userId3);

    @Query("select p from PreachingLog p where (userId1 = :userId or userId2 = :userId or userId3 = :userId) and date between :startDate and :endDate")
    List<PreachingLog> getByUserIdAndDate(@Param("userId") Integer userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    PreachingLog getPreachingLogByUserId1OrUserId2OrUserId3AndLocationAndDateBetween(Integer userId1, Integer userId2, Integer userId3, String location, String startDate, String endDate);
}
