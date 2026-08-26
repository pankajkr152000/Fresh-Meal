package com.foodies.freshmeal.user.valueObject;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.GeoLocation;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * AddressSnapshot
 * ============================================================================
 *
 * Historical snapshot of the delivery address used for an order.
 *
 * This should never depend on the customer's current saved address because
 * customers may update or delete their addresses after placing an order.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * House / Flat / Building number.
     */
    @NotBlank
    @Size(max = 100)
    private String houseNumber;

    /**
     * Apartment / Society / Building name.
     */
    @Size(max = 150)
    private String apartmentName;

    /**
     * Street / Road name.
     */
    @NotBlank
    @Size(max = 150)
    private String street;

    /**
     * Area / Locality.
     */
    @NotBlank
    @Size(max = 150)
    private String area;

    /**
     * City.
     */
    @NotBlank
    @Size(max = 100)
    private String city;

    /**
     * District.
     */
    @Size(max = 100)
    private String district;

    /**
     * State.
     */
    @NotBlank
    @Size(max = 100)
    private String state;

    /**
     * Country.
     */
    @Builder.Default
    private String country = "India";

    /**
     * Postal / PIN code.
     */
    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid pincode.")
    private String pincode;

    /**
     * Nearby landmark.
     */
    @Size(max = 200)
    private String landmark;

    /**
     * Delivery contact person.
     */
    @NotBlank
    @Size(max = 100)
    private String contactPersonName;

    /**
     * Delivery contact mobile number.
     */
    @NotBlank
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number.")
    private PhoneNumber contactMobileNumber;

    /**
     * Delivery-specific instructions.
     */
    @Size(max = 500)
    private String deliveryInstruction;

    /**
     * Geographic coordinates of the delivery location.
     */
    @Valid
    private GeoLocation location;

}