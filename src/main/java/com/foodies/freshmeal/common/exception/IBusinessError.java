package com.foodies.freshmeal.common.exception;

import com.foodies.freshmeal.common.constants.HttpStatusCode;

/**
 * ============================================================================
 * Business Error
 * ============================================================================
 *
 * Represents a business error within the application.
 *
 * Every business error provides:
 *
 * • Error Code • Error Message • HTTP Status • Retry Information
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IBusinessError {

	/**
	 * Returns unique business error code.
	 *
	 * Example:
	 *
	 * FM-FOOD-001
	 *
	 * @return Error code.
	 */
	String getErrorCode();

	/**
	 * Returns user friendly error message.
	 *
	 * @return Error message.
	 */
	String getErrorMessage();

	/**
	 * Returns HTTP status.
	 *
	 * @return HTTP status.
	 */
	HttpStatusCode getHttpStatusCode();

	/**
	 * Indicates whether the failed operation can safely be retried.
	 *
	 * @return true if retryable.
	 */
	default boolean isRetryable() {
		return false;
	}

	/**
	 * Returns true if the error represents a client-side failure.
	 *
	 * @return true if 4xx.
	 */
	default boolean isClientError() {

		return getHttpStatusCode().isClientError();
	}

	/**
	 * Returns true if the error represents a server-side failure.
	 *
	 * @return true if 5xx.
	 */
	default boolean isServerError() {

		return getHttpStatusCode().isServerError();
	}

}