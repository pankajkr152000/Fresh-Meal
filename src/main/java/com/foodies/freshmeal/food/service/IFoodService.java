package com.foodies.freshmeal.food.service;

import java.util.List;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.entity.IFoodEntity;

public interface IFoodService {

    IServiceOutput<IFoodEntity> createFoodEntity(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<String> generateFoodId(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<FoodResponse> addFood(IServiceInput<CreateFoodInputDTO> input);

    IServiceOutput<List<FoodResponse>> readFoods(IServiceInput<Void> input);

    IServiceOutput<List<String>> getFoodCategories(IServiceInput<Void> input);

    IServiceOutput<List<String>> getDietCategories(IServiceInput<Void> input);

    IServiceOutput<List<String>> getCuisineCategories(IServiceInput<Void> input);

    IServiceOutput<List<String>> getGroupCategories(IServiceInput<Void> input);

}
