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
}
