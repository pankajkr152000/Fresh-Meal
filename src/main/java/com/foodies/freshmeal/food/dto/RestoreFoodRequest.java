package com.foodies.freshmeal.food.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Restore Food Request
 * ============================================================================
 *
 * Request used to restore an archived food item.
 *
 * A restored food becomes available for normal business
 * operations again.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class RestoreFoodRequest {

    /**
     * Food identifier.
     */
    private String foodId;

}