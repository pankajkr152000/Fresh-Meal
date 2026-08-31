package com.foodies.freshmeal.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Restaurant Controller Contract
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Restaurant module.
 *
 * <p>
 * The controller is responsible only for request handling and delegation.
 * Business logic belongs to the Restaurant service layer.
 * </p>
 *
 * <p>
 * OpenAPI documentation is defined at the controller contract level so that
 * the Restaurant API contract remains centralized and independent of the
 * controller implementation.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Restaurant", description = "APIs for restaurant management.")
public interface IRestaurantController {

    // =========================================================================
    // Create Restaurant
    // =========================================================================

    /**
     * Creates a new restaurant.
     *
     * <p>
     * The request is multipart/form-data and contains:
     * </p>
     *
     * <ul>
     * <li>restaurant - RestaurantCreateRequest JSON</li>
     * <li>logoImage - optional restaurant logo</li>
     * <li>coverImage - optional restaurant cover image</li>
     * </ul>
     *
     * @param restaurantJson restaurant request JSON
     * @param logoImage      optional restaurant logo
     * @param coverImage     optional restaurant cover image
     *
     * @return created restaurant
     *
     * @throws JsonProcessingException if restaurant JSON cannot be parsed
     */
    @Operation(summary = "Create restaurant", description = """
            Creates a new restaurant in FreshMeal.

            The request uses multipart/form-data and accepts the
            restaurant details as JSON along with optional logo and
            cover images.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurant created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant request or validation failure.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurant already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantDetailsResponse>> createRestaurant(
            String restaurantJson,
            MultipartFile logoImage,
            MultipartFile coverImage) throws JsonProcessingException;

    // =========================================================================
    // Read All Restaurants
    // =========================================================================

    /**
     * Retrieves all active restaurants.
     *
     * @return active restaurants
     */
    @Operation(summary = "Read all restaurants", description = "Retrieves all active restaurants registered in FreshMeal.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<RestaurantListResponse>>> readRestaurants();

    // =========================================================================
    // Get Restaurant By ID
    // =========================================================================

    /**
     * Retrieves a restaurant by identifier.
     *
     * @param input restaurant identifier request
     *
     * @return restaurant details
     */
    @Operation(summary = "Get restaurant by ID", description = "Retrieves an active restaurant using its MongoDB identifier.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantDetailsResponse>> getRestaurantById(
            @RequestBody RestaurantIdRequest input);

    // =========================================================================
    // Update Restaurant
    // =========================================================================

    /**
     * Updates an existing restaurant.
     *
     * <p>
     * The request is multipart/form-data and contains:
     * </p>
     *
     * <ul>
     * <li>restaurant - RestaurantUpdateRequest JSON</li>
     * <li>logoImage - optional replacement logo</li>
     * <li>coverImage - optional replacement cover image</li>
     * </ul>
     *
     * <p>
     * If an image is not supplied, the existing image reference remains
     * unchanged according to the service-layer update rules.
     * </p>
     *
     * @param restaurantJson restaurant update JSON
     * @param logoImage      optional replacement logo
     * @param coverImage     optional replacement cover image
     *
     * @return updated restaurant
     *
     * @throws JsonProcessingException if restaurant JSON cannot be parsed
     */
    @Operation(summary = "Update restaurant", description = """
            Updates an existing restaurant.

            The request uses multipart/form-data and accepts the
            restaurant update details as JSON along with optional
            replacement logo and cover images.
            """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid restaurant update request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurant update conflicts with an existing business rule.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<RestaurantDetailsResponse>> updateRestaurant(
            String restaurantJson,
            MultipartFile logoImage,
            MultipartFile coverImage) throws JsonProcessingException;
}
