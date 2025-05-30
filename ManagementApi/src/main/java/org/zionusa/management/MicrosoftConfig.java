package org.zionusa.management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrosoftConfig {
    @Bean
    public Boolean isOffice365SyncEnabled() {
        String o365UserSync = System.getenv("microsoft.o365.production");
        return o365UserSync != null && o365UserSync.equals("true");
    }
}
