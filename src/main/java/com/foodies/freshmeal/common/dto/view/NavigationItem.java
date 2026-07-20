package com.foodies.freshmeal.common.dto.view;


import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Navigation Item
 * ============================================================================
 *
 * Represents one navigation target.
 *
 * ============================================================================
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavigationItem {

    /**
     * Entity Identifier
     */
    private String id;

    /**
     * Display Name
     */
    private String displayName;

    /**
     * Whether navigation is available.
     */
    @Builder.Default
    private boolean available = false;

    /**
     * Optional future metadata.
     *
     * Examples:
     * - status
     * - thumbnail
     * - badge
     * - order number
     */
    private Map<String, Object> metadata;

}
