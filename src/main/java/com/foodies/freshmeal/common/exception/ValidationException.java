package com.foodies.freshmeal.common.exception;

import com.foodies.freshmeal.common.validation.model.ValidationResult;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        super("Validation failed.");
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}