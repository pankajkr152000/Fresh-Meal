package com.foodies.freshmeal.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Restaurant Branch Controller Contract
 * ============================================================================
 *
 * <p>
 * Defines HTTP operations exposed by the Restaurant Branch module.
 * </p>
 *
 * <p>
 * The controller is responsible only for request handling, validation,
 * service-input construction, and delegation to the service layer.
 * Business logic belongs to {@code IRestaurantBranchService}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Restaurant Branch", description = "APIs for restaurant branch management.")
public interface IRestaurantBranchController {

    // =========================================================================
    // Create Branch
    // =========================================================================

    /**
     * Creates a new restaurant branch.
     *
     * @param input restaurant branch creation request
     *
     * @return created restaurant branch
     */
    @Operation(summary = "Create restaurant branch", description = "Creates a new branch for a restaurant.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurant branch created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant branch request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurant branch already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> createBranch(
            @RequestBody RestaurantBranchCreateRequest input);

    // =========================================================================
    // Read All Branches
    // =========================================================================

    /**
     * Retrieves all active restaurant branches.
     *
     * @return active restaurant branches
     */
    @Operation(summary = "Read all restaurant branches", description = "Retrieves all active restaurant branches.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant branches retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<RestaurantBranchListResponse>>> readBranches();

    // =========================================================================
    // Get Branch By ID
    // =========================================================================

    /**
     * Retrieves a restaurant branch by identifier.
     *
     * @param input restaurant branch identifier request
     *
     * @return restaurant branch details
     */
    @Operation(summary = "Get restaurant branch by ID", description = "Retrieves an active restaurant branch using its identifier.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant branch retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant branch identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant branch not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> getBranchById(
            @RequestBody RestaurantBranchIdRequest input);

    // =========================================================================
    // Get Branches By Restaurant
    // =========================================================================

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * @param input restaurant identifier request
     *
     * @return restaurant branches belonging to the restaurant
     */
    @Operation(summary = "Get branches by restaurant ID", description = "Retrieves all active branches belonging to the specified restaurant.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant branches retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<RestaurantBranchListResponse>>> getBranchesByRestaurantId(
            @RequestBody RestaurantIdRequest input);

    // =========================================================================
    // Update Branch
    // =========================================================================

    /**
     * Updates an existing restaurant branch.
     *
     * @param input restaurant branch update request
     *
     * @return updated restaurant branch
     */
    @Operation(summary = "Update restaurant branch", description = "Updates an existing restaurant branch.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant branch updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant branch update request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant branch not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurant branch update conflicts with an existing business rule.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> updateBranch(
            @RequestBody RestaurantBranchUpdateRequest input);
}