package org.zionusa.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.zionusa.base.util.auth.JWTAuthorizationFilter;
import org.zionusa.event.domain.Application.ApplicationDao;
import org.zionusa.event.util.auth.ApplicationRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationDao applicationDao;
    @Value("${app.environment}")
    private String environment;
    @Value("${zionusa.core.jwt-secret}")
    private String jwtSecret;

    @Autowired
    public WebSecurityConfig(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.logout().logoutSuccessUrl("/").permitAll();

        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.GET, "/event-proposals/display").permitAll()
            .antMatchers(HttpMethod.GET, "/event-proposals/{\\d+}").permitAll()
            .antMatchers(HttpMethod.GET, "/event-proposals/upcoming/{\\d+}").permitAll()
            .antMatchers(HttpMethod.GET, "/event-fundraising/event/{\\d+}/user/{\\d+}").permitAll()
            .antMatchers(HttpMethod.GET, "/event-registrations/event/{\\d+}").permitAll()
            .antMatchers(HttpMethod.POST, "/event-registrations").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ApplicationRequestFilter(applicationDao), BasicAuthenticationFilter.class)
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), environment, jwtSecret));
    }

}
