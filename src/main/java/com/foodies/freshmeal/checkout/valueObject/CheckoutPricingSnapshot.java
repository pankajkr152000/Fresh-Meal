
package com.foodies.freshmeal.checkout.valueObject;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a snapshot of the pricing breakdown calculated for a checkout.
 *
 * <p>
 * This value object preserves the monetary amounts presented to the
 * customer during checkout review. It is generated and maintained by
 * the backend and must not be directly populated with client-supplied
 * pricing values.
 * </p>
 *
 * <p>
 * All monetary values must use a consistent currency. The Checkout
 * service is responsible for calculating the amounts, applying the
 * appropriate precision and rounding rules, and validating the final
 * payable amount.
 * </p>
 *
 * <p>
 * The snapshot provides an auditable breakdown of the checkout
 * amount and remains independent of subsequent changes to the original
 * cart or pricing configuration.
 * </p>
 *
 * @author Pankaj Kumar
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutPricingSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Total value of all checkout items before discounts, taxes,
     * and additional fees.
     */
    private Money itemSubtotal;

    /**
     * Total discount applied to eligible checkout items.
     */
    private Money discountAmount;

    /**
     * Total tax calculated according to applicable tax rules.
     */
    private Money taxAmount;

    /**
     * Standard delivery fee applicable to the checkout.
     */
    private Money deliveryFee;

    /**
     * Packaging charge applied to the checkout.
     */
    private Money packagingCharge;

    /**
     * Platform or service fee applicable to the checkout.
     */
    private Money platformFee;

    /**
     * Additional charge applicable during rainy weather conditions.
     *
     * <p>
     * This charge may be zero when weather-based pricing is not
     * applicable.
     * </p>
     */
    private Money rainCharge;

    /**
     * Tip amount voluntarily added by the customer.
     *
     * <p>
     * The tip is maintained separately from the item subtotal and other
     * charges for transparent pricing and order reconciliation.
     * </p>
     */
    private Money tipAmount;

    /**
     * Final amount payable by the customer.
     *
     * <p>
     * This amount is calculated by the backend after applying
     * all applicable discounts, taxes, and fees.
     * </p>
     */
    private Money totalPayable;
}