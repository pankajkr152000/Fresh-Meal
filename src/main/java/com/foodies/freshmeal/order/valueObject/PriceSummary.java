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
 * Represents the complete pricing breakdown of an order.
 *
 * <p>
 * This Value Object stores every monetary component involved in calculating the
 * final payable amount. Storing these values ensures historical accuracy and
 * avoids recalculating prices after an order has been placed.
 * </p>
 *
 * Example:
 *
 * Item Total : ₹500
 * Item Discount : ₹50
 * Coupon Discount : ₹30
 * Tax : ₹37.80
 * Delivery Charge : ₹40
 * Packing Charge : ₹20
 * Platform Fee : ₹5
 * Tip : ₹30
 * Round Off : ₹0.20
 * -----------------------------------
 * Grand Total : ₹552.00
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
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
     * Discount applied directly on food items.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal itemDiscount = BigDecimal.ZERO;

    /**
     * Coupon discount.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal couponDiscount = BigDecimal.ZERO;

    private String couponCode;

    private String couponName;
    /**
     * Total tax amount.
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
     * Platform / Convenience fee.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal platformFee = BigDecimal.ZERO;

    /**
     * Tip paid to the delivery partner.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal tipAmount = BigDecimal.ZERO;

    /**
     * Round-off adjustment.
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
     *
     * Default: INR
     */
    @Builder.Default
    private String currency = "INR";
}