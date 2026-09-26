package com.foodies.freshmeal.food.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.dto.view.EntityNavigation;
import com.foodies.freshmeal.common.dto.view.NavigationItem;
import com.foodies.freshmeal.common.mapper.NavigationMapper;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.food.repository.IFoodRepository;
import com.foodies.freshmeal.food.service.IFoodNavigationService;

/**
 * ============================================================================
 * Class : FoodNavigationServiceImpl
 * ============================================================================
 *
 * <p>
 * Provides Previous and Next navigation support for Food entities.
 * </p>
 *
 * <p>
 * Navigation is scoped to the persisted restaurant and restaurant branch of
 * the current Food. This ensures that navigation never crosses restaurant or
 * branch boundaries.
 * </p>
 *
 * <p>
 * The current Food entity is the source of truth for navigation scope.
 * Restaurant and branch identifiers are therefore never accepted as
 * client-supplied navigation parameters.
 * </p>
 *
 * <p>
 * Responsibilities:
 * </p>
 *
 * <ul>
 * <li>Load the current Food entity.</li>
 * <li>Resolve its persisted restaurant and branch context.</li>
 * <li>Retrieve the immediate previous Food.</li>
 * <li>Retrieve the immediate next Food.</li>
 * <li>Convert Food entities into navigation DTOs.</li>
 * <li>Build the EntityNavigation response.</li>
 * </ul>
 *
 * <p>
 * This implementation serves as the reference implementation for future
 * navigation services such as Restaurant, Category, User, Order and Offer.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class FoodNavigationServiceImpl implements IFoodNavigationService {

    // =========================================================================
    // Logger
    // =========================================================================

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodNavigationServiceImpl.class);

    // =========================================================================
    // Dependencies
    // =========================================================================

    private final IFoodRepository foodRepository;

    /**
     * Creates the Food navigation service.
     *
     * @param foodRepository Food repository.
     */
    public FoodNavigationServiceImpl(
            final IFoodRepository foodRepository) {

        this.foodRepository = foodRepository;
    }

    // =========================================================================
    // Public Methods
    // =========================================================================

    /**
     * Builds previous and next navigation for the supplied Food identifier.
     *
     * <p>
     * The current Food is loaded first so that its persisted restaurant and
     * restaurant branch identifiers can be used as the navigation scope.
     * </p>
     *
     * @param currentFoodId Current Food database identifier.
     * @return EntityNavigation containing previous and next Food navigation.
     */
    @Override
    public EntityNavigation getNavigation(
            final String currentFoodId) {

        LOGGER.info(
                "Building navigation for Food Id : {}",
                currentFoodId);

        final FoodEntity currentFood = foodRepository.findActiveById(currentFoodId)
                .orElse(null);

        if (currentFood == null) {

            LOGGER.warn(
                    "Current Food not found. Navigation unavailable. foodId={}",
                    currentFoodId);

            return EntityNavigation.builder()
                    .previous(NavigationMapper.unavailable())
                    .next(NavigationMapper.unavailable())
                    .build();
        }

        final String restaurantNumber = currentFood.getRestaurantNumber();

        final String restaurantBranchNumber = currentFood.getRestaurantBranchNumber();

        final EntityNavigation navigation = EntityNavigation.builder()
                .previous(
                        getPreviousNavigation(
                                restaurantNumber,
                                restaurantBranchNumber,
                                currentFood.getId()))
                .next(
                        getNextNavigation(
                                restaurantNumber,
                                restaurantBranchNumber,
                                currentFood.getId()))
                .build();

        LOGGER.info(
                "Navigation generated successfully for Food Id : {}",
                currentFoodId);

        return navigation;
    }

    // =========================================================================
    // Private Methods
    // =========================================================================

    /**
     * Retrieves the immediate previous Food within the same restaurant branch.
     *
     * @param restaurantNumber       Restaurant business identifier.
     * @param restaurantBranchNumber Restaurant branch business identifier.
     * @param currentFoodId          Current Food database identifier.
     * @return Previous Food navigation item.
     */
    private NavigationItem getPreviousNavigation(
            final String restaurantNumber,
            final String restaurantBranchNumber,
            final String currentFoodId) {

        LOGGER.debug(
                "Searching previous Food. foodId={}, restaurantNumber={}, restaurantBranchNumber={}",
                currentFoodId,
                restaurantNumber,
                restaurantBranchNumber);

        final Optional<FoodEntity> previousFood = foodRepository
                .findFirstByRestaurantNumberAndRestaurantBranchNumberAndIdLessThanOrderByIdDesc(
                        restaurantNumber,
                        restaurantBranchNumber,
                        currentFoodId);

        return mapNavigation(previousFood);
    }

    /**
     * Retrieves the immediate next Food within the same restaurant branch.
     *
     * @param restaurantNumber       Restaurant business identifier.
     * @param restaurantBranchNumber Restaurant branch business identifier.
     * @param currentFoodId          Current Food database identifier.
     * @return Next Food navigation item.
     */
    private NavigationItem getNextNavigation(
            final String restaurantNumber,
            final String restaurantBranchNumber,
            final String currentFoodId) {

        LOGGER.debug(
                "Searching next Food. foodId={}, restaurantNumber={}, restaurantBranchNumber={}",
                currentFoodId,
                restaurantNumber,
                restaurantBranchNumber);

        final Optional<FoodEntity> nextFood = foodRepository
                .findFirstByRestaurantNumberAndRestaurantBranchNumberAndIdGreaterThanOrderByIdAsc(
                        restaurantNumber,
                        restaurantBranchNumber,
                        currentFoodId);

        return mapNavigation(nextFood);
    }

    /**
     * Maps a Food entity into a navigation item.
     *
     * @param entity Optional Food entity.
     * @return Available navigation item when the Food exists; otherwise an
     *         unavailable navigation item.
     */
    private NavigationItem mapNavigation(
            final Optional<FoodEntity> entity) {

        if (entity.isEmpty()) {

            LOGGER.debug(
                    "Navigation target not found.");

            return NavigationMapper.unavailable();
        }

        final FoodEntity food = entity.get();

        LOGGER.debug(
                "Navigation target found : {}",
                food.getId());

        return NavigationMapper.available(
                food.getId(),
                food.getFoodName());
    }
}