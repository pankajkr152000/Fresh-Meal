package com.foodies.freshmeal.restaurant.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.restaurant.constants.BranchStatusConstant;
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
     * Current business status of the branch.
     *
     * Default:
     * ACTIVE
     */
    @Field("status")
    private BranchStatusConstant status = BranchStatusConstant.ACTIVE;

    /**
     * Timestamp of the most recent branch status change.
     */
    private LocalDateTime statusUpdatedAt;

    /**
     * Administrator username who last changed the branch status.
     */
    private String statusUpdatedBy;

    /**
     * Indicates whether the branch is currently operationally available.
     *
     * This is intentionally separate from branch lifecycle status.
     */
    @Field("branch_availability")
    private boolean isAvailable = true;

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