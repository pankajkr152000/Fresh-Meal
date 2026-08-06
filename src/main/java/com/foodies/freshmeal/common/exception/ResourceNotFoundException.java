package com.foodies.freshmeal.common.exception;

/**
 * Resource Not Found Exception.
 */
public class ResourceNotFoundException extends AbstractBusinessException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(final IBusinessError error) {

		super(error);
	}

}