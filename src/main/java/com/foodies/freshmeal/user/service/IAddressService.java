package com.foodies.freshmeal.user.service;

import java.util.List;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.user.dto.AddressInputDTO;
import com.foodies.freshmeal.user.dto.AddressNumberRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.dto.AddressUpdateRequest;

/**
 * ============================================================================
 * Service : IAddressService
 * ============================================================================
 *
 * Defines business operations for customer address management.
 *
 * <p>
 * The service is responsible for address ownership validation, address
 * lifecycle management, pincode resolution, default-address management and
 * persistence orchestration.
 * </p>
 *
 * <p>
 * Technical operations such as identifier generation and entity construction
 * remain internal implementation details and are intentionally not exposed
 * through this contract.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IAddressService {

    /**
     * Creates a new customer address.
     *
     * @param input address creation input
     * @return created address response
     */
    IServiceOutput<AddressResponse> addAddress(
            IServiceInput<AddressInputDTO> input);

    /**
     * Retrieves an address using its business-facing address number.
     *
     * @param input address number request
     * @return address response
     */
    IServiceOutput<AddressResponse> getByAddressNumber(
            IServiceInput<AddressNumberRequest> input);

    /**
     * Retrieves all active addresses belonging to the authenticated customer.
     *
     * @param input service input containing the authenticated service context
     * @return customer's active addresses
     */
    IServiceOutput<List<AddressResponse>> getMyAddresses(
            IServiceInput<Void> input);

    /**
     * Updates an existing customer address.
     *
     * @param input address update input
     * @return updated address response
     */
    IServiceOutput<AddressResponse> updateAddress(
            IServiceInput<AddressUpdateRequest> input);

    /**
     * Sets an address as the authenticated customer's default address.
     *
     * @param input address number request
     * @return updated address response
     */
    IServiceOutput<AddressResponse> setDefaultAddress(
            IServiceInput<AddressNumberRequest> input);

    /**
     * Deactivates an address belonging to the authenticated customer.
     *
     * <p>
     * The underlying address document is handled through the application's
     * soft-delete lifecycle.
     * </p>
     *
     * @param input address number request
     * @return empty service output after successful deletion
     */
    IServiceOutput<Void> deleteAddress(
            IServiceInput<AddressNumberRequest> input);
}