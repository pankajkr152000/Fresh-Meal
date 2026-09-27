package com.foodies.freshmeal.delivery.service.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerErrorConstants;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerStatus;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerVerificationStatus;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerAvailabilityRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerStatusRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerUpdateRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerVerificationRequest;
import com.foodies.freshmeal.delivery.entity.DeliveryPartnerEntity;
import com.foodies.freshmeal.delivery.mapper.DeliveryPartnerMapper;
import com.foodies.freshmeal.delivery.repository.IDeliveryPartnerRepository;
import com.foodies.freshmeal.delivery.service.IDeliveryPartnerService;
import com.foodies.freshmeal.user.entity.UserProfile;

/**
 * ============================================================================
 * Service Implementation : Delivery Partner
 * ============================================================================
 *
 * <p>
 * Provides business operations for Delivery Partner management.
 * </p>
 *
 * <p>
 * Delivery Partner onboarding is intentionally handled by
 * {@code DeliveryPartnerOnboardingServiceImpl}. This service is responsible
 * for managing an already established Delivery Partner profile.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Delivery Partner retrieval</li>
 * <li>Delivery Partner profile update</li>
 * <li>Delivery Partner verification management</li>
 * <li>Delivery Partner business status management</li>
 * <li>Delivery Partner operational availability management</li>
 * </ul>
 *
 * <h3>Lifecycle Separation</h3>
 *
 * <p>
 * Delivery Partner verification, business status and operational availability
 * are intentionally maintained as separate concerns.
 * </p>
 *
 * <ul>
 * <li>Verification determines whether the partner has passed verification.</li>
 * <li>Status determines the partner's business lifecycle state.</li>
 * <li>Availability determines whether the partner is currently available for
 * operational delivery work.</li>
 * </ul>
 *
 * <p>
 * Verification does not automatically activate the Delivery Partner and
 * activation does not automatically make the Delivery Partner available.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class DeliveryPartnerServiceImpl implements IDeliveryPartnerService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DeliveryPartnerServiceImpl.class);

    /**
     * Delivery Partner repository.
     */
    private final IDeliveryPartnerRepository deliveryPartnerRepository;

    /**
     * Delivery Partner mapper.
     */
    private final DeliveryPartnerMapper deliveryPartnerMapper;

    /**
     * Creates the Delivery Partner management service.
     *
     * @param deliveryPartnerRepository Delivery Partner repository
     * @param deliveryPartnerMapper     Delivery Partner mapper
     */
    public DeliveryPartnerServiceImpl(
            final IDeliveryPartnerRepository deliveryPartnerRepository,
            final DeliveryPartnerMapper deliveryPartnerMapper) {

        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.deliveryPartnerMapper = deliveryPartnerMapper;
    }

    // =========================================================================
    // Get By ID
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> getById(
            final IServiceInput<String> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner service input must not be null.");

        final String partnerId = Objects.requireNonNull(
                input.getInput(),
                "Delivery Partner id must not be null.");

        if (partnerId.isBlank()) {
            throw new IllegalArgumentException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_ID_REQUIRED
                            .getErrorMessage());
        }

        final DeliveryPartnerEntity deliveryPartner =
                deliveryPartnerRepository.findActiveById(partnerId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                DeliveryPartnerErrorConstants.DELIVERY_PARTNER_NOT_FOUND));

        validateSelfOwnership(input, deliveryPartner);

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(deliveryPartner));
    }

    // =========================================================================
    // Get By User Number
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> getByUserNumber(
            final IServiceInput<String> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner service input must not be null.");

        final String userNumber = Objects.requireNonNull(
                input.getInput(),
                "Delivery Partner user number must not be null.");

        if (userNumber.isBlank()) {
            throw new IllegalArgumentException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_USER_REQUIRED
                            .getErrorMessage());
        }

        /*
         * Use the active-query infrastructure instead of the repository's
         * derived findByUserNumber method so that soft-deleted records are
         * excluded from management operations.
         */
        final DeliveryPartnerEntity deliveryPartner =
                loadActiveByUserNumber(userNumber);

        validateSelfOwnership(input, deliveryPartner);

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(deliveryPartner));
    }

    // =========================================================================
    // Update Profile
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> update(
            final IServiceInput<DeliveryPartnerUpdateRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner service input must not be null.");

        final DeliveryPartnerUpdateRequest request =
                Objects.requireNonNull(
                        input.getInput(),
                        "Delivery Partner update request must not be null.");

        final DeliveryPartnerEntity deliveryPartner =
                loadActiveByPartnerNumber(request.getPartnerNumber());

        validateSelfOwnership(input, deliveryPartner);

        // ---------------------------------------------------------------------
        // Update editable business information
        // ---------------------------------------------------------------------

        deliveryPartnerMapper.mapUpdateRequestToEntity(
                request,
                deliveryPartner);

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        final DeliveryPartnerEntity savedDeliveryPartner =
                deliveryPartnerRepository.save(deliveryPartner);

        LOGGER.info(
                "Delivery Partner updated successfully. "
                        + "partnerNumber=[{}]",
                savedDeliveryPartner.getPartnerNumber());

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(savedDeliveryPartner));
    }

    // =========================================================================
    // Update Verification Status
    // =========================================================================

    /**
     * {@inheritDoc}
     *
     * <p>
     * Verification is an administrative decision and is deliberately kept
     * separate from business status and operational availability.
     * </p>
     *
     * <p>
     * Supported verification transitions are:
     * </p>
     *
     * <pre>
     * PENDING → VERIFIED
     * PENDING → REJECTED
     * </pre>
     *
     * <p>
     * Verification does not automatically change the business status or
     * operational availability.
     * </p>
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> updateVerificationStatus(
            final IServiceInput<DeliveryPartnerVerificationRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner verification input must not be null.");

        final DeliveryPartnerVerificationRequest request =
                Objects.requireNonNull(
                        input.getInput(),
                        "Delivery Partner verification request must not be null.");

        validateAdminAccess(input);

        final DeliveryPartnerEntity deliveryPartner =
                loadActiveByPartnerNumber(request.getPartnerNumber());

        final DeliveryPartnerVerificationStatus currentStatus =
                Objects.requireNonNull(
                        deliveryPartner.getVerificationStatus(),
                        "Current Delivery Partner verification status must not be null.");

        final DeliveryPartnerVerificationStatus requestedStatus =
                Objects.requireNonNull(
                        request.getVerificationStatus(),
                        DeliveryPartnerErrorConstants.VERIFICATION_STATUS_REQUIRED
                                .getErrorMessage());

        // ---------------------------------------------------------------------
        // Validate duplicate verification status
        // ---------------------------------------------------------------------

        if (currentStatus == requestedStatus) {

            if (requestedStatus ==
                    DeliveryPartnerVerificationStatus.VERIFIED) {

                throw new IllegalStateException(
                        DeliveryPartnerErrorConstants
                                .DELIVERY_PARTNER_ALREADY_VERIFIED
                                .getErrorMessage());
            }

            if (requestedStatus ==
                    DeliveryPartnerVerificationStatus.REJECTED) {

                throw new IllegalStateException(
                        DeliveryPartnerErrorConstants
                                .DELIVERY_PARTNER_ALREADY_REJECTED
                                .getErrorMessage());
            }

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .INVALID_VERIFICATION_TRANSITION
                            .getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Validate verification transition
        // ---------------------------------------------------------------------

        validateVerificationTransition(
                currentStatus,
                requestedStatus);

        // ---------------------------------------------------------------------
        // Apply verification decision
        // ---------------------------------------------------------------------

        deliveryPartner.setVerificationStatus(requestedStatus);

        /*
         * Verification must not automatically activate or enable the partner.
         *
         * If a previously active partner is rejected, availability is revoked
         * immediately because a rejected partner must not remain operationally
         * available.
         */
        if (requestedStatus !=
                DeliveryPartnerVerificationStatus.VERIFIED) {

            deliveryPartner.setAvailable(false);
        }

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        final DeliveryPartnerEntity savedDeliveryPartner =
                deliveryPartnerRepository.save(deliveryPartner);

        LOGGER.info(
                "Delivery Partner verification updated. "
                        + "partnerNumber=[{}], oldVerificationStatus=[{}], "
                        + "newVerificationStatus=[{}]",
                savedDeliveryPartner.getPartnerNumber(),
                currentStatus,
                requestedStatus);

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(savedDeliveryPartner));
    }

    // =========================================================================
    // Update Business Status
    // =========================================================================

    /**
     * {@inheritDoc}
     *
     * <p>
     * Business status remains independent from verification and availability.
     * A Delivery Partner must be verified before becoming ACTIVE.
     * </p>
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> updateStatus(
            final IServiceInput<DeliveryPartnerStatusRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner status input must not be null.");

        final DeliveryPartnerStatusRequest request =
                Objects.requireNonNull(
                        input.getInput(),
                        "Delivery Partner status request must not be null.");

        final DeliveryPartnerEntity deliveryPartner =
                loadActiveByPartnerNumber(request.getPartnerNumber());

        final DeliveryPartnerStatus currentStatus =
                Objects.requireNonNull(
                        deliveryPartner.getStatus(),
                        "Current Delivery Partner status must not be null.");

        final DeliveryPartnerStatus requestedStatus =
                Objects.requireNonNull(
                        request.getStatus(),
                        DeliveryPartnerErrorConstants.STATUS_REQUIRED
                                .getErrorMessage());

        // ---------------------------------------------------------------------
        // Validate duplicate status
        // ---------------------------------------------------------------------

        if (currentStatus == requestedStatus) {

            throw new IllegalStateException(
                    getAlreadyStatusError(currentStatus)
                            .getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Verification prerequisite
        // ---------------------------------------------------------------------

        if (requestedStatus == DeliveryPartnerStatus.ACTIVE
                && deliveryPartner.getVerificationStatus()
                        != DeliveryPartnerVerificationStatus.VERIFIED) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_VERIFICATION_REQUIRED
                            .getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Validate lifecycle transition
        // ---------------------------------------------------------------------

        validateStatusTransition(
                currentStatus,
                requestedStatus);

        // ---------------------------------------------------------------------
        // Apply status
        // ---------------------------------------------------------------------

        deliveryPartner.setStatus(requestedStatus);

        /*
         * Only ACTIVE partners can remain operationally available.
         */
        if (requestedStatus != DeliveryPartnerStatus.ACTIVE) {
            deliveryPartner.setAvailable(false);
        }

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        final DeliveryPartnerEntity savedDeliveryPartner =
                deliveryPartnerRepository.save(deliveryPartner);

        LOGGER.info(
                "Delivery Partner status updated. "
                        + "partnerNumber=[{}], oldStatus=[{}], newStatus=[{}]",
                savedDeliveryPartner.getPartnerNumber(),
                currentStatus,
                requestedStatus);

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(savedDeliveryPartner));
    }

    // =========================================================================
    // Update Availability
    // =========================================================================

    /**
     * {@inheritDoc}
     *
     * <p>
     * Availability can only be enabled for a verified and active Delivery
     * Partner.
     * </p>
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> updateAvailability(
            final IServiceInput<DeliveryPartnerAvailabilityRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner availability input must not be null.");

        final DeliveryPartnerAvailabilityRequest request =
                Objects.requireNonNull(
                        input.getInput(),
                        "Delivery Partner availability request must not be null.");

        final DeliveryPartnerEntity deliveryPartner =
                loadActiveByPartnerNumber(request.getPartnerNumber());

        validateSelfOwnership(input, deliveryPartner);

        final Boolean requestedAvailability =
                Objects.requireNonNull(
                        request.getAvailable(),
                        DeliveryPartnerErrorConstants.AVAILABILITY_REQUIRED
                                .getErrorMessage());

        // ---------------------------------------------------------------------
        // Availability can only be enabled after verification
        // ---------------------------------------------------------------------

        if (requestedAvailability
                && deliveryPartner.getVerificationStatus()
                        != DeliveryPartnerVerificationStatus.VERIFIED) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_NOT_VERIFIED_FOR_AVAILABILITY
                            .getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Availability can only be enabled while ACTIVE
        // ---------------------------------------------------------------------

        if (requestedAvailability
                && deliveryPartner.getStatus()
                        != DeliveryPartnerStatus.ACTIVE) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_NOT_ACTIVE_FOR_AVAILABILITY
                            .getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Validate duplicate availability
        // ---------------------------------------------------------------------

        if (deliveryPartner.isAvailable() == requestedAvailability) {

            if (requestedAvailability) {

                throw new IllegalStateException(
                        DeliveryPartnerErrorConstants
                                .DELIVERY_PARTNER_ALREADY_AVAILABLE
                                .getErrorMessage());
            }

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_ALREADY_UNAVAILABLE
                            .getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Apply availability
        // ---------------------------------------------------------------------

        deliveryPartner.setAvailable(requestedAvailability);

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        final DeliveryPartnerEntity savedDeliveryPartner =
                deliveryPartnerRepository.save(deliveryPartner);

        LOGGER.info(
                "Delivery Partner availability updated. "
                        + "partnerNumber=[{}], available=[{}]",
                savedDeliveryPartner.getPartnerNumber(),
                savedDeliveryPartner.isAvailable());

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(savedDeliveryPartner));
    }

    // =========================================================================
    // Repository Helpers
    // =========================================================================

    /**
     * Loads an active Delivery Partner using the business-facing partner
     * number.
     *
     * @param partnerNumber Delivery Partner business number
     *
     * @return active Delivery Partner entity
     *
     * @throws ResourceNotFoundException when the partner cannot be found
     */
    private DeliveryPartnerEntity loadActiveByPartnerNumber(
            final String partnerNumber) {

        if (partnerNumber == null || partnerNumber.isBlank()) {

            throw new IllegalArgumentException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_ID_REQUIRED
                            .getErrorMessage());
        }

        final Query query = Query.query(
                Criteria.where("partnerNumber").is(partnerNumber));

        return deliveryPartnerRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(
                        DeliveryPartnerErrorConstants.DELIVERY_PARTNER_NOT_FOUND));
    }

    /**
     * Loads an active Delivery Partner using the associated user number.
     *
     * <p>
     * Uses the active-query infrastructure so that soft-deleted Delivery
     * Partner records are not returned by management operations.
     * </p>
     *
     * @param userNumber associated FreshMeal user number
     *
     * @return active Delivery Partner entity
     *
     * @throws ResourceNotFoundException when the partner cannot be found
     */
    private DeliveryPartnerEntity loadActiveByUserNumber(
            final String userNumber) {

        if (userNumber == null || userNumber.isBlank()) {

            throw new IllegalArgumentException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_USER_REQUIRED
                            .getErrorMessage());
        }

        final Query query = Query.query(
                Criteria.where("userNumber").is(userNumber));

        return deliveryPartnerRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(
                        DeliveryPartnerErrorConstants.DELIVERY_PARTNER_NOT_FOUND));
    }

    // =========================================================================
    // Verification Transition Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner verification transition.
     *
     * @param currentStatus   current verification status
     * @param requestedStatus requested verification status
     */
    private void validateVerificationTransition(
            final DeliveryPartnerVerificationStatus currentStatus,
            final DeliveryPartnerVerificationStatus requestedStatus) {

        final boolean validTransition =
                currentStatus == DeliveryPartnerVerificationStatus.PENDING
                        && (requestedStatus ==
                                DeliveryPartnerVerificationStatus.VERIFIED
                                || requestedStatus ==
                                DeliveryPartnerVerificationStatus.REJECTED);

        if (!validTransition) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .INVALID_VERIFICATION_TRANSITION
                            .getErrorMessage());
        }
    }

    // =========================================================================
    // Business Status Transition Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner business status transition.
     *
     * <p>
     * PENDING can transition to ACTIVE only after verification. ACTIVE,
     * INACTIVE and SUSPENDED remain operational lifecycle states.
     * </p>
     *
     * @param currentStatus   current business status
     * @param requestedStatus requested business status
     */
    private void validateStatusTransition(
            final DeliveryPartnerStatus currentStatus,
            final DeliveryPartnerStatus requestedStatus) {

        final boolean validTransition = switch (currentStatus) {

            case PENDING ->
                requestedStatus == DeliveryPartnerStatus.ACTIVE;

            case ACTIVE ->
                requestedStatus == DeliveryPartnerStatus.INACTIVE
                        || requestedStatus == DeliveryPartnerStatus.SUSPENDED;

            case INACTIVE ->
                requestedStatus == DeliveryPartnerStatus.ACTIVE
                        || requestedStatus == DeliveryPartnerStatus.SUSPENDED;

            case SUSPENDED ->
                requestedStatus == DeliveryPartnerStatus.ACTIVE
                        || requestedStatus == DeliveryPartnerStatus.INACTIVE;
        };

        if (!validTransition) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .INVALID_STATUS_TRANSITION
                            .getErrorMessage());
        }
    }

    /**
     * Resolves the appropriate duplicate-status error.
     *
     * @param status current Delivery Partner status
     * @return applicable business error
     */
    private DeliveryPartnerErrorConstants getAlreadyStatusError(
            final DeliveryPartnerStatus status) {

        return switch (status) {

            case ACTIVE ->
                DeliveryPartnerErrorConstants
                        .DELIVERY_PARTNER_ALREADY_ACTIVE;

            case INACTIVE ->
                DeliveryPartnerErrorConstants
                        .DELIVERY_PARTNER_ALREADY_INACTIVE;

            case SUSPENDED ->
                DeliveryPartnerErrorConstants
                        .DELIVERY_PARTNER_ALREADY_SUSPENDED;

            case PENDING ->
                DeliveryPartnerErrorConstants
                        .INVALID_STATUS_TRANSITION;
        };
    }

    // =========================================================================
    // Authorization
    // =========================================================================

    /**
     * Validates that the current user is allowed to operate on the supplied
     * Delivery Partner.
     *
     * <p>
     * Administrators can operate on any Delivery Partner. A Delivery Partner
     * can operate only on their own Delivery Partner profile.
     * </p>
     *
     * @param input         service input containing the authenticated context
     * @param deliveryPartner target Delivery Partner
     */
    private void validateSelfOwnership(
            final IServiceInput<?> input,
            final DeliveryPartnerEntity deliveryPartner) {

        Objects.requireNonNull(
                input,
                "Delivery Partner service input must not be null.");

        Objects.requireNonNull(
                deliveryPartner,
                "Delivery Partner must not be null.");

        if (input.getServiceContext() == null
                || input.getServiceContext().getUserProfile() == null) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_OPERATION_NOT_ALLOWED
                            .getErrorMessage());
        }

        final UserProfile userProfile =
                input.getServiceContext().getUserProfile();

        final boolean isAdmin = userProfile.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        ("ROLE_" + RoleType.ADMIN.name())
                                .equals(authority.getAuthority()));

        /*
         * Administrators are allowed to operate on any Delivery Partner.
         */
        if (isAdmin) {
            return;
        }

        final boolean isDeliveryPartner = userProfile.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        ("ROLE_" + RoleType.DELIVERY_PARTNER.name())
                                .equals(authority.getAuthority()));

        if (!isDeliveryPartner) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_OPERATION_NOT_ALLOWED
                            .getErrorMessage());
        }

        final String authenticatedUserNumber =
                userProfile.getUserNumber();

        if (authenticatedUserNumber == null
                || authenticatedUserNumber.isBlank()
                || !authenticatedUserNumber.equals(
                        deliveryPartner.getUserNumber())) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_OPERATION_NOT_ALLOWED
                            .getErrorMessage());
        }
    }

    // =========================================================================
    // Admin Authorization
    // =========================================================================

    /**
     * Validates that the current request is being performed by an administrator.
     *
     * <p>
     * Delivery Partner verification is an administrative decision and must
     * never be performed by the Delivery Partner themselves.
     * </p>
     *
     * @param input service input containing the request context
     */
    private void validateAdminAccess(
            final IServiceInput<?> input) {

        if (input.getServiceContext() == null
                || input.getServiceContext().getUserProfile() == null) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_OPERATION_NOT_ALLOWED
                            .getErrorMessage());
        }

        final UserProfile userProfile =
                input.getServiceContext().getUserProfile();

        final boolean isAdmin = userProfile.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        ("ROLE_" + RoleType.ADMIN.name())
                                .equals(authority.getAuthority()));

        if (!isAdmin) {

            throw new IllegalStateException(
                    DeliveryPartnerErrorConstants
                            .DELIVERY_PARTNER_OPERATION_NOT_ALLOWED
                            .getErrorMessage());
        }
    }
}