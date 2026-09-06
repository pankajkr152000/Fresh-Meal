package com.foodies.freshmeal.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Change Password Input DTO
 * ============================================================================
 *
 * Service-layer input wrapper for the authenticated password-change workflow.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ChangePasswordInputDTO {

    /**
     * Password-change request.
     */
    private ChangePasswordRequest changePasswordRequest;
}