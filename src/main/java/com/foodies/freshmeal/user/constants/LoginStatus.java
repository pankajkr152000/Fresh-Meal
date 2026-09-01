package com.foodies.freshmeal.user.constants;

/**
 * ============================================================================
 * Enum : LoginStatus
 * ============================================================================
 *
 * Defines the outcome of a user authentication attempt in FreshMeal.
 *
 * <p>
 * This enum provides a controlled set of values for login-history records
 * instead of storing free-form status strings.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum LoginStatus {

    /**
     * Authentication completed successfully.
     */
    SUCCESS,

    /**
     * Authentication failed because the supplied credentials were invalid.
     */
    FAILED,

    /**
     * Authentication was rejected because the user account is locked.
     */
    LOCKED,

    /**
     * Authentication was rejected because the user account is disabled.
     */
    DISABLED,

    /**
     * Authentication was rejected because the user account has expired.
     */
    ACCOUNT_EXPIRED,

    /**
     * Authentication was rejected because the user's credentials have expired.
     */
    CREDENTIALS_EXPIRED
}