package com.foodies.freshmeal.food.controller;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * ============================================================================
 * Food Navigation Controller Contract
 * ============================================================================
 *
 * <p>
 * Defines navigation-related HTTP operations exposed by the Food module.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Define navigation operations for Food resources.</li>
 * <li>Keep the navigation controller contract separate from its
 * implementation.</li>
 * <li>Return the application's standardized {@link ApiResponse} structure.</li>
 * </ul>
 *
 * <p>
 * The implementation is responsible for delegating the navigation request
 * to the appropriate Food service layer operation.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Tag(name = "Food Navigation", description = "Navigation APIs for Food resources.")
public interface IFoodNavigationController {

    // =========================================================================
    // Food Navigation
    // =========================================================================

    /**
     * Retrieves the Food resource identified by the navigation context.
     *
     * @return standardized response containing the Food view
     *
     * @throws JsonProcessingException if response processing fails
     */
    @Operation(summary = "Get food", description = "Retrieves the Food resource associated with the current navigation context.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Food retrieved successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Food resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    ResponseEntity<ApiResponse<EntityViewResponse<FoodResponse>>> getFoodByFoodId() throws JsonProcessingException;
}