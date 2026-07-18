package com.foodies.freshmeal.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.UpdateFoodStatusRequest;

public interface IFoodController {

    ResponseEntity<ApiResponse<FoodResponse>> addFood(String foodJson, MultipartFile imageFile)
            throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> foodCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> dietCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> cuisineCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> groupCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<FoodResponse>> getFoodByFoodId(FoodStatusRequest foodId) throws JsonProcessingException;

    ResponseEntity<ApiResponse<FoodMetadataResponse>> foodCategoryMetadata() throws JsonProcessingException;
    
    ResponseEntity<ApiResponse<FoodResponse>> updateFoodStatus(String foodId, UpdateFoodStatusRequest updateRequest) throws JsonProcessingException;
    
    //ResponseEntity<ApiResponse<FoodResponse>> getFoodByFoodId(String foodId) throws JsonProcessingException;

}
