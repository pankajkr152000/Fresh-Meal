package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DeliveryPartnerDetailsResponse
 * ============================================================================
 *
 * Represents delivery partner information displayed on the Admin Order
 * Details page.
 *
 * This DTO is an API representation of DeliveryPartnerSnapshot stored inside
 * the order.
 *
 * The delivery partner may be null when the order has not yet been assigned
 * to a delivery partner.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Identification
    // =========================================================================

    /**
     * Delivery partner unique identifier.
     */
    private String deliveryPartnerId;

    /**
     * Rider / employee code.
     */
    private String partnerCode;

    /**
     * Delivery partner name.
     */
    private String partnerName;

    // =========================================================================
    // Contact Information
    // =========================================================================

    /**
     * Delivery partner email address.
     */
    private EmailAddress emailAddress;

    /**
     * Delivery partner contact number.
     */
    private PhoneNumber phoneNumber;

    /**
     * Delivery partner profile image.
     */
    private String profileImageUrl;

    // =========================================================================
    // Vehicle Information
    // =========================================================================

    /**
     * Vehicle registration number.
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