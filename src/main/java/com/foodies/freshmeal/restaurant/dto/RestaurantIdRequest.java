package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantIdRequest
 * ============================================================================
 *
 * Represents the request payload used when a restaurant operation requires
 * a restaurant identifier.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantIdRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * MongoDB identifier of the restaurant.
     */
    @NotBlank
    private String restaurantId;

}