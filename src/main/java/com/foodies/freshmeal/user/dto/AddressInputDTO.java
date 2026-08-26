package com.foodies.freshmeal.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : AddressInputDTO
 * =============================================================================
 *
 * Purpose
 * -------
 * Internal service-layer input used while creating or updating an address.
 *
 * AddressRequest represents the HTTP request contract, while this DTO
 * represents the data passed through the FreshMeal service layer.
 * =============================================================================
 */
@Getter
@Setter
public class AddressInputDTO {

    /**
     * Address request supplied by the client.
     */
    private AddressRequest addressRequest;

}