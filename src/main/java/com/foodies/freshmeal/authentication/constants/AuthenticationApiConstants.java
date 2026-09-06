package com.foodies.freshmeal.authentication.constants;

/**
 * ============================================================================
 * Authentication API Constants
 * ============================================================================
 *
 * Centralized endpoint constants for the FreshMeal Authentication module.
 *
 * <p>
 * These constants define the relative paths exposed below the authentication
 * base URL configured in {@code ApiBaseConstants}.
 * </p>
 *
 * <h3>Authentication Flow</h3>
 *
 * <ul>
 * <li>Registration</li>
 * <li>Email OTP verification</li>
 * <li>Login</li>
 * <li>Access token refresh</li>
 * <li>Logout</li>
 * <li>Password change</li>
 * <li>Password recovery</li>
 * <li>Password reset</li>
 * </ul>
 *
 * <p>
 * Token validation and token generation do not have public endpoints.
 * Token generation happens as part of login/refresh, while access-token
 * validation is performed internally by Spring Security.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class AuthenticationApiConstants {

    /**
     * Prevents instantiation of this utility class.
     */
    private AuthenticationApiConstants() {
        throw new UnsupportedOperationException(
                "AuthenticationApiConstants is a utility class and cannot be instantiated.");
    }

    /**
     * Register a new FreshMeal user.
     */
    public static final String REGISTER = "/register";

    /**
     * Verify the user's email address using the registration OTP.
     */
    public static final String VERIFY_EMAIL_OTP = "/verify-email-otp";

    /**
     * Authenticate a user and issue authentication tokens.
     */
    public static final String LOGIN = "/login";

    /**
     * Refresh authentication tokens using a valid refresh token.
     */
    public static final String REFRESH = "/refresh";

    /**
     * Logout the currently authenticated user.
     */
    public static final String LOGOUT = "/logout";

    /**
     * Change the password of the currently authenticated user.
     */
    public static final String CHANGE_PASSWORD = "/change-password";

    /**
     * Initiate password recovery.
     */
    public static final String FORGOT_PASSWORD = "/forgot-password";

    /**
     * Reset the password using a valid password-reset token.
     */
    public static final String RESET_PASSWORD = "/reset-password";

    /**
     * Resend the email OTP for email verification.
     */
    public static final String RESEND_EMAIL_OTP = "/resend-email-otp";

    public static final String REFRESH_TOKEN = "/refresh-token";
}