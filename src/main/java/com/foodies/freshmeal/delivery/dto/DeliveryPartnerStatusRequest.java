package com.foodies.freshmeal.delivery.dto;

import com.foodies.freshmeal.delivery.constants.DeliveryPartnerStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Delivery Partner Status Request
 * ============================================================================
 *
 * <p>
 * Represents a request to change the business lifecycle status of a delivery
 * partner.
 * </p>
 *
 * <p>
 * The delivery partner identifier is intentionally not included in this DTO.
 * The target partner is resolved from the service input/context or endpoint
 * path, while the requested status is supplied by the caller.
 * </p>
 *
 * <p>
 * Verification status and operational availability are separate concerns and
 * must not be modified through this request.
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
public class DeliveryPartnerStatusRequest {
    private String partnerNumber;
    // =========================================================================
    // Business Status
    // =========================================================================

    /**
     * Requested business lifecycle status of the delivery partner.
     */
    @NotNull
    private DeliveryPartnerStatus status;
}