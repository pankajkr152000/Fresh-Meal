package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.image.dto.ImageSnapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : UpdateRestaurantInputDTO
 * ============================================================================
 *
 * Internal service input used during Restaurant update.
 *
 * <p>
 * Combines the Restaurant update request with optional replacement images.
 * </p>
 *
 * <p>
 * If an image file is not supplied, the existing Restaurant image remains
 * unchanged.
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
public class UpdateRestaurantInputDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Restaurant business update data.
     */
    private RestaurantUpdateRequest restaurantRequest;

    /**
     * Optional replacement logo image.
     */
    private ImageSnapshot logoImage;

    /**
     * Optional replacement cover image.
     */
    private ImageSnapshot coverImage;

}