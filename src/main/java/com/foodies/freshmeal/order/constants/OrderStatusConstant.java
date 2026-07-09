package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : OrderStatus
 * ============================================================================
 *
 * Represents the overall lifecycle status of a customer order.
 *
 * <p>
 * This status reflects the current stage of an order from placement
 * until completion or cancellation.
 * </p>
 *
 * <p>
 * Each enum constant exposes:
 * <ul>
 * <li>A user-friendly label for UI rendering.</li>
 * <li>An internal value for API communication and business logic.</li>
 * </ul>
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public enum OrderStatusConstant implements IDisplayOption {

    /**
     * Order has been created.
     */
    PENDING("Pending"),

    /**
     * Restaurant has accepted the order.
     */
    CONFIRMED("Confirmed"),

    /**
     * Order has been completed successfully.
     */
    COMPLETED("Completed"),

    /**
     * Order has been cancelled.
     */
    CANCELLED("Cancelled");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates an order status.
     *
     * @param displayName user-friendly display label
     */
    OrderStatusConstant(String displayName) {
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