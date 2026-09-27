package com.foodies.freshmeal.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.user.dto.AddressNumberRequest;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.dto.AddressUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Controller : IAddressController
 * ============================================================================
 *
 * Defines HTTP operations for customer address management.
 *
 * <p>
 * The authenticated customer owns the addresses exposed through this
 * controller. User identity is resolved from the authenticated security
 * context and is not supplied by the client.
 * </p>
 *
 * <p>
 * Address creation, retrieval, update, default-address management and
 * deletion are exposed as separate operations so that each business
 * responsibility remains explicit.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Address", description = "APIs for customer address management.")
public interface IAddressController {

    /**
     * Creates a new customer address.
     *
     * @param request address creation request
     * @return created address information
     */
    @Operation(summary = "Add address", description = "Creates a new address for the authenticated customer.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Address created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address request or validation failure.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Address already exists or conflicts with an existing address.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            AddressRequest request);

    /**
     * Retrieves a customer address using its business-facing address number.
     *
     * @param request address number request
     * @return address information
     */
    @Operation(summary = "Get address", description = "Retrieves an address using its business-facing address number.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address number.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<AddressResponse>> getAddressByAddressNumber(
            AddressNumberRequest request);

    /**
     * Retrieves all active addresses belonging to the authenticated customer.
     *
     * @return customer's saved addresses
     */
    @Operation(summary = "Get my addresses", description = "Retrieves all active addresses belonging to the authenticated customer.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Addresses retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses();

    /**
     * Updates an existing customer address.
     *
     * @param request address update request
     * @return updated address information
     */
    @Operation(summary = "Update address", description = "Updates an existing address belonging to the authenticated customer.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address update request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            AddressUpdateRequest request);

    /**
     * Sets an address as the default address of the authenticated customer.
     *
     * @param request address number request
     * @return updated address information
     */
    @Operation(summary = "Set default address", description = "Sets the specified address as the default address for the authenticated customer.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Default address updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            AddressNumberRequest request);

    /**
     * Deletes the specified customer address.
     *
     * <p>
     * The implementation should follow the application's soft-delete
     * lifecycle rather than physically removing the document.
     * </p>
     *
     * @param request address number request
     * @return standardized API response
     */
    @Operation(summary = "Delete address", description = "Deactivates an address belonging to the authenticated customer.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Address deleted successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<Void>> deleteAddress(
            AddressNumberRequest request);
}