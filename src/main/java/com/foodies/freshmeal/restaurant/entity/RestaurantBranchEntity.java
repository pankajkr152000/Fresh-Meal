package com.foodies.freshmeal.restaurant.entity;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.restaurant.valueObject.OperatingHours;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Entity : Restaurant Branch
 * ============================================================================
 *
 * Represents a physical operating location of a restaurant.
 *
 * A restaurant may have one or more branches.
 *
 * Restaurant-level business information remains inside RestaurantEntity.
 *
 * Branch-specific physical information such as address, geolocation,
 * operating hours, and branch availability belongs to this entity.
 *
 * ============================================================================
 */
@Getter
@Setter
@Document(collection = "fm_restaurant_branch")
public class RestaurantBranchEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * External/business identifier of the restaurant branch.
     */
    @Indexed(unique = true)
    private String branchNumber;

    /**
     * Internal identifier of the parent restaurant.
     *
     * References RestaurantEntity.id.
     */
    @Indexed
    private String restaurantId;

    /**
     * Business/display name of the branch.
     */
    private String branchName;

    /**
     * Physical address of the branch.
     */
    private Address address;

    /**
     * Regular weekly operating hours of the branch.
     */
    private List<OperatingHours> operatingHours;

    /**
     * Package-private constructor.
     *
     * Entity creation should happen through the factory.
     */
    RestaurantBranchEntity() {
        // Package-private constructor.
    }

    /**
     * Creates a new RestaurantBranchEntity instance.
     *
     * @return new RestaurantBranchEntity instance
     */
    public static IEntity create() {
        return new RestaurantBranchEntity();
    }
}