package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Forgot Password Request
 * ============================================================================
 *
 * Represents the API request used to initiate password recovery.
 *
 * <p>
 * The email address is used as the password recovery identifier. The API
 * response must remain intentionally generic so that account existence is not
 * disclosed.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ForgotPasswordRequest {

    /**
     * Email address associated with the account requesting password recovery.
     */
    @NotNull(message = "Email is required.")
    @Valid
    private EmailAddress email;
}