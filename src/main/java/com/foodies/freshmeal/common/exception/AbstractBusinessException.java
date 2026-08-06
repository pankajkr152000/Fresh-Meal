package com.foodies.freshmeal.common.exception;

import com.foodies.freshmeal.common.constants.HttpStatusCode;

import lombok.Getter;

/**
 * ============================================================================
 * Abstract Business Exception
 * ============================================================================
 *
 * Base exception for all business exceptions.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
public abstract class AbstractBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Business Error.
	 */
	private final IBusinessError error;

	/**
	 * Creates business exception.
	 *
	 * @param error Business error.
	 */
	protected AbstractBusinessException(final IBusinessError error) {

		super(error.getErrorMessage());

		this.error = error;
	}

	/**
	 * Creates business exception.
	 *
	 * @param error Business error.
	 * @param cause Root cause.
	 */
	protected AbstractBusinessException(final IBusinessError error, final Throwable cause) {

		super(error.getErrorMessage(), cause);

		this.error = error;
	}

	/**
	 * Returns error code.
	 *
	 * @return Error code.
	 */
	public String getErrorCode() {

		return error.getErrorCode();
	}

	/**
	 * Returns error message.
	 *
	 * @return Error message.
	 */
	public String getErrorMessage() {

		return error.getErrorMessage();
	}

	/**
	 * Returns HTTP status.
	 *
	 * @return HTTP status.
	 */
	public HttpStatusCode getHttpStatusCode() {

		return error.getHttpStatusCode();
	}

}