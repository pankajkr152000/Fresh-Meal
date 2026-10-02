
package com.foodies.freshmeal.checkout.dto;

import java.time.LocalDateTime;

import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the customer-facing response for a checkout.
 *
 * <p>
 * Exposes checkout identity, lifecycle information, associated business
 * references, and timestamps without exposing persistence-specific details.
 * </p>
 *
 * <p>
 * This DTO must not expose internal MongoDB identifiers, technical exception
 * details, or sensitive reconciliation information.
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
public class CheckoutResponse {

    /**
     * Unique business identifier of the checkout.
     */
    private String checkoutNumber;

    /**
     * Business identifier of the associated cart.
     */
    private String cartNumber;

    /**
     * Business identifier of the restaurant.
     */
    private String restaurantNumber;

    /**
     * Business identifier of the restaurant branch.
     */
    private String restaurantBranchNumber;

    /**
     * Current lifecycle status of the checkout.
     */
    private CheckoutStatusConstant status;

    /**
     * Timestamp of the most recent checkout status transition.
     */
    private LocalDateTime statusUpdatedAt;

    /**
     * Timestamp when the checkout was initiated.
     */
    private LocalDateTime initiatedAt;

    /**
     * Timestamp after which the checkout is no longer eligible for
     * confirmation.
     */
    private LocalDateTime expiresAt;

    /**
     * Timestamp when checkout confirmation was completed.
     */
    private LocalDateTime confirmedAt;

    /**
     * Timestamp when the checkout workflow was completed.
     */
    private LocalDateTime completedAt;

    /**
     * Business identifier of the associated order, if one has been created.
     */
    private String orderNumber;

    /**
     * Indicates whether the checkout is eligible for customer review.
     *
     * <p>
     * This is a UI guidance field. The service must independently validate
     * the checkout state before processing a review request.
     * </p>
     */
    private boolean canBeReviewed;

    /**
     * Indicates whether the checkout is eligible for confirmation.
     *
     * <p>
     * This is a UI guidance field and does not replace server-side
     * lifecycle validation.
     * </p>
     */
    private boolean canBeConfirmed;

    /**
     * Indicates whether the checkout is eligible for cancellation.
     *
     * <p>
     * This is a UI guidance field and does not replace server-side
     * lifecycle validation.
     * </p>
     */
    private boolean canBeCancelled;

    /**
     * Indicates whether the checkout has expired according to the
     * checkout lifecycle rules.
     */
    private boolean expired;

}