package org.zionusa.admin.service;

import com.currencyfair.onesignal.model.notification.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.MothersTeachingDao;
import org.zionusa.admin.domain.MothersTeaching;
import org.zionusa.admin.domain.Notification;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.base.util.notifications.PushNotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MothersTeachingService extends BaseService<MothersTeaching, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(MothersTeachingService.class);

    @Value("${notification.key}")
    private String notificationKey;

    @Value("${notification.app.id}")
    private String notificationAppId;

    private final NotificationService notificationService;

    private final MothersTeachingDao mothersTeachingDao;

    private final MessageSource messageSource;

    public MothersTeachingService(NotificationService notificationService, MothersTeachingDao mothersTeachingDao, MessageSource messageSource) {
        super(mothersTeachingDao, logger, MothersTeaching.class);
        this.notificationService = notificationService;
        this.mothersTeachingDao = mothersTeachingDao;
        this.messageSource = messageSource;
    }

    public List<MothersTeaching> getAll() {
        return mothersTeachingDao.findAll();
    }

    public MothersTeaching saveMothersTeaching(MothersTeaching mothersTeaching) {
        return mothersTeachingDao.save(mothersTeaching);
    }

    public void deleteMothersTeaching(Integer id) {
        MothersTeaching mothersTeaching = getById(id);

        if (mothersTeaching == null)
            throw new NotFoundException("The translation is not the system.");
        mothersTeachingDao.delete(mothersTeaching);
    }

    public MothersTeaching getMothersTeachingById(Integer id) {
        Optional<MothersTeaching> mothersTeachingOptional = mothersTeachingDao.findById(id);

        if (!mothersTeachingOptional.isPresent())
            throw new NotFoundException("The requested mother's teaching was not found");
        return mothersTeachingOptional.get();
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void getMothersTeachingToSend(Integer number) throws NotFoundException {
        MothersTeaching mothersTeaching = mothersTeachingDao.getMothersTeachingByNumber(number);

        if (mothersTeaching == null) {
            throw new NotFoundException("Could not find Mother's teaching number " + number);
        }

        sendMothersTeachingNotification(mothersTeaching);
    }

    public void sendMothersTeachingNotification(MothersTeaching mothersTeaching) {
        String deliveryTimeOfDay = "8:45AM";
        logger.warn("Mothers teaching service scheduling OneSignal to send this notification at {} in the users timezone", deliveryTimeOfDay);

        if (mothersTeaching != null) {
            Notification notification = new Notification();
            notification.setTitle("announcement.general.mother.teaching.title");
            notification.setSubtitle("announcement.general.mothers.teaching.subtitle");
            notification.setContent("announcement.general.mothers.teaching.content");

            PushNotificationService pushNotificationService = new PushNotificationService(notificationKey, notificationAppId, messageSource);

            String[] titleParams = new String[]{"Mother's Teaching of the Day: #" + mothersTeaching.getNumber()};
            String[] contentParams = new String[]{mothersTeaching.getTitle()};
            Map<String, String> additionalData = new HashMap<>();
            additionalData.put("mothersTeachingId", mothersTeaching.getId().toString());
            additionalData.put("status", "Published");
            additionalData.put("type", "MothersTeaching");

            // Send notification to the USA
            List<Filter> filters = notificationService.createEastCoastFilters();
            pushNotificationService.createPushNotificationBasedOnTimezone(
                    filters,
                    notification.getTitle(),
                    titleParams,
                    notification.getContent(),
                    contentParams,
                    deliveryTimeOfDay,
                    additionalData
            );
        }
    }
}
