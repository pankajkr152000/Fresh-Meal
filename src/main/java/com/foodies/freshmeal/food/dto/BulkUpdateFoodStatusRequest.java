package com.foodies.freshmeal.food.dto;

import java.util.List;

import com.foodies.freshmeal.food.constants.FoodStatusConstant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Bulk status update request.
 *
 * Example
 *
 * {
 * "foodIds":[
 * "101",
 * "102",
 * "103"
 * ],
 *
 * "status":"DISABLED"
 * }
 * ============================================================================
 */

@Getter
@Setter
public class BulkUpdateFoodStatusRequest {

    @NotEmpty(message = "Food IDs cannot be empty.")
    private List<String> foodIds;

    @NotNull(message = "Food status is required.")
    private FoodStatusConstant status;

}
