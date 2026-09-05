package com.foodies.freshmeal.authentication.dto;

/**
 * ============================================================================
 * Register Response
 * ============================================================================
 *
 * Response returned after a FreshMeal account registration request has been
 * accepted for email verification.
 *
 * <p>
 * This DTO intentionally exposes only the information required by the client
 * to continue the email verification workflow.
 * </p>
 *
 * <h3>Security Considerations</h3>
 *
 * <ul>
 * <li>The email address is returned as the verification target.</li>
 * <li>The verification requirement indicates that email verification is
 * pending.</li>
 * <li>The verification OTP is never returned.</li>
 * <li>The OTP persistence identifier is never returned.</li>
 * <li>The internal MongoDB identifier is never returned.</li>
 * <li>Password and password-related information are never returned.</li>
 * <li>Roles and account security flags are never returned.</li>
 * </ul>
 *
 * <p>
 * This response does not issue authentication tokens. A newly registered
 * account must successfully complete email verification before it can
 * authenticate through the login flow.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class RegisterResponse {

    /**
     * Email address associated with the registration request.
     */
    private String email;

    /**
     * Indicates whether email verification is required before authentication
     * can proceed.
     */
    private boolean verificationRequired;

    /**
     * Returns the email address associated with the registration.
     *
     * @return Email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address associated with the registration.
     *
     * @param email Email address.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Returns whether email verification is required.
     *
     * @return {@code true} when email verification is required;
     *         otherwise {@code false}.
     */
    public boolean isVerificationRequired() {
        return verificationRequired;
    }

    /**
     * Sets whether email verification is required.
     *
     * @param verificationRequired Verification requirement.
     */
    public void setVerificationRequired(final boolean verificationRequired) {
        this.verificationRequired = verificationRequired;
    }
}