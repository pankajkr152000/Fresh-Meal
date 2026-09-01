package com.foodies.freshmeal.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.food.dto.ArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.BulkRestoreFoodRequest;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.PermanentDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.RestoreFoodRequest;
import com.foodies.freshmeal.food.dto.UpdateFoodStatusRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Food Controller Contract
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Food module.
 *
 * <p>
 * The controller is responsible only for request handling and delegation.
 * Business logic belongs to the Food service layer.
 * </p>
 *
 * <p>
 * OpenAPI documentation is defined at the controller contract level so that
 * the Food API contract remains centralized while the controller
 * implementation remains focused on HTTP request handling and service
 * delegation.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Food", description = "APIs for food management, metadata, archive, restore and permanent deletion.")
public interface IFoodController {

    // =========================================================================
    // Food CRUD Operations
    // =========================================================================

    /**
     * Adds a new food item.
     *
     * <p>
     * The request contains food information as JSON and an optional food image.
     * </p>
     *
     * @param foodJson  food request JSON
     * @param imageFile optional food image
     *
     * @return created food
     *
     * @throws JsonProcessingException if food JSON cannot be parsed
     */
    @Operation(summary = "Add food", description = "Creates a new food item with optional food image.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Food created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid food request or validation failure.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Food already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<FoodResponse>> addFood(
            String foodJson,
            MultipartFile imageFile) throws JsonProcessingException;

    /**
     * Retrieves all active food items.
     *
     * @return active food list
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Read all foods", description = "Retrieves all active food items.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Foods retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods()
            throws JsonProcessingException;

    /**
     * Retrieves a food item by its identifier.
     *
     * @param foodId food identifier request
     *
     * @return food details
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get food by ID", description = "Retrieves an active food item using its identifier.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid food identifier.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Food not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<EntityViewResponse<FoodResponse>>> getFoodByFoodId(
            FoodStatusRequest foodId) throws JsonProcessingException;

    /**
     * Updates the status of a food item.
     *
     * @param foodId        food identifier
     * @param updateRequest food status update request
     *
     * @return updated food
     *
     * @throws JsonProcessingException if request processing fails
     */
    @Operation(summary = "Update food status", description = "Updates the lifecycle status of an existing food item.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food status updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid food status or status transition.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Food not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<FoodResponse>> updateFoodStatus(
            String foodId,
            UpdateFoodStatusRequest updateRequest) throws JsonProcessingException;

    /**
     * Updates an existing food item.
     *
     * <p>
     * The request contains food information as JSON and an optional replacement
     * image.
     * </p>
     *
     * @param foodJson  food update JSON
     * @param imageFile optional replacement food image
     *
     * @return updated food
     *
     * @throws JsonProcessingException if food JSON cannot be parsed
     */
    @Operation(summary = "Edit food", description = "Updates an existing food item with an optional replacement image.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid food update request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Food not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Food update conflicts with an existing business rule.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<FoodResponse>> editFood(
            String foodJson,
            MultipartFile imageFile) throws JsonProcessingException;

    // =========================================================================
    // Food Metadata Operations
    // =========================================================================

    /**
     * Retrieves available food categories.
     *
     * @return food category options
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get food categories", description = "Retrieves the available food category options.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food categories retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> foodCategories()
            throws JsonProcessingException;

    /**
     * Retrieves available diet categories.
     *
     * @return diet category options
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get diet categories", description = "Retrieves the available diet category options.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Diet categories retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> dietCategories()
            throws JsonProcessingException;

    /**
     * Retrieves available cuisine categories.
     *
     * @return cuisine category options
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get cuisine categories", description = "Retrieves the available cuisine category options.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cuisine categories retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> cuisineCategories()
            throws JsonProcessingException;

    /**
     * Retrieves available food group categories.
     *
     * @return food group category options
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get food group categories", description = "Retrieves the available food group category options.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food group categories retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> groupCategories()
            throws JsonProcessingException;

    /**
     * Retrieves food category metadata.
     *
     * @return food metadata
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get food category metadata", description = "Retrieves metadata used by the Food module.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food metadata retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<FoodMetadataResponse>> foodCategoryMetadata()
            throws JsonProcessingException;

    // =========================================================================
    // Archive Operations
    // =========================================================================

    /**
     * Archives a food item.
     *
     * @param input archive food request
     *
     * @return archived food response
     */
    @Operation(summary = "Archive food", description = "Archives an active food item using a logical deletion operation.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food archived successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Food not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Food is already archived or cannot be archived.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<FoodResponse>> archiveFood(
            @RequestBody ArchiveFoodRequest input);

    /**
     * Archives multiple food items.
     *
     * @param input bulk archive request
     *
     * @return success response
     */
    @Operation(summary = "Bulk archive foods", description = "Archives multiple food items in a single operation.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Foods archived successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid bulk archive request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "One or more foods could not be archived.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<Void>> bulkArchiveFoods(
            @RequestBody BulkArchiveFoodRequest input);

    // =========================================================================
    // Restore Operations
    // =========================================================================

    /**
     * Restores an archived food item.
     *
     * @param input restore food request
     *
     * @return restored food response
     */
    @Operation(summary = "Restore food", description = "Restores a previously archived food item.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food restored successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Archived food not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Food is already active or cannot be restored.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<FoodResponse>> restoreFood(
            @RequestBody RestoreFoodRequest input);

    /**
     * Restores multiple archived food items.
     *
     * @param input bulk restore request
     *
     * @return success response
     */
    @Operation(summary = "Bulk restore foods", description = "Restores multiple archived food items in a single operation.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Foods restored successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid bulk restore request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "One or more foods could not be restored.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<Void>> bulkRestoreFoods(
            @RequestBody BulkRestoreFoodRequest input);

    // =========================================================================
    // Permanent Delete Operations
    // =========================================================================

    /**
     * Permanently deletes an archived food item.
     *
     * @param input permanent delete request
     *
     * @return success response
     */
    @Operation(summary = "Permanently delete food", description = "Permanently deletes an archived food item from the database.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food permanently deleted successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Archived food not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Food cannot be permanently deleted.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<Void>> permanentDeleteFood(
            @RequestBody PermanentDeleteFoodRequest input);

    /**
     * Permanently deletes multiple archived food items.
     *
     * @param input bulk permanent delete request
     *
     * @return success response
     */
    @Operation(summary = "Bulk permanently delete foods", description = "Permanently deletes multiple archived food items.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Foods permanently deleted successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid bulk permanent delete request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "One or more foods could not be permanently deleted.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<Void>> bulkPermanentDeleteFoods(
            @RequestBody BulkDeleteFoodRequest input);

    // =========================================================================
    // Archived Food Operations
    // =========================================================================

    /**
     * Retrieves all archived food items.
     *
     * @return archived food list
     */
    @Operation(summary = "Read archived foods", description = "Retrieves all food items that have been archived.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Archived foods retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<List<FoodResponse>>> readArchivedFoods();
}