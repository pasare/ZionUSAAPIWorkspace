package org.zionusa.management.domain.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.enums.EUserGender;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.management.dao.*;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.UserPasswordReset;
import org.zionusa.management.domain.UserRegistration;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ChurchDao churchDao;
    private final JavaMailSender javaMailSender;
    private final UserDao userDao;
    private final UserPasswordResetDao userPasswordResetDao;
    private final UserProfileUserDao userProfileUserDao;
    private final UserRegistrationDao userRegistrationDao;

    @Value("${microsoft.o365.domain}")
    private String microsoftO365Domain;
    @Value("${microsoft.o365.webDomain}")
    private String microsoftO365WebDomain;
    @Value("${microsoft.tenant.greeting}")
    private String microsoftTenantGreeting;

    public ReportService(ChurchDao churchDao,
                         JavaMailSender javaMailSender,
                         UserDao userDao,
                         UserPasswordResetDao userPasswordResetDao,
                         UserProfileUserDao userProfileUserDao,
                         UserRegistrationDao userRegistrationDao) {
        this.churchDao = churchDao;
        this.javaMailSender = javaMailSender;
        this.userDao = userDao;
        this.userPasswordResetDao = userPasswordResetDao;
        this.userProfileUserDao = userProfileUserDao;
        this.userRegistrationDao = userRegistrationDao;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void branchUserActivityEmail(Integer branchId, String emailTo, Integer customMonthsActive) {
        try {
            int monthsActive = customMonthsActive == null ? 3 : customMonthsActive;
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);

            Optional<Church> branchOptional = churchDao.findById(branchId);

            if (!branchOptional.isPresent()) {
                // If no branch found, do not send the email
                return;
            }

            Church branch = branchOptional.get();

            // Set up the to field for the email
            List<InternetAddress> toList = new ArrayList<>();
            if (emailTo != null && !emailTo.equals("")) {
                toList.add(new InternetAddress(emailTo));
            } else {
                // Add branch leader information to the email
                if (branch.getLeaderId() != null) {
                    Optional<User> leaderOneOptional = userDao.findById(branch.getLeaderId());
                    if (leaderOneOptional.isPresent()) {
                        toList.add(new InternetAddress(leaderOneOptional.get().getUsername()));
                    }
                }
                if (branch.getLeaderTwoId() != null) {
                    Optional<User> leaderTwoOptional = userDao.findById(branch.getLeaderTwoId());
                    if (leaderTwoOptional.isPresent()) {
                        toList.add(new InternetAddress(leaderTwoOptional.get().getUsername()));
                    }
                }
            }
            helper.setTo(toList.toArray(new InternetAddress[0]));
            String reportDate = LocalDateTime.now().atZone(EZoneId.NEW_YORK.getValue())
                .format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
            helper.setSubject("[O365] " + branch.getName() + " User Report - " + reportDate + "(BranchID: " + branch.getId() + ")");
            helper.setFrom("AppAdmin@" + microsoftO365Domain);
            helper.setReplyTo("AppSupport@" + microsoftO365Domain);

            // =========================================================================================================
            // List of Open User Registrations
            // =========================================================================================================

            List<UserRegistration> userRegistrations = userRegistrationDao.findAllByChurchIdAndApprovedIsNull(branchId);
            userRegistrations.sort(Comparator.comparing(UserRegistration::getFirstName));
            StringBuilder userRegistrationsText = new StringBuilder("<ol>");

            if (!userRegistrations.isEmpty()) {
                for (UserRegistration userRegistration : userRegistrations) {
                    String displayName = userRegistration.getFirstName() + " " + userRegistration.getLastName();
                    userRegistrationsText = addBranchUserActivityString(userRegistrationsText, displayName, userRegistration.getEmail());
                }
            }

            // =========================================================================================================
            // List of Open User Password Resets
            // =========================================================================================================

            List<UserPasswordReset> userPasswordResets = userPasswordResetDao.getAllByChurchIdAndApprovedIsNull(branchId);
            userPasswordResets.sort(Comparator.comparing(UserPasswordReset::getFirstName));
            StringBuilder userPasswordResetText = new StringBuilder("<ol>");

            if (!userPasswordResets.isEmpty()) {
                for (UserPasswordReset userPasswordReset : userPasswordResets) {
                    String displayName = userPasswordReset.getFirstName() + " " + userPasswordReset.getLastName();
                    userPasswordResetText = addBranchUserActivityString(userPasswordResetText, displayName, userPasswordReset.getUserName(), userPasswordReset.getEmail());
                }
            }

            // =========================================================================================================
            // List of Users in Branch
            // =========================================================================================================

            List<User.ProfileUser> users = userProfileUserDao.findProfileUsersByChurchId(branchId);
            users.sort(Comparator.comparing(User.ProfileUser::getFirstName));

            int activeFemaleCount = 0;
            StringBuilder activeFemaleUsers = new StringBuilder("<ol>");
            int activeMaleCount = 0;
            StringBuilder activeMaleUsers = new StringBuilder("<ol>");
            int lessActiveFemaleCount = 0;
            StringBuilder lessActiveFemaleUsers = new StringBuilder("<ol>");
            int lessActiveMaleCount = 0;
            StringBuilder lessActiveMaleUsers = new StringBuilder("<ol>");
            int inactiveFemaleCount = 0;
            StringBuilder inactiveFemaleUsers = new StringBuilder("<ol>");
            int inactiveMaleCount = 0;
            StringBuilder inactiveMaleUsers = new StringBuilder("<ol>");

            for (User.ProfileUser user : users) {
                boolean isThisPeriod = false;
                boolean isThisYear = false;
                String lastLogin = "Never";
                if (user.getLastLoginDate() != null) {
                    long timestamp = (long) Integer.parseInt(user.getLastLoginDate()) * 1000;
                    LocalDateTime lastLoginDate = LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(timestamp), EZoneId.NEW_YORK.getValue());
                    lastLogin = lastLoginDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
                    isThisPeriod = lastLoginDate.isAfter(LocalDateTime.now().minusMonths(monthsActive));
                    isThisYear = lastLoginDate.isAfter(LocalDateTime.now().minusMonths(12));
                }
                if (lastLogin.equals("Never")) {
                    if (EUserGender.FEMALE.is(user.getGender())) {
                        inactiveFemaleCount += 1;
                        inactiveFemaleUsers = addBranchUserActivityString(inactiveFemaleUsers, user, lastLogin);
                    } else {
                        inactiveMaleCount += 1;
                        inactiveMaleUsers = addBranchUserActivityString(inactiveMaleUsers, user, lastLogin);
                    }
                } else if (isThisPeriod) {
                    if (EUserGender.FEMALE.is(user.getGender())) {
                        activeFemaleCount += 1;
                        activeFemaleUsers = addBranchUserActivityString(activeFemaleUsers, user, lastLogin);
                    } else {
                        activeMaleCount += 1;
                        activeMaleUsers = addBranchUserActivityString(activeMaleUsers, user, lastLogin);
                    }
                } else if (isThisYear) {
                    if (EUserGender.FEMALE.is(user.getGender())) {
                        lessActiveFemaleCount += 1;
                        lessActiveFemaleUsers = addBranchUserActivityString(lessActiveFemaleUsers, user, lastLogin);
                    } else {
                        lessActiveMaleCount += 1;
                        lessActiveMaleUsers = addBranchUserActivityString(lessActiveMaleUsers, user, lastLogin);
                    }
                } else { // More than one year
                    if (EUserGender.FEMALE.is(user.getGender())) {
                        inactiveFemaleCount += 1;
                        inactiveFemaleUsers = addBranchUserActivityString(inactiveFemaleUsers, user, lastLogin);
                    } else {
                        inactiveMaleCount += 1;
                        inactiveMaleUsers = addBranchUserActivityString(inactiveMaleUsers, user, lastLogin);
                    }
                }
            }

            // =========================================================================================================
            // Compile Report
            // =========================================================================================================


            StringBuilder messageText = new StringBuilder();
            messageText
                .append("<p>")
                .append(microsoftTenantGreeting)
                .append(",</p><p>Please review the list of members below for accuracy.</p><ul><li>")
                .append("Make changes by going to ")
                .append("<a href=\"https://management.")
                .append(microsoftO365WebDomain)
                .append("\">management.")
                .append(microsoftO365WebDomain)
                .append("</a></li><li>Or, reply to this email report any problems to <a href=\"mailto:AppSupport@")
                .append(microsoftO365Domain)
                .append("\">App Support</a></li></ul>")
                .append("<h2>Summary</h2>")
                .append("<table style=\"max-width:640px;width:100%;\"><thead><tr><th style=\"text-align:left;\">")
                .append(branch.getName())
                .append("</th><th>Female</th><th>Male</th><th>Total</th></tr></thead><tbody>")
                .append("<tr><td>Pending Sign Up</td><td>&nbsp;</td><td>&nbsp;</td><td style=\"text-align:center;\">")
                .append(userRegistrations.size())
                .append("</td></tr>")
                .append("<tr><td>Pending Password Reset</td><td>&nbsp;</td><td>&nbsp;</td><td style=\"text-align:center;\">")
                .append(userPasswordResets.size())
                .append("</td></tr>")
                .append("<tr><td>Active Users</td><td style=\"text-align:center;\">")
                .append(activeFemaleCount)
                .append("</td><td style=\"text-align:center;\">")
                .append(activeMaleCount)
                .append("</td><td style=\"text-align:center;\">")
                .append(activeFemaleCount + activeMaleCount)
                .append("</td></tr><tr><td>")
                .append("No Activity in ").append(monthsActive).append(" Months")
                .append("</td><td style=\"text-align:center;\">")
                .append(lessActiveFemaleCount)
                .append("</td><td style=\"text-align:center;\">")
                .append(lessActiveMaleCount)
                .append("</td><td style=\"text-align:center;\">")
                .append(lessActiveFemaleCount + lessActiveMaleCount)
                .append("</td></tr><tr><td>")
                .append("No Activity in Past Year")
                .append("</td><td style=\"text-align:center;\">")
                .append(inactiveFemaleCount)
                .append("</td><td style=\"text-align:center;\">")
                .append(inactiveMaleCount)
                .append("</td><td style=\"text-align:center;\">")
                .append(inactiveFemaleCount + inactiveMaleCount)
                .append("</td></tr></tbody><tfooter><tr><th style=\"text-align:left;\">Totals</th><th>")
                .append(activeFemaleCount + lessActiveFemaleCount + inactiveFemaleCount)
                .append("</th><th>")
                .append(activeMaleCount + lessActiveMaleCount + inactiveMaleCount)
                .append("</th><th>")
                .append(activeFemaleCount + lessActiveFemaleCount + inactiveFemaleCount + activeMaleCount + lessActiveMaleCount + inactiveMaleCount)
                .append("</th></tr></tfooter></table>")
                .append("<h2>Details</h2>");

            messageText = addReportSection(messageText, "User Sign Up Request(s)", userRegistrations.size());
            userRegistrationsText.append("</ol><hr />");
            messageText.append(userRegistrationsText);

            messageText = addReportSection(messageText, "Password Reset Request(s)", userPasswordResets.size());
            userPasswordResetText.append("</ol><hr />");
            messageText.append(userPasswordResetText);

            messageText = addReportSection(messageText, "Active Female(s)", activeFemaleCount);
            activeFemaleUsers.append("</ol><hr />");
            messageText.append(activeFemaleUsers);

            messageText = addReportSection(messageText, "Active Male(s)", activeMaleCount);
            activeMaleUsers.append("</ol><hr />");
            messageText.append(activeMaleUsers);

            messageText = addReportSection(messageText, "No Activity in " + monthsActive + " Months (Female)", lessActiveFemaleCount);
            lessActiveFemaleUsers.append("</ol><hr />");
            messageText.append(lessActiveFemaleUsers);

            messageText = addReportSection(messageText, "No Activity in " + monthsActive + " Months (Female)", lessActiveMaleCount);
            lessActiveMaleUsers.append("</ol><hr />");
            messageText.append(lessActiveMaleUsers);

            messageText = addReportSection(messageText, "No Activity Past Year (Female)", inactiveFemaleCount);
            inactiveFemaleUsers.append("</ol><hr />");
            messageText.append(inactiveFemaleUsers);

            messageText = addReportSection(messageText, "No Activity Past Year (Male)", inactiveMaleCount);
            inactiveMaleUsers.append("</ol><hr />");
            messageText.append(inactiveMaleUsers);

            helper.setText(messageText.toString(), true);
            log.info(messageText.toString());

            javaMailSender.send(mail);

            log.info("[O365] Emailed Branch User Activity Email for {}", branch.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder addReportSection(StringBuilder stringBuilder, String title, int count) {
        return stringBuilder
            .append("<h3>")
            .append(count)
            .append(" - ")
            .append(title)
            .append("</h3>");
    }

    private StringBuilder addBranchUserActivityString(StringBuilder stringBuilder, User.ProfileUser user, String lastLogin) {
        return stringBuilder
            .append("<li>")
            .append(user.getDisplayName())
            .append(" | id: ")
            .append(user.getId())
            .append(" (")
            .append(lastLogin)
            .append(")</li>");
    }

    private StringBuilder addBranchUserActivityString(StringBuilder stringBuilder, String displayName, String email) {
        return stringBuilder
            .append("<li>Name: ")
            .append(displayName)
            .append("<ul><li>Email: ")
            .append(email)
            .append("</li></ul></li>");
    }

    private StringBuilder addBranchUserActivityString(StringBuilder stringBuilder, String displayName, String username, String email) {
        return stringBuilder
            .append("<li>")
            .append(displayName)
            .append("</li><li><ul><li>Username: ")
            .append(username)
            .append("</li><li>Email: ")
            .append(email)
            .append("</li></ul></li>");
    }
}
