
package com.foodies.freshmeal.authentication.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationInput;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationReleaseInput;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationTransferInput;
import com.foodies.freshmeal.authentication.entity.RegistrationIdentityReservationEntity;
import com.foodies.freshmeal.authentication.repository.IRegistrationIdentityReservationRepository;
import com.foodies.freshmeal.authentication.service.IRegistrationIdentityReservationService;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.util.FreshMealUtilities;

import lombok.RequiredArgsConstructor;

/**
 * =============================================================================
 * SERVICE : RegistrationIdentityReservationServiceImpl
 * =============================================================================
 *
 * Purpose
 * -------
 * Implements business operations for managing registration identity
 * reservations throughout the FreshMeal registration lifecycle.
 *
 * Responsibilities
 * -----------------
 * - Reserve username, email address, and phone number for pending
 * registrations.
 * - Transfer reservation ownership to permanent users.
 * - Release reservations belonging to pending registrations.
 * - Enforce reservation ownership and lifecycle rules.
 *
 * Business Rules
 * --------------
 * - All three identity attributes must be reserved together.
 * - Identity uniqueness is ultimately enforced by MongoDB unique indexes.
 * - Pending registration ownership is transferred only after successful
 * permanent user creation.
 * - Permanent user reservations must never be released by pending
 * registration cleanup.
 * - Ownership-sensitive operations must be atomic.
 *
 * Transaction Management
 * ----------------------
 * All operations participate in an existing registration workflow
 * transaction when one is active.
 *
 * =============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class RegistrationIdentityReservationServiceImpl
        implements IRegistrationIdentityReservationService {

    /**
     * Registration identity reservation repository.
     */
    private final IRegistrationIdentityReservationRepository registrationIdentityReservationRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public IServiceOutput<Boolean> reserveRegistrationIdentity(
            final IServiceInput<RegistrationIdentityReservationInput> input) {

        /*
         * Validate the service input and execution context.
         */
        final IServiceContext serviceContext = validateReservationInput(input);

        final RegistrationIdentityReservationInput reservationInput = input.getInput();

        /*
         * Extract the registration identity information.
         *
         * Email normalization must remain consistent with UserServiceImpl
         * so the permanent user and reservation use identical email values.
         */
        final String registrationNumber = reservationInput.getRegistrationNumber();

        final String username = reservationInput.getUsername();

        final String normalizedEmail = FreshMealUtilities.normalizeEmail(
                reservationInput.getEmail().getValue());

        final String phoneNumber = reservationInput.getPhoneNumber().getValue();

        validateNormalizedIdentity(username, normalizedEmail, phoneNumber);

        /*
         * Check whether the requested username is already reserved.
         *
         * Both pending registrations and permanent users must prevent
         * another registration from claiming this username.
         */
        if (registrationIdentityReservationRepository
                .findActiveByUsername(username)
                .isPresent()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_USERNAME_ALREADY_RESERVED);
        }

        /*
         * Check whether the requested email is already reserved.
         */
        if (registrationIdentityReservationRepository
                .findActiveByEmail(normalizedEmail)
                .isPresent()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_EMAIL_ALREADY_RESERVED);
        }

        /*
         * Check whether the requested phone number is already reserved.
         */
        if (registrationIdentityReservationRepository
                .findActiveByPhoneNumber(phoneNumber)
                .isPresent()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_PHONE_NUMBER_ALREADY_RESERVED);
        }

        /*
         * Capture the current FreshMeal business timestamp.
         */
        final LocalDateTime createdAt = AppCalendar.getBusinessLocalDateTime();

        /*
         * Resolve the audit actor.
         *
         * Public registration is normally anonymous, so the system actor
         * is used when no authenticated profile is available.
         */
        final String auditUser = resolveAuditUser(serviceContext);

        /*
         * Create the reservation entity through the standard FreshMeal
         * entity factory.
         */
        final RegistrationIdentityReservationEntity reservationEntity = (RegistrationIdentityReservationEntity) EntityFactory
                .createEntity(
                        EntityName.REGISTRATION_IDENTITY_RESERVATION_ENTITY);

        /*
         * Initialize the reservation.
         *
         * The entity establishes PENDING_REGISTRATION ownership internally.
         */
        reservationEntity.initialize(
                username,
                normalizedEmail,
                phoneNumber,
                registrationNumber);

        /*
         * Populate creation and modification audit information.
         */
        reservationEntity.setCreatedAt(createdAt);
        reservationEntity.setUpdatedAt(createdAt);
        reservationEntity.setCreatedBy(auditUser);
        reservationEntity.setUpdatedBy(auditUser);

        /*
         * Persist all identity attributes in one MongoDB document.
         *
         * Independent unique indexes enforce uniqueness under concurrent
         * registration requests.
         */
        registrationIdentityReservationRepository.save(reservationEntity);

        return new ServiceOutput<>(Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public IServiceOutput<Boolean> transferOwnershipToUser(
            final IServiceInput<RegistrationIdentityReservationTransferInput> input) {

        /*
         * Validate the service input and execution context.
         */
        final IServiceContext serviceContext = validateTransferInput(input);

        final RegistrationIdentityReservationTransferInput transferInput = input.getInput();

        /*
         * Capture the ownership transfer audit information.
         */
        final LocalDateTime transferredAt = AppCalendar.getBusinessLocalDateTime();

        final String auditUser = resolveAuditUser(serviceContext);

        /*
         * Transfer ownership through the repository's atomic operation.
         *
         * The repository verifies the existing registration owner before
         * changing the reservation to permanent USER ownership.
         */
        final Optional<RegistrationIdentityReservationEntity> transferredReservation = registrationIdentityReservationRepository
                .transferOwnershipToUser(
                        transferInput.getRegistrationNumber(),
                        transferInput.getUserNumber(),
                        transferredAt,
                        auditUser);

        /*
         * An empty result means the expected pending ownership condition
         * was not satisfied.
         */
        if (transferredReservation.isEmpty()) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_IDENTITY_RESERVATION_OWNERSHIP_MISMATCH);
        }

        return new ServiceOutput<>(Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public IServiceOutput<Boolean> releasePendingReservation(
            final IServiceInput<RegistrationIdentityReservationReleaseInput> input) {

        /*
         * Validate the service input and execution context.
         */
        validateReleaseInput(input);

        final RegistrationIdentityReservationReleaseInput releaseInput = input.getInput();

        /*
         * Physically delete only the reservation currently owned by
         * the specified pending registration.
         *
         * The repository prevents deletion of USER-owned reservations.
         */
        final long deletedCount = registrationIdentityReservationRepository
                .deletePendingReservation(
                        releaseInput.getRegistrationNumber());

        /*
         * Return false when the reservation was already absent or
         * ownership had previously been transferred.
         *
         * This makes repeated cleanup requests safe.
         */
        if (deletedCount > 1L) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_IDENTITY_RESERVATION_RELEASE_FAILED);
        }

        return new ServiceOutput<>(deletedCount == 1L);
    }

    // =========================================================================
    // Private Validation Methods
    // =========================================================================

    /**
     * Validates the reservation creation input and its service context.
     *
     * @param input reservation service input
     * @return validated service context
     */
    private IServiceContext validateReservationInput(
            final IServiceInput<RegistrationIdentityReservationInput> input) {

        if (input == null
                || input.getInput() == null
                || input.getServiceContext() == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        final RegistrationIdentityReservationInput reservationInput = input.getInput();

        if (isBlank(reservationInput.getRegistrationNumber())
                || isBlank(reservationInput.getUsername())
                || reservationInput.getEmail() == null
                || isBlank(reservationInput.getEmail().getValue())
                || reservationInput.getPhoneNumber() == null
                || isBlank(reservationInput.getPhoneNumber().getValue())) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        return input.getServiceContext();
    }

    /**
     * Validates the ownership transfer input and its service context.
     *
     * @param input ownership transfer service input
     * @return validated service context
     */
    private IServiceContext validateTransferInput(
            final IServiceInput<RegistrationIdentityReservationTransferInput> input) {

        if (input == null
                || input.getInput() == null
                || input.getServiceContext() == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        final RegistrationIdentityReservationTransferInput transferInput = input.getInput();

        if (isBlank(transferInput.getRegistrationNumber())
                || isBlank(transferInput.getUserNumber())) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        return input.getServiceContext();
    }

    /**
     * Validates the reservation release input and its service context.
     *
     * @param input reservation release service input
     * @return validated service context
     */
    private IServiceContext validateReleaseInput(
            final IServiceInput<RegistrationIdentityReservationReleaseInput> input) {

        if (input == null
                || input.getInput() == null
                || input.getServiceContext() == null
                || isBlank(input.getInput().getRegistrationNumber())) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        return input.getServiceContext();
    }

    /**
     * Validates identity values after normalization.
     *
     * @param username        requested username
     * @param normalizedEmail normalized email address
     * @param phoneNumber     canonical phone number
     */
    private void validateNormalizedIdentity(
            final String username,
            final String normalizedEmail,
            final String phoneNumber) {

        if (isBlank(username)
                || isBlank(normalizedEmail)
                || isBlank(phoneNumber)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }
    }

    // =========================================================================
    // Private Utility Methods
    // =========================================================================

    /**
     * Resolves the audit actor from the current service context.
     *
     * @param serviceContext current service context
     * @return authenticated user number or system actor
     */
    private String resolveAuditUser(final IServiceContext serviceContext) {

        if (serviceContext.getUserProfile() != null) {

            return serviceContext.getUserProfile().getUserNumber();
        }

        return RepositoryConstants.SYSTEM_USER;
    }

    /**
     * Checks whether a string is null, empty, or whitespace-only.
     *
     * @param value value to inspect
     * @return true when the supplied value is blank
     */
    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }
}
