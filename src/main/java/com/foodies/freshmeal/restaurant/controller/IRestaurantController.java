package com.foodies.freshmeal.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;

/**
 * ============================================================================
 * Restaurant Controller Contract
 * ============================================================================
 *
 * Defines HTTP operations exposed by the Restaurant module.
 *
 * <p>
 * The controller is responsible only for request handling and delegation.
 * Business logic belongs to the Restaurant service layer.
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
	 * <p>
	 * The request is multipart/form-data and contains:
	 * </p>
	 *
	 * <ul>
	 * <li>restaurant - RestaurantCreateRequest JSON</li>
	 * <li>logoImage - optional restaurant logo</li>
	 * <li>coverImage - optional restaurant cover image</li>
	 * </ul>
	 *
	 * @param restaurantJson restaurant request JSON
	 * @param logoImage      optional logo image
	 * @param coverImage     optional cover image
	 *
	 * @return created restaurant
	 *
	 * @throws JsonProcessingException if restaurant JSON cannot be parsed
	 */
	ResponseEntity<ApiResponse<RestaurantDetailsResponse>> createRestaurant(String restaurantJson,
			MultipartFile logoImage, MultipartFile coverImage) throws JsonProcessingException;

	/**
	 * Retrieves all active restaurants.
	 *
	 * @return active restaurants
	 */
	ResponseEntity<ApiResponse<List<RestaurantListResponse>>> readRestaurants();

	/**
	 * Retrieves a restaurant by identifier.
	 *
	 * @param input restaurant identifier request
	 *
	 * @return restaurant details
	 */
	ResponseEntity<ApiResponse<RestaurantDetailsResponse>> getRestaurantById(@RequestBody RestaurantIdRequest input);

	/**
	 * Updates an existing restaurant.
	 *
	 * <p>
	 * The request is multipart/form-data and contains:
	 * </p>
	 *
	 * <ul>
	 * <li>restaurant - RestaurantUpdateRequest JSON</li>
	 * <li>logoImage - optional replacement logo</li>
	 * <li>coverImage - optional replacement cover image</li>
	 * </ul>
	 *
	 * @param restaurantJson restaurant update JSON
	 * @param logoImage      optional replacement logo
	 * @param coverImage     optional replacement cover image
	 *
	 * @return updated restaurant
	 *
	 * @throws JsonProcessingException if restaurant JSON cannot be parsed
	 */
	ResponseEntity<ApiResponse<RestaurantDetailsResponse>> updateRestaurant(String restaurantJson,
			MultipartFile logoImage, MultipartFile coverImage) throws JsonProcessingException;
}