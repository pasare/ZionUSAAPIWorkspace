package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.Util;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.management.authorizer.PermissionsAuthorizer;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.userprofile.UserProfile;
import org.zionusa.management.exception.NotFoundException;
import org.zionusa.management.util.StringUtil;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@CacheConfig(cacheNames = "users-cache")
public class UserService extends BaseService<User, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final GroupDao groupDao;
    private final ChurchDao churchDao;
    private final ChurchDisplayDao churchDisplayDao;
    private final TeamDao teamDao;
    private final UserDisplayDao userDisplayDao;
    private final UserPictureDao userPictureDao;
    private final UserProfileService userProfileService;
    private final UserPreferencesDao userPreferencesDao;
    private final UserApplicationRoleDao userApplicationRoleDao;
    private final UserApplicationRoleUserDao userApplicationRoleUserDao;
    private final UserRegistrationDao userRegistrationDao;
    private final UserPasswordResetDao userPasswordResetDao;
    private final MicrosoftGraphService microsoftGraphService;
    private final JavaMailSender javaMailSender;
    private final PermissionsAuthorizer permissionsAuthorizer;

    @Value("${app.environment}")
    private String appEnvironment;
    @Value("${microsoft.o365.domain}")
    private String microsoftO365Domain;
    @Value("${microsoft.tenant.name}")
    private String microsoftTenantName;

    @Autowired
    public UserService(UserDao userDao,
                       GroupDao groupDao,
                       ChurchDao churchDao,
                       ChurchDisplayDao churchDisplayDao,
                       TeamDao teamDao,
                       UserDisplayDao userDisplayDao,
                       UserPictureDao userPictureDao,
                       UserPreferencesDao userPreferencesDao,
                       UserProfileService userProfileService,
                       UserApplicationRoleDao userApplicationRoleDao,
                       UserApplicationRoleUserDao userApplicationRoleUserDao,
                       UserRegistrationDao userRegistrationDao,
                       UserPasswordResetDao userPasswordResetDao,
                       MicrosoftGraphService microsoftGraphService,
                       JavaMailSender javaMailSender,
                       PermissionsAuthorizer permissionsAuthorizer
    ) {
        super(userDao, logger, User.class);
        this.userDao = userDao;
        this.groupDao = groupDao;
        this.churchDao = churchDao;
        this.churchDisplayDao = churchDisplayDao;
        this.teamDao = teamDao;
        this.userDisplayDao = userDisplayDao;
        this.userPictureDao = userPictureDao;
        this.userPreferencesDao = userPreferencesDao;
        this.userProfileService = userProfileService;
        this.userApplicationRoleDao = userApplicationRoleDao;
        this.userApplicationRoleUserDao = userApplicationRoleUserDao;
        this.userRegistrationDao = userRegistrationDao;
        this.userPasswordResetDao = userPasswordResetDao;
        this.microsoftGraphService = microsoftGraphService;
        this.javaMailSender = javaMailSender;
        this.permissionsAuthorizer = permissionsAuthorizer;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public Boolean checkActiveDirectoryUser(String email) {
        return microsoftGraphService.checkActiveDirectoryUser(email);
    }

    public Integer countAllActive() {
        return userDisplayDao.countAllByEnabledIsTrue();
    }

    @PreAuthorize("hasAuthority('Admin')")
    @Cacheable
    @Override
    public List<User> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @PostAuthorize("@authenticatedUserService.canAccessUser(principal, #id)")
    @Override
    public User getById(Integer id) {
        Optional<User> userOptional = userDao.findById(id);

        if (!userOptional.isPresent())
            throw new NotFoundException("User was not found");

        return userOptional.get();
    }

    public List<User.DisplayUser> getDisplayUsers(Integer associationId) {
        if (associationId == null) {
            return userDisplayDao.findAll();
        }
        return userDisplayDao.getAllByAssociationId(associationId);
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Retrieving list of display items");
        if (authenticatedUser.getAssociationId() == null) {
            return getAllDisplayFromList(userDisplayDao.findAll(), columns);
        }
        return getAllDisplayFromList(userDisplayDao.getAllByAssociationId(authenticatedUser.getAssociationId()), columns);
    }

    public List<User.DisplayUser> getActiveDisplayUsers(Integer associationId) {
        if (associationId == null) {
            return userDisplayDao.getAllByEnabledTrueAndChurchIdNot(199);
        }
        return userDisplayDao.getAllByAssociationIdAndEnabledIsTrue(associationId);
    }

    @PreAuthorize("hasAuthority('Admin')")
    public List<User> getByGender(String gender) {
        List<User> users = userDao.getUsersByGenderOrderByFirstNameAsc(gender);

        if (users == null)
            return new ArrayList<>();

        return users;
    }

    public List<User.DisplayUser> getByAccessId(Integer id) {
        return userDisplayDao.getAllByAccessId(id);
    }

    public List<User.DisplayUser> getByRoleId(Integer id) {
        return userDisplayDao.getAllByRoleId(id);
    }

    public List<User.DisplayUser> getDisplayByChurchId(Integer churchId) {
        return userDisplayDao.getAllByChurchId(churchId);
    }

    public List<User.DisplayUser> getDisplayByGroupId(Integer groupId) {
        return userDisplayDao.getAllByGroupId(groupId);
    }

    public List<User.DisplayUser> getDisplayByTeamId(Integer teamId) {
        return userDisplayDao.getAllByTeamId(teamId);
    }

    public List<User.ApplicationRole> getAllGaGraders() {
        return userApplicationRoleUserDao.getAllByApplicationRoleName(EApplicationRole.STUDY_GA_GRADER.getValue());
    }

    public List<User.ApplicationRole> getAllGaGradersByRoleIdOrMainChurchId(Integer mainChurchId) {
        return userApplicationRoleUserDao.getAllByMainChurchIdAndApplicationRoleName(mainChurchId, EApplicationRole.STUDY_GA_GRADER.getValue());
    }

    public List<User.ApplicationRole> getAllTeachersDisplay(Integer associationId) {
        if (associationId == null) {
            return userApplicationRoleUserDao.getAllByApplicationRoleName(EApplicationRole.STUDY_TEACHER.getValue());
        }
        return userApplicationRoleUserDao.getAllByAssociationIdAndApplicationRoleName(associationId, EApplicationRole.STUDY_TEACHER.getValue());
    }

    public List<User> getAllTeachers() {
        return userDao.getUsersByTeacherIsTrue();
    }

    public List<User.ApplicationRole> getDisplayByTeacherAndChurchId(Integer churchId) {
        return userApplicationRoleUserDao.getAllByChurchIdAndApplicationRoleName(churchId, EApplicationRole.STUDY_TEACHER.getValue());
    }

    public List<User.ApplicationRole> getAllReadyGradersDisplay() {
        return userApplicationRoleUserDao.getAllByApplicationRoleName(EApplicationRole.STUDY_READY_GRADER.getValue());
    }

    public List<User> getAllReadyGraders() {
        return userDao.getUsersByReadyGraderIsTrue();
    }

    public List<User.ApplicationRole> getDisplayByReadyGraderAndMainChurchId(Integer churchId) {
        return userApplicationRoleUserDao.getAllByMainChurchIdAndApplicationRoleName(churchId, EApplicationRole.STUDY_READY_GRADER.getValue());
    }

    public User.DisplayUser getDisplayByUserId(Integer userId) throws NotFoundException {
        Optional<User.DisplayUser> userOptional = userDisplayDao.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new NotFoundException();
    }

    public Map<String, Object> getDisplayByUserIdWithColumns(List<String> columns, Integer userId) throws NotFoundException {
        Optional<User.DisplayUser> userOptional = userDisplayDao.findById(userId);
        if (userOptional.isPresent()) {
            return Util.getFieldsAndValues(columns, userOptional.get());
        }
        throw new NotFoundException();
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal,#id)")
    public List<User.DisplayUser> getByParentChurchId(Integer id) {
        List<User.DisplayUser> users = userDisplayDao.getAllByChurchId(id);
        users.addAll(userDisplayDao.getAllByParentChurchId(id));
        return users;
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal,#id)")
    public List<User.DisplayUser> getByChurchId(Integer id) {
        logger.info("getting all users by church id: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userDisplayDao.getAllByChurchId(id);
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurch(principal,#id)")
    public List<User.DisplayUser> getByChurchIdAndAccessId(Integer id, Integer accessId) {
        return userDisplayDao.getAllByChurchIdAndAccessId(id, accessId);
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurches(principal,#ids)")
    public List<User.DisplayUser> getByChurchIds(String ids) {
        List<User.DisplayUser> users = new ArrayList<>();

        for (String id : ids.split(",")) {
            try {
                int churchId = Integer.parseInt(id.trim());
                users.addAll(userDisplayDao.getAllByChurchId(churchId));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("One of the provided id's is invalid");
            }
        }

        return users;
    }

    @PreAuthorize("@authenticatedUserService.canAccessChurches(principal,#ids)")
    public List<User.DisplayUser> getByChurchIdsAndAccessId(String ids, Integer accessId) {
        List<User.DisplayUser> users = getByChurchIds(ids);
        return users.stream().filter(user -> user.getAccessId().equals(accessId)).collect(Collectors.toList());
    }

    @PreAuthorize("@authenticatedUserService.canAccessGroup(principal,#id)")
    public List<User.DisplayUser> getByGroupId(Integer id) {
        logger.info("getting all users by group id: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userDisplayDao.getAllByGroupId(id);
    }

    @PreAuthorize("@authenticatedUserService.canAccessGroups(principal,#ids)")
    public List<User.DisplayUser> getByGroupIds(String ids) {
        List<User.DisplayUser> users = new ArrayList<>();

        for (String id : ids.split(",")) {
            try {
                int groupId = Integer.parseInt(id.trim());
                users.addAll(userDisplayDao.getAllByGroupId(groupId));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("One of the provided id's is invalid");
            }
        }

        return users;
    }

    @PreAuthorize("@authenticatedUserService.canAccessTeam(principal,#id)")
    public List<User> getByTeamId(Integer id) {
        logger.info("getting all users by team id: {} ({})", id, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userDao.getByTeamId(id);
    }

    @PreAuthorize("@authenticatedUserService.canAccessTeams(principal,#ids)")
    public List<User> getByTeamIds(String ids) {
        List<User> users = new ArrayList<>();

        for (String id : ids.split(",")) {
            try {
                int teamId = Integer.parseInt(id.trim());
                users.addAll(userDao.getByTeamId(teamId));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("One of the provided id's is invalid");
            }
        }

        return users;
    }

    public List<User> getGaGraders() {
        return userDao.getUsersByGaGraderIsTrue();
    }

    public String getUserToken() {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return permissionsAuthorizer.getAccessToken(authenticatedUser);
    }

    public List<User> getTheologicalStudents() {
        return userDao.getUsersByTheologicalStudentIsTrue();
    }

    public List<User> getByAccessId(int accessId) {
        return userDao.getUsersByAccessId(accessId);
    }

    public List<User.ApplicationRole> getByApplicationRoleContains(Integer applicationRoleId) {
        return userApplicationRoleUserDao.getAllByApplicationRoleId(applicationRoleId);
    }

    @Transactional
    public User saveUserApplicationRoles(Integer userId, List<UserApplicationRole> userApplicationRoles) {
        Optional<User> userOptional = userDao.findById(userId);

        if (!userOptional.isPresent()) return null;

        User user = userOptional.get();

        // remove all previous application roles
        userApplicationRoleDao.deleteUserApplicationRolesByUserId(userId);

        List<UserApplicationRole> returnedUserApplicationRoles = userApplicationRoleDao.saveAll(userApplicationRoles);
        user.setApplicationRoles(returnedUserApplicationRoles);
        return user;
    }

    @Transactional
    public boolean saveUserApplicationRole(Integer userId, UserApplicationRole userApplicationRole) {
        try {
            Optional<User> userOptional = userDao.findById(userId);

            if (userOptional.isPresent()) {
                userApplicationRoleDao.save(userApplicationRole);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.getUserByUsernameIgnoreCase(username);

        if (!userOptional.isPresent())
            throw new NotFoundException("The user was not found in the system");

        return userOptional.get();
    }

    @Override
    @Transactional
    @PreAuthorize("@authenticatedUserService.canModifyUser(principal,#user)")
    @CacheEvict(cacheNames = {"users-cache", "partners-cache", "display-users-cache", "display-active-users-cache"}, allEntries = true)
    public User save(User user) {
        logger.info("Started saving user {} with id: {} ({})", user.getDisplayName(), user.getId(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        Boolean isNewUser = user.getId() == null;

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setActiveDirectoryId(null);

        // Sanitize email and username to remove accents and other characters
        if (user.getRecoveryEmail() != null) {
            user.setRecoveryEmail(StringUtil.sanitizeEmail(user.getRecoveryEmail()));
        }
        if (user.getUsername() != null) {
            user.setUsername(StringUtil.sanitizeEmail(user.getUsername()));
        }

        User returnedUser = internalSaveUser(user);
        UserProfile returnedUserProfile = null;

        if (user.getProfile() != null) {
            logger.info("Started saving user profile {} ({})", returnedUser.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            // Use this service call because it attaches the user profile information
            returnedUserProfile = userProfileService.save(user.getProfile());
            logger.info("Finished saving user profile {} ({})", returnedUser.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        }

        if (returnedUser.getId() != null) {
            returnedUser.setApplicationRoles(userApplicationRoleDao.findByUserId(returnedUser.getId()));
        }
        if (returnedUserProfile != null) {
            returnedUser.setProfile(returnedUserProfile);
        }

        if (Boolean.TRUE.equals(isNewUser)) {
            logger.info("Creating O365 User {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Create a new O365 user without a license. This will use the registration object and registration user
            // flow to send emails.
            UserRegistration userRegistration = new UserRegistration();
            userRegistration.setApproved(true);
            userRegistration.setApproverId(authenticatedUser.getId());

            // The user object only has the teamId on it, so we need to query to get the church/branchId.
            Team team = teamDao.getOne(user.getTeamId());
            Group group = groupDao.getOne(team.getGroupId());
            userRegistration.setChurchId(group.getChurchId());

            // Set the email to empty string, and the leader's username/email will be used instead
            userRegistration.setEmail("");
            userRegistration.setFirstName(user.getFirstName());
            userRegistration.setLastName(user.getLastName());
            userRegistration.setGender(user.getGender());
            userRegistration.setProcessed(true);
            userRegistration.setProcessedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));

            // Add leader's username so they get the email
            microsoftGraphService.createActiveDirectoryUser(userRegistration, authenticatedUser.getUsername());
            logger.info("Finished creating O365 User {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        }

        logger.info("Finished saving user {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return returnedUser;
    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.canModifyUser(principal,#user)")
    public User.ProfileUser saveProfile(User user) {
        // Keep same user save process
        save(user);

        // Return proper ProfileUser
        return userProfileService.getByUserId(user.getId());
    }

    public List<UserRegistration> getUserRegistrations() {
        return userRegistrationDao.findAll();
    }

    public List<UserRegistration> getUserRegistrationsByChurchId(Integer churchId) {
        return userRegistrationDao.findAllByChurchId(churchId);
    }

    public Map<String, List> getUserRegistrationsAndPasswordResetsByChurchId(Integer churchId) {
        List<UserRegistration> userRegistrations = userRegistrationDao.findAllByChurchIdAndApprovedIsNull(churchId);
        List<UserPasswordReset> userPasswordResets = userPasswordResetDao.getAllByChurchIdAndApprovedIsNull(churchId);
        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("userRegistrations", userRegistrations);
        resultMap.put("userPasswordResets", userPasswordResets);
        return resultMap;
    }

    public Integer getTotalRegistrationsAndResetsByChurchId(Integer churchId) {
        Integer totalUserRegistrations =
            userRegistrationDao.countAllByChurchIdAndApprovedNullAndProcessedIsNull(churchId);
        Integer totalPasswordResets = userPasswordResetDao.countAllByChurchIdAndApprovedIsNull(churchId);
        return totalUserRegistrations + totalPasswordResets;
    }

    public UserRegistration saveUserRegistration(UserRegistration userRegistration) {
        userRegistration.setEmail(StringUtil.sanitizeEmail(userRegistration.getEmail()));
        Optional<UserRegistration> userRegistrationOptional = userRegistrationDao.findByEmail(userRegistration.getEmail());
        if (userRegistrationOptional.isPresent()) {
            // Update existing
            UserRegistration existingUserRegistration = userRegistrationOptional.get();
            existingUserRegistration.setChurchId(userRegistration.getChurchId());
            existingUserRegistration.setFirstName(userRegistration.getFirstName());
            existingUserRegistration.setGender(userRegistration.getGender());
            existingUserRegistration.setLastName(userRegistration.getLastName());
            existingUserRegistration.setApproved(null);
            existingUserRegistration.setApproverId(null);
            existingUserRegistration.setProcessed(null);
            existingUserRegistration.setProcessedDate(null);
            return userRegistrationDao.save(existingUserRegistration);
        }
        // Save new one
        return userRegistrationDao.save(userRegistration);
    }

    public void deleteUserRegistration(Integer id) {
        Optional<UserRegistration> userRegistrationOptional = userRegistrationDao.findById(id);

        if (!userRegistrationOptional.isPresent())
            throw new NotFoundException("Cannot delete a user registration that does not exist");

        userRegistrationDao.delete(userRegistrationOptional.get());
    }

    public UserRegistration processUserRegistration(Integer id, boolean approval) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<UserRegistration> userRegistrationOptional = userRegistrationDao.findById(id);
        if (!userRegistrationOptional.isPresent())
            throw new NotFoundException("Cannot approve a registration that does not exist");

        UserRegistration userRegistration = userRegistrationOptional.get();

        // This registration has already been approved
        if (approval && userRegistration.getProcessed() != null && userRegistration.getProcessed() && userRegistration.getApproved() != null && userRegistration.getApproved()) {
            logger.info("{} {} has already been approved", userRegistration.getFirstName(), userRegistration.getLastName());
            return userRegistration;
        }
        // This registration has already been denied
        if (!approval && userRegistration.getProcessed() != null && userRegistration.getProcessed() && userRegistration.getApproved() != null && !userRegistration.getApproved()) {
            logger.info("{} {} has already been denied", userRegistration.getFirstName(), userRegistration.getLastName());
            return userRegistration;
        }

        // This registration has not been processed
        if (approval) {
            logger.info("Processing approval for user {} {}", userRegistration.getFirstName(), userRegistration.getLastName());
        } else {
            logger.info("Processing denial for user {} {}", userRegistration.getFirstName(), userRegistration.getLastName());
        }

        // Create the user in microsoft active directory
        if (approval) {
            microsoftGraphService.createActiveDirectoryUser(userRegistration);
        }

        userRegistration.setApproved(approval);
        userRegistration.setApproverId(authenticatedUser.getId());
        userRegistration.setProcessed(true);
        userRegistration.setProcessedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));

        return userRegistrationDao.save(userRegistration);
    }

    public UserPasswordReset processUserPasswordReset(Integer id, boolean approval) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<UserPasswordReset> userPasswordResetOptional = userPasswordResetDao.findById(id);
        if (!userPasswordResetOptional.isPresent())
            throw new NotFoundException("Cannot approve a pw reset request that does not exist");

        UserPasswordReset userPasswordReset = userPasswordResetOptional.get();

        // This pw reset request has already been approved
        if (approval && userPasswordReset.getApproved() != null && Boolean.TRUE.equals(userPasswordReset.getApproved())) {
            logger.info("{} {}'s pw reset request has already been approved", userPasswordReset.getFirstName(),
                userPasswordReset.getLastName());
            return userPasswordReset;
        }
        // This pw reset request has already been denied
        if (!approval && userPasswordReset.getApproved() != null && !Boolean.TRUE.equals(userPasswordReset.getApproved())) {
            logger.info("{} {}'s pw reset request has already been denied", userPasswordReset.getFirstName(), userPasswordReset.getLastName());
            return userPasswordReset;
        }

        // This pw reset request has not been processed
        if (approval) {
            logger.info("Processing pw reset approval for user {} {}", userPasswordReset.getFirstName(),
                userPasswordReset.getLastName());
        } else {
            logger.info("Processing pw reset denial for user {} {}", userPasswordReset.getFirstName(),
                userPasswordReset.getLastName());
        }

        if (approval) {
            try {
                // Add branch leader information to the email
                Church branch = churchDao.getOne(userPasswordReset.getChurchId());
                User leaderOne = null;
                User leaderTwo = null;

                if (branch != null) {
                    if (branch.getLeaderId() != null) {
                        leaderOne = userDao.getOne(branch.getLeaderId());
                    }
                    if (branch.getLeaderTwoId() != null) {
                        leaderTwo = userDao.getOne(branch.getLeaderTwoId());
                    }
                }

                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mail);

                helper.setTo("it@" + microsoftO365Domain);
                if (appEnvironment.equals("test")) {
                    helper.setSubject("[TEST] [" + microsoftTenantName + "] Reset Password Approved for " + userPasswordReset.getFirstName() +
                        " " + userPasswordReset.getLastName());
                } else {
                    helper.setSubject("[" + microsoftTenantName + "] Reset Password Approved for " + userPasswordReset.getFirstName() + " " + userPasswordReset.getLastName());
                }

                String messageText = "";
                if (appEnvironment.equals("test")) {
                    messageText += "This is a test email. Please disregard. \n\n\n\n";
                }

                messageText += "God Bless You,\n\n" +
                    authenticatedUser.getDisplayName() + " (" + authenticatedUser.getChurchName() + ") approved " +
                    "to reset the password for " + userPasswordReset.getFirstName() + " "
                    + userPasswordReset.getLastName() + " \n\n" +
                    "User Information\n\n" +
                    "First Name: " + userPasswordReset.getFirstName() + "\n" +
                    "Last Name: " + userPasswordReset.getLastName() + "\n" +
                    "Username: " + userPasswordReset.getUserName() + "\n" +
                    "Email: " + userPasswordReset.getEmail() + "\n" +
                    "Branch Name: " + userPasswordReset.getChurchName() + "\n\n" +
                    "Approved By Information\n\n" +
                    "Full Name: " + authenticatedUser.getDisplayName() + "\n" +
                    "Username: " + authenticatedUser.getUsername() + "\n\n";
                if (leaderOne != null || leaderTwo != null) {
                    messageText += "Branch Leader Information\n\n";
                    if (leaderOne != null) {
                        messageText += "Name: " + (leaderOne == null ? "Unknown" : (leaderOne.getFirstName() + " " + leaderOne.getLastName())) + "\n" +
                            "Email: " + (leaderOne == null ? "Unknown" : leaderOne.getUsername()) + "\n\n";
                    }
                    if (leaderTwo != null) {
                        messageText += "Name: " + (leaderTwo == null ? "Unknown" : (leaderTwo.getFirstName() + " " + leaderTwo.getLastName())) + "\n" +
                            "Email: " + (leaderTwo == null ? "Unknown" : leaderTwo.getUsername()) + "\n\n";
                    }
                } else {
                    messageText += "No branch leader information available\n\n";
                }
                messageText += "Please reach out to them as soon as possible.";
                helper.setText(messageText);
                helper.setFrom("appadmin@" + microsoftO365Domain);
                helper.setReplyTo(userPasswordReset.getEmail());

                javaMailSender.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        userPasswordReset.setApproved(approval);
        userPasswordReset.setApproverId(authenticatedUser.getId());
        userPasswordReset.setDeterminedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));

        return userPasswordResetDao.save(userPasswordReset);
    }

    public UserPasswordReset saveUserPasswordReset(UserPasswordReset userPasswordReset) throws IOException {

        try {
            // Throw exception when user is not found by the given email
            Optional<User> user = userDao.getUserByUsernameIgnoreCase(userPasswordReset.getUserName());
            if (!user.isPresent()) {
                throw new IOException();
            }

            // Reset the approval data since the member is asking for password reset again
            userPasswordReset.setApproved(false);
            userPasswordReset.setApproverId(null);
            userPasswordReset.setDeterminedDate(null);

            // Update an existing password reset request
            UserPasswordReset foundUserPasswordReset = userPasswordResetDao.getByUserName(userPasswordReset.getUserName());
            if (foundUserPasswordReset != null) {
                userPasswordReset.setId(foundUserPasswordReset.getId());
            }
            try {
                if ((SecurityContextHolder.getContext().getAuthentication().getPrincipal()) != null) {
                    AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                    //if submitted by overseer, admin, church leader, or group leader  it will be automatically approved
                    if (authenticatedUser.getUserApplicationRoles() != null && (
                        authenticatedUser.getUserApplicationRoles().contains("admin") ||
                            authenticatedUser.getUserApplicationRoles().contains("overseer") ||
                            authenticatedUser.getUserApplicationRoles().contains("church") ||
                            authenticatedUser.getUserApplicationRoles().contains("group") ||
                            authenticatedUser.getUserApplicationRoles().contains("ADMIN_ACCESS") ||
                            authenticatedUser.getUserApplicationRoles().contains("OVERSEER_ACCESS") ||
                            authenticatedUser.getUserApplicationRoles().contains("CHURCH_ACCESS") ||
                            authenticatedUser.getUserApplicationRoles().contains("BRANCH_ACCESS") ||
                            authenticatedUser.getUserApplicationRoles().contains("GROUP_ACCESS"))) {

                        UserPasswordReset createdPasswordReset = userPasswordResetDao.save(userPasswordReset);
                        return processUserPasswordReset(createdPasswordReset.getId(), true);
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
            return userPasswordResetDao.save(userPasswordReset);
        } catch (IOException e) {
            throw new IOException("User not found by the given email/username");
        }
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    @Transactional
    @CacheEvict(cacheNames = {"users-cache", "partners-cache", "display-users-cache", "display-active-users-cache"}, allEntries = true)
    public void delete(Integer id) {
        logger.info("Start deleting user id {}", id);

        logger.info("Checking user {} preferences", id);
        List<UserPreference> userPreferences = userPreferencesDao.getAllByUserId(id);
        for (UserPreference userPreference : userPreferences) {
            userPreferencesDao.deleteById(userPreference.getId());
            logger.info("Deleted user id {} preference {}", id, userPreference.getPreferenceKey());
        }

        logger.info("Checking if user {} is a team leader", id);
        List<Team> teams = teamDao.getTeamsByLeaderId(id);
        for (Team team : teams) {
            team.setLeaderId(null);
            team.setLeader(null);
            teamDao.save(team);
            logger.info("Removed user id {} from being a leader of team {}", id, team.getId());
        }

        logger.info("Checking if user {} is a group leader", id);
        List<Group> groups = groupDao.getGroupsByLeaderId(id);
        for (Group group : groups) {
            group.setLeaderId(null);
            group.setLeader(null);
            groupDao.save(group);
            logger.info("Removed user id {} from being a leader of group {}", id, group.getId());
        }

        logger.info("Checking if user {} is a church leader", id);
        List<Church> churches = churchDao.getChurchesByLeaderId(id);
        for (Church church : churches) {
            church.setLeaderId(null);
            church.setLeader(null);
            churchDao.save(church);
            logger.info("Removed user id {} from being a leader of church {}", id, church.getId());
        }

        logger.info("Checking if user {} exists", id);
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Spouse has a potential circular reference preventing delete. The spouse reference to this user must be
            // removed.
            logger.info("Checking user {} spouse", id);
            if (user.getSpouseId() != null) {
                Optional<User> spouseOptional = userDao.findById(id);
                if (spouseOptional.isPresent()) {
                    User spouse = spouseOptional.get();
                    spouse.setSpouseId(null);
                    userDao.save(spouse);
                    logger.info("Removed user {} spouse reference to {}", spouse.getId(), id);
                }
            }
            user.setSpouseId(null);

            // The pictureId on the user and the userId on the picture have a circular reference. The pictureId on the
            // user can be deleted first however, the userId on the picture cannot.
            logger.info("Checking user {} profile picture", id);
            if (user.getPictureId() != null) {
                // Remove user reference
                user.setPictureId(null);

                // Delete the picture
                userPictureDao.deleteById(user.getPictureId());
                logger.info("Removed user {} profile picture", id);
            }

            // Save the user in a state where it can be deleted
            userDao.save(user);

            userDao.delete(userOptional.get());
            logger.info("Deleted user id {}", id);
        } else {
            logger.info("User id {} already deleted", id);
        }
    }

    public Boolean isUsernameAvailable(String username) {
        Optional<User> userOptional = userDao.getUserByUsernameIgnoreCase(username);
        return !userOptional.isPresent();
    }

    @PreAuthorize("@authenticatedUserService.canDeleteUser(principal, #id)")
    @CacheEvict(cacheNames = {"users-cache", "partners-cache", "display-users-cache", "display-active-users-cache"}, allEntries = true)
    public void archive(Integer id) {
        Optional<User> userOptional = userDao.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("Archiving user {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

            Optional<Team> churchTeamOptional = teamDao.getTeamByGroupIdAndChurchTeamTrue(user.getTeam().getGroupId());

            //move the user to the church group
            if (churchTeamOptional.isPresent()) {
                user.setTeamId(churchTeamOptional.get().getId());
            }

            user.setArchived(true);
            user = internalSaveUser(user);

            //various clean up depending on role
            final String userRole = user.getRole().getName();
            if (EUserRole.CHURCH_LEADER.is(userRole)) {
                archiveChurchLeader(user.getId());
            } else if (EUserRole.GROUP_LEADER.is(userRole)) {
                archiveGroupLeader(user.getId());
            } else if (EUserRole.TEAM_LEADER.is(userRole)) {
                archiveTeamLeader(user.getId());
            }

            logger.info("Finished archiving user {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        }
    }

    @PreAuthorize("@authenticatedUserService.canDeleteUser(principal, #id)")
    public void hide(Integer id) {
        Optional<User> userOptional = userDao.findById(id);

        if (!userOptional.isPresent())
            throw new NotFoundException("Cannot exile user who does not moving the user");

        User user = userOptional.get();
        logger.info("Archiving user {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

        // Find Patmos
        Church church = churchDao.getFirstByNameLikeAndHiddenLocationIsTrue("Patmos");

        if (church == null)
            throw new NotFoundException("Cannot find Patmos cannot continue moving the user");

        Group churchGroup = church.getGroups().stream().filter(Group::isChurchGroup).findFirst().orElse(null);

        if (churchGroup == null)
            throw new NotFoundException("Cannot find the church group to continue moving the user");

        Team churchTeam = churchGroup.getTeams().stream().filter(Team::isChurchTeam).findFirst().orElse(null);

        if (churchTeam == null)
            throw new NotFoundException("Cannot find the church team to continue moving the user");

        user.setTeamId(churchTeam.getId());

        internalSaveUser(user);

        // various clean up depending on role
        final String userRole = user.getRole().getName();
        if (EUserRole.CHURCH_LEADER.is(userRole)) {
            archiveChurchLeader(user.getId());
        } else if (EUserRole.GROUP_LEADER.is(userRole)) {
            archiveGroupLeader(user.getId());
        } else if (EUserRole.TEAM_LEADER.is(userRole)) {
            archiveTeamLeader(user.getId());
        }

        logger.info("Finished hiding user {} ({})", user.getDisplayName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission(" + UserPermission.UPDATE_GOSPEL_WORKER_STATUS + ")")
    @CacheEvict(cacheNames = {"users-cache", "partners-cache", "display-users-cache", "display-active-users-cache"}, allEntries = true)
    public User updateGospelWorkerStatus(Integer id, Boolean status) {
        Optional<User> userOptional = userDao.findById(id);

        if (!userOptional.isPresent())
            throw new NotFoundException("Cannot update gospel worker status for a user that does not exist");

        User user = userOptional.get();

        user.setGospelWorker(status);

        return internalSaveUser(user);
    }

    @PreAuthorize("@authenticatedUserService.canAccessUser(principal, #id)")
    @CacheEvict(cacheNames = {"users-cache", "partners-cache", "display-users-cache", "display-active-users-cache"}, allEntries = true)
    public User reactivateUser(Integer id) {
        Optional<User> userOptional = userDao.findById(id);

        if (!userOptional.isPresent())
            throw new NotFoundException("Cannot reactive a user that does not exist");

        User user = userOptional.get();

        user.setArchived(false);
        return internalSaveUser(user);

    }

    @PreAuthorize("@authenticatedUserService.canAccessUser(principal, #id)")
    public User enableUser(Integer id) {
        Optional<User> userOptional = userDao.findById(id);

        if (!userOptional.isPresent())
            throw new NotFoundException("Cannot enable a user that does not exist");

        User user = userOptional.get();

        user.setEnabled(true);
        return internalSaveUser(user);
    }

    @PreAuthorize("@authenticatedUserService.canAccessUser(principal, #id)")
    public User disableUser(Integer id) {

        Optional<User> userOptional = userDao.findById(id);

        if (!userOptional.isPresent())
            throw new NotFoundException("Cannot disable a user that does not exist");

        User user = userOptional.get();

        user.setEnabled(false);
        return internalSaveUser(user);
    }

    @PreAuthorize("hasAuthority('Admin')")
    public List<User> getDeactivatedUsers() {
        return userDao.getUsersByArchivedTrue();
    }

    @PreAuthorize("hasAuthority('Admin')")
    public List<User> getDisabledUsers() {
        return userDao.getUsersByEnabledFalse();
    }

    @PreAuthorize("hasAuthority('Admin')")
    @CacheEvict(cacheNames = {"users-cache", "partners-cache", "display-users-cache", "display-active-users-cache"}, allEntries = true)
    public void expireCache() {
    }

    public EventManagementTeam getEventManagementTeam(Integer requesterId) {
        Optional<User.DisplayUser> requesterOptional = userDisplayDao.findById(requesterId);
        if (!requesterOptional.isPresent())
            throw new org.zionusa.base.util.exceptions.NotFoundException("Requester not found");

        User.DisplayUser requester = requesterOptional.get();

        return getEventManagementTeamByChurch(requester.getChurchId());
    }

    public EventManagementTeam getEventManagementTeamByChurch(Integer churchId) {
        Optional<Church> churchOptional = churchDao.findById(churchId);
        if (!churchOptional.isPresent())
            throw new org.zionusa.base.util.exceptions.NotFoundException("Church not found");

        Church church = churchOptional.get();
        return getTeamMembers(church.getId(), church.getName());
    }

    public List<EventManagementTeam> getAllEventManagementTeams() {
        return churchDisplayDao.
            getAllByArchivedFalse()
            .stream()
            .map(church -> getTeamMembers(church.getId(), church.getName()))
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> searchUsers(String query, Integer limit) {
        // Enough columns to do transfer request and identify the user
        List<String> columns = new ArrayList<>();
        columns.add("churchId");
        columns.add("churchName");
        columns.add("displayName");
        columns.add("groupId");
        columns.add("id");
        columns.add("pictureUrl");
        columns.add("teamId");
        columns.add("username");
        return userDisplayDao
            .getAllByDisplayNameContainsOrUsernameContainsOrChurchNameContains(query, query, query)
            .stream()
            .sorted(this::sortDisplayUsers)
            .limit((limit == null || limit <= 0) ? 25 : limit)
            .map(user -> Util.getFieldsAndValues(columns, user))
            .collect(Collectors.toList());
    }

    public int sortDisplayUsers(User.DisplayUser a, User.DisplayUser b) {
        if (a.getDisplayName().equals(b.getDisplayName())) {
            return 0;
        }
        return a.getDisplayName().compareTo(b.getDisplayName());
    }

    private EventManagementTeam getTeamMembers(Integer churchId, String churchName) {
        // Get the management team by church
        List<User.ApplicationRole> displayUsers = userApplicationRoleUserDao.getApplicationRoleDisplayUserByChurchId(churchId);
        List<User.ApplicationRole> displayEventManagers = userApplicationRoleUserDao.getAllByApplicationRoleName("ZIONUSA_EVENT_MANAGER");
        List<User.ApplicationRole> displayEventAdmin = userApplicationRoleUserDao.getAllByApplicationRoleName("ZIONUSA_EVENT_ADMIN");
        List<User.ApplicationRole> displayEventGA = userApplicationRoleUserDao.getAllByApplicationRoleName("ZIONUSA_EVENT_GA");
        return assembleEventManagementTeam(churchName, churchId, displayUsers, displayEventManagers, displayEventAdmin, displayEventGA);
    }

    private void archiveChurchLeader(Integer userId) {
        Optional<Church> churchOptional = churchDao.getChurchByLeaderId(userId);

        if (!churchOptional.isPresent())
            throw new NotFoundException("The leader's group was not found");

        Church church = churchOptional.get();
        church.setLeaderId(null);
        churchDao.save(church);

        logger.info("Removed user with userId: {} as the church leader of {} ({})", userId, church.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    private void archiveGroupLeader(Integer userId) {
        Optional<Group> groupOptional = groupDao.getGroupByLeaderId(userId);

        if (!groupOptional.isPresent())
            throw new NotFoundException("The leader's group was not found");

        Group group = groupOptional.get();
        group.setLeaderId(null);
        groupDao.save(group);

        logger.info("Removed user with userId: {} as the group leader of {} ({})", userId, group.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
    }

    private void archiveTeamLeader(Integer userId) {
        Optional<Team> teamOptional = teamDao.getTeamByLeaderId(userId);

        if (!teamOptional.isPresent())
            throw new NotFoundException("The leader's team was not found");

        Team team = teamOptional.get();
        team.setLeaderId(null);
        teamDao.save(team);

        logger.info("Removed user with userId: {} as the team leader of {} ({})", userId, team.getName(), LocalDateTime.now(EZoneId.NEW_YORK.getValue()));

    }

    public User internalSaveUser(User user) {
        // If the team id of the user was changed also update the group and team id

        User returnedUser = userDao.save(user);

        // Update Microsoft fields
        if (microsoftGraphService != null) {
            microsoftGraphService.updateMicrosoftUser(returnedUser);
        }

        // Save user
        return returnedUser;
    }


    private EventManagementTeam assembleEventManagementTeam(String churchName, Integer churchId, List<User.ApplicationRole> displayUsers,
                                                            List<User.ApplicationRole> displayEventManagers, List<User.ApplicationRole> displayEventAdmin, List<User.ApplicationRole> displayEventGA
    ) {

        List<User.ApplicationRole> eventRepresentatives = new ArrayList<>();
        List<User.ApplicationRole> managerApprovers;
        List<User.ApplicationRole> adminApprovers;
        List<User.ApplicationRole> gaApprovers;

        List<User.ApplicationRole> audioVisualEngineers = new ArrayList<>();
        List<User.ApplicationRole> graphicDesigners = new ArrayList<>();
        List<User.ApplicationRole> photographers = new ArrayList<>();
        List<User.ApplicationRole> videographers = new ArrayList<>();
        List<User.ApplicationRole> publicRelationsRepresentatives = new ArrayList<>();
        List<User.ApplicationRole> spokespersons = new ArrayList<>();

        managerApprovers = displayEventManagers;
        adminApprovers = displayEventAdmin;
        gaApprovers = displayEventGA;

        // get event team for all categories
        for (User.ApplicationRole displayUser : displayUsers) {
            if (displayUser.getApplicationRoleName() != null) {
                switch (displayUser.getApplicationRoleName()) {
                    case "ZIONUSA_EVENT_REPRESENTATIVE":
                        eventRepresentatives.add(displayUser);
                        break;
                    case "ZIONUSA_EVENT_PUBLIC_RELATIONS":
                        publicRelationsRepresentatives.add(displayUser);
                        break;
                    case "ZIONUSA_EVENT_PHOTOGRAPHER":
                        photographers.add(displayUser);
                        break;
                    case "ZIONUSA_EVENT_GRAPHIC_DESIGNER":
                        graphicDesigners.add(displayUser);
                        break;
                    case "ZIONUSA_EVENT_AUDIO_VISUAL":
                        audioVisualEngineers.add(displayUser);
                        break;
                    case "ZIONUSA_EVENT_VIDEOGRAPHER":
                        videographers.add(displayUser);
                        break;
                    case "ZIONUSA_EVENT_SPOKESPERSON":
                        spokespersons.add(displayUser);
                        break;
                    default:
                        break;
                }
            }
        }

        EventManagementTeam eventManagementTeam = new EventManagementTeam(churchName, churchId, eventRepresentatives, managerApprovers, adminApprovers, gaApprovers);
        eventManagementTeam.setAudioVisualEngineers(audioVisualEngineers.stream().map(this::getEventApproverFromApplicationRoleDisplayUser).collect(Collectors.toList()));
        eventManagementTeam.setVideographers(videographers.stream().map(this::getEventApproverFromApplicationRoleDisplayUser).collect(Collectors.toList()));
        eventManagementTeam.setPhotographers(photographers.stream().map(this::getEventApproverFromApplicationRoleDisplayUser).collect(Collectors.toList()));
        eventManagementTeam.setGraphicDesigners(graphicDesigners.stream().map(this::getEventApproverFromApplicationRoleDisplayUser).collect(Collectors.toList()));
        eventManagementTeam.setPublicRelationsRepresentatives(publicRelationsRepresentatives.stream().map(this::getEventApproverFromApplicationRoleDisplayUser).collect(Collectors.toList()));
        eventManagementTeam.setSpokespersons(spokespersons.stream().map(this::getEventApproverFromApplicationRoleDisplayUser).collect(Collectors.toList()));

        Optional<Church> churchOptional = churchDao.findById(churchId);
        if (churchOptional.isPresent() && churchOptional.get().getLeaderId() != null) {
            Optional<User.DisplayUser> displayUserOptional = userDisplayDao.findById(churchOptional.get().getLeaderId());
            if (displayUserOptional.isPresent()) {
                User.DisplayUser displayUser = displayUserOptional.get();
                eventManagementTeam.setChurchLeader(new EventApprover(
                    displayUser.getId(),
                    displayUser.getUsername(),
                    displayUser.getDisplayName(),
                    displayUser.getAssociationId(),
                    displayUser.getChurchId(),
                    displayUser.getChurchName(),
                    displayUser.getPictureUrl(),
                    displayUser.getProfilePicture()
                ));
            }
        }
        return eventManagementTeam;
    }


    private EventApprover getEventApproverFromApplicationRoleDisplayUser(User.ApplicationRole user) {
        return new EventApprover(
            user.getId(),
            user.getUserName(),
            user.getDisplayName(),
            user.getAssociationId(),
            user.getChurchId(),
            user.getChurchName(),
            user.getPictureUrl(),
            user.getProfilePicture());
    }

}
