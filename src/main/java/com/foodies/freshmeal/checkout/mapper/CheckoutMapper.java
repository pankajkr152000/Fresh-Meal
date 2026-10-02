
package com.foodies.freshmeal.checkout.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;
import com.foodies.freshmeal.checkout.dto.CheckoutResponse;
import com.foodies.freshmeal.checkout.dto.CheckoutReviewResponse;
import com.foodies.freshmeal.checkout.entity.CheckoutEntity;

/**
 * Handles conversion of checkout entities into customer-facing response DTOs.
 *
 * <p>
 * This mapper centralizes entity-to-response transformation and prevents
 * persistence-specific details from leaking into the API layer.
 * </p>
 *
 * <p>
 * Business operations such as checkout validation, pricing calculation,
 * availability checks, and lifecycle transitions must remain in the
 * Checkout service.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class CheckoutMapper {

    /**
     * Converts a checkout entity into its general response representation.
     *
     * @param checkoutEntity the checkout entity to map
     * @return the mapped checkout response
     * @throws NullPointerException if the entity is null
     */
    public CheckoutResponse toCheckoutResponse(
            final CheckoutEntity checkoutEntity) {

        Objects.requireNonNull(
                checkoutEntity,
                "Checkout entity is required.");

        final CheckoutStatusConstant status = checkoutEntity.getStatus();

        final boolean expired = checkoutEntity.isExpired();

        final boolean readyForConfirmation = checkoutEntity.isReadyForConfirmation() && !expired;

        return CheckoutResponse.builder()
                .checkoutNumber(checkoutEntity.getCheckoutNumber())
                .cartNumber(checkoutEntity.getCartNumber())
                .restaurantNumber(checkoutEntity.getRestaurantNumber())
                .restaurantBranchNumber(
                        checkoutEntity.getRestaurantBranchNumber())
                .status(status)
                .statusUpdatedAt(checkoutEntity.getStatusUpdatedAt())
                .initiatedAt(checkoutEntity.getInitiatedAt())
                .expiresAt(checkoutEntity.getExpiresAt())
                .confirmedAt(checkoutEntity.getConfirmedAt())
                .completedAt(checkoutEntity.getCompletedAt())
                .orderNumber(checkoutEntity.getOrderNumber())
                .canBeReviewed(readyForConfirmation)
                .canBeConfirmed(readyForConfirmation)
                .canBeCancelled(readyForConfirmation)
                .expired(expired)
                .build();
    }

    /**
     * Converts a checkout entity into the detailed review response.
     *
     * @param checkoutEntity the checkout entity to map
     * @return the mapped checkout review response
     * @throws NullPointerException if the entity is null
     */
    public CheckoutReviewResponse toCheckoutReviewResponse(
            final CheckoutEntity checkoutEntity) {

        Objects.requireNonNull(
                checkoutEntity,
                "Checkout entity is required.");

        final boolean canBeConfirmed = checkoutEntity.isReadyForConfirmation()
                && !checkoutEntity.isExpired();

        return CheckoutReviewResponse.builder()
                .checkoutNumber(checkoutEntity.getCheckoutNumber())
                .status(checkoutEntity.getStatus())
                .items(checkoutEntity.getItems())
                .deliveryAddress(checkoutEntity.getDeliveryAddress())
                .pricing(checkoutEntity.getPricing())
                .expiresAt(checkoutEntity.getExpiresAt())
                .canBeConfirmed(canBeConfirmed)
                .build();
    }
}