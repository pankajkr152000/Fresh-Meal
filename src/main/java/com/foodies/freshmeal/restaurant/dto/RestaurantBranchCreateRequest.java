package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.common.valueObject.GeoLocation;
import com.foodies.freshmeal.restaurant.valueObject.OperatingHours;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantBranchCreateRequest
 * ============================================================================
 *
 * Represents the data required to create a restaurant branch.
 *
 * Server-managed fields such as branchNumber, lifecycle status, availability,
 * and audit information are intentionally excluded.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranchCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Parent Restaurant
    // =========================================================================

    /**
     * MongoDB identifier of the parent restaurant.
     */
    @NotBlank
    private String restaurantId;

    // =========================================================================
    // Branch Information
    // =========================================================================

    /**
     * Business/display name of the branch.
     */
    @NotBlank
    @Size(max = 150)
    private String branchName;

    // =========================================================================
    // Physical Location
    // =========================================================================

    /**
     * Physical address of the branch.
     */
    @NotNull
    @Valid
    private Address address;

    /**
     * Geographic location of the branch.
     */
    @NotNull
    @Valid
    private GeoLocation geoLocation;

    // =========================================================================
    // Operating Hours
    // =========================================================================

    /**
     * Regular weekly operating hours of the branch.
     */
    @Valid
    private List<OperatingHours> operatingHours;

}