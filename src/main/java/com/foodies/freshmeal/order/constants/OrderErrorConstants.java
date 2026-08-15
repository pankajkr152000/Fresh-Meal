package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Order Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Order module.
 *
 * <p>
 * Each error contains:
 * </p>
 *
 * <ul>
 * <li>Error Code</li>
 * <li>User Friendly Message</li>
 * <li>HTTP Status Code</li>
 * </ul>
 *
 * <p>
 * These errors are used by:
 * </p>
 *
 * <ul>
 * <li>Order Services</li>
 * <li>Order Validators</li>
 * <li>Order Controllers</li>
 * <li>GlobalExceptionHandler</li>
 * </ul>
 *
 * ============================================================================
 *
 * Error Code Convention
 * ---------------------
 *
 * FM-ORDER-001
 *
 * FM -> FreshMeal
 * ORDER -> Order module
 * 001 -> Error number
 *
 * ============================================================================
 *
 * Error Groups
 * ------------
 *
 * 001 - General Order Errors
 * 100 - Order Validation
 * 200 - Status / Lifecycle
 * 300 - Customer
 * 400 - Restaurant
 * 500 - Food / Items
 * 600 - Delivery
 * 700 - Payment
 * 800 - Cancellation
 * 900 - Archive / Delete
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum OrderErrorConstants implements IBusinessError {

    // =========================================================================
    // General Order Errors
    // =========================================================================

    ORDER_NOT_FOUND(
            "FM-ORDER-001",
            "Order not found.",
            HttpStatusCode.NOT_FOUND),

    ORDER_ALREADY_EXISTS(
            "FM-ORDER-002",
            "Order already exists.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_ARCHIVED(
            "FM-ORDER-003",
            "Order is already archived.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_ACTIVE(
            "FM-ORDER-004",
            "Order is already active.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_DELETED(
            "FM-ORDER-005",
            "Order has already been permanently deleted.",
            HttpStatusCode.GONE),

    ORDER_NUMBER_NOT_FOUND(
            "FM-ORDER-006",
            "Order number not found.",
            HttpStatusCode.NOT_FOUND),

    // =========================================================================
    // Order Validation
    // =========================================================================

    ORDER_TYPE_REQUIRED(
            "FM-ORDER-100",
            "Order type is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_REQUIRED(
            "FM-ORDER-101",
            "Restaurant is required.",
            HttpStatusCode.BAD_REQUEST),

    ORDER_ITEMS_REQUIRED(
            "FM-ORDER-102",
            "At least one food item is required.",
            HttpStatusCode.BAD_REQUEST),

    PAYMENT_MODE_REQUIRED(
            "FM-ORDER-103",
            "Payment mode is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_ORDER_REQUEST(
            "FM-ORDER-104",
            "Invalid order request.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_ORDER_TYPE(
            "FM-ORDER-105",
            "Invalid order type.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_ORDER_QUANTITY(
            "FM-ORDER-106",
            "Order item quantity must be greater than zero.",
            HttpStatusCode.BAD_REQUEST),

    DUPLICATE_ORDER_ITEM(
            "FM-ORDER-107",
            "Duplicate food items are not allowed in an order.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_ADDRESS_REQUIRED(
            "FM-ORDER-108",
            "Delivery address is required for delivery orders.",
            HttpStatusCode.BAD_REQUEST),

    SCHEDULED_DELIVERY_TIME_REQUIRED(
            "FM-ORDER-109",
            "Scheduled delivery time is required for scheduled orders.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_SCHEDULED_DELIVERY_TIME(
            "FM-ORDER-110",
            "Scheduled delivery time must be in the future.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Order Status / Lifecycle
    // =========================================================================

    INVALID_ORDER_STATUS(
            "FM-ORDER-200",
            "Invalid order status.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_STATUS_TRANSITION(
            "FM-ORDER-201",
            "Invalid order status transition.",
            HttpStatusCode.BAD_REQUEST),

    ORDER_ALREADY_PLACED(
            "FM-ORDER-202",
            "Order has already been placed.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_CONFIRMED(
            "FM-ORDER-203",
            "Order is already confirmed.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_PREPARING(
            "FM-ORDER-204",
            "Order is already being prepared.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_READY(
            "FM-ORDER-205",
            "Order is already ready for pickup.",
            HttpStatusCode.CONFLICT),

    ORDER_ALREADY_DELIVERED(
            "FM-ORDER-206",
            "Order has already been delivered.",
            HttpStatusCode.CONFLICT),

    ORDER_NOT_MODIFIABLE(
            "FM-ORDER-207",
            "Order can no longer be modified.",
            HttpStatusCode.CONFLICT),

    ORDER_NOT_CANCELLABLE(
            "FM-ORDER-208",
            "Order can no longer be cancelled.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Customer
    // =========================================================================

    CUSTOMER_REQUIRED(
            "FM-ORDER-300",
            "Customer information is required.",
            HttpStatusCode.BAD_REQUEST),

    CUSTOMER_NOT_FOUND(
            "FM-ORDER-301",
            "Customer not found.",
            HttpStatusCode.NOT_FOUND),

    CUSTOMER_NOT_AUTHENTICATED(
            "FM-ORDER-302",
            "Authenticated customer could not be identified.",
            HttpStatusCode.UNAUTHORIZED),

    CUSTOMER_ADDRESS_NOT_FOUND(
            "FM-ORDER-303",
            "Customer delivery address not found.",
            HttpStatusCode.NOT_FOUND),

    CUSTOMER_ADDRESS_NOT_AVAILABLE(
            "FM-ORDER-304",
            "Selected delivery address is not available.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Restaurant
    // =========================================================================

    RESTAURANT_NOT_FOUND(
            "FM-ORDER-400",
            "Restaurant not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_NOT_AVAILABLE(
            "FM-ORDER-401",
            "Restaurant is currently unavailable.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_CLOSED(
            "FM-ORDER-402",
            "Restaurant is currently closed.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_NOT_ACCEPTING_ORDERS(
            "FM-ORDER-403",
            "Restaurant is currently not accepting orders.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_DOES_NOT_SERVE_ADDRESS(
            "FM-ORDER-404",
            "Restaurant does not serve the selected delivery address.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Food / Order Items
    // =========================================================================

    FOOD_NOT_FOUND(
            "FM-ORDER-500",
            "One or more ordered food items were not found.",
            HttpStatusCode.NOT_FOUND),

    FOOD_NOT_AVAILABLE(
            "FM-ORDER-501",
            "One or more selected food items are not available.",
            HttpStatusCode.CONFLICT),

    FOOD_OUT_OF_STOCK(
            "FM-ORDER-502",
            "One or more selected food items are out of stock.",
            HttpStatusCode.CONFLICT),

    FOOD_DISCONTINUED(
            "FM-ORDER-503",
            "One or more selected food items have been discontinued.",
            HttpStatusCode.CONFLICT),

    FOOD_NOT_SUPPORTED_BY_RESTAURANT(
            "FM-ORDER-504",
            "One or more selected food items are not available from this restaurant.",
            HttpStatusCode.CONFLICT),

    INVALID_FOOD_QUANTITY(
            "FM-ORDER-505",
            "Invalid food quantity.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Delivery
    // =========================================================================

    INVALID_DELIVERY_STATUS(
            "FM-ORDER-600",
            "Invalid delivery status.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_DELIVERY_STATUS_TRANSITION(
            "FM-ORDER-601",
            "Invalid delivery status transition.",
            HttpStatusCode.BAD_REQUEST),

    DELIVERY_NOT_AVAILABLE(
            "FM-ORDER-602",
            "Delivery service is not available for this order.",
            HttpStatusCode.CONFLICT),

    DELIVERY_PARTNER_NOT_FOUND(
            "FM-ORDER-603",
            "Delivery partner not found.",
            HttpStatusCode.NOT_FOUND),

    DELIVERY_PARTNER_ALREADY_ASSIGNED(
            "FM-ORDER-604",
            "A delivery partner is already assigned to this order.",
            HttpStatusCode.CONFLICT),

    DELIVERY_PARTNER_NOT_ASSIGNED(
            "FM-ORDER-605",
            "No delivery partner is assigned to this order.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Payment
    // =========================================================================

    INVALID_PAYMENT_MODE(
            "FM-ORDER-700",
            "Invalid payment mode.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_PAYMENT_STATUS(
            "FM-ORDER-701",
            "Invalid payment status.",
            HttpStatusCode.BAD_REQUEST),

    PAYMENT_REQUIRED(
            "FM-ORDER-702",
            "Payment information is required.",
            HttpStatusCode.BAD_REQUEST),

    PAYMENT_FAILED(
            "FM-ORDER-703",
            "Order payment failed.",
            HttpStatusCode.CONFLICT),

    PAYMENT_ALREADY_COMPLETED(
            "FM-ORDER-704",
            "Payment has already been completed for this order.",
            HttpStatusCode.CONFLICT),

    REFUND_NOT_ALLOWED(
            "FM-ORDER-705",
            "Refund is not allowed for this order.",
            HttpStatusCode.CONFLICT),

    REFUND_ALREADY_COMPLETED(
            "FM-ORDER-706",
            "Refund has already been completed for this order.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Cancellation
    // =========================================================================

    INVALID_CANCELLATION_REASON(
            "FM-ORDER-800",
            "Invalid cancellation reason.",
            HttpStatusCode.BAD_REQUEST),

    CANCELLATION_REASON_REQUIRED(
            "FM-ORDER-801",
            "Cancellation reason is required.",
            HttpStatusCode.BAD_REQUEST),

    ORDER_ALREADY_CANCELLED(
            "FM-ORDER-802",
            "Order has already been cancelled.",
            HttpStatusCode.CONFLICT),

    ORDER_CANCELLATION_NOT_ALLOWED(
            "FM-ORDER-803",
            "Order cancellation is not allowed at this stage.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Archive / Delete
    // =========================================================================

    ORDER_CANNOT_BE_ARCHIVED(
            "FM-ORDER-900",
            "Order cannot be archived.",
            HttpStatusCode.CONFLICT),

    ORDER_CANNOT_BE_RESTORED(
            "FM-ORDER-901",
            "Order cannot be restored.",
            HttpStatusCode.CONFLICT),

    ORDER_CANNOT_BE_DELETED(
            "FM-ORDER-902",
            "Order cannot be permanently deleted.",
            HttpStatusCode.CONFLICT),

    ORDER_REFERENCED_BY_ACTIVE_PROCESS(
            "FM-ORDER-903",
            "Order is referenced by an active business process.",
            HttpStatusCode.CONFLICT);

    // =========================================================================
    // Fields
    // =========================================================================

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * User-friendly error message.
     */
    private final String errorMessage;

    /**
     * HTTP status associated with the error.
     */
    private final HttpStatusCode httpStatusCode;

    // =========================================================================
    // Constructor
    // =========================================================================

    OrderErrorConstants(
            final String errorCode,
            final String errorMessage,
            final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    // =========================================================================
    // IBusinessError
    // =========================================================================

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

}