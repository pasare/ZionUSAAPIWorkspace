package org.zionusa.biblestudy.service;


import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.base.util.notifications.PushNotificationService;
import org.zionusa.biblestudy.dao.BibleStudyDao;
import org.zionusa.biblestudy.dao.StudentDao;
import org.zionusa.biblestudy.dao.StudyDao;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.domain.Notification;
import org.zionusa.biblestudy.domain.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BibleStudyService extends BaseService<BibleStudy, Integer> {
    private static final Logger log = LoggerFactory.getLogger(BibleStudyService.class);
    private final MessageSource messageSource;
    private final BibleStudyDao bibleStudyDao;
    private final StudentDao studentDao;
    private final StudyDao studyDao;
    private final NotificationService notificationService;
    @Value("${notification.key}")
    private String notificationKey;
    @Value("${notification.app.id}")
    private String notificationAppId;


    @Autowired
    public BibleStudyService(MessageSource messageSource, BibleStudyDao bibleStudyDao, StudentDao studentDao, StudyDao studyDao, NotificationService notificationService) {
        super(bibleStudyDao, log, BibleStudy.class);
        this.messageSource = messageSource;
        this.bibleStudyDao = bibleStudyDao;
        this.studentDao = studentDao;
        this.studyDao = studyDao;
        this.notificationService = notificationService;
    }

    public List<BibleStudy> getByParentChurch(Integer churchId) {
        List<BibleStudy> bibleStudies = bibleStudyDao.findByChurchIdAndArchived(churchId, false);
        bibleStudies.addAll(bibleStudyDao.findByParentChurchIdAndArchivedAndDeniedIsFalse(churchId, false));
        return bibleStudies;
    }

    public List<BibleStudy> getByChurch(Integer churchId) {
        return bibleStudyDao.findByChurchIdAndArchived(churchId, false);
    }

    public List<BibleStudy> getApprovedAttendedByStudent(Integer studentId) {
        return bibleStudyDao.findByStudentIdAndArchivedAndDeniedIsFalseAndApprovedIsTrueAndAttendedIsTrue(studentId, false);
    }

    // this is for history
    public List<BibleStudy> getAllByStudent(Integer studentId) {
        return bibleStudyDao.findByStudentIdAndArchived(studentId, false);
    }

    public List<BibleStudy> getByDate(String startDate, String endDate) {
        return bibleStudyDao.findByArchivedAndDateBetweenAndDeniedIsFalse(false, startDate, endDate);
    }

    public List<BibleStudy> getByChurchAndDate(Integer churchId, String startDate, String endDate) {
        return bibleStudyDao.findByChurchIdAndArchivedAndDateBetweenAndDeniedIsFalse(churchId, false, startDate, endDate);
    }

    public List<BibleStudy> getByStudentAndDate(Integer studentId, String startDate, String endDate) {
        return bibleStudyDao.findByStudentIdAndArchivedAndDateBetweenAndDeniedIsFalseAndApprovedIsTrueAndAttendedIsTrue(studentId, false, startDate, endDate);
    }

    public BibleStudy getStudentStudy(Integer id) {
        return getById(id);
    }

    public BibleStudy save(BibleStudy bibleStudy) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("{} (UserId: {}) is saving a bible study", authenticatedUser.getDisplayName(), authenticatedUser.getId());

        // check if we need to call the attend endpoint right after the save to update the students progress
        boolean newlyAttended = false;
        if (bibleStudy.getId() == null && bibleStudy.isAttended()) {
            log.info("This bible study is newly attended");
            newlyAttended = true;
        }

        // check if the teacher was changed. If so we need to reset the status of teacher availability.
        if (bibleStudy.getId() != null) {
            BibleStudy retrievedBibleStudy = null;
            try {
                retrievedBibleStudy = getById(bibleStudy.getId());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            if (retrievedBibleStudy != null && bibleStudy.getTeacherId() != null && !bibleStudy.getTeacherId().equals(retrievedBibleStudy.getTeacherId())) {
                log.info("This bible study teacher was changed");
                bibleStudy.setTeacherAvailable(null);
            }
        }

        // if the bible study is scheduled in the past then set attended/approved to be true right away
        if (bibleStudy.getDate() != null) {
            // subtract a day so that bible studies made today are not already marked as attended/approved

            try {
                LocalDateTime parsedDateTime = LocalDate.parse(bibleStudy.getDate()).atStartOfDay();
                LocalDateTime currentDateTime = LocalDate.now().atStartOfDay();

                // the date was in the past mark approved
                if (parsedDateTime.isBefore(currentDateTime)) {
                    log.info("The date ({}) has already passed, so mark the bible study as approved and attended", parsedDateTime);
                    bibleStudy.setApproved(true);
                    bibleStudy.setAttended(true);
                    newlyAttended = true;
                } else if (parsedDateTime.isEqual(currentDateTime)) {
                    // the date is the same so need to check the time
                    LocalTime parsedTime = LocalTime.parse(bibleStudy.getTime(), DateTimeFormatter.ofPattern("h:mm a"));

                    if (parsedTime.isBefore(LocalTime.now())) {
                        log.info("The date ({}) is the same but the time ({}) has already passed, so mark the bible study as approved and attended", parsedDateTime, parsedTime);
                        bibleStudy.setApproved(true);
                        bibleStudy.setAttended(true);
                        newlyAttended = true;
                    }
                }
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

        }

        BibleStudy returnedBibleStudy = bibleStudyDao.save(bibleStudy);

        //load the props that wont auto fetch on save
        returnedBibleStudy.setStudent(studentDao.getOne(bibleStudy.getStudentId()));
        returnedBibleStudy.setStudy(studyDao.getOne(bibleStudy.getStudyId()));

        if (returnedBibleStudy.getStudent() != null) {

            // no need to send notification in this case
            if (newlyAttended) {
                updateStudentProgress(returnedBibleStudy);
            } else {
                saveBibleStudyAddedNotification(returnedBibleStudy);
            }

            log.info("{} is saving a bible study for {}", authenticatedUser.getDisplayName(), returnedBibleStudy.getStudent().getFirstName());
        }

        return returnedBibleStudy;
    }

    @PreAuthorize("@authenticatedUserService.canApproveBibleStudy(principal, #id)")
    public BibleStudy approve(Integer id) throws NotFoundException {
        BibleStudy bibleStudy = getById(id);

        bibleStudy.setApproved(true);

        BibleStudy returnedBibleStudy = bibleStudyDao.save(bibleStudy);

        saveTeacherAssignedNotification(bibleStudy);
        saveCreatorBibleStudyApprovedNotification(bibleStudy);

        return returnedBibleStudy;
    }

    @PreAuthorize("@authenticatedUserService.canApproveBibleStudy(principal, #id)")
    public BibleStudy deny(Integer id) throws NotFoundException {
        BibleStudy bibleStudy = getById(id);

        bibleStudy.setDenied(true);

        return bibleStudyDao.save(bibleStudy);
    }

    public BibleStudy attend(Integer id) throws NotFoundException {
        BibleStudy bibleStudy = getById(id);

        bibleStudy.setAttended(true);

        BibleStudy result = bibleStudyDao.save(bibleStudy);

        // update the students study progress
        updateStudentProgress(bibleStudy);

        return result;
    }

    public void archive(Integer id) {
        Optional<BibleStudy> bibleStudyOptional = bibleStudyDao.findById(id);

        if (!bibleStudyOptional.isPresent())
            throw new NotFoundException("The bible study could not be found");

        BibleStudy bibleStudy = bibleStudyOptional.get();
        bibleStudy.setArchived(true);

        bibleStudyDao.save(bibleStudy);
    }

    public BibleStudy teacherAvailable(Integer id) throws NotFoundException {
        BibleStudy bibleStudy = getById(id);

        if (bibleStudy == null)
            throw new NotFoundException("The bible study is not in the system");

        bibleStudy.setTeacherAvailable(true);

        BibleStudy returnedBibleStudy = bibleStudyDao.save(bibleStudy);

        //send the notification to the bible study organizer that teacher will be available
        String titleMessage = "bible-study.teacher.available.title";
        String subTitleMessage = "bible-study.teacher.available.subtitle";
        String contentMessage = "bible-study.teacher.available.content";


        if (bibleStudy.getRequesterId() != null) {
            saveTeacherAvailabilityNotification(returnedBibleStudy, titleMessage, subTitleMessage, contentMessage);
        }

        return returnedBibleStudy;
    }

    public BibleStudy teacherUnavailable(Integer id) throws NotFoundException {
        BibleStudy bibleStudy = getById(id);

        if (bibleStudy == null)
            throw new NotFoundException("The bible study is not in the system");

        bibleStudy.setTeacherAvailable(false);
        bibleStudy.setApproved(false);

        BibleStudy returnedBibleStudy = bibleStudyDao.save(bibleStudy);

        // send the notification to the bible study organizer that teacher will be unavailable
        String titleMessage = "bible-study.teacher.unavailable.title";
        String subTitleMessage = "bible-study.teacher.unavailable.subtitle";
        String contentMessage = "bible-study.teacher.unavailable.content";

        if (returnedBibleStudy.getRequesterId() != null) {
            saveTeacherAvailabilityNotification(returnedBibleStudy, titleMessage, subTitleMessage, contentMessage);
        }
        return returnedBibleStudy;
    }

    public Student updateStudentProgress(BibleStudy bibleStudy) {
        Optional<Student> studentOptional = studentDao.findById(bibleStudy.getStudentId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Integer bibleStudyCount = bibleStudyDao.countDistinctByStudentIdAndAttended(student.getId(), true);

            student.setStudiesCompleted(bibleStudyCount);

            int percentage = Math.round(((float) bibleStudyCount / 50) * 100);
            student.setStudiesPercentage(percentage);

            //set the students last study to this one if applicable
            if (bibleStudy.getStudy() != null) {
                student.setLastStudyId(bibleStudy.getId());
                student.setLastStudyName(bibleStudy.getStudy().getTitle());
                student.setLastStudyDate(LocalDate.parse(bibleStudy.getDate(), DateTimeFormatter.ISO_DATE).format(formatter));
            }

            studentDao.save(student);
            return student;
        }
        return null;
    }

    public void saveBibleStudyAddedNotification(BibleStudy bibleStudy) {

        String titleMessage = "bible-study.upload.title";
        String subTitleMessage = "bible-study.upload.subtitle";
        String contentMessage = "bible-study.upload.content";

        notificationService.saveSystemCreatedNotification(
            bibleStudy.getId(),
            bibleStudy.getRequesterId(),
            bibleStudy.getParentChurchId(),
            bibleStudy.getChurchId(),
            bibleStudy.getGroupId(),
            bibleStudy.getTeamId(),
            bibleStudy.getTeacherId(),
            bibleStudy.getTeacherName(),
            bibleStudy.getRequesterName(),
            bibleStudy.getChurchName(),
            bibleStudy.getDate(),
            Notification.BIBLE_STUDY_SOURCE,
            BibleStudy.BIBLE_STUDY_ADDED_SUB_SOURCE,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );

    }

    public void saveTeacherAvailabilityNotification(BibleStudy bibleStudy, String titleMessage, String subTitleMessage, String contentMessage) {

        String notificationSource = Notification.BIBLE_STUDY_SOURCE;

        notificationService.saveSystemCreatedNotification(
            bibleStudy.getId(),
            bibleStudy.getRequesterId(),
            bibleStudy.getParentChurchId(),
            bibleStudy.getChurchId(),
            bibleStudy.getGroupId(),
            bibleStudy.getTeamId(),
            bibleStudy.getTeacherId(),
            bibleStudy.getTeacherName(),
            bibleStudy.getRequesterName(),
            bibleStudy.getChurchName(),
            bibleStudy.getDate(),
            notificationSource,
            BibleStudy.BIBLE_STUDY_TEACHER_AVAILABLE_SUB_SOURCE,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );

    }

    public void saveTeacherAssignedNotification(BibleStudy bibleStudy) {


        String titleMessage = "bible-study.teacher.approved.title";
        String subTitleMessage = "bible-study.teacher.approved.subtitle";
        String contentMessage = "bible-study.teacher.approved.content";

        notificationService.saveSystemCreatedNotification(
            bibleStudy.getId(),
            bibleStudy.getRequesterId(),
            bibleStudy.getParentChurchId(),
            bibleStudy.getChurchId(),
            bibleStudy.getGroupId(),
            bibleStudy.getTeamId(),
            bibleStudy.getTeacherId(),
            bibleStudy.getTeacherName(),
            bibleStudy.getRequesterName(),
            bibleStudy.getChurchName(),
            bibleStudy.getDate(),
            Notification.BIBLE_STUDY_SOURCE,
            BibleStudy.BIBLE_STUDY_TEACHER_ASSIGNED_SUB_SOURCE,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );

    }

    public void saveCreatorBibleStudyApprovedNotification(BibleStudy bibleStudy) {

        String titleMessage = "bible-study.creator.approved.title";
        String subTitleMessage = "bible-study.creator.approved.subtitle";
        String contentMessage = "bible-study.creator.approved.content";

        notificationService.saveSystemCreatedNotification(
            bibleStudy.getId(),
            bibleStudy.getRequesterId(),
            bibleStudy.getParentChurchId(),
            bibleStudy.getChurchId(),
            bibleStudy.getGroupId(),
            bibleStudy.getTeamId(),
            bibleStudy.getTeacherId(),
            bibleStudy.getTeacherName(),
            bibleStudy.getRequesterName(),
            bibleStudy.getChurchName(),
            bibleStudy.getDate(),
            Notification.BIBLE_STUDY_SOURCE,
            BibleStudy.BIBLE_STUDY_APPROVED_SUB_SOURCE,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );
    }

    public void sendBibleStudyAddedPushNotification(Notification notification) {

        Optional<BibleStudy> bibleStudyOptional = bibleStudyDao.findById(notification.getObjectId());


        if (bibleStudyOptional.isPresent()) {
            BibleStudy bibleStudy = bibleStudyOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String formattedDate = LocalDate.parse(bibleStudy.getDate()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{bibleStudy.getLocation()};
            String[] contentParams = new String[]{bibleStudy.getStudentName(), bibleStudy.getStudy().getTitle(), formattedDate, bibleStudy.getTime()};

            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("bibleStudyId", bibleStudy.getId().toString());
            additionalData.put("status", "Added");

            // send notification to Group Leaders
            List<Filter> groupLeaderFilters = notificationService.createGroupLeaderFilters(notification.getChurchId());
            pushNotificationService.createPushNotification(
                groupLeaderFilters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

            // send notification to Church Leaders
            List<Filter> churchLeaderfilters =
                notificationService.createChurchLeaderFilters(notification.getChurchId());
            pushNotificationService.createPushNotification(
                churchLeaderfilters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

            Integer parentChurchId = notification.getParentChurchId();
            if (parentChurchId == null) parentChurchId = notification.getChurchId();

            // send notification to the overseer if there is one
            List<Filter> overseerFilters = notificationService.createOverseerFilters(parentChurchId, notification.getUserId());
            pushNotificationService.createPushNotification(
                overseerFilters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

            //send notification to all admins as well
            /*List<Filter> adminFilters = notificationService.createAdminFilters(notification.getUserId());
            pushNotificationService.createPushNotification(
                    adminFilters,
                    notification.getTitle(),
                    titleParams,
                    notification.getSubtitle(),
                    subTitleParams,
                    notification.getContent(),
                    contentParams,
                    additionalData
            );*/
        }

    }

    public void sendBibleStudyApprovedPushNotification(Notification notification) {

        Optional<BibleStudy> bibleStudyOptional = bibleStudyDao.findById(notification.getObjectId());

        if (bibleStudyOptional.isPresent()) {
            BibleStudy bibleStudy = bibleStudyOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{bibleStudy.getLocation()};
            String[] contentParams = new String[]{bibleStudy.getStudy().getTitle(), bibleStudy.getStudentName(), bibleStudy.getTeacherName()};

            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("bibleStudyId", bibleStudy.getId().toString());
            additionalData.put("status", "Approved");

            // send notification to the creator that the bible study has been approved
            List<Filter> filters = notificationService.createRequesterFilters(notification.getUserId());
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );
        }

    }

    public void sendBibleStudyTeachingAssignedPushNotification(Notification notification) {

        Optional<BibleStudy> bibleStudyOptional = bibleStudyDao.findById(notification.getObjectId());

        if (bibleStudyOptional.isPresent()) {
            BibleStudy bibleStudy = bibleStudyOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String formattedDate = LocalDate.parse(bibleStudy.getDate()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{bibleStudy.getLocation()};
            String[] contentParams = new String[]{bibleStudy.getStudentName(), bibleStudy.getStudy().getTitle(), bibleStudy.getTeacherName(), formattedDate, bibleStudy.getTime()};

            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("bibleStudyId", bibleStudy.getId().toString());
            additionalData.put("status", "TeachingAssigned");

            // send notification to the teacher that they have been assigned to teach
            List<Filter> filters = notificationService.createTeacherFilters(notification.getTeacherId());
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

        }

    }

    public void sendBibleStudyTeacherAvailabilityPushNotification(Notification notification) {
        Optional<BibleStudy> bibleStudyOptional = bibleStudyDao.findById(notification.getObjectId());

        if (bibleStudyOptional.isPresent()) {
            BibleStudy bibleStudy = bibleStudyOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String formattedDate = LocalDate.parse(bibleStudy.getDate()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{bibleStudy.getLocation()};
            String[] contentParams = new String[]{bibleStudy.getTeacherName(), bibleStudy.getStudy().getTitle(), bibleStudy.getStudentName(), formattedDate, bibleStudy.getTime()};

            Map<String, String> additionalData = new HashMap<String, String>();
            additionalData.put("bibleStudyId", bibleStudy.getId().toString());
            additionalData.put("status", "TeacherAvailability");

            // send notification to the church leader
            List<Filter> filters = notificationService.createChurchLeaderFilters(notification.getChurchId());
            pushNotificationService.createPushNotification(
                filters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

            Integer parentChurchId = notification.getParentChurchId();
            if (parentChurchId == null) parentChurchId = notification.getChurchId();

            // send notification to the overseer if there is one
            List<Filter> overseerFilters = notificationService.createOverseerFilters(parentChurchId, notification.getUserId());
            pushNotificationService.createPushNotification(
                overseerFilters,
                notification.getTitle(),
                titleParams,
                notification.getSubtitle(),
                subTitleParams,
                notification.getContent(),
                contentParams,
                additionalData
            );

        }
    }

}
