package com.foodies.freshmeal.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Logout Request
 * ============================================================================
 *
 * Represents the API request used to terminate the current authentication
 * session.
 *
 * <p>
 * FreshMeal uses stateless JWT authentication. Therefore, logout behavior is
 * handled according to the configured token lifecycle rather than relying on
 * an HTTP server session.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class LogoutRequest {
	/**
     * Identifies the login session that should be terminated.
     *
     * <p>
     * The value is obtained from the authenticated session established during
     * login and is used by the backend to terminate the appropriate session.
     * </p>
     */
    @NotBlank(message = "Login session ID is required.")
    private String loginSessionId;
    
    /**
     * Indicates whether all active login sessions should be terminated.
     */
   // private boolean logoutFromAllDevices;
    
}