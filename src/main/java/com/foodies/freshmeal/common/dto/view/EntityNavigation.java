package com.foodies.freshmeal.common.dto.view;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Entity Navigation
 * ============================================================================
 *
 * Holds previous and next navigation details.
 *
 * ============================================================================
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityNavigation {

    private NavigationItem previous;

    private NavigationItem next;

}
