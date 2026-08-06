package com.foodies.freshmeal.common.exception;

import com.foodies.freshmeal.common.validation.model.ValidationResult;

import lombok.Getter;

/**
 * ============================================================================
 * Validation Exception
 * ============================================================================
 *
 * Thrown when one or more validation rules fail.
 *
 * Carries the complete validation result so the
 * GlobalExceptionHandler can return all validation
 * errors to the client.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
public class ValidationException extends AbstractBusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Validation result.
     */
    private final ValidationResult validationResult;

    /**
     * Creates validation exception.
     *
     * @param error            Business error.
     * @param validationResult Validation result.
     */
    public ValidationException(
            final IBusinessError error,
            final ValidationResult validationResult) {

        super(error);

        this.validationResult = validationResult;
    }

    public ValidationException(ValidationResult validationResult) {
        super((IBusinessError) null);
        this.validationResult = validationResult;
    }

}