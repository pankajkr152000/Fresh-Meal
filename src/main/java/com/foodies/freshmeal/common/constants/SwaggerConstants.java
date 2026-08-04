package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : SwaggerConstants
 * ============================================================================
 *
 * Centralized constants used for OpenAPI (Swagger) documentation.
 *
 * <p>
 * This class standardizes API documentation across the FreshMeal application
 * by eliminating duplicate descriptions and maintaining consistent wording.
 * </p>
 *
 * <p>
 * This class should only contain documentation-related constants.
 * Business messages belong in {@link ApiMessageConstants}.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 * ============================================================================
 * 
 * 
 * 
 */

/**
 * 
 * Uses
 * 
 * @ApiResponses({
 *                 @ApiResponse(
 *                 responseCode = "201",
 *                 description = SwaggerConstants.CREATED
 *                 ),
 *                 @ApiResponse(
 *                 responseCode = "400",
 *                 description = SwaggerConstants.BAD_REQUEST
 *                 ),
 *                 @ApiResponse(
 *                 responseCode = "500",
 *                 description = SwaggerConstants.INTERNAL_SERVER_ERROR
 *                 )
 *                 })
 * 
 *                 @Operation(
 *                 summary = SwaggerConstants.CREATE,
 *                 description = SwaggerConstants.CREATE_DESCRIPTION
 *                 )
 * 
 *                 @Tag(
 *                 name = SwaggerConstants.ORDER,
 *                 description = SwaggerConstants.ORDER_TAG_DESCRIPTION
 *                 )
 */
public final class SwaggerConstants {

    /**
     * Private constructor.
     */
    private SwaggerConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // API Response Descriptions
    // =========================================================

    public static final String SUCCESS = "Request processed successfully.";

    public static final String CREATED = "Resource created successfully.";

    public static final String UPDATED = "Resource updated successfully.";

    public static final String DELETED = "Resource deleted successfully.";

    public static final String BAD_REQUEST = "The request is invalid.";

    public static final String UNAUTHORIZED = "Authentication is required.";

    public static final String FORBIDDEN = "You do not have permission to access this resource.";

    public static final String NOT_FOUND = "The requested resource was not found.";

    public static final String CONFLICT = "The request could not be completed due to a conflict.";

    public static final String VALIDATION_FAILED = "One or more validation errors occurred.";

    public static final String INTERNAL_SERVER_ERROR = "An unexpected server error occurred.";

    // =========================================================
    // Common Operation Summaries
    // =========================================================

    public static final String CREATE = "Create Resource";

    public static final String UPDATE = "Update Resource";

    public static final String DELETE = "Delete Resource";

    public static final String VIEW = "View Resource";

    public static final String READ_ALL = "Retrieve Resources";

    public static final String SEARCH = "Search Resources";

    public static final String FILTER = "Filter Resources";

    public static final String METADATA = "Retrieve Metadata";

    // =========================================================
    // Common Operation Descriptions
    // =========================================================

    public static final String CREATE_DESCRIPTION = "Creates a new resource.";

    public static final String UPDATE_DESCRIPTION = "Updates an existing resource.";

    public static final String DELETE_DESCRIPTION = "Deletes an existing resource.";

    public static final String VIEW_DESCRIPTION = "Retrieves a specific resource.";

    public static final String READ_ALL_DESCRIPTION = "Retrieves a collection of resources.";

    // =========================================================
    // Tag Names
    // =========================================================

    public static final String AUTHENTICATION = "Authentication";

    public static final String USER = "User Management";

    public static final String FOOD = "Food Management";

    public static final String ORDER = "Order Management";

    public static final String RESTAURANT = "Restaurant Management";

    public static final String PAYMENT = "Payment Management";

    public static final String IMAGE = "Image Management";

    public static final String COUPON = "Coupon Management";

    public static final String DELIVERY_PARTNER = "Delivery Partner Management";

    // =========================================================
    // Tag Descriptions
    // =========================================================

    public static final String USER_TAG_DESCRIPTION = "APIs for managing users.";

    public static final String FOOD_TAG_DESCRIPTION = "APIs for managing food items.";

    public static final String ORDER_TAG_DESCRIPTION = "APIs for managing customer orders.";

    public static final String RESTAURANT_TAG_DESCRIPTION = "APIs for managing restaurants.";

    public static final String PAYMENT_TAG_DESCRIPTION = "APIs for managing payments.";

    public static final String IMAGE_TAG_DESCRIPTION = "APIs for managing images.";

    // =========================================================
    // Parameter Descriptions
    // =========================================================

    public static final String ID_PARAMETER = "Unique identifier.";

    public static final String PAGE_NUMBER = "Page number (zero-based).";

    public static final String PAGE_SIZE = "Number of records per page.";

    public static final String SORT_BY = "Field used for sorting.";

    public static final String SORT_DIRECTION = "Sorting direction (ASC or DESC).";

}