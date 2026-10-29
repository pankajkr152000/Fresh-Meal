
package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.restaurant.constants.RestaurantOnboardingStatus;

/**
 * Represents the onboarding status of a restaurant owned by the
 * authenticated restaurant owner.
 *
 * <p>
 * This DTO exposes only the information required to display a
 * restaurant in the restaurant-selection and onboarding flow.
 * </p>
 */
public class RestaurantOnboardingStatusDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String restaurantId;

    private String restaurantNumber;

    private String restaurantName;

    private RestaurantOnboardingStatus onboardingStatus;

    public RestaurantOnboardingStatusDTO() {
        // Required for serialization and deserialization.
    }

    public RestaurantOnboardingStatusDTO(
            String restaurantId,
            String restaurantNumber,
            String restaurantName,
            RestaurantOnboardingStatus onboardingStatus) {
        this.restaurantId = restaurantId;
        this.restaurantNumber = restaurantNumber;
        this.restaurantName = restaurantName;
        this.onboardingStatus = onboardingStatus;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantNumber() {
        return restaurantNumber;
    }

    public void setRestaurantNumber(String restaurantNumber) {
        this.restaurantNumber = restaurantNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public RestaurantOnboardingStatus getOnboardingStatus() {
        return onboardingStatus;
    }

    public void setOnboardingStatus(RestaurantOnboardingStatus onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }
}
