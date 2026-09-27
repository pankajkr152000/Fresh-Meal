package com.foodies.freshmeal.delivery.service.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerErrorConstants;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerStatus;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerVerificationStatus;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerRegistrationRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.entity.DeliveryPartnerEntity;
import com.foodies.freshmeal.delivery.mapper.DeliveryPartnerMapper;
import com.foodies.freshmeal.delivery.repository.IDeliveryPartnerRepository;
import com.foodies.freshmeal.delivery.service.IDeliveryPartnerOnboardingService;
import com.foodies.freshmeal.user.entity.UserProfile;

/**
 * ============================================================================
 * Service Implementation : Delivery Partner Onboarding
 * ============================================================================
 *
 * <p>
 * Provides the onboarding workflow for FreshMeal delivery partners.
 * </p>
 *
 * <p>
 * Delivery partner onboarding is intentionally separated from delivery partner
 * management. This service is responsible only for establishing the delivery
 * partner profile for the currently authenticated user.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Resolve the authenticated FreshMeal user.</li>
 * <li>Validate the required delivery-partner role.</li>
 * <li>Prevent duplicate delivery-partner onboarding.</li>
 * <li>Generate delivery-partner identifiers.</li>
 * <li>Map registration data to the entity.</li>
 * <li>Initialize the partner lifecycle state.</li>
 * <li>Persist the delivery-partner entity.</li>
 * <li>Return the persisted delivery-partner response.</li>
 * </ul>
 *
 * <h3>System-Controlled Fields</h3>
 *
 * <p>
 * The following values are never accepted from the client:
 * </p>
 *
 * <ul>
 * <li>Database identifier</li>
 * <li>Partner number</li>
 * <li>Partner code</li>
 * <li>User number</li>
 * <li>Verification status</li>
 * <li>Business status</li>
 * <li>Availability</li>
 * </ul>
 *
 * <p>
 * A newly onboarded delivery partner starts with
 * {@code PENDING} verification, {@code PENDING} business status and
 * {@code isAvailable = false}.
 * </p>
 *
 * <p>
 * Authentication and user identity management remain outside this service.
 * The authenticated identity is obtained exclusively from the request-scoped
 * {@link IServiceContext}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
@Transactional
public class DeliveryPartnerOnboardingServiceImpl
        implements IDeliveryPartnerOnboardingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryPartnerOnboardingServiceImpl.class);

    /**
     * Delivery partner repository.
     */
    private final IDeliveryPartnerRepository deliveryPartnerRepository;

    /**
     * Database sequence service.
     */
    private final IDatabaseSequenceService databaseSequenceService;

    /**
     * Current request-scoped service context.
     */
    private final IServiceContext serviceContext;

    /**
     * Delivery partner mapper.
     */
    private final DeliveryPartnerMapper deliveryPartnerMapper;

    /**
     * Creates the delivery partner onboarding service.
     *
     * @param deliveryPartnerRepository delivery partner repository
     * @param databaseSequenceService   database sequence service
     * @param serviceContext            current request-scoped service context
     * @param deliveryPartnerMapper     delivery partner mapper
     */
    public DeliveryPartnerOnboardingServiceImpl(
            final IDeliveryPartnerRepository deliveryPartnerRepository,
            final IDatabaseSequenceService databaseSequenceService,
            final IServiceContext serviceContext,
            final DeliveryPartnerMapper deliveryPartnerMapper) {

        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.databaseSequenceService = databaseSequenceService;
        this.serviceContext = serviceContext;
        this.deliveryPartnerMapper = deliveryPartnerMapper;
    }

    // =========================================================================
    // Delivery Partner Onboarding
    // =========================================================================

    /**
     * {@inheritDoc}
     *
     * <p>
     * The authenticated user is resolved from the request-scoped service
     * context. The client cannot provide or override the associated user number.
     * </p>
     */
    @Override
    public IServiceOutput<DeliveryPartnerResponse> onboardDeliveryPartner(
            final IServiceInput<DeliveryPartnerRegistrationRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery partner onboarding service input must not be null.");

        final DeliveryPartnerRegistrationRequest registrationRequest = input.getInput();

        Objects.requireNonNull(
                registrationRequest,
                "Delivery partner registration request must not be null.");

        // ---------------------------------------------------------------------
        // Resolve authenticated user
        // ---------------------------------------------------------------------

        final UserProfile userProfile = resolveAuthenticatedUser();

        final String userNumber = userProfile.getUserNumber();

        // ---------------------------------------------------------------------
        // Validate delivery-partner role
        // ---------------------------------------------------------------------

        validateDeliveryPartnerRole(userProfile);

        // ---------------------------------------------------------------------
        // Prevent duplicate onboarding
        // ---------------------------------------------------------------------

        validateNotAlreadyOnboarded(userNumber);

        // ---------------------------------------------------------------------
        // Create delivery partner
        // ---------------------------------------------------------------------

        final DeliveryPartnerEntity deliveryPartner = createDeliveryPartner(
                registrationRequest,
                userNumber,
                input.getServiceContext());

        final DeliveryPartnerEntity savedDeliveryPartner = deliveryPartnerRepository.save(deliveryPartner);

        LOGGER.info(
                "Delivery partner onboarding completed. "
                        + "partnerNumber=[{}], partnerCode=[{}], userNumber=[{}]",
                savedDeliveryPartner.getPartnerNumber(),
                savedDeliveryPartner.getPartnerCode(),
                savedDeliveryPartner.getUserNumber());

        return new ServiceOutput<>(
                deliveryPartnerMapper.toResponse(savedDeliveryPartner));
    }

    // =========================================================================
    // Delivery Partner Creation
    // =========================================================================

    /**
     * Creates a delivery-partner entity from the registration request.
     *
     * <p>
     * System-controlled values are generated internally and are not accepted
     * from the client.
     * </p>
     *
     * @param request    delivery-partner registration request
     * @param userNumber authenticated user's user number
     * @param context    service context
     *
     * @return initialized delivery-partner entity
     */
    private DeliveryPartnerEntity createDeliveryPartner(
            final DeliveryPartnerRegistrationRequest request,
            final String userNumber,
            final IServiceContext context) {

        final DeliveryPartnerEntity deliveryPartner = (DeliveryPartnerEntity) DeliveryPartnerEntity.create();

        final long sequence = databaseSequenceService.generateSequence(
                context,
                SequenceConstants.DELIVERY_PARTNER_SEQUENCE);

        // ---------------------------------------------------------------------
        // Generate system identifiers
        // ---------------------------------------------------------------------

        deliveryPartner.setId(
                String.format(
                        SequenceConstants.DELIVERY_PARTNER_DB_ID_PATTERN,
                        sequence));

        deliveryPartner.setPartnerNumber(
                String.format(
                        SequenceConstants.DELIVERY_PARTNER_NUMBER_PATTERN,
                        sequence));

        deliveryPartner.setPartnerCode(
                String.format(
                        SequenceConstants.DELIVERY_PARTNER_CODE_PATTERN,
                        sequence));

        // ---------------------------------------------------------------------
        // Associate authenticated user
        // ---------------------------------------------------------------------

        deliveryPartner.setUserNumber(userNumber);

        // ---------------------------------------------------------------------
        // Map client-editable fields
        // ---------------------------------------------------------------------

        deliveryPartnerMapper.mapRegistrationRequestToEntity(
                request,
                deliveryPartner);

        // ---------------------------------------------------------------------
        // Initialize onboarding lifecycle state
        // ---------------------------------------------------------------------

        deliveryPartner.setVerificationStatus(
                DeliveryPartnerVerificationStatus.PENDING);

        deliveryPartner.setStatus(
                DeliveryPartnerStatus.PENDING);

        deliveryPartner.setAvailable(false);

        return deliveryPartner;
    }

    // =========================================================================
    // Authentication / Authorization Validation
    // =========================================================================

    /**
     * Resolves the currently authenticated FreshMeal user.
     *
     * @return authenticated user profile
     *
     * @throws BusinessException when no authenticated user is available
     */
    private UserProfile resolveAuthenticatedUser() {

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile == null
                || userProfile.getUserNumber() == null
                || userProfile.getUserNumber().isBlank()) {

            throw new BusinessException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_OPERATION_NOT_ALLOWED);
        }

        return userProfile;
    }

    /**
     * Validates that the authenticated user has the delivery-partner role.
     *
     * <p>
     * Role information is taken from the authenticated security principal.
     * Client-supplied role information is never trusted for authorization.
     * </p>
     *
     * @param userProfile authenticated user profile
     *
     * @throws BusinessException when the required role is missing
     */
    private void validateDeliveryPartnerRole(
            final UserProfile userProfile) {

        final boolean deliveryPartner = userProfile.getAuthorities()
                .stream()
                .anyMatch(authority -> ("ROLE_" + RoleType.DELIVERY_PARTNER.name())
                        .equals(authority.getAuthority()));

        if (!deliveryPartner) {
            throw new BusinessException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_ROLE_REQUIRED);
        }
    }

    /**
     * Ensures that the authenticated user does not already have a delivery
     * partner profile.
     *
     * @param userNumber authenticated user's user number
     *
     * @throws BusinessException when a delivery partner already exists
     */
    private void validateNotAlreadyOnboarded(
            final String userNumber) {

        if (deliveryPartnerRepository
                .findByUserNumber(userNumber)
                .isPresent()) {

            throw new BusinessException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_ALREADY_ONBOARDED);
        }
    }
}