package com.foodies.freshmeal.authentication.service.impl;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.dto.PendingRegistrationCreationInput;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationInput;
import com.foodies.freshmeal.authentication.dto.RegistrationIdentityReservationReleaseInput;
import com.foodies.freshmeal.authentication.entity.PendingRegistrationEntity;
import com.foodies.freshmeal.authentication.repository.IPendingRegistrationRepository;
import com.foodies.freshmeal.authentication.service.IPendingRegistrationService;
import com.foodies.freshmeal.authentication.service.IRegistrationIdentityReservationService;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;

import lombok.RequiredArgsConstructor;

/**
 * ============================================================================
 * Service : PendingRegistrationServiceImpl
 * ============================================================================
 *
 * <p>
 * Manages the lifecycle and persistence of temporary pending registrations.
 * </p>
 *
 * <p>
 * This service is responsible for creating and managing pending registration
 * records. OTP generation, OTP verification, email delivery, and permanent
 * user creation are handled by their respective services.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Service
@RequiredArgsConstructor
public class PendingRegistrationServiceImpl implements IPendingRegistrationService {

    /**
     * Pending registration repository.
     */
    private final IPendingRegistrationRepository pendingRegistrationRepository;

    /**
     * Database sequence service.
     */
    private final IDatabaseSequenceService databaseSequenceService;

    /**
     * Registration identity reservation service.
     *
     * Manages the exclusive reservation of username, email and phone number
     * throughout the pending registration lifecycle.
     */
    private final IRegistrationIdentityReservationService registrationIdentityReservationService;

    /**
     * Default validity period of a pending registration, in hours.
     */
    private static final long PENDING_REGISTRATION_VALIDITY_HOURS = 24L;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PendingRegistrationEntity createPendingRegistration(
            final PendingRegistrationCreationInput input,
            final IServiceContext serviceContext) {

        /*
         * Validate the service input and execution context.
         */
        if (input == null || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Validate mandatory registration information.
         */
        if (input.getUsername() == null || input.getUsername().isBlank()
                || input.getEmail() == null
                || input.getEncodedPassword() == null
                || input.getEncodedPassword().isBlank()
                || input.getRequestedRole() == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Capture the business timestamp and calculate the registration expiry.
         */
        final LocalDateTime createdAt = AppCalendar.getBusinessLocalDateTime();

        final LocalDateTime expiresAt = createdAt.plusHours(
                PENDING_REGISTRATION_VALIDITY_HOURS);

        /*
         * Generate the business-facing registration number.
         */
        final long sequence = databaseSequenceService.generateSequence(
                serviceContext,
                SequenceConstants.PENDING_REGISTRATION_SEQUENCE);

        final String registrationNumber = String.format(
                Locale.ROOT,
                SequenceConstants.PENDING_REGISTRATION_NUMBER_PATTERN,
                sequence);

        /*
         * Create and initialize the pending registration entity.
         */
        final PendingRegistrationEntity pendingRegistrationEntity = (PendingRegistrationEntity) EntityFactory
                .createEntity(
                        EntityName.PENDING_REGISTRATION_ENTITY);

        pendingRegistrationEntity.initialize(
                registrationNumber,
                input.getUsername(),
                input.getFirstName(),
                input.getLastName(),
                input.getEmail(),
                input.getPhoneNumber(),
                input.getEncodedPassword(),
                input.getRequestedRole(),
                expiresAt);

        /*
         * Populate the standard audit fields.
         */
        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setCreatedAt(createdAt);
        pendingRegistrationEntity.setUpdatedAt(createdAt);
        pendingRegistrationEntity.setCreatedBy(auditUser);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        /*
         * Persist the pending registration first.
         *
         * The identity reservation references this registration number.
         * Both operations participate in the same MongoDB transaction.
         */
        final PendingRegistrationEntity savedPendingRegistration = pendingRegistrationRepository
                .save(pendingRegistrationEntity);

        /*
         * Prepare the identity reservation request.
         */
        final RegistrationIdentityReservationInput reservationInput = RegistrationIdentityReservationInput.builder()
                .registrationNumber(registrationNumber)
                .username(input.getUsername())
                .email(input.getEmail())
                .phoneNumber(input.getPhoneNumber())
                .build();

        /*
         * Reserve username, email and phone number.
         *
         * Duplicate identity attributes are rejected by the reservation service.
         * MongoDB unique indexes remain the final concurrency safeguard.
         */
        final IServiceInput<RegistrationIdentityReservationInput> reservationServiceInput = new ServiceInput<>(
                reservationInput, serviceContext);

        registrationIdentityReservationService.reserveRegistrationIdentity(
                reservationServiceInput);

        /*
         * Return the persisted pending registration.
         */
        return savedPendingRegistration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PendingRegistrationEntity getPendingRegistration(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        /*
         * Validate the registration identifier and service context.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Build a query using the business-facing registration number.
         *
         * The repository automatically applies active-record filtering,
         * excluding logically deleted registrations.
         */
        final Query query = Query.query(
                Criteria.where("registrationNumber").is(registrationNumber));

        /*
         * Retrieve the pending registration.
         *
         * A registration that does not exist or has been soft-deleted
         * is treated as not found.
         */
        return pendingRegistrationRepository.findOne(query)
                .orElseThrow(() -> new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_NOT_FOUND));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void completePendingRegistration(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        /*
         * Validate the registration identifier and service context.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Retrieve the existing pending registration.
         */
        final PendingRegistrationEntity pendingRegistrationEntity = getPendingRegistration(registrationNumber,
                serviceContext);

        /*
         * Capture the current business timestamp.
         */
        final LocalDateTime completedAt = AppCalendar.getBusinessLocalDateTime();

        /*
         * Apply the registration lifecycle transition.
         *
         * The entity validates its current state and expiration.
         */
        pendingRegistrationEntity.completeRegistration(completedAt);

        /*
         * Remove the temporary encoded password after successful completion.
         */
        pendingRegistrationEntity.clearEncodedPassword();

        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setUpdatedAt(completedAt);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        /*
         * Persist the completed registration.
         */
        pendingRegistrationRepository.save(pendingRegistrationEntity);
    }

    @Override
    @Transactional
    public void cancelPendingRegistration(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        /*
         * Validate the registration identifier and service context.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Retrieve the pending registration.
         */
        final PendingRegistrationEntity pendingRegistrationEntity = getPendingRegistration(registrationNumber,
                serviceContext);

        /*
         * Capture the cancellation timestamp.
         */
        final LocalDateTime cancelledAt = AppCalendar.getBusinessLocalDateTime();

        /*
         * Apply the domain lifecycle transition.
         */
        pendingRegistrationEntity.cancelRegistration(cancelledAt);

        /*
         * Populate audit information.
         */
        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setUpdatedAt(cancelledAt);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        /*
         * Persist the cancellation.
         */
        pendingRegistrationRepository.save(pendingRegistrationEntity);

        /*
         * Release the identity reservation owned by this registration.
         *
         * Both operations participate in the same transaction.
         */
        releaseIdentityReservation(registrationNumber, serviceContext);
    }

    @Override
    @Transactional
    public void expirePendingRegistration(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        /*
         * Validate the registration identifier and service context.
         */
        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        /*
         * Retrieve the pending registration.
         */
        final PendingRegistrationEntity pendingRegistrationEntity = getPendingRegistration(registrationNumber,
                serviceContext);

        /*
         * Capture the expiration timestamp.
         */
        final LocalDateTime expiredAt = AppCalendar.getBusinessLocalDateTime();

        /*
         * Apply the domain lifecycle transition.
         *
         * The entity determines whether the registration is eligible for expiration.
         */
        final boolean expired = pendingRegistrationEntity.expireRegistration(expiredAt);

        /*
         * Do not release the reservation if no lifecycle transition occurred.
         */
        if (!expired) {
            return;
        }

        /*
         * Populate audit information.
         */
        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setUpdatedAt(expiredAt);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        /*
         * Persist the expired registration.
         */
        pendingRegistrationRepository.save(pendingRegistrationEntity);

        /*
         * Release the identity reservation owned by this registration.
         *
         * Both operations participate in the same transaction.
         */
        releaseIdentityReservation(registrationNumber, serviceContext);
    }

    /**
     * Releases the identity reservation belonging to a pending registration.
     *
     * <p>
     * The reservation service performs an ownership-conditional physical deletion.
     * Consequently, this operation cannot release a reservation already transferred
     * to a permanent user.
     * </p>
     *
     * @param registrationNumber pending registration business identifier
     * @param serviceContext     current service execution context
     */
    private void releaseIdentityReservation(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        final RegistrationIdentityReservationReleaseInput releaseInput = RegistrationIdentityReservationReleaseInput
                .builder()
                .registrationNumber(registrationNumber)
                .build();

        final IServiceInput<RegistrationIdentityReservationReleaseInput> serviceInput = new ServiceInput<>(releaseInput,
                serviceContext);

        registrationIdentityReservationService.releasePendingReservation(serviceInput);
    }
}