package org.zionusa.management.util.auth;

import com.adobe.xmp.impl.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.AdalUser;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.zionusa.management.util.auth.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;

    public static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserDao userDao) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            String adalToken = null;
            AdalUser adalUser = null;

            //try to get the adal user from token first
            if (req.getHeader("Token") != null && !req.getHeader("Token").equals("null")) {
                adalToken = req.getHeader("Token");
                adalUser = getAdalUser(adalToken);
            }

            //if token is not present try to get adal user from request body
            if (adalToken == null) {
                adalUser = new ObjectMapper().readValue(req.getInputStream(), AdalUser.class);
            }

            if (adalUser != null) {
                logger.info("[{}] Attempting Authentication, oid: {}", adalUser.getUsername(), adalUser.getOid());

                //if active directory id is null this is first time login need to set it first
                Optional<org.zionusa.management.domain.User> userOptional = userDao.getUserByUsernameIgnoreCase(adalUser.getUsername());
                if (userOptional.isPresent()) {
                    org.zionusa.management.domain.User user = userOptional.get();
                    if (user.getActiveDirectoryId() == null) {
                        logger.info("[{}] Setting active directory id for first time user login", adalUser.getUsername());
                        user.setActiveDirectoryId(new BCryptPasswordEncoder().encode(adalUser.getOid()));
                        userDao.save(user);
                    }
                }

                return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        adalUser.getUsername(),
                        adalUser.getOid(),
                        new ArrayList<>())
                );
            } else {
                logger.error("Login failed. The token is invalid unrecoverable error.");
                return authenticationManager.authenticate(null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        Gson gson = new Gson();
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME); // at least 10 days from now
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getPrincipal();

        // Add branch migration code
        authenticatedUser.setBranchId(authenticatedUser.getChurchId());
        authenticatedUser.setBranchName(authenticatedUser.getChurchName());

        String token = Jwts.builder()
            .setSubject(authenticatedUser.getUsername())
            .claim(AUTHORITY_STRING, authenticatedUser.getAuthorities().toString())
            .claim(AUTHENTICATED_USER, authenticatedUser)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
            .compact();

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.addHeader("Access-Control-Expose-Headers", "Authorization");

        res.getWriter().write(gson.toJson(authenticatedUser));
        res.getWriter().close();

        logger.info("[{}] Authentication successful", authenticatedUser.getUsername());
    }

    //Login may have failed because this is the first time the user is trying to login, so attempt to set active directory id
    //Then try login again
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                              HttpServletResponse res,
                                              AuthenticationException failed) throws IOException, ServletException {

        String adalToken = req.getHeader("Token");
        AdalUser adalUser = getAdalUser(adalToken);

        //if we have a valid token, then can try setting oid and login again
        if (adalUser != null) {
            Optional<org.zionusa.management.domain.User> userOptional = userDao.getUserByUsernameIgnoreCase(adalUser.getUsername());

            if (userOptional.isPresent()) {
                org.zionusa.management.domain.User user = userOptional.get();

                //if users active directory id was successfully added then try to authenticate again
                if (user.getActiveDirectoryId() == null || user.getActiveDirectoryId().equals("")) {
                    user.setActiveDirectoryId(new BCryptPasswordEncoder().encode(adalUser.getOid()));

                    userDao.save(user);

                    Authentication auth = attemptAuthentication(req, res);

                    if (auth != null) {
                        successfulAuthentication(req, res, null, auth);
                        return;
                    } else {
                        super.unsuccessfulAuthentication(req, res, failed);
                    }
                } else {
                    super.unsuccessfulAuthentication(req, res, failed);
                }

            }
        } else {
            super.unsuccessfulAuthentication(req, res, failed);
        }

        logger.warn("Authentication failed");
    }

    private AdalUser getAdalUser(String adalToken) {
        if (adalToken != null && StringUtils.countMatches(adalToken, ".") >= 2) {

            String[] jwtParts = adalToken.split("\\.");

            JsonObject jwtPayload = Json.createReader(
                    new ByteArrayInputStream(Base64.decode(jwtParts[1]).getBytes()))
                .readObject();

            // maintain unique_name for backwards compatibility
            String email = "";
            String version = jwtPayload.getString("ver");

            if (version.startsWith("1")) email = jwtPayload.getString("unique_name");
            else if (version.startsWith("2")) email = jwtPayload.getString("preferred_username");
            String oid = jwtPayload.getString("oid");


            if (email != null && oid != null) {
                return new AdalUser(email, oid);
            }
        }

        return null;
    }

}
