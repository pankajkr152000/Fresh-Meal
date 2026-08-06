package com.foodies.freshmeal.common.exception;

/**
 * Forbidden Exception.
 */
public class ForbiddenException extends AbstractBusinessException {

	private static final long serialVersionUID = 1L;

	public ForbiddenException(final IBusinessError error) {

		super(error);
	}

}