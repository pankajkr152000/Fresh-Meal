package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
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
 * DeliveryPartnerSnapshot
 * ============================================================================
 *
 * Represents the delivery partner details captured at the time of order
 * assignment.
 *
 * This snapshot preserves the delivery partner information even if the
 * delivery partner profile changes later.
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
public class DeliveryPartnerSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Delivery partner unique identifier.
     */
    @NotBlank
    private String deliveryPartnerId;

    /**
     * Employee / Rider code.
     */
    @NotBlank
    private String partnerCode;

    /**
     * Delivery partner name.
     */
    @NotBlank
    @Size(max = 150)
    private String partnerName;

    /**
     * Delivery partner email.
     */
    @Valid
    private EmailAddress emailAddress;

    /**
     * Delivery partner contact number.
     */
    @Valid
    private PhoneNumber phoneNumber;

    /**
     * Profile image URL.
     */
    private String profileImageUrl;

    /**
     * Vehicle number.
     */
    private String vehicleNumber;

    /**
     * Vehicle type.
     *
     * Example:
     * Bike
     * Bicycle
     * Scooter
     * Car
     */
    private String vehicleType;

    /**
     * Average delivery partner rating.
     */
    private Double averageRating;

    /**
     * Total completed deliveries.
     */
    private Integer totalDeliveries;

    /**
     * Indicates whether the partner was active at the time of assignment.
     */
    @Builder.Default
    private Boolean active = Boolean.TRUE;

}