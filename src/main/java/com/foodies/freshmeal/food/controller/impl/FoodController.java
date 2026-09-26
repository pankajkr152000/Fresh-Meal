package com.foodies.freshmeal.food.controller.impl;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.foodies.freshmeal.common.constants.AuthorizationConstants;
import com.foodies.freshmeal.common.constants.MethodType;
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
import com.foodies.freshmeal.food.dto.ArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.BulkRestoreFoodRequest;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.EditFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.PermanentDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.RestoreFoodRequest;
import com.foodies.freshmeal.food.dto.UpdateFoodStatusRequest;
import com.foodies.freshmeal.food.service.IFoodService;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * Food Controller
 * ============================================================================
 *
 * <p>
 * Handles HTTP requests related to food management.
 * </p>
 *
 * <p>
 * Responsibilities:
 * </p>
 * <ul>
 * <li>Receive HTTP requests.</li>
 * <li>Validate request payloads.</li>
 * <li>Build service-layer input objects.</li>
 * <li>Delegate business operations to the service layer.</li>
 * <li>Return standardized {@link ApiResponse} responses.</li>
 * </ul>
 *
 * <p>
 * The controller must not contain business or ownership validation logic.
 * Restaurant and branch ownership authorization is handled by the service
 * layer.
 * </p>
 *
 * @author Pankaj Kumar
 *         ============================================================================
 */
@RestController
@RequestMapping(ApiBaseConstants.FOOD_BASE_URL)
public class FoodController implements IFoodController {

    private final IFoodService foodService;
    private final ObjectMapper objectMapper;
    private final IServiceContext serviceContext;

    public FoodController(
            IFoodService foodService,
            ObjectMapper objectMapper,
            IServiceContext serviceContext) {

        this.foodService = foodService;
        this.objectMapper = objectMapper;
        this.serviceContext = serviceContext;
    }

    /**
     * =========================================================================
     * Create Food
     * =========================================================================
     *
     * <p>
     * Creates a new food item for the specified restaurant branch.
     * </p>
     *
     * <p>
     * Supported multipart request parts:
     * </p>
     * <ul>
     * <li>
     * {@code food} - JSON representation of {@link FoodRequest}
     * </li>
     * <li>
     * {@code restaurantNumber} - restaurant business identifier
     * </li>
     * <li>
     * {@code restaurantBranchNumber} - restaurant branch business
     * identifier
     * </li>
     * <li>
     * {@code image} - optional food image
     * </li>
     * </ul>
     *
     * <p>
     * The controller only transports the restaurant and branch context to the
     * service layer. The service layer is responsible for validating that the
     * restaurant exists, the branch belongs to that restaurant, and the
     * authenticated user is authorized to operate on the specified branch.
     * </p>
     *
     * @param foodJsonRequest        food details as JSON
     * @param restaurantNumber       restaurant business identifier
     * @param restaurantBranchNumber restaurant branch business identifier
     * @param imageFile              optional food image
     * @return created food information
     * @throws JsonProcessingException if the food JSON cannot be parsed
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @AuditApi(action = ActionType.ADD_FOOD, module = ModuleType.FOOD, method = MethodType.CREATE)
    @PostMapping(value = FoodApiConstants.ADD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> addFood(
            @RequestPart("food") String foodJsonRequest,
            @RequestPart("restaurantNumber") String restaurantNumber,
            @RequestPart("restaurantBranchNumber") String restaurantBranchNumber,
            @RequestPart(value = "image", required = false) MultipartFile imageFile)
            throws JsonProcessingException {

        final FoodRequest foodRequest = objectMapper.readValue(
                foodJsonRequest,
                FoodRequest.class);

        serviceContext.setAttribute("foodRequest", foodJsonRequest);

        final IServiceInput<CreateFoodInputDTO> input = new ServiceInput<>();

        final CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO();
        createFoodInputDTO.setFoodRequest(foodRequest);
        createFoodInputDTO.setRestaurantNumber(restaurantNumber);
        createFoodInputDTO.setRestaurantBranchNumber(restaurantBranchNumber);
        createFoodInputDTO.setImageFile(imageFile);

        input.setInput(createFoodInputDTO);
        input.setServiceContext(serviceContext);

        final IServiceOutput<FoodResponse> output = foodService.addFood(input);

        return ApiResponseBuilder.created(
                ApiMessageConstants.FOOD_CREATED,
                output.getOutput());
    }

    /*
     * =========================================================================
     * Read Operations
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.READ_ALL_FOODS, module = ModuleType.FOOD, method = MethodType.READ)
    @GetMapping(FoodApiConstants.READ_ALL_FOODS)
    public ResponseEntity<ApiResponse<List<FoodResponse>>> readFoodsForManagement()
            throws JsonProcessingException {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        IServiceOutput<List<FoodResponse>> output = foodService.readFoodsForManagement(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FOOD_LIST_FOUND,
                output.getOutput());
    }

    /*
     * =========================================================================
     * Food Metadata
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @GetMapping(FoodApiConstants.FOOD_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> foodCategories()
            throws JsonProcessingException {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        foodService.getFoodCategories(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @GetMapping(FoodApiConstants.DIET_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> dietCategories()
            throws JsonProcessingException {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        foodService.getDietCategories(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @GetMapping(FoodApiConstants.CUISINE_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> cuisineCategories()
            throws JsonProcessingException {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        foodService.getCuisineCategories(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @GetMapping(FoodApiConstants.GROUP_CATEGORIES)
    public ResponseEntity<ApiResponse<List<DisplayOptionResponse>>> groupCategories()
            throws JsonProcessingException {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        foodService.getGroupCategories(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FETCHED_SUCCESSFULLY);
    }

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @GetMapping(FoodApiConstants.FOOD_CATEGORY_METADATA)
    public ResponseEntity<ApiResponse<FoodMetadataResponse>> foodCategoryMetadata()
            throws JsonProcessingException {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        IServiceOutput<FoodMetadataResponse> response = foodService.foodCategoryMetadata(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FETCHED_SUCCESSFULLY,
                response.getOutput());
    }

    /*
     * =========================================================================
     * Food Status
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @AuditApi(action = ActionType.UPDATE_FOOD_STATUS, module = ModuleType.FOOD, method = MethodType.UPDATE)
    @PatchMapping(FoodApiConstants.UPDATE_FOOD_STATUS)
    public ResponseEntity<ApiResponse<FoodResponse>> updateFoodStatus(
            @PathVariable String foodId,
            @Valid @RequestBody UpdateFoodStatusRequest updateRequest) {

        FoodStatusRequest request = FoodStatusRequest.builder()
                .foodId(foodId)
                .updateFoodStatusRequest(updateRequest)
                .build();

        IServiceInput<FoodStatusRequest> input = new ServiceInput<>();
        input.setInput(request);
        input.setServiceContext(serviceContext);

        IServiceOutput<FoodResponse> output = foodService.updateFoodStatus(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FOOD_STATUS_UPDATED,
                output.getOutput());
    }

    /*
     * =========================================================================
     * View Food
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.VIEW_FOOD, module = ModuleType.FOOD, method = MethodType.READ)
    @PostMapping(FoodApiConstants.GET_FOOD_BY_FOOD_ID)
    public ResponseEntity<ApiResponse<EntityViewResponse<FoodResponse>>> getFoodByFoodId(
            @RequestBody FoodStatusRequest foodRequest)
            throws JsonProcessingException {

        IServiceInput<FoodStatusRequest> input = new ServiceInput<>();
        input.setInput(foodRequest);
        input.setServiceContext(serviceContext);

        IServiceOutput<EntityViewResponse<FoodResponse>> output = foodService.getFoodByFoodId(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FOOD_FOUND,
                output.getOutput());
    }

    /*
     * =========================================================================
     * Edit Food
     * =========================================================================
     */

    @Override
    @AuditApi(action = ActionType.UPDATE_FOOD, module = ModuleType.FOOD, method = MethodType.UPDATE)
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @PutMapping(value = FoodApiConstants.EDIT_FOOD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> editFood(
            @RequestPart("food") String foodJsonRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile)
            throws JsonProcessingException {

        final EditFoodInputDTO editFoodInputDTO = objectMapper.readValue(
                foodJsonRequest,
                EditFoodInputDTO.class);

        editFoodInputDTO.setImageFile(imageFile);

        serviceContext.setAttribute(
                "foodRequest",
                foodJsonRequest);

        final IServiceInput<EditFoodInputDTO> input = new ServiceInput<>();

        input.setInput(editFoodInputDTO);
        input.setServiceContext(serviceContext);

        final IServiceOutput<FoodResponse> output = foodService.editFood(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.FOOD_UPDATED,
                output.getOutput());
    }

    /*
     * =========================================================================
     * Archive Operations
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.ARCHIVE_FOOD, method = MethodType.UPDATE)
    @PatchMapping(FoodApiConstants.ARCHIVE_FOOD)
    public ResponseEntity<ApiResponse<FoodResponse>> archiveFood(
            @RequestBody ArchiveFoodRequest input) {

        IServiceInput<ArchiveFoodRequest> serviceInput = new ServiceInput<>();
        serviceInput.setServiceContext(serviceContext);
        serviceInput.setInput(input);

        IServiceOutput<FoodResponse> serviceOutput = foodService.archiveFood(serviceInput);

        return ApiResponseBuilder.success(serviceOutput.getOutput());
    }

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.ARCHIVE_FOOD, method = MethodType.UPDATE)
    @PatchMapping(FoodApiConstants.BULK_ARCHIVE_FOOD)
    public ResponseEntity<ApiResponse<Void>> bulkArchiveFoods(
            @RequestBody BulkArchiveFoodRequest bulkArchiveFoodRequest) {

        IServiceInput<BulkArchiveFoodRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(bulkArchiveFoodRequest);

        foodService.bulkArchiveFoods(input);

        return ApiResponseBuilder.success();
    }

    /*
     * =========================================================================
     * Restore Operations
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.RESTORE_FOOD, method = MethodType.UPDATE)
    @PatchMapping(FoodApiConstants.RESTORE_FOOD)
    public ResponseEntity<ApiResponse<FoodResponse>> restoreFood(
            @RequestBody RestoreFoodRequest input) {

        IServiceInput<RestoreFoodRequest> serviceInput = new ServiceInput<>();
        serviceInput.setServiceContext(serviceContext);
        serviceInput.setInput(input);

        IServiceOutput<FoodResponse> serviceOutput = foodService.restoreFood(serviceInput);

        return ApiResponseBuilder.success(serviceOutput.getOutput());
    }

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.RESTORE_FOOD, method = MethodType.UPDATE)
    @PatchMapping(FoodApiConstants.BULK_RESTORE_FOOD)
    public ResponseEntity<ApiResponse<Void>> bulkRestoreFoods(
            @RequestBody BulkRestoreFoodRequest input) {

        IServiceInput<BulkRestoreFoodRequest> serviceInput = new ServiceInput<>();
        serviceInput.setServiceContext(serviceContext);
        serviceInput.setInput(input);

        foodService.bulkRestoreFoods(serviceInput);

        return ApiResponseBuilder.success();
    }

    /*
     * =========================================================================
     * Permanent Delete Operations
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.PERMANENT_DELETE_FOOD, method = MethodType.DELETE)
    @DeleteMapping(FoodApiConstants.PERMANENT_DELETE_FOOD)
    public ResponseEntity<ApiResponse<Void>> permanentDeleteFood(
            @RequestBody PermanentDeleteFoodRequest input) {

        IServiceInput<PermanentDeleteFoodRequest> serviceInput = new ServiceInput<>();
        serviceInput.setServiceContext(serviceContext);
        serviceInput.setInput(input);

        foodService.permanentDeleteFood(serviceInput);

        return ApiResponseBuilder.success();
    }

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.PERMANENT_DELETE_FOOD, method = MethodType.DELETE)
    @DeleteMapping(FoodApiConstants.BULK_PERMANENT_DELETE_FOOD)
    public ResponseEntity<ApiResponse<Void>> bulkPermanentDeleteFoods(
            @RequestBody BulkDeleteFoodRequest input) {

        IServiceInput<BulkDeleteFoodRequest> serviceInput = new ServiceInput<>();
        serviceInput.setServiceContext(serviceContext);
        serviceInput.setInput(input);

        foodService.bulkPermanentDeleteFoods(serviceInput);

        return ApiResponseBuilder.success();
    }

    /*
     * =========================================================================
     * Archived Food Operations
     * =========================================================================
     */

    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @AuditApi(module = ModuleType.FOOD, action = ActionType.READ_ARCHIVED_FOODS, method = MethodType.READ)
    @GetMapping(FoodApiConstants.GET_ARCHIVED_FOODS)
    public ResponseEntity<ApiResponse<List<FoodResponse>>> readArchivedFoods() {

        IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        IServiceOutput<List<FoodResponse>> serviceOutput = foodService.readArchivedFoods();

        return ApiResponseBuilder.success(serviceOutput.getOutput());
    }
}