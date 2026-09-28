package com.foodies.freshmeal.cart.mapper;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.cart.dto.CartItemResponse;
import com.foodies.freshmeal.cart.dto.CartResponse;
import com.foodies.freshmeal.cart.dto.CartSummaryResponse;
import com.foodies.freshmeal.cart.entity.CartEntity;
import com.foodies.freshmeal.cart.entity.CartItem;
import com.foodies.freshmeal.cart.valueObject.FoodSnapshot;
import com.foodies.freshmeal.image.dto.ImageSnapshot;

/**
 * Mapper responsible for converting Cart domain objects into API response DTOs.
 *
 * <p>
 * This mapper intentionally contains no business logic, repository access,
 * authentication logic, or external entity resolution.
 * </p>
 *
 * <p>
 * Cart request DTOs are not mapped directly to {@link CartEntity}. Cart
 * mutation requires domain information such as the current Food entity,
 * restaurant context, availability, and current price, which are resolved
 * by the Cart service before invoking the Cart aggregate.
 * </p>
 */
@Component
public class CartMapper {

    // ============================================================================
    // Cart Response Mapping
    // ============================================================================

    /**
     * Maps a Cart entity to its API response representation.
     *
     * @param cart Cart entity.
     * @return Cart response, or {@code null} when the supplied cart is null.
     */
    public CartResponse toResponse(final CartEntity cart) {

        if (cart == null) {
            return null;
        }

        return CartResponse.builder()
                .cartNumber(cart.getCartNumber())
                .restaurantNumber(cart.getRestaurantNumber())
                .restaurantBranchNumber(cart.getRestaurantBranchNumber())
                .status(cart.getStatus())
                .expiresAt(cart.getExpiresAt())
                .items(toItemResponses(cart.getItems()))
                .subtotal(cart.getSubtotal())
                .totalItemCount(cart.getTotalItemCount())
                .totalQuantity(cart.getTotalQuantity())
                .build();
    }

    // ============================================================================
    // Cart Summary Mapping
    // ============================================================================

    /**
     * Maps a Cart entity to its lightweight summary response.
     *
     * @param cart Cart entity.
     * @return Cart summary response, or {@code null} when the supplied cart is
     *         null.
     */
    public CartSummaryResponse toSummaryResponse(final CartEntity cart) {

        if (cart == null) {
            return null;
        }

        return CartSummaryResponse.builder()
                .cartNumber(cart.getCartNumber())
                .status(cart.getStatus())
                .totalItemCount(cart.getTotalItemCount())
                .totalQuantity(cart.getTotalQuantity())
                .subtotal(cart.getSubtotal())
                .build();
    }

    // ============================================================================
    // Cart Item Mapping
    // ============================================================================

    /**
     * Maps a Cart item to its API response representation.
     *
     * @param cartItem Cart item.
     * @return Cart item response, or {@code null} when the supplied item is null.
     */
    public CartItemResponse toItemResponse(final CartItem cartItem) {

        if (cartItem == null) {
            return null;
        }

        final FoodSnapshot foodSnapshot = cartItem.getFoodSnapshot();

        return CartItemResponse.builder()
                .foodNumber(getFoodNumber(foodSnapshot))
                .foodName(getFoodName(foodSnapshot))
                .foodImage(getFoodImage(foodSnapshot))
                .unitPrice(cartItem.getUnitPrice())
                .quantity(cartItem.getQuantity())
                .itemTotal(cartItem.getItemTotal())
                .addedAt(cartItem.getAddedAt())
                .build();
    }

    /**
     * Maps a list of Cart items to API response DTOs.
     *
     * @param items Cart items.
     * @return mapped Cart item responses.
     */
    public List<CartItemResponse> toItemResponses(
            final List<CartItem> items) {

        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(this::toItemResponse)
                .toList();
    }

    // ============================================================================
    // Food Snapshot Mapping Helpers
    // ============================================================================

    /**
     * Extracts the food business identifier from the snapshot.
     *
     * @param foodSnapshot food snapshot.
     * @return food number, or {@code null} when the snapshot is null.
     */
    private String getFoodNumber(final FoodSnapshot foodSnapshot) {

        return foodSnapshot != null
                ? foodSnapshot.getFoodNumber()
                : null;
    }

    /**
     * Extracts the food name from the snapshot.
     *
     * @param foodSnapshot food snapshot.
     * @return food name, or {@code null} when the snapshot is null.
     */
    private String getFoodName(final FoodSnapshot foodSnapshot) {

        return foodSnapshot != null
                ? foodSnapshot.getFoodName()
                : null;
    }

    /**
     * Extracts the food image from the snapshot.
     *
     * @param foodSnapshot food snapshot.
     * @return food image, or {@code null} when the snapshot is null.
     */
    private ImageSnapshot getFoodImage(
            final FoodSnapshot foodSnapshot) {

        return foodSnapshot != null
                ? foodSnapshot.getFoodImage()
                : null;
    }
}