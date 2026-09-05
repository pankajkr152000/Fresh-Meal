package com.foodies.freshmeal.authentication.dto;

import jakarta.validation.constraints.NotNull;

/**
 * ============================================================================
 * Register Input DTO
 * ============================================================================
 *
 * Service input wrapper for the FreshMeal user registration operation.
 *
 * <p>
 * This DTO separates the externally supplied registration request from the
 * internal service-input infrastructure represented by
 * {@code IServiceInput}.
 * </p>
 *
 * <p>
 * The service context is intentionally not stored inside this DTO. It is
 * provided through {@code IServiceInput} by the application service layer,
 * following the established FreshMeal service architecture.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class RegisterInputDTO {

    /**
     * Registration request supplied by the client.
     */
    @NotNull(message = "Registration request is required.")
    private RegisterRequest registerRequest;

    /**
     * Returns the registration request.
     *
     * @return Registration request.
     */
    public RegisterRequest getRegisterRequest() {
        return registerRequest;
    }

    /**
     * Sets the registration request.
     *
     * @param registerRequest Registration request.
     */
    public void setRegisterRequest(final RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
    }
}