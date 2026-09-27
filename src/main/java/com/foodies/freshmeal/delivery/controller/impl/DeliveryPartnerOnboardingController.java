package com.foodies.freshmeal.delivery.controller.impl;

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
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerApiConstants;
import com.foodies.freshmeal.delivery.controller.IDeliveryPartnerOnboardingController;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerRegistrationRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.service.IDeliveryPartnerOnboardingService;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * Controller : DeliveryPartnerOnboardingController
 * ============================================================================
 *
 * REST controller responsible for Delivery Partner onboarding.
 *
 * <p>
 * This controller handles the creation of a Delivery Partner profile for an
 * authenticated user having the DELIVERY_PARTNER role.
 * </p>
 *
 * <p>
 * Delivery Partner management operations such as verification, status and
 * availability are intentionally handled by {@code DeliveryPartnerController}
 * and are not part of this onboarding controller.
 * </p>
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
@RestController
@RequestMapping(DeliveryPartnerApiConstants.BASE_URL)
public class DeliveryPartnerOnboardingController
        implements IDeliveryPartnerOnboardingController {

    private final IDeliveryPartnerOnboardingService deliveryPartnerOnboardingService;
    private final IServiceContext serviceContext;

    /**
     * Creates a Delivery Partner onboarding controller.
     *
     * @param deliveryPartnerOnboardingService onboarding service
     * @param serviceContext                   request-scoped service context
     */
    public DeliveryPartnerOnboardingController(
            final IDeliveryPartnerOnboardingService deliveryPartnerOnboardingService,
            final IServiceContext serviceContext) {

        this.deliveryPartnerOnboardingService = deliveryPartnerOnboardingService;
        this.serviceContext = serviceContext;
    }

    /**
     * Onboards the authenticated user as a Delivery Partner.
     *
     * <p>
     * The authenticated user must already possess the DELIVERY_PARTNER role.
     * The onboarding service performs the actual business validation and
     * creation of the Delivery Partner profile.
     * </p>
     *
     * @param request delivery partner registration details
     * @return created Delivery Partner response
     */
    @Override
    @PostMapping(DeliveryPartnerApiConstants.ONBOARD)
    @PreAuthorize(AuthorizationConstants.DELIVERY_PARTNER_ONLY)
    @AuditApi(action = ActionType.REGISTER_DELIVERY_PARTNER, module = ModuleType.DELIVERY_PARTNER, method = MethodType.CREATE)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> onboardDeliveryPartner(
            @Valid @RequestBody final DeliveryPartnerRegistrationRequest request) {

        final IServiceInput<DeliveryPartnerRegistrationRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);
        serviceInput.setServiceContext(serviceContext);

        final IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerOnboardingService
                .onboardDeliveryPartner(serviceInput);

        return ApiResponseBuilder.created(
                ApiMessageConstants.DELIVERY_PARTNER_CREATED,
                serviceOutput.getOutput());
    }
}