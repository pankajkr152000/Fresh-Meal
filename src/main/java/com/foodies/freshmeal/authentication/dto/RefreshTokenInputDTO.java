package com.foodies.freshmeal.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Refresh Token Input DTO
 * ============================================================================
 *
 * Service-layer input wrapper for the refresh-token workflow.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class RefreshTokenInputDTO {

    /**
     * Refresh-token request.
     */
    private RefreshTokenRequest refreshTokenRequest;
}