package com.foodies.freshmeal.delivery.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerRegistrationRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Controller : IDeliveryPartnerOnboardingController
 * ============================================================================
 *
 * Contract for Delivery Partner onboarding APIs.
 *
 * <p>
 * This controller is responsible only for the onboarding of an authenticated
 * user having the DELIVERY_PARTNER role. It is intentionally kept separate
 * from {@code IDeliveryPartnerController}, which handles post-onboarding
 * delivery partner management operations.
 * </p>
 *
 * <p>
 * Onboarding creates the DeliveryPartnerEntity and initializes its business
 * state as defined by the onboarding workflow.
 * </p>
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
@Tag(name = "Delivery Partner Onboarding", description = "APIs for Delivery Partner onboarding.")
public interface IDeliveryPartnerOnboardingController {

    /**
     * Onboards the authenticated user as a Delivery Partner.
     *
     * @param request delivery partner registration details
     * @return created delivery partner response
     */
    @Operation(summary = "Onboard Delivery Partner", description = "Creates a Delivery Partner profile for the authenticated user.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Delivery Partner onboarded successfully.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid Delivery Partner registration request.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication is required.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User is not authorized to onboard as a Delivery Partner.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Delivery Partner profile already exists.", content = @Content)
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> onboardDeliveryPartner(
            DeliveryPartnerRegistrationRequest request);
}