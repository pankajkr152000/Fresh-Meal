package com.foodies.freshmeal.restaurant.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantOnboardingInputDTO;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Restaurant Onboarding Controller Contract
 * ============================================================================
 *
 * <p>
 * Defines HTTP operations exposed for restaurant-owner onboarding.
 * </p>
 *
 * <p>
 * The onboarding controller is responsible only for receiving the HTTP
 * request, delegating it to the restaurant onboarding service, and returning
 * the standardized API response.
 * </p>
 *
 * <p>
 * Restaurant onboarding establishes the restaurant business profile and its
 * initial branch for an authenticated FreshMeal user who has the
 * {@code RESTAURANT_OWNER} role.
 * </p>
 *
 * <p>
 * The authenticated user's identity must never be supplied by the client.
 * The onboarding service resolves the authenticated user from the existing
 * request-scoped service context.
 * </p>
 *
 * ============================================================================
 *
 * @author FreshMeal Development Team
 * @since 1.0
 */
@Tag(name = "Restaurant Onboarding", description = "APIs for restaurant-owner onboarding.")
public interface IRestaurantOnboardingController {

    // =========================================================================
    // Restaurant Owner Onboarding
    // =========================================================================

    /**
     * Onboards an authenticated restaurant owner.
     *
     * <p>
     * The onboarding request contains:
     * </p>
     *
     * <ul>
     * <li>Restaurant business information.</li>
     * <li>Initial restaurant branch information.</li>
     * </ul>
     *
     * <p>
     * System-controlled information such as restaurant identifiers,
     * owner user number, branch identifiers, restaurant status, and
     * availability are not accepted from the client.
     * </p>
     *
     * @param request restaurant-owner onboarding request
     *
     * @return created restaurant
     */
    @Operation(summary = "Onboard restaurant owner", description = """
            Creates a restaurant business profile and its initial branch
            for the authenticated FreshMeal restaurant owner.
            The authenticated user's identity is resolved from the
            server-side security context and is never accepted from
            the request payload.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurant onboarding completed successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant onboarding request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Authenticated user is not authorized to onboard a restaurant.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurant onboarding conflicts with an existing business rule.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantEntity>> onboardRestaurant(RestaurantOnboardingInputDTO request);
}