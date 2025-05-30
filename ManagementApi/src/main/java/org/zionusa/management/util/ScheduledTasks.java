package org.zionusa.management.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.management.service.ChurchService;
import org.zionusa.management.service.MigrationService;
import org.zionusa.management.service.UserService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Component
public class ScheduledTasks {

    public static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final ChurchService churchService;
    private final MigrationService migrationService;
    private final UserService userService;

    @Autowired
    public ScheduledTasks(ChurchService churchService,
                          MigrationService migrationService,
                          UserService userService) {
        this.churchService = churchService;
        this.migrationService = migrationService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 */2 * ?")
    public void expireUserCache() {
        log.info("Begin expiring users cache {}", LocalDate.now(EZoneId.NEW_YORK.getValue()));
        userService.expireCache();
    }

    @Scheduled(cron = "0 0 0 */2 * ?")
    public void expireChurchCache() {
        log.info("Begin expiring churches cache {}", LocalDate.now(EZoneId.NEW_YORK.getValue()));
        churchService.expireCache();
    }

    @Scheduled(cron = "0 4 0 * * ?")
    public void migrateUserApplicationRoles() {
        migrationService.migrateUserApplicationRoles();
    }
}
