
package com.foodies.freshmeal.cart.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.cart.constants.CartApiConstants;
import com.foodies.freshmeal.cart.constants.CartApiMessageConstants;
import com.foodies.freshmeal.cart.controller.ICartController;
import com.foodies.freshmeal.cart.dto.AddCartItemRequest;
import com.foodies.freshmeal.cart.dto.CartResponse;
import com.foodies.freshmeal.cart.dto.CartSummaryResponse;
import com.foodies.freshmeal.cart.dto.RemoveCartItemRequest;
import com.foodies.freshmeal.cart.dto.UpdateCartItemRequest;
import com.foodies.freshmeal.cart.service.ICartService;
import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.AuthorizationConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;

/**
 * ============================================================================
 * Cart Controller
 * ============================================================================
 *
 * Responsibilities
 * ----------------
 *
 * <ul>
 * <li>Receive HTTP requests for Cart operations.</li>
 * <li>Build service input objects.</li>
 * <li>Delegate business operations to CartService.</li>
 * <li>Return standardized ApiResponse objects.</li>
 * </ul>
 *
 * <p>
 * The controller must not contain Cart business logic. All cart ownership,
 * food availability, pricing, restaurant consistency and lifecycle validation
 * are handled by the service and domain layers.
 * </p>
 *
 * <p>
 * Customer identity is resolved from the authenticated service context.
 * The controller does not accept a customer identifier from the client.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(CartApiConstants.BASE_URL)
public class CartController implements ICartController {

    private final ICartService cartService;

    private final IServiceContext serviceContext;

    /**
     * Creates CartControllerImpl.
     *
     * @param cartService    Cart service.
     * @param serviceContext Service execution context.
     */
    public CartController(
            ICartService cartService,
            IServiceContext serviceContext) {

        this.cartService = cartService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Add Cart Item
    // =========================================================================

    /**
     * Adds a food item to the authenticated customer's active cart.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * POST / api / cart / items / create
     * </pre>
     *
     * @param request food identifier and quantity
     * @return updated cart
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.ADD_CART_ITEM, module = ModuleType.CART, method = MethodType.CREATE)
    @PostMapping(CartApiConstants.CREATE_ITEM)
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @RequestBody AddCartItemRequest request) {

        IServiceInput<AddCartItemRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<CartResponse> serviceOutput = cartService.addItem(serviceInput);

        return ApiResponseBuilder.success(
                CartApiMessageConstants.CART_ITEM_CREATED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Update Cart Item Quantity
    // =========================================================================

    /**
     * Updates the quantity of an existing cart item.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * PUT / api / cart / items / update
     * </pre>
     *
     * <p>
     * The request quantity represents the final desired quantity rather than
     * an increment.
     * </p>
     *
     * @param request food identifier and desired quantity
     * @return updated cart
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.UPDATE_CART_ITEM, module = ModuleType.CART, method = MethodType.UPDATE)
    @PutMapping(CartApiConstants.UPDATE_ITEM)
    public ResponseEntity<ApiResponse<CartResponse>> updateItemQuantity(
            @RequestBody UpdateCartItemRequest request) {

        IServiceInput<UpdateCartItemRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<CartResponse> serviceOutput = cartService.updateItemQuantity(serviceInput);

        return ApiResponseBuilder.success(
                CartApiMessageConstants.CART_ITEM_UPDATED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Remove Cart Item
    // =========================================================================

    /**
     * Removes a food item from the authenticated customer's active cart.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * DELETE / api / cart / items / delete
     * </pre>
     *
     * <p>
     * The item can be removed even if its associated food is no longer
     * available.
     * </p>
     *
     * @param request food identifier
     * @return updated cart
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.REMOVE_CART_ITEM, module = ModuleType.CART, method = MethodType.DELETE)
    @DeleteMapping(CartApiConstants.DELETE_ITEM)
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @RequestBody RemoveCartItemRequest request) {

        IServiceInput<RemoveCartItemRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<CartResponse> serviceOutput = cartService.removeItem(serviceInput);

        return ApiResponseBuilder.success(
                CartApiMessageConstants.CART_ITEM_DELETED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Clear Cart
    // =========================================================================

    /**
     * Removes all items from the authenticated customer's active cart.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * DELETE / api / cart / clear
     * </pre>
     *
     * <p>
     * The cart document is retained and remains reusable after clearing.
     * </p>
     *
     * @return empty cart
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.CLEAR_CART, module = ModuleType.CART, method = MethodType.DELETE)
    @DeleteMapping(CartApiConstants.CLEAR_CART)
    public ResponseEntity<ApiResponse<CartResponse>> clearCart() {

        IServiceInput<Void> serviceInput = new ServiceInput<>();

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<CartResponse> serviceOutput = cartService.clearCart(serviceInput);

        return ApiResponseBuilder.success(
                CartApiMessageConstants.CART_CLEARED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Read Active Cart
    // =========================================================================

    /**
     * Retrieves the authenticated customer's active cart.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * GET / api / cart / readActiveCart
     * </pre>
     *
     * <p>
     * If no active cart exists, an empty cart response is returned.
     * This operation does not create a cart.
     * </p>
     *
     * @return active cart or empty cart response
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.READ_ACTIVE_CART, module = ModuleType.CART, method = MethodType.READ)
    @GetMapping(CartApiConstants.READ_ACTIVE_CART)
    public ResponseEntity<ApiResponse<CartResponse>> getActiveCart() {

        IServiceInput<Void> serviceInput = new ServiceInput<>();

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<CartResponse> serviceOutput = cartService.getActiveCart(serviceInput);

        return ApiResponseBuilder.success(
                CartApiMessageConstants.ACTIVE_CART_FOUND,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Read Cart Summary
    // =========================================================================

    /**
     * Retrieves a lightweight summary of the authenticated customer's
     * active cart.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * GET / api / cart / readSummary
     * </pre>
     *
     * <p>
     * The summary contains the cart status, total item count, total quantity
     * and subtotal.
     * </p>
     *
     * @return cart summary
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.READ_CART_SUMMARY, module = ModuleType.CART, method = MethodType.READ)
    @GetMapping(CartApiConstants.READ_CART_SUMMARY)
    public ResponseEntity<ApiResponse<CartSummaryResponse>> getCartSummary() {

        IServiceInput<Void> serviceInput = new ServiceInput<>();

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<CartSummaryResponse> serviceOutput = cartService.getCartSummary(serviceInput);

        return ApiResponseBuilder.success(
                CartApiMessageConstants.CART_SUMMARY_FOUND,
                serviceOutput.getOutput());
    }
}