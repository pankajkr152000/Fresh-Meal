package com.foodies.freshmeal.restaurant.entity;

import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.food.constants.CuisineTypeConstant;
import com.foodies.freshmeal.restaurant.constants.RestaurantStatusConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : Restaurant
 * ============================================================================
 *
 * Represents a restaurant registered on the FreshMeal platform.
 *
 * A restaurant is a business-level entity that owns the food catalog and
 * may operate through one or more physical branches.
 *
 * Restaurant-specific physical information such as:
 *
 * - Address
 * - GeoLocation
 * - Operating Hours
 *
 * belongs to RestaurantBranchEntity.
 *
 * ============================================================================
 */
@Getter
@Setter
@Document(collection = "fm_restaurant")
public class RestaurantEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * External/business identifier of the restaurant.
     *
     * Example:
     *
     * FM-RST-0000001
     *
     * This identifier may be exposed to the frontend, APIs, reports,
     * and other business-facing operations.
     */
    @Indexed(unique = true)
    private String restaurantNumber;

    /**
     * Business/display name of the restaurant.
     */
    private String restaurantName;

    /**
     * Optional description of the restaurant.
     */
    private String description;

    /**
     * Primary contact phone number of the restaurant.
     */
    private PhoneNumber phoneNumber;

    /**
     * Primary contact email address of the restaurant.
     */
    private EmailAddress emailAddress;

    /**
     * Official restaurant website.
     */
    private String website;

    /**
     * Cuisine types offered by the restaurant.
     *
     * Reuses the existing Food module cuisine taxonomy so that
     * FreshMeal maintains a single source of truth for cuisines.
     */
    private Set<CuisineTypeConstant> cuisineTypes;

    /**
     * Current business lifecycle status of the restaurant.
     *
     * Default:
     * ACTIVE
     */
    @Field("status")
    private RestaurantStatusConstant status = RestaurantStatusConstant.ACTIVE;

    /**
     * Indicates whether the restaurant is currently operationally
     * available.
     *
     * This is intentionally separate from restaurant lifecycle status.
     *
     * Example:
     *
     * status = ACTIVE
     * isAvailable = false
     *
     * means the restaurant is an active registered business but is
     * temporarily not accepting operations/orders.
     */
    @Field("restaurant_availability")
    private boolean isAvailable = true;

    /**
     * Reference to the restaurant logo image.
     *
     * Stores the MongoDB identifier of the ImageEntity.
     */
    private String logoImageUrl;
    private String logoImageId;

    /**
     * Reference to the restaurant cover image.
     *
     * Stores the MongoDB identifier of the ImageEntity.
     */
    private String coverImageUrl;
    private String coverImageId;

    /**
     * Package-private constructor.
     *
     * Entity creation should happen through the factory.
     */
    RestaurantEntity() {
        // Package-private constructor.
    }

    /**
     * Creates a new RestaurantEntity instance.
     *
     * @return new RestaurantEntity instance
     */
    public static IEntity create() {
        return new RestaurantEntity();
    }
}