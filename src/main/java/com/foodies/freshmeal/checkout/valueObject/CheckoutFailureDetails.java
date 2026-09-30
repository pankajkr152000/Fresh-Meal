
package com.foodies.freshmeal.checkout.valueObject;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Class : CheckoutFailureDetails
 * ============================================================================
 *
 * Represents structured failure information associated with a checkout
 * session.
 *
 * <p>
 * This value object preserves the failure context to support troubleshooting,
 * customer communication, and checkout lifecycle management.
 * </p>
 *
 * <p>
 * Failure details are embedded within CheckoutEntity and do not maintain
 * independent persistence or business lifecycle behavior.
 * </p>
 *
 * ============================================================================
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutFailureDetails {

    /**
     * Machine-readable category identifying the type of failure.
     *
     * <p>
     * Examples include VALIDATION_FAILURE, BUSINESS_RULE_FAILURE,
     * and PROCESSING_FAILURE.
     * </p>
     */
    private String failureType;

    /**
     * Application error code associated with the failure, when applicable.
     *
     * <p>
     * Error codes should originate from the appropriate module-specific
     * error constants rather than being hard-coded.
     * </p>
     */
    private String errorCode;

    /**
     * Human-readable explanation of the failure.
     *
     * <p>
     * This message should be safe to expose to authorized consumers
     * and must not contain internal stack traces or sensitive data.
     * </p>
     */
    private String message;

    /**
     * Date and time when the failure occurred.
     */
    private LocalDateTime occurredAt;

    /**
     * Indicates whether the failure may be eligible for retry.
     *
     * <p>
     * This is an informational attribute. Actual retry eligibility
     * must be determined by checkout business rules and the current
     * lifecycle state.
     * </p>
     */
    private boolean retryable;
}