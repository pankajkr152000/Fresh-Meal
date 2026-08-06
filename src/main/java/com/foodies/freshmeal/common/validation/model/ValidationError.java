package com.foodies.freshmeal.common.validation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a single validation error.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    /**
     * Field name.
     */
    private String field;

    /**
     * Validation message.
     */
    private String message;

    /**
     * Creates a validation error.
     *
     * @param field   Field name.
     * @param message Validation message.
     *
     * @return ValidationError instance.
     */
    public static ValidationError of(final String field, final String message) {

        return new ValidationError(field, message);
    }
}
