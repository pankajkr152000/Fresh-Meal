package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.GeoLocation;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantDetailsResponse
 * ============================================================================
 *
 * Represents restaurant information displayed on the Admin Order Details page.
 *
 * This DTO is an API representation of the RestaurantSnapshot stored inside
 * the order.
 *
 * The response intentionally remains independent from the persistence
 * Value Object so that the API contract can evolve independently.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Identification
    // =========================================================================

    /**
     * Restaurant unique identifier.
     */
    private String restaurantId;

    /**
     * Restaurant business code.
     */
    private String restaurantCode;

    /**
     * Restaurant display name.
     */
    private String restaurantName;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * Restaurant email address.
     */
    private EmailAddress emailAddress;

    /**
     * Restaurant contact number.
     */
    private PhoneNumber phoneNumber;

    // =========================================================================
    // Location
    // =========================================================================

    /**
     * Restaurant address captured with the order.
     */
    private Address address;

    /**
     * Restaurant geographical location.
     */
    private GeoLocation geoLocation;

    // =========================================================================
    // Business Information
    // =========================================================================

    /**
     * Restaurant logo/image URL.
     */
    private String imageUrl;

    /**
     * FSSAI license number.
     */
    private String fssaiLicenseNumber;

    /**
     * GST number.
     */
    private String gstNumber;

}