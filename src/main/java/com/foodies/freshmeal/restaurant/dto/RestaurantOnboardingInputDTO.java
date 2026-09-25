package com.foodies.freshmeal.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : RestaurantOnboardingInputDTO
 * ============================================================================
 *
 * <p>
 * Service-layer input DTO for restaurant-owner onboarding.
 * </p>
 *
 * <p>
 * This DTO acts as the boundary between the application/service layer and the
 * restaurant onboarding API contract. The HTTP request DTO is intentionally
 * wrapped instead of being passed directly into the service layer.
 * </p>
 *
 * <p>
 * Restaurant onboarding is responsible for establishing the restaurant
 * business profile and its initial branch after the common FreshMeal user
 * identity has been registered and verified.
 * </p>
 *
 * <p>
 * System-controlled values such as restaurant number, owner user number,
 * restaurant status, availability, audit information, and branch identifiers
 * are deliberately excluded. These values must be established by the service
 * and domain layers.
 * </p>
 *
 * @author FreshMeal Development Team
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOnboardingInputDTO {

    /**
     * Restaurant owner onboarding request containing the restaurant profile
     * and initial branch information.
     */
    private RestaurantOwnerRegistrationRequest registrationRequest;
}