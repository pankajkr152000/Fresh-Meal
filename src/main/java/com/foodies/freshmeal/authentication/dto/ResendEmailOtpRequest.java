
package com.foodies.freshmeal.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Resend Email OTP Request
 * ============================================================================
 *
 * Represents the API request used to request a new email verification OTP
 * for an existing pending registration.
 *
 * <p>
 * The request identifies the pending registration using its business-facing
 * registration number. The registered email address is retrieved from the
 * pending registration rather than accepted from the client.
 * </p>
 *
 * <h3>Security Considerations</h3>
 * <ul>
 * <li>The request does not contain an OTP.</li>
 * <li>The request does not accept an email address as an identity.</li>
 * <li>OTP generation and replacement remain responsibilities of the
 * authentication and OTP services.</li>
 * <li>Raw OTP values must never be persisted or returned to the client.</li>
 * <li>Resend cooldown and attempt limits must be enforced.</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
public class ResendEmailOtpRequest {

    /**
     * Business-facing identifier of the pending registration.
     */
    @NotBlank(message = "Registration number is required.")
    private String registrationNumber;
}
