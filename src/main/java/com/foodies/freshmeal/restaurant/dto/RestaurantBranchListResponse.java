package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.valueObject.GeoLocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantBranchListResponse
 * ============================================================================
 *
 * Represents a single restaurant branch record displayed in the
 * Admin Branch List.
 *
 * This DTO intentionally contains only the information required to identify
 * and manage a branch from a list/table view.
 *
 * Detailed information such as:
 *
 * - Complete address
 * - Operating hours
 * - Status audit information
 * - Creation/update audit information
 *
 * belongs to RestaurantBranchDetailsResponse.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranchListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Identification
    // =========================================================================

    /**
     * MongoDB document identifier.
     */
    private String id;

    /**
     * Business-facing branch identifier.
     */
    private String branchNumber;

    /**
     * Internal identifier of the parent restaurant.
     */
    private String restaurantId;

    // =========================================================================
    // Branch Information
    // =========================================================================

    /**
     * Business/display name of the branch.
     */
    private String branchName;

    /**
     * Concise display representation of the branch address.
     */
    private String addressSummary;

    /**
     * Geographic location of the branch.
     */
    private GeoLocation geoLocation;

    // =========================================================================
    // Branch Status
    // =========================================================================

    /**
     * Current branch lifecycle status.
     */
    private DisplayOptionResponse status;

    /**
     * Indicates whether the branch is currently operationally available.
     */
    private boolean isAvailable;

}