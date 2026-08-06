package com.foodies.freshmeal.food.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Bulk Archive Food Request
 * ============================================================================
 *
 * Request used for archiving multiple food items.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class BulkArchiveFoodRequest {

    /**
     * Food identifiers.
     */
    private List<String> foodIds;

}