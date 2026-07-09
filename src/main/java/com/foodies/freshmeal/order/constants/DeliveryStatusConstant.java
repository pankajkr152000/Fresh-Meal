
package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : DeliveryStatus
 * ============================================================================
 *
 * Represents the delivery progress of an order.
 *
 * <p>
 * This status is managed by the restaurant and delivery partner
 * throughout the fulfillment process.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public enum DeliveryStatusConstant implements IDisplayOption {

    /**
     * Delivery request has been created.
     */
    PENDING("Pending"),

    /**
     * Restaurant confirmed the order.
     */
    CONFIRMED("Confirmed"),

    /**
     * Food is currently being prepared.
     */
    PREPARING("Preparing"),

    /**
     * Food is packed and ready for pickup.
     */
    READY("Ready"),

    /**
     * Delivery partner has picked up the order.
     */
    OUT_FOR_DELIVERY("Out for Delivery"),

    /**
     * Delivery partner is travelling to the customer.
     */
    ON_THE_WAY("On the Way"),

    /**
     * Order delivered successfully.
     */
    DELIVERED("Delivered"),

    /**
     * Delivery has been cancelled.
     */
    CANCELLED("Cancelled");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a delivery status.
     *
     * @param displayName user-friendly display label
     */
    DeliveryStatusConstant(String displayName) {
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