package org.zionusa.management.util.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.zionusa.management.domain.ManagementAuthenticatedUser;
import org.zionusa.management.service.AuthenticatedUserService;
import org.zionusa.management.util.CookieUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.zionusa.management.util.auth.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final String environment;
    private final AuthenticatedUserService authenticatedUserService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, AuthenticatedUserService authenticatedUserService, String environment) {
        super(authManager);
        this.authenticatedUserService = authenticatedUserService;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = CookieUtil.getValue(request, JWTTOKENCOOKIENAME);

        if (token == null)
            token = request.getHeader(HEADER_STRING);

        if (token != null) {
            // parse the token.
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            if (!environment.equals("test") && claims.getExpiration().before(new Date())) return null;

            String user = claims.getSubject();
            String authorityString = (String) claims.get(AUTHORITY_STRING);

            //remove brackets
            authorityString = authorityString.substring(1, authorityString.length() - 1);

            if (user != null) {
                ManagementAuthenticatedUser authenticatedUser = authenticatedUserService.loadUserByUsername(user);
                if (authenticatedUser != null) {
                    return new UsernamePasswordAuthenticationToken(authenticatedUser, null, AuthorityUtils.createAuthorityList(authorityString));
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
