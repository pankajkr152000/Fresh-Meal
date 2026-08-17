package com.foodies.freshmeal.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantUpdateRequest;

/**
 * ============================================================================
 * Restaurant Controller
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Restaurant module.
 *
 * <p>
 * The controller is responsible only for receiving HTTP requests and
 * delegating operations to the Restaurant service layer.
 * </p>
 *
 * <p>
 * Business logic must remain inside the service layer.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRestaurantController {

    /**
     * Creates a new restaurant.
     *
     * @param input restaurant creation request
     *
     * @return created restaurant
     */
    ResponseEntity<ApiResponse<RestaurantDetailsResponse>> createRestaurant(
            RestaurantCreateRequest input);

    /**
     * Retrieves all active restaurants.
     *
     * @return restaurant list
     */
    ResponseEntity<ApiResponse<List<RestaurantListResponse>>> readRestaurants();

    /**
     * Retrieves a restaurant by its identifier.
     *
     * @param input restaurant identifier request
     *
     * @return restaurant details
     */
    ResponseEntity<ApiResponse<RestaurantDetailsResponse>> getRestaurantById(
            RestaurantIdRequest input);

    /**
     * Updates an existing restaurant.
     *
     * @param input restaurant update request
     *
     * @return updated restaurant
     */
    ResponseEntity<ApiResponse<RestaurantDetailsResponse>> updateRestaurant(
            RestaurantUpdateRequest input);

}