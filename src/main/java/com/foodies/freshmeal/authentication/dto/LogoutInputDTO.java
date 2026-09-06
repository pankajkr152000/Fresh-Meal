package com.foodies.freshmeal.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Logout Input DTO
 * ============================================================================
 *
 * Service-layer input wrapper for the logout workflow.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class LogoutInputDTO {

    /**
     * Logout request.
     */
    private LogoutRequest logoutRequest;
}