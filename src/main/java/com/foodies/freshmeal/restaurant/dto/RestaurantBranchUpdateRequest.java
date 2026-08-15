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
 * RestaurantBranchUpdateRequest
 * ============================================================================
 *
 * Represents branch information that can be modified through the standard
 * branch update operation.
 *
 * Parent restaurant relationship and lifecycle fields are intentionally
 * excluded.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranchUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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