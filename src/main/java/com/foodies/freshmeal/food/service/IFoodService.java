package com.foodies.freshmeal.food.service;

import java.util.List;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.food.dto.ArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.BulkRestoreFoodRequest;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodIdRequest;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.PermanentDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.RestoreFoodRequest;
import com.foodies.freshmeal.food.entity.FoodEntity;

public interface IFoodService {

    IServiceOutput<FoodEntity> loadFood(IServiceInput<FoodIdRequest> input);

    IServiceOutput<FoodEntity> createFoodEntity(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<String> generateFoodId(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<FoodResponse> addFood(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<FoodResponse> editFood(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<List<FoodResponse>> readFoods(IServiceInput<Void> input);

    IServiceOutput<List<DisplayOptionResponse>> getFoodCategories(IServiceInput<Void> input);

    IServiceOutput<List<DisplayOptionResponse>> getDietCategories(IServiceInput<Void> input);

    IServiceOutput<List<DisplayOptionResponse>> getCuisineCategories(IServiceInput<Void> input);

    IServiceOutput<List<DisplayOptionResponse>> getGroupCategories(IServiceInput<Void> input);

    IServiceOutput<FoodMetadataResponse> foodCategoryMetadata(IServiceInput<Void> input);

    /**
     * Updates the lifecycle status of a food item.
     *
     * @param foodId  Food Id
     * @param request Status update request
     *
     * @return Updated status details
     */
    IServiceOutput<FoodResponse> updateFoodStatus(IServiceInput<FoodStatusRequest> input);

    /**
     * reads the status of a food item.
     *
     * @param foodId Food Id
     *
     * @return @Code{FoodResponse} status details
     */
    IServiceOutput<FoodResponse> readFoodByFoodId(IServiceInput<FoodStatusRequest> input);

    /**
     * get the @Code{FoodEntity}.
     *
     * @param foodId Food Id
     *
     * @return @Code{FoodResponse} status details
     */
    IServiceOutput<EntityViewResponse<FoodResponse>> getFoodByFoodId(IServiceInput<FoodStatusRequest> input);

	// ============================================================================
	// Archive Operations
	// ============================================================================

	/**
	 * Archives a food item.
	 *
	 * @param input Archive food request.
	 *
	 * @return Archived food details.
	 */
	IServiceOutput<FoodResponse> archiveFood(IServiceInput<ArchiveFoodRequest> input);

	/**
	 * Archives multiple food items.
	 *
	 * @param input Bulk archive request.
	 *
	 * @return Success response.
	 */
	IServiceOutput<Void> bulkArchiveFoods(IServiceInput<BulkArchiveFoodRequest> input);

	// ============================================================================
	// Restore Operations
	// ============================================================================

	/**
	 * Restores an archived food item.
	 *
	 * @param input Restore food request.
	 *
	 * @return Restored food details.
	 */
	IServiceOutput<FoodResponse> restoreFood(IServiceInput<RestoreFoodRequest> input);

	/**
	 * Restores multiple archived food items.
	 *
	 * @param input Bulk restore request.
	 *
	 * @return Success response.
	 */
	IServiceOutput<Void> bulkRestoreFoods(IServiceInput<BulkRestoreFoodRequest> input);

	// ============================================================================
	// Permanent Delete Operations
	// ============================================================================

	/**
	 * Permanently deletes an archived food item.
	 *
	 * @param input Permanent delete request.
	 *
	 * @return Deleted food details.
	 */
	IServiceOutput<FoodResponse> permanentDeleteFood(IServiceInput<PermanentDeleteFoodRequest> input);

	/**
	 * Permanently deletes multiple archived food items.
	 *
	 * @param input Bulk permanent delete request.
	 *
	 * @return Success response.
	 */
	IServiceOutput<Void> bulkPermanentDeleteFoods(IServiceInput<BulkDeleteFoodRequest> input);

	// ============================================================================
	// Archived Food Operations
	// ============================================================================

	/**
	 * Retrieves all archived food items.
	 *
	 * @return Archived food list.
	 */
	IServiceOutput<List<FoodResponse>> readArchivedFoods();

	IServiceOutput<String> generateFoodNumber(IServiceInput<CreateFoodInputDTO> input);
}
