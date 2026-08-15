package com.foodies.freshmeal.order.service;

import java.util.List;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.order.dto.CreateOrderRequest;
import com.foodies.freshmeal.order.dto.OrderCancellationRequest;
import com.foodies.freshmeal.order.dto.OrderDetailsResponse;
import com.foodies.freshmeal.order.dto.OrderListResponse;
import com.foodies.freshmeal.order.dto.OrderMetadataResponse;
import com.foodies.freshmeal.order.dto.OrderSearchRequest;
import com.foodies.freshmeal.order.dto.OrderStatusUpdateRequest;
import com.foodies.freshmeal.order.entity.OrderEntity;

/**
 * ============================================================================
 * Interface : IOrderService
 * ============================================================================
 *
 * Defines the application service contract for the FreshMeal Order module.
 *
 * <p>
 * The service layer coordinates Order-related business operations while
 * keeping controllers independent from repositories and persistence models.
 * </p>
 *
 * <p>
 * All service operations use {@link IServiceInput} and
 * {@link IServiceOutput} so that the FreshMeal service infrastructure can
 * consistently carry request context, validation, auditing and execution
 * information.
 * </p>
 *
 * <p>
 * {@link com.foodies.freshmeal.common.io.service.IServiceContext} remains the
 * central execution context for the service layer.
 * </p>
 *
 * ============================================================================
 *
 * Service Flow
 * ------------
 *
 * Controller
 * ↓
 * IServiceInput
 * ↓
 * IOrderService
 * ↓
 * OrderServiceImpl
 * ↓
 * Repository / Domain Operations
 * ↓
 * OrderMapper
 * ↓
 * IServiceOutput
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IOrderService {

    // =========================================================================
    // Order Creation
    // =========================================================================

    /**
     * Creates a new Order.
     *
     * <p>
     * The authenticated customer is resolved from {@code IServiceContext}.
     * Customer, restaurant, address and food information are converted into
     * historical snapshots by the service layer.
     * </p>
     *
     * <p>
     * Pricing, Order number, lifecycle status and other server-controlled
     * values must never be trusted from the client request.
     * </p>
     *
     * @param input Order creation request.
     *
     * @return created Order details.
     */
    IServiceOutput<OrderDetailsResponse> createOrder(
            IServiceInput<CreateOrderRequest> input);

    // =========================================================================
    // Order Loading
    // =========================================================================

    /**
     * Loads an OrderEntity by its database identifier.
     *
     * <p>
     * This method is intended for internal service/domain operations where the
     * persistence entity itself is required.
     * </p>
     *
     * @param input Order identifier.
     *
     * @return OrderEntity when found.
     */
    IServiceOutput<OrderEntity> loadOrder(
            IServiceInput<String> input);

    // =========================================================================
    // Order Search / List
    // =========================================================================

    /**
     * Searches Orders for the Order Management screen.
     *
     * <p>
     * Supports search, filtering, sorting and pagination according to
     * {@link OrderSearchRequest}.
     * </p>
     *
     * @param input Order search criteria.
     *
     * @return matching Order list.
     */
    IServiceOutput<List<OrderListResponse>> searchOrders(
            IServiceInput<OrderSearchRequest> input);

    // =========================================================================
    // Order Details
    // =========================================================================

    /**
     * Retrieves complete Order details using the database identifier.
     *
     * @param input Order identifier.
     *
     * @return complete Order details.
     */
    IServiceOutput<EntityViewResponse<OrderDetailsResponse>> getOrderDetails(
            IServiceInput<String> input);

    /**
     * Retrieves complete Order details using the business-facing Order number.
     *
     * <p>
     * Example:
     *
     * <pre>
     * FM - ORD - 0000001
     * </pre>
     *
     * </p>
     *
     * @param input Business Order number.
     *
     * @return complete Order details.
     */
    IServiceOutput<EntityViewResponse<OrderDetailsResponse>> getOrderByOrderNumber(
            IServiceInput<String> input);

    // =========================================================================
    // Order Lifecycle
    // =========================================================================

    /**
     * Updates the lifecycle status of an Order.
     *
     * <p>
     * The service layer validates the requested transition against the
     * current Order status before modifying the entity.
     * </p>
     *
     * @param input Order status update request.
     *
     * @return updated Order details.
     */
    IServiceOutput<OrderDetailsResponse> updateOrderStatus(
            IServiceInput<OrderStatusUpdateRequest> input);

    // =========================================================================
    // Cancellation
    // =========================================================================

    /**
     * Cancels an Order.
     *
     * <p>
     * Cancellation rules are enforced by the service/domain layer.
     * </p>
     *
     * @param input Order cancellation request.
     *
     * @return cancelled Order details.
     */
    IServiceOutput<OrderDetailsResponse> cancelOrder(
            IServiceInput<OrderCancellationRequest> input);

    // =========================================================================
    // Archive Operations
    // =========================================================================

    /**
     * Archives an Order using logical deletion.
     *
     * @param input Order identifier.
     *
     * @return archived Order details.
     */
    IServiceOutput<OrderDetailsResponse> archiveOrder(
            IServiceInput<String> input);

    // =========================================================================
    // Restore Operations
    // =========================================================================

    /**
     * Restores a previously archived Order.
     *
     * @param input Order identifier.
     *
     * @return restored Order details.
     */
    IServiceOutput<OrderDetailsResponse> restoreOrder(
            IServiceInput<String> input);

    // =========================================================================
    // Permanent Delete Operations
    // =========================================================================

    /**
     * Permanently deletes an archived Order.
     *
     * <p>
     * Permanent deletion should only be allowed for an already archived
     * Order.
     * </p>
     *
     * @param input Order identifier.
     *
     * @return deleted Order details.
     */
    IServiceOutput<OrderDetailsResponse> permanentDeleteOrder(
            IServiceInput<String> input);

    // =========================================================================
    // Archived Orders
    // =========================================================================

    /**
     * Retrieves archived Orders.
     *
     * @return archived Order list.
     */
    IServiceOutput<List<OrderListResponse>> readArchivedOrders();

    // =========================================================================
    // Order Metadata
    // =========================================================================

    /**
     * Retrieves metadata required by Order Management UI controls.
     *
     * @return Order metadata.
     */
    IServiceOutput<OrderMetadataResponse> getOrderMetadata();

    // =========================================================================
    // Display Options
    // =========================================================================

    /**
     * Retrieves available Order lifecycle statuses.
     *
     * @return Order status options.
     */
    IServiceOutput<List<DisplayOptionResponse>> getOrderStatuses();

    /**
     * Retrieves available Delivery statuses.
     *
     * @return Delivery status options.
     */
    IServiceOutput<List<DisplayOptionResponse>> getDeliveryStatuses();

    /**
     * Retrieves available Payment statuses.
     *
     * @return Payment status options.
     */
    IServiceOutput<List<DisplayOptionResponse>> getPaymentStatuses();

    /**
     * Retrieves available Refund statuses.
     *
     * @return Refund status options.
     */
    IServiceOutput<List<DisplayOptionResponse>> getRefundStatuses();

    /**
     * Retrieves available Order types.
     *
     * @return Order type options.
     */
    IServiceOutput<List<DisplayOptionResponse>> getOrderTypes();

}