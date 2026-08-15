package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantListResponse
 * ============================================================================
 *
 * Represents a single restaurant record displayed in the Admin Restaurant List.
 *
 * This DTO intentionally contains only the information required by the
 * Restaurant Management table.
 *
 * Detailed information such as:
 *
 * - Contact information
 * - Address
 * - GeoLocation
 * - Operating hours
 * - Branch details
 * - Image details
 *
 * should be loaded through RestaurantDetailsResponse and dedicated
 * branch operations.
 *
 * ============================================================================
 *
 * Design Principle
 * ----------------
 *
 * RestaurantEntity is the persistence model.
 *
 * RestaurantListResponse is the API projection required by the
 * Admin Restaurant List.
 *
 * The API should never expose RestaurantEntity directly.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Restaurant Identification
    // =========================================================================

    /**
     * MongoDB document identifier.
     */
    private String id;

    /**
     * Business-facing restaurant number.
     *
     * Example:
     *
     * FM-RST-0000001
     */
    private String restaurantNumber;

    // =========================================================================
    // Restaurant Information
    // =========================================================================

    /**
     * Display name of the restaurant.
     */
    private String restaurantName;

    /**
     * Short restaurant description.
     */
    private String description;

    // =========================================================================
    // Restaurant Classification
    // =========================================================================

    /**
     * Cuisine types offered by the restaurant.
     */
    private Set<DisplayOptionResponse> cuisineTypes;

    // =========================================================================
    // Restaurant Image
    // =========================================================================

    /**
     * Resolved logo image URL used by the restaurant list.
     *
     * The entity stores the ImageEntity identifier.
     * The API exposes the usable image URL.
     */
    private String logoImageUrl;

    // =========================================================================
    // Restaurant Status
    // =========================================================================

    /**
     * Current restaurant lifecycle status.
     */
    private DisplayOptionResponse status;

    /**
     * Indicates whether the restaurant is currently operationally available.
     */
    private boolean isAvailable;

    // =========================================================================
    // Restaurant Branch
    // =========================================================================

    /**
     * Number of physical branches associated with the restaurant.
     */
    private Integer branchCount;

}