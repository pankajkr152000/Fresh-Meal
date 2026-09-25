package com.foodies.freshmeal.delivery.dto;

import com.foodies.freshmeal.delivery.constants.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Delivery Partner Registration Request
 * ============================================================================
 *
 * Represents delivery-partner-specific information supplied during
 * delivery-partner onboarding.
 *
 * <p>
 * This DTO intentionally contains only information that may be supplied by
 * the delivery partner during onboarding. System-controlled information such
 * as partner number, user number, partner code, verification status, business
 * status, availability, and audit information is excluded.
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
public class DeliveryPartnerRegistrationRequest {

    // =========================================================================
    // Vehicle Information
    // =========================================================================

    /**
     * Registration number of the vehicle used by the delivery partner.
     */
    @NotBlank
    @Size(max = 30)
    private String vehicleNumber;

    /**
     * Type of vehicle used by the delivery partner.
     */
    @NotNull
    private VehicleType vehicleType;
}