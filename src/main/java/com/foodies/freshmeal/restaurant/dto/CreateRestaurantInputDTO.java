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
 * DTO : CreateRestaurantInputDTO
 * ============================================================================
 *
 * Internal service input used during Restaurant creation.
 *
 * <p>
 * This DTO combines the Restaurant business request with the optional image
 * files received through multipart/form-data.
 * </p>
 *
 * <p>
 * Image files are intentionally kept outside RestaurantCreateRequest because
 * they are transport-level multipart data rather than Restaurant business
 * attributes.
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
public class CreateRestaurantInputDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Restaurant business data.
     */
    private RestaurantCreateRequest restaurantRequest;

    /**
     * Optional Restaurant logo image.
     */
    private ImageSnapshot logoImage;

    /**
     * Optional Restaurant cover image.
     */
    private ImageSnapshot coverImage;

}