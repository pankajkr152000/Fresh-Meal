package com.foodies.freshmeal.restaurant.dto;

import java.util.List;

import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.restaurant.valueObject.OperatingHours;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Restaurant Branch Registration Request
 * ============================================================================
 *
 * Represents the initial branch information supplied during restaurant-owner
 * onboarding.
 *
 * <p>
 * A restaurant may have multiple branches. This DTO represents the first
 * branch created as part of the initial restaurant onboarding flow.
 * Additional branches can be created later through the restaurant management
 * module.
 * </p>
 *
 * <p>
 * System-controlled information such as branch number, restaurant identifier,
 * and audit information is intentionally excluded.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranchRegistrationRequest {

    // =========================================================================
    // Branch Information
    // =========================================================================

    /**
     * Business/display name of the branch.
     */
    @NotBlank
    @Size(max = 150)
    private String branchName;

    /**
     * Physical address of the restaurant branch.
     */
    @Valid
    private Address address;

    /**
     * Regular weekly operating hours of the branch.
     */
    @Valid
    private List<OperatingHours> operatingHours;
}