package com.foodies.freshmeal.cart.service;

import com.foodies.freshmeal.cart.dto.AddCartItemRequest;
import com.foodies.freshmeal.cart.dto.CartResponse;
import com.foodies.freshmeal.cart.dto.CartSummaryResponse;
import com.foodies.freshmeal.cart.dto.RemoveCartItemRequest;
import com.foodies.freshmeal.cart.dto.UpdateCartItemRequest;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

/**
 * =============================================================================
 * Service : ICartService
 * =============================================================================
 *
 * Purpose
 * -------
 * Defines the business operations supported by the FreshMeal Cart module.
 *
 * <p>
 * The Cart service represents the application/business boundary for customer
 * cart operations. It orchestrates request validation, authenticated-user
 * resolution, food validation, cart ownership validation, restaurant/branch
 * consistency, domain mutation, persistence and response mapping.
 * </p>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * Customer identity is resolved from the authenticated service context.
 * The Cart API must never trust a user identifier supplied by the client.
 * </p>
 *
 * <h3>Business Boundary</h3>
 *
 * <p>
 * The service coordinates business rules but does not duplicate domain
 * invariants that belong inside {@code CartEntity}.
 * </p>
 *
 * <h3>Responsibility Separation</h3>
 *
 * <ul>
 * <li>{@code CartValidator} - request/input validation.</li>
 * <li>{@code ICartService} - application/business orchestration.</li>
 * <li>{@code CartEntity} - Cart aggregate invariants and state mutation.</li>
 * <li>{@code FoodService} - food resolution and food business state.</li>
 * <li>{@code ICartRepository} - Cart persistence.</li>
 * <li>{@code CartMapper} - entity-to-response mapping.</li>
 * </ul>
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface ICartService {

    /**
     * =========================================================================
     * ADD CART ITEM
     * =========================================================================
     *
     * <p>
     * Adds a food item to the authenticated customer's active cart.
     * </p>
     *
     * <p>
     * The service is responsible for resolving the current food information,
     * validating orderability, determining the restaurant/branch context and
     * obtaining the current price snapshot. The Cart aggregate then performs
     * the actual item mutation.
     * </p>
     *
     * @param input service input containing {@link AddCartItemRequest}
     * @return service output containing the updated {@link CartResponse}
     */
    IServiceOutput<CartResponse> addItem(
            IServiceInput<AddCartItemRequest> input);

    /**
     * =========================================================================
     * UPDATE CART ITEM QUANTITY
     * =========================================================================
     *
     * <p>
     * Updates the quantity of an existing cart item.
     * </p>
     *
     * <p>
     * The supplied quantity represents the final desired quantity rather than
     * an increment or decrement operation.
     * </p>
     *
     * @param input service input containing {@link UpdateCartItemRequest}
     * @return service output containing the updated {@link CartResponse}
     */
    IServiceOutput<CartResponse> updateItemQuantity(
            IServiceInput<UpdateCartItemRequest> input);

    /**
     * =========================================================================
     * REMOVE CART ITEM
     * =========================================================================
     *
     * <p>
     * Removes a food item from the authenticated customer's active cart.
     * </p>
     *
     * <p>
     * Removing the final item does not delete the Cart entity. The Cart remains
     * available as an empty active cart for future use.
     * </p>
     *
     * @param input service input containing {@link RemoveCartItemRequest}
     * @return service output containing the updated {@link CartResponse}
     */
    IServiceOutput<CartResponse> removeItem(
            IServiceInput<RemoveCartItemRequest> input);

    /**
     * =========================================================================
     * CLEAR CART
     * =========================================================================
     *
     * <p>
     * Removes all items from the authenticated customer's active cart while
     * retaining the Cart entity.
     * </p>
     *
     * <p>
     * No request payload is required because the authenticated customer is
     * resolved from the service context.
     * </p>
     *
     * @param input service input containing the authenticated service context
     * @return service output containing the empty {@link CartResponse}
     */
    IServiceOutput<CartResponse> clearCart(
            IServiceInput<Void> input);

    /**
     * =========================================================================
     * GET ACTIVE CART
     * =========================================================================
     *
     * <p>
     * Loads the authenticated customer's active Cart.
     * </p>
     *
     * <p>
     * The operation uses the authenticated user identity from the service
     * context and never accepts a customer identifier from the request.
     * </p>
     *
     * @param input service input containing the authenticated service context
     * @return service output containing the active {@link CartResponse}
     */
    IServiceOutput<CartResponse> getActiveCart(
            IServiceInput<Void> input);

    /**
     * =========================================================================
     * GET CART SUMMARY
     * =========================================================================
     *
     * <p>
     * Loads the lightweight summary of the authenticated customer's active
     * Cart.
     * </p>
     *
     * <p>
     * This operation is intended for scenarios such as cart badges, headers,
     * navigation elements and other views that do not require complete item
     * details.
     * </p>
     *
     * @param input service input containing the authenticated service context
     * @return service output containing {@link CartSummaryResponse}
     */
    IServiceOutput<CartSummaryResponse> getCartSummary(
            IServiceInput<Void> input);
}