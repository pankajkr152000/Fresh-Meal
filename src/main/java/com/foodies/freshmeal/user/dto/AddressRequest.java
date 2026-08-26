package com.foodies.freshmeal.user.dto;

import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.user.constants.AddressTypeConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : AddressRequest
 * =============================================================================
 *
 * Purpose
 * -------
 * Represents the address information supplied by the client.
 *
 * Location fields such as district, state and country are intentionally not
 * accepted from the client. They are resolved by PincodeService using the
 * supplied postal code.
 * =============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    /**
     * User-defined address label.
     *
     * Examples:
     * HOME
     * WORK
     * OTHER
     */
    @NotBlank
    private AddressTypeConstant addressType;

    /**
     * Indicates whether this address should be the user's default address.
     */
    private boolean defaultAddress;

    /**
     * Recipient / contact person name.
     */
    @NotBlank
    @Size(max = 100)
    private String recipientName;

    /**
     * Contact phone number for delivery.
     */
    @NotBlank
    private PhoneNumber phoneNumber;

    /**
     * Flat, apartment, house or building number.
     */
    @NotBlank
    @Size(max = 150)
    private String addressLine1;

    /**
     * Street, road, locality or area.
     */
    
    @Size(max = 150)
    private String addressLine2;

    /**
     * Landmark near the address.
     */
    @Size(max = 200)
    private String landmark;

    /**
     * City entered by the user.
     */
    @NotBlank
    @Size(max = 100)
    private String city;

    /**
     * Postal / PIN code.
     *
     * The pincode is used by PincodeService to resolve:
     * country, state and district.
     */
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid pincode.")
    private String postalCode;
}