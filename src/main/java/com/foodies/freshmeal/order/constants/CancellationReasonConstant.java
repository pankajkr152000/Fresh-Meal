package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * CancellationReasonConstant
 * ============================================================================
 *
 * Represents the valid reasons for order cancellation.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum CancellationReasonConstant implements IDisplayOption {

    // =====================================================
    // CUSTOMER
    // =====================================================

    CUSTOMER_CHANGED_MIND("Customer Changed Mind"),
    ORDERED_BY_MISTAKE("Ordered By Mistake"),
    DUPLICATE_ORDER("Duplicate Order"),
    FOUND_BETTER_PRICE("Found Better Price"),
    WRONG_DELIVERY_ADDRESS("Wrong Delivery Address"),
    PAYMENT_ISSUE("Payment Issue"),

    // =====================================================
    // RESTAURANT
    // =====================================================

    RESTAURANT_CLOSED("Restaurant Closed"),
    ITEM_OUT_OF_STOCK("Item Out Of Stock"),
    RESTAURANT_UNAVAILABLE("Restaurant Unavailable"),
    RESTAURANT_REJECTED_ORDER("Restaurant Rejected Order"),
    HIGH_ORDER_VOLUME("High Order Volume"),

    // =====================================================
    // DELIVERY
    // =====================================================

    DELIVERY_PARTNER_UNAVAILABLE("Delivery Partner Unavailable"),
    DELIVERY_DELAY("Delivery Delay"),
    DELIVERY_ADDRESS_NOT_SERVICEABLE("Delivery Address Not Serviceable"),

    // =====================================================
    // SYSTEM
    // =====================================================

    TECHNICAL_ISSUE("Technical Issue"),
    FRAUD_DETECTED("Fraud Detected"),
    ORDER_EXPIRED("Order Expired"),
    PAYMENT_TIMEOUT("Payment Timeout"),

    // =====================================================
    // ADMIN
    // =====================================================

    ADMIN_CANCELLED("Cancelled By Admin"),
    OTHER("Other");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a cancellation reason.
     *
     * @param displayName user-friendly display label
     */
    CancellationReasonConstant(String displayName) {
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