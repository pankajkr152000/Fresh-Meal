package com.foodies.freshmeal.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Reset Password Request
 * ============================================================================
 *
 * Represents the API request used to reset a user's password using a valid
 * password-reset token.
 *
 * <p>
 * The reset token identifies and authorizes the password-reset operation.
 * User identity must be derived from the validated token rather than being
 * independently supplied by the client.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ResetPasswordRequest {

    /**
     * Password-reset token issued during password recovery.
     */
    @NotBlank(message = "Password reset token is required.")
    private String resetToken;

    /**
     * New account password.
     */
    @NotBlank(message = "New password is required.")
    @Size(min = 6, max = 16, message = "New password must be between 6 and 16 characters.")
    private String newPassword;

    /**
     * Confirmation of the new password.
     */
    @NotBlank(message = "Confirm password is required.")
    @Size(min = 6, max = 16, message = "Confirm password must be between 6 and 16 characters.")
    private String confirmPassword;
}