
package com.foodies.freshmeal.checkout.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;
import com.foodies.freshmeal.checkout.valueObject.CheckoutAddressSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutItemSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutPricingSnapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the checkout review presented to the customer before
 * confirming an order.
 *
 * <p>
 * This response contains the checkout item snapshots, delivery address,
 * and pricing breakdown that the customer is expected to review.
 * </p>
 *
 * <p>
 * All item and pricing information is generated and validated by the
 * backend. The client must not modify these values when submitting
 * a checkout confirmation request.
 * </p>
 *
 * <p>
 * The response reflects the latest validated checkout state.
 * The Checkout service must revalidate relevant business conditions
 * before creating an order, since the underlying food availability,
 * pricing, or delivery conditions may change after review.
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
public class CheckoutReviewResponse {

    /**
     * Unique business identifier of the checkout session.
     */
    private String checkoutNumber;

    /**
     * Current lifecycle status of the checkout.
     */
    private CheckoutStatusConstant status;

    /**
     * Food items and quantities included in the checkout review.
     */
    private List<CheckoutItemSnapshot> items;

    /**
     * Delivery address reviewed by the customer.
     */
    private CheckoutAddressSnapshot deliveryAddress;

    /**
     * Backend-calculated pricing breakdown presented to the customer.
     */
    private CheckoutPricingSnapshot pricing;

    /**
     * Timestamp after which the checkout session expires.
     */
    private LocalDateTime expiresAt;

    /**
     * Indicates whether the checkout is currently eligible for
     * customer confirmation.
     */
    private boolean canBeConfirmed;
}