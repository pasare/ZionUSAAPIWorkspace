package org.zionusa.base.util.notifications;

import com.currencyfair.onesignal.OneSignal;
import com.currencyfair.onesignal.model.notification.DelayedOption;
import com.currencyfair.onesignal.model.notification.Filter;
import com.currencyfair.onesignal.model.notification.NotificationRequest;
import com.currencyfair.onesignal.model.notification.NotificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    private final String notificationKey;

    private final String notificationAppId;

    private final MessageSource messageSource;

    public PushNotificationService(String notificationKey, String notificationAppId, MessageSource messageSource) {
        this.notificationKey = notificationKey;
        this.notificationAppId = notificationAppId;
        this.messageSource = messageSource;
    }

    public boolean createPushNotification(List<Filter> filters,
                                          String titleMessage,
                                          String[] titleParams,
                                          String subTitleMessage,
                                          String[] subTitleParams,
                                          String contentMessage,
                                          String[] contentParams) {
        return createPushNotification(filters, titleMessage, titleParams, subTitleMessage, subTitleParams, contentMessage, contentParams, new HashMap<>());
    }

    public boolean createPushNotification(
            List<Filter> filters,
            String titleMessage,
            String[] titleParams,
            String subTitleMessage,
            String[] subTitleParams,
            String contentMessage,
            String[] contentParams,
            Map<String, String> additionalData
    ) {

        String englishTitle = messageSource.getMessage(titleMessage, titleParams, Locale.ENGLISH);
        String spanishTitle = messageSource.getMessage(titleMessage, titleParams, Locale.ENGLISH);
        String frenchTitle = messageSource.getMessage(titleMessage, titleParams, Locale.FRENCH);
        Map<String, String> titles = createLanguageMap(englishTitle, spanishTitle, frenchTitle);

        String englishSubtitle = messageSource.getMessage(subTitleMessage, subTitleParams, Locale.ENGLISH);
        String spanishSubtitle = messageSource.getMessage(subTitleMessage, subTitleParams, Locale.ENGLISH);
        String frenchSubtitle = messageSource.getMessage(subTitleMessage, subTitleParams, Locale.ENGLISH);
        Map<String, String> subtitles = createLanguageMap(englishSubtitle, spanishSubtitle, frenchSubtitle);

        String englishContent = messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH);
        String spanishContent = messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH);
        String frenchContent = messageSource.getMessage(contentMessage, contentParams, Locale.FRENCH);
        Map<String, String> contents = createLanguageMap(englishContent, spanishContent, frenchContent);

        return sendPushNotification(filters, titles, subtitles, contents, additionalData);
    }

    public boolean createPushNotificationBasedOnTimezone(
            List<Filter> filters,
            String titleMessage,
            String[] titleParams,
            String contentMessage,
            String[] contentParams,
            String deliveryTimeOfDay,
            Map<String, String> additionalData
    ) {

        String englishTitle = messageSource.getMessage(titleMessage, titleParams, Locale.ENGLISH);
        String spanishTitle = messageSource.getMessage(titleMessage, titleParams, Locale.ENGLISH);
        String frenchTitle = messageSource.getMessage(titleMessage, titleParams, Locale.FRENCH);
        Map<String, String> titles = createLanguageMap(englishTitle, spanishTitle, frenchTitle);

        String englishContent = messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH);
        String spanishContent = messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH);
        String frenchContent = messageSource.getMessage(contentMessage, contentParams, Locale.FRENCH);
        Map<String, String> contents = createLanguageMap(englishContent, spanishContent, frenchContent);

        return sendPushNotificationBasedOnTimezone(filters, titles, contents, deliveryTimeOfDay, additionalData);
    }

    private boolean sendPushNotification(List<Filter> filters,
                                         Map<String, String> titles,
                                         Map<String, String> subTitles,
                                         Map<String, String> contents,
                                         Map<String, String> data) {

        NotificationRequest notificationRequest = NotificationRequestBuilder.aNotificationRequest()
                .withAppId(notificationAppId)
                .withFilters(filters)
                .withHeadings(titles)
                //.withSubtitles(subTitles)
                .withContents(contents)
                .withData(data)
                .build();

        OneSignal.createNotification(notificationKey, notificationRequest);
        return true;
    }

    private boolean sendPushNotificationBasedOnTimezone(
            List<Filter> filters,
            Map<String, String> titles,
            Map<String, String> contents,
            String deliveryTimeOfDay,
            Map<String, String> data
    ) {
        NotificationRequest notificationRequest = NotificationRequestBuilder.aNotificationRequest()
                .withAppId(notificationAppId)
                .withFilters(filters)
                .withHeadings(titles)
                .withContents(contents)
                .withData(data)
                .withDelayedOption(DelayedOption.TIMEZONE)
                .withDeliveryTimeOfDay(deliveryTimeOfDay)
                .build();

        OneSignal.createNotification(notificationKey, notificationRequest);
        return true;
    }

    private Map<String, String> createLanguageMap(String englishMessage, String spanishMessage, String frenchMessage) {
        Map<String, String> messages = new HashMap<>();
        messages.put("en", englishMessage);
        messages.put("es", spanishMessage);
        messages.put("fr", frenchMessage);

        return messages;
    }

}
