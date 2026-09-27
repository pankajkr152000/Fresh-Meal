package com.foodies.freshmeal.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : Delivery Partner Availability Request
 * ============================================================================
 *
 * <p>
 * Represents a request to change the current operational availability of a
 * delivery partner.
 * </p>
 *
 * <p>
 * Operational availability is intentionally maintained separately from the
 * delivery partner's business lifecycle status and verification status.
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
public class DeliveryPartnerAvailabilityRequest {
    private String partnerNumber;
    // =========================================================================
    // Availability
    // =========================================================================

    /**
     * Requested operational availability.
     *
     * <p>
     * {@code true} indicates that the delivery partner is available for
     * delivery assignments, while {@code false} indicates temporary
     * unavailability.
     * </p>
     */
    @NotNull
    private Boolean available;
}