package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.Address;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.GeoLocation;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantSnapshot
 * ============================================================================
 *
 * Historical snapshot of restaurant information captured when the order
 * was placed.
 *
 * This prevents historical orders from changing when restaurant master data
 * changes later.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Restaurant unique identifier.
     */
    @NotBlank
    private String restaurantId;

    /**
     * Restaurant code.
     */
    @NotBlank
    private String restaurantCode;

    /**
     * Restaurant name.
     */
    @NotBlank
    @Size(max = 150)
    private String restaurantName;

    /**
     * Restaurant email.
     */
    @Valid
    private EmailAddress emailAddress;

    /**
     * Restaurant contact number.
     */
    @Valid
    private PhoneNumber phoneNumber;

    /**
     * Restaurant address snapshot.
     */
    @Valid
    private Address address;

    /**
     * Restaurant location snapshot.
     */
    @Valid
    private GeoLocation geoLocation;

    /**
     * Restaurant logo/image.
     */
    private String imageUrl;

    /**
     * FSSAI license number.
     *
     * Useful for compliance/invoice requirements.
     */
    private String fssaiLicenseNumber;

    /**
     * GST number.
     */
    private String gstNumber;

}