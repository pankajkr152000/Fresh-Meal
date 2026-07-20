package com.foodies.freshmeal.common.service;

import com.foodies.freshmeal.common.dto.view.EntityNavigation;


/**
 * ============================================================================
 * Interface: EntityNavigationService
 * ============================================================================
 *
 * Purpose:
 * Defines the contract for generating Previous and Next navigation
 * information for any entity.
 *
 * Implementations:
 * - FoodNavigationService
 * - RestaurantNavigationService
 * - CategoryNavigationService
 * - OrderNavigationService
 * - UserNavigationService
 *
 * Responsibilities:
 * 1. Build navigation for the current entity.
 * 2. Return previous entity information.
 * 3. Return next entity information.
 *
 * ============================================================================
 */
public interface IEntityNavigationService {

    /**
     * Generates navigation details for the given entity.
     *
     * @param currentId Current entity identifier.
     * @return Navigation details.
     */
    EntityNavigation getNavigation(String currentId);

}
