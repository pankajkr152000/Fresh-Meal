package com.foodies.freshmeal.authentication.constants;

/**
 * ============================================================================
 * Registration Status
 * ============================================================================
 *
 * Represents the lifecycle states of a pending FreshMeal registration.
 *
 * <p>
 * A pending registration exists independently of a permanent UserEntity.
 * Its lifecycle begins when registration details are submitted and ends
 * when registration is completed, expired, or cancelled.
 * </p>
 *
 * <h3>Lifecycle</h3>
 *
 * <pre>
 * PENDING_VERIFICATION
 *        |
 *        +------> COMPLETED
 *        |
 *        +------> EXPIRED
 *        |
 *        +------> CANCELLED
 * </pre>
 *
 * Terminal states cannot transition back to PENDING_VERIFICATION.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum RegistrationStatus {

    /**
     * Registration details have been submitted but email verification
     * has not yet been completed.
     */
    PENDING_VERIFICATION,

    /**
     * Email verification succeeded and the permanent user account
     * was successfully created.
     */
    COMPLETED,

    /**
     * Registration validity period elapsed before completion.
     */
    EXPIRED,

    /**
     * Registration was explicitly cancelled.
     */
    CANCELLED
}