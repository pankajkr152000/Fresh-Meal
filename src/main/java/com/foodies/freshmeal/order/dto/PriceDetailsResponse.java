package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * PriceDetailsResponse
 * ============================================================================
 *
 * Represents the complete pricing breakdown displayed on the Admin Order
 * Details page.
 *
 * This DTO is an API representation of PriceSummary.
 *
 * All monetary values are represented using the common Money value object
 * instead of exposing raw BigDecimal values.
 *
 * This keeps amount and currency together as one business concept.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Item Pricing
    // =========================================================================

    /**
     * Total price of all ordered items before discounts and taxes.
     */
    private Money itemTotal;

    /**
     * Discount applied directly to food items.
     */
    private Money itemDiscount;

    /**
     * Coupon discount applied to the order.
     */
    private Money couponDiscount;

    /**
     * Coupon code applied to the order.
     */
    private String couponCode;

    /**
     * Coupon display name.
     */
    private String couponName;

    // =========================================================================
    // Tax
    // =========================================================================

    /**
     * Total tax amount.
     */
    private Money taxAmount;

    /**
     * CGST amount.
     */
    private Money cgst;

    /**
     * SGST amount.
     */
    private Money sgst;

    // =========================================================================
    // Additional Charges
    // =========================================================================

    /**
     * Delivery charge.
     */
    private Money deliveryCharge;

    /**
     * Food packaging charge.
     */
    private Money packingCharge;

    /**
     * Platform / convenience fee.
     */
    private Money platformFee;

    /**
     * Tip amount.
     */
    private Money tipAmount;

    /**
     * Round-off adjustment.
     */
    private Money roundOffAmount;

    // =========================================================================
    // Final Amount
    // =========================================================================

    /**
     * Final amount payable by the customer.
     */
    private Money grandTotal;

}