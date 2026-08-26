package com.foodies.freshmeal.user.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * ============================================================================
 * Address Error Constants
 * ============================================================================
 *
 * Centralized business errors for the Address module.
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
 * FM-USER-001
 *
 * FM -> FreshMeal
 * USER -> User module
 * 001 -> Error Number
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum AddressErrorConstants implements IBusinessError {

    // =========================================================================
    // General
    // =========================================================================

    ADDRESS_NOT_FOUND(
            "FM-USER-001",
            "Address not found.",
            HttpStatusCode.NOT_FOUND),

    ADDRESS_ALREADY_EXISTS(
            "FM-USER-002",
            "Address already exists.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Validation
    // =========================================================================

    ADDRESS_TYPE_REQUIRED(
            "FM-USER-100",
            "Address type is required.",
            HttpStatusCode.BAD_REQUEST),

    RECIPIENT_NAME_REQUIRED(
            "FM-USER-101",
            "Recipient name is required.",
            HttpStatusCode.BAD_REQUEST),

    PHONE_NUMBER_REQUIRED(
            "FM-USER-102",
            "Phone number is required.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_LINE1_REQUIRED(
            "FM-USER-103",
            "Address line 1 is required.",
            HttpStatusCode.BAD_REQUEST),

    ADDRESS_LINE2_REQUIRED(
            "FM-USER-104",
            "Address line 2 is required.",
            HttpStatusCode.BAD_REQUEST),

    CITY_REQUIRED(
            "FM-USER-105",
            "City is required.",
            HttpStatusCode.BAD_REQUEST),

    POSTAL_CODE_REQUIRED(
            "FM-USER-106",
            "Postal code is required.",
            HttpStatusCode.BAD_REQUEST),

    INVALID_POSTAL_CODE(
            "FM-USER-107",
            "Invalid postal code.",
            HttpStatusCode.BAD_REQUEST),

    // =========================================================================
    // Default Address
    // =========================================================================

    ADDRESS_ALREADY_DEFAULT(
            "FM-USER-200",
            "Address is already set as the default address.",
            HttpStatusCode.CONFLICT),

    DEFAULT_ADDRESS_UPDATE_FAILED(
            "FM-USER-201",
            "Unable to update the default address.",
            HttpStatusCode.CONFLICT),

    // =========================================================================
    // Pincode
    // =========================================================================

    PINCODE_DETAILS_NOT_FOUND(
            "FM-USER-300",
            "Unable to resolve address details for the provided postal code.",
            HttpStatusCode.NOT_FOUND);

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

    AddressErrorConstants(
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