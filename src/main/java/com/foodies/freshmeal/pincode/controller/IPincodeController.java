package com.foodies.freshmeal.pincode.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Pincode Controller Contract
 * ============================================================================
 *
 * <p>
 * Defines HTTP operations exposed by the Pincode module.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Define HTTP operations exposed by the Pincode module.</li>
 * <li>Keep the controller contract separate from its implementation.</li>
 * <li>Return the application's standardized {@link ApiResponse} structure.</li>
 * </ul>
 *
 * <p>
 * The implementation is responsible for delegating the request to the Pincode
 * service layer.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Pincode", description = "APIs for pincode-based location lookup.")
public interface IPincodeController {

	// =========================================================================
	// Pincode Lookup
	// =========================================================================

	/**
	 * Looks up location details for the supplied pincode.
	 *
	 * <p>
	 * The lookup returns the location information associated with the supplied
	 * pincode.
	 * </p>
	 *
	 * @param request pincode lookup request
	 *
	 * @return standardized response containing pincode details
	 */
	@Operation(summary = "Lookup pincode", description = "Retrieves location details for the supplied pincode.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pincode details retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid pincode lookup request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pincode details not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))) })
	ResponseEntity<ApiResponse<PincodeDetails>> lookupPincode(PincodeLookupRequest request);
}