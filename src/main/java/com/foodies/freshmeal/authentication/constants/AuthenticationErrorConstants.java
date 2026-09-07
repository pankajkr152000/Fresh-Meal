package com.foodies.freshmeal.authentication.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Authentication Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Authentication module.
 *
 * <p>
 * This enum defines business-level errors related to:
 * </p>
 *
 * <ul>
 * <li>Authentication and credentials</li>
 * <li>Password management</li>
 * <li>Account authentication state</li>
 * <li>Session management</li>
 * <li>Access token and JWT validation</li>
 * <li>Refresh token management</li>
 * <li>Login and logout operations</li>
 * <li>Login history</li>
 * <li>Password recovery and password reset</li>
 * </ul>
 *
 * <p>
 * Authentication-specific errors are maintained here rather than in the
 * User module. The User module remains responsible for user lifecycle and
 * account-management operations, while Authentication is responsible for
 * establishing and maintaining authenticated identity.
 * </p>
 *
 * <p>
 * Authorization-specific errors such as access denied, insufficient
 * permissions, and role/permission violations are intentionally excluded.
 * Those errors will belong to the Authorization module.
 * </p>
 *
 * ============================================================================
 *
 * Error Code Convention
 * ---------------------
 *
 * <p>
 * Authentication module errors follow the format:
 * </p>
 *
 * <pre>
 * FM - AUTH - XXXX
 * </pre>
 *
 * <ul>
 * <li><b>FM</b> - FreshMeal</li>
 * <li><b>AUTH</b> - Authentication module</li>
 * <li><b>XXXX</b> - Unique authentication error number</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum AuthenticationErrorConstants implements IBusinessError {

    // =========================================================================
    // General Authentication Errors - 001 to 009
    // =========================================================================

    AUTHENTICATION_REQUIRED(
            "FM-AUTH-001",
            "Authentication is required.",
            HttpStatusCode.UNAUTHORIZED),

    AUTHENTICATION_FAILED(
            "FM-AUTH-002",
            "Authentication failed.",
            HttpStatusCode.UNAUTHORIZED),

    AUTHENTICATION_OPERATION_NOT_ALLOWED(
            "FM-AUTH-003",
            "The requested authentication operation is not allowed.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Credential and Password Errors - 010 to 019
    // =========================================================================

    CREDENTIALS_REQUIRED(
            "FM-AUTH-010",
            "Authentication credentials are required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CREDENTIALS(
            "FM-AUTH-011",
            "Invalid username or password.",
            HttpStatusCode.UNAUTHORIZED),

    PASSWORD_REQUIRED(
            "FM-AUTH-012",
            "Password is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_PASSWORD(
            "FM-AUTH-013",
            "Invalid password.",
            HttpStatusCode.UNAUTHORIZED),

    PASSWORD_MISMATCH(
            "FM-AUTH-014",
            "Passwords do not match.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_CHANGE_REQUIRED(
            "FM-AUTH-015",
            "Password change is required.",
            HttpStatusCode.FORBIDDEN),

    PASSWORD_EXPIRED(
            "FM-AUTH-016",
            "Password has expired.",
            HttpStatusCode.FORBIDDEN),

    PASSWORD_UPDATE_FAILED(
            "FM-AUTH-017",
            "Password update failed.",
            HttpStatusCode.BAD_REQUEST),

    NEW_PASSWORD_REQUIRED(
            "FM-AUTH-018",
            "New password is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_NEW_PASSWORD(
            "FM-AUTH-019",
            "Invalid new password.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Account Authentication State Errors - 020 to 029
    // =========================================================================

    ACCOUNT_DISABLED(
            "FM-AUTH-020",
            "User account is disabled.",
            HttpStatusCode.FORBIDDEN),

    ACCOUNT_LOCKED(
            "FM-AUTH-021",
            "User account is locked.",
            HttpStatusCode.FORBIDDEN),

    ACCOUNT_EXPIRED(
            "FM-AUTH-022",
            "User account has expired.",
            HttpStatusCode.FORBIDDEN),

    CREDENTIALS_EXPIRED(
            "FM-AUTH-023",
            "User credentials have expired.",
            HttpStatusCode.FORBIDDEN),

    ACCOUNT_NOT_ACTIVE(
            "FM-AUTH-024",
            "User account is not active.",
            HttpStatusCode.FORBIDDEN),

    // =========================================================================
    // Session Errors - 030 to 039
    // =========================================================================

    SESSION_REQUIRED(
            "FM-AUTH-030",
            "Authenticated session is required.",
            HttpStatusCode.UNAUTHORIZED),

    INVALID_SESSION(
            "FM-AUTH-031",
            "Invalid authentication session.",
            HttpStatusCode.UNAUTHORIZED),

    SESSION_EXPIRED(
            "FM-AUTH-032",
            "Authentication session has expired.",
            HttpStatusCode.UNAUTHORIZED),

    SESSION_NOT_FOUND(
            "FM-AUTH-033",
            "Authentication session not found.",
            HttpStatusCode.NOT_FOUND),

    SESSION_ALREADY_TERMINATED(
            "FM-AUTH-034",
            "Authentication session has already been terminated.",
            HttpStatusCode.CONFLICT),

    SESSION_TERMINATION_FAILED(
            "FM-AUTH-035",
            "Authentication session termination failed.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Access Token / JWT Errors - 040 to 049
    // =========================================================================

    ACCESS_TOKEN_REQUIRED(
            "FM-AUTH-040",
            "Access token is required.",
            HttpStatusCode.UNAUTHORIZED),

    INVALID_ACCESS_TOKEN(
            "FM-AUTH-041",
            "Invalid access token.",
            HttpStatusCode.UNAUTHORIZED),

    ACCESS_TOKEN_EXPIRED(
            "FM-AUTH-042",
            "Access token has expired.",
            HttpStatusCode.UNAUTHORIZED),

    ACCESS_TOKEN_REVOKED(
            "FM-AUTH-043",
            "Access token has been revoked.",
            HttpStatusCode.UNAUTHORIZED),

    ACCESS_TOKEN_GENERATION_FAILED(
            "FM-AUTH-044",
            "Access token generation failed.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    ACCESS_TOKEN_VALIDATION_FAILED(
            "FM-AUTH-045",
            "Access token validation failed.",
            HttpStatusCode.UNAUTHORIZED),

    // =========================================================================
    // Refresh Token Errors - 050 to 059
    // =========================================================================

    REFRESH_TOKEN_REQUIRED(
            "FM-AUTH-050",
            "Refresh token is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_REFRESH_TOKEN(
            "FM-AUTH-051",
            "Invalid refresh token.",
            HttpStatusCode.UNAUTHORIZED),

    REFRESH_TOKEN_EXPIRED(
            "FM-AUTH-052",
            "Refresh token has expired.",
            HttpStatusCode.UNAUTHORIZED),

    REFRESH_TOKEN_REVOKED(
            "FM-AUTH-053",
            "Refresh token has been revoked.",
            HttpStatusCode.UNAUTHORIZED),

    REFRESH_TOKEN_GENERATION_FAILED(
            "FM-AUTH-054",
            "Refresh token generation failed.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    REFRESH_TOKEN_VALIDATION_FAILED(
            "FM-AUTH-055",
            "Refresh token validation failed.",
            HttpStatusCode.UNAUTHORIZED),

    // =========================================================================
    // Login Errors - 060 to 069
    // =========================================================================

    LOGIN_REQUIRED(
            "FM-AUTH-060",
            "Login information is required.",
            HttpStatusCode.BAD_REQUEST),

    LOGIN_FAILED(
            "FM-AUTH-061",
            "Login failed.",
            HttpStatusCode.UNAUTHORIZED),

    ALREADY_AUTHENTICATED(
            "FM-AUTH-062",
            "User is already authenticated.",
            HttpStatusCode.CONFLICT),

    LOGIN_NOT_ALLOWED(
            "FM-AUTH-063",
            "Login is not allowed.",
            HttpStatusCode.FORBIDDEN),

    // =========================================================================
    // Logout Errors - 070 to 079
    // =========================================================================

    LOGOUT_REQUIRED(
            "FM-AUTH-070",
            "Authenticated session is required for logout.",
            HttpStatusCode.UNAUTHORIZED),

    LOGOUT_FAILED(
            "FM-AUTH-071",
            "Logout failed.",
            HttpStatusCode.BAD_REQUEST),

    ALREADY_LOGGED_OUT(
            "FM-AUTH-072",
            "User is already logged out.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Login History Errors - 080 to 089
    // =========================================================================

    LOGIN_HISTORY_NOT_FOUND(
            "FM-AUTH-080",
            "Login history not found.",
            HttpStatusCode.NOT_FOUND),

    LOGIN_HISTORY_OPERATION_NOT_ALLOWED(
            "FM-AUTH-081",
            "The requested login history operation is not allowed.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Password Recovery and Reset Errors - 090 to 109
    // =========================================================================

    PASSWORD_RECOVERY_IDENTIFIER_REQUIRED(
            "FM-AUTH-090",
            "Password recovery identifier is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_PASSWORD_RECOVERY_REQUEST(
            "FM-AUTH-091",
            "Invalid password recovery request.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RESET_TOKEN_REQUIRED(
            "FM-AUTH-092",
            "Password reset token is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_PASSWORD_RESET_TOKEN(
            "FM-AUTH-093",
            "Invalid password reset token.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RESET_TOKEN_EXPIRED(
            "FM-AUTH-094",
            "Password reset token has expired.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RESET_TOKEN_REVOKED(
            "FM-AUTH-095",
            "Password reset token has been revoked.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RESET_TOKEN_ALREADY_USED(
            "FM-AUTH-096",
            "Password reset token has already been used.",
            HttpStatusCode.CONFLICT),

    PASSWORD_RESET_FAILED(
            "FM-AUTH-097",
            "Password reset failed.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RESET_ATTEMPTS_EXCEEDED(
            "FM-AUTH-098",
            "Password reset attempts exceeded.",
            HttpStatusCode.TOO_MANY_REQUESTS),

    PASSWORD_RECOVERY_FAILED(
            "FM-AUTH-099",
            "Password recovery failed.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RECOVERY_VERIFICATION_REQUIRED(
            "FM-AUTH-100",
            "Password recovery verification is required.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RECOVERY_VERIFICATION_FAILED(
            "FM-AUTH-101",
            "Password recovery verification failed.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RECOVERY_CREDENTIAL_EXPIRED(
            "FM-AUTH-102",
            "Password recovery credential has expired.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_RECOVERY_CREDENTIAL_ALREADY_USED(
            "FM-AUTH-103",
            "Password recovery credential has already been used.",
            HttpStatusCode.CONFLICT),

    NEW_PASSWORD_SAME_AS_OLD_PASSWORD(
            "FM-AUTH-104",
            "New password must be different from the old password.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_TOO_SHORT(
            "FM-AUTH-105",
            "Password must contain at least 8 characters.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_TOO_LONG(
            "FM-AUTH-106",
            "Password must not contain more than 100 characters.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_UPPERCASE_REQUIRED(
            "FM-AUTH-107",
            "Password must contain at least one uppercase letter.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_LOWERCASE_REQUIRED(
            "FM-AUTH-108",
            "Password must contain at least one lowercase letter.",
            HttpStatusCode.BAD_REQUEST),

    PASSWORD_DIGIT_REQUIRED(
            "FM-AUTH-109",
            "Password must contain at least one digit.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * ============================================================================
     * Email Verification / OTP Errors
     * ============================================================================
     */

    /**
     * Indicates that account verification must be completed before the requested
     * authentication operation can proceed.
     */
    ACCOUNT_VERIFICATION_REQUIRED(
            "FM-AUTH-110",
            "Account verification is required.",
            HttpStatusCode.UNAUTHORIZED),

    /**
     * Indicates that an email verification OTP was not supplied.
     */
    EMAIL_OTP_REQUIRED(
            "FM-AUTH-111",
            "Email verification OTP is required.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Indicates that the supplied email verification OTP is invalid.
     */
    INVALID_EMAIL_OTP(
            "FM-AUTH-112",
            "Invalid email verification OTP.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Indicates that the email verification OTP has expired.
     */
    EMAIL_OTP_EXPIRED(
            "FM-AUTH-113",
            "Email verification OTP has expired.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Indicates that the email verification OTP has already been successfully
     * consumed.
     */
    EMAIL_OTP_ALREADY_USED(
            "FM-AUTH-114",
            "Email verification OTP has already been used.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Indicates that the maximum number of verification attempts has been
     * exceeded for the current email verification OTP.
     */
    EMAIL_OTP_ATTEMPTS_EXCEEDED(
            "FM-AUTH-115",
            "Maximum email verification OTP attempts exceeded.",
            HttpStatusCode.TOO_MANY_REQUESTS),

    /**
     * Indicates that the email verification operation could not be completed.
     */
    EMAIL_VERIFICATION_FAILED(
            "FM-AUTH-116",
            "Email verification failed.",
            HttpStatusCode.BAD_REQUEST),

    /**
     * Indicates that another email verification OTP cannot be requested yet
     * because the resend cooldown period has not elapsed.
     */
    EMAIL_OTP_RESEND_COOLDOWN(
            "FM-AUTH-117",
            "Please wait before requesting another email verification OTP.",
            HttpStatusCode.TOO_MANY_REQUESTS),

    /**
     * Indicates that email verification is temporarily locked because the
     * maximum number of OTP verification attempts has been exceeded.
     */
    EMAIL_OTP_LOCKED(
            "FM-AUTH-118",
            "Email verification is temporarily locked. Please try again later.",
            HttpStatusCode.TOO_MANY_REQUESTS),

    PASSWORD_SPECIAL_CHARACTER_REQUIRED(
            "FM-AUTH-119",
            "Password must contain at least one special character.",
            HttpStatusCode.BAD_REQUEST);

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * User-friendly error message.
     */
    private final String errorMessage;

    /**
     * HTTP status code associated with the business error.
     */
    private final HttpStatusCode httpStatusCode;

    /**
     * Creates an Authentication business error.
     *
     * @param errorCode      unique authentication error code
     * @param errorMessage   user-friendly error message
     * @param httpStatusCode HTTP status code
     */
    AuthenticationErrorConstants(
            final String errorCode,
            final String errorMessage,
            final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorCode() {

        return errorCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage() {

        return errorMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpStatusCode getHttpStatusCode() {

        return httpStatusCode;
    }
}