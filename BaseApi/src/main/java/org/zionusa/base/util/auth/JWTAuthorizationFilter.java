package org.zionusa.base.util.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.zionusa.base.util.auth.SecurityConstants.*;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final String environment;

    private final String jwtSecret;

    public JWTAuthorizationFilter(AuthenticationManager authManager, String environment, String jwtSecret) {
        super(authManager);
        this.environment = environment;
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(AUTHORIZATION);

        if (token != null && jwtSecret != null) {
            // parse the token.
            Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

            if (!environment.equals("test") && claims.getExpiration().before(new Date())) return null;

            String user = claims.getSubject();

            if (user != null && claims.get(AUTHENTICATED_USER) != null) {
                String authorityString = (String) claims.get(AUTHORITY_STRING);

                //remove brackets
                authorityString = authorityString.substring(1, authorityString.length() - 1);

                AuthenticatedUser authenticatedUser = createAuthenticatedUser((LinkedHashMap<String, Object>) claims.get(AUTHENTICATED_USER), authorityString);
                if (authenticatedUser != null) {
                    return new UsernamePasswordAuthenticationToken(authenticatedUser, null, AuthorityUtils.createAuthorityList(authorityString));
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private AuthenticatedUser createAuthenticatedUser(LinkedHashMap<String, Object> authUserClaims, String authorityString) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authorityString);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser((String) authUserClaims.get("username"), "", authorities);
        authenticatedUser.setId((Integer) authUserClaims.get("id"));
        if (authUserClaims.containsKey("parentChurchId"))
            authenticatedUser.setParentChurchId((Integer) authUserClaims.get("parentChurchId"));
        if (authUserClaims.containsKey("associationId"))
            authenticatedUser.setAssociationId((Integer) authUserClaims.get("associationId"));
        authenticatedUser.setUserApplicationRoles((List<String>) authUserClaims.get("userApplicationRoles"));
        authenticatedUser.setBranchId((Integer) authUserClaims.get("churchId"));
        authenticatedUser.setBranchName((String) authUserClaims.get("churchName"));
        authenticatedUser.setChurchId((Integer) authUserClaims.get("churchId"));
        authenticatedUser.setGroupId((Integer) authUserClaims.get("groupId"));
        authenticatedUser.setTeamId((Integer) authUserClaims.get("teamId"));
        authenticatedUser.setDisplayName((String) authUserClaims.get("displayName"));
        authenticatedUser.setRole((String) authUserClaims.get("role"));
        authenticatedUser.setAccess((String) authUserClaims.get("access"));
        authenticatedUser.setTeamName((String) authUserClaims.get("teamName"));
        authenticatedUser.setGroupName((String) authUserClaims.get("groupName"));
        authenticatedUser.setChurchName((String) authUserClaims.get("churchName"));
        //authenticatedUser.setGroupEffectiveDate(new Date((String) authUserClaims.get("groupEffectiveDate")));

        // backwards comparability
        if (authenticatedUser.getUserApplicationRoles() == null)
            authenticatedUser.setUserApplicationRoles(new ArrayList<>());

        return authenticatedUser;
    }
}
