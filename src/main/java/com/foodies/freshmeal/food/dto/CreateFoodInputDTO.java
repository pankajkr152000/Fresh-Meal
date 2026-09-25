package com.foodies.freshmeal.food.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : CreateFoodInputDTO
 * ============================================================================
 *
 * <p>
 * Service-layer input used when creating a food item.
 * </p>
 *
 * <p>
 * Combines the food information request with the optional image uploaded for
 * the food item.
 * </p>
 *
 * <p>
 * Restaurant ownership is intentionally not part of this DTO. The service
 * layer resolves the restaurant from the authenticated user's context.
 * </p>
 *
 * @author Pankaj Kumar
 *         ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodInputDTO {

    /**
     * Food information supplied by the caller.
     */
    private FoodRequest foodRequest;

    /**
     * Image uploaded for the food item.
     */
    private MultipartFile imageFile;
}