package org.zionusa.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.zionusa.admin.dao.ApplicationDao;
import org.zionusa.admin.util.auth.ApplicationRequestFilter;
import org.zionusa.base.util.auth.JWTAuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.environment}")
    private String environment;

    @Value("${zionusa.core.jwt-secret}")
    private String jwtSecret;

    private final ApplicationDao applicationDao;

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
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new ApplicationRequestFilter(applicationDao), BasicAuthenticationFilter.class)
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), environment, jwtSecret));
    }

}