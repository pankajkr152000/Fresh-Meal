package com.foodies.freshmeal.user.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ApiBaseConstants;
import com.foodies.freshmeal.common.constants.ApiMessageConstants;
import com.foodies.freshmeal.common.constants.AuthorizationConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.user.constants.AddressApiConstants;
import com.foodies.freshmeal.user.controller.IAddressController;
import com.foodies.freshmeal.user.dto.AddressInputDTO;
import com.foodies.freshmeal.user.dto.AddressNumberRequest;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.dto.AddressUpdateRequest;
import com.foodies.freshmeal.user.service.IAddressService;

import jakarta.validation.Valid;

/**
 * ============================================================================
 * Controller : AddressController
 * ============================================================================
 *
 * REST controller responsible for customer address management.
 *
 * <p>
 * The controller receives HTTP requests, prepares the common
 * {@code IServiceInput} structure and delegates business processing to the
 * Address service.
 * </p>
 *
 * <p>
 * The controller does not contain address business rules. Ownership,
 * pincode resolution, default-address handling and address lifecycle
 * decisions belong to the service layer.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(ApiBaseConstants.ADDRESS_BASE_URL)
public class AddressController implements IAddressController {

    private final IAddressService addressService;
    private final IServiceContext serviceContext;

    /**
     * Creates an AddressController.
     *
     * @param addressService address service
     * @param serviceContext request-scoped service context
     */
    public AddressController(
            final IAddressService addressService,
            final IServiceContext serviceContext) {

        this.addressService = addressService;
        this.serviceContext = serviceContext;
    }

    /**
     * Creates a new customer address.
     *
     * @param request address creation request
     * @return created address information
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.ADD_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.CREATE)
    @PostMapping(AddressApiConstants.ADD)
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @Valid @RequestBody final AddressRequest request) {

        final AddressInputDTO addressInputDTO = new AddressInputDTO();
        addressInputDTO.setAddressRequest(request);

        final IServiceInput<AddressInputDTO> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(addressInputDTO);

        final IServiceOutput<AddressResponse> output = addressService.addAddress(input);

        return ApiResponseBuilder.created(
                ApiMessageConstants.ADDRESS_CREATED,
                output.getOutput());
    }

    /**
     * Retrieves an address using its business-facing address number.
     *
     * @param request address number request
     * @return address information
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.VIEW_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.READ)
    @PostMapping(AddressApiConstants.GET_BY_ID)
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressByAddressNumber(
            @Valid @RequestBody final AddressNumberRequest request) {

        final IServiceInput<AddressNumberRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(request);

        final IServiceOutput<AddressResponse> output = addressService.getByAddressNumber(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.ADDRESS_FETCHED,
                output.getOutput());
    }

    /**
     * Retrieves all active addresses belonging to the authenticated customer.
     *
     * @return customer's saved addresses
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.VIEW_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.READ)
    @GetMapping(AddressApiConstants.GET_MY_ADDRESSES)
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses() {

        final IServiceInput<Void> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);

        final IServiceOutput<List<AddressResponse>> output = addressService.getMyAddresses(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.ADDRESS_FETCHED,
                output.getOutput());
    }

    /**
     * Updates an existing customer address.
     *
     * @param request address update request
     * @return updated address information
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.UPDATE_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.UPDATE)
    @PutMapping(AddressApiConstants.UPDATE)
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @Valid @RequestBody final AddressUpdateRequest request) {

        final IServiceInput<AddressUpdateRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(request);

        final IServiceOutput<AddressResponse> output = addressService.updateAddress(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.UPDATED_SUCCESSFULLY,
                output.getOutput());
    }

    /**
     * Sets an address as the default address of the authenticated customer.
     *
     * @param request address number request
     * @return updated address information
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.SET_DEFAULT_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.UPDATE)
    @PutMapping(AddressApiConstants.SET_DEFAULT)
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @Valid @RequestBody final AddressNumberRequest request) {

        final IServiceInput<AddressNumberRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(request);

        final IServiceOutput<AddressResponse> output = addressService.setDefaultAddress(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.DEFAULT_ADDRESS_UPDATED,
                output.getOutput());
    }

    /**
     * Deactivates the specified customer address.
     *
     * @param request address number request
     * @return standardized API response
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.DELETE_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.DELETE)
    @DeleteMapping(AddressApiConstants.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @Valid @RequestBody final AddressNumberRequest request) {

        final IServiceInput<AddressNumberRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(request);

        final IServiceOutput<Void> output = addressService.deleteAddress(input);

        return ApiResponseBuilder.success(
                ApiMessageConstants.ADDRESS_DELETED,
                output.getOutput());
    }
}