package com.foodies.freshmeal.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Refresh Token Request
 * ============================================================================
 *
 * Represents the API request used to obtain a new access token using a valid
 * refresh token.
 *
 * <p>
 * The refresh token is validated by the authentication/token service. Clients
 * must not provide username, roles, or other identity information because
 * those values must be derived from the validated token.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class RefreshTokenRequest {

    /**
     * Refresh token issued during authentication.
     */
    @NotBlank(message = "Refresh token is required.")
    private String refreshToken;
}