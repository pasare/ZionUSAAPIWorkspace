package org.zionusa.biblestudy.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.zionusa.base.util.auth.AuthenticatedUser;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        AuthenticatedUser principal = new AuthenticatedUser("aname", "apassword", AuthorityUtils.createAuthorityList(customUser.access()));

        principal.setId(Integer.valueOf(customUser.id()));
        principal.setTeamId(Integer.parseInt(customUser.teamId()));
        principal.setGroupId(Integer.parseInt(customUser.groupId()));
        principal.setChurchId(Integer.parseInt(customUser.churchId()));
        principal.setDisplayName(customUser.username());
        principal.setActiveDirectoryId("2121-adad-2332-adf");
        principal.setAccess(customUser.access());
        principal.setRole(customUser.role());
        principal.setTeamName("A Team");
        principal.setGroupName("A Group");
        principal.setChurchName("A Church");

        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

}
