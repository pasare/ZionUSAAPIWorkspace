package org.zionusa.management.authorizer;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.authorizer.BaseAuthorizer;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.management.dao.UserApplicationRoleUserPermissionViewDao;
import org.zionusa.management.dao.UserPermissionDao;
import org.zionusa.management.domain.UserApplicationRoleUserPermissionForManagement;
import org.zionusa.management.domain.UserPermissionForManagement;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.zionusa.management.util.auth.SecurityConstants.SECRET;

@Service
@Transactional
public class PermissionsAuthorizer extends BaseAuthorizer<UserApplicationRoleUserPermissionForManagement, UserPermissionForManagement, Integer> {
    public PermissionsAuthorizer(
        UserApplicationRoleUserPermissionViewDao userApplicationRoleUserPermissionViewDao,
        UserPermissionDao userPermissionDao) {
        super(userApplicationRoleUserPermissionViewDao, userPermissionDao);
    }

    public String getAccessToken(AuthenticatedUser authenticatedUser) {
        List<String> applicationRoles = authenticatedUser.getUserApplicationRoles();
        List<Map<String, Boolean>> claims = checkAllPermissions(applicationRoles);
        return createJWT(authenticatedUser.getId().toString(), claims, 7920000);
    }

    public String getPermissionsToken(Integer userId, List<String> userPermissionNames) {
        return createJWT(userId.toString(), userPermissionNames, 79200000);
    }

    private String createJWT(String userId, Object claims, long ttlMillis) {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
            .setId(userId)
            .setIssuedAt(now)
            .setSubject("MY")
            .setIssuer("ZIONUSA")
            .claim("claims", claims)
            .signWith(signatureAlgorithm, signingKey);

        // If it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
