package com.foodies.freshmeal.common.exception;

/**
 * Thrown when an invalid food status transition is attempted.
 */
public class InvalidFoodStatusTransitionException extends RuntimeException {

    private static final long serialVersionUID = -2417326243081309297L;

	public InvalidFoodStatusTransitionException(String message) {
        super(message);
    }

}
