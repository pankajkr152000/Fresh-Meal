package com.foodies.freshmeal.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Restaurant Owner Registration Request
 * ============================================================================
 *
 * Represents the restaurant-specific onboarding information submitted by a
 * restaurant owner after selecting the RESTAURANT_OWNER role.
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
public class RestaurantOwnerRegistrationRequest {

    /**
     * Restaurant-level business information.
     */
    @NotNull
    @Valid
    private RestaurantRegistrationRequest restaurant;

    /**
     * Initial branch information.
     */
    @NotNull
    @Valid
    private RestaurantBranchRegistrationRequest branch;
}