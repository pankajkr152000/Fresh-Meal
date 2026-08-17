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
 * RestaurantCreateRequest
 * ============================================================================
 *
 * Represents the data required to create a new restaurant.
 *
 * This DTO contains only client-supplied restaurant information.
 *
 * Server-managed fields such as:
 *
 * - id
 * - restaurantNumber
 * - audit information
 * - record status
 * - status tracking information
 *
 * are intentionally excluded.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Basic Information
    // =========================================================================

    @NotBlank
    @Size(max = 150)
    private String restaurantName;

    @Size(max = 1000)
    private String description;

    // =========================================================================
    // Contact Information
    // =========================================================================

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Email
    private String emailAddress;

    @Size(max = 500)
    private String website;

    // =========================================================================
    // Restaurant Classification
    // =========================================================================

    @NotEmpty
    private Set<CuisineTypeConstant> cuisineTypes;

}