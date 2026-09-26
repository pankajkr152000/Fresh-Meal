package com.foodies.freshmeal.food.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.food.entity.FoodEntity;

/**
 * ============================================================================
 * Repository : IFoodRepository
 * ============================================================================
 *
 * <p>
 * Repository contract for {@link FoodEntity} persistence operations.
 * </p>
 *
 * <p>
 * Food records are scoped by both restaurant and restaurant branch because
 * food configuration such as price, availability and menu presence may differ
 * from one branch to another.
 * </p>
 *
 * <p>
 * Soft-delete behavior is inherited from {@link IBaseRepository}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Repository
public interface IFoodRepository
        extends IBaseRepository<FoodEntity, String> {

    // =========================================================================
    // Navigation Queries
    // =========================================================================

    /**
     * Retrieves the immediate previous Food within the same restaurant branch.
     *
     * <p>
     * Navigation is intentionally scoped by both restaurant number and
     * restaurant branch number so that a Food from one branch can never
     * navigate into another branch's menu.
     * </p>
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * Restaurant : FM-RST-0000001
     * Branch     : FM-BRN-0000001
     *
     * Current : FOD01_00031
     * Returns : FOD01_00030
     * </pre>
     *
     * @param restaurantNumber       Restaurant business identifier.
     * @param restaurantBranchNumber Restaurant branch business identifier.
     * @param id                     Current Food database identifier.
     *
     * @return Previous Food within the same restaurant branch if present.
     */
    Optional<FoodEntity> findFirstByRestaurantNumberAndRestaurantBranchNumberAndIdLessThanOrderByIdDesc(
            String restaurantNumber,
            String restaurantBranchNumber,
            String id);

    /**
     * Retrieves the immediate next Food within the same restaurant branch.
     *
     * <p>
     * Navigation is intentionally scoped by both restaurant number and
     * restaurant branch number so that a Food from one branch can never
     * navigate into another branch's menu.
     * </p>
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * Restaurant : FM-RST-0000001
     * Branch     : FM-BRN-0000001
     *
     * Current : FOD01_00031
     * Returns : FOD01_00032
     * </pre>
     *
     * @param restaurantNumber       Restaurant business identifier.
     * @param restaurantBranchNumber Restaurant branch business identifier.
     * @param id                     Current Food database identifier.
     *
     * @return Next Food within the same restaurant branch if present.
     */
    Optional<FoodEntity> findFirstByRestaurantNumberAndRestaurantBranchNumberAndIdGreaterThanOrderByIdAsc(
            String restaurantNumber,
            String restaurantBranchNumber,
            String id);
}
