package com.foodies.freshmeal.user.controller;

import org.springframework.http.ResponseEntity;

import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.user.dto.AddressIdRequest;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;

/**
 * =============================================================================
 * Address Controller
 * =============================================================================
 *
 * Responsibilities
 * ----------------
 * • Define HTTP operations for customer addresses.
 * • Receive request data from the client.
 * • Delegate processing to the Address service implementation.
 *
 * The controller implementation is responsible for creating the common
 * IServiceInput structure before invoking the service layer.
 * =============================================================================
 */
public interface IAddressController {

    /**
     * Creates a new customer address.
     *
     * @param request address creation request
     * @return created address information
     */
    ResponseEntity<ApiResponse<AddressResponse>> addAddress(AddressRequest request);

    /**
     * Retrieves an address using its business-facing address number.
     *
     * @param request address identifier request
     * @return address information
     */
    ResponseEntity<ApiResponse<AddressResponse>> getAddressByAddressId(AddressIdRequest request);
}