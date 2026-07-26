package com.foodies.freshmeal.common.constants;

/**
 * ===========================================================
 * Response Message Constants
 * ===========================================================
 *
 * Centralized response messages used throughout the application.
 *
 * Keeping all response messages in one place improves
 * consistency, readability, and maintainability.
 *
 * @author Pankaj Kumar
 * @version 2.0
 *          ===========================================================
 */
public final class ResponseMessageConstants {

    /**
     * Prevent instantiation.
     */
    private ResponseMessageConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ===========================================================
    // Success Messages
    // ===========================================================

    public static final String SUCCESS = "Operation completed successfully.";

    public static final String CREATED = "Resource created successfully.";

    public static final String UPDATED = "Resource updated successfully.";

    public static final String DELETED = "Resource deleted successfully.";

    public static final String ACCEPTED = "Request accepted successfully.";

    public static final String NO_CONTENT = "No content available.";

    // ===========================================================
    // Client Error Messages
    // ===========================================================

    public static final String BAD_REQUEST = "Invalid request.";

    public static final String UNAUTHORIZED = "Unauthorized access.";

    public static final String FORBIDDEN = "Access denied.";

    public static final String NOT_FOUND = "Requested resource not found.";

    public static final String CONFLICT = "Resource already exists.";

    public static final String METHOD_NOT_ALLOWED = "HTTP method not allowed.";

    public static final String PAYLOAD_TOO_LARGE = "Uploaded payload exceeds the maximum allowed size.";

    // ===========================================================
    // Server Error Messages
    // ===========================================================

    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";

}
