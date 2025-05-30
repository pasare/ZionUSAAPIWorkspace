package org.zionusa.management.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.management.domain.ApplicationRole;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.UserApplicationRole;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MigrationService {
    private final ApplicationRoleService applicationRoleService;
    private final UserService userService;

    public MigrationService(ApplicationRoleService applicationRoleService,
                            UserService userService) {
        this.applicationRoleService = applicationRoleService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void adminTriggerMigrateUserApplicationRoles() {
        migrateUserApplicationRoles();
    }

    // Cron should use this one directly
    public void migrateUserApplicationRoles() {
        migrateTeacherApplicationRoles();
        migrateGAGraderApplicationRoles();
        migrateReadyGraderApplicationRoles();
        migrateTheologicalStudentApplicationRoles();
        migrateOverseerAccessApplicationRoles();
        migrateAdminAccessApplicationRoles();
        migrateBranchAccessApplicationRoles();
        migrateGroupAccessApplicationRoles();
        migrateTeamAccessApplicationRoles();
        // TODO: Events application roles
    }

    public void migrateTeacherApplicationRoles() {
        // get users who have teacher access
        List<User> users = userService.getAllTeachers();

        //get a teacher access application role
        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName(EApplicationRole.STUDY_TEACHER.getValue());
        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateGAGraderApplicationRoles() {
        List<User> users = userService.getGaGraders();

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName(EApplicationRole.STUDY_GA_GRADER.getValue());

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateTheologicalStudentApplicationRoles() {
        List<User> users = userService.getTheologicalStudents();

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName(EApplicationRole.STUDY_THEOLOGICAL_STUDENT.getValue());

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateReadyGraderApplicationRoles() {
        List<User> users = userService.getAllReadyGraders();

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName(EApplicationRole.STUDY_READY_GRADER.getValue());

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateOverseerAccessApplicationRoles() {
        // id 6 for overseer
        List<User> users = userService.getByAccessId(6);

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName("OVERSEER_ACCESS");

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateAdminAccessApplicationRoles() {
        // id 1 for admin
        List<User> users = userService.getByAccessId(1);

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName("ADMIN_ACCESS");

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateBranchAccessApplicationRoles() {
        // id 2 for church
        List<User> users = userService.getByAccessId(2);

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName("BRANCH_ACCESS");

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateGroupAccessApplicationRoles() {
        // id 3 for group
        List<User> users = userService.getByAccessId(3);

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName("GROUP_ACCESS");

        processApplicationRoleMigration(users, applicationRole);
    }

    public void migrateTeamAccessApplicationRoles() {
        // id 4 for team
        List<User> users = userService.getByAccessId(4);

        ApplicationRole applicationRole = applicationRoleService.getApplicationRoleByName("TEAM_ACCESS");

        processApplicationRoleMigration(users, applicationRole);
    }

    public void processApplicationRoleMigration(List<User> users, ApplicationRole applicationRole) {
        // filter out teachers that already has the teacher access tag
        List<User> filteredUsers = users.stream().filter((user) -> {
            List<UserApplicationRole> userApplicationRoles = user.getApplicationRoles();

            List<UserApplicationRole> queriesUserApplicationRoles =
                    userApplicationRoles.stream().filter(userApplicationRole -> userApplicationRole.getApplicationRole().getName().equals(applicationRole.getName())).collect(Collectors.toList());
            return queriesUserApplicationRoles.isEmpty();
        }).collect(Collectors.toList());

        // save teacher application role for the filtered teachers
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();
        filteredUsers.forEach((user) -> {
            UserApplicationRole teacherUserApplicationRole = new UserApplicationRole(
                    user.getId(),
                    applicationRole
            );

            if (userService.saveUserApplicationRole(user.getId(), teacherUserApplicationRole)) {
                success.getAndIncrement();
            } else {
                failure.getAndIncrement();
            }
        });
        System.out.println("Application Role: " + applicationRole.getName() + ", Success: " + success.toString() + ", " +
                "Failure: " + failure.toString());
    }
}
