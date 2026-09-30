
package com.foodies.freshmeal.checkout.valueObject;

import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Class : CheckoutPricingSnapshot
 * ============================================================================
 *
 * Represents a snapshot of the pricing breakdown calculated for a checkout.
 *
 * <p>
 * The snapshot preserves the monetary values presented to the customer
 * during checkout review. All amounts must be calculated and validated
 * by the backend.
 * </p>
 *
 * <p>
 * This class is an embedded value object within CheckoutEntity and does
 * not maintain independent persistence or business lifecycle behavior.
 * </p>
 *
 * ============================================================================
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutPricingSnapshot {

    /**
     * Total value of all checkout items before discounts and additional
     * charges.
     */
    private Money itemSubtotal;

    /**
     * Total discount applied to the checkout.
     *
     * <p>
     * Defaults to zero when no discount is applicable.
     * </p>
     */
    private Money discountAmount;

    /**
     * Total tax amount applicable to the checkout.
     *
     * <p>
     * Defaults to zero when no tax is applicable.
     * </p>
     */
    private Money taxAmount;

    /**
     * Delivery fee applicable to the checkout.
     *
     * <p>
     * Defaults to zero when delivery is free or no delivery fee applies.
     * </p>
     */
    private Money deliveryFee;

    /**
     * Platform fee applicable to the checkout.
     *
     * <p>
     * Defaults to zero when no platform fee applies.
     * </p>
     */
    private Money platformFee;

    /**
     * Final amount payable by the customer.
     *
     * <p>
     * This amount must be calculated by the backend using the applicable
     * pricing rules and must never be accepted directly from the client.
     * </p>
     */
    private Money totalPayable;
}