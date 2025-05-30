package org.zionusa.management;

import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.web.internal.WebRequestTrackingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;


@Configuration
public class ApplicationInsightsConfig {

    //Initialize AI TelemetryConfiguration via Spring Beans
    @Bean
    public String telemetryConfig() {
        String telemetryKey = System.getenv("azure.application-insights.instrumentation-key");
        if (telemetryKey != null) {
            TelemetryConfiguration.getActive().setInstrumentationKey(telemetryKey);
        }
        return telemetryKey;
    }

    //Set AI Web Request Tracking Filter
    @Bean
    public FilterRegistrationBean<WebRequestTrackingFilter> aiFilterRegistration(@Value("${spring.application.name:application}") String applicationName) {
        FilterRegistrationBean<WebRequestTrackingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new WebRequestTrackingFilter(applicationName));
        registration.setName("webRequestTrackingFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }

    //Set up AI Web Request Tracking Filter
    @Bean(name = "WebRequestTrackingFilter")
    public Filter webRequestTrackingFilter(@Value("${spring.application.name:application}") String applicationName) {
        return new WebRequestTrackingFilter(applicationName);
    }
}
