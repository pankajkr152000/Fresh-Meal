package com.foodies.freshmeal.user.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.user.dto.AddressIdRequest;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Address Controller
 * ============================================================================
 *
 * <p>
 * Defines HTTP operations for customer address management.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Define HTTP operations for customer addresses.</li>
 * <li>Receive request data from the client.</li>
 * <li>Delegate processing to the Address service implementation.</li>
 * </ul>
 *
 * <p>
 * The controller implementation is responsible for creating the common
 * {@code IServiceInput} structure before invoking the service layer.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Address", description = "APIs for customer address management.")
public interface IAddressController {

    // =========================================================================
    // Create Address
    // =========================================================================

    /**
     * Creates a new customer address.
     *
     * <p>
     * The address is created using the information supplied in the request.
     * </p>
     *
     * @param request address creation request
     *
     * @return created address information
     */
    @Operation(summary = "Add address", description = "Creates a new customer address.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Address created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address request or validation failure.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Address already exists or conflicts with an existing address.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<AddressResponse>> addAddress(AddressRequest request);

    // =========================================================================
    // View Address
    // =========================================================================

    /**
     * Retrieves an address using its business-facing address number.
     *
     * @param request address identifier request
     *
     * @return address information
     */
    @Operation(summary = "Get address by ID", description = "Retrieves a customer address using its business-facing address number.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<AddressResponse>> getAddressByAddressId(AddressIdRequest request);
}