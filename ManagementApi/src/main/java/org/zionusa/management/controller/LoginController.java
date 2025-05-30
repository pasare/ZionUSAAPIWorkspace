package org.zionusa.management.controller;

import com.adobe.xmp.impl.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.AdalUser;
import org.zionusa.management.domain.ManagementAuthenticatedUser;
import org.zionusa.management.domain.User;
import org.zionusa.management.service.AuthenticatedUserService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MessageSource messages;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserDao userDao;

    @Autowired
    public LoginController(AuthenticatedUserService authenticatedUserService, UserDao userDao, @Qualifier("messageSource") MessageSource messages) {
        this.authenticatedUserService = authenticatedUserService;
        this.messages = messages;
        this.userDao = userDao;
    }

    public ManagementAuthenticatedUser login(@RequestBody AdalUser adalUser) {

        if (adalUser != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //First try with active directory id
            ManagementAuthenticatedUser authenticatedUser = null;

            if (adalUser.getOid() != null && !adalUser.getOid().equals("")) {
                authenticatedUser = authenticatedUserService.loadUserByActiveDirectoryId(encoder.encode(adalUser.getOid()));
            }

            //Active directory id not in db, try username
            if (authenticatedUser == null) {
                authenticatedUser = authenticatedUserService.loadUserByUsername(adalUser.getUsername());

                //user exists in db need to add active directory id
                if (authenticatedUser != null) {
                    //add active directory id
                    User user = userDao.getOne(authenticatedUser.getId());
                    String hash_password = encoder.encode(adalUser.getOid());
                    user.setActiveDirectoryId(hash_password);
                    authenticatedUser = new ManagementAuthenticatedUser(userDao.save(user));

                } else {
                    return null;
                }
                //User does not exist in our db, cannot login
            }

            // Add branch migration code
            authenticatedUser.setBranchId(authenticatedUser.getChurchId());
            authenticatedUser.setBranchName(authenticatedUser.getChurchName());

            //set session authentication, and redirect back to the app
            authenticatedUserService.setAuthenticatedUser(authenticatedUser);

            //remove sensitive information
            authenticatedUser.eraseCredentials();
            return authenticatedUser;
        }
        return null;
    }

    public ManagementAuthenticatedUser loginAd(@RequestParam("token") String token) throws InvalidObjectException {

        String[] jwtParts = token.split("\\.");

        JsonObject jwtHeader = Json.createReader(
                new ByteArrayInputStream(Base64.decode(jwtParts[0]).getBytes()))
            .readObject();

        JsonObject jwtPayload = Json.createReader(
                new ByteArrayInputStream(Base64.decode(jwtParts[1]).getBytes()))
            .readObject();

        String email = jwtPayload.getString("unique_name");
        String oid = jwtPayload.getString("oid");


        if (email != null && oid != null) {
            AdalUser adalUser = new AdalUser(email, oid);
            return login(adalUser);
        } else {
            throw new InvalidObjectException("The Token is invalid! Cannot proceed");
        }
    }

    @PostMapping(value = "/ad-auth")
    public boolean setAdIdentifier(@RequestBody AdalUser adalUser) {
        Optional<User> userOptional = userDao.getUserByUsernameIgnoreCase(adalUser.getUsername());

        if (userOptional.isPresent() && userOptional.get().getActiveDirectoryId() == null
            || userOptional.get().getActiveDirectoryId() == "") {
            User user = userOptional.get();
            user.setActiveDirectoryId(new BCryptPasswordEncoder().encode(adalUser.getOid()));

            userDao.save(user);
            return true;
        }

        //user is not in our system
        return false;

    }

    @RequestMapping(value = "/logout-service", method = {RequestMethod.GET, RequestMethod.POST})
    public void Logout(HttpServletResponse response) {
        response.setStatus(200);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
