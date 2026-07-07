package com.foodies.freshmeal.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1742060935711286780L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
