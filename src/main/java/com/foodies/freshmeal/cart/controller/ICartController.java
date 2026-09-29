
package com.foodies.freshmeal.cart.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.cart.dto.AddCartItemRequest;
import com.foodies.freshmeal.cart.dto.CartResponse;
import com.foodies.freshmeal.cart.dto.CartSummaryResponse;
import com.foodies.freshmeal.cart.dto.RemoveCartItemRequest;
import com.foodies.freshmeal.cart.dto.UpdateCartItemRequest;
import com.foodies.freshmeal.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Cart Controller Contract
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Cart module.
 *
 * <p>
 * The controller is responsible only for request handling and delegation.
 * Business logic belongs to the Cart service layer.
 * </p>
 *
 * <p>
 * OpenAPI documentation is defined at the controller contract level so that
 * the Cart API contract remains centralized while the controller
 * implementation remains focused on HTTP request handling and service
 * delegation.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Cart", description = "APIs for customer cart management, item operations and cart summary.")
public interface ICartController {

    // =========================================================================
    // Cart Item Operations
    // =========================================================================

    /**
     * Adds a food item to the authenticated customer's active cart.
     *
     * <p>
     * If the food item already exists in the cart, its quantity is increased.
     * The cart service validates food availability, pricing and restaurant
     * consistency before updating the cart.
     * </p>
     *
     * @param request food identifier and quantity
     * @return updated cart
     */
    @Operation(summary = "Add item to cart", description = "Adds a food item to the authenticated customer's active cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart item added successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid cart item request or food is unavailable.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Food item not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cart conflicts with an existing business rule or concurrent update.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<CartResponse>> addItem(
            AddCartItemRequest request);

    /**
     * Updates the quantity of an existing cart item.
     *
     * <p>
     * The requested quantity represents the final desired quantity rather
     * than an increment. The service validates the current food state and
     * price before applying the update.
     * </p>
     *
     * @param request food identifier and desired quantity
     * @return updated cart
     */
    @Operation(summary = "Update cart item quantity", description = "Sets the final desired quantity of an existing cart item.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart item quantity updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid quantity or cart item request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Active cart or cart item not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cart update conflicts with an existing business rule or concurrent update.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<CartResponse>> updateItemQuantity(
            UpdateCartItemRequest request);

    /**
     * Removes a food item from the authenticated customer's active cart.
     *
     * <p>
     * Removal is permitted even if the food item is no longer available.
     * The cart document is retained when the last item is removed.
     * </p>
     *
     * @param request food identifier
     * @return updated cart
     */
    @Operation(summary = "Remove cart item", description = "Removes a food item from the authenticated customer's active cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart item removed successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Active cart or cart item not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cart update conflicts with a concurrent modification.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<CartResponse>> removeItem(
            RemoveCartItemRequest request);

    /**
     * Removes all items from the authenticated customer's active cart.
     *
     * <p>
     * The cart document is retained and remains reusable after clearing.
     * </p>
     *
     * @return empty cart
     */
    @Operation(summary = "Clear cart", description = "Removes all items from the authenticated customer's active cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart cleared successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Active cart not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cart update conflicts with a concurrent modification.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<CartResponse>> clearCart();

    // =========================================================================
    // Cart Retrieval Operations
    // =========================================================================

    /**
     * Retrieves the authenticated customer's active cart.
     *
     * <p>
     * If the customer does not have an active cart, an empty cart response
     * is returned. This operation does not create a cart.
     * </p>
     *
     * @return active cart or empty cart response
     */
    @Operation(summary = "Get active cart", description = "Retrieves the authenticated customer's active cart without creating a new cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active cart retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<CartResponse>> getActiveCart();

    /**
     * Retrieves a lightweight summary of the authenticated customer's active cart.
     *
     * <p>
     * If no active cart exists, a summary containing zero item counts and
     * a zero subtotal is returned.
     * </p>
     *
     * @return active cart summary
     */
    @Operation(summary = "Get cart summary", description = "Retrieves item counts and subtotal for the authenticated customer's active cart.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart summary retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<CartSummaryResponse>> getCartSummary();
}