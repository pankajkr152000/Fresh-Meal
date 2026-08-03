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
 * Snapshot of restaurant details at the time an order is placed.
 *
 * This class preserves historical restaurant information even if the
 * restaurant profile is modified later.
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
     * Restaurant address.
     */
    @Valid
    private Address address;

    /**
     * Restaurant geographical location.
     */
    @Valid
    private GeoLocation geoLocation;

    /**
     * Restaurant logo/image URL.
     */
    private String imageUrl;

    /**
     * FSSAI License Number.
     */
    private String fssaiLicenseNumber;

    /**
     * GST Number.
     */
    private String gstNumber;

    /**
     * Average customer rating at the time of order.
     */
    private Double averageRating;

    /**
     * Total customer ratings.
     */
    private Integer totalRatings;

    /**
     * Indicates whether delivery service is available.
     */
    @Builder.Default
    private Boolean deliveryAvailable = Boolean.TRUE;

    /**
     * Indicates whether takeaway is available.
     */
    @Builder.Default
    private Boolean takeawayAvailable = Boolean.TRUE;

    /**
     * Indicates whether dine-in is available.
     */
    @Builder.Default
    private Boolean dineInAvailable = Boolean.FALSE;

}