package com.foodies.freshmeal.delivery.dto;

import com.foodies.freshmeal.delivery.constants.DeliveryPartnerVerificationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Delivery Partner Verification Request
 * ============================================================================
 *
 * <p>
 * Represents an administrative request to update the verification status of a
 * delivery partner.
 * </p>
 *
 * <p>
 * Verification is an administrative workflow. A delivery partner may register
 * for the delivery-partner role, but cannot verify their own delivery-partner
 * profile.
 * </p>
 *
 * <p>
 * The target delivery partner is resolved separately by the service operation.
 * This DTO contains only the requested verification decision.
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
public class DeliveryPartnerVerificationRequest {
    private String partnerNumber;
    // =========================================================================
    // Verification
    // =========================================================================

    /**
     * Requested verification status.
     *
     * <p>
     * Administrative verification decisions are expected to transition the
     * delivery partner from {@code PENDING} to either {@code VERIFIED} or
     * {@code REJECTED}.
     * </p>
     */
    @NotNull
    private DeliveryPartnerVerificationStatus verificationStatus;
}