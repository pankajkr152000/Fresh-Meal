
package com.foodies.freshmeal.authentication.service;

import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationInput;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationReleaseInput;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationTransferInput;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

/**
 * =============================================================================
 * SERVICE : IRegistrationIdentityReservationService
 * =============================================================================
 *
 * Purpose
 * -------
 * Defines business operations for managing registration identity reservations.
 *
 * Responsibilities
 * -----------------
 * - Reserve identity attributes for pending registrations.
 * - Transfer reservation ownership to permanent users.
 * - Release reservations belonging to pending registrations.
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IRegistrationIdentityReservationService {

    /**
     * Reserves username, email address, and phone number for a pending
     * registration.
     *
     * @param input registration identity reservation input
     * @return reservation operation result
     */
    IServiceOutput<Boolean> reserveRegistrationIdentity(
            IServiceInput<RegistrationIdentityReservationInput> input);

    /**
     * Transfers reservation ownership from a pending registration
     * to a permanent user.
     *
     * @param input ownership transfer input
     * @return ownership transfer result
     */
    IServiceOutput<Boolean> transferOwnershipToUser(
            IServiceInput<RegistrationIdentityReservationTransferInput> input);

    /**
     * Releases an identity reservation owned by a pending registration.
     *
     * @param input reservation release input
     * @return reservation release result
     */
    IServiceOutput<Boolean> releasePendingReservation(
            IServiceInput<RegistrationIdentityReservationReleaseInput> input);
}
