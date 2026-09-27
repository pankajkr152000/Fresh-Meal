package com.foodies.freshmeal.restaurant.service.impl;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.food.repository.IFoodRepository;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;
import com.foodies.freshmeal.restaurant.repository.IRestaurantBranchRepository;
import com.foodies.freshmeal.restaurant.service.IRestaurantLifecycleService;

/**
 * ============================================================================
 * Service Implementation : Restaurant Lifecycle
 * ============================================================================
 *
 * <p>
 * Coordinates persistence lifecycle operations across the Restaurant hierarchy.
 * </p>
 *
 * <pre>
 * Restaurant
 *     |
 *     +-- Restaurant Branch
 *             |
 *             +-- Food
 * </pre>
 *
 * <h3>Lifecycle Rules</h3>
 *
 * <ul>
 * <li>Archiving a Restaurant archives all currently active Branches.</li>
 * <li>Archiving a Branch archives all currently active Foods.</li>
 * <li>Archiving a Restaurant therefore cascades through Branches to Foods.</li>
 * <li>Restoration does not automatically restore child entities.</li>
 * <li>Permanent deletion removes archived descendants before the parent.</li>
 * <li>Entities already archived independently remain archived.</li>
 * </ul>
 *
 * <p>
 * This class intentionally operates directly on repository lifecycle methods.
 * It does not invoke the normal Restaurant, Branch, or Food business services,
 * thereby preventing circular service dependencies and avoiding construction of
 * API-oriented request DTOs for internal lifecycle operations.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class RestaurantLifecycleServiceImpl
        implements IRestaurantLifecycleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantLifecycleServiceImpl.class);

    private final IRestaurantBranchRepository restaurantBranchRepository;

    private final IFoodRepository foodRepository;

    /**
     * Creates RestaurantLifecycleServiceImpl.
     *
     * @param restaurantBranchRepository restaurant branch repository
     * @param foodRepository             food repository
     */
    public RestaurantLifecycleServiceImpl(
            IRestaurantBranchRepository restaurantBranchRepository,
            IFoodRepository foodRepository) {

        this.restaurantBranchRepository = restaurantBranchRepository;
        this.foodRepository = foodRepository;
    }

    // =========================================================================
    // Restaurant Archive Cascade
    // =========================================================================

    /**
     * Archives a Restaurant and its currently active child hierarchy.
     *
     * <p>
     * The operation follows:
     * </p>
     *
     * <pre>
     * Restaurant
     *     ↓
     * Active Branches
     *     ↓
     * Active Foods
     * </pre>
     *
     * <p>
     * Children that are already archived are intentionally ignored.
     * </p>
     *
     * @param restaurant     restaurant being archived
     * @param serviceContext current service execution context
     */
    @Override
    public void archiveRestaurantHierarchy(
            final RestaurantEntity restaurant,
            final IServiceContext serviceContext) {

        Objects.requireNonNull(
                restaurant,
                "Restaurant entity must not be null.");

        Objects.requireNonNull(
                restaurant.getId(),
                "Restaurant id must not be null.");

        Objects.requireNonNull(
                restaurant.getRestaurantNumber(),
                "Restaurant number must not be null.");

        @SuppressWarnings("unused")
        final RepositoryContext repositoryContext = createRepositoryContext(serviceContext);

        final Query branchQuery = Query.query(
                Criteria.where("restaurantId")
                        .is(restaurant.getId()));

        final List<RestaurantBranchEntity> branches = restaurantBranchRepository.findAll(branchQuery);

        LOGGER.info(
                "Starting Restaurant archive cascade. restaurantId=[{}], restaurantNumber=[{}], branchCount=[{}]",
                restaurant.getId(),
                restaurant.getRestaurantNumber(),
                branches.size());

        for (RestaurantBranchEntity branch : branches) {

            archiveBranchHierarchy(
                    branch,
                    serviceContext);
        }

        LOGGER.info(
                "Restaurant archive cascade completed. restaurantId=[{}], restaurantNumber=[{}]",
                restaurant.getId(),
                restaurant.getRestaurantNumber());
    }

    // =========================================================================
    // Restaurant Permanent Delete Cascade
    // =========================================================================

    /**
     * Permanently deletes an archived Restaurant and its archived child
     * hierarchy.
     *
     * <p>
     * Descendants are permanently deleted before the Restaurant itself is
     * permanently deleted by the caller.
     * </p>
     *
     * <p>
     * This method intentionally does not permanently delete the Restaurant
     * itself. The owning Restaurant service remains responsible for deleting
     * the Restaurant after this method completes successfully.
     * </p>
     *
     * @param restaurant     archived restaurant
     * @param serviceContext current service execution context
     */
    @Override
    public void deleteRestaurantHierarchyPermanently(
            final RestaurantEntity restaurant,
            final IServiceContext serviceContext) {

        Objects.requireNonNull(
                restaurant,
                "Restaurant entity must not be null.");

        Objects.requireNonNull(
                restaurant.getId(),
                "Restaurant id must not be null.");

        Objects.requireNonNull(
                restaurant.getRestaurantNumber(),
                "Restaurant number must not be null.");

        final Query branchQuery = Query.query(
                Criteria.where("restaurantId")
                        .is(restaurant.getId()));

        /*
         * findAllDeleted() is intentionally used here because permanent deletion
         * is only allowed for already archived descendants.
         */
        final List<RestaurantBranchEntity> branches = restaurantBranchRepository.findAllDeleted(branchQuery);

        LOGGER.info(
                "Starting Restaurant permanent-delete cascade. restaurantId=[{}], restaurantNumber=[{}], archivedBranchCount=[{}]",
                restaurant.getId(),
                restaurant.getRestaurantNumber(),
                branches.size());

        for (RestaurantBranchEntity branch : branches) {

            deleteBranchHierarchyPermanently(
                    branch,
                    serviceContext);
        }

        /*
         * Defensive check:
         *
         * A Restaurant must not be permanently deleted while an active Branch
         * still references it.
         */
        final List<RestaurantBranchEntity> activeBranches = restaurantBranchRepository.findAll(branchQuery);

        if (!activeBranches.isEmpty()) {

            throw new IllegalStateException(
                    "Restaurant cannot be permanently deleted while active branches still exist.");
        }

        LOGGER.info(
                "Restaurant permanent-delete cascade completed. restaurantId=[{}], restaurantNumber=[{}]",
                restaurant.getId(),
                restaurant.getRestaurantNumber());
    }

    // =========================================================================
    // Branch Archive Cascade
    // =========================================================================

    /**
     * Archives a Restaurant Branch and all currently active Foods belonging to
     * the Branch.
     *
     * <p>
     * Food ownership is resolved using the persisted Restaurant Branch business
     * number. The Restaurant identifier is not supplied by the caller.
     * </p>
     *
     * @param branch         branch being archived
     * @param serviceContext current service execution context
     */
    @Override
    public void archiveBranchHierarchy(
            final RestaurantBranchEntity branch,
            final IServiceContext serviceContext) {

        Objects.requireNonNull(
                branch,
                "Restaurant branch entity must not be null.");

        Objects.requireNonNull(
                branch.getId(),
                "Restaurant branch id must not be null.");

        Objects.requireNonNull(
                branch.getBranchNumber(),
                "Restaurant branch number must not be null.");

        final Query foodQuery = Query.query(
                Criteria.where("restaurantBranchNumber")
                        .is(branch.getBranchNumber()));

        final List<FoodEntity> foods = foodRepository.findAll(foodQuery);

        LOGGER.info(
                "Starting Restaurant Branch archive cascade. branchId=[{}], branchNumber=[{}], activeFoodCount=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                foods.size());

        final RepositoryContext repositoryContext = createRepositoryContext(serviceContext);

        /*
         * Archive only active Foods.
         *
         * findAll(Query) uses the repository's active-query infrastructure,
         * therefore independently archived Foods are not returned here.
         */
        for (FoodEntity food : foods) {

            foodRepository.softDelete(
                    food.getId(),
                    repositoryContext);

            LOGGER.debug(
                    "Food archived through Branch lifecycle cascade. foodId=[{}], foodNumber=[{}], branchNumber=[{}]",
                    food.getId(),
                    food.getFoodNumber(),
                    branch.getBranchNumber());
        }

        /*
         * Archive the Branch itself.
         */
        restaurantBranchRepository.softDelete(
                branch.getId(),
                repositoryContext);

        LOGGER.info(
                "Restaurant Branch archive cascade completed. branchId=[{}], branchNumber=[{}], foodCount=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                foods.size());
    }

    // =========================================================================
    // Branch Permanent Delete Cascade
    // =========================================================================

    /**
     * Permanently deletes an archived Restaurant Branch and its archived Foods.
     *
     * <p>
     * Archived Foods are permanently deleted before the Branch itself is
     * permanently deleted by the caller.
     * </p>
     *
     * <p>
     * This method intentionally does not permanently delete the Branch itself.
     * The owning Branch service remains responsible for that operation.
     * </p>
     *
     * @param branch         archived branch
     * @param serviceContext current service execution context
     */
    @Override
    public void deleteBranchHierarchyPermanently(
            final RestaurantBranchEntity branch,
            final IServiceContext serviceContext) {

        Objects.requireNonNull(
                branch,
                "Restaurant branch entity must not be null.");

        Objects.requireNonNull(
                branch.getId(),
                "Restaurant branch id must not be null.");

        Objects.requireNonNull(
                branch.getBranchNumber(),
                "Restaurant branch number must not be null.");

        final Query foodQuery = Query.query(
                Criteria.where("restaurantBranchNumber")
                        .is(branch.getBranchNumber()));

        final List<FoodEntity> foods = foodRepository.findAllDeleted(foodQuery);

        LOGGER.info(
                "Starting Restaurant Branch permanent-delete cascade. branchId=[{}], branchNumber=[{}], archivedFoodCount=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                foods.size());

        for (FoodEntity food : foods) {

            foodRepository.deletePermanently(
                    food.getId());

            LOGGER.debug(
                    "Food permanently deleted through Branch lifecycle cascade. foodId=[{}], foodNumber=[{}], branchNumber=[{}]",
                    food.getId(),
                    food.getFoodNumber(),
                    branch.getBranchNumber());
        }

        /*
         * Defensive check:
         *
         * A Branch must not be permanently deleted while an active Food still
         * references it.
         */
        final List<FoodEntity> activeFoods = foodRepository.findAll(foodQuery);

        if (!activeFoods.isEmpty()) {

            throw new IllegalStateException(
                    "Restaurant branch cannot be permanently deleted while active foods still exist.");
        }

        /*
         * The Branch itself is deleted by RestaurantBranchServiceImpl after this
         * method completes successfully.
         */
        LOGGER.info(
                "Restaurant Branch permanent-delete cascade completed. branchId=[{}], branchNumber=[{}]",
                branch.getId(),
                branch.getBranchNumber());
    }

    // =========================================================================
    // Repository Context
    // =========================================================================

    /**
     * Creates the repository context used by lifecycle operations.
     *
     * <p>
     * The authenticated username is used when available. System execution is
     * represented by the configured system user.
     * </p>
     *
     * @param serviceContext current service context
     * @return repository context
     */
    private RepositoryContext createRepositoryContext(
            final IServiceContext serviceContext) {

        if (serviceContext != null
                && serviceContext.getUserProfile() != null) {

            return RepositoryContext.of(
                    serviceContext.getUserProfile().getUsername(),
                    AppCalendar.getBusinessLocalDateTime());
        }

        return RepositoryContext.of(
                RepositoryConstants.SYSTEM_USER,
                AppCalendar.getBusinessLocalDateTime());
    }
}