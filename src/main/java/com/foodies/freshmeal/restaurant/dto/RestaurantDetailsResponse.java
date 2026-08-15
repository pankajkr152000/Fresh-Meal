package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantDetailsResponse
 * ============================================================================
 *
 * Represents the detailed API view of a restaurant.
 *
 * This DTO is used when the complete restaurant information is required,
 * such as the Restaurant View page.
 *
 * It is intentionally separate from RestaurantEntity.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailsResponse implements Serializable {

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
     * Business-facing restaurant identifier.
     *
     * Example:
     * FM-RST-0000001
     */
    private String restaurantNumber;

    // =========================================================================
    // Basic Information
    // =========================================================================

    /**
     * Display name of the restaurant.
     */
    private String restaurantName;

    /**
     * Restaurant description.
     */
    private String description;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * Restaurant phone number.
     *
     * Returned as a display-ready value rather than exposing
     * the internal PhoneNumber value object.
     */
    private String phoneNumber;

    /**
     * Restaurant email address.
     *
     * Returned as a display-ready value.
     */
    private String emailAddress;

    /**
     * Official restaurant website.
     */
    private String website;

    // =========================================================================
    // Classification
    // =========================================================================

    /**
     * Cuisine types offered by the restaurant.
     */
    private Set<DisplayOptionResponse> cuisineTypes;

    // =========================================================================
    // Restaurant Status
    // =========================================================================

    /**
     * Current restaurant lifecycle status.
     */
    private DisplayOptionResponse status;

    /**
     * Timestamp of the most recent restaurant status change.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime statusUpdatedAt;

    /**
     * Administrator who last changed the restaurant status.
     */
    private String statusUpdatedBy;

    /**
     * Indicates whether the restaurant is currently operationally available.
     */
    private boolean isAvailable;

    // =========================================================================
    // Images
    // =========================================================================

    /**
     * Resolved restaurant logo image URL.
     */
    private String logoImageUrl;

    /**
     * Resolved restaurant cover image URL.
     */
    private String coverImageUrl;

    // =========================================================================
    // Branch Information
    // =========================================================================

    /**
     * Number of physical branches associated with the restaurant.
     */
    private Integer branchCount;

    // =========================================================================
    // Audit Information
    // =========================================================================

    /**
     * Timestamp when the restaurant was created.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    /**
     * User who created the restaurant.
     */
    private String createdBy;

    /**
     * Timestamp when the restaurant was last updated.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

    /**
     * User who last updated the restaurant.
     */
    private String updatedBy;

}