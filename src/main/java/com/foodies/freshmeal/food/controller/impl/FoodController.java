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
import com.foodies.freshmeal.common.constants.ApiBaseConstants;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.ApiResponses;
import com.foodies.freshmeal.common.dto.DropdownOption;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.food.constants.FoodApiConstants;
import com.foodies.freshmeal.food.controller.IFoodController;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.service.IFoodService;

@RestController
@RequestMapping(ApiBaseConstants.FOOD_BASE_URL) // /api/foods
public class FoodController implements IFoodController {

    private final IFoodService foodService;
    private final ObjectMapper objectMapper;

    public FoodController(IFoodService foodService, ObjectMapper objectMapper) {
        this.foodService = foodService;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new food item.
     *
     * Supported Request Parts:
     * - food : FoodRequest (required)
     * - image : MultipartFile (optional)
     *
     * @param request food details
     * @param image   optional food image
     * @return created food information
     */
    /*
     * ("/add")
     */
    @Override
    @AuditApi
    @PostMapping(value = FoodApiConstants.ADD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> addFood(@RequestPart("food") String foodJsonRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile)
            throws JsonProcessingException {

        FoodRequest foodRequest = objectMapper.readValue(
                foodJsonRequest,
                FoodRequest.class);

        IServiceContext serviceContext = new ServiceContext();

        serviceContext.setAttribute("foodRequest", foodJsonRequest);

        IServiceInput<CreateFoodInputDTO> input = new ServiceInput<>();
        CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO();
        createFoodInputDTO.setFoodRequest(foodRequest);
        createFoodInputDTO.setImageFile(imageFile);
        input.setInput(createFoodInputDTO);

        IServiceOutput<FoodResponse> output = foodService.addFood(input);

        return ApiResponses.created("Food created successfully", output.getOutput());
    }

    /*
     * ("/readAllFoods")
     */
    @AuditApi
    @Override
    @GetMapping(FoodApiConstants.READ_ALL_FOODS)
    public ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();
        IServiceOutput<List<FoodResponse>> output = foodService.readFoods(input);
        return ApiResponses.ok("Foods retrieved successfully", output.getOutput());
    }

    /*
     * ("/metadata/food-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.FOOD_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DropdownOption>>> foodCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<DropdownOption>> output = foodService.getFoodCategories(input);

        return ApiResponses.ok("Foods categories", output.getOutput());
    }

    /*
     * ("/metadata/diet-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.DIET_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DropdownOption>>> dietCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<DropdownOption>> output = foodService.getDietCategories(input);

        return ApiResponses.ok("Diet categories", output.getOutput());
    }

    /*
     * ("/metadata/cuisine-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.CUISINE_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DropdownOption>>> cuisineCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<DropdownOption>> output = foodService.getCuisineCategories(input);

        return ApiResponses.ok("Cuisine categories", output.getOutput());
    }

    /*
     * ("/metadata/group-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.GROUP_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DropdownOption>>> groupCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<List<DropdownOption>> output = foodService.getGroupCategories(input);

        return ApiResponses.ok("Group categories", output.getOutput());
    }

    /*
     * ("/foodCategoryMetadata")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.FOOD_CATEGORY_METADATA)
    public ResponseEntity<ApiResponse<Map<String, Object>>> foodCategoryMetadata() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        IServiceOutput<Map<String, Object>> output = foodService.foodCategoryMetadata(input);

        return ApiResponses.ok("Group categories", output.getOutput());
    }

}
