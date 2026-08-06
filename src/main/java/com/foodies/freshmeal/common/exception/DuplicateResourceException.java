package com.foodies.freshmeal.common.exception;

/**
 * Duplicate Resource Exception.
 */
public class DuplicateResourceException extends AbstractBusinessException {

	private static final long serialVersionUID = 1L;

	public DuplicateResourceException(final IBusinessError error) {

		super(error);
	}

}