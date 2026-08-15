package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.common.valueObject.GeoLocation;
import com.foodies.freshmeal.restaurant.valueObject.OperatingHours;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantBranchDetailsResponse
 * ============================================================================
 *
 * Represents the detailed API view of a restaurant branch.
 *
 * This DTO is intended for the Branch View/Details page.
 *
 * It contains complete physical-location information together with
 * branch lifecycle and audit information.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranchDetailsResponse implements Serializable {

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

    // =========================================================================
    // Physical Location
    // =========================================================================

    /**
     * Complete physical address of the branch.
     */
    private Address address;

    /**
     * Geographic location of the branch.
     */
    private GeoLocation geoLocation;

    // =========================================================================
    // Operating Hours
    // =========================================================================

    /**
     * Regular weekly operating hours of the branch.
     */
    private List<OperatingHours> operatingHours;

    // =========================================================================
    // Branch Status
    // =========================================================================

    /**
     * Current branch lifecycle status.
     */
    private DisplayOptionResponse status;

    /**
     * Timestamp of the most recent branch status change.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime statusUpdatedAt;

    /**
     * Administrator who last changed the branch status.
     */
    private String statusUpdatedBy;

    /**
     * Indicates whether the branch is currently operationally available.
     */
    private boolean isAvailable;

    // =========================================================================
    // Audit Information
    // =========================================================================

    /**
     * Timestamp when the branch was created.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    /**
     * User who created the branch.
     */
    private String createdBy;

    /**
     * Timestamp when the branch was last updated.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

    /**
     * User who last updated the branch.
     */
    private String updatedBy;

}