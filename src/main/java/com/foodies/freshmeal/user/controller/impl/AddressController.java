package com.foodies.freshmeal.user.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.foodies.freshmeal.user.dto.AddressIdRequest;
import com.foodies.freshmeal.user.dto.AddressInputDTO;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.entity.AddressEntity;
import com.foodies.freshmeal.user.service.IAddressService;

/**
 * ============================================================================
 * Address Controller
 * ============================================================================
 *
 * Responsibilities
 * ----------------
 * • Receive HTTP requests.
 * • Validate request payload.
 * • Prepare IServiceInput.
 * • Delegate business logic to the service layer.
 * • Return standardized ApiResponse.
 *
 * The controller should NEVER contain business logic.
 *
 * ============================================================================
 *
 * {@code /api/addresses}
 *
 * ============================================================================
 */
@RestController
@RequestMapping(ApiBaseConstants.ADDRESS_BASE_URL)
public class AddressController implements IAddressController {

    private final IAddressService addressService;

    private final IServiceContext serviceContext;

    public AddressController(
            IAddressService addressService,
            IServiceContext serviceContext) {

        this.addressService = addressService;
        this.serviceContext = serviceContext;
    }

    /**
     * Creates a new customer address.
     *
     * @param request address details
     * @return created address information
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @AuditApi(action = ActionType.ADD_ADDRESS, module = ModuleType.ADDRESS, method = MethodType.CREATE)
    @PostMapping(AddressApiConstants.ADD)
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(@RequestBody AddressRequest request) {

        IServiceInput<AddressInputDTO> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        AddressInputDTO addressInputDTO = new AddressInputDTO();

        addressInputDTO.setAddressRequest(request);

        input.setInput(addressInputDTO);

        IServiceOutput<AddressResponse> output = addressService.addAddress(input);

        return ApiResponseBuilder.created(
                ApiMessageConstants.ADDRESS_CREATED,
                output.getOutput());
    }

    /**
     * Retrieves an address using its business-facing address number.
     *
     * @param request address identifier request
     * @return address information
     */
    @Override
    @PreAuthorize(AuthorizationConstants.USER_ONLY)
    @PostMapping(AddressApiConstants.GET_BY_ID)
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressByAddressId(@RequestBody AddressIdRequest request) {

        IServiceInput<AddressIdRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(request);

        IServiceOutput<AddressEntity> output = addressService.loadAddress(input);

        AddressEntity addressEntity = output.getOutput();

        AddressResponse response = new AddressResponse();

        response.setAddressNumber(addressEntity.getAddressNumber());
        response.setAddressType(addressEntity.getAddressType());
        response.setDefaultAddress(addressEntity.isDefaultAddress());
        response.setRecipientName(addressEntity.getRecipientName());
        response.setPhoneNumber(addressEntity.getPhoneNumber());
        response.setAddressLine1(addressEntity.getAddressLine1());
        response.setAddressLine2(addressEntity.getAddressLine2());
        response.setLandmark(addressEntity.getLandmark());
        response.setCity(addressEntity.getCity());
        response.setDistrict(addressEntity.getDistrict());
        response.setState(addressEntity.getState());
        response.setCountry(addressEntity.getCountry());
        response.setPostalCode(addressEntity.getPostalCode());
        response.setLocation(addressEntity.getLocation());

        return ApiResponseBuilder.success(
                ApiMessageConstants.ADDRESS_FETCHED,
                response);
    }
}