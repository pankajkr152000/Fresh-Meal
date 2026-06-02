package com.foodies.freshmeal.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;

public interface IFoodController {

    ResponseEntity<ApiResponse<FoodResponse>> addFood(String foodJson, MultipartFile imageFile) throws JsonProcessingException;

    
    ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException;




}
