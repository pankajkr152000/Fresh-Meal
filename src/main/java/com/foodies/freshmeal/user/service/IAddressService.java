package com.foodies.freshmeal.user.service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.user.dto.AddressIdRequest;
import com.foodies.freshmeal.user.dto.AddressInputDTO;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.entity.AddressEntity;

/**
 * =============================================================================
 * Address Service
 * =============================================================================
 *
 * Responsibilities ---------------- • Load an existing address. • Create a new
 * AddressEntity. • Generate a business-facing address identifier. • Create and
 * persist a complete address.
 *
 * All service operations follow the common FreshMeal IServiceInput /
 * IServiceOutput architecture.
 * =============================================================================
 */
public interface IAddressService {

	/**
	 * Loads an address using its business identifier.
	 *
	 * @param input address identifier request
	 * @return address entity
	 */
	IServiceOutput<AddressEntity> loadAddress(IServiceInput<AddressIdRequest> input);

	/**
	 * Creates a new AddressEntity instance.
	 *
	 * @param input address creation input
	 * @return newly created address entity
	 */
	IServiceOutput<AddressEntity> createAddressEntity(IServiceInput<AddressInputDTO> input);

	/**
	 * Generates a business-facing address identifier.
	 *
	 * Example: ADRDB0000001
	 *
	 * @param input address creation input
	 * @return generated address identifier
	 */
	IServiceOutput<String> generateAddressId(IServiceInput<AddressInputDTO> input);

	/**
	 * Generates a business-facing address identifier.
	 *
	 * Example: FM-ADR-0000001
	 *
	 * @param input address creation input
	 * @return generated address identifier
	 */
	IServiceOutput<String> generateAddressNumber(IServiceInput<AddressInputDTO> input);

	/**
	 * Creates and persists a customer address.
	 *
	 * @param input address creation input
	 * @return created address response
	 */
	IServiceOutput<AddressResponse> addAddress(IServiceInput<AddressInputDTO> input);

}