package com.foodies.freshmeal.common.validation.validator;

import com.foodies.freshmeal.common.validation.model.ValidationResult;

/**
 * Generic validator contract.
 *
 * @param <T> Request type
 */
public interface IValidator<T> {

    ValidationResult validate(T request);

}