package com.foodies.freshmeal.common.exception;

/**
 * =============================================================================
 * Exception : PincodeException
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents a failure while communicating with the external pincode API.
 *
 * The business error definition is supplied through IBusinessError, following
 * the common FreshMeal exception architecture.
 *
 * The external HTTP status is retained separately for logging and diagnostics.
 * =============================================================================
 */
public class PincodeException extends AbstractBusinessException {

    private static final long serialVersionUID = 1L;

    private final int statusCode;

    /**
     * Creates a PincodeException.
     *
     * @param error      business error definition
     * @param statusCode HTTP status returned by the external API
     */
    public PincodeException(
            final IBusinessError error,
            final int statusCode) {

        super(error);
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status returned by the external API.
     *
     * @return external HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}