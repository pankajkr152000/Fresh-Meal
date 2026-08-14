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
 * Historical snapshot of the delivery partner assigned to the order.
 *
 * This object is null until a delivery partner is assigned.
 *
 * ============================================================================
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
     * Rider / employee code.
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
     * Delivery partner phone.
     */
    @Valid
    private PhoneNumber phoneNumber;

    /**
     * Profile image.
     */
    private String profileImageUrl;

    /**
     * Vehicle number.
     */
    private String vehicleNumber;

    /**
     * Vehicle type.
     *
     * Examples:
     *
     * BIKE
     * SCOOTER
     * BICYCLE
     * CAR
     */
    private String vehicleType;

}