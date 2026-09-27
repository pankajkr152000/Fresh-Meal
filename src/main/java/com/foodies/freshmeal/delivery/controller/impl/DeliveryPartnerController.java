package com.foodies.freshmeal.delivery.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.foodies.freshmeal.delivery.controller.IDeliveryPartnerController;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerAvailabilityRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerIdRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerStatusRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerUpdateRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerVerificationRequest;
import com.foodies.freshmeal.delivery.service.IDeliveryPartnerService;
import com.foodies.freshmeal.user.dto.UserNumberRequest;

/**
 * ============================================================================
 * Delivery Partner Controller
 * ============================================================================
 *
 * Responsibilities ----------------
 *
 * • Receive HTTP requests.
 * • Validate request payloads through the controller contract.
 * • Build service input objects.
 * • Delegate business operations to DeliveryPartnerService.
 * • Return standardized ApiResponse.
 *
 * <p>
 * The controller must not contain Delivery Partner business logic.
 * </p>
 *
 * ============================================================================
 *
 * Current Operations ------------------
 *
 * • View Delivery Partner
 * • View Delivery Partner By User Number
 * • Update Delivery Partner
 * • Update Delivery Partner Verification Status
 * • Update Delivery Partner Status
 * • Update Delivery Partner Availability
 *
 * <p>
 * Delivery Partner onboarding is handled by a separate onboarding controller.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(DeliveryPartnerApiConstants.BASE_URL)
public class DeliveryPartnerController
        implements IDeliveryPartnerController {

    private final IDeliveryPartnerService deliveryPartnerService;

    private final IServiceContext serviceContext;

    /**
     * Creates DeliveryPartnerController.
     *
     * @param deliveryPartnerService Delivery Partner service.
     * @param serviceContext         Service execution context.
     */
    public DeliveryPartnerController(
            IDeliveryPartnerService deliveryPartnerService,
            IServiceContext serviceContext) {

        this.deliveryPartnerService = deliveryPartnerService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Get Delivery Partner By ID
    // =========================================================================

    /**
     * Retrieves a Delivery Partner by its identifier.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * POST / api / delivery - partners / view
     * </pre>
     *
     * @param input Delivery Partner identifier request.
     *
     * @return Delivery Partner details.
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_DELIVERY_PARTNER)
    @AuditApi(action = ActionType.VIEW_DELIVERY_PARTNER, module = ModuleType.DELIVERY_PARTNER, method = MethodType.READ)
    @PostMapping(DeliveryPartnerApiConstants.GET_BY_ID)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> getById(
            @RequestBody DeliveryPartnerIdRequest input) {

        IServiceInput<String> serviceInput = new ServiceInput<>();

        serviceInput.setInput(input.getPartnerId());

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerService.getById(serviceInput);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DELIVERY_PARTNER_FOUND,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Get Delivery Partner By User Number
    // =========================================================================

    /**
     * Retrieves a Delivery Partner using the associated User Number.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * POST / api / delivery - partners / view - by - user
     * </pre>
     *
     * @param input user number request.
     *
     * @return Delivery Partner details.
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_DELIVERY_PARTNER)
    @AuditApi(action = ActionType.VIEW_DELIVERY_PARTNER, module = ModuleType.DELIVERY_PARTNER, method = MethodType.READ)
    @PostMapping(DeliveryPartnerApiConstants.GET_BY_USER_NUMBER)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> getByUserNumber(
            @RequestBody UserNumberRequest input) {

        IServiceInput<String> serviceInput = new ServiceInput<>();

        serviceInput.setInput(input.getUserNumber());

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerService.getByUserNumber(serviceInput);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DELIVERY_PARTNER_FOUND,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Update Delivery Partner
    // =========================================================================

    /**
     * Updates an existing Delivery Partner profile.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * PUT / api / delivery - partners / update
     * </pre>
     *
     * @param request Delivery Partner update request.
     *
     * @return updated Delivery Partner.
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_DELIVERY_PARTNER)
    @AuditApi(action = ActionType.UPDATE_DELIVERY_PARTNER, module = ModuleType.DELIVERY_PARTNER, method = MethodType.UPDATE)
    @PutMapping(DeliveryPartnerApiConstants.UPDATE)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> update(
            @RequestBody DeliveryPartnerUpdateRequest request) {

        IServiceInput<DeliveryPartnerUpdateRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerService.update(serviceInput);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DELIVERY_PARTNER_UPDATED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Update Verification Status
    // =========================================================================

    /**
     * Updates the verification status of a Delivery Partner.
     *
     * <p>
     * Verification is an administrative operation and is maintained
     * independently from the Delivery Partner business status and availability.
     * </p>
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * PUT / api / delivery - partners / verification - status
     * </pre>
     *
     * @param request Delivery Partner verification request.
     *
     * @return updated Delivery Partner.
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(action = ActionType.UPDATE_DELIVERY_PARTNER_VERIFICATION_STATUS, module = ModuleType.DELIVERY_PARTNER, method = MethodType.UPDATE)
    @PutMapping(DeliveryPartnerApiConstants.UPDATE_VERIFICATION_STATUS)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updateVerificationStatus(
            @RequestBody DeliveryPartnerVerificationRequest request) {

        IServiceInput<DeliveryPartnerVerificationRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerService
                .updateVerificationStatus(serviceInput);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DELIVERY_PARTNER_VERIFICATION_STATUS_UPDATED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Update Delivery Partner Status
    // =========================================================================

    /**
     * Updates the operational status of a Delivery Partner.
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * PUT / api / delivery - partners / status
     * </pre>
     *
     * @param request Delivery Partner status request.
     *
     * @return updated Delivery Partner.
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_ONLY)
    @AuditApi(action = ActionType.UPDATE_DELIVERY_PARTNER_STATUS, module = ModuleType.DELIVERY_PARTNER, method = MethodType.UPDATE)
    @PutMapping(DeliveryPartnerApiConstants.UPDATE_STATUS)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updateStatus(
            @RequestBody DeliveryPartnerStatusRequest request) {

        IServiceInput<DeliveryPartnerStatusRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerService.updateStatus(serviceInput);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DELIVERY_PARTNER_STATUS_UPDATED,
                serviceOutput.getOutput());
    }

    // =========================================================================
    // Update Availability
    // =========================================================================

    /**
     * Updates the availability of a Delivery Partner.
     *
     * <p>
     * Availability is an operational state and is maintained independently
     * from verification status and business status.
     * </p>
     *
     * <p>
     * Endpoint:
     * </p>
     *
     * <pre>
     * PUT / api / delivery - partners / availability
     * </pre>
     *
     * @param request Delivery Partner availability request.
     *
     * @return updated Delivery Partner.
     */
    @Override
    @PreAuthorize(AuthorizationConstants.ADMIN_OR_DELIVERY_PARTNER)
    @AuditApi(action = ActionType.UPDATE_DELIVERY_PARTNER_AVAILABILITY, module = ModuleType.DELIVERY_PARTNER, method = MethodType.UPDATE)
    @PutMapping(DeliveryPartnerApiConstants.UPDATE_AVAILABILITY)
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updateAvailability(
            @RequestBody DeliveryPartnerAvailabilityRequest request) {

        IServiceInput<DeliveryPartnerAvailabilityRequest> serviceInput = new ServiceInput<>();

        serviceInput.setInput(request);

        serviceInput.setServiceContext(serviceContext);

        IServiceOutput<DeliveryPartnerResponse> serviceOutput = deliveryPartnerService.updateAvailability(serviceInput);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DELIVERY_PARTNER_AVAILABILITY_UPDATED,
                serviceOutput.getOutput());
    }
}