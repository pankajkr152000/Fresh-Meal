package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : HeaderConstants
 * ============================================================================
 *
 * Centralized HTTP header constants used throughout the FreshMeal application.
 *
 * <p>
 * This class provides standard HTTP request and response header names to
 * eliminate hardcoded strings and ensure consistency.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class HeaderConstants {

    /**
     * Private constructor.
     */
    private HeaderConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Standard HTTP Headers
    // =========================================================

    public static final String ACCEPT = "Accept";

    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    public static final String AUTHORIZATION = "Authorization";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String CONNECTION = "Connection";

    public static final String HOST = "Host";

    public static final String USER_AGENT = "User-Agent";

    public static final String ORIGIN = "Origin";

    public static final String REFERER = "Referer";

    public static final String LOCATION = "Location";

    public static final String ETAG = "ETag";

    public static final String IF_NONE_MATCH = "If-None-Match";

    // =========================================================
    // Authentication Headers
    // =========================================================

    public static final String BEARER = "Bearer ";

    public static final String BASIC = "Basic ";

    // =========================================================
    // Proxy Headers
    // =========================================================

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";

    public static final String X_REAL_IP = "X-Real-IP";

    // =========================================================
    // Correlation & Tracing
    // =========================================================

    public static final String X_REQUEST_ID = "X-Request-Id";

    public static final String X_CORRELATION_ID = "X-Correlation-Id";

    // =========================================================
    // Custom FreshMeal Headers
    // =========================================================

    public static final String X_CLIENT_VERSION = "X-Client-Version";

    public static final String X_DEVICE_ID = "X-Device-Id";

    public static final String X_PLATFORM = "X-Platform";

}