package com.foodies.freshmeal.common.validation.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Stores all validation errors.
 * 
 * 
 * Uses
 * 
 * ValidationResult validation = foodValidator.validate(foodRequest);
 * if (validation.hasErrors()) {
 * throw new ValidationException(validation);
 * }
 * 
 * 
 * ValidationResult result = foodValidator.validate(foodRequest);
 * serviceContext.setValidationResult(result);
 * 
 * if (result.hasErrors()) {
 * throw new ValidationException(result);
 * }
 */
@Getter
public class ValidationResult {

    private final List<ValidationError> errors = new ArrayList<>();

    public void addError(String field, String message) {
        errors.add(new ValidationError(field, message));
    }

    public void addError(ValidationError validationError) {
        
        errors.add(validationError);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
