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
 * Combines the food information request with the target restaurant branch
 * and the optional image uploaded for the food item.
 * </p>
 *
 * <p>
 * Restaurant and branch identifiers are service-operation context rather than
 * editable food properties. The service layer must validate that the
 * authenticated user is authorized to create food for the specified
 * restaurant branch before assigning these identifiers to the FoodEntity.
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
     * Restaurant business identifier for which the food is being created.
     */
    private String restaurantNumber;

    /**
     * Restaurant branch business identifier for which the food is being created.
     */
    private String restaurantBranchNumber;

    /**
     * Image uploaded for the food item.
     */
    private MultipartFile imageFile;
}