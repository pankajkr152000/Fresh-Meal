
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
 * This value object preserves the failure context required for troubleshooting,
 * customer communication, and controlled recovery.
 * </p>
 *
 * <p>
 * Failure details are embedded within CheckoutEntity and do not maintain
 * independent persistence or business lifecycle behavior.
 * </p>
 *
 * <p>
 * A recorded failure does not, by itself, authorize a retry. Recovery
 * eligibility must be determined by the checkout service after evaluating
 * the current lifecycle state and any side effects of the failed operation.
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
     * Classification of the failure.
     */
    private FailureCategory failureCategory;

    /**
     * Application error code associated with the failure, when applicable.
     *
     * <p>
     * Error codes must originate from the appropriate module-specific
     * error constants rather than being hard-coded.
     * </p>
     */
    private String errorCode;

    /**
     * Human-readable explanation of the failure.
     *
     * <p>
     * This message must be safe to expose to authorized consumers and
     * must not contain internal stack traces or sensitive information.
     * </p>
     */
    private String message;

    /**
     * Date and time when the failure occurred.
     */
    private LocalDateTime occurredAt;

    /**
     * Indicates whether the failure requires reconciliation before
     * any further processing can safely occur.
     *
     * <p>
     * This is particularly relevant when an operation may have produced
     * side effects but its final outcome is uncertain.<br>
     * reconciliationRequired is not a retry flag. It tells the system that the
     * current outcome must be investigated before taking another action.
     * 
     * For example, if order creation times out, the system should check whether the
     * order was persisted before attempting creation again. It must not interpret
     * the timeout as proof that no order exists.
     * </p>
     */
    private boolean reconciliationRequired;

    /**
     * Additional contextual information about the failure.
     *
     * <p>
     * This field is optional and must not contain sensitive information
     * such as passwords, authentication tokens, or payment credentials.
     * </p>
     */
    private String remarks;

    /**
     * =========================================================================
     * Enum : FailureCategory
     * =========================================================================
     *
     * Classifies checkout failures by their business or technical nature.
     *
     * <p>
     * This classification supports consistent error handling and recovery
     * decisions without relying on message parsing.
     * </p>
     *
     * =========================================================================
     */
    public enum FailureCategory {

        /**
         * Failure caused by business validation rules.
         *
         * <p>
         * Examples include unavailable food, changed pricing, or invalid
         * delivery information.
         * </p>
         */
        BUSINESS_VALIDATION,

        /**
         * Failure caused by a temporary technical condition.
         *
         * <p>
         * Examples include temporary service unavailability or network
         * interruption.
         * </p>
         */
        TRANSIENT_TECHNICAL,

        /**
         * Failure caused by a definitive business or processing rejection.
         *
         * <p>
         * The operation cannot proceed under the current conditions.
         * </p>
         */
        DEFINITIVE_PROCESSING,

        /**
         * Failure caused by a concurrent modification or optimistic-lock
         * conflict.
         *
         * <p>
         * The latest persisted state must be reloaded before deciding
         * whether the operation can be safely resumed.
         * </p>
         */
        CONCURRENCY_CONFLICT,

        /**
         * Failure whose cause or outcome could not be reliably determined.
         *
         * <p>
         * Such failures require investigation or reconciliation before
         * another side-effecting operation is attempted.
         * </p>
         */
        UNKNOWN
    }
}