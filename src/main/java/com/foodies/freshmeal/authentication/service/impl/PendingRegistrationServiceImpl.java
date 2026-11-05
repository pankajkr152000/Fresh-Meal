package com.foodies.freshmeal.authentication.service.impl;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.dto.PendingRegistrationCreationInput;
import com.foodies.freshmeal.authentication.entity.PendingRegistrationEntity;
import com.foodies.freshmeal.authentication.repository.IPendingRegistrationRepository;
import com.foodies.freshmeal.authentication.service.IPendingRegistrationService;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
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
     * Default validity period of a pending registration, in hours.
     */
    private static final long PENDING_REGISTRATION_VALIDITY_HOURS = 24L;

    /**
     * {@inheritDoc}
     */
    @Override
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
         *
         * Additional domain-level validation is performed by
         * PendingRegistrationEntity.initialize().
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
         * Use the configured FreshMeal business clock.
         *
         * Pending registrations remain valid for 24 hours.
         */
        final LocalDateTime createdAt = AppCalendar.getBusinessLocalDateTime();

        final LocalDateTime expiresAt = createdAt.plusHours(24);

        /*
         * Generate a unique business-facing registration number.
         *
         * The internal MongoDB identifier remains managed by the
         * established persistence infrastructure.
         */
        final long sequence = databaseSequenceService.generateSequence(
                serviceContext,
                SequenceConstants.PENDING_REGISTRATION_SEQUENCE);

        final String registrationNumber = String.format(
                Locale.ROOT,
                SequenceConstants.PENDING_REGISTRATION_NUMBER_PATTERN,
                sequence);

        /*
         * Create the entity through the standard FreshMeal entity factory.
         */
        final PendingRegistrationEntity pendingRegistrationEntity = (PendingRegistrationEntity) EntityFactory
                .createEntity(EntityName.PENDING_REGISTRATION_ENTITY);

        /*
         * Initialize the pending registration.
         *
         * The entity enforces its own initialization and lifecycle rules.
         */
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
         *
         * Registration is normally initiated by an unauthenticated user,
         * so the system user is used when no authenticated profile exists.
         */
        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setCreatedAt(createdAt);
        pendingRegistrationEntity.setUpdatedAt(createdAt);
        pendingRegistrationEntity.setCreatedBy(auditUser);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        /*
         * Persist and return the newly created pending registration.
         */
        return pendingRegistrationRepository.save(pendingRegistrationEntity);
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
    public void cancelPendingRegistration(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        final PendingRegistrationEntity pendingRegistrationEntity = getPendingRegistration(registrationNumber,
                serviceContext);

        final LocalDateTime cancelledAt = AppCalendar.getBusinessLocalDateTime();

        pendingRegistrationEntity.cancelRegistration(cancelledAt);

        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setUpdatedAt(cancelledAt);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        pendingRegistrationRepository.save(pendingRegistrationEntity);
    }

    @Override
    public void expirePendingRegistration(
            final String registrationNumber,
            final IServiceContext serviceContext) {

        if (registrationNumber == null || registrationNumber.isBlank()
                || serviceContext == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        final PendingRegistrationEntity pendingRegistrationEntity = getPendingRegistration(registrationNumber,
                serviceContext);

        final LocalDateTime expiredAt = AppCalendar.getBusinessLocalDateTime();

        final boolean expired = pendingRegistrationEntity.expireRegistration(expiredAt);

        if (!expired) {
            return;
        }

        final String auditUser = serviceContext.getUserProfile() != null
                ? serviceContext.getUserProfile().getUserNumber()
                : RepositoryConstants.SYSTEM_USER;

        pendingRegistrationEntity.setUpdatedAt(expiredAt);
        pendingRegistrationEntity.setUpdatedBy(auditUser);

        pendingRegistrationRepository.save(pendingRegistrationEntity);
    }
}