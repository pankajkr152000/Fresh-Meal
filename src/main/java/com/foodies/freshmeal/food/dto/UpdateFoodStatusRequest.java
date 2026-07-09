package com.foodies.freshmeal.food.dto;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : UpdateFoodStatusRequest
 * ============================================================================
 *
 * Represents a request to update the lifecycle status
 * of a single food item.
 *
 * Example Request
 * ----------------
 *
 * {
 * "status":"OUT_OF_STOCK"
 * }
 *
 * ============================================================================
 */

/**
 * Request DTO for updating the status of a single food.
 */
@Getter
@Setter
public class UpdateFoodStatusRequest {

    @NotNull(message = "Food status is required.")
    private DisplayOptionResponse status;

}
