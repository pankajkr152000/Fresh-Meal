package com.foodies.freshmeal.delivery.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerAvailabilityRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerIdRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerStatusRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerUpdateRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerVerificationRequest;
import com.foodies.freshmeal.user.dto.UserNumberRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Delivery Partner Controller Contract
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Delivery Partner management module.
 *
 * <p>
 * The controller is responsible only for request handling, authorization
 * metadata and delegation to the Delivery Partner service layer.
 * Business logic belongs to the service layer.
 * </p>
 *
 * <p>
 * OpenAPI documentation is defined at the controller contract level so that
 * the Delivery Partner API contract remains centralized and independent of
 * the controller implementation.
 * </p>
 *
 * <p>
 * Delivery Partner onboarding is intentionally excluded from this contract.
 * Onboarding represents a separate workflow and is exposed through the
 * dedicated Delivery Partner onboarding controller.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Delivery Partner", description = "APIs for Delivery Partner management.")
public interface IDeliveryPartnerController {

    // =========================================================================
    // Get Delivery Partner By ID
    // =========================================================================

    /**
     * Retrieves a Delivery Partner by identifier.
     *
     * <p>
     * Only active Delivery Partner records are returned.
     * </p>
     *
     * @param input Delivery Partner identifier request
     *
     * @return Delivery Partner details
     */
    @Operation(summary = "Get delivery partner by ID", description = """
            Retrieves an active Delivery Partner using its
            MongoDB identifier.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery Partner retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid Delivery Partner identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Delivery Partner not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> getById(
            DeliveryPartnerIdRequest input);

    // =========================================================================
    // Get Delivery Partner By User Number
    // =========================================================================

    /**
     * Retrieves a Delivery Partner associated with a User.
     *
     * <p>
     * The User Number identifies the FreshMeal user associated with the
     * Delivery Partner profile.
     * </p>
     *
     * @param input user number request
     *
     * @return Delivery Partner details
     */
    @Operation(summary = "Get delivery partner by user number", description = """
            Retrieves the active Delivery Partner profile associated
            with a FreshMeal user number.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery Partner retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user number.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Delivery Partner not found for the specified user.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> getByUserNumber(
            UserNumberRequest input);

    // =========================================================================
    // Update Delivery Partner
    // =========================================================================

    /**
     * Updates an existing Delivery Partner profile.
     *
     * <p>
     * Only editable Delivery Partner profile information should be supplied
     * through this operation. System-controlled fields such as verification
     * status, business status and availability are managed through their
     * dedicated operations.
     * </p>
     *
     * @param request Delivery Partner update request
     *
     * @return updated Delivery Partner
     */
    @Operation(summary = "Update delivery partner", description = """
            Updates the editable profile information of an existing
            Delivery Partner.

            Verification status, business status and availability are
            managed through their dedicated API operations.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery Partner updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid Delivery Partner update request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Delivery Partner not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Delivery Partner update conflicts with an existing business rule.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> update(
            DeliveryPartnerUpdateRequest request);

    // =========================================================================
    // Update Verification Status
    // =========================================================================

    /**
     * Updates the verification status of a Delivery Partner.
     *
     * <p>
     * Verification is an administrative operation and is intentionally
     * separated from the Delivery Partner business status and availability.
     * </p>
     *
     * @param request Delivery Partner verification request
     *
     * @return updated Delivery Partner
     */
    @Operation(summary = "Update delivery partner verification status", description = """
            Updates the verification status of a Delivery Partner.

            Verification is maintained independently from the Delivery
            Partner business status and availability.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery Partner verification status updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid verification status or transition.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Delivery Partner not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updateVerificationStatus(
            DeliveryPartnerVerificationRequest request);

    // =========================================================================
    // Update Delivery Partner Status
    // =========================================================================

    /**
     * Updates the business status of a Delivery Partner.
     *
     * <p>
     * Business status represents the operational lifecycle of the Delivery
     * Partner and is maintained independently from verification and
     * availability.
     * </p>
     *
     * @param request Delivery Partner status request
     *
     * @return updated Delivery Partner
     */
    @Operation(summary = "Update delivery partner status", description = """
            Updates the operational business status of a Delivery
            Partner.

            The requested status must comply with the allowed Delivery
            Partner status transition rules.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery Partner status updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status or status transition.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Delivery Partner not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updateStatus(
            DeliveryPartnerStatusRequest request);

    // =========================================================================
    // Update Availability
    // =========================================================================

    /**
     * Updates the current availability of a Delivery Partner.
     *
     * <p>
     * Availability is an operational state and is separate from verification
     * status and business status.
     * </p>
     *
     * @param request Delivery Partner availability request
     *
     * @return updated Delivery Partner
     */
    @Operation(summary = "Update delivery partner availability", description = """
            Updates the current availability of a Delivery Partner.

            Availability can only be changed when the Delivery Partner
            satisfies the required verification and operational
            eligibility rules.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Delivery Partner availability updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid availability request or Delivery Partner is not eligible.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Delivery Partner not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updateAvailability(
            DeliveryPartnerAvailabilityRequest request);
}