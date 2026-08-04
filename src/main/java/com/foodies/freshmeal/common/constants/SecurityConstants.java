package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : SecurityConstants
 * ============================================================================
 *
 * Centralized security-related constants used throughout the FreshMeal
 * application.
 *
 * <p>
 * This class contains authentication schemes, JWT configuration,
 * authorization headers, roles, permissions and security attributes.
 * </p>
 *
 * <p>
 * Business-specific security rules should not be placed here.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class SecurityConstants {

    /**
     * Private constructor.
     */
    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Authentication Schemes
    // =========================================================

    public static final String BEARER = "Bearer";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String BASIC = "Basic";

    // =========================================================
    // JWT
    // =========================================================

    public static final String JWT = "JWT";

    public static final String JWT_TOKEN = "jwtToken";

    public static final String TOKEN = "token";

    public static final String REFRESH_TOKEN = "refreshToken";

    public static final String TOKEN_TYPE = "tokenType";

    // =========================================================
    // Claims
    // =========================================================

    public static final String CLAIM_USERNAME = "username";

    public static final String CLAIM_EMAIL = "email";

    public static final String CLAIM_ROLE = "role";

    public static final String CLAIM_AUTHORITIES = "authorities";

    public static final String CLAIM_USER_ID = "userId";

    public static final String CLAIM_SESSION_ID = "sessionId";

    // =========================================================
    // HTTP Headers
    // =========================================================

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    // =========================================================
    // Security Context
    // =========================================================

    public static final String ANONYMOUS_USER = "anonymousUser";

    public static final String SYSTEM_USER = "SYSTEM";

    // =========================================================
    // Roles
    // =========================================================

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    public static final String ROLE_RESTAURANT = "ROLE_RESTAURANT";

    public static final String ROLE_DELIVERY_PARTNER = "ROLE_DELIVERY_PARTNER";

    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    // =========================================================
    // Authorities
    // =========================================================

    public static final String READ = "READ";

    public static final String WRITE = "WRITE";

    public static final String UPDATE = "UPDATE";

    public static final String DELETE = "DELETE";

    public static final String MANAGE = "MANAGE";

    // =========================================================
    // Session
    // =========================================================

    public static final String SESSION = "SESSION";

    public static final String SESSION_ID = "SESSION_ID";

}
