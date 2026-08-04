package com.foodies.freshmeal.food.controller.impl;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ApiBaseConstants;
import com.foodies.freshmeal.common.constants.ApiMessageConstants;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.food.constants.FoodApiConstants;
import com.foodies.freshmeal.food.controller.IFoodController;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.UpdateFoodStatusRequest;
import com.foodies.freshmeal.food.service.IFoodService;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * Food Controller
 * ============================================================================
 *
 * Responsibilities
 * ----------------
 * • Receive HTTP requests.
 * • Validate request payload.
 * • Delegate business logic to the service layer.
 * • Return standardized ApiResponse.
 *
 * The controller should NEVER contain business logic.
 * ============================================================================
 */
/*
 * {"/api/foods"}
 */
@RestController
@RequestMapping(ApiBaseConstants.FOOD_BASE_URL)
public class FoodController implements IFoodController {

    private final IFoodService foodService;
    private final ObjectMapper objectMapper;
    private final IServiceContext serviceContext;

    public FoodController(IFoodService foodService, ObjectMapper objectMapper, IServiceContext serviceContext) {
        this.foodService = foodService;
        this.objectMapper = objectMapper;
        this.serviceContext = serviceContext;
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
    @AuditApi(action = ActionType.CREATE, module = ModuleType.FOOD)
    @PostMapping(value = FoodApiConstants.ADD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> addFood(@RequestPart("food") String foodJsonRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile)
            throws JsonProcessingException {

        FoodRequest foodRequest = objectMapper.readValue(
                foodJsonRequest,
                FoodRequest.class);

        serviceContext.setAttribute("foodRequest", foodJsonRequest);

        IServiceInput<CreateFoodInputDTO> input = new ServiceInput<>();
        CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO();
        createFoodInputDTO.setFoodRequest(foodRequest);
        createFoodInputDTO.setImageFile(imageFile);
        input.setInput(createFoodInputDTO);

        IServiceOutput<FoodResponse> output = foodService.addFood(input);

        return ApiResponseBuilder.created(ApiMessageConstants.FOOD_CREATED , output.getOutput());
    }

    /*
     * ("/readAllFoods")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.READ_ALL_FOODS)
    public ResponseEntity<ApiResponse<List<FoodResponse>>> readFoods() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();
        IServiceOutput<List<FoodResponse>> output = foodService.readFoods(input);
        return  ApiResponseBuilder.success(ApiMessageConstants.FOOD_LIST_FOUND , output.getOutput());
    }

    /*
     * ("/metadata/food-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.FOOD_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> foodCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        foodService.getFoodCategories(input);

        return  ApiResponseBuilder.success(ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    /*
     * ("/metadata/diet-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.DIET_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> dietCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        foodService.getDietCategories(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    /*
     * ("/metadata/cuisine-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.CUISINE_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> cuisineCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        foodService.getCuisineCategories(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    /*
     * ("/metadata/group-categories")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.GROUP_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> groupCategories() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        foodService.getGroupCategories(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    /*
     * ("/foodCategoryMetadata")
     */
    // @AuditApi
    @Override
    @GetMapping(FoodApiConstants.FOOD_CATEGORY_METADATA)
    public ResponseEntity<ApiResponse<FoodMetadataResponse>> foodCategoryMetadata() throws JsonProcessingException {
        IServiceInput<Void> input = new ServiceInput<>();

        foodService.foodCategoryMetadata(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    /**
     * =========================================================================
     * Update Food Status
     * =========================================================================
     *
     * PATCH /api/admin/foods/{foodId}/status
     *
     * Example Request
     *
     * {
     * "status":"OUT_OF_STOCK"
     * }
     *
     * =========================================================================
     */

    /*
     * ("/{foodId}/status")
     */
    @Override
    @AuditApi(action = ActionType.UPDATE, module = ModuleType.FOOD)
    @PatchMapping(FoodApiConstants.UPDATE_FOOD_STATUS)
    public ResponseEntity<ApiResponse<FoodResponse>> updateFoodStatus(@PathVariable String foodId,
            @Valid @RequestBody UpdateFoodStatusRequest updateRequest) {

        FoodStatusRequest request = FoodStatusRequest.builder()
                .foodId(foodId)
                .updateFoodStatusRequest(updateRequest)
                .build();

        IServiceInput<FoodStatusRequest> input = new ServiceInput<>();
        input.setInput(request);

        IServiceOutput<FoodResponse> output = foodService.updateFoodStatus(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FOOD_STATUS_UPDATED, output.getOutput());
    }

    /*
     * {"/view"}
     */
    @AuditApi(action = ActionType.GET, module = ModuleType.FOOD)
    @PostMapping(FoodApiConstants.GET_FOOD_BY_FOOD_ID)
    @Override
    public ResponseEntity<ApiResponse<EntityViewResponse<FoodResponse>>> getFoodByFoodId(
            @RequestBody FoodStatusRequest foodRequest)
            throws JsonProcessingException {
        IServiceInput<FoodStatusRequest> input = new ServiceInput<>();
        input.setInput(foodRequest);

        IServiceOutput<EntityViewResponse<FoodResponse>> output = foodService.getFoodByFoodId(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FOOD_FOUND, output.getOutput());

    }

    /*
     * {"/edit"}
     */
    @AuditApi(action = ActionType.UPDATE, module = ModuleType.FOOD)
    // @PutMapping(FoodApiConstants.EDIT_FOOD)
    @Override

    @PutMapping(value = FoodApiConstants.EDIT_FOOD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> editFood(@RequestPart("food") String foodJsonRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws JsonProcessingException {

        FoodRequest foodRequest = objectMapper.readValue(
                foodJsonRequest,
                FoodRequest.class);

        serviceContext.setAttribute("foodRequest", foodJsonRequest);

        IServiceInput<CreateFoodInputDTO> input = new ServiceInput<>();
        CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO();
        createFoodInputDTO.setFoodRequest(foodRequest);
        createFoodInputDTO.setImageFile(imageFile);
        input.setInput(createFoodInputDTO);
        input.setServiceContext(serviceContext);

        IServiceOutput<FoodResponse> output = foodService.editFood(input);

        return ApiResponseBuilder.success(ApiMessageConstants.FOOD_UPDATED, output.getOutput());
    }

}
