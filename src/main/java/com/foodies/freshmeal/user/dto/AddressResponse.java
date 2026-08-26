package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.GeoLocation;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.user.constants.AddressTypeConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : AddressResponse
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents a saved customer address returned by the Address module.
 *
 * The response contains the location information resolved from the supplied
 * pincode.
 * =============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    /**
     * Business-facing address identifier.
     */
    private String addressNumber;

    /**
     * User-defined address label.
     */
    private AddressTypeConstant addressType;

    /**
     * Indicates whether this is the user's default address.
     */
    private boolean defaultAddress;

    /**
     * Recipient / contact person name.
     */
    private String recipientName;

    /**
     * Contact phone number for delivery.
     */
    private PhoneNumber phoneNumber;

    /**
     * Flat, apartment, house or building number.
     */
    private String addressLine1;

    /**
     * Street, road, locality or area.
     */
    private String addressLine2;

    /**
     * Landmark near the address.
     */
    private String landmark;

    /**
     * City.
     */
    private String city;

    /**
     * District resolved from the pincode.
     */
    private String district;

    /**
     * State resolved from the pincode.
     */
    private String state;

    /**
     * Country resolved from the pincode.
     */
    private String country;

    /**
     * Postal / PIN code.
     */
    private String postalCode;

    /**
     * Geographic coordinates.
     */
    private GeoLocation location;
}