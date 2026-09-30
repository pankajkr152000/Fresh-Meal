
package com.foodies.freshmeal.checkout.valueObject;

import java.time.LocalDateTime;

import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Class : CheckoutStatusHistory
 * ============================================================================
 *
 * Represents a single lifecycle transition of a checkout session.
 *
 * <p>
 * Each instance captures the status assigned to the checkout, the timestamp
 * of the transition, and optional contextual information explaining why
 * the transition occurred.
 * </p>
 *
 * <p>
 * Status history is embedded within the Checkout document to preserve
 * the chronological lifecycle of a checkout session.
 * </p>
 *
 * ============================================================================
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutStatusHistory {

    /**
     * Status assigned to the checkout after the transition.
     */
    private CheckoutStatusConstant status;

    /**
     * Date and time when the status transition occurred.
     */
    private LocalDateTime changedAt;

    /**
     * Machine-readable or business-level reason for the transition.
     *
     * <p>
     * Examples include CUSTOMER_CONFIRMED, VALIDATION_FAILED,
     * CUSTOMER_CANCELLED, and SESSION_EXPIRED.
     * </p>
     */
    private String reason;

    /**
     * Additional contextual information about the transition.
     *
     * <p>
     * This field is optional and should not contain sensitive information
     * such as passwords, authentication tokens, or payment credentials.
     * </p>
     */
    private String remarks;
}