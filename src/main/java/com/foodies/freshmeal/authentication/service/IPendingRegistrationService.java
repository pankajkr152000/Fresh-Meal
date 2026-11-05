
package com.foodies.freshmeal.authentication.service;

import com.foodies.freshmeal.authentication.dto.PendingRegistrationCreationInput;
import com.foodies.freshmeal.authentication.entity.PendingRegistrationEntity;
import com.foodies.freshmeal.common.io.service.IServiceContext;

/**
 * ============================================================================
 * PENDING REGISTRATION SERVICE
 * ============================================================================
 *
 * <p>
 * Defines operations for managing temporary user registrations before
 * permanent account creation.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Create and persist pending registrations.</li>
 * <li>Retrieve pending registrations using their business identifier.</li>
 * <li>Manage pending registration lifecycle transitions.</li>
 * <li>Enforce registration expiration.</li>
 * <li>Maintain registration audit information.</li>
 * </ul>
 *
 * <p>
 * This service does not create permanent user accounts and does not manage
 * email verification OTP generation or verification. Those responsibilities
 * belong to their respective services.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IPendingRegistrationService {

    /**
     * Creates and persists a new pending registration.
     *
     * <p>
     * The supplied entity must contain the validated registration information,
     * encoded password, requested role, business identifier and expiration
     * timestamp.
     * </p>
     *
     * <p>
     * The implementation must ensure that an existing active pending
     * registration or permanent account does not result in an unintended
     * duplicate registration.
     * </p>
     *
     * @param pendingRegistrationEntity pending registration to persist
     * @param serviceContext            current service context
     * @return persisted pending registration
     */
    PendingRegistrationEntity createPendingRegistration(
            PendingRegistrationCreationInput pendingRegistrationCreationInput,
            IServiceContext serviceContext);

    /**
     * Retrieves a pending registration using its business identifier.
     *
     * <p>
     * The implementation must validate the registration lifecycle and
     * expiration before allowing the registration to proceed.
     * </p>
     *
     * @param registrationNumber business identifier of the registration
     * @param serviceContext     current service context
     * @return matching pending registration
     */
    PendingRegistrationEntity getPendingRegistration(
            String registrationNumber,
            IServiceContext serviceContext);

    /**
     * Marks a pending registration as successfully completed.
     *
     * <p>
     * This operation is intended to be coordinated with permanent user
     * creation after successful email verification.
     * </p>
     *
     * @param registrationNumber business identifier of the registration
     * @param serviceContext     current service context
     */
    void completePendingRegistration(
            String registrationNumber,
            IServiceContext serviceContext);

    /**
     * Cancels a pending registration.
     *
     * <p>
     * Only registrations eligible for cancellation can be cancelled.
     * The implementation must preserve the registration lifecycle rules.
     * </p>
     *
     * @param registrationNumber business identifier of the registration
     * @param serviceContext     current service context
     */
    void cancelPendingRegistration(
            String registrationNumber,
            IServiceContext serviceContext);

    /**
     * Expires a pending registration when its validity period has elapsed.
     *
     * <p>
     * This operation is intended for explicit lifecycle management,
     * including scheduled cleanup and expiration checks.
     * </p>
     *
     * @param registrationNumber business identifier of the registration
     * @param serviceContext     current service context
     */
    void expirePendingRegistration(
            String registrationNumber,
            IServiceContext serviceContext);
}
