package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : ErrorCodeConstants
 * ============================================================================
 *
 * Centralized application error codes used throughout the FreshMeal
 * application.
 *
 * <p>
 * Error Code Format:
 * </p>
 *
 * <pre>
 * FM-MODULE-XXXX
 *
 * Example:
 * FM-FOOD-001
 * FM-ORD-002
 * FM-AUTH-003
 * </pre>
 * 
 * ==============================================================
 * uses :
 * throw new BusinessException(
 * ErrorCodeConstants.FM_ORD_004,
 * ApiMessageConstants.ORDER_ALREADY_DELIVERED);
 * ==============================================================
 * 
 * 
 * <p>
 * Error codes should remain stable once released.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class ErrorCodeConstants {

    /**
     * Private constructor.
     */
    private ErrorCodeConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Common Errors
    // =========================================================

    public static final String FM_COM_001 = "Invalid Request";

    public static final String FM_COM_002 = "Validation Failed";

    public static final String FM_COM_003 = "Resource Not Found";

    public static final String FM_COM_004 = "Duplicate Record";

    public static final String FM_COM_005 = "Internal Server Error";

    // =========================================================
    // Authentication Errors
    // =========================================================

    public static final String FM_AUTH_001 = "Invalid Credentials";

    public static final String FM_AUTH_002 = "Unauthorized";

    public static final String FM_AUTH_003 = "Forbidden";

    public static final String FM_AUTH_004 = "Token Expired";

    public static final String FM_AUTH_005 = "Account Locked";

    // =========================================================
    // User Errors
    // =========================================================

    public static final String FM_USER_001 = "User Not Found";

    public static final String FM_USER_002 = "User Already Exists";

    public static final String FM_USER_003 = "Invalid User";

    // =========================================================
    // Food Errors
    // =========================================================

    public static final String FM_FOOD_001 = "Food Not Found";

    public static final String FM_FOOD_002 = "Food Already Exists";

    public static final String FM_FOOD_003 = "Invalid Food Status";

    public static final String FM_FOOD_004 = "Invalid Food Category";

    // =========================================================
    // Order Errors
    // =========================================================

    public static final String FM_ORD_001 = "Order Not Found";

    public static final String FM_ORD_002 = "Invalid Order Status";

    public static final String FM_ORD_003 = "Order Already Cancelled";

    public static final String FM_ORD_004 = "Order Already Delivered";

    public static final String FM_ORD_005 = "Payment Pending";

    // =========================================================
    // Restaurant Errors
    // =========================================================

    public static final String FM_RST_001 = "Restaurant Not Found";

    public static final String FM_RST_002 = "Restaurant Closed";

    // =========================================================
    // Payment Errors
    // =========================================================

    public static final String FM_PAY_001 = "Payment Failed";

    public static final String FM_PAY_002 = "Payment Timeout";

    public static final String FM_PAY_003 = "Refund Failed";

    // =========================================================
    // Image Errors
    // =========================================================

    public static final String FM_IMG_001 = "Image Not Found";

    public static final String FM_IMG_002 = "Image Upload Failed";

    // =========================================================
    // Coupon Errors
    // =========================================================

    public static final String FM_CPN_001 = "Invalid Coupon";

    public static final String FM_CPN_002 = "Coupon Expired";

    // =========================================================
    // Delivery Partner Errors
    // =========================================================

    public static final String FM_DLP_001 = "Delivery Partner Not Found";

    public static final String FM_DLP_002 = "Delivery Partner Unavailable";

}