package com.foodies.freshmeal.restaurant.service;

import java.util.List;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.restaurant.dto.CreateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantAvailabilityUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantStatusUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.UpdateRestaurantInputDTO;

/**
 * ============================================================================
 * Service : Restaurant
 * ============================================================================
 *
 * Defines business operations for restaurant management.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRestaurantService {

    /**
     * Creates a new restaurant.
     *
     * @param input restaurant creation request
     * @return created restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> create(
            IServiceInput<CreateRestaurantInputDTO> input);

    /**
     * Retrieves an active restaurant by its MongoDB identifier.
     *
     * @param input restaurant identifier
     * @return restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> getById(
            IServiceInput<RestaurantIdRequest> input);

    /**
     * Retrieves all active restaurants.
     *
     * @param input service input containing execution context
     * @return active restaurant list
     */
    IServiceOutput<List<RestaurantListResponse>> getAll(
            IServiceInput<Void> input);

    /**
     * Updates an existing restaurant.
     *
     * @param input restaurant update request
     * @return updated restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> update(
            IServiceInput<UpdateRestaurantInputDTO> input);

    /**
     * Updates the lifecycle status of a restaurant.
     *
     * @param input restaurant status update input
     * @return updated restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> updateStatus(
            IServiceInput<RestaurantStatusUpdateRequest> input);

    /**
     * Updates the operational availability of a restaurant.
     *
     * @param input restaurant availability update request
     * @return updated restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> updateAvailability(
            IServiceInput<RestaurantAvailabilityUpdateRequest> input);

    /**
     * Soft-deletes an active restaurant.
     *
     * <p>
     * The restaurant remains physically stored and can be restored through the
     * appropriate lifecycle operation.
     * </p>
     *
     * @param input restaurant identifier
     * @return archived restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> archive(
            IServiceInput<RestaurantIdRequest> input);

    /**
     * Restores a previously archived restaurant.
     *
     * @param input restaurant identifier
     * @return restored restaurant details
     */
    IServiceOutput<RestaurantDetailsResponse> restore(
            IServiceInput<RestaurantIdRequest> input);

    /**
     * Retrieves archived restaurants.
     *
     * @param input service execution input
     * @return archived restaurant list
     */
    IServiceOutput<List<RestaurantListResponse>> getArchived(
            IServiceInput<Void> input);

    /**
     * Permanently deletes a restaurant.
     *
     * <p>
     * This operation is irreversible and should be restricted to the
     * appropriate administrative authorization layer.
     * </p>
     *
     * @param input restaurant identifier
     */
    void deletePermanently(
            IServiceInput<RestaurantIdRequest> input);
}