package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.restaurant.constants.RestaurantStatusConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantStatusUpdateRequest
 * ============================================================================
 *
 * <p>
 * Represents a request to change the business lifecycle status of a
 * restaurant.
 * </p>
 *
 * <p>
 * This request is intentionally separate from
 * {@link RestaurantUpdateRequest}. A standard restaurant update modifies
 * business information, whereas this request modifies the restaurant
 * lifecycle.
 * </p>
 *
 * <p>
 * Operational availability is intentionally excluded and is managed through
 * a separate availability operation.
 * </p>
 *
 * ============================================================================
 *
 * Excluded Fields
 * ---------------
 *
 * <ul>
 * <li>restaurantNumber</li>
 * <li>restaurantName</li>
 * <li>description</li>
 * <li>phoneNumber</li>
 * <li>emailAddress</li>
 * <li>website</li>
 * <li>cuisineTypes</li>
 * <li>isAvailable</li>
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
public class RestaurantStatusUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * MongoDB identifier of the restaurant.
     */
    @NotBlank
    private String restaurantId;

    /**
     * Requested restaurant lifecycle status.
     */
    @NotNull
    private RestaurantStatusConstant status;
}