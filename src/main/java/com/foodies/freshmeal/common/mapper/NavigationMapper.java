package com.foodies.freshmeal.common.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodies.freshmeal.common.dto.view.NavigationItem;



/**
 * ============================================================================
 * Class: NavigationMapper
 * ============================================================================
 *
 * Purpose:
 * Utility class responsible for creating navigation DTOs.
 *
 * Responsibilities:
 * 1. Create available navigation item.
 * 2. Create unavailable navigation item.
 *
 * ============================================================================
 */
public final class NavigationMapper {

    // =========================================================================
    // Logger
    // =========================================================================

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NavigationMapper.class);

    // =========================================================================
    // Constructor
    // =========================================================================

    private NavigationMapper() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Public Methods
    // =========================================================================

    /**
     * Creates an available navigation item.
     *
     * @param id Entity identifier.
     * @param displayName Entity display name.
     * @return NavigationItem
     */
    public static NavigationItem available(String id, String displayName) {

        LOGGER.debug("Creating available navigation item. Id: {}", id);

        return NavigationItem.builder()
                .id(id)
                .displayName(displayName)
                .available(true)
                .build();
    }

    /**
     * Creates an unavailable navigation item.
     *
     * @return NavigationItem
     */
    public static NavigationItem unavailable() {

        LOGGER.debug("Creating unavailable navigation item.");

        return NavigationItem.builder()
                .available(false)
                .build();
    }

}
