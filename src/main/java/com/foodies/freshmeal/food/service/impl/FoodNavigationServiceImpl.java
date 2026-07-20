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
 * Purpose:
 * Provides Previous and Next navigation support for Food entities.
 *
 * Responsibilities:
 *
 * 1. Retrieve previous food.
 * 2. Retrieve next food.
 * 3. Convert entities into navigation DTOs.
 * 4. Build EntityNavigation response.
 *
 * Future Scope:
 *
 * This implementation serves as the reference implementation for
 * Restaurant, Category, User, Order and Offer navigation services.
 *
 * ============================================================================
 */

@Service
//@RequiredArgsConstructor
public class FoodNavigationServiceImpl implements IFoodNavigationService {

    // =========================================================================
    // Logger
    // =========================================================================

    private static final Logger LOGGER =
            LoggerFactory.getLogger(FoodNavigationServiceImpl.class);

    // =========================================================================
    // Dependencies
    // =========================================================================

    private final IFoodRepository foodRepository;
    
    public FoodNavigationServiceImpl(IFoodRepository foodRepository) {
		this.foodRepository = foodRepository;
    	
    }
    // =========================================================================
    // Public Methods
    // =========================================================================

    /**
     * Builds navigation for the supplied Food Id.
     *
     * @param currentFoodId Current Food Id.
     * @return EntityNavigation
     */
    @Override
    public EntityNavigation getNavigation(String currentFoodId) {

        LOGGER.info("Building navigation for Food Id : {}", currentFoodId);

        EntityNavigation navigation = EntityNavigation.builder()
                .previous(getPreviousNavigation(currentFoodId))
                .next(getNextNavigation(currentFoodId))
                .build();

        LOGGER.info("Navigation generated successfully for Food Id : {}", currentFoodId);

        return navigation;
    }

    // =========================================================================
    // Private Methods
    // =========================================================================

    /**
     * Retrieves previous food navigation.
     *
     * @param currentFoodId Current Food Id.
     * @return NavigationItem
     */
    private NavigationItem getPreviousNavigation(String currentFoodId) {

        LOGGER.debug("Searching previous food for Food Id : {}", currentFoodId);

        Optional<FoodEntity> previousFood =
                foodRepository.findFirstByIdLessThanOrderByIdDesc(currentFoodId);

        return mapNavigation(previousFood);
    }

    /**
     * Retrieves next food navigation.
     *
     * @param currentFoodId Current Food Id.
     * @return NavigationItem
     */
    private NavigationItem getNextNavigation(String currentFoodId) {

        LOGGER.debug("Searching next food for Food Id : {}", currentFoodId);

        Optional<FoodEntity> nextFood =
                foodRepository.findFirstByIdGreaterThanOrderByIdAsc(currentFoodId);

        return mapNavigation(nextFood);
    }

    /**
     * Maps FoodEntity into NavigationItem.
     *
     * @param entity Optional FoodEntity.
     * @return NavigationItem
     */
    private NavigationItem mapNavigation(Optional<FoodEntity> entity) {

        if (entity.isEmpty()) {

            LOGGER.debug("Navigation target not found.");

            return NavigationMapper.unavailable();
        }

        FoodEntity food = entity.get();

        LOGGER.debug("Navigation target found : {}", food.getId());

        return NavigationMapper.available(
                food.getId(),
                food.getFoodName());
    }

}