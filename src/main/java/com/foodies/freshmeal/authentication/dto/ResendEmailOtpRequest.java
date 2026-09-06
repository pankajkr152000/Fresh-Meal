package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Resend Email OTP Request
 * ============================================================================
 *
 * Represents the API request used to request a new email verification OTP.
 *
 * <p>
 * The request identifies the account through its email address. The actual
 * OTP generation, invalidation of previous OTPs, expiration handling, and
 * delivery workflow remain responsibilities of the authentication service.
 * </p>
 *
 * <h3>Security Considerations</h3>
 *
 * <ul>
 * <li>The request does not contain an OTP.</li>
 * <li>OTP generation is handled by the authentication service.</li>
 * <li>Raw OTP values must never be persisted.</li>
 * <li>Resend limits must be enforced by the authentication business rules.</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ResendEmailOtpRequest {

    /**
     * Email address for which a new verification OTP is requested.
     */
    @NotNull(message = "Email is required.")
    @Valid
    private EmailAddress email;
}