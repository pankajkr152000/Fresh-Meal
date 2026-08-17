package com.foodies.freshmeal.restaurant.service;

import java.util.List;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.restaurant.dto.CreateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
import com.foodies.freshmeal.restaurant.dto.UpdateRestaurantInputDTO;

/**
 * ============================================================================
 * Service : Restaurant
 * ============================================================================
 *
 * Defines business operations for restaurant management.
 *
 * ============================================================================
 */
public interface IRestaurantService {

    /**
     * Creates a new restaurant.
     *
     * @param input restaurant creation request
     *
     * @return created restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> create(
            IServiceInput<CreateRestaurantInputDTO> input);

    /**
     * Retrieves a restaurant by its MongoDB identifier.
     *
     * @param input restaurant identifier
     *
     * @return restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> getById(
            IServiceInput<RestaurantIdRequest> input);

    /**
     * Retrieves all active restaurants.
     *
     * @param input service input containing execution context
     *
     * @return restaurant list
     */
    IServiceOutput<List<RestaurantListResponse>> getAll(
            IServiceInput<Void> input);

    /**
     * Updates an existing restaurant.
     *
     * @param input restaurant update request
     *
     * @return updated restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> update(
            IServiceInput<UpdateRestaurantInputDTO> input);

}