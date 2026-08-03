package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.GeoLocation;

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
 * Represents the delivery address snapshot captured at the time an order is
 * placed.
 *
 * <p>
 * This is a Value Object and should not be treated as a reusable customer
 * address. It preserves the exact delivery location even if the customer
 * updates or deletes their saved addresses later.
 * </p>
 *
 * Example:
 *
 * <pre>
 * House No      : A-101
 * Apartment     : Green Residency
 * Street        : MG Road
 * Area          : Salt Lake
 * City          : Kolkata
 * State         : West Bengal
 * Country       : India
 * Pincode       : 700091
 * Landmark      : Near City Centre Mall
 * Latitude      : 22.5726
 * Longitude     : 88.3639
 * </pre>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
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
     * Flat / House / Building Number.
     */
    @NotBlank
    @Size(max = 100)
    private String houseNumber;

    /**
     * Apartment / Society / Building Name.
     */
    @Size(max = 150)
    private String apartmentName;

    /**
     * Street / Road Name.
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
     * City name.
     */
    @NotBlank
    @Size(max = 100)
    private String city;

    /**
     * District name.
     */
    @Size(max = 100)
    private String district;

    /**
     * State name.
     */
    @NotBlank
    @Size(max = 100)
    private String state;

    /**
     * Country name.
     */
    @Builder.Default
    private String country = "India";

    /**
     * Postal / ZIP Code.
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
     * Contact person name for delivery.
     */
    @NotBlank
    @Size(max = 100)
    private String contactPersonName;

    /**
     * Contact mobile number.
     */
    @NotBlank
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number.")
    private String contactMobileNumber;

    /**
     * Delivery instructions.
     *
     * Example:
     * - Ring the bell once.
     * - Leave at security gate.
     * - Call before delivery.
     */
    @Size(max = 500)
    private String deliveryInstruction;

    /**
     * geoLocation of delivery location.
     */
    private GeoLocation location;
}
