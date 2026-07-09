
package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : PaymentStatus
 * ============================================================================
 *
 * Represents the payment lifecycle of an order.
 *
 * <p>
 * Used to indicate whether payment is pending, completed,
 * failed or refunded.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public enum PaymentStatusConstant implements IDisplayOption {

    /**
     * Payment has not yet been completed.
     */
    PENDING("Pending"),

    /**
     * Payment completed successfully.
     */
    PAID("Paid"),

    /**
     * Payment attempt failed.
     */
    FAILED("Failed"),

    /**
     * Payment has been refunded.
     */
    REFUNDED("Refunded"),

    /**
     * Payment has been cancelled.
     */
    CANCELLED("Cancelled");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a payment status.
     *
     * @param displayName user-friendly display label
     */
    PaymentStatusConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return name();
    }

}
