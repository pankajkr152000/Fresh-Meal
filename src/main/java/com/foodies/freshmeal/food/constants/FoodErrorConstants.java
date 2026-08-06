package com.foodies.freshmeal.food.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Food Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Food module.
 *
 * Each error contains:
 * • Error Code
 * • User Friendly Message
 *
 * These errors are used by:
 *
 * • Services
 * • Validators
 * • Controllers
 * • GlobalExceptionHandler
 *
 * ============================================================================
 *
 * Error Code Convention
 *
 * FM-FOOD-001
 *
 * FM -> FreshMeal
 * FOOD -> Module
 * 001 -> Error Number
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum FoodErrorConstants implements IBusinessError {

	// =========================================================================
	// General
	// =========================================================================
    FOOD_NOT_FOUND(
            "FM-FOOD-001",
            "Food not found.",
            HttpStatusCode.NOT_FOUND),

    FOOD_ALREADY_EXISTS(
            "FM-FOOD-002",
            "Food already exists.",
            HttpStatusCode.CONFLICT),

    FOOD_ALREADY_ARCHIVED(
            "FM-FOOD-003",
            "Food is already archived.",
            HttpStatusCode.CONFLICT),

    FOOD_ALREADY_ACTIVE(
            "FM-FOOD-004",
            "Food is already active.",
            HttpStatusCode.CONFLICT),

    FOOD_ALREADY_DELETED(
            "FM-FOOD-005",
            "Food has already been permanently deleted.",
            HttpStatusCode.GONE),

    // =========================================================================
    // Status
    // =========================================================================

    INVALID_FOOD_STATUS(
            "FM-FOOD-100",
            "Invalid food status.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_STATUS_TRANSITION(
            "FM-FOOD-101",
            "Invalid food status transition.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_ALREADY_AVAILABLE(
            "FM-FOOD-102",
            "Food is already available.",
            HttpStatusCode.CONFLICT),

    FOOD_ALREADY_OUT_OF_STOCK(
            "FM-FOOD-103",
            "Food is already out of stock.",
            HttpStatusCode.CONFLICT),

    FOOD_ALREADY_DISABLED(
            "FM-FOOD-104",
            "Food is already disabled.",
            HttpStatusCode.CONFLICT),

    FOOD_ALREADY_DISCONTINUED(
            "FM-FOOD-105",
            "Food is already discontinued.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Category
    // =========================================================================

    INVALID_FOOD_CATEGORY(
            "FM-FOOD-200",
            "Invalid food category.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_DIET_CATEGORY(
            "FM-FOOD-201",
            "Invalid diet category.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CUISINE_TYPE(
            "FM-FOOD-202",
            "Invalid cuisine type.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_CATEGORY_GROUP(
            "FM-FOOD-203",
            "Invalid category group.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Validation
    // =========================================================================

    FOOD_NAME_REQUIRED(
            "FM-FOOD-300",
            "Food name is required.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_DESCRIPTION_REQUIRED(
            "FM-FOOD-301",
            "Food description is required.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_PRICE_REQUIRED(
            "FM-FOOD-302",
            "Food price is required.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_PRICE_INVALID(
            "FM-FOOD-303",
            "Food price must be greater than zero.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_IMAGE_REQUIRED(
            "FM-FOOD-304",
            "Food image is required.",
            HttpStatusCode.BAD_REQUEST),

    FOOD_CATEGORY_REQUIRED(
            "FM-FOOD-305",
            "Food category is required.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Archive
    // =========================================================================

    FOOD_CANNOT_BE_ARCHIVED(
            "FM-FOOD-400",
            "Food cannot be archived.",
            HttpStatusCode.CONFLICT),

    FOOD_CANNOT_BE_RESTORED(
            "FM-FOOD-401",
            "Food cannot be restored.",
            HttpStatusCode.CONFLICT),

    FOOD_CANNOT_BE_DELETED(
            "FM-FOOD-402",
            "Food cannot be permanently deleted.",
            HttpStatusCode.CONFLICT),

    FOOD_REFERENCED_BY_ACTIVE_ORDER(
            "FM-FOOD-403",
            "Food is referenced by active orders.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Image
    // =========================================================================

    FOOD_IMAGE_NOT_FOUND(
            "FM-FOOD-500",
            "Food image not found.",
            HttpStatusCode.NOT_FOUND),

    FOOD_IMAGE_UPLOAD_FAILED(
            "FM-FOOD-501",
            "Unable to upload food image.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    FOOD_IMAGE_DELETE_FAILED(
            "FM-FOOD-502",
            "Unable to delete food image.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Bulk Operations
    // =========================================================================

    BULK_ARCHIVE_FAILED(
            "FM-FOOD-600",
            "Unable to archive one or more foods.",
            HttpStatusCode.CONFLICT),

    BULK_RESTORE_FAILED(
            "FM-FOOD-601",
            "Unable to restore one or more foods.",
            HttpStatusCode.CONFLICT),

    BULK_DELETE_FAILED(
            "FM-FOOD-602",
            "Unable to permanently delete one or more foods.",
            HttpStatusCode.CONFLICT);

    /**
     * Error Code.
     */
    private final String errorCode;

    /**
     * Error Message.
     */
    private final String errorMessage;

    /**
     * HTTP Status Code.
     */
    private final HttpStatusCode httpStatusCode;

    FoodErrorConstants(
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