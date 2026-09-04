package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : ApiMessageConstants
 * ============================================================================
 *
 * Centralized API response messages used throughout the FreshMeal application.
 *
 * <p>
 * This class eliminates hardcoded messages and ensures consistency across
 * controllers, services, exception handlers, audit logs and API responses.
 * </p>
 *
 * <p>
 * New module-specific messages should always be added under their respective
 * section.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class ApiMessageConstants {

    /**
     * Private constructor.
     */
    private ApiMessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Common Success Messages
    // =========================================================

    public static final String CREATED_SUCCESSFULLY = "Created successfully";

    public static final String UPDATED_SUCCESSFULLY = "Updated successfully";

    public static final String DELETED_SUCCESSFULLY = "Deleted successfully";

    public static final String FETCHED_SUCCESSFULLY = "Fetched successfully";

    public static final String SAVED_SUCCESSFULLY = "Saved successfully";

    public static final String OPERATION_SUCCESSFUL = "Operation completed successfully";

    // =========================================================
    // Common Failure Messages
    // =========================================================

    public static final String OPERATION_FAILED = "Operation failed";

    public static final String INVALID_REQUEST = "Invalid request";

    public static final String RESOURCE_NOT_FOUND = "Requested resource not found";

    public static final String DUPLICATE_RECORD = "Record already exists";

    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    public static final String VALIDATION_FAILED = "Validation failed";

    public static final String ACCESS_DENIED = "Access denied";

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";

    // =========================================================
    // Authentication
    // =========================================================

    public static final String LOGIN_SUCCESS = "Login successful";

    public static final String LOGIN_FAILED = "Invalid username or password";

    public static final String LOGOUT_SUCCESS = "Logout successful";

    public static final String PASSWORD_CHANGED = "Password changed successfully";

    public static final String PASSWORD_RESET = "Password reset successfully";

    public static final String INVALID_CREDENTIALS = "Invalid credentials";

    public static final String ACCOUNT_LOCKED = "Account is locked";

    // =========================================================
    // User
    // =========================================================

    public static final String USER_CREATED = "User created successfully";

    public static final String USER_UPDATED = "User updated successfully";

    public static final String USER_DELETED = "User deleted successfully";

    public static final String USER_FOUND = "User fetched successfully";

    public static final String USER_NOT_FOUND = "User not found";

    public static final String USER_ALREADY_EXISTS = "User already exists";

    public static final String USER_ENABLED = "User enabled successfully";

    public static final String USER_DISABLED = "User disabled successfully";

    public static final String USER_LOCKED = "User locked successfully";

    public static final String USER_UNLOCKED = "User unlocked successfully";

    // =========================================================
    // Food
    // =========================================================

    public static final String FOOD_CREATED = "Food created successfully";

    public static final String FOOD_UPDATED = "Food updated successfully";

    public static final String FOOD_DELETED = "Food deleted successfully";

    public static final String FOOD_FOUND = "Food fetched successfully";

    public static final String FOOD_LIST_FOUND = "Foods fetched successfully";

    public static final String FOOD_NOT_FOUND = "Food not found";

    public static final String FOOD_ALREADY_EXISTS = "Food already exists";

    public static final String FOOD_STATUS_UPDATED = "Food status updated successfully";

    // =========================================================
    // Order
    // =========================================================

    public static final String ORDER_CREATED = "Order created successfully";

    public static final String ORDER_UPDATED = "Order updated successfully";

    public static final String ORDER_CANCELLED = "Order cancelled successfully";

    public static final String ORDER_DELETED = "Order deleted successfully";

    public static final String ORDER_FOUND = "Order fetched successfully";

    public static final String ORDER_LIST_FOUND = "Orders fetched successfully";

    public static final String ORDER_NOT_FOUND = "Order not found";

    public static final String ORDER_STATUS_UPDATED = "Order status updated successfully";

    public static final String PAYMENT_STATUS_UPDATED = "Payment status updated successfully";

    public static final String DELIVERY_PARTNER_ASSIGNED = "Delivery partner assigned successfully";

    // =========================================================
    // Restaurant
    // =========================================================

    public static final String RESTAURANT_CREATED = "Restaurant created successfully";

    public static final String RESTAURANT_UPDATED = "Restaurant updated successfully";

    public static final String RESTAURANT_DELETED = "Restaurant deleted successfully";

    public static final String RESTAURANT_FOUND = "Restaurant fetched successfully";

    public static final String RESTAURANT_LIST_FOUND = "Restaurants fetched successfully";

    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found";

    // =========================================================
    // Payment
    // =========================================================

    public static final String PAYMENT_SUCCESSFUL = "Payment completed successfully";

    public static final String PAYMENT_FAILED = "Payment failed";

    public static final String PAYMENT_INITIATED = "Payment initiated successfully";

    public static final String REFUND_INITIATED = "Refund initiated successfully";

    public static final String REFUND_COMPLETED = "Refund completed successfully";

    // =========================================================
    // Image
    // =========================================================

    public static final String IMAGE_UPLOADED = "Image uploaded successfully";

    public static final String IMAGE_UPDATED = "Image updated successfully";

    public static final String IMAGE_DELETED = "Image deleted successfully";

    public static final String IMAGE_NOT_FOUND = "Image not found";

    // =========================================================
    // Coupon
    // =========================================================

    public static final String COUPON_APPLIED = "Coupon applied successfully";

    public static final String COUPON_REMOVED = "Coupon removed successfully";

    public static final String INVALID_COUPON = "Invalid coupon";

    // =========================================================
    // Delivery Partner
    // =========================================================

    public static final String DELIVERY_PARTNER_CREATED = "Delivery partner created successfully";

    public static final String DELIVERY_PARTNER_UPDATED = "Delivery partner updated successfully";

    public static final String DELIVERY_PARTNER_NOT_FOUND = "Delivery partner not found";

    // =========================================================
    // Metadata
    // =========================================================

    public static final String METADATA_FETCHED = "Metadata fetched successfully";

    public static final String PINCODE_DETAILS_FETCHED = "Pincode details fetched successfully";

    // =========================================================================
    // Address
    // =========================================================================

    public static final String ADDRESS_CREATED = "Address created successfully.";

    public static final String ADDRESS_FETCHED = "Address details fetched successfully.";

}
