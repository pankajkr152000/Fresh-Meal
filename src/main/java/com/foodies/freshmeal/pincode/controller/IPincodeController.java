package com.foodies.freshmeal.pincode.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;

/**
 * =============================================================================
 * Pincode Controller Contract
 * =============================================================================
 *
 * Responsibilities
 * ----------------
 * • Define HTTP operations exposed by the Pincode module.
 * • Keep the controller contract separate from its implementation.
 * • Return the application's standardized ApiResponse structure.
 *
 * The implementation is responsible for delegating the request to the
 * Pincode service layer.
 * =============================================================================
 */
public interface IPincodeController {

    /**
     * Looks up location details for the supplied pincode.
     *
     * @param request pincode lookup request
     * @return standardized response containing pincode details
     */
    ResponseEntity<ApiResponse<PincodeDetails>> lookupPincode(
            PincodeLookupRequest request);

}