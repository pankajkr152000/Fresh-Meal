
package com.foodies.freshmeal.checkout.service;

import com.foodies.freshmeal.checkout.dto.CheckoutResponse;
import com.foodies.freshmeal.checkout.dto.CheckoutReviewResponse;
import com.foodies.freshmeal.checkout.dto.ConfirmCheckoutRequest;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

/**
 * =============================================================================
 * Service : ICheckoutService
 * =============================================================================
 *
 * Purpose
 * -------
 * Defines the business operations supported by the FreshMeal Checkout module.
 *
 * <p>
 * The Checkout service represents the application/business boundary for
 * customer checkout operations. It coordinates cart resolution, ownership
 * validation, food and restaurant validation, address resolution, pricing,
 * checkout lifecycle management, order creation and persistence.
 * </p>
 *
 * <h3>Security Boundary</h3>
 *
 * <p>
 * Customer identity is resolved exclusively from the authenticated service
 * context. The Checkout API must never trust a user identifier supplied
 * by the client.
 * </p>
 *
 * <h3>Business Boundary</h3>
 *
 * <p>
 * The service coordinates business workflows without duplicating domain
 * invariants that belong inside {@code CheckoutEntity}.
 * </p>
 *
 * <h3>Reliability Requirements</h3>
 *
 * <ul>
 * <li>Validate current cart and food availability before checkout review.</li>
 * <li>Calculate prices and totals on the server.</li>
 * <li>Preserve immutable snapshots for customer review.</li>
 * <li>Revalidate critical conditions before confirmation.</li>
 * <li>Handle repeated requests and concurrent modifications safely.</li>
 * <li>Reconcile uncertain order-creation outcomes before retrying.</li>
 * <li>Preserve checkout history for audit and troubleshooting.</li>
 * </ul>
 *
 * <h3>Responsibility Separation</h3>
 *
 * <ul>
 * <li>{@code CheckoutValidator} - request and checkout validation.</li>
 * <li>{@code ICheckoutService} - application/business orchestration.</li>
 * <li>{@code CheckoutEntity} - checkout lifecycle and domain invariants.</li>
 * <li>{@code ICheckoutRepository} - checkout persistence.</li>
 * <li>{@code CartService} - cart resolution and cart business state.</li>
 * <li>{@code FoodService} - food resolution and food business state.</li>
 * <li>{@code CheckoutMapper} - entity-to-response mapping.</li>
 * <li>{@code OrderService} - order creation and order lifecycle.</li>
 * </ul>
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface ICheckoutService {

    /**
     * =========================================================================
     * INITIATE CHECKOUT
     * =========================================================================
     *
     * <p>
     * Initiates a new checkout attempt for the authenticated customer using
     * the customer's active cart.
     * </p>
     *
     * <p>
     * The service resolves the authenticated user, retrieves the active cart,
     * validates its eligibility for checkout, establishes the restaurant
     * context, and creates a persisted checkout attempt.
     * </p>
     *
     * <p>
     * Repeated requests must be handled safely to avoid creating unintended
     * duplicate checkout attempts.
     * </p>
     *
     * @param input service input containing the authenticated service context
     * @return service output containing the initiated checkout
     */
    IServiceOutput<CheckoutResponse> initiateCheckout(
            IServiceInput<Void> input);

    /**
     * =========================================================================
     * GET CHECKOUT
     * =========================================================================
     *
     * <p>
     * Retrieves a checkout using its business identifier.
     * </p>
     *
     * <p>
     * The service must verify that the checkout belongs to the authenticated
     * customer before returning its details.
     * </p>
     *
     * @param input service input containing the checkout business identifier
     * @return service output containing the checkout details
     */
    IServiceOutput<CheckoutResponse> getCheckout(
            IServiceInput<String> input);

    /**
     * =========================================================================
     * PREPARE CHECKOUT REVIEW
     * =========================================================================
     *
     * <p>
     * Validates the checkout against current business information and prepares
     * immutable snapshots of the items, delivery address and pricing for
     * customer review.
     * </p>
     *
     * <p>
     * The service must not trust previously captured prices or availability
     * without revalidation.
     * </p>
     *
     * @param input service input containing the checkout business identifier
     * @return service output containing the prepared checkout review
     */
    IServiceOutput<CheckoutReviewResponse> prepareCheckoutReview(
            IServiceInput<String> input);

    /**
     * =========================================================================
     * CONFIRM CHECKOUT
     * =========================================================================
     *
     * <p>
     * Confirms a checkout after validating its current state and coordinating
     * order creation.
     * </p>
     *
     * <p>
     * The implementation must handle duplicate requests, concurrency conflicts
     * and uncertain order-creation outcomes safely. It must reconcile an
     * uncertain outcome before attempting order creation again.
     * </p>
     *
     * @param input service input containing the confirmation request
     * @return service output containing the confirmed checkout and order
     *         reference
     */
    IServiceOutput<CheckoutResponse> confirmCheckout(
            IServiceInput<ConfirmCheckoutRequest> input);

    /**
     * =========================================================================
     * CANCEL CHECKOUT
     * =========================================================================
     *
     * <p>
     * Cancels a checkout when its current lifecycle state permits cancellation.
     * </p>
     *
     * <p>
     * The service verifies ownership and delegates lifecycle mutation to the
     * Checkout aggregate.
     * </p>
     *
     * @param input service input containing the checkout business identifier
     * @return service output containing the updated checkout
     */
    IServiceOutput<CheckoutResponse> cancelCheckout(
            IServiceInput<String> input);

}