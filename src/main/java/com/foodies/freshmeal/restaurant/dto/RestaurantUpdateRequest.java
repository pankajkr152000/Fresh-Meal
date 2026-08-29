package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import com.foodies.freshmeal.food.constants.CuisineTypeConstant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantUpdateRequest
 * ============================================================================
 *
 * Represents the restaurant information that can be modified through the
 * standard restaurant update operation.
 *
 * Server-managed fields and lifecycle-specific fields are intentionally
 * excluded from this request.
 *
 * ============================================================================
 *
 * Excluded Fields
 * ---------------
 *
 * restaurantNumber
 * id
 * status
 * statusUpdatedAt
 * statusUpdatedBy
 * isAvailable
 * audit fields
 * record status
 * delete information
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Basic Information
    // =========================================================================
    private String restaurantId;
    /**
     * Business/display name of the restaurant.
     */
    @NotBlank
    @Size(max = 150)
    private String restaurantName;

    /**
     * Optional restaurant description.
     */
    @Size(max = 1000)
    private String description;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * Primary restaurant phone number.
     */
    @NotBlank
    private String phoneNumber;

    /**
     * Primary restaurant email address.
     */
    @NotBlank
    @Email
    private String emailAddress;

    /**
     * Official restaurant website.
     */
    @Size(max = 500)
    private String website;

    // =========================================================================
    // Restaurant Classification
    // =========================================================================

    /**
     * Cuisine types offered by the restaurant.
     *
     * Uses the existing FreshMeal cuisine taxonomy.
     */
    @NotEmpty
    private Set<CuisineTypeConstant> cuisineTypes;

    // =========================================================================
    // Images
    // =========================================================================

    /**
     * MongoDB identifier of the restaurant logo image.
     */
    private String logoImageId;

    /**
     * MongoDB identifier of the restaurant cover image.
     */
    private String coverImageId;

}