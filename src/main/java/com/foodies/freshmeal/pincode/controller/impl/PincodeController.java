package com.foodies.freshmeal.pincode.controller.impl;

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
import com.foodies.freshmeal.pincode.controller.IPincodeController;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.service.IPincodeService;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

/**
 * =============================================================================
 * Pincode Controller
 * =============================================================================
 *
 * Responsibilities ---------------- • Receive HTTP requests. • Validate request
 * payload. • Prepare IServiceInput for the service layer. • Delegate business
 * logic to the service layer. • Return standardized ApiResponse.
 *
 * The controller should NEVER contain business logic.
 * =============================================================================
 */
@RestController
@RequestMapping(ApiBaseConstants.PINCODE_BASE_URL)
public class PincodeController implements IPincodeController {

    private final IPincodeService pincodeService;
    private final IServiceContext serviceContext;

    public PincodeController(IPincodeService pincodeService, IServiceContext serviceContext) {
        this.pincodeService = pincodeService;
        this.serviceContext = serviceContext;
    }

    /**
     * Looks up location details for the supplied pincode.
     *
     * Example: POST /api/pincodes/lookup
     *
     * { "pincode": "700001" }
     *
     * @param request pincode lookup request
     * @return pincode location details
     */
    @Override
    @PreAuthorize(AuthorizationConstants.IS_AUTHENTICATED)
    @AuditApi(action = ActionType.PINCODE_LOOKUP, method = MethodType.READ, module = ModuleType.PINCODE)
    @PostMapping("/lookup")
    public ResponseEntity<ApiResponse<PincodeDetails>> lookupPincode(@RequestBody PincodeLookupRequest request) {

        IServiceInput<PincodeLookupRequest> input = new ServiceInput<>();
        input.setServiceContext(serviceContext);
        input.setInput(request);

        IServiceOutput<PincodeDetails> output = pincodeService.getPincodeDetails(input);

        return ApiResponseBuilder.success(ApiMessageConstants.PINCODE_DETAILS_FETCHED, output.getOutput());
    }
}