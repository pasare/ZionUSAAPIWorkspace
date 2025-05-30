package org.zionusa.management.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.zionusa.management.domain.*;
import org.zionusa.management.domain.access.Access;
import org.zionusa.management.domain.role.Role;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = new User();
        user.setId(Integer.valueOf(customUser.id()));
        user.setTeamId(Integer.parseInt(customUser.teamId()));
        user.setUsername(customUser.username());
        user.setFirstName(customUser.name());
        user.setActiveDirectoryId("2121-adad-2332-adf");
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccess(new Access(1, customUser.access()));
        user.setRole(new Role(5, customUser.role()));

        Team team = new Team();
        team.setId(Integer.valueOf(customUser.teamId()));
        team.setGroupId(Integer.valueOf(customUser.groupId()));

        Group group = new Group();
        group.setId(Integer.valueOf(customUser.groupId()));
        group.setChurchId(Integer.valueOf(customUser.churchId()));

        Church church = new Church();
        church.setId(Integer.valueOf(customUser.churchId()));
        church.setName("Mock Church Name");

        user.setTeam(team);
        team.setGroup(group);
        group.setChurch(church);


        ManagementAuthenticatedUser principal = new ManagementAuthenticatedUser(user);
        Authentication auth =
            new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

}
