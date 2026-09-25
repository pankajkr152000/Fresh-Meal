package com.foodies.freshmeal.restaurant.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ApiMessageConstants;
import com.foodies.freshmeal.common.constants.AuthorizationConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.restaurant.constants.RestaurantApiConstants;
import com.foodies.freshmeal.restaurant.controller.IRestaurantOnboardingController;
import com.foodies.freshmeal.restaurant.dto.RestaurantOnboardingInputDTO;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;
import com.foodies.freshmeal.restaurant.service.IRestaurantOnboardingService;

/**
 * ============================================================================
 * Restaurant Onboarding Controller
 * ============================================================================
 *
 * <p>
 * Handles restaurant-owner onboarding requests.
 * </p>
 *
 * <p>
 * The controller is intentionally thin and contains no restaurant business
 * logic. Its responsibilities are limited to:
 * </p>
 *
 * <ul>
 * <li>Receiving the HTTP request.</li>
 * <li>Building the service-layer input.</li>
 * <li>Passing the existing request-scoped service context.</li>
 * <li>Delegating processing to the onboarding service.</li>
 * <li>Returning the standardized API response.</li>
 * </ul>
 *
 * <p>
 * The authenticated user's identity is resolved by the service layer from
 * {@link IServiceContext}. The client must not provide an owner user number
 * as part of the onboarding request.
 * </p>
 *
 * ============================================================================
 *
 * @author FreshMeal Development Team
 * @since 1.0
 */
@RestController
@RequestMapping(RestaurantApiConstants.BASE_URL)
public class RestaurantOnboardingController
        implements IRestaurantOnboardingController {

    /**
     * Restaurant onboarding service.
     */
    private final IRestaurantOnboardingService restaurantOnboardingService;

    /**
     * Request-scoped service context.
     */
    private final IServiceContext serviceContext;

    /**
     * Creates RestaurantOnboardingController.
     *
     * @param restaurantOnboardingService restaurant onboarding service
     * @param serviceContext              request-scoped service context
     */
    public RestaurantOnboardingController(
            IRestaurantOnboardingService restaurantOnboardingService,
            IServiceContext serviceContext) {

        this.restaurantOnboardingService = restaurantOnboardingService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Restaurant Owner Onboarding
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.CREATE_RESTAURANT, module = ModuleType.RESTAURANT, method = MethodType.CREATE)
    @PostMapping(RestaurantApiConstants.ONBOARD)
    public ResponseEntity<ApiResponse<RestaurantEntity>> onboardRestaurant(
            @RequestBody RestaurantOnboardingInputDTO request) {

        IServiceInput<RestaurantOnboardingInputDTO> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<RestaurantEntity> serviceOutput = restaurantOnboardingService.onboardRestaurant(serviceInput);

        return ApiResponseBuilder.created(
                ApiMessageConstants.RESTAURANT_CREATED,
                serviceOutput.getOutput());
    }
}