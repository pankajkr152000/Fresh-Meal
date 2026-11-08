
package com.foodies.freshmeal.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =============================================================================
 * DTO : RegistrationIdentityReservationTransferInput
 * =============================================================================
 *
 * Purpose
 * -------
 * Carries the ownership references required to transfer a registration
 * identity reservation from a pending registration to a permanent user.
 *
 * Responsibilities
 * -----------------
 * - Identifies the pending registration currently owning the reservation.
 * - Identifies the permanent user receiving reservation ownership.
 *
 * Business Rules
 * --------------
 * - Ownership can only be transferred from PENDING_REGISTRATION to USER.
 * - The supplied registration number must match the current reservation owner.
 * - The target user must already have been created successfully.
 * - An existing USER-owned reservation must never be transferred again.
 *
 * Security
 * --------
 * This DTO is strictly an internal service-layer input.
 * The user number must come from the trusted registration workflow,
 * never directly from an untrusted public request.
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
public class RegistrationIdentityReservationTransferInput {

    /**
     * Registration number currently owning the identity reservation.
     */
    private String registrationNumber;

    /**
     * Permanent user number that will receive reservation ownership.
     */
    private String userNumber;
}
