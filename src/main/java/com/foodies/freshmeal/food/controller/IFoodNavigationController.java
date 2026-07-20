package com.foodies.freshmeal.food.controller;


import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.food.dto.FoodResponse;

public interface IFoodNavigationController {
	ResponseEntity<ApiResponse<EntityViewResponse<FoodResponse>>> getFoodByFoodId() throws JsonProcessingException;
}
