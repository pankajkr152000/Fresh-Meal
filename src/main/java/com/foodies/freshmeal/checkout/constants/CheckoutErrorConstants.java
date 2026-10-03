
package com.foodies.freshmeal.checkout.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Checkout Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Checkout module.
 *
 * <p>
 * Each error contains:
 * </p>
 *
 * <ul>
 * <li>Error Code</li>
 * <li>User-Friendly Error Message</li>
 * <li>HTTP Status Code</li>
 * </ul>
 *
 * <p>
 * These errors are used by:
 * </p>
 *
 * <ul>
 * <li>Checkout Services</li>
 * <li>Checkout Validators</li>
 * <li>Checkout Controllers</li>
 * <li>Checkout Repositories and Persistence Adapters</li>
 * <li>Checkout Lifecycle Operations</li>
 * <li>GlobalExceptionHandler</li>
 * </ul>
 *
 * <p>
 * Technical exceptions, stack traces, database details, and sensitive
 * information must never be exposed through user-facing error messages.
 * </p>
 *
 * ============================================================================
 * Error Code Convention
 * ============================================================================
 *
 * FM-CHECKOUT-001
 *
 * <p>
 * FM -> FreshMeal
 * CHECKOUT -> Module
 * 001 -> Error Number
 * </p>
 *
 * ============================================================================
 * Error Number Groups
 * ============================================================================
 *
 * 001 - 099 General Checkout Errors
 * 100 - 199 Checkout Lifecycle / Status
 * 200 - 299 Request / Input Validation
 * 300 - 399 Cart / Ownership Validation
 * 400 - 499 Address / Restaurant / Food Validation
 * 500 - 599 Pricing / Checkout Review
 * 600 - 699 Confirmation / Order Creation / Reconciliation
 * 700 - 799 Concurrency / Persistence
 * 800 - 899 Expiration / Maintenance
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum CheckoutErrorConstants implements IBusinessError {

    // =========================================================================
    // General Checkout Errors
    // =========================================================================

    CHECKOUT_NOT_FOUND(
            "FM-CHECKOUT-001",
            "Checkout session not found.",
            HttpStatusCode.NOT_FOUND),

    CHECKOUT_ALREADY_EXISTS(
            "FM-CHECKOUT-002",
            "Checkout session already exists.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_NOT_AVAILABLE(
            "FM-CHECKOUT-003",
            "Checkout session is not available.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_ALREADY_DELETED(
            "FM-CHECKOUT-004",
            "Checkout session has already been deleted.",
            HttpStatusCode.GONE),

    CHECKOUT_OPERATION_NOT_ALLOWED(
            "FM-CHECKOUT-005",
            "This operation is not allowed for the current checkout session.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_NOT_INITIALIZED(
            "FM-CHECKOUT-006",
            "Checkout session has not been initialized.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_INITIALIZATION_FAILED(
            "FM-CHECKOUT-007",
            "Unable to initialize checkout session.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_CONTEXT_REQUIRED(
            "FM-CHECKOUT-008",
            "Checkout context is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_DATA_INCONSISTENT(
            "FM-CHECKOUT-009",
            "Checkout data is inconsistent and cannot be processed.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_PROCESSING_FAILED(
            "FM-CHECKOUT-010",
            "Unable to process checkout request.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_REQUEST_DUPLICATE(
            "FM-CHECKOUT-011",
            "This checkout request has already been processed.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_REQUEST_IDEMPOTENCY_CONFLICT(
            "FM-CHECKOUT-012",
            "The request identifier has already been used with different data.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CONFIGURATION_INVALID(
            "FM-CHECKOUT-013",
            "Checkout configuration is invalid.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_DEPENDENCY_UNAVAILABLE(
            "FM-CHECKOUT-014",
            "A required service is temporarily unavailable. Please try again.",
            HttpStatusCode.SERVICE_UNAVAILABLE),

    CHECKOUT_UNEXPECTED_ERROR(
            "FM-CHECKOUT-015",
            "An unexpected error occurred while processing checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Checkout Lifecycle / Status
    // =========================================================================

    INVALID_CHECKOUT_STATUS(
            "FM-CHECKOUT-100",
            "Invalid checkout status.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CHECKOUT_STATUS_TRANSITION(
            "FM-CHECKOUT-101",
            "Invalid checkout status transition.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_EXPIRED(
            "FM-CHECKOUT-102",
            "Checkout session has expired. Please start checkout again.",
            HttpStatusCode.GONE),

    CHECKOUT_ALREADY_CONFIRMED(
            "FM-CHECKOUT-103",
            "Checkout has already been confirmed.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CANNOT_BE_MODIFIED(
            "FM-CHECKOUT-104",
            "Checkout cannot be modified in its current status.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CANNOT_BE_CANCELLED(
            "FM-CHECKOUT-105",
            "Checkout cannot be cancelled in its current status.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CANNOT_BE_EXPIRED(
            "FM-CHECKOUT-106",
            "Checkout cannot be expired in its current status.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CANNOT_BE_REVALIDATED(
            "FM-CHECKOUT-107",
            "Checkout cannot be revalidated in its current status.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_VALIDATION_ALREADY_IN_PROGRESS(
            "FM-CHECKOUT-108",
            "Checkout validation is already in progress.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CONFIRMATION_ALREADY_IN_PROGRESS(
            "FM-CHECKOUT-109",
            "Checkout confirmation is already in progress.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_NOT_READY_FOR_CONFIRMATION(
            "FM-CHECKOUT-110",
            "Checkout is not ready for confirmation.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_NOT_IN_PROGRESS(
            "FM-CHECKOUT-111",
            "Checkout is not currently in progress.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_ALREADY_CANCELLED(
            "FM-CHECKOUT-112",
            "Checkout has already been cancelled.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_ALREADY_EXPIRED(
            "FM-CHECKOUT-113",
            "Checkout session has already expired.",
            HttpStatusCode.GONE),

    CHECKOUT_ALREADY_COMPLETED(
            "FM-CHECKOUT-114",
            "Checkout has already been completed.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_FAILED(
            "FM-CHECKOUT-115",
            "Checkout could not be completed.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CANNOT_BE_RETRIED(
            "FM-CHECKOUT-116",
            "Checkout cannot be retried in its current state.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_REVALIDATION_REQUIRED(
            "FM-CHECKOUT-117",
            "Checkout details have changed. Please review them again.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_STATUS_HISTORY_INVALID(
            "FM-CHECKOUT-118",
            "Checkout status history is invalid.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_STATUS_UPDATE_FAILED(
            "FM-CHECKOUT-119",
            "Unable to update checkout status.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Request / Input Validation
    // =========================================================================

    CHECKOUT_NUMBER_REQUIRED(
            "FM-CHECKOUT-200",
            "Checkout number is required.",
            HttpStatusCode.BAD_REQUEST),

    CART_NUMBER_REQUIRED(
            "FM-CHECKOUT-201",
            "Cart number is required.",
            HttpStatusCode.BAD_REQUEST),

    USER_NUMBER_REQUIRED(
            "FM-CHECKOUT-202",
            "User number is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_NUMBER_REQUIRED(
            "FM-CHECKOUT-203",
            "Restaurant number is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_BRANCH_NUMBER_REQUIRED(
            "FM-CHECKOUT-204",
            "Restaurant branch number is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CHECKOUT_REQUEST(
            "FM-CHECKOUT-205",
            "Invalid checkout request.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CHECKOUT_NUMBER(
            "FM-CHECKOUT-206",
            "Checkout number is invalid.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CART_NUMBER(
            "FM-CHECKOUT-207",
            "Cart number is invalid.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_USER_NUMBER(
            "FM-CHECKOUT-208",
            "User number is invalid.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_RESTAURANT_NUMBER(
            "FM-CHECKOUT-209",
            "Restaurant number is invalid.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_RESTAURANT_BRANCH_NUMBER(
            "FM-CHECKOUT-210",
            "Restaurant branch number is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEMS_REQUIRED(
            "FM-CHECKOUT-211",
            "At least one checkout item is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEM_INVALID(
            "FM-CHECKOUT-212",
            "One or more checkout items are invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEM_QUANTITY_REQUIRED(
            "FM-CHECKOUT-213",
            "Checkout item quantity is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEM_QUANTITY_INVALID(
            "FM-CHECKOUT-214",
            "Checkout item quantity must be greater than zero.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEM_QUANTITY_EXCEEDED(
            "FM-CHECKOUT-215",
            "Checkout item quantity exceeds the maximum allowed quantity.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_EXPIRY_REQUIRED(
            "FM-CHECKOUT-216",
            "Checkout expiry time is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_EXPIRY_INVALID(
            "FM-CHECKOUT-217",
            "Checkout expiry time must be in the future.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_EXPIRY_DURATION_INVALID(
            "FM-CHECKOUT-218",
            "Checkout expiry duration is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_REMARKS_TOO_LONG(
            "FM-CHECKOUT-219",
            "Checkout remarks exceed the maximum allowed length.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_FAILURE_DETAILS_INVALID(
            "FM-CHECKOUT-220",
            "Checkout failure details are invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_IDEMPOTENCY_KEY_REQUIRED(
            "FM-CHECKOUT-221",
            "Idempotency key is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_IDEMPOTENCY_KEY_INVALID(
            "FM-CHECKOUT-222",
            "Idempotency key is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_VERSION_REQUIRED(
            "FM-CHECKOUT-223",
            "Checkout version is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_VERSION_INVALID(
            "FM-CHECKOUT-224",
            "Checkout version is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_REQUEST_PAYLOAD_INVALID(
            "FM-CHECKOUT-225",
            "Checkout request payload is invalid.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Cart / Ownership Validation
    // =========================================================================

    CART_NOT_FOUND(
            "FM-CHECKOUT-300",
            "Cart not found.",
            HttpStatusCode.NOT_FOUND),

    CART_NOT_OWNED_BY_USER(
            "FM-CHECKOUT-301",
            "Cart does not belong to the authenticated user.",
            HttpStatusCode.FORBIDDEN),

    CART_EMPTY(
            "FM-CHECKOUT-302",
            "Cannot proceed to checkout with an empty cart.",
            HttpStatusCode.BAD_REQUEST),

    CART_NOT_ACTIVE(
            "FM-CHECKOUT-303",
            "Cart is not active and cannot be checked out.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_IN_CHECKOUT(
            "FM-CHECKOUT-304",
            "Cart already has a checkout session in progress.",
            HttpStatusCode.CONFLICT),

    CART_ALREADY_CONVERTED(
            "FM-CHECKOUT-305",
            "Cart has already been converted into an order.",
            HttpStatusCode.CONFLICT),

    CART_EXPIRED(
            "FM-CHECKOUT-306",
            "Cart has expired and cannot be checked out.",
            HttpStatusCode.GONE),

    CART_UNAVAILABLE(
            "FM-CHECKOUT-307",
            "Cart is currently unavailable.",
            HttpStatusCode.CONFLICT),

    CART_USER_MISMATCH(
            "FM-CHECKOUT-308",
            "Cart ownership does not match the authenticated user.",
            HttpStatusCode.FORBIDDEN),

    CART_RESTAURANT_MISMATCH(
            "FM-CHECKOUT-309",
            "Cart restaurant does not match the checkout restaurant.",
            HttpStatusCode.CONFLICT),

    CART_BRANCH_MISMATCH(
            "FM-CHECKOUT-310",
            "Cart branch does not match the checkout branch.",
            HttpStatusCode.CONFLICT),

    CART_ITEMS_CHANGED(
            "FM-CHECKOUT-311",
            "Cart items have changed. Please review checkout again.",
            HttpStatusCode.CONFLICT),

    CART_MODIFIED_DURING_CHECKOUT(
            "FM-CHECKOUT-312",
            "Cart was modified during checkout. Please refresh and try again.",
            HttpStatusCode.CONFLICT),

    CART_VALIDATION_FAILED(
            "FM-CHECKOUT-313",
            "Cart validation failed.",
            HttpStatusCode.CONFLICT),

    CART_LOCK_FAILED(
            "FM-CHECKOUT-314",
            "Unable to secure the cart for checkout.",
            HttpStatusCode.CONFLICT),

    CART_RELEASE_FAILED(
            "FM-CHECKOUT-315",
            "Unable to release the cart from checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CART_CHECKOUT_CONTEXT_INVALID(
            "FM-CHECKOUT-316",
            "Cart checkout context is invalid.",
            HttpStatusCode.CONFLICT),

    CART_TOTAL_MISMATCH(
            "FM-CHECKOUT-317",
            "Cart total does not match the validated checkout total.",
            HttpStatusCode.CONFLICT),

    CART_ITEM_SNAPSHOT_INVALID(
            "FM-CHECKOUT-318",
            "One or more cart item snapshots are invalid.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_ACCESS_DENIED(
            "FM-CHECKOUT-319",
            "Access to checkout is denied.",
            HttpStatusCode.FORBIDDEN),

    // =========================================================================
    // Address / Restaurant / Food Validation
    // =========================================================================

    ADDRESS_NOT_FOUND(
            "FM-CHECKOUT-400",
            "Delivery address not found.",
            HttpStatusCode.NOT_FOUND),

    ADDRESS_INVALID(
            "FM-CHECKOUT-401",
            "Delivery address is invalid.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_REQUIRED(
            "FM-CHECKOUT-402",
            "A delivery address is required.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_NOT_OWNED_BY_USER(
            "FM-CHECKOUT-403",
            "Delivery address does not belong to the authenticated user.",
            HttpStatusCode.FORBIDDEN),

    ADDRESS_UNAVAILABLE(
            "FM-CHECKOUT-404",
            "Selected delivery address is unavailable.",
            HttpStatusCode.CONFLICT),

    ADDRESS_INCOMPLETE(
            "FM-CHECKOUT-405",
            "Delivery address is incomplete.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_CHANGED(
            "FM-CHECKOUT-406",
            "Delivery address has changed. Please review it again.",
            HttpStatusCode.CONFLICT),

    ADDRESS_OUTSIDE_DELIVERY_AREA(
            "FM-CHECKOUT-407",
            "Delivery is not available for the selected address.",
            HttpStatusCode.CONFLICT),

    ADDRESS_PINCODE_INVALID(
            "FM-CHECKOUT-408",
            "Delivery pincode is invalid.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_SNAPSHOT_REQUIRED(
            "FM-CHECKOUT-409",
            "Delivery address snapshot is required.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_SNAPSHOT_INVALID(
            "FM-CHECKOUT-410",
            "Delivery address snapshot is invalid.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_NOT_FOUND(
            "FM-CHECKOUT-411",
            "Restaurant not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_UNAVAILABLE(
            "FM-CHECKOUT-412",
            "Restaurant is currently unavailable.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_CLOSED(
            "FM-CHECKOUT-413",
            "Restaurant is currently closed.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_BRANCH_NOT_FOUND(
            "FM-CHECKOUT-414",
            "Restaurant branch not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_BRANCH_UNAVAILABLE(
            "FM-CHECKOUT-415",
            "Restaurant branch is currently unavailable.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_BRANCH_CLOSED(
            "FM-CHECKOUT-416",
            "Restaurant branch is currently closed.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_DELIVERY_UNAVAILABLE(
            "FM-CHECKOUT-417",
            "Restaurant does not deliver to the selected address.",
            HttpStatusCode.CONFLICT),

    FOOD_NOT_FOUND(
            "FM-CHECKOUT-418",
            "One or more foods in the cart could not be found.",
            HttpStatusCode.NOT_FOUND),

    FOOD_UNAVAILABLE(
            "FM-CHECKOUT-419",
            "One or more foods in the cart are unavailable.",
            HttpStatusCode.CONFLICT),

    FOOD_OUT_OF_STOCK(
            "FM-CHECKOUT-420",
            "One or more foods in the cart are out of stock.",
            HttpStatusCode.CONFLICT),

    FOOD_DISABLED(
            "FM-CHECKOUT-421",
            "One or more foods in the cart are disabled.",
            HttpStatusCode.CONFLICT),

    FOOD_DISCONTINUED(
            "FM-CHECKOUT-422",
            "One or more foods in the cart have been discontinued.",
            HttpStatusCode.CONFLICT),

    FOOD_STATUS_INVALID(
            "FM-CHECKOUT-423",
            "One or more foods have an invalid status for ordering.",
            HttpStatusCode.CONFLICT),

    FOOD_RESTAURANT_MISMATCH(
            "FM-CHECKOUT-424",
            "Food does not belong to the selected restaurant.",
            HttpStatusCode.CONFLICT),

    FOOD_BRANCH_MISMATCH(
            "FM-CHECKOUT-425",
            "Food does not belong to the selected restaurant branch.",
            HttpStatusCode.CONFLICT),

    FOOD_QUANTITY_UNAVAILABLE(
            "FM-CHECKOUT-426",
            "Requested quantity is no longer available for one or more foods.",
            HttpStatusCode.CONFLICT),

    FOOD_SNAPSHOT_INVALID(
            "FM-CHECKOUT-427",
            "One or more food snapshots are invalid.",
            HttpStatusCode.CONFLICT),

    FOOD_VALIDATION_FAILED(
            "FM-CHECKOUT-428",
            "Food validation failed. Please review your cart.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Pricing / Checkout Review
    // =========================================================================

    CHECKOUT_PRICING_REQUIRED(
            "FM-CHECKOUT-500",
            "Checkout pricing information is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_PRICING_INVALID(
            "FM-CHECKOUT-501",
            "Checkout pricing information is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_PRICE_CHANGED(
            "FM-CHECKOUT-502",
            "Checkout price has changed. Please review the updated total.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_PRICE_INCREASED(
            "FM-CHECKOUT-503",
            "Checkout total has increased. Please review the updated amount.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_PRICE_DECREASED(
            "FM-CHECKOUT-504",
            "Checkout total has decreased. Please review the updated amount.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_SUBTOTAL_INVALID(
            "FM-CHECKOUT-505",
            "Checkout item subtotal is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_DISCOUNT_INVALID(
            "FM-CHECKOUT-506",
            "Checkout discount amount is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_TAX_INVALID(
            "FM-CHECKOUT-507",
            "Checkout tax amount is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_DELIVERY_FEE_INVALID(
            "FM-CHECKOUT-508",
            "Checkout delivery fee is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_PACKAGING_CHARGE_INVALID(
            "FM-CHECKOUT-509",
            "Checkout packaging charge is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_PLATFORM_FEE_INVALID(
            "FM-CHECKOUT-510",
            "Checkout platform fee is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_RAIN_CHARGE_INVALID(
            "FM-CHECKOUT-511",
            "Checkout rain charge is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_TOTAL_PAYABLE_INVALID(
            "FM-CHECKOUT-512",
            "Checkout total payable amount is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_TOTAL_MISMATCH(
            "FM-CHECKOUT-513",
            "Checkout total does not match the calculated amount.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CURRENCY_INVALID(
            "FM-CHECKOUT-514",
            "Checkout currency is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_MONEY_PRECISION_INVALID(
            "FM-CHECKOUT-515",
            "Checkout monetary precision is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_PRICE_CALCULATION_FAILED(
            "FM-CHECKOUT-516",
            "Unable to calculate checkout pricing.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_PRICING_CONFIGURATION_INVALID(
            "FM-CHECKOUT-517",
            "Checkout pricing configuration is invalid.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_PRICING_SERVICE_UNAVAILABLE(
            "FM-CHECKOUT-518",
            "Checkout pricing service is temporarily unavailable.",
            HttpStatusCode.SERVICE_UNAVAILABLE),

    CHECKOUT_REVIEW_REQUIRED(
            "FM-CHECKOUT-519",
            "Please review checkout details before confirming your order.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_REVIEW_OUTDATED(
            "FM-CHECKOUT-520",
            "Checkout review is outdated. Please validate checkout again.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_SNAPSHOT_REQUIRED(
            "FM-CHECKOUT-521",
            "Checkout snapshot is required.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_SNAPSHOT_INVALID(
            "FM-CHECKOUT-522",
            "Checkout snapshot is invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEM_TOTAL_INVALID(
            "FM-CHECKOUT-523",
            "One or more checkout item totals are invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_ITEM_PRICE_INVALID(
            "FM-CHECKOUT-524",
            "One or more checkout item prices are invalid.",
            HttpStatusCode.BAD_REQUEST),

    CHECKOUT_PRICING_SNAPSHOT_MISMATCH(
            "FM-CHECKOUT-525",
            "Checkout pricing snapshot does not match the validated pricing.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Confirmation / Order Creation / Reconciliation
    // =========================================================================

    CHECKOUT_CONFIRMATION_FAILED(
            "FM-CHECKOUT-600",
            "Unable to confirm checkout.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CONFIRMATION_REJECTED(
            "FM-CHECKOUT-601",
            "Checkout confirmation was rejected.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CONFIRMATION_OUTCOME_UNKNOWN(
            "FM-CHECKOUT-602",
            "Checkout confirmation is being verified. Please check your order status before retrying.",
            HttpStatusCode.SERVICE_UNAVAILABLE),

    ORDER_CREATION_FAILED(
            "FM-CHECKOUT-603",
            "Unable to create an order from checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    ORDER_CREATION_OUTCOME_UNKNOWN(
            "FM-CHECKOUT-604",
            "Order creation outcome is being verified. Please check your order status before retrying.",
            HttpStatusCode.SERVICE_UNAVAILABLE),

    ORDER_NOT_FOUND_AFTER_CONFIRMATION(
            "FM-CHECKOUT-605",
            "The order could not be found after checkout confirmation.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    ORDER_ALREADY_CREATED(
            "FM-CHECKOUT-606",
            "An order has already been created for this checkout.",
            HttpStatusCode.CONFLICT),

    ORDER_CHECKOUT_MISMATCH(
            "FM-CHECKOUT-607",
            "The created order does not match the originating checkout.",
            HttpStatusCode.CONFLICT),

    ORDER_LINK_REQUIRED(
            "FM-CHECKOUT-608",
            "Order reference is required to complete checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    ORDER_LINK_INVALID(
            "FM-CHECKOUT-609",
            "Order reference is invalid.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    ORDER_RECONCILIATION_REQUIRED(
            "FM-CHECKOUT-610",
            "Checkout requires reconciliation before another attempt can be made.",
            HttpStatusCode.CONFLICT),

    ORDER_RECONCILIATION_FAILED(
            "FM-CHECKOUT-611",
            "Unable to reconcile checkout with order creation.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    ORDER_RECONCILIATION_IN_PROGRESS(
            "FM-CHECKOUT-612",
            "Checkout order reconciliation is already in progress.",
            HttpStatusCode.CONFLICT),

    ORDER_DUPLICATE_CREATION_DETECTED(
            "FM-CHECKOUT-613",
            "Duplicate order creation was detected for this checkout.",
            HttpStatusCode.CONFLICT),

    ORDER_CREATION_COMPENSATION_FAILED(
            "FM-CHECKOUT-614",
            "Unable to complete checkout recovery.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_FINALIZATION_FAILED(
            "FM-CHECKOUT-615",
            "Order was created, but checkout finalization could not be completed.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_FINALIZATION_OUTCOME_UNKNOWN(
            "FM-CHECKOUT-616",
            "Checkout finalization is being verified. Please check your order status.",
            HttpStatusCode.SERVICE_UNAVAILABLE),

    CHECKOUT_ALREADY_LINKED_TO_ORDER(
            "FM-CHECKOUT-617",
            "Checkout is already linked to an order.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_ORDER_REFERENCE_CONFLICT(
            "FM-CHECKOUT-618",
            "Checkout is linked to a different order.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_CONFIRMATION_REQUIRES_REVALIDATION(
            "FM-CHECKOUT-619",
            "Checkout must be revalidated before confirmation.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Concurrency / Persistence
    // =========================================================================

    CHECKOUT_CONCURRENT_UPDATE(
            "FM-CHECKOUT-700",
            "Checkout was modified by another request. Please refresh and try again.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_VERSION_CONFLICT(
            "FM-CHECKOUT-701",
            "Checkout has been modified by another operation.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_SAVE_FAILED(
            "FM-CHECKOUT-702",
            "Unable to save checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_UPDATE_FAILED(
            "FM-CHECKOUT-703",
            "Unable to update checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_DELETE_FAILED(
            "FM-CHECKOUT-704",
            "Unable to delete checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_LOAD_FAILED(
            "FM-CHECKOUT-705",
            "Unable to load checkout.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_SEQUENCE_GENERATION_FAILED(
            "FM-CHECKOUT-706",
            "Unable to generate checkout number.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_DUPLICATE_KEY(
            "FM-CHECKOUT-707",
            "A checkout record with the same identifier already exists.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_PERSISTENCE_FAILED(
            "FM-CHECKOUT-708",
            "Checkout persistence operation failed.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_TRANSACTION_FAILED(
            "FM-CHECKOUT-709",
            "Unable to complete the checkout transaction.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_LOCK_FAILED(
            "FM-CHECKOUT-710",
            "Unable to acquire checkout processing lock.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_LOCK_RELEASE_FAILED(
            "FM-CHECKOUT-711",
            "Unable to release checkout processing lock.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_IDEMPOTENCY_RECORD_FAILED(
            "FM-CHECKOUT-712",
            "Unable to persist checkout request tracking information.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_AUDIT_FAILED(
            "FM-CHECKOUT-713",
            "Unable to record checkout audit information.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_REPOSITORY_UNAVAILABLE(
            "FM-CHECKOUT-714",
            "Checkout storage is temporarily unavailable.",
            HttpStatusCode.SERVICE_UNAVAILABLE),

    CHECKOUT_WRITE_CONFLICT(
            "FM-CHECKOUT-715",
            "Checkout could not be updated because of a conflicting operation.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_READ_FAILED(
            "FM-CHECKOUT-716",
            "Unable to retrieve checkout information.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Expiration / Maintenance
    // =========================================================================

    CHECKOUT_EXPIRATION_FAILED(
            "FM-CHECKOUT-800",
            "Unable to expire checkout session.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_EXPIRATION_ALREADY_PROCESSED(
            "FM-CHECKOUT-801",
            "Checkout expiration has already been processed.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_EXPIRATION_NOT_DUE(
            "FM-CHECKOUT-802",
            "Checkout session is not yet eligible for expiration.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_EXPIRATION_BATCH_FAILED(
            "FM-CHECKOUT-803",
            "Unable to process checkout expiration batch.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_CLEANUP_FAILED(
            "FM-CHECKOUT-804",
            "Unable to complete checkout cleanup.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_MAINTENANCE_FAILED(
            "FM-CHECKOUT-805",
            "Unable to complete checkout maintenance.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_RECONCILIATION_BATCH_FAILED(
            "FM-CHECKOUT-806",
            "Unable to process checkout reconciliation batch.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    CHECKOUT_STALE_SESSION_DETECTED(
            "FM-CHECKOUT-807",
            "A stale checkout session was detected and requires recovery.",
            HttpStatusCode.CONFLICT),

    CHECKOUT_EXPIRATION_RECOVERY_FAILED(
            "FM-CHECKOUT-808",
            "Unable to recover an expired checkout session.",
            HttpStatusCode.INTERNAL_SERVER_ERROR);

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
     * HTTP status code associated with the business error.
     */
    private final HttpStatusCode httpStatusCode;

    // =========================================================================
    // Constructor
    // =========================================================================

    CheckoutErrorConstants(
            final String errorCode,
            final String errorMessage,
            final HttpStatusCode httpStatusCode) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    // =========================================================================
    // IBusinessError Implementation
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