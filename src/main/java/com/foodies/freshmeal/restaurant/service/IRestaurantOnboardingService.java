package com.foodies.freshmeal.restaurant.service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.restaurant.dto.RestaurantOnboardingInputDTO;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;

/**
 * ============================================================================
 * Service : IRestaurantOnboardingService
 * ============================================================================
 *
 * <p>
 * Defines the service contract for restaurant-owner onboarding.
 * </p>
 *
 * <p>
 * Restaurant onboarding establishes the restaurant business profile and its
 * initial branch for an authenticated FreshMeal user who has the
 * {@code RESTAURANT_OWNER} role.
 * </p>
 *
 * <p>
 * Authentication and identity management remain the responsibility of the
 * authentication and user modules. This service is responsible only for the
 * restaurant-domain onboarding process.
 * </p>
 *
 * <p>
 * The authenticated user's identity must be resolved from the service context
 * rather than being trusted from client-supplied input.
 * </p>
 *
 * @author FreshMeal Development Team
 * @since 1.0
 */
public interface IRestaurantOnboardingService {

    /**
     * Creates a restaurant and its initial branch for the authenticated
     * restaurant owner.
     *
     * <p>
     * The implementation is expected to:
     * </p>
     *
     * <ul>
     * <li>Validate the onboarding input.</li>
     * <li>Resolve the authenticated FreshMeal user.</li>
     * <li>Verify that the user has the required restaurant-owner role.</li>
     * <li>Create and persist the restaurant entity.</li>
     * <li>Associate the restaurant with the authenticated user.</li>
     * <li>Create and persist the initial restaurant branch.</li>
     * </ul>
     *
     * @param input service-layer onboarding input
     * @return service output containing the created restaurant
     */
    IServiceOutput<RestaurantEntity> onboardRestaurant(IServiceInput<RestaurantOnboardingInputDTO> input);
}