package com.foodies.freshmeal.restaurant.dto;

import java.util.Set;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.food.constants.CuisineTypeConstant;

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
 * DTO : Restaurant Registration Request
 * ============================================================================
 *
 * Represents restaurant-level information supplied during restaurant-owner
 * onboarding.
 *
 * <p>
 * This DTO contains only restaurant information that may be supplied by the
 * restaurant owner. System-controlled information such as restaurant number,
 * owner user number, status, availability, audit information, and image
 * identifiers is intentionally excluded.
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
public class RestaurantRegistrationRequest {

    // =========================================================================
    // Restaurant Information
    // =========================================================================

    /**
     * Business/display name of the restaurant.
     */
    @NotBlank
    @Size(max = 150)
    private String restaurantName;

    /**
     * Optional description of the restaurant.
     */
    @Size(max = 1000)
    private String description;

    /**
     * Primary contact phone number of the restaurant.
     */
    @Valid
    private PhoneNumber phoneNumber;

    /**
     * Primary contact email address of the restaurant.
     */
    @Valid
    private EmailAddress emailAddress;

    /**
     * Official restaurant website.
     */
    @Size(max = 500)
    private String website;

    /**
     * Cuisine types offered by the restaurant.
     */
    private Set<CuisineTypeConstant> cuisineTypes;
}