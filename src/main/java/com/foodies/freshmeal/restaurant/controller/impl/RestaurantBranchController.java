package com.foodies.freshmeal.restaurant.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.AuthorizationConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.restaurant.constants.RestaurantBranchApiConstants;
import com.foodies.freshmeal.restaurant.constants.RestaurantBranchApiMessageConstants;
import com.foodies.freshmeal.restaurant.controller.IRestaurantBranchController;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.service.IRestaurantBranchService;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * Controller : Restaurant Branch
 * ============================================================================
 *
 * <p>
 * Provides REST endpoints for Restaurant Branch management.
 * </p>
 *
 * <p>
 * The controller is intentionally limited to HTTP request handling,
 * validation, service-input construction, audit metadata, and delegation to
 * {@link IRestaurantBranchService}.
 * </p>
 *
 * <p>
 * Business rules and persistence operations remain inside the service and
 * repository layers respectively.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(RestaurantBranchApiConstants.BASE_URL)
public class RestaurantBranchController implements IRestaurantBranchController {

    private final IRestaurantBranchService restaurantBranchService;

    private final IServiceContext serviceContext;

    /**
     * Creates RestaurantBranchController.
     *
     * @param restaurantBranchService
     *                                restaurant branch service
     * @param serviceContext
     *                                service execution context
     */
    public RestaurantBranchController(
            IRestaurantBranchService restaurantBranchService,
            IServiceContext serviceContext) {

        this.restaurantBranchService = restaurantBranchService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Create
    // =========================================================================

    /**
     * Creates a new restaurant branch.
     *
     * @param input restaurant branch creation request
     *
     * @return created restaurant branch
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @PostMapping(RestaurantBranchApiConstants.CREATE)
    @AuditApi(module = ModuleType.RESTAURANT, action = ActionType.CREATE_RESTAURANT_BRANCH, method = MethodType.CREATE)
    public ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> createBranch(
            @Valid @RequestBody RestaurantBranchCreateRequest input) {

        IServiceInput<RestaurantBranchCreateRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(input);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<RestaurantBranchDetailsResponse> serviceOutput = restaurantBranchService.create(serviceInput);

        return ApiResponseBuilder.success(
                RestaurantBranchApiMessageConstants.RESTAURANT_BRANCH_CREATED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Read All
    // =========================================================================

    /**
     * Retrieves all active restaurant branches.
     *
     * @return active restaurant branches
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @GetMapping(RestaurantBranchApiConstants.READ_ALL_BRANCHES)
    @AuditApi(module = ModuleType.RESTAURANT, action = ActionType.READ_ALL_RESTAURANT_BRANCHES, method = MethodType.READ)
    public ResponseEntity<ApiResponse<List<RestaurantBranchListResponse>>> readBranches() {

        IServiceInput<Void> serviceInput = new ServiceInput<>();

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<List<RestaurantBranchListResponse>> serviceOutput = restaurantBranchService.getAll(serviceInput);

        return ApiResponseBuilder.success(
                RestaurantBranchApiMessageConstants.RESTAURANT_BRANCH_LIST_FOUND,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Read By ID
    // =========================================================================

    /**
     * Retrieves a restaurant branch by identifier.
     *
     * @param input restaurant branch identifier request
     *
     * @return restaurant branch details
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @PostMapping(RestaurantBranchApiConstants.GET_BRANCH_BY_ID)
    @AuditApi(module = ModuleType.RESTAURANT, action = ActionType.VIEW_RESTAURANT_BRANCH, method = MethodType.READ)
    public ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> getBranchById(
            @Valid @RequestBody RestaurantBranchIdRequest input) {

        IServiceInput<RestaurantBranchIdRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(input);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<RestaurantBranchDetailsResponse> serviceOutput = restaurantBranchService.getById(serviceInput);

        return ApiResponseBuilder.success(
                RestaurantBranchApiMessageConstants.RESTAURANT_BRANCH_FOUND,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Read By Restaurant
    // =========================================================================

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * @param input restaurant identifier request
     *
     * @return restaurant branches belonging to the restaurant
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @PostMapping(RestaurantBranchApiConstants.GET_BY_RESTAURANT_ID)
    @AuditApi(module = ModuleType.RESTAURANT, action = ActionType.READ_RESTAURANT_BRANCHES_BY_RESTAURANT, method = MethodType.READ)
    public ResponseEntity<ApiResponse<List<RestaurantBranchListResponse>>> getBranchesByRestaurantId(
            @Valid @RequestBody RestaurantIdRequest input) {

        IServiceInput<RestaurantIdRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(input);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<List<RestaurantBranchListResponse>> serviceOutput = restaurantBranchService
                .getByRestaurantId(serviceInput);

        return ApiResponseBuilder.success(
                RestaurantBranchApiMessageConstants.RESTAURANT_BRANCH_LIST_BY_RESTAURANT_FOUND,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Update
    // =========================================================================

    /**
     * Updates an existing restaurant branch.
     *
     * @param input restaurant branch update request
     *
     * @return updated restaurant branch
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_RESTAURANT_OWNER)
    @PutMapping(RestaurantBranchApiConstants.UPDATE)
    @AuditApi(module = ModuleType.RESTAURANT, action = ActionType.UPDATE_RESTAURANT_BRANCH, method = MethodType.UPDATE)
    public ResponseEntity<ApiResponse<RestaurantBranchDetailsResponse>> updateBranch(
            @Valid @RequestBody RestaurantBranchUpdateRequest input) {

        IServiceInput<RestaurantBranchUpdateRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(input);
        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<RestaurantBranchDetailsResponse> serviceOutput = restaurantBranchService.update(serviceInput);

        return ApiResponseBuilder.success(
                RestaurantBranchApiMessageConstants.RESTAURANT_BRANCH_UPDATED,
                serviceOutput.getOutput());
    }
}
