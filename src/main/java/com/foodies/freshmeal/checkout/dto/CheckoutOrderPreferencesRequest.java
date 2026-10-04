
package com.foodies.freshmeal.checkout.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.order.constants.OrderTypeConstant;
import com.foodies.freshmeal.order.constants.PaymentModeConstant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * Checkout Order Preferences Request
 * ============================================================================
 *
 * Captures customer-selected preferences required to prepare a checkout
 * session for review.
 *
 * <p>
 * This DTO contains customer intent only. The server remains responsible
 * for validating these preferences, determining eligibility, calculating
 * pricing, and creating authoritative order data.
 * </p>
 *
 * <p>
 * This request must never be treated as a trusted pricing or order snapshot.
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
public class CheckoutOrderPreferencesRequest {

    /**
     * Requested order fulfillment type.
     */
    @NotNull
    private OrderTypeConstant orderType;

    /**
     * Customer-selected payment mode.
     */
    @NotNull
    private PaymentModeConstant paymentMode;

    /**
     * Optional coupon code entered by the customer.
     */
    @Size(max = 50)
    private String couponCode;

    /**
     * Optional customer tip.
     *
     * <p>
     * The service layer must validate the amount and currency before
     * accepting this value.
     * </p>
     */
    @Valid
    private Money tipAmount;

    /**
     * Indicates whether the customer wants a scheduled order.
     */
    @Builder.Default
    private Boolean scheduledOrder = Boolean.FALSE;

    /**
     * Requested scheduled delivery or fulfillment time.
     */
    private LocalDateTime scheduledDeliveryAt;

    /**
     * Indicates whether the order is intended as a gift.
     */
    @Builder.Default
    private Boolean giftOrder = Boolean.FALSE;

    /**
     * Optional note from the customer.
     */
    @Size(max = 500)
    private String customerNote;

    /**
     * Optional special instructions associated with individual checkout items.
     */
    @Valid
    @Size(max = 100)
    private List<CheckoutItemInstructionRequest> itemInstructions;

    public List<CheckoutItemInstructionRequest> getItemInstruction() {
        if (itemInstructions == null) {
            itemInstructions = new ArrayList<>();
        }
        return itemInstructions;
    }
}
