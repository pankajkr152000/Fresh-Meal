package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : EmailRequest
 * ============================================================================
 *
 * Represents an email address used to identify a FreshMeal user.
 *
 * <p>
 * This DTO is primarily used by internal user-service operations that need
 * to locate a user by email address.
 * </p>
 *
 * <h3>Business Meaning</h3>
 * <p>
 * An email address is a business-level identifier of a FreshMeal user and is
 * therefore represented using the application's {@link EmailAddress} value
 * object rather than as an unstructured {@link String}.
 * </p>
 *
 * <h3>Validation</h3>
 * <p>
 * The nested {@link EmailAddress} validation is triggered through
 * {@link Valid}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class EmailRequest {

    // =========================================================================
    // Email
    // =========================================================================

    /**
     * Email address used to identify the user.
     */
    @NotNull(message = "Email is required.")
    @Valid
    private EmailAddress email;
}