package com.foodies.freshmeal.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.food.dto.ArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.BulkRestoreFoodRequest;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.PermanentDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.RestoreFoodRequest;
import com.foodies.freshmeal.food.dto.UpdateFoodStatusRequest;

public interface IFoodController {

    ResponseEntity<ApiResponse<FoodResponse>> addFood(String foodJson, MultipartFile imageFile)
            throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> foodCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> dietCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> cuisineCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> groupCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<EntityViewResponse<FoodResponse>>> getFoodByFoodId(FoodStatusRequest foodId)
            throws JsonProcessingException;

    ResponseEntity<ApiResponse<FoodMetadataResponse>> foodCategoryMetadata() throws JsonProcessingException;

    ResponseEntity<ApiResponse<FoodResponse>> updateFoodStatus(String foodId, UpdateFoodStatusRequest updateRequest)
            throws JsonProcessingException;

    ResponseEntity<ApiResponse<FoodResponse>> editFood(String foodJson, MultipartFile imageFile)
            throws JsonProcessingException;

    // ResponseEntity<ApiResponse<FoodResponse>> getFoodByFoodId(String foodId)
    // throws JsonProcessingException;

	// ============================================================================
	// Archive Operations
	// ============================================================================

	/**
	 * Archives a food item.
	 *
	 * @param input Archive food request.
	 *
	 * @return Archived food response.
	 */
	ResponseEntity<ApiResponse<FoodResponse>> archiveFood(@RequestBody ArchiveFoodRequest input);

	/**
	 * Archives multiple food items.
	 *
	 * @param input Bulk archive request.
	 *
	 * @return Success response.
	 */
	ResponseEntity<ApiResponse<Void>> bulkArchiveFoods(@RequestBody BulkArchiveFoodRequest input);

	// ============================================================================
	// Restore Operations
	// ============================================================================

	/**
	 * Restores an archived food item.
	 *
	 * @param input Restore food request.
	 *
	 * @return Restored food response.
	 */
	ResponseEntity<ApiResponse<FoodResponse>> restoreFood(@RequestBody RestoreFoodRequest input);

	/**
	 * Restores multiple archived food items.
	 *
	 * @param input Bulk restore request.
	 *
	 * @return Success response.
	 */
	ResponseEntity<ApiResponse<Void>> bulkRestoreFoods(@RequestBody BulkRestoreFoodRequest input);

	// ============================================================================
	// Permanent Delete Operations
	// ============================================================================

	/**
	 * Permanently deletes an archived food item.
	 *
	 * @param input Permanent delete request.
	 *
	 * @return Success response.
	 */
	ResponseEntity<ApiResponse<Void>> permanentDeleteFood(@RequestBody PermanentDeleteFoodRequest input);

	/**
	 * Permanently deletes multiple archived food items.
	 *
	 * @param input Bulk permanent delete request.
	 *
	 * @return Success response.
	 */
	ResponseEntity<ApiResponse<Void>> bulkPermanentDeleteFoods(@RequestBody BulkDeleteFoodRequest input);

	// ============================================================================
	// Archived Food Operations
	// ============================================================================

	/**
	 * Retrieves all archived food items.
	 *
	 * @return Archived food list.
	 */
	ResponseEntity<ApiResponse<List<FoodResponse>>> readArchivedFoods();

}
