package com.foodies.freshmeal.food.dto;

import java.util.Set;

import com.foodies.freshmeal.food.constants.CategoryGroupConstant;
import com.foodies.freshmeal.food.constants.CuisineTypeConstant;
import com.foodies.freshmeal.food.constants.DietCategoryConstant;
import com.foodies.freshmeal.food.constants.FoodCategoryConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : FoodRequest
 * ============================================================================
 *
 * <p>
 * Represents the food information supplied when creating or updating a food
 * item.
 * </p>
 *
 * <p>
 * This DTO intentionally contains only food-specific editable information.
 * Restaurant ownership is resolved by the backend from the authenticated
 * user's restaurant context and is therefore not accepted from the client.
 * </p>
 *
 * <p>
 * Food identity, lifecycle status, audit information and restaurant ownership
 * are managed by the service/domain layer and are not part of this request.
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
public class FoodRequest {

    /**
     * Display name of the food item.
     */
    private String foodName;

    /**
     * Description of the food item.
     */
    private String description;

    /**
     * Selling price of the food item.
     */
    private double price;

    /**
     * Food categories assigned to the food item.
     */
    private Set<FoodCategoryConstant> foodCategories;

    /**
     * Dietary classification of the food item.
     */
    private DietCategoryConstant dietCategory;

    /**
     * Cuisine classification of the food item.
     */
    private CuisineTypeConstant cuisineType;

    /**
     * Higher-level category groups derived from the selected food categories.
     */
    private Set<CategoryGroupConstant> categoryGroups;
}