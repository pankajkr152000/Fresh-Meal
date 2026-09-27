package com.foodies.freshmeal.delivery.dto;

import java.time.LocalDateTime;

import com.foodies.freshmeal.delivery.constants.DeliveryPartnerStatus;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerVerificationStatus;
import com.foodies.freshmeal.delivery.constants.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Delivery Partner Response
 * ============================================================================
 *
 * <p>
 * Represents the delivery-partner information exposed by the application layer
 * to API consumers.
 * </p>
 *
 * <p>
 * The response contains business-facing delivery-partner information while
 * keeping persistence-specific implementation details outside the API contract.
 * </p>
 *
 * <p>
 * User identity information remains represented through {@code userNumber}.
 * Detailed user information continues to belong to {@code UserEntity} and its
 * corresponding user/profile APIs.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerResponse {

    // =========================================================================
    // Business Identity
    // =========================================================================

    /**
     * Unique business identifier of the delivery partner.
     */
    private String partnerNumber;

    /**
     * Business identifier of the associated FreshMeal user.
     */
    private String userNumber;

    /**
     * Operational/business code assigned to the delivery partner.
     */
    private String partnerCode;

    // =========================================================================
    // Vehicle Information
    // =========================================================================

    /**
     * Registration number of the delivery partner's vehicle.
     */
    private String vehicleNumber;

    /**
     * Type of vehicle used by the delivery partner.
     */
    private VehicleType vehicleType;

    // =========================================================================
    // Verification
    // =========================================================================

    /**
     * Current verification status of the delivery partner.
     */
    private DeliveryPartnerVerificationStatus verificationStatus;

    // =========================================================================
    // Business Status
    // =========================================================================

    /**
     * Current business lifecycle status of the delivery partner.
     */
    private DeliveryPartnerStatus status;

    // =========================================================================
    // Availability
    // =========================================================================

    /**
     * Indicates whether the delivery partner is currently available for
     * delivery assignments.
     */
    private boolean available;

    // =========================================================================
    // Audit Information
    // =========================================================================

    /**
     * Timestamp at which the delivery-partner profile was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp at which the delivery-partner profile was last updated.
     */
    private LocalDateTime updatedAt;
}