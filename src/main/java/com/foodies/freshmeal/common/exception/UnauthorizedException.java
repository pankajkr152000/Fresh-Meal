package com.foodies.freshmeal.common.exception;

/**
 * Unauthorized Exception.
 */
public class UnauthorizedException extends AbstractBusinessException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(final IBusinessError error) {

		super(error);
	}

}