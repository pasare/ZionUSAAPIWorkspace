package org.zionusa.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.application.ApplicationDao;
import org.zionusa.management.service.AuthenticatedUserService;
import org.zionusa.management.util.auth.ApplicationRequestFilter;
import org.zionusa.management.util.auth.JWTAuthenticationFilter;
import org.zionusa.management.util.auth.JWTAuthorizationFilter;

import static org.zionusa.management.util.auth.SecurityConstants.AUTH_SET_URL;
import static org.zionusa.management.util.auth.SecurityConstants.LOGIN_URL;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticatedUserService authenticatedUserService;
    private final ApplicationDao applicationDao;
    private final UserDao userDao;

    @Value("${app.environment}")
    private String environment;

    @Autowired
    public WebSecurityConfig(ApplicationDao applicationDao,
                             AuthenticatedUserService authenticatedUserService,
                             UserDao userDao) {
        this.applicationDao = applicationDao;
        this.authenticatedUserService = authenticatedUserService;
        this.userDao = userDao;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.logout().permitAll();

        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
            .antMatchers(HttpMethod.POST, AUTH_SET_URL).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // Allow unauthenticated users to use these endpoints
            .antMatchers(HttpMethod.GET, "/churches/display").permitAll()
            .antMatchers(HttpMethod.POST, "/users/registrations").permitAll()
            .antMatchers(HttpMethod.POST, "/users/password-reset").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ApplicationRequestFilter(applicationDao), BasicAuthenticationFilter.class)
            .addFilter(new JWTAuthenticationFilter(authenticationManager(), userDao))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), authenticatedUserService, environment))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(authenticatedUserService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

}
