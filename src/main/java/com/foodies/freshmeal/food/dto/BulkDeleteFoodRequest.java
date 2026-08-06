package com.foodies.freshmeal.food.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Bulk Permanent Delete Food Request
 * ============================================================================
 *
 * Request used for permanently deleting multiple
 * archived food items.
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
public class BulkDeleteFoodRequest {

    /**
     * Food identifiers.
     */
    private List<String> foodIds;

}