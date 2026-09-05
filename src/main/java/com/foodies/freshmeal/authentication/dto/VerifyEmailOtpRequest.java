package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.valueObject.EmailAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * ============================================================================
 * Verify Email OTP Request
 * ============================================================================
 *
 * Request payload used to verify a user's email address during FreshMeal
 * account registration.
 *
 * <p>
 * The OTP represented by this request belongs exclusively to the email
 * verification lifecycle. It must not be reused for password recovery or
 * password reset operations.
 * </p>
 *
 * <h3>Security Considerations</h3>
 *
 * <ul>
 * <li>The OTP is short-lived.</li>
 * <li>The OTP has a limited number of verification attempts.</li>
 * <li>The raw OTP must never be persisted.</li>
 * <li>The OTP must become unusable after successful verification.</li>
 * <li>A successful verification marks the user's email as verified.</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class VerifyEmailOtpRequest {

    /**
     * Email address whose ownership is being verified.
     */
    @Valid
    private EmailAddress email;

    /**
     * Six-digit email verification OTP.
     */
    @NotBlank(message = "Email verification OTP is required.")
    @Pattern(regexp = "^[0-9]{6}$", message = "Email verification OTP must be a 6-digit number.")
    private String otp;

    /**
     * Returns the email address.
     *
     * @return Email address.
     */
    public EmailAddress getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email Email address.
     */
    public void setEmail(final EmailAddress email) {
        this.email = email;
    }

    /**
     * Returns the email verification OTP.
     *
     * @return OTP.
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Sets the email verification OTP.
     *
     * @param otp Email verification OTP.
     */
    public void setOtp(final String otp) {
        this.otp = otp;
    }
}