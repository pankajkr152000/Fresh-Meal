// package com.foodies.freshmeal.common.validation.validator;

// import com.foodies.freshmeal.common.validation.model.ValidationResult;

// /**
//  * Base validator for all modules.
//  *
//  * @param <T> Request type
//  */
// public abstract class AbstractValidator<T> implements IValidator<T> {

//     protected ValidationResult result;

//     @Override
//     public ValidationResult validate(T request) {

//         result = new ValidationResult();

//         doValidate(request);

//         return result;
//     }

//     /**
//      * Module-specific validation logic.
//      */
//     protected abstract void doValidate(T request);

//     protected void reject(String field, String message) {
//         result.addError(field, message);
//     }

// }

package com.foodies.freshmeal.common.validation.validator;

import com.foodies.freshmeal.common.exception.ValidationException;
import com.foodies.freshmeal.common.validation.model.ValidationResult;

/**
 * ============================================================================
 * Abstract Validator
 * ============================================================================
 *
 * Base class for all module validators.
 *
 * Provides common validation helper methods used by
 * FoodValidator, UserValidator, OrderValidator, etc.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public abstract class AbstractValidator {

    /**
     * Creates a new validation result.
     *
     * @return Validation result.
     */
    protected ValidationResult createValidationResult() {

        return new ValidationResult();
    }

    /**
     * Adds a validation error.
     *
     * @param validationResult Validation result.
     * @param field            Field name.
     * @param message          Validation message.
     */
    protected void reject(
            final ValidationResult validationResult,
            final String field,
            final String message) {

        validationResult.addError(field, message);
    }

    protected void reject(
            final String field,
            final String message) {
        ValidationResult validationResult = createValidationResult();
        validationResult.addError(field, message);
    }

    /**
     * Throws ValidationException if validation failed.
     *
     * @param validationResult Validation result.
     */
    protected void validate(
            final ValidationResult validationResult) {

        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult);
        }
    }

    /**
     * Validates request object.
     *
     * @param request          Request object.
     * @param validationResult Validation result.
     */
    protected void validateRequest(
            final Object request,
            final ValidationResult validationResult) {

        if (request == null) {

            reject(
                    validationResult,
                    "request",
                    "Request cannot be null.");
        }
    }
}