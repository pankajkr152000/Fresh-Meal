package com.foodies.freshmeal.food.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.DropdownOption;
import com.foodies.freshmeal.food.dto.FoodResponse;

public interface IFoodController {

    ResponseEntity<ApiResponse<FoodResponse>> addFood(String foodJson, MultipartFile imageFile)
            throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DropdownOption>>> foodCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DropdownOption>>> dietCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DropdownOption>>> cuisineCategories() throws JsonProcessingException;

    ResponseEntity<ApiResponse<List<DropdownOption>>> groupCategories() throws JsonProcessingException;

    public ResponseEntity<ApiResponse<Map<String, Object>>> foodCategoryMetadata() throws JsonProcessingException;

}
