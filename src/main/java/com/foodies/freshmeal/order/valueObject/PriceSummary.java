package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * PriceSummary
 * ============================================================================
 *
 * Represents the complete historical pricing breakdown of an order.
 *
 * All monetary values are calculated by the backend and persisted with the
 * order so that historical orders do not change when pricing rules change.
 *
 * ============================================================================
 *
 * Grand Total Formula
 *
 * Item Total
 * - Item Discount
 * - Coupon Discount
 * + Tax
 * + Delivery Charge
 * + Packing Charge
 * + Platform Fee
 * + Tip
 * + Round Off
 * ---------------------------
 * = Grand Total
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceSummary implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Total price of all items before discounts and taxes.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal itemTotal = BigDecimal.ZERO;

    /**
     * Total discount applied directly to food items.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal itemDiscount = BigDecimal.ZERO;

    /**
     * Order-level coupon discount.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal couponDiscount = BigDecimal.ZERO;

    /**
     * Coupon code applied to the order.
     */
    private String couponCode;

    /**
     * Coupon display name.
     */
    private String couponName;

    /**
     * Total tax amount.
     *
     * Normally:
     *
     * taxAmount = CGST + SGST
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    /**
     * CGST amount.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal cgst = BigDecimal.ZERO;

    /**
     * SGST amount.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal sgst = BigDecimal.ZERO;

    /**
     * Delivery charge.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal deliveryCharge = BigDecimal.ZERO;

    /**
     * Food packaging charge.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal packingCharge = BigDecimal.ZERO;

    /**
     * Platform / convenience fee.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal platformFee = BigDecimal.ZERO;

    /**
     * Tip provided for delivery.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal tipAmount = BigDecimal.ZERO;

    /**
     * Rounding adjustment.
     */
    @Builder.Default
    @Digits(integer = 10, fraction = 2)
    private BigDecimal roundOffAmount = BigDecimal.ZERO;

    /**
     * Final payable amount.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    /**
     * Currency.
     */
    @Builder.Default
    private String currency = "INR";

}