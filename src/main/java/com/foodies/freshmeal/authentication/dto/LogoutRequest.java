package com.foodies.freshmeal.authentication.dto;

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
    /*
     * Intentionally empty.
     *
     * The authenticated user is obtained from Spring Security's
     * SecurityContext rather than from client-supplied identity data.
     */
}