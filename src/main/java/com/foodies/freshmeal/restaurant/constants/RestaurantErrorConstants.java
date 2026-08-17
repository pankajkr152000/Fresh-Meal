package com.foodies.freshmeal.restaurant.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Restaurant Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Restaurant module.
 *
 * Each error contains:
 * • Error Code
 * • User Friendly Message
 * • HTTP Status Code
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
 * FM-RST-001
 *
 * FM -> FreshMeal
 * RST -> Restaurant Module
 * 001 -> Error Number
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum RestaurantErrorConstants implements IBusinessError {

    // =========================================================================
    // General
    // =========================================================================

    RESTAURANT_NOT_FOUND(
            "FM-RST-001",
            "Restaurant not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_ALREADY_EXISTS(
            "FM-RST-002",
            "Restaurant already exists.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_ALREADY_ARCHIVED(
            "FM-RST-003",
            "Restaurant is already archived.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_ALREADY_ACTIVE(
            "FM-RST-004",
            "Restaurant is already active.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_ALREADY_DELETED(
            "FM-RST-005",
            "Restaurant has already been permanently deleted.",
            HttpStatusCode.GONE),

    // =========================================================================
    // Status
    // =========================================================================

    INVALID_RESTAURANT_STATUS(
            "FM-RST-100",
            "Invalid restaurant status.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_RESTAURANT_STATUS_TRANSITION(
            "FM-RST-101",
            "Invalid restaurant status transition.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_ALREADY_AVAILABLE(
            "FM-RST-102",
            "Restaurant is already available.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_ALREADY_UNAVAILABLE(
            "FM-RST-103",
            "Restaurant is already unavailable.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Validation
    // =========================================================================

    RESTAURANT_NAME_REQUIRED(
            "FM-RST-300",
            "Restaurant name is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_PHONE_REQUIRED(
            "FM-RST-301",
            "Restaurant phone number is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_EMAIL_REQUIRED(
            "FM-RST-302",
            "Restaurant email address is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_CUISINE_REQUIRED(
            "FM-RST-303",
            "At least one cuisine type is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_RESTAURANT_CUISINE(
            "FM-RST-304",
            "Invalid restaurant cuisine type.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Image
    // =========================================================================

    RESTAURANT_LOGO_IMAGE_NOT_FOUND(
            "FM-RST-500",
            "Restaurant logo image not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_COVER_IMAGE_NOT_FOUND(
            "FM-RST-501",
            "Restaurant cover image not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_IMAGE_UPLOAD_FAILED(
            "FM-RST-502",
            "Unable to upload restaurant image.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    RESTAURANT_IMAGE_DELETE_FAILED(
            "FM-RST-503",
            "Unable to delete restaurant image.",
            HttpStatusCode.INTERNAL_SERVER_ERROR),

    // =========================================================================
    // Branch
    // =========================================================================

    RESTAURANT_BRANCH_NOT_FOUND(
            "FM-RST-600",
            "Restaurant branch not found.",
            HttpStatusCode.NOT_FOUND),

    RESTAURANT_BRANCH_ALREADY_EXISTS(
            "FM-RST-601",
            "Restaurant branch already exists.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_BRANCH_REQUIRES_RESTAURANT(
            "FM-RST-602",
            "Restaurant branch must belong to a valid restaurant.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_BRANCH_ALREADY_ACTIVE(
            "FM-RST-603",
            "Restaurant branch is already active.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_BRANCH_ALREADY_ARCHIVED(
            "FM-RST-604",
            "Restaurant branch is already archived.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Branch Validation
    // =========================================================================

    RESTAURANT_BRANCH_NAME_REQUIRED(
            "FM-RST-700",
            "Restaurant branch name is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_BRANCH_ADDRESS_REQUIRED(
            "FM-RST-701",
            "Restaurant branch address is required.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_BRANCH_LOCATION_INVALID(
            "FM-RST-702",
            "Restaurant branch location is invalid.",
            HttpStatusCode.BAD_REQUEST),

    RESTAURANT_BRANCH_OPERATING_HOURS_INVALID(
            "FM-RST-703",
            "Restaurant branch operating hours are invalid.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Archive
    // =========================================================================

    RESTAURANT_CANNOT_BE_ARCHIVED(
            "FM-RST-800",
            "Restaurant cannot be archived.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_CANNOT_BE_RESTORED(
            "FM-RST-801",
            "Restaurant cannot be restored.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_CANNOT_BE_DELETED(
            "FM-RST-802",
            "Restaurant cannot be permanently deleted.",
            HttpStatusCode.CONFLICT),

    RESTAURANT_HAS_ACTIVE_BRANCHES(
            "FM-RST-803",
            "Restaurant cannot be archived while it has active branches.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Bulk Operations
    // =========================================================================

    BULK_ARCHIVE_FAILED(
            "FM-RST-900",
            "Unable to archive one or more restaurants.",
            HttpStatusCode.CONFLICT),

    BULK_RESTORE_FAILED(
            "FM-RST-901",
            "Unable to restore one or more restaurants.",
            HttpStatusCode.CONFLICT),

    BULK_DELETE_FAILED(
            "FM-RST-902",
            "Unable to permanently delete one or more restaurants.",
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

    RestaurantErrorConstants(
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