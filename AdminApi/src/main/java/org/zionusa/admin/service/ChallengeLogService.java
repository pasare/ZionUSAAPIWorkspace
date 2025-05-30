package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zionusa.admin.dao.ChallengeLogDao;
import org.zionusa.admin.dao.activities.ActivityLogDao;
import org.zionusa.admin.domain.ChallengeLog;
import org.zionusa.base.domain.Member;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Deprecated
@Service
public class ChallengeLogService extends BaseService<ChallengeLog, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ChallengeLogService.class);

    private final ChallengeLogDao dao;
    private final ActivityLogDao activityLogDao;
    private final RestService restService;

    @Autowired
    public ChallengeLogService(ChallengeLogDao dao, ActivityLogDao activityLogDao, RestService restService) {
        super(dao, logger, ChallengeLog.class);
        this.dao = dao;
        this.activityLogDao = activityLogDao;
        this.restService = restService;
    }

    public List<ChallengeLog> getChallengeLogsByDateBetween(String startDate, String endDate) {
        return dao.getChallengeLogsByDateBetween(startDate, endDate);
    }

    public List<ChallengeLog> getChallengeLogsByUserIdAndDateBetween(Integer userId, String startDate, String endDate) {
        return dao.getChallengeLogsByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public List<ChallengeLog> getChallengeLogsByUserId(Integer userId) {
        return dao.getChallengeLogsByUserId(userId);
    }

    @Override
    public ChallengeLog save(ChallengeLog challengeLog) {

        //create challenge logs for all group members, if the option is selected
        if (challengeLog.isAllGroup() && challengeLog.getId() == null) {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            for (Member groupMember : restService.getGroupMembers(request, authenticatedUser.getGroupId())) {

                if (!authenticatedUser.getId().equals(groupMember.getId())) {
                    ChallengeLog memberChallengeLog = new ChallengeLog();
                    memberChallengeLog.setChallenge(challengeLog.getChallenge());
                    memberChallengeLog.setCompleted(false);
                    memberChallengeLog.setDate(challengeLog.getDate());
                    memberChallengeLog.setChallengeId(challengeLog.getChallengeId());
                    memberChallengeLog.setAllGroup(false);
                    memberChallengeLog.setUserId(groupMember.getId());
                    dao.save(memberChallengeLog);
                }
            }
        }

        return super.save(challengeLog);
    }
}
