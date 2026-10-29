
package com.foodies.freshmeal.restaurant.constants;

/**
 * Represents the minimum setup completion state of a restaurant.
 *
 * <p>
 * Onboarding status is independent of restaurant approval,
 * operational status, and availability.
 * </p>
 */
public enum RestaurantOnboardingStatus {

    /**
     * The restaurant has the required initial setup, including
     * at least one active branch.
     */
    COMPLETED,

    /**
     * The restaurant requires additional setup before its
     * operational dashboard can be accessed.
     */
    INCOMPLETE
}
