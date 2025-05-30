package org.zionusa.base.util.auth;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_APPLICATION_ID = "X-APPLICATION-ID";
    public static final String AUTHORITY_STRING = "authorities";
    public static final String AUTHENTICATED_USER = "authenticatedUser";
}
