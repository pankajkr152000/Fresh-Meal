package com.foodies.freshmeal.pincode.constants;

import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.exception.IBusinessError;

/**
 * =============================================================================
 * Pincode Error Constants
 * =============================================================================
 *
 * Centralized business errors for the Pincode module.
 *
 * Error Code Convention
 * ---------------------
 * FM-PINCODE-001
 *
 * FM -> FreshMeal
 * PINCODE -> Module
 * 001 -> Error Number
 * =============================================================================
 */
public enum PincodeErrorConstants implements IBusinessError {

    /**
     * External pincode API could not be reached or returned an HTTP error.
     */
    PINCODE_API_FAILURE(
            "FM-PINCODE-001",
            "Unable to fetch pincode details from external service.",
            HttpStatusCode.BAD_GATEWAY);

    private final String errorCode;

    private final String errorMessage;

    private final HttpStatusCode httpStatusCode;

    PincodeErrorConstants(
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