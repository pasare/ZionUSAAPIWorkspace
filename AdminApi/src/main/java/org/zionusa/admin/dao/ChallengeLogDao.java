package org.zionusa.admin.dao;

import org.zionusa.admin.domain.ChallengeLog;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ChallengeLogDao extends BaseDao<ChallengeLog, Integer> {

    List<ChallengeLog> getChallengeLogsByChallengeId(Integer challengeId);

    List<ChallengeLog> findAllByChallengeId(Integer challengeId);

    List<ChallengeLog> getChallengeLogsByDateBetween(String startDate, String endDate);

    List<ChallengeLog> getChallengeLogsByUserIdAndDateBetween(Integer userId, String startDate, String endDate);

    List<ChallengeLog> getChallengeLogsByUserId(Integer userId);

    Integer countChallengeLogsByUserIdAndDateIsBetween(Integer userId, String startDate, String endDate);

    Integer countChallengeLogsByUserIdAndDateIsBetweenAndCompletedTrue(Integer userId, String startDate, String endDate);
}
