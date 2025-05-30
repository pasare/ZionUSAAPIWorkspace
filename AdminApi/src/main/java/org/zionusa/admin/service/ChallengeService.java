package org.zionusa.admin.service;

import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.admin.dao.*;
import org.zionusa.admin.domain.*;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.base.util.notifications.PushNotificationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.zionusa.admin.domain.Announcement.*;

@Service
public class ChallengeService extends BaseService<Challenge, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ChallengeService.class);
    private final ChallengeDao dao;
    private final ChallengeCategoryDao challengeCategoryDao;
    private final ChallengeLogDao challengeLogDao;
    private final GoalLogDao goalLogDao;
    private final NotificationService notificationService;
    private final MessageSource messageSource;
    private final ChallengeActivityDao challengeActivityDao;
    @Value("${notification.key}")
    private String notificationKey;
    @Value("${notification.app.id}")
    private String notificationAppId;

    @Autowired
    public ChallengeService(ChallengeDao dao, ChallengeCategoryDao challengeCategoryDao, ChallengeLogDao challengeLogDao, GoalLogDao goalLogDao, NotificationService notificationService, MessageSource messageSource, ChallengeActivityDao challengeActivityDao) {
        super(dao, logger, Challenge.class);
        this.dao = dao;
        this.challengeCategoryDao = challengeCategoryDao;
        this.challengeLogDao = challengeLogDao;
        this.goalLogDao = goalLogDao;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
        this.challengeActivityDao = challengeActivityDao;
    }

    public List<Challenge> getChallenges(Boolean archived) {
        logger.warn("Getting all approved and pending challenges");
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Challenge> challenges = super.getAll(archived);
        return challenges.stream().filter(challenge -> (challenge.getApproved() != null && challenge.getApproved() && !challenge.isArchived())
            || (challenge.getApproved() == null && challenge.getRequesterId() != null && challenge.getRequesterId().equals(authenticatedUser.getId()))).collect(Collectors.toList());
    }

    public List<ChallengesActivity> getChallengesByActivityId(Integer id) {

        return challengeActivityDao.findAllByActivityId(id);
    }

    public List<Challenge> getApprovedChallenges() {
        return dao.getChallengesByApprovedTrue();
    }

    public List<Challenge> getApprovedChallengesByChurchId(Integer churchId) {
        return dao.getChallengesByApprovedTrueAndChurchIdOrType(churchId, 'A');
    }

    public List<Challenge> getChallengesByCategoryId(Integer id) {
        logger.warn("Getting all challenges by category id {}", id);
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Challenge> challenges = dao.getChallengesByCategoryId(id);
        return challenges.stream().filter(challenge -> (challenge.getApproved() != null && challenge.getApproved() && challenge.isPublished() && !challenge.isArchived())
            || ((challenge.getApproved() == null || challenge.getPublishedDate() == null) && challenge.getRequesterId() != null && challenge.getRequesterId().equals(authenticatedUser.getId()))).collect(Collectors.toList());
    }

    public List<Challenge> getChallengesByMovementId(Integer id) {
        logger.warn("Getting all challenges by movement id {}", id);

        return dao.getChallengesByMovementId(id);
    }

    public List<Challenge> getPendingChallenges() {
        return dao.getChallengesByApprovedIsNull();
    }

    public List<Challenge> getUnapprovedChallenges() {
        return dao.getChallengesByApprovedIsFalse();
    }

    public List<ChallengeCategory> getChallengeCategories() {
        return challengeCategoryDao.findAll();
    }

    @Override
    public Challenge save(Challenge challenge) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean sendNotification = challenge.getId() == null;
        boolean shouldApprove = challenge.getApproved() != null && challenge.getApproved();
        boolean shouldPublish = challenge.isPublished();

        //for now approving all challenges
        shouldApprove = true;
        shouldPublish = true;
        sendNotification = false;

        double points = 0.0;
        BigDecimal pointMultiplier = new BigDecimal(1);

        // for each activity in the challenge increase the point multiplier by .2
        for (Activity activity : challenge.getActivities()) {
            points += activity.getPoints();
            pointMultiplier = pointMultiplier.add(new BigDecimal(".2"));
        }

        challenge.setPoints(points);
        challenge.setPointsMultiplier(pointMultiplier.doubleValue());

        Challenge savedChallenge = dao.save(challenge);

        // handle approval, if previously approved do not approve again
        if (savedChallenge.getId() != null && shouldApprove) {
            boolean alreadyApproved = dao.existsChallengeByIdAndApprovedIsTrue(savedChallenge.getId());
            if (!alreadyApproved) {
                savedChallenge = approveChallenge(savedChallenge.getId());
            }
        }

        // handle publish, if previously published should not publish again
        if (savedChallenge.getId() != null && shouldPublish) {
            boolean alreadyPublished = dao.existsChallengeByIdAndEditorIdIsNotNull(savedChallenge.getId());
            if (!alreadyPublished) {
                savedChallenge = publishChallenge(savedChallenge.getId());
            }
        }

        //create notification that challenge is ready to be approved
        if (sendNotification) saveChallengeAddedNotification(savedChallenge);

        return savedChallenge;
    }

    @Transactional
    public void archive(Integer id) {
        Optional<Challenge> challengeOptional = dao.findById(id);

        if (!challengeOptional.isPresent())
            throw new NotFoundException("Cannot archive a challenge that does not exist");

        Challenge challenge = challengeOptional.get();
        challenge.setArchived(true);

        dao.save(challenge);
    }

    public Challenge approveChallenge(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Challenge> challengeOptional = dao.findById(id);

        if (!challengeOptional.isPresent())
            throw new NotFoundException("Cannot approve an challenge that does not exist");

        Challenge challenge = challengeOptional.get();


        // always approve challenges for now 8/18/2020
        dao.setChallengeApproved(id, authenticatedUser.getId(), authenticatedUser.getDisplayName());
        challenge.setApproved(true);
        challenge.setApproverId(authenticatedUser.getId());
        challenge.setApproverName(authenticatedUser.getDisplayName());
        //saveChallengeApprovedNotification(challenge);

        return challenge;
    }

    public void denyChallenge(Integer id, String feedback) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Challenge> challengeOptional = dao.findById(id);

        if (!challengeOptional.isPresent())
            throw new NotFoundException("Cannot deny a challenge that does not exist");

        Challenge challenge = challengeOptional.get();

        //For security only certain roles can actually do approvals
        final String access = authenticatedUser.getAccess();
        if (EUserAccess.OVERSEER.is(access) || EUserAccess.ADMIN.is(access)) {
            dao.setChallengeUnapproved(id, authenticatedUser.getId(), authenticatedUser.getDisplayName(), feedback);
            challenge.setApproved(false);
            challenge.setApproverId(authenticatedUser.getId());
            challenge.setApproverName(authenticatedUser.getDisplayName());
        } else {
            throw new ForbiddenException("That operation is not allowed");
        }
    }

    public Challenge publishChallenge(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Challenge> challengeOptional = dao.findById(id);

        if (!challengeOptional.isPresent())
            throw new NotFoundException("Cannot publish a challenge that does not exist");

        Challenge challenge = challengeOptional.get();

        //For security only certain roles can actually do publishing

        if (challenge.getApproved() == null || !challenge.getApproved())
            throw new ForbiddenException("This announcement has not been approved yet, publication is not allowed");

        //always publish challenges for now
        String publishedDate = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        dao.setChallengePublished(id, authenticatedUser.getId(), authenticatedUser.getDisplayName(), publishedDate);
        challenge.setEditorId(authenticatedUser.getId());
        challenge.setEditorName(authenticatedUser.getDisplayName());
        challenge.setPublishedDate(publishedDate);
        //saveChallengePublishedNotification(challenge);


        return challenge;
    }

    public void unPublishAnnouncement(Integer id) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Challenge> challengeOptional = dao.findById(id);

        if (!challengeOptional.isPresent())
            throw new NotFoundException("Cannot un-publish a challenge that does not exist");

        Challenge challenge = challengeOptional.get();

        //For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {
            dao.setChallengeUnpublished(id);
            challenge.setEditorId(null);
            challenge.setEditorName(null);
            challenge.setPublishedDate(null);
        }
    }

    @Override
    public void delete(Integer challengeId) {

        List<ChallengeLog> challengeLogs = challengeLogDao.findAllByChallengeId(challengeId);
        List<GoalLog> goalLogs = goalLogDao.findAllByChallengeId(challengeId);

        for (ChallengeLog challengeLog : challengeLogs) {
            challengeLogDao.delete(challengeLog);
        }

        goalLogDao.deleteAll(goalLogs);

        super.delete(challengeId);
    }

    public void saveChallengeAddedNotification(Challenge challenge) {

        String date = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        String titleMessage = "challenge.added.title";
        String subTitleMessage = "challenge.added.subtitle";
        String contentMessage = "challenge.added.content";
        String notificationSource = Notification.ACTIVITIES_SOURCE;
        String notificationSubSource = CHALLENGE_ADDED_SUB_SOURCE;

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        notificationService.saveSystemCreatedNotification(
            challenge.getId(),
            authenticatedUser.getId(),
            null,
            authenticatedUser.getChurchId(),
            authenticatedUser.getGroupId(),
            authenticatedUser.getTeamId(),
            authenticatedUser.getDisplayName(),
            authenticatedUser.getChurchName(),
            date,
            notificationSource,
            notificationSubSource,
            titleMessage,
            subTitleMessage,
            contentMessage,
            challenge.getNotificationDateAndTime()
        );

    }

    public void saveChallengeApprovedNotification(Challenge challenge) {
        String date = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        String notificationSource = Notification.ACTIVITIES_SOURCE;

        // notification for editors to review the challenge
        String titleMessage = "challenge.approved.title";
        String subTitleMessage = "challenge.approved.subtitle";
        String contentMessage = "challenge.approved.content";
        String notificationSubSource = CHALLENGE_APPROVED_SUB_SOURCE;


        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        notificationService.saveSystemCreatedNotification(
            challenge.getId(),
            authenticatedUser.getId(),
            null,
            authenticatedUser.getChurchId(),
            authenticatedUser.getGroupId(),
            authenticatedUser.getTeamId(),
            authenticatedUser.getDisplayName(),
            authenticatedUser.getChurchName(),
            date,
            notificationSource,
            notificationSubSource,
            titleMessage,
            subTitleMessage,
            contentMessage,
            challenge.getNotificationDateAndTime()
        );


    }

    public void saveChallengePublishedNotification(Challenge challenge) {
        String churchTitleMessage = "announcement.general.new.title";
        String churchSubTitleMessage = "announcement.general.new.subtitle";
        String churchContentMessage = "announcement.general.new.content";
        String churchNotificationSubSource = CHALLENGE_NEW_SUB_SOURCE;
        String notificationSource = Notification.ACTIVITIES_SOURCE;

        // notification for all church members that there is a new post
        notificationService.saveSystemCreatedNotification(
            challenge.getId(),
            challenge.getRequesterId(),
            null,
            challenge.getChurchId(),
            challenge.getGroupId(),
            challenge.getTeamId(),
            challenge.getRequesterName(),
            challenge.getChurchName(),
            challenge.getDate(),
            notificationSource,
            churchNotificationSubSource,
            churchTitleMessage,
            churchSubTitleMessage,
            churchContentMessage,
            challenge.getNotificationDateAndTime()
        );


    }

    public void sendChallengeApprovalNeededPushNotification(Notification notification) {
        Optional<Challenge> challengeOptional = dao.findById(notification.getObjectId());

        if (challengeOptional.isPresent()) {
            Challenge challenge = challengeOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{challenge.getName(), notification.getUserName()};

            Integer parentChurchId = notification.getParentChurchId();
            if (parentChurchId == null) parentChurchId = notification.getChurchId();

            // send notification to approver
            List<Filter> filters = notificationService.createOverseerFilters(parentChurchId);
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams
            );

            // send notification to all admins as well
            List<Filter> adminFilters = notificationService.createAdminFilters(notification.getUserId());
            pushNotificationService.createPushNotification(
                adminFilters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams
            );
        }
    }

    public void sendChallengeApprovedPushNotification(Notification notification) {
        Optional<Challenge> challengeOptional = dao.findById(notification.getObjectId());

        if (challengeOptional.isPresent()) {
            Challenge challenge = challengeOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{challenge.getName(), notification.getUserName()};

            // send notification to the owner
            List<Filter> filters = notificationService.createEditorFilters();
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams
            );
        }
    }

    //# not used right so that members are not overwhelmed with alerts
    public void sendChallengeNewPushNotification(Notification notification) {
        Optional<Challenge> challengeOptional = dao.findById(notification.getObjectId());

        if (challengeOptional.isPresent()) {
            Challenge challenge = challengeOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{challenge.getName(), notification.getUserName()};

            // send notification to the church
            List<Filter> filters = notificationService.createChurchFilters(notification.getChurchId());
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams
            );
        }
    }

}
