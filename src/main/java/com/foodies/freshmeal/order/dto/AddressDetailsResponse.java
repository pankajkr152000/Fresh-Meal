package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.GeoLocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * AddressDetailsResponse
 * ============================================================================
 *
 * Represents the delivery address displayed on the Admin Order Details page.
 *
 * This DTO is an API representation of the AddressSnapshot stored inside
 * the order.
 *
 * The address is historical information captured when the order was placed
 * and should not be replaced with the customer's current saved address.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Address
    // =========================================================================

    /**
     * House / flat / building number.
     */
    private String houseNumber;

    /**
     * Apartment / society / building name.
     */
    private String apartmentName;

    /**
     * Street / road name.
     */
    private String street;

    /**
     * Area / locality.
     */
    private String area;

    /**
     * City.
     */
    private String city;

    /**
     * District.
     */
    private String district;

    /**
     * State.
     */
    private String state;

    /**
     * Country.
     */
    private String country;

    /**
     * Postal / PIN code.
     */
    private String pincode;

    /**
     * Nearby landmark.
     */
    private String landmark;

    // =========================================================================
    // Delivery Contact
    // =========================================================================

    /**
     * Person who should receive the delivery.
     */
    private String contactPersonName;

    /**
     * Contact number for delivery.
     */
    private String contactMobileNumber;

    /**
     * Customer's delivery instructions.
     */
    private String deliveryInstruction;

    // =========================================================================
    // Location
    // =========================================================================

    /**
     * Geographical coordinates of the delivery location.
     */
    private GeoLocation location;

}