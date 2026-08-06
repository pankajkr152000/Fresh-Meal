package com.foodies.freshmeal.food.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Permanent Delete Food Request
 * ============================================================================
 *
 * Request used for permanently removing an archived
 * food item from the database.
 *
 * This operation is irreversible.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class PermanentDeleteFoodRequest {

    /**
     * Food identifier.
     */
    private String foodId;

}