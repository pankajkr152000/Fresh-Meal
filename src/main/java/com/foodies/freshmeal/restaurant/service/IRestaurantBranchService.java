package com.foodies.freshmeal.restaurant.service;

import java.util.List;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;

/**
 * ============================================================================
 * Service : Restaurant Branch
 * ============================================================================
 *
 * Defines business operations for restaurant branch management.
 *
 * ============================================================================
 */
public interface IRestaurantBranchService {

    /**
     * Creates a new restaurant branch.
     *
     * @param input branch creation request
     *
     * @return created branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> create(IServiceInput<RestaurantBranchCreateRequest> input);

    /**
     * Retrieves a branch by its identifier.
     *
     * @param input branch identifier request
     *
     * @return branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> getById(IServiceInput<RestaurantBranchIdRequest> input);

    /**
     * Retrieves all active branches.
     *
     * @param input service execution input
     *
     * @return branch list
     */
    IServiceOutput<List<RestaurantBranchListResponse>> getAll(IServiceInput<Void> input);

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * @param input restaurant identifier request
     *
     * @return branches belonging to the restaurant
     */
    IServiceOutput<List<RestaurantBranchListResponse>> getByRestaurantId(IServiceInput<RestaurantIdRequest> input);

    /**
     * Updates an existing restaurant branch.
     *
     * @param input branch update request
     *
     * @return updated branch details
     */
    IServiceOutput<RestaurantBranchDetailsResponse> update(IServiceInput<RestaurantBranchUpdateRequest> input);

}