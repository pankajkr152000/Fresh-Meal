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
 * RestaurantAvailabilityUpdateRequest
 * ============================================================================
 *
 * <p>
 * Represents a request to change the operational availability of a restaurant.
 * </p>
 *
 * <p>
 * Operational availability is intentionally separated from the restaurant
 * lifecycle status represented by {@code RestaurantStatusConstant}.
 * </p>
 *
 * <p>
 * A restaurant may be in {@code ACTIVE} lifecycle status while temporarily
 * unavailable for operations.
 * </p>
 *
 * ============================================================================
 *
 * Excluded Fields
 * ---------------
 *
 * <ul>
 * <li>restaurantNumber</li>
 * <li>status</li>
 * <li>audit fields</li>
 * <li>record status</li>
 * <li>delete information</li>
 * </ul>
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAvailabilityUpdateRequest
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * MongoDB identifier of the restaurant.
     */
    @NotBlank
    private String restaurantId;

    /**
     * Requested operational availability.
     */
    private boolean available;
}