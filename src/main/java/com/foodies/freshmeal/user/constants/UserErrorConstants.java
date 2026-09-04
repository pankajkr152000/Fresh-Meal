package com.foodies.freshmeal.user.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * User Error Constants
 * ============================================================================
 *
 * Centralized business errors for the User module.
 *
 * <p>
 * This enum defines business-level errors related to:
 * </p>
 *
 * <ul>
 * <li>User existence and lifecycle</li>
 * <li>Username management</li>
 * <li>Email address management</li>
 * <li>Phone number management</li>
 * <li>User profile information</li>
 * <li>User account state</li>
 * <li>User roles</li>
 * <li>User address associations</li>
 * </ul>
 *
 * <p>
 * Authentication-specific errors such as invalid credentials, password
 * validation, OTP validation, token expiration, and JWT errors are
 * intentionally
 * excluded and will be maintained by the Authentication module.
 * Authorization-specific errors such as access denied and insufficient
 * permissions will be maintained by the Authorization module.
 * </p>
 *
 * ============================================================================
 *
 * Error Code Convention
 * ---------------------
 *
 * <p>
 * User module errors follow the format:
 * </p>
 *
 * <pre>
 * FM - USER - XXXXXXXX
 * </pre>
 *
 * <ul>
 * <li><b>FM</b> - FreshMeal</li>
 * <li><b>USER</b> - User module</li>
 * <li><b>XXXXXXX</b> - Unique error number</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum UserErrorConstants implements IBusinessError {

    // =========================================================================
    // General User Errors - 001 to 009
    // =========================================================================

    USER_NOT_FOUND(
            "FM-USER-001",
            "User not found.",
            HttpStatusCode.NOT_FOUND),

    USER_ALREADY_EXISTS(
            "FM-USER-002",
            "User already exists.",
            HttpStatusCode.CONFLICT),

    USER_INVALID(
            "FM-USER-003",
            "Invalid user information.",
            HttpStatusCode.BAD_REQUEST),

    USER_OPERATION_NOT_ALLOWED(
            "FM-USER-004",
            "The requested operation is not allowed for this user.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Username Errors - 010 to 019
    // =========================================================================

    USERNAME_REQUIRED(
            "FM-USER-010",
            "Username is required.",
            HttpStatusCode.BAD_REQUEST),

    USERNAME_ALREADY_EXISTS(
            "FM-USER-011",
            "Username is already in use.",
            HttpStatusCode.CONFLICT),

    INVALID_USERNAME(
            "FM-USER-012",
            "Invalid username.",
            HttpStatusCode.BAD_REQUEST),

    USERNAME_NOT_FOUND(
            "FM-USER-013",
            "Username not found.",
            HttpStatusCode.NOT_FOUND),

    // =========================================================================
    // Email Errors - 020 to 029
    // =========================================================================

    EMAIL_REQUIRED(
            "FM-USER-020",
            "Email address is required.",
            HttpStatusCode.BAD_REQUEST),

    EMAIL_ALREADY_EXISTS(
            "FM-USER-021",
            "Email address is already in use.",
            HttpStatusCode.CONFLICT),

    INVALID_EMAIL(
            "FM-USER-022",
            "Invalid email address.",
            HttpStatusCode.BAD_REQUEST),

    EMAIL_NOT_FOUND(
            "FM-USER-023",
            "Email address not found.",
            HttpStatusCode.NOT_FOUND),

    // =========================================================================
    // Phone Number Errors - 030 to 039
    // =========================================================================

    PHONE_NUMBER_REQUIRED(
            "FM-USER-030",
            "Phone number is required.",
            HttpStatusCode.BAD_REQUEST),

    PHONE_NUMBER_ALREADY_EXISTS(
            "FM-USER-031",
            "Phone number is already in use.",
            HttpStatusCode.CONFLICT),

    INVALID_PHONE_NUMBER(
            "FM-USER-032",
            "Invalid phone number.",
            HttpStatusCode.BAD_REQUEST),

    PHONE_NUMBER_NOT_FOUND(
            "FM-USER-033",
            "Phone number not found.",
            HttpStatusCode.NOT_FOUND),

    // =========================================================================
    // User Profile Errors - 040 to 049
    // =========================================================================

    FIRST_NAME_REQUIRED(
            "FM-USER-040",
            "First name is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_FIRST_NAME(
            "FM-USER-041",
            "Invalid first name.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_LAST_NAME(
            "FM-USER-042",
            "Invalid last name.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_USER_PROFILE(
            "FM-USER-043",
            "Invalid user profile information.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Account State Errors - 050 to 069
    // =========================================================================

    ACCOUNT_DISABLED(
            "FM-USER-050",
            "User account is disabled.",
            HttpStatusCode.FORBIDDEN),

    ACCOUNT_LOCKED(
            "FM-USER-051",
            "User account is locked.",
            HttpStatusCode.FORBIDDEN),

    ACCOUNT_ALREADY_ENABLED(
            "FM-USER-052",
            "User account is already enabled.",
            HttpStatusCode.CONFLICT),

    ACCOUNT_ALREADY_DISABLED(
            "FM-USER-053",
            "User account is already disabled.",
            HttpStatusCode.CONFLICT),

    ACCOUNT_ALREADY_LOCKED(
            "FM-USER-054",
            "User account is already locked.",
            HttpStatusCode.CONFLICT),

    ACCOUNT_NOT_LOCKED(
            "FM-USER-055",
            "User account is not locked.",
            HttpStatusCode.CONFLICT),

    ACCOUNT_NOT_ACTIVE(
            "FM-USER-056",
            "User account is not active.",
            HttpStatusCode.FORBIDDEN),

    // =========================================================================
    // Role Errors - 070 to 079
    // =========================================================================

    ROLE_REQUIRED(
            "FM-USER-070",
            "At least one user role is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_USER_ROLE(
            "FM-USER-071",
            "Invalid user role.",
            HttpStatusCode.BAD_REQUEST),

    USER_ROLE_ALREADY_ASSIGNED(
            "FM-USER-072",
            "The requested role is already assigned to the user.",
            HttpStatusCode.CONFLICT),

    USER_ROLE_NOT_ASSIGNED(
            "FM-USER-073",
            "The requested role is not assigned to the user.",
            HttpStatusCode.NOT_FOUND),

    // =========================================================================
    // Address Association Errors - 080 to 089
    // =========================================================================

    ADDRESS_NOT_ASSOCIATED(
            "FM-USER-080",
            "Address is not associated with the user.",
            HttpStatusCode.NOT_FOUND),

    ADDRESS_ALREADY_ASSOCIATED(
            "FM-USER-081",
            "Address is already associated with the user.",
            HttpStatusCode.CONFLICT),

    INVALID_ADDRESS_ASSOCIATION(
            "FM-USER-082",
            "Invalid user address association.",
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
     * Creates a User business error.
     *
     * @param errorCode      unique error code
     * @param errorMessage   user-friendly error message
     * @param httpStatusCode HTTP status code
     */
    UserErrorConstants(
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