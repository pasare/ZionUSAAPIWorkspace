package org.zionusa.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zionusa.base.util.audit.UsernameAuditorAware;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;


@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = {"org.zionusa.event","org.zionusa.base.util.config"})
public class EventsApiApplication {

	@Autowired
	public EventsApiApplication() {

	}

	@Bean
	AuditorAware<String> auditorProvider() {
		return new UsernameAuditorAware();
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("ET"));
		System.out.println("Spring boot application running in EST timezone :"+ new Date());
	}

	public static void main(final String[] args) {
		SpringApplication.run(EventsApiApplication.class, args);
	}

}
