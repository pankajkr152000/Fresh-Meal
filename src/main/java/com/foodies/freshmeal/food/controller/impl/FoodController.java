package com.foodies.freshmeal.food.controller.impl;


import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.ApiResponses;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.food.controller.IFoodController;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.service.IFoodService;

@RestController
@RequestMapping("/api/foods")
public class FoodController implements IFoodController {

    private final IFoodService foodService;
    private final ObjectMapper objectMapper;

    public FoodController(IFoodService foodService, ObjectMapper objectMapper) {
        this.foodService = foodService;
        this.objectMapper = objectMapper;
    }

    @Override
    @AuditApi
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> addFood(
            @RequestPart("food") String foodJson,
            @RequestPart("image") MultipartFile imageFile)
            throws JsonProcessingException {

        FoodRequest foodRequest = objectMapper.readValue(
                foodJson,
                FoodRequest.class);

        IServiceContext serviceContext = new ServiceContext();

        serviceContext.setAttribute("foodRequest", foodJson);
                
        IServiceInput<CreateFoodInputDTO> input = new ServiceInput<>();
        CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO();
        createFoodInputDTO.setFoodRequest(foodRequest);
        createFoodInputDTO.setImageFile(imageFile);
        input.setInput(createFoodInputDTO);

        IServiceOutput<FoodResponse> output = foodService.addFood(input);
        
        return ApiResponses.created("Food created successfully", output.getOutput());
    }

    @AuditApi
    @Override
    @GetMapping("/readAllFoods")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();
        IServiceOutput<List<FoodResponse>> output = foodService.readFoods(input);
        return ApiResponses.ok("Foods retrieved successfully", output.getOutput());
    }

    @AuditApi
    @Override
    @GetMapping("/metadata/food-categories")
    public ResponseEntity<ApiResponse<List<String>>> foodCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<String>> output = foodService.getFoodCategories(input);

        return ApiResponses.ok("Foods categories", output.getOutput());
    }

    @AuditApi
    @Override
    @GetMapping("/metadata/diet-categories")
    public ResponseEntity<ApiResponse<List<String>>> dietCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<String>> output = foodService.getDietCategories(input);

        return ApiResponses.ok("Diet categories", output.getOutput());
    }

    @AuditApi
    @Override
    @GetMapping("/metadata/cuisine-categories")
    public ResponseEntity<ApiResponse<List<String>>> cuisineCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<String>> output = foodService.getCuisineCategories(input);

        return ApiResponses.ok("Cuisine categories", output.getOutput());
    }

    @AuditApi
    @Override
    @GetMapping("/metadata/group-categories")
    public ResponseEntity<ApiResponse<List<String>>> groupCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<String>> output = foodService.getGroupCategories(input);

        return ApiResponses.ok("Group categories", output.getOutput());
    }

    @AuditApi
    @Override
    @GetMapping("/foodCategoryMetadata")
    public ResponseEntity<ApiResponse<Map<String,Object>>> foodCategoryMetadata() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<Map<String,Object>> output = foodService.foodCategoryMetadata(input);

        return ApiResponses.ok("Group categories", output.getOutput());
    }



}
