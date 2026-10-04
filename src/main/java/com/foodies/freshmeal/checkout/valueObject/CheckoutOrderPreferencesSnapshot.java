
package com.foodies.freshmeal.checkout.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.order.constants.OrderTypeConstant;
import com.foodies.freshmeal.order.constants.PaymentModeConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a snapshot of the customer's order-placement preferences
 * captured during checkout review.
 *
 * <p>
 * This snapshot preserves the customer's intent independently of the
 * calculated pricing outcome and the authoritative Order aggregate.
 * </p>
 *
 * <p>
 * The Checkout service is responsible for validating these preferences
 * before allowing checkout confirmation.
 * </p>
 *
 * <p>
 * Any change to these preferences after review requires checkout
 * revalidation. The snapshot must not be silently modified during
 * order creation.
 * </p>
 *
 * <p>
 * Monetary values are represented using the application's Money
 * value object. Final pricing, discounts, taxes, and payable amounts
 * remain the responsibility of the backend Order workflow.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutOrderPreferencesSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Order Type
    // =========================================================================

    /**
     * Order fulfillment type selected by the customer.
     */
    private OrderTypeConstant orderType;

    // =========================================================================
    // Payment
    // =========================================================================

    /**
     * Payment method selected by the customer.
     *
     * <p>
     * This represents the requested payment method only.
     * Payment status is controlled by the Order and payment workflows.
     * </p>
     */
    private PaymentModeConstant paymentMode;

    // =========================================================================
    // Coupon
    // =========================================================================

    /**
     * Coupon code submitted by the customer.
     *
     * <p>
     * The code must be validated by the backend before any discount
     * is applied. This snapshot does not represent an approved discount.
     * </p>
     */
    private String couponCode;

    // =========================================================================
    // Tip
    // =========================================================================

    /**
     * Optional tip amount requested by the customer.
     *
     * <p>
     * The amount must be validated and incorporated into the
     * backend-calculated pricing.
     * </p>
     */
    private Money tipAmount;

    // =========================================================================
    // Scheduled Order
    // =========================================================================

    /**
     * Indicates whether the customer requested a scheduled order.
     */
    @Builder.Default
    private Boolean scheduledOrder = Boolean.FALSE;

    /**
     * Requested scheduled delivery time.
     *
     * <p>
     * Required when scheduledOrder is true. The Checkout service
     * must validate the scheduling rules before confirmation.
     * </p>
     */
    private LocalDateTime scheduledDeliveryAt;

    // =========================================================================
    // Gift Order
    // =========================================================================

    /**
     * Indicates whether the customer requested the order to be
     * treated as a gift.
     */
    @Builder.Default
    private Boolean giftOrder = Boolean.FALSE;

    // =========================================================================
    // Customer Note
    // =========================================================================

    /**
     * Customer-facing note associated with the order.
     *
     * <p>
     * Restaurant-internal notes and operational notes are not
     * accepted through this field.
     * </p>
     */
    private String customerNote;

    // =========================================================================
    // Item Instructions
    // =========================================================================

    /**
     * Special instructions associated with individual checkout items.
     *
     * <p>
     * Each instruction must reference a food identifier present in
     * the reviewed checkout item snapshots.
     * </p>
     */
    private List<CheckoutItemInstructionSnapshot> itemInstructions;

    public List<CheckoutItemInstructionSnapshot> getItemInstructions() {
        if (itemInstructions == null) {
            itemInstructions = new ArrayList<>();
        }
        return itemInstructions;
    }

}
