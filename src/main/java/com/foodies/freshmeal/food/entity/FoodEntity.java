package com.foodies.freshmeal.food.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.food.constants.CategoryGroupConstant;
import com.foodies.freshmeal.food.constants.CuisineTypeConstant;
import com.foodies.freshmeal.food.constants.DietCategoryConstant;
import com.foodies.freshmeal.food.constants.FoodCategoryConstant;
import com.foodies.freshmeal.food.constants.FoodStatusConstant;
import com.foodies.freshmeal.image.dto.ImageSnapshot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "fm_food")
public class FoodEntity extends ABaseEntity {
    private static final long serialVersionUID = 9030570160895262180L;

    /**
     * External/business identifier of the food.
     *
     * <p>
     * This identifier is unique and may be exposed to the frontend,
     * URLs, reports, and other business-facing operations.
     * </p>
     */
    @Indexed(unique = true)
    private String foodNumber;
    private ImageSnapshot foodImage;
    private String foodName;
    private String description;
    private double price;
    private Set<FoodCategoryConstant> foodCategories;
    private DietCategoryConstant dietCategory; // veg or non-veg
    private CuisineTypeConstant cuisineType;
    private Set<CategoryGroupConstant> categoryGroups;
    /**
     * Current lifecycle status of the food.
     *
     * Default:
     * AVAILABLE
     */
    /**
     * ============================================================================
     * Food Lifecycle Status
     * ============================================================================
     *
     * This field determines whether a food item is available for ordering
     * and its current lifecycle stage.
     *
     * IMPORTANT
     * ---------
     * Never delete food records simply because they are no longer sold.
     *
     * Instead:
     *
     * AVAILABLE
     * OUT_OF_STOCK
     * DISABLED
     * DISCONTINUED
     *
     * preserve historical order data and reporting.
     *
     * All status modifications must go through the dedicated
     * status update API to ensure:
     *
     * • Validation
     * • Audit logging
     * • Security checks
     * • Status transition rules
     *
     * ============================================================================
     */

    @Field("status")
    private FoodStatusConstant status = FoodStatusConstant.AVAILABLE;

    /**
     * Timestamp of the most recent status change.
     */
    private LocalDateTime statusUpdatedAt;

    /**
     * Administrator username who last changed the status.
     */
    private String statusUpdatedBy;

    @Field("food_availability")
    private boolean isAvailable = true;

    FoodEntity() {
        // Package-private constructor.
        // Entity creation should happen only through factory.
    }

    public static IEntity create() {
        return new FoodEntity();
    }

}
