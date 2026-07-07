package com.foodies.freshmeal.food.service;

import java.util.List;
import java.util.Map;

import com.foodies.freshmeal.common.dto.DropdownOption;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.FoodStatusResponse;
import com.foodies.freshmeal.food.entity.FoodEntity;

public interface IFoodService {

    IServiceOutput<FoodEntity> createFoodEntity(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<String> generateFoodId(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<FoodResponse> addFood(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<List<FoodResponse>> readFoods(IServiceInput<Void> input);

    IServiceOutput<List<DropdownOption>> getFoodCategories(IServiceInput<Void> input);

    IServiceOutput<List<DropdownOption>> getDietCategories(IServiceInput<Void> input);

    IServiceOutput<List<DropdownOption>> getCuisineCategories(IServiceInput<Void> input);

    IServiceOutput<List<DropdownOption>> getGroupCategories(IServiceInput<Void> input);

    IServiceOutput<Map<String, Object>> foodCategoryMetadata(IServiceInput<Void> input);

    /**
     * Updates the lifecycle status of a food item.
     *
     * @param foodId  Food Id
     * @param request Status update request
     *
     * @return Updated status details
     */
    IServiceOutput<FoodStatusResponse> updateFoodStatus(IServiceInput<FoodStatusRequest> input);

}
