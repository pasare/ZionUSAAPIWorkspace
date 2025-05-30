package org.zionusa.biblestudy.service;


import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.notifications.PushNotificationService;
import org.zionusa.biblestudy.dao.SermonRecordingDao;
import org.zionusa.biblestudy.domain.Notification;
import org.zionusa.biblestudy.domain.SermonRecording;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SermonRecordingService extends BaseService<SermonRecording, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SermonRecordingService.class);
    private final MessageSource messageSource;
    private final SermonRecordingDao sermonRecordingDao;
    private final NotificationService notificationService;
    private final AzureBlobStorageService azureBlobStorageService;
    @Value("${notification.key}")
    private String notificationKey;
    @Value("${notification.app.id}")
    private String notificationAppId;


    @Autowired
    public SermonRecordingService(SermonRecordingDao sermonRecordingDao, MessageSource messageSource,
                                  NotificationService notificationService,
                                  AzureBlobStorageService azureBlobStorageService) {
        super(sermonRecordingDao, logger, SermonRecording.class);
        this.sermonRecordingDao = sermonRecordingDao;
        this.messageSource = messageSource;
        this.notificationService = notificationService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    public List<SermonRecording> getByParentChurch(Integer churchId) {
        List<SermonRecording> sermonRecordings = sermonRecordingDao.findByChurchId(churchId);
        sermonRecordings.addAll(sermonRecordingDao.findByParentChurchId(churchId));
        return sermonRecordings;
    }

    public List<SermonRecording> getByChurch(Integer churchId) {
        return sermonRecordingDao.findByChurchId(churchId);
    }

    public List<SermonRecording> getByPreacher(Integer preacherId) {
        return sermonRecordingDao.findByPreacherId(preacherId);
    }

    public SermonRecording gradeSermon(SermonRecording sermonRecording) {
        if (sermonRecording.getId() == null)
            throw new IllegalStateException("Cannot grade sermon that is not in the system yet");

        //ensure that the only data that is changed is data related to grades
        SermonRecording retrievedSermonRecording = getById(sermonRecording.getId());
        retrievedSermonRecording.setGrade(sermonRecording.getGrade());
        retrievedSermonRecording.setGraderId(sermonRecording.getGraderId());
        retrievedSermonRecording.setPastorsSignature(sermonRecording.isPastorsSignature());
        retrievedSermonRecording.setSignatureType(sermonRecording.getSignatureType());
        retrievedSermonRecording.setExampleSermon(sermonRecording.isExampleSermon());
        retrievedSermonRecording.setComments(sermonRecording.getComments());

        SermonRecording returnedSermonRecording = sermonRecordingDao.save(retrievedSermonRecording);

        saveSermonGradedNotification(retrievedSermonRecording);

        if (returnedSermonRecording.isExampleSermon()) {
            saveExampleSermonNotification(sermonRecording);
        }

        return returnedSermonRecording;
    }

    public SermonRecording save(SermonRecording sermonRecording) {

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean sendNotification = sermonRecording.getId() == null;

        // No grading information should be saved
        sermonRecording.setPreacherName(authenticatedUser.getDisplayName());
        sermonRecording.setGrade(0);
        sermonRecording.setGraderId(0);
        sermonRecording.setExampleSermon(false);

        // truncate digits that come after T in date field
        String date = sermonRecording.getDate();
        if (date != null) {
            date = date.split("T")[0];
            sermonRecording.setDate(date);
        }

        SermonRecording returnedSermonRecording = sermonRecordingDao.save(sermonRecording);

        if (sendNotification) {
            saveSermonUploadedNotification(sermonRecording);
        }

        return returnedSermonRecording;
    }

    @Override
    public void delete(Integer id) {
        SermonRecording sermonRecording = getById(id);
        azureBlobStorageService.deleteUploadedSermonAudio(sermonRecording.getPreacherId(),
            sermonRecording.getStudy().getTitle());
        super.delete(id);
    }

    public void saveSermonUploadedNotification(SermonRecording sermonRecording) {

        String notificationSubSource = SermonRecording.SERMON_UPLOADED_SUB_SOURCE;
        String notificationSource = Notification.SERMON_RECORDING_SOURCE;

        String titleMessage = "sermon.upload.title";
        String subTitleMessage = "sermon.upload.subtitle";
        String contentMessage = "sermon.upload.content";

        notificationService.saveSystemCreatedNotification(
            sermonRecording.getId(),
            sermonRecording.getPreacherId(),
            null,
            sermonRecording.getChurchId(),
            sermonRecording.getGroupId(),
            sermonRecording.getTeamId(),
            null,
            null,
            sermonRecording.getPreacherName(),
            sermonRecording.getChurchName(),
            sermonRecording.getDate(),
            notificationSource,
            notificationSubSource,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );

    }

    public void saveExampleSermonNotification(SermonRecording sermonRecording) {

        String notificationSubSource = SermonRecording.SERMON_EXAMPLE_SUB_SOURCE;
        String notificationSource = Notification.SERMON_RECORDING_SOURCE;

        String titleMessage = "sermon.example.title";
        String subTitleMessage = "sermon.example.subtitle";
        String contentMessage = "sermon.example.content";


        notificationService.saveSystemCreatedNotification(
            sermonRecording.getId(),
            sermonRecording.getPreacherId(),
            null,
            sermonRecording.getChurchId(),
            sermonRecording.getGroupId(),
            sermonRecording.getTeamId(),
            null,
            null,
            sermonRecording.getPreacherName(),
            sermonRecording.getChurchName(),
            sermonRecording.getDate(),
            notificationSource,
            notificationSubSource,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );
    }

    public void saveSermonGradedNotification(SermonRecording sermonRecording) {

        String notificationSubSource = SermonRecording.SERMON_GRADED_SUB_SOURCE;
        String notificationSource = Notification.SERMON_RECORDING_SOURCE;

        String titleMessage = "sermon.graded.title";
        String subTitleMessage = "sermon.graded.subtitle";
        String contentMessage;

        if (sermonRecording.getGrade() >= 3) contentMessage = "sermon.graded.content-passed";
        else contentMessage = "sermon.graded.content-failed";

        Map<String, String> additionalData = new HashMap<>();
        additionalData.put("preacherId", sermonRecording.getPreacherId().toString());
        additionalData.put("sermonRecordingId", sermonRecording.getId().toString());
        additionalData.put("status", "Graded");
        additionalData.put("type", "SermonRecording");

        notificationService.saveSystemCreatedNotification(
            sermonRecording.getId(),
            sermonRecording.getPreacherId(),
            null,
            sermonRecording.getChurchId(),
            sermonRecording.getGroupId(),
            sermonRecording.getTeamId(),
            null,
            null,
            sermonRecording.getPreacherName(),
            sermonRecording.getChurchName(),
            sermonRecording.getDate(),
            notificationSource,
            notificationSubSource,
            titleMessage,
            subTitleMessage,
            contentMessage,
            null
        );
    }

    public void sendSermonUploadedNotification(Notification notification) {
        Optional<SermonRecording> sermonRecordingOptional = sermonRecordingDao.findById(notification.getObjectId());

        if (sermonRecordingOptional.isPresent()) {
            SermonRecording sermonRecording = sermonRecordingOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{sermonRecording.getPreacherName(), sermonRecording.getChurchName(), sermonRecording.getStudy().getTitle()};

            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("preacherId", sermonRecording.getPreacherId().toString());
            additionalData.put("sermonRecordingId", sermonRecording.getId().toString());
            additionalData.put("status", "GradeNeeded");
            additionalData.put("type", "SermonRecording");

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

            // send notification to all admins as well
            List<Filter> adminFilters = notificationService.createAdminFilters(notification.getUserId());
            pushNotificationService.createPushNotification(
                adminFilters,
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

    public void sendSermonGradedPushNotification(Notification notification) {
        Optional<SermonRecording> sermonRecordingOptional = sermonRecordingDao.findById(notification.getObjectId());

        if (sermonRecordingOptional.isPresent()) {
            SermonRecording sermonRecording = sermonRecordingOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{sermonRecording.getStudy().getTitle(), sermonRecording.getPreacherName(), sermonRecording.getGrade().toString()};

            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("preacherId", sermonRecording.getPreacherId().toString());
            additionalData.put("sermonRecordingId", sermonRecording.getId().toString());
            additionalData.put("status", "SermonGraded");
            additionalData.put("type", "SermonRecording");

            // send notification to the east coast
            List<Filter> filters = notificationService.createRequesterFilters(sermonRecording.getPreacherId());
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

    public void sendExampleSermonPushNotification(Notification notification) {
        Optional<SermonRecording> sermonRecordingOptional = sermonRecordingDao.findById(notification.getObjectId());

        if (sermonRecordingOptional.isPresent()) {
            SermonRecording sermonRecording = sermonRecordingOptional.get();
            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{};
            String[] subTitleParams = new String[]{};
            String[] contentParams = new String[]{sermonRecording.getStudy().getTitle()};

            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("preacherId", sermonRecording.getPreacherId().toString());
            additionalData.put("sermonRecordingId", sermonRecording.getId().toString());
            additionalData.put("status", "ExampleSermon");
            additionalData.put("type", "SermonRecording");

            // send notification to the east coast
            List<Filter> filters = notificationService.createEastCoastFilters();
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

}
