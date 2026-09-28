package com.foodies.freshmeal.cart.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Cart Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Cart module.
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
 * <li>Services</li>
 * <li>Validators</li>
 * <li>Controllers</li>
 * <li>GlobalExceptionHandler</li>
 * </ul>
 *
 * ============================================================================
 *
 * Error Code Convention
 * ============================================================================
 *
 * FM-CART-001
 *
 * <p>
 * FM -> FreshMeal
 * CART -> Module
 * 001 -> Error Number
 * </p>
 *
 * ============================================================================
 *
 * Error Number Groups
 * ============================================================================
 *
 * 001 - 099 General Cart Errors
 * 100 - 199 Cart Lifecycle / Status
 * 200 - 299 Cart Item Errors
 * 300 - 399 Validation Errors
 * 400 - 499 Ownership / Restaurant / Branch
 * 500 - 599 Food / Availability / Price
 * 600 - 699 Checkout
 * 700 - 799 Concurrency / Persistence
 * 800 - 899 Bulk / Maintenance
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum CartErrorConstants implements IBusinessError {

    // =========================================================================
    // General Cart
    // =========================================================================

    CART_NOT_FOUND(
            "FM-CART-001",
            "Cart not found.",
            HttpStatusCode.NOT_FOUND),

    CART_ALREADY_EXISTS(
            "FM-CART-002",
            "Cart already exists.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_DELETED(
            "FM-CART-003",
            "Cart has already been permanently deleted.",
            HttpStatusCode.GONE),

    CART_NOT_AVAILABLE(
            "FM-CART-004",
            "Cart is not available.",
            HttpStatusCode.CONFLICT),

    CART_EMPTY(
            "FM-CART-005",
            "Cart is empty.",
            HttpStatusCode.BAD_REQUEST),

    CART_HAS_ITEMS(
            "FM-CART-006",
            "Cart contains one or more items.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_EMPTY(
            "FM-CART-007",
            "Cart is already empty.",
            HttpStatusCode.CONFLICT),

    CART_OPERATION_NOT_ALLOWED(
            "FM-CART-008",
            "This operation is not allowed for the current cart.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Cart Lifecycle / Status
    // =========================================================================

    INVALID_CART_STATUS(
            "FM-CART-100",
            "Invalid cart status.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_STATUS_TRANSITION(
            "FM-CART-101",
            "Invalid cart status transition.",
            HttpStatusCode.BAD_REQUEST),

    CART_ALREADY_ACTIVE(
            "FM-CART-102",
            "Cart is already active.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_IN_CHECKOUT(
            "FM-CART-103",
            "Checkout is already in progress for this cart.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_CONVERTED(
            "FM-CART-104",
            "Cart has already been converted into an order.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_ABANDONED(
            "FM-CART-105",
            "Cart has already been abandoned.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_EXPIRED(
            "FM-CART-106",
            "Cart has already expired.",
            HttpStatusCode.GONE),

    CART_CANNOT_BE_MODIFIED(
            "FM-CART-107",
            "Cart cannot be modified in its current status.",
            HttpStatusCode.CONFLICT),

    CART_CANNOT_START_CHECKOUT(
            "FM-CART-108",
            "Checkout cannot be started for this cart.",
            HttpStatusCode.CONFLICT),

    CART_CANNOT_BE_CONVERTED(
            "FM-CART-109",
            "Cart cannot be converted into an order.",
            HttpStatusCode.CONFLICT),

    CART_CANNOT_BE_ABANDONED(
            "FM-CART-110",
            "Cart cannot be abandoned in its current status.",
            HttpStatusCode.CONFLICT),

    CART_CANNOT_BE_REACTIVATED(
            "FM-CART-111",
            "Cart cannot be reactivated in its current status.",
            HttpStatusCode.CONFLICT),

    CART_CANNOT_BE_EXPIRED(
            "FM-CART-112",
            "Cart cannot be expired in its current status.",
            HttpStatusCode.CONFLICT),

    CART_CHECKOUT_TIMEOUT(
            "FM-CART-113",
            "Cart checkout session has expired.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Cart Item
    // =========================================================================

    CART_ITEM_NOT_FOUND(
            "FM-CART-200",
            "Cart item not found.",
            HttpStatusCode.NOT_FOUND),

    CART_ITEM_ALREADY_EXISTS(
            "FM-CART-201",
            "Food is already present in the cart.",
            HttpStatusCode.CONFLICT),

    CART_ITEM_INVALID(
            "FM-CART-202",
            "Cart item is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_QUANTITY_REQUIRED(
            "FM-CART-203",
            "Cart item quantity is required.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_QUANTITY_INVALID(
            "FM-CART-204",
            "Cart item quantity must be greater than zero.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_QUANTITY_EXCEEDED(
            "FM-CART-205",
            "Cart item quantity exceeds the maximum allowed quantity.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_QUANTITY_NOT_CHANGED(
            "FM-CART-206",
            "Cart item quantity has not changed.",
            HttpStatusCode.CONFLICT),

    CART_ITEM_CANNOT_BE_UPDATED(
            "FM-CART-207",
            "Cart item cannot be updated.",
            HttpStatusCode.CONFLICT),

    CART_ITEM_CANNOT_BE_REMOVED(
            "FM-CART-208",
            "Cart item cannot be removed.",
            HttpStatusCode.CONFLICT),

    CART_ITEM_FOOD_SNAPSHOT_REQUIRED(
            "FM-CART-209",
            "Food snapshot is required for the cart item.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_FOOD_NUMBER_REQUIRED(
            "FM-CART-210",
            "Food number is required for the cart item.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_PRICE_REQUIRED(
            "FM-CART-211",
            "Cart item price is required.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_PRICE_INVALID(
            "FM-CART-212",
            "Cart item price is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_TOTAL_INVALID(
            "FM-CART-213",
            "Cart item total is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CART_ITEM_DUPLICATE(
            "FM-CART-214",
            "The same food cannot be added as multiple cart items.",
            HttpStatusCode.CONFLICT),

    CART_ITEM_PRICE_UPDATED(
            "FM-CART-215",
            "Cart item price is updated.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Validation
    // =========================================================================

    CART_NUMBER_REQUIRED(
            "FM-CART-300",
            "Cart number is required.",
            HttpStatusCode.BAD_REQUEST),

    USER_NUMBER_REQUIRED(
            "FM-CART-301",
            "User number is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_NUMBER_REQUIRED(
            "FM-CART-302",
            "Restaurant number is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_BRANCH_NUMBER_REQUIRED(
            "FM-CART-303",
            "Restaurant branch number is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CART_REQUEST(
            "FM-CART-304",
            "Invalid cart request.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CART_ITEM_REQUEST(
            "FM-CART-305",
            "Invalid cart item request.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_QUANTITY(
            "FM-CART-306",
            "Invalid cart item quantity.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_MONEY(
            "FM-CART-307",
            "Invalid monetary value.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CURRENCY(
            "FM-CART-308",
            "Invalid currency.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_FOOD_SNAPSHOT(
            "FM-CART-309",
            "Invalid food snapshot.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Ownership / Restaurant / Branch
    // =========================================================================

    CART_ACCESS_DENIED(
            "FM-CART-400",
            "You are not authorized to access this cart.",
            HttpStatusCode.FORBIDDEN),

    CART_NOT_OWNED_BY_USER(
            "FM-CART-401",
            "Cart does not belong to the authenticated user.",
            HttpStatusCode.FORBIDDEN),

    CART_USER_MISMATCH(
            "FM-CART-402",
            "Cart ownership does not match the authenticated user.",
            HttpStatusCode.FORBIDDEN),

    RESTAURANT_MISMATCH(
            "FM-CART-403",
            "Food must belong to the same restaurant as the cart.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_BRANCH_MISMATCH(
            "FM-CART-404",
            "Food must belong to the same restaurant branch as the cart.",
            HttpStatusCode.CONFLICT),

    CART_RESTAURANT_CONTEXT_REQUIRED(
            "FM-CART-405",
            "Restaurant context is required for this cart operation.",
            HttpStatusCode.BAD_REQUEST),

    CART_RESTAURANT_CONTEXT_INVALID(
            "FM-CART-406",
            "Cart restaurant context is invalid.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_NOT_FOUND(
            "FM-CART-407",
            "Restaurant associated with the cart was not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_BRANCH_NOT_FOUND(
            "FM-CART-408",
            "Restaurant branch associated with the cart was not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_UNAVAILABLE(
            "FM-CART-409",
            "Restaurant is currently unavailable.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_BRANCH_UNAVAILABLE(
            "FM-CART-410",
            "Restaurant branch is currently unavailable.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Food / Availability / Price
    // =========================================================================

    FOOD_NOT_FOUND(
            "FM-CART-500",
            "Food added to the cart could not be found.",
            HttpStatusCode.NOT_FOUND),

    FOOD_UNAVAILABLE(
            "FM-CART-501",
            "Food is currently unavailable.",
            HttpStatusCode.CONFLICT),

    FOOD_STATUS_INVALID(
            "FM-CART-502",
            "Food is not in a valid status for cart operations.",
            HttpStatusCode.CONFLICT),

    FOOD_OUT_OF_STOCK(
            "FM-CART-503",
            "Food is currently out of stock.",
            HttpStatusCode.CONFLICT),

    FOOD_DISABLED(
            "FM-CART-504",
            "Food is currently disabled.",
            HttpStatusCode.CONFLICT),

    FOOD_DISCONTINUED(
            "FM-CART-505",
            "Food has been discontinued.",
            HttpStatusCode.CONFLICT),

    FOOD_NOT_AVAILABLE_FOR_ORDER(
            "FM-CART-506",
            "Food is no longer available for ordering.",
            HttpStatusCode.CONFLICT),

    FOOD_PRICE_CHANGED(
            "FM-CART-507",
            "The price of one or more foods in your cart has changed.",
            HttpStatusCode.CONFLICT),

    FOOD_PRICE_INCREASED(
            "FM-CART-508",
            "The price of one or more foods in your cart has increased.",
            HttpStatusCode.CONFLICT),

    FOOD_PRICE_DECREASED(
            "FM-CART-509",
            "The price of one or more foods in your cart has decreased.",
            HttpStatusCode.CONFLICT),

    FOOD_SNAPSHOT_OUTDATED(
            "FM-CART-510",
            "Food information in the cart is outdated.",
            HttpStatusCode.CONFLICT),

    FOOD_RESTAURANT_MISMATCH(
            "FM-CART-511",
            "Food does not belong to the restaurant associated with the cart.",
            HttpStatusCode.CONFLICT),

    FOOD_BRANCH_MISMATCH(
            "FM-CART-512",
            "Food does not belong to the branch associated with the cart.",
            HttpStatusCode.CONFLICT),

    FOOD_CANNOT_BE_ADDED_TO_CART(
            "FM-CART-513",
            "Food cannot be added to the cart.",
            HttpStatusCode.CONFLICT),

    FOOD_PRICE_REQUIRED(
            "FM-CART-514",
            "Food item price is required.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_PRICE_INVALID(
            "FM-CART-515",
            "Food item price is invalid.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Checkout
    // =========================================================================

    CHECKOUT_CART_REQUIRED(
            "FM-CART-600",
            "A cart is required to start checkout.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_CART_EMPTY(
            "FM-CART-601",
            "Cannot proceed to checkout with an empty cart.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_NOT_ALLOWED(
            "FM-CART-602",
            "Checkout is not allowed for this cart.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_ALREADY_STARTED(
            "FM-CART-603",
            "Checkout has already been started for this cart.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_NOT_IN_PROGRESS(
            "FM-CART-604",
            "Checkout is not currently in progress for this cart.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_VALIDATION_FAILED(
            "FM-CART-605",
            "Cart validation failed. Please review your cart before checkout.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_FOOD_UNAVAILABLE(
            "FM-CART-606",
            "One or more foods in the cart are no longer available.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_PRICE_VALIDATION_FAILED(
            "FM-CART-607",
            "One or more food prices have changed since they were added to the cart.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_RESTAURANT_VALIDATION_FAILED(
            "FM-CART-608",
            "Restaurant information could not be validated for checkout.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_BRANCH_VALIDATION_FAILED(
            "FM-CART-609",
            "Restaurant branch information could not be validated for checkout.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CART_EXPIRED(
            "FM-CART-610",
            "Cart has expired and cannot proceed to checkout.",
            HttpStatusCode.GONE),

    CHECKOUT_CART_CONVERTED(
            "FM-CART-611",
            "Cart has already been converted into an order.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_FAILED(
            "FM-CART-612",
            "Unable to complete checkout for this cart.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Concurrency / Persistence
    // =========================================================================

    CART_CONCURRENT_UPDATE(
            "FM-CART-700",
            "Cart was modified by another request. Please refresh and try again.",
            HttpStatusCode.CONFLICT),

    CART_VERSION_CONFLICT(
            "FM-CART-701",
            "Cart has been modified by another operation.",
            HttpStatusCode.CONFLICT),

    CART_SAVE_FAILED(
            "FM-CART-702",
            "Unable to save cart.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_UPDATE_FAILED(
            "FM-CART-703",
            "Unable to update cart.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_DELETE_FAILED(
            "FM-CART-704",
            "Unable to delete cart.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_LOAD_FAILED(
            "FM-CART-705",
            "Unable to load cart.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_SEQUENCE_GENERATION_FAILED(
            "FM-CART-706",
            "Unable to generate cart number.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_DATA_INCONSISTENT(
            "FM-CART-707",
            "Cart data is inconsistent and cannot be processed.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_TOTAL_CALCULATION_FAILED(
            "FM-CART-708",
            "Unable to calculate cart totals.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Bulk / Maintenance
    // =========================================================================

    BULK_CART_UPDATE_FAILED(
            "FM-CART-800",
            "Unable to update one or more carts.",
            HttpStatusCode.CONFLICT),

    BULK_CART_EXPIRATION_FAILED(
            "FM-CART-801",
            "Unable to expire one or more carts.",
            HttpStatusCode.CONFLICT),

    BULK_CART_ABANDONMENT_FAILED(
            "FM-CART-802",
            "Unable to abandon one or more carts.",
            HttpStatusCode.CONFLICT),

    CART_CLEANUP_FAILED(
            "FM-CART-803",
            "Unable to complete cart cleanup.",
            HttpStatusCode.INTERNAL_SERVER_ERROR);

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * User-friendly error message.
     */
    private final String errorMessage;

    /**
     * HTTP status code associated with the business error.
     */
    private final HttpStatusCode httpStatusCode;

    CartErrorConstants(
            final String errorCode,
            final String errorMessage,
            final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

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