package com.foodies.freshmeal.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Forgot Password Input DTO
 * ============================================================================
 *
 * Service-layer input wrapper for password recovery initiation.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ForgotPasswordInputDTO {

    /**
     * Password recovery request.
     */
    private ForgotPasswordRequest forgotPasswordRequest;
}