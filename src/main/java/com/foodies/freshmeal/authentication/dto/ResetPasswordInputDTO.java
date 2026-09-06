package com.foodies.freshmeal.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Reset Password Input DTO
 * ============================================================================
 *
 * Service-layer input wrapper for the password-reset workflow.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ResetPasswordInputDTO {

    /**
     * Password-reset request.
     */
    private ResetPasswordRequest resetPasswordRequest;
}