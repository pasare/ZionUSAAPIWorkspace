package org.zionusa.management.service;

import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.PasswordProfile;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IUserCollectionPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.zionusa.base.domain.AppTimeZone;
import org.zionusa.base.enums.EUserType;
import org.zionusa.management.MicrosoftConfig;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.title.Title;
import org.zionusa.management.domain.title.TitleDao;
import org.zionusa.management.exception.NotFoundException;
import org.zionusa.management.util.GraphAuthenticationProvider;
import org.zionusa.management.util.StringUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class MicrosoftGraphService {

    private static final Logger log = LoggerFactory.getLogger(MicrosoftGraphService.class);
    private static final List<String> SCOPES = Collections.singletonList("https://graph.microsoft.com/.default");

    private final JavaMailSender javaMailSender;
    private final UserDao userDao;
    private final ChurchDao churchDao;
    private final GroupDao groupDao;
    private final TitleDao titleDao;
    private final ApplicationRoleDao applicationRoleDao;
    private final UserApplicationRoleDao userApplicationRoleDao;
    private final MicrosoftConfig microsoftConfig;

    @Value("${app.environment}")
    private String appEnvironment;
    @Value("${microsoft.calendar.email}")
    private String microsoftCalendarEmail;
    @Value("${microsoft.o365.clientId}")
    private String microsoftClientId;
    @Value("${microsoft.o365.clientSecret}")
    private String microsoftClientSecret;
    @Value("${microsoft.o365.domain}")
    private String microsoftO365Domain;
    @Value("${microsoft.tenant.greeting}")
    private String microsoftTenantGreeting;
    @Value("${microsoft.o365.tenantId}")
    private String microsoftTenantId;
    @Value("${microsoft.tenant.name}")
    private String microsoftTenantName;

    @Autowired
    public MicrosoftGraphService(JavaMailSender javaMailSender,
                                 UserDao userDao,
                                 ChurchDao churchDao,
                                 GroupDao groupDao,
                                 TitleDao titleDao,
                                 ApplicationRoleDao applicationRoleDao,
                                 UserApplicationRoleDao userApplicationRoleDao,
                                 MicrosoftConfig microsoftConfig) {
        this.javaMailSender = javaMailSender;
        this.userDao = userDao;
        this.churchDao = churchDao;
        this.groupDao = groupDao;
        this.titleDao = titleDao;
        this.applicationRoleDao = applicationRoleDao;
        this.userApplicationRoleDao = userApplicationRoleDao;
        this.microsoftConfig = microsoftConfig;
    }

    public Boolean checkActiveDirectoryUser(String email) {
        IGraphServiceClient graphClient = getGraphServiceClient();

        // Check if the user already exists in graph (for now we are straight checking the domain suffix, case insensitive),
        if (Pattern.compile(Pattern.quote("@" + microsoftO365Domain), Pattern.CASE_INSENSITIVE).matcher(email).find()) {
            log.info("Start checking an Active Directory user for {}", email);
            Option filterOption = new QueryOption("$filter", "userPrincipalName eq '" + email + "'");
            IUserCollectionPage users = graphClient.users().buildRequest(Collections.singletonList(filterOption)).get();

            // Found a matching user account
            List<com.microsoft.graph.models.extensions.User> microsoftUsers = users.getCurrentPage();
            if (microsoftUsers.isEmpty()) {
                log.warn("[O365] No user found");
                return false;
            } else {
                log.warn("[O365] User found");
                return true;
            }
        }
        log.warn("[O365] Cannot check an Active Directory user for non {} account {}", microsoftO365Domain, email);
        return false;
    }

    public void createActiveDirectoryUser(UserRegistration userRegistration) {
        createActiveDirectoryUser(userRegistration, "");
    }

    public void createActiveDirectoryUser(UserRegistration userRegistration, String leaderUsername) {
        GraphAuthenticationProvider authProvider = new GraphAuthenticationProvider(microsoftClientId, SCOPES, microsoftClientSecret, microsoftTenantId, NationalCloud.Global);
        log.info("[O365] Start creating/checking a user for {} {}", userRegistration.getFirstName(), userRegistration.getLastName());

        IGraphServiceClient graphClient = GraphServiceClient
            .builder()
            .authenticationProvider(authProvider)
            .buildClient();

        String generatedEmail;
        if (Pattern.compile(Pattern.quote("@" + microsoftO365Domain), Pattern.CASE_INSENSITIVE).matcher(userRegistration.getEmail()).find()) {
            generatedEmail = userRegistration.getEmail();
        } else {
            generatedEmail = generateEmailAddress(userRegistration);
        }

        // Check if the user already exists in graph (for now we are straight checking the domain suffix, case-insensitive)
        log.info("[O365] Checking if the user account {} currently exists", generatedEmail);
        Option filterOption = new QueryOption("$filter", "userPrincipalName eq '" + generatedEmail + "'");
        IUserCollectionPage user = graphClient.users().buildRequest(Collections.singletonList(filterOption)).get();

        //we can create the user account without issue
        if (user.getCurrentPage().isEmpty()) {
            log.info("[O365] No O365 account {} exists, so create it now", generatedEmail);
            try {
                com.microsoft.graph.models.extensions.User microsoftUser = createMicrosoftUser(userRegistration);
                graphClient.users().buildRequest().post(microsoftUser);
                log.info("[O365] Created O365 account successfully: {}", microsoftUser.userPrincipalName);
                constructNewUserEmail(userRegistration, microsoftUser, leaderUsername);
                try {
                    User newManagementUser = createManagementUser(userRegistration, microsoftUser.userPrincipalName);
                    if (newManagementUser != null) {
                        log.info("[O365] Created management user successfully: {}", newManagementUser.getUsername());
                        updateMicrosoftUser(newManagementUser);
                    }
                } catch (Exception e) {
                    log.error("[O365] Error creating management user: {}", generatedEmail);
                    e.printStackTrace();
                    String reason = "Your sign up request has been received. You should receive an email within 24 hours with details of how to sign in";
                    Church church = churchDao.getOne(userRegistration.getChurchId());
                    constructUserRegistrationFailureEmail(userRegistration, church, generatedEmail, reason, leaderUsername);
                }
            } catch (Exception e) {
                log.info("[O365] The user {} exists, so try to fix an existing management user", generatedEmail);
                e.printStackTrace();
                createManagementUser(userRegistration, generatedEmail);
                constructExistingUserEmail(userRegistration, generatedEmail, leaderUsername);
            }
        } else {
            log.info("[O365] The user {} exists, so try to create or update a management user", generatedEmail);
            createManagementUser(userRegistration, generatedEmail);
            constructExistingUserEmail(userRegistration, generatedEmail, leaderUsername);
        }
    }

    public String createCalendarEvent(CalendarEventCreateRequest calendarEventCreateRequest) {
        IGraphServiceClient graphClient = getGraphServiceClient();

        Option filterOption = new QueryOption("$filter", "userPrincipalName eq '" + microsoftCalendarEmail + "'");
        IUserCollectionPage user = graphClient.users().buildRequest(Collections.singletonList(filterOption)).get();

        //we can create the user account without issue
        if (user.getCurrentPage().isEmpty()) {
            log.warn("[O365] No O365 account {} exists, so cannot create calendar event", microsoftCalendarEmail);
            throw new NotFoundException(microsoftCalendarEmail + " not found");
        }

        com.microsoft.graph.models.extensions.User eventsUser = user.getCurrentPage().get(0);

        // Event
        com.microsoft.graph.models.extensions.Event event = new com.microsoft.graph.models.extensions.Event();

        // Subject
        if (appEnvironment.equals("production")) {
            event.subject = calendarEventCreateRequest.getSubject();
        } else {
            event.subject = "[Test] " + calendarEventCreateRequest.getSubject();
        }

        // Body
        com.microsoft.graph.models.extensions.ItemBody body = new com.microsoft.graph.models.extensions.ItemBody();
        body.contentType = com.microsoft.graph.models.generated.BodyType.HTML;
        body.content = calendarEventCreateRequest.getBodyContent() == null ? "" : calendarEventCreateRequest.getBodyContent();
        if (calendarEventCreateRequest.getEventLink() != null) {
            body.content += "<p>" + calendarEventCreateRequest.getEventLink() + "</p>";
        }
        event.body = body;
        event.bodyPreview = calendarEventCreateRequest.getBodyPreview() == null ? "" : calendarEventCreateRequest.getBodyPreview();

        // Start and End Date/Time
        com.microsoft.graph.models.extensions.DateTimeTimeZone start = new com.microsoft.graph.models.extensions.DateTimeTimeZone();
        start.dateTime = calendarEventCreateRequest.getEventStartDateTime();
        start.timeZone = calendarEventCreateRequest.getEventTimeZone() == null ? AppTimeZone.EasternStandardTime : calendarEventCreateRequest.getEventTimeZone();
        event.start = start;
        com.microsoft.graph.models.extensions.DateTimeTimeZone end = new com.microsoft.graph.models.extensions.DateTimeTimeZone();
        end.dateTime = calendarEventCreateRequest.getEventEndDateTime();
        end.timeZone = calendarEventCreateRequest.getEventTimeZone() == null ? AppTimeZone.EasternStandardTime : calendarEventCreateRequest.getEventTimeZone();
        event.end = end;

        // Location
        com.microsoft.graph.models.extensions.Location location = new com.microsoft.graph.models.extensions.Location();
        com.microsoft.graph.models.extensions.PhysicalAddress physicalAddress = new com.microsoft.graph.models.extensions.PhysicalAddress();
        physicalAddress.street = calendarEventCreateRequest.getLocationAddress();
        physicalAddress.city = calendarEventCreateRequest.getLocationCity();
        physicalAddress.state = calendarEventCreateRequest.getLocationState();
        physicalAddress.postalCode = calendarEventCreateRequest.getLocationPostalCode();
        location.displayName = calendarEventCreateRequest.getLocationDisplayName();
        location.address = physicalAddress;
        event.location = location;

        LinkedList<com.microsoft.graph.models.extensions.Attendee> attendeesLinkedList = new LinkedList<>();
        if (calendarEventCreateRequest.getAttendeesList() == null) {
            calendarEventCreateRequest.setAttendeesList(new ArrayList<>());
        }
        if (!calendarEventCreateRequest.getAttendeesList().isEmpty()) {
            for (CalendarEventAttendee calendarEventAttendee : calendarEventCreateRequest.getAttendeesList()) {
                com.microsoft.graph.models.extensions.Attendee attendee = new com.microsoft.graph.models.extensions.Attendee();
                com.microsoft.graph.models.extensions.EmailAddress emailAddress = new com.microsoft.graph.models.extensions.EmailAddress();
                emailAddress.address = calendarEventAttendee.getEmailAddress();
                emailAddress.name = calendarEventAttendee.getName();
                attendee.emailAddress = emailAddress;
                attendeesLinkedList.add(attendee);
            }
        }
        event.attendees = attendeesLinkedList;

        if (calendarEventCreateRequest.getOrganizer() == null) {
            // Events user is the organizer
            com.microsoft.graph.models.extensions.Recipient organizer = new com.microsoft.graph.models.extensions.Recipient();
            com.microsoft.graph.models.extensions.EmailAddress organizerEmailAddress = new com.microsoft.graph.models.extensions.EmailAddress();
            organizerEmailAddress.address = microsoftCalendarEmail;
            organizerEmailAddress.name = eventsUser.displayName;
            organizer.emailAddress = organizerEmailAddress;
            event.organizer = organizer;
        } else {
            // Add the events user as an attendee
            com.microsoft.graph.models.extensions.Attendee attendee = new com.microsoft.graph.models.extensions.Attendee();
            com.microsoft.graph.models.extensions.EmailAddress attendeeEmailAddress = new com.microsoft.graph.models.extensions.EmailAddress();
            attendeeEmailAddress.address = microsoftCalendarEmail;
            attendeeEmailAddress.name = eventsUser.displayName;
            attendee.emailAddress = attendeeEmailAddress;
            attendeesLinkedList.add(attendee);

            // Add the custom organizer
            com.microsoft.graph.models.extensions.Recipient organizer = new com.microsoft.graph.models.extensions.Recipient();
            com.microsoft.graph.models.extensions.EmailAddress organizerEmailAddress = new com.microsoft.graph.models.extensions.EmailAddress();
            organizerEmailAddress.address = calendarEventCreateRequest.getOrganizer().getEmailAddress();
            organizerEmailAddress.name = calendarEventCreateRequest.getOrganizer().getName();
            organizer.emailAddress = organizerEmailAddress;
            event.organizer = organizer;
        }

        com.microsoft.graph.models.extensions.Event savedEvent = graphClient.users(eventsUser.id)
            .events()
            .buildRequest()
            .post(event);
        return savedEvent.id;
    }

    private IGraphServiceClient getGraphServiceClient() {
        GraphAuthenticationProvider authProvider = new GraphAuthenticationProvider(
            microsoftClientId,
            SCOPES,
            microsoftClientSecret,
            microsoftTenantId,
            NationalCloud.Global
        );
        return GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
    }

    public void deleteCalendarEvent(String eventId) {
        IGraphServiceClient graphClient = getGraphServiceClient();

        Option filterOption = new QueryOption("$filter", "userPrincipalName eq '" + microsoftCalendarEmail + "'");
        IUserCollectionPage user = graphClient.users().buildRequest(Collections.singletonList(filterOption)).get();

        //we can create the user account without issue
        if (user.getCurrentPage().isEmpty()) {
            log.warn("[O365] No O365 account {} exists, so cannot create calendar event", microsoftCalendarEmail);
            throw new NotFoundException(microsoftCalendarEmail + " not found");
        }

        com.microsoft.graph.models.extensions.User eventsUser = user.getCurrentPage().get(0);

        graphClient.users(eventsUser.id)
            .events(eventId)
            .buildRequest()
            .delete();
    }

    private com.microsoft.graph.models.extensions.User createMicrosoftUser(UserRegistration userRegistration) {
        com.microsoft.graph.models.extensions.User microsoftUser = new com.microsoft.graph.models.extensions.User();

        microsoftUser.accountEnabled = true;
        microsoftUser.givenName = sanitizeFirstName(userRegistration);
        microsoftUser.surname = sanitizeLastName(userRegistration);
        microsoftUser.displayName = sanitizeDisplayName(userRegistration);
        microsoftUser.mailNickname = "Unknown";
        microsoftUser.userPrincipalName = generateEmailAddress(userRegistration);

        microsoftUser.passwordProfile = getPasswordProfile();

        return microsoftUser;
    }

    private PasswordProfile getPasswordProfile() {
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.forceChangePasswordNextSignIn = true;
        passwordProfile.password = generateRandomSpecialCharacters(10);
        return passwordProfile;
    }

    public void updateMicrosoftUser(User user) {
        // Only sync O365 users in production
        if (Boolean.TRUE.equals(microsoftConfig.isOffice365SyncEnabled())) {
            try {
                GraphAuthenticationProvider authProvider = new GraphAuthenticationProvider(microsoftClientId, SCOPES, microsoftClientSecret, microsoftTenantId, NationalCloud.Global);
                log.info("[O365] Start updating an Active Directory user for {} {}", user.getFirstName(), user.getLastName());

                IGraphServiceClient graphClient = GraphServiceClient
                    .builder()
                    .authenticationProvider(authProvider)
                    .buildClient();

                // Check that the user exists
                log.info("[O365] Checking if the account {} currently exists in Active Directory", user.getUsername());
                Option filterOption = new QueryOption("$filter", "userPrincipalName eq '" + user.getUsername() + "'");
                IUserCollectionPage microsoftUsers = graphClient.users().buildRequest(Collections.singletonList(filterOption)).get();

                // We can create the user account without issue
                if (!microsoftUsers.getCurrentPage().isEmpty()) {
                    // Get titles to get the string values
                    Optional<Title> titleOptional = titleDao.findById(user.getTitleId());

                    if (titleOptional.isPresent()) {
                        log.info("[O365] Found a matching Active Directory User: {}", user.getDisplayName());
                        com.microsoft.graph.models.extensions.User microsoftUser = microsoftUsers.getCurrentPage().get(0);

                        // Update specific user fields
                        com.microsoft.graph.models.extensions.User userUpdate = new com.microsoft.graph.models.extensions.User();
                        if (user.getTeam() != null) {
                            userUpdate.department = user.getTeam().getGroup().getChurch().getName();
                        }
                        userUpdate.jobTitle = titleOptional.get().getName();
                        userUpdate.usageLocation = "US";
                        graphClient.users(microsoftUser.id)
                            .buildRequest()
                            .patch(userUpdate);
                        log.info("[O365] Updated Active Directory User: {}", user.getDisplayName());
                    } else {
                        log.error("[O365] Could not load titles");
                    }
                } else {
                    log.warn("[O365] {} does not exist in Active Directory", user.getUsername());
                }
            } catch (Exception e) {
                log.error("[O365] Microsoft user update failed: {}", e.getMessage());
            }
        }
    }

    public String generateEmailAddress(UserRegistration userRegistration) {
        // First name should be one name only
        String[] firstNamesArray = sanitizeFirstName(userRegistration).split(" ");
        String firstNameOnly = firstNamesArray[0].trim();

        // Last name can be multiple names, but they will be separated with a dash
        String sanitizedLastName = sanitizeLastName(userRegistration).replace(' ', '-').trim();

        return firstNameOnly + '.' + sanitizedLastName + "@" + microsoftO365Domain;
    }

    public String sanitizeFirstName(UserRegistration userRegistration) {
        return StringUtils.capitalize(StringUtil.sanitizeString(userRegistration.getFirstName()));
    }

    public String sanitizeDisplayName(UserRegistration userRegistration) {
        return sanitizeFirstName(userRegistration) + ' ' + sanitizeLastName(userRegistration);
    }

    public String sanitizeLastName(UserRegistration userRegistration) {
        return StringUtils.capitalize(StringUtil.sanitizeString(userRegistration.getLastName()).trim());
    }

    private String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
            .build();
        return pwdGenerator.generate(length) + "1";
    }

    private String generateRandomStrongPassword(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
            .build();
        return pwdGenerator.generate(length) + "1@";
    }

    private void constructPasswordResetEmail(User user, PasswordProfile passwordProfile) throws MessagingException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);

        ArrayList<String> emails = new ArrayList<>();
        emails.add(user.getRecoveryEmail());

        helper.setTo(emails.toArray(new String[0]));
        helper.setSubject(microsoftTenantName + " Account Password Reset");
        helper.setText(getAccountEmailText(user.getUsername(), passwordProfile.password, false));
        helper.setFrom("AppAdmin@" + microsoftO365Domain);
        helper.setReplyTo("AppSupport@" + microsoftO365Domain);
        helper.setBcc("AppSupport@" + microsoftO365Domain);

        javaMailSender.send(mail);
    }

    private String getAccountEmailText(String username, String password, Boolean isNew) {
        return microsoftTenantGreeting + ",\n\n" +
            "A " + microsoftTenantName + " account has been " + (Boolean.TRUE.equals(isNew) ? "created" : "updated") + " for you.\n\n" +
            "Username: " + username + "\n" +
            "Temporary Password: " + password + "\n\n" +
            "Login Steps:\n" +
            "1. Please go to https://www.office.com on desktop and log in.\n" +
            "2. If you are presented with an option of logging in with a \"work\" or \"personal\" account, please choose a \"work\" account.\n" +
            "3. Enter your username: " + username + "\n" +
            "4. Enter the temporary password: " + password + "\n" +
            "5. Next, you will be prompted to replace the temporary password and create your new password.\n" +
            "6. You should now have access to " + microsoftTenantName + " apps.\n\n" +
            "If you have any problems or questions, please email AppSupport@" + microsoftO365Domain + ".";
    }

    private void constructNewUserEmail(UserRegistration userRegistration, com.microsoft.graph.models.extensions.User microsoftUser) {
        constructNewUserEmail(userRegistration, microsoftUser, "");
    }

    private void constructNewUserEmail(UserRegistration userRegistration, com.microsoft.graph.models.extensions.User microsoftUser, String leaderEmail) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);

            ArrayList<String> emails = new ArrayList<>();
            // When a leader creates the account, the member's email will not be given, but it can be sent to the leader.
            if (!Objects.equals(userRegistration.getEmail(), "")) {
                emails.add(userRegistration.getEmail());
            }
            if (!Objects.equals(leaderEmail, "")) {
                emails.add(leaderEmail);
            }

            if (helper.getMimeMessage() != null) {
                helper.setTo(emails.toArray(new String[emails.size()]));
                helper.setSubject("Your " + microsoftTenantName + " Account Is Ready!");
                helper.setText(getAccountEmailText(microsoftUser.userPrincipalName, microsoftUser.passwordProfile.password, true));
                helper.setFrom("AppAdmin@" + microsoftO365Domain);
                helper.setReplyTo("AppSupport@" + microsoftO365Domain);
            }

            javaMailSender.send(mail);

            log.info("Emailed {} to log into new account", userRegistration.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void constructExistingUserEmail(UserRegistration userRegistration, String generatedEmail) {
        constructExistingUserEmail(userRegistration, generatedEmail, "");
    }

    private void constructExistingUserEmail(UserRegistration userRegistration, String generatedEmail, String leaderUsername) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);

            ArrayList<String> emails = new ArrayList<>();
            // When a leader creates the account, the member's email will not be given, but it can be sent to the leader.
            if (!Objects.equals(userRegistration.getEmail(), "")) {
                emails.add(userRegistration.getEmail());
            }
            if (!Objects.equals(leaderUsername, "")) {
                emails.add(leaderUsername);
            }

            if (helper.getMimeMessage() != null) {
                helper.setTo(emails.toArray(new String[emails.size()]));
                helper.setSubject("Your " + microsoftTenantName + " Account Already Exists");
                helper.setText(
                    microsoftTenantGreeting + ",\n\n" +
                        "Your " + microsoftTenantName + " account was created previously. You may login with the information below:\n\n" +
                        "Username: " + generatedEmail + "\n" +
                        "Password: ********\n\n" +
                        "If you have any problems or questions, please email AppSupport@" + microsoftO365Domain
                );
                helper.setFrom("AppAdmin@" + microsoftO365Domain);
                helper.setReplyTo("AppSupport@" + microsoftO365Domain);
            }

            javaMailSender.send(mail);

            log.info("Emailed {} to log into existing account", userRegistration.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void constructUserRegistrationFailureEmail(UserRegistration userRegistration, Church branch, String generatedEmail, String reason) {
        constructUserRegistrationFailureEmail(userRegistration, branch, generatedEmail, reason, "");
    }

    private void constructUserRegistrationFailureEmail(UserRegistration userRegistration, Church branch, String generatedEmail, String reason, String leaderUsername) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);

            if (helper.getMimeMessage() != null) {
                String gender = userRegistration.getGender();

                // Add branch leader information to the email
                User leaderOne = null;
                if (branch.getLeaderId() != null) {
                    leaderOne = userDao.getOne(branch.getLeaderId());
                }
                User leaderTwo = null;
                if (branch.getLeaderTwoId() != null) {
                    leaderTwo = userDao.getOne(branch.getLeaderTwoId());
                }

                helper.setTo("AppSupport@" + microsoftO365Domain);
                helper.setSubject("[O365] " + userRegistration.getFirstName() + " " + userRegistration.getLastName() + " User Registration");

                // Allow leaders who create accounts to get the password reset email just in case
                if (!Objects.equals(leaderUsername, "")) {
                    helper.setCc(new String[]{leaderUsername, userRegistration.getEmail()});
                } else {
                    helper.setCc(userRegistration.getEmail());
                }

                String messageText = microsoftTenantGreeting + ",\n\n" +
                    "A " + microsoftTenantName + " account was not able to be generated for this user:\n\n" +
                    "Reason: " + reason + "\n\n" +
                    "User Information\n\n" +
                    "First Name: " + userRegistration.getFirstName() + "\n" +
                    "Last Name: " + userRegistration.getLastName() + "\n" +
                    "Gender: " + (gender != null ? gender : "No Answer") + "\n" +
                    "Email: " + userRegistration.getEmail() + "\n" +
                    "New Account: " + generatedEmail + "\n" +
                    "Branch Leader Information\n\n" +
                    "Branch Name: " + (branch == null ? "Unknown" : branch.getName()) + "\n\n" +
                    "Branch Id: " + userRegistration.getChurchId() + "\n\n";
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
                helper.setFrom("AppAdmin@" + microsoftO365Domain);
                helper.setReplyTo(userRegistration.getEmail());
            }

            javaMailSender.send(mail);

            log.error("[O365] Created support ticket");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User createManagementUser(UserRegistration userRegistration, String username) {
        // Update the user object with the new email address if account exists in our system. If not create account.
        Optional<org.zionusa.management.domain.User> userOptional = userDao.getUserByUsernameIgnoreCase(username);
        Optional<org.zionusa.management.domain.User> nonZionUsaUserOptional = userDao.getUserByUsernameIgnoreCase(StringUtil.sanitizeEmail(userRegistration.getEmail()));

        if (userOptional.isPresent()) {
            // User hasO365 account
            org.zionusa.management.domain.User user = userOptional.get();
            log.info("[O365] Updating existing management user: {}", user.getUsername());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setArchived(false);
            user.setEnabled(true);
            user.setUsername(username);
            return userDao.save(user);
        } else if (nonZionUsaUserOptional.isPresent()) {
            // User had another email
            org.zionusa.management.domain.User user = nonZionUsaUserOptional.get();
            log.info("[O365] Updating existing management user: {}", user.getUsername());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setArchived(false);
            user.setEnabled(true);
            user.setUsername(username);
            return userDao.save(user);
        } else {
            Optional<Church> churchOptional = churchDao.findById(userRegistration.getChurchId());
            log.info("[O365] branchId {} branch isPresent: {}", userRegistration.getChurchId(), churchOptional.isPresent());
            Optional<Group> churchGroupOptional = groupDao.getGroupByChurchIdAndChurchGroupTrue(userRegistration.getChurchId());
            log.info("[O365] branchId {} branch group isPresent: {}", userRegistration.getChurchId(), churchGroupOptional.isPresent());

            if (churchGroupOptional.isPresent() && churchOptional.isPresent()) {

                Group churchGroup = churchGroupOptional.get();
                Team churchTeam = null;
                String gender = userRegistration.getGender().length() > 1 ? userRegistration.getGender().substring(1) : userRegistration.getGender();

                //case for when gender is still not defined
                if (gender == null) gender = "F";

                if (!churchGroup.getTeams().isEmpty())
                    churchTeam = churchGroup.getTeams().get(0);

                if (churchTeam != null) {
                    org.zionusa.management.domain.User user = new org.zionusa.management.domain.User();
                    user.setAccessId(5);
                    user.setRoleId(5);
                    user.setAccountNonExpired(true);
                    user.setAccountNonLocked(true);
                    user.setCredentialsNonExpired(true);
                    user.setArchived(false);
                    user.setEnabled(true);
                    user.setUsername(username);
                    user.setFirstName(userRegistration.getFirstName().trim());
                    user.setLastName(userRegistration.getLastName().trim());
                    user.setTeamId(churchTeam.getId());
                    user.setGender(gender.toUpperCase());
                    user.setRecoveryEmail(StringUtil.sanitizeEmail(userRegistration.getEmail()));
                    user.setLanguagePreference("en");
                    user.setGaGrader(false);
                    user.setReadyGrader(false);
                    user.setTeacher(false);
                    user.setTypeId(EUserType.DEFAULT);

                    if (gender.equalsIgnoreCase("M")) {
                        user.setTitleId(7);
                    } else {
                        user.setTitleId(8);
                    }

                    return userDao.save(user);
                } else {
                    log.error("[O365] church team is not there");
                }
            } else {
                log.error("[O365] churchId {} is not available", userRegistration.getChurchId());
            }
        }
        Church church = churchDao.getOne(userRegistration.getChurchId());
        String reason = "Your sign up request has been received. You should receive an email within 24 hours with details of how to sign in";
        constructUserRegistrationFailureEmail(userRegistration, church, username, reason);
        return null;
    }
}
