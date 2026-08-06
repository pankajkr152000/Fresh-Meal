package com.foodies.freshmeal.food.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Archive Food Request
 * ============================================================================
 *
 * Request used for archiving a food item.
 *
 * A food is never physically removed during archive.
 * Instead, it is marked as logically deleted and can later
 * be restored from the Archived Foods module.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ArchiveFoodRequest {

    /**
     * Food identifier.
     */
    private String foodId;

}
