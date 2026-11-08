
package com.foodies.freshmeal.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : RegistrationIdentityReservationReleaseInput
 * =============================================================================
 *
 * Purpose
 * -------
 * Carries the pending registration reference required to release
 * its identity reservation.
 *
 * Responsibilities
 * -----------------
 * - Identifies the pending registration owning the reservation.
 *
 * Business Rules
 * --------------
 * - Only PENDING_REGISTRATION-owned reservations can be released.
 * - USER-owned reservations must never be released through this operation.
 * - Reservation release is performed through conditional physical deletion.
 * - Repeated release attempts must not affect permanent user reservations.
 *
 * Security
 * --------
 * This DTO is strictly an internal service-layer input.
 * Registration ownership must be established by the trusted business workflow.
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationIdentityReservationReleaseInput {

    /**
     * Registration number currently owning the identity reservation.
     */
    private String registrationNumber;
}
