package com.foodies.freshmeal.food.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Bulk Restore Food Request
 * ============================================================================
 *
 * Request used for restoring multiple archived food
 * items.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class BulkRestoreFoodRequest {

    /**
     * Food identifiers.
     */
    private List<String> foodIds;

}