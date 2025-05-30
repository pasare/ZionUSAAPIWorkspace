package org.zionusa.event.service;

import com.currencyfair.onesignal.OneSignal;
import com.currencyfair.onesignal.model.notification.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.NotificationsDao;
import org.zionusa.event.domain.Notification;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class NotificationsService extends BaseService<Notification, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsService.class);

    private final MessageSource messageSource;
    private final NotificationsDao notificationsDao;
    private final EmailService emailService;

    @Value("${onesignal.admin.notification.key}")
    private String adminNotificationKey;

    @Value("${onesignal.admin.app.id}")
    private String adminNotificationAppId;

    @Value("${onesignal.global.production.flag}")
    private String oneSignalProduction;

    @Autowired
    public NotificationsService(MessageSource messageSource, NotificationsDao notificationsDao, EmailService emailService) {
        super(notificationsDao, logger, Notification.class);
        this.messageSource = messageSource;
        this.notificationsDao = notificationsDao;
        this.emailService = emailService;
    }

    // get all pending notifications
    public List<Notification> getPendingNotifications() {
        logger.warn("Retrieving all pending notifications.");
        return notificationsDao.getAllByProcessedFalse();
    }

    public void sendEmailNotification(Notification notification) {
        logger.warn("Sending Email Notifications For: {}", new Date());

        if (notification.getRecipients() != null && !notification.getRecipients().contains("null") && !notification.getRecipients().isEmpty()) {
            try {
                logger.warn("email recipients: {}: ", notification.getRecipients());
                logger.warn("email BCC recipients: {}: ", notification.getBccRecipients());
//                this.emailService.sendHtmlEmail(notification.getRecipients(), notification.getBccRecipients(),  notification.getTitle(), notification.getMessage());
                this.emailService.sendHtmlEmail(notification.getRecipients(), notification.getBccRecipients(), notification.getTitle(), notification.getMessage());

            } catch (MessagingException e) {
                logger.error("There was a problem sending the mail", e);
            }
        }
    }

    public void sendPushNotification(Notification notification) {
        //Todo: Not usable because we have no way to receive phone numbers, a
        /*LocalDate parsedDate = LocalDate.parse(bibleStudy.getDate(), DateTimeFormatter.ISO_DATE_TIME);

        if (bibleStudy.getTeacherId() != null && parsedDate.atStartOfDay().isAfter(LocalDate.now().atStartOfDay())) {

            String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            String[] contentParams = new String[]{
                    bibleStudy.getPreacherName(),
                    bibleStudy.getStudy().getShortTitle(),
                    bibleStudy.getStudent().getFirstName() + " " + bibleStudy.getStudent().getLastName(),
                    formattedDate,
                    bibleStudy.getTime()
            };

            List<Filter> filters = new ArrayList<Filter>();
            Filter userFilter = new Filter(Field.TAG, "userId", Relation.EQUALS, bibleStudy.getTeacherId().toString());
            filters.add(userFilter);
            */
    }

    private Map<String, String> createLanguageMap(String englishMessage, String spanishMessage, String frenchMessage, String koreanMessage) {
        Map<String, String> messages = new HashMap<>();
        messages.put("en", englishMessage);
        messages.put("es", spanishMessage);
        messages.put("fr", frenchMessage);
        messages.put("ko", koreanMessage);
        return messages;
    }

    public boolean createAdminPushNotification(List<Filter> filters,
                                               String titleMessage,
                                               String[] titleParams,
                                               String contentMessage,
                                               String[] contentParams) {
        return createAdminPushNotification(filters, titleMessage, titleParams, contentMessage, contentParams, new HashMap<>());
    }

    public boolean createAdminPushNotification(List<Filter> filters,
                                               String titleMessage,
                                               String[] titleParams,
                                               String contentMessage,
                                               String[] contentParams,
                                               Map<String, String> additionalData) {

        String englishTitle = messageSource.getMessage(titleMessage, titleParams, Locale.ENGLISH);
        String spanishTitle = messageSource.getMessage(titleMessage, titleParams, Locale.ENGLISH);
        String frenchTitle = messageSource.getMessage(titleMessage, titleParams, Locale.FRENCH);
        String koreanTitle = messageSource.getMessage(titleMessage, titleParams, Locale.KOREAN);
        Map<String, String> titles = createLanguageMap(englishTitle, spanishTitle, frenchTitle, koreanTitle);

        String englishContent = messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH);
        String spanishContent = messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH);
        String frenchContent = messageSource.getMessage(contentMessage, contentParams, Locale.FRENCH);
        String koreanContent = messageSource.getMessage(contentMessage, contentParams, Locale.KOREAN);
        Map<String, String> contents = createLanguageMap(englishContent, spanishContent, frenchContent, koreanContent);

        // limit the notifications based on the environment
        Filter environmentFilter = new Filter(Field.TAG, "production", Relation.EQUALS, oneSignalProduction);
        filters.add(environmentFilter);

        return sendAdminPushNotification(filters, titles, contents, additionalData);
    }

    private boolean sendAdminPushNotification(List<Filter> filters,
                                              Map<String, String> titles,
                                              Map<String, String> contents,
                                              Map<String, String> data) {

        NotificationRequest notificationRequest = NotificationRequestBuilder.aNotificationRequest()
            .withAppId(adminNotificationAppId)
            .withFilters(filters)
            .withHeadings(titles)
            .withContents(contents)
            .withData(data)
            .build();

        OneSignal.createNotification(adminNotificationKey, notificationRequest);
        return true;
    }
}
