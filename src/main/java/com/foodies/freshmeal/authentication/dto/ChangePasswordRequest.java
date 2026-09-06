package com.foodies.freshmeal.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Change Password Request
 * ============================================================================
 *
 * Represents the API request used by an authenticated user to change the
 * current account password.
 *
 * <p>
 * The current password must be verified before the new password is accepted.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ChangePasswordRequest {

    /**
     * Current account password.
     */
    @NotBlank(message = "Current password is required.")
    @Size(max = 16, message = "Current password must not exceed 16 characters.")
    private String currentPassword;

    /**
     * New account password.
     */
    @NotBlank(message = "New password is required.")
    @Size(min = 6, max = 16, message = "New password must be between 6 and 16 characters.")
    private String newPassword;

    /**
     * Confirmation of the new account password.
     */
    @NotBlank(message = "Confirm password is required.")
    @Size(min = 6, max = 16, message = "Confirm password must be between 6 and 16 characters.")
    private String confirmPassword;
}