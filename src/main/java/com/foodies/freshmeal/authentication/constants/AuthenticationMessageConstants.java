package com.foodies.freshmeal.authentication.constants;

/**
 * ============================================================================
 * Authentication Message Constants
 * ============================================================================
 *
 * Centralized success messages for the Authentication module.
 *
 * <p>
 * This class provides consistent user-facing messages for successful
 * authentication, token, password, logout, and password-recovery operations.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Centralize Authentication API success messages.</li>
 * <li>Prevent hard-coded success messages in controllers and services.</li>
 * <li>Maintain consistent terminology across the Authentication module.</li>
 * <li>Provide stable messages that can be reused by future API
 * implementations.</li>
 * </ul>
 *
 * <p>
 * Authentication failure messages are intentionally excluded from this class.
 * Authentication business failures are maintained by
 * {@link AuthenticationErrorConstants}.
 * </p>
 *
 * <p>
 * This class also does not contain Authorization messages. Authorization
 * messages will belong to the Authorization module.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class AuthenticationMessageConstants {

    /**
     * Prevents instantiation of this constants class.
     */
    private AuthenticationMessageConstants() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated");
    }

    // =========================================================================
    // Login Messages
    // =========================================================================

    /**
     * Indicates that the user has been authenticated successfully.
     */
    public static final String LOGIN_SUCCESSFUL = "Login successful.";

    // =========================================================================
    // Logout Messages
    // =========================================================================

    /**
     * Indicates that the user has been logged out successfully.
     */
    public static final String LOGOUT_SUCCESSFUL = "Logout successful.";

    // =========================================================================
    // Token Messages
    // =========================================================================

    /**
     * Indicates that authentication tokens have been refreshed successfully.
     */
    public static final String TOKEN_REFRESHED = "Authentication token refreshed successfully.";

    // =========================================================================
    // Password Messages
    // =========================================================================

    /**
     * Indicates that the authenticated user's password was changed
     * successfully.
     */
    public static final String PASSWORD_CHANGED = "Password changed successfully.";

    // =========================================================================
    // Password Recovery Messages
    // =========================================================================

    /**
     * Indicates that a password recovery request was processed.
     *
     * <p>
     * The message intentionally does not reveal whether an account exists.
     * This helps prevent user/account enumeration through the password
     * recovery API.
     * </p>
     */
    public static final String PASSWORD_RECOVERY_REQUESTED = "If an account exists for the provided information, "
            + "password recovery instructions have been sent.";

    /**
     * Indicates that password recovery verification was completed
     * successfully.
     */
    public static final String PASSWORD_RECOVERY_VERIFIED = "Password recovery verification successful.";

    /**
     * Indicates that the password was reset successfully.
     */
    public static final String PASSWORD_RESET_SUCCESSFUL = "Password reset successful.";
}