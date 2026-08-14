package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : OrderTypeConstant
 * ============================================================================
 *
 * Represents the fulfillment type selected for an order.
 *
 * Order type describes HOW the customer receives or consumes the order.
 *
 * It is intentionally separate from OrderStatusConstant because order type
 * does not represent an order lifecycle.
 *
 * ============================================================================
 *
 * Order Types
 * -----------
 *
 * DELIVERY
 * Order is delivered to the customer's delivery address.
 *
 * TAKEAWAY
 * Customer collects the order from the restaurant.
 *
 * DINE_IN
 * Customer consumes the order at the restaurant.
 *
 * ============================================================================
 */
public enum OrderTypeConstant implements IDisplayOption {

    /**
     * Order is delivered to the customer.
     */
    DELIVERY("Delivery"),

    /**
     * Customer collects the order from the restaurant.
     */
    TAKEAWAY("Takeaway"),

    /**
     * Customer consumes the order at the restaurant.
     */
    DINE_IN("Dine In");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates an order type.
     *
     * @param displayName user-friendly display label
     */
    OrderTypeConstant(String displayName) {
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