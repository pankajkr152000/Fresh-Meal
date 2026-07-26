package com.foodies.freshmeal.common.validation.validator;

import com.foodies.freshmeal.common.validation.model.ValidationResult;

/**
 * Base validator for all modules.
 *
 * @param <T> Request type
 */
public abstract class AbstractValidator<T> implements IValidator<T> {

    protected ValidationResult result;

    @Override
    public ValidationResult validate(T request) {

        result = new ValidationResult();

        doValidate(request);

        return result;
    }

    /**
     * Module-specific validation logic.
     */
    protected abstract void doValidate(T request);

    protected void reject(String field, String message) {
        result.addError(field, message);
    }

}