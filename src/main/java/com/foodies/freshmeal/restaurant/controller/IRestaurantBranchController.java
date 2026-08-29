package com.foodies.freshmeal.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;

/**
 * ============================================================================
 * Restaurant Branch Controller Contract
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Restaurant Branch module.
 *
 * <p>
 * The controller is responsible only for request handling, validation,
 * service-input construction, and delegation to the service layer.
 * Business logic belongs to {@code IRestaurantBranchService}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRestaurantBranchController {

    /**
     * Creates a new restaurant branch.
     *
     * @param input restaurant branch creation request
     *
     * @return created restaurant branch
     */
    ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> createBranch(
            @RequestBody RestaurantBranchCreateRequest input);

    /**
     * Retrieves all active restaurant branches.
     *
     * @return active restaurant branches
     */
    ResponseEntity<ApiResponse<List<RestaurantBranchListResponse>>> readBranches();

    /**
     * Retrieves a restaurant branch by identifier.
     *
     * @param input restaurant branch identifier request
     *
     * @return restaurant branch details
     */
    ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> getBranchById(
            @RequestBody RestaurantBranchIdRequest input);

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * @param input restaurant identifier request
     *
     * @return restaurant branches belonging to the restaurant
     */
    ResponseEntity<ApiResponse<List<RestaurantBranchListResponse>>> getBranchesByRestaurantId(
            @RequestBody RestaurantIdRequest input);

    /**
     * Updates an existing restaurant branch.
     *
     * @param input restaurant branch update request
     *
     * @return updated restaurant branch
     */
    ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> updateBranch(
            @RequestBody RestaurantBranchUpdateRequest input);
}