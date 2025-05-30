package org.zionusa.management.util.auth;

public class SecurityConstants {
    public static final String SECRET = "1918Born1948Baptized1964Established1985Ascended";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/login";
    public static final String AUTH_SET_URL = "/ad-auth";
    public static final String JWTTOKENCOOKIENAME = "JWT-MANAGEMENT-TOKEN";
    public static final String AUTHORITY_STRING = "authorities";
    public static final String AUTHENTICATED_USER = "authenticatedUser";
//    public static final String TENANT_GUID = "cf071c71-d594-4feb-aa63-1298df8a204c";
}
