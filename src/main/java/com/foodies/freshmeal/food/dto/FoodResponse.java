package com.foodies.freshmeal.food.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : FoodResponse
 * ============================================================================
 *
 * <p>
 * Represents the API response for a food item.
 * </p>
 *
 * <p>
 * The response contains food information, restaurant ownership information,
 * lifecycle information and audit information.
 * </p>
 *
 * <p>
 * Display-oriented enum values are represented using
 * {@link DisplayOptionResponse} so that the frontend receives a consistent
 * {@code value}/{@code label} structure.
 * </p>
 *
 * <p>
 * Restaurant ownership is represented using the restaurant business identifier
 * rather than embedding the complete {@code RestaurantEntity} or a
 * {@code RestaurantSnapshot}.
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
public class FoodResponse {

    /**
     * Database identifier of the food item.
     */
    private String id;

    /**
     * Business identifier of the food item.
     */
    private String foodNumber;

    /**
     * Business identifier of the restaurant that owns the food item.
     */
    private String restaurantNumber;
    
    /**
     * Business identifier of the restaurant branch that owns the food item.
     */
    private String restaurantBranchNumber;

    /**
     * Name of the food image.
     */
    private String imageName;

    /**
     * Name of the food item.
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
     * URL of the food image.
     */
    private String imageUrl;

    /**
     * Food categories assigned to the food item.
     */
    private Set<DisplayOptionResponse> foodCategories;

    /**
     * Dietary classification of the food item.
     */
    private DisplayOptionResponse dietCategory;

    /**
     * Cuisine classification of the food item.
     */
    private DisplayOptionResponse cuisineType;

    /**
     * Category groups associated with the food item.
     */
    private Set<DisplayOptionResponse> categoryGroups;

    /**
     * Current lifecycle status of the food item.
     */
    private DisplayOptionResponse foodStatus;

    /**
     * Indicates whether the food item is currently available.
     */
    private boolean isAvailable;

    /**
     * Status transitions currently permitted for the food item.
     */
    private Set<DisplayOptionResponse> allowedStatuses;

    /**
     * Previous lifecycle status of the food item.
     */
    private DisplayOptionResponse previousStatus;

    /**
     * Timestamp when the food status was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * User who last updated the food item.
     */
    private String updatedBy;

    /**
     * User who created the food item.
     */
    private String createdBy;

    /**
     * Timestamp when the food item was created.
     */
    private LocalDateTime createdAt;
}