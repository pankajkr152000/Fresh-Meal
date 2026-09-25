package com.foodies.freshmeal.restaurant.service.impl;

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
import com.foodies.freshmeal.restaurant.constants.RestaurantErrorConstants;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchRegistrationRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantOnboardingInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantOwnerRegistrationRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantRegistrationRequest;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;
import com.foodies.freshmeal.restaurant.repository.IRestaurantBranchRepository;
import com.foodies.freshmeal.restaurant.repository.IRestaurantRepository;
import com.foodies.freshmeal.restaurant.service.IRestaurantOnboardingService;
import com.foodies.freshmeal.user.entity.UserProfile;

/**
 * ============================================================================
 * Service Implementation : Restaurant Onboarding
 * ============================================================================
 *
 * <p>
 * Provides the restaurant-owner onboarding workflow for FreshMeal.
 * </p>
 *
 * <p>
 * Restaurant onboarding is intentionally separated from the existing restaurant
 * CRUD service. The existing restaurant creation operation is an administrative
 * restaurant-management operation, whereas this service establishes the
 * restaurant business profile for the currently authenticated
 * {@link RoleType#RESTAURANT_OWNER}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Resolve the authenticated FreshMeal user.</li>
 * <li>Validate the required restaurant-owner role.</li>
 * <li>Create the restaurant business entity.</li>
 * <li>Generate restaurant identifiers.</li>
 * <li>Associate the restaurant with the authenticated user.</li>
 * <li>Create the initial restaurant branch.</li>
 * <li>Generate branch identifiers.</li>
 * <li>Persist the restaurant and branch entities.</li>
 * </ul>
 *
 * <p>
 * Authentication, password management and user identity management remain
 * outside this service. The authenticated identity is obtained exclusively
 * from the request-scoped {@link IServiceContext}.
 * </p>
 *
 * ============================================================================
 *
 * POST /api/restaurants/onboarding
 * │
 * ▼
 * RestaurantOnboardingController
 * │
 * ▼
 * 
 *POST /api/restaurants/onboarding
            │
            ▼
RestaurantOnboardingController
            │
            ▼
@Transactional
RestaurantOnboardingServiceImpl
            │
            ├── Create Restaurant
            │      └── save()
            │
            ├── Create Initial Branch
            │      └── save()
            │
            └── return Restaurant
                  │
          ┌───────┴────────┐
          │                │
       success           failure
          │                │
       COMMIT           ROLLBACK
 * 
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
@Transactional
public class RestaurantOnboardingServiceImpl
        implements IRestaurantOnboardingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantOnboardingServiceImpl.class);

    /**
     * Restaurant repository.
     */
    private final IRestaurantRepository restaurantRepository;

    /**
     * Restaurant branch repository.
     */
    private final IRestaurantBranchRepository restaurantBranchRepository;

    /**
     * Database sequence service.
     */
    private final IDatabaseSequenceService databaseSequenceService;

    /**
     * Current request-scoped service context.
     */
    private final IServiceContext serviceContext;

    /**
     * Creates the restaurant onboarding service.
     *
     * @param restaurantRepository       restaurant repository
     * @param restaurantBranchRepository restaurant branch repository
     * @param databaseSequenceService    database sequence service
     * @param serviceContext             current service context
     */
    public RestaurantOnboardingServiceImpl(
            final IRestaurantRepository restaurantRepository,
            final IRestaurantBranchRepository restaurantBranchRepository,
            final IDatabaseSequenceService databaseSequenceService,
            final IServiceContext serviceContext) {

        this.restaurantRepository = restaurantRepository;
        this.restaurantBranchRepository = restaurantBranchRepository;
        this.databaseSequenceService = databaseSequenceService;
        this.serviceContext = serviceContext;
    }

    // =========================================================================
    // Restaurant Onboarding
    // =========================================================================

    /**
     * {@inheritDoc}
     *
     * <p>
     * The authenticated user is resolved from the service context. The client
     * cannot provide or override the restaurant owner user number.
     * </p>
     */
    @Override
    public IServiceOutput<RestaurantEntity> onboardRestaurant(
            final IServiceInput<RestaurantOnboardingInputDTO> input) {

        Objects.requireNonNull(
                input,
                "Restaurant onboarding service input must not be null.");

        final RestaurantOnboardingInputDTO onboardingInput = input.getInput();

        Objects.requireNonNull(
                onboardingInput,
                "Restaurant onboarding input must not be null.");

        final RestaurantOwnerRegistrationRequest registrationRequest = onboardingInput.getRegistrationRequest();

        Objects.requireNonNull(
                registrationRequest,
                "Restaurant owner registration request must not be null.");

        // ---------------------------------------------------------------------
        // Resolve authenticated user
        // ---------------------------------------------------------------------

        final UserProfile userProfile = resolveAuthenticatedUser();

        final String userNumber = userProfile.getUserNumber();

        // ---------------------------------------------------------------------
        // Validate restaurant-owner role
        // ---------------------------------------------------------------------

        validateRestaurantOwnerRole(userProfile);

        // ---------------------------------------------------------------------
        // Extract registration data
        // ---------------------------------------------------------------------

        final RestaurantRegistrationRequest restaurantRequest = registrationRequest.getRestaurant();

        final RestaurantBranchRegistrationRequest branchRequest = registrationRequest.getBranch();

        Objects.requireNonNull(
                restaurantRequest,
                "Restaurant registration request must not be null.");

        Objects.requireNonNull(
                branchRequest,
                "Restaurant branch registration request must not be null.");

        // ---------------------------------------------------------------------
        // Create restaurant
        // ---------------------------------------------------------------------

        final RestaurantEntity restaurant = createRestaurant(restaurantRequest, userNumber, input.getServiceContext());

        final RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        LOGGER.info(
                "Restaurant onboarding created restaurant. restaurantNumber=[{}], ownerUserNumber=[{}]",
                savedRestaurant.getRestaurantNumber(),
                userNumber);

        // ---------------------------------------------------------------------
        // Create initial branch
        // ---------------------------------------------------------------------

        final RestaurantBranchEntity branch = createInitialBranch(
                branchRequest,
                savedRestaurant.getId(),
                input.getServiceContext());

        final RestaurantBranchEntity savedBranch = restaurantBranchRepository.save(branch);

        LOGGER.info(
                "Restaurant onboarding created initial branch. branchNumber=[{}], restaurantId=[{}]",
                savedBranch.getBranchNumber(),
                savedRestaurant.getId());

        return new ServiceOutput<>(savedRestaurant);
    }

    // =========================================================================
    // Restaurant Creation
    // =========================================================================

    /**
     * Creates a restaurant entity from the onboarding request.
     *
     * <p>
     * System-controlled values are generated here rather than accepted from
     * the client.
     * </p>
     *
     * @param request         restaurant registration request
     * @param ownerUserNumber authenticated owner's user number
     * @param context         service context
     *
     * @return initialized restaurant entity
     */
    private RestaurantEntity createRestaurant(
            final RestaurantRegistrationRequest request,
            final String ownerUserNumber,
            final IServiceContext context) {

        final RestaurantEntity restaurant = (RestaurantEntity) RestaurantEntity.create();

        final long sequence = databaseSequenceService.generateSequence(
                context,
                SequenceConstants.RESTAURANT_SEQUENCE);

        restaurant.setId(
                String.format(
                        SequenceConstants.RESTAURANT_DB_ID_PATTERN,
                        sequence));

        restaurant.setRestaurantNumber(
                String.format(
                        SequenceConstants.RESTAURANT_NUMBER_PATTERN,
                        sequence));

        restaurant.setOwnerUserNumber(ownerUserNumber);

        restaurant.setRestaurantName(request.getRestaurantName());
        restaurant.setDescription(request.getDescription());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setEmailAddress(request.getEmailAddress());
        restaurant.setWebsite(request.getWebsite());
        restaurant.setCuisineTypes(request.getCuisineTypes());

        return restaurant;
    }

    // =========================================================================
    // Initial Branch Creation
    // =========================================================================

    /**
     * Creates the initial branch associated with the newly created restaurant.
     *
     * <p>
     * The restaurant identifier is derived from the newly persisted restaurant
     * and therefore cannot be supplied by the client.
     * </p>
     *
     * @param request      branch registration request
     * @param restaurantId persisted restaurant database identifier
     * @param context      service context
     *
     * @return initialized restaurant branch entity
     */
    private RestaurantBranchEntity createInitialBranch(
            final RestaurantBranchRegistrationRequest request,
            final String restaurantId,
            final IServiceContext context) {

        final RestaurantBranchEntity branch = (RestaurantBranchEntity) RestaurantBranchEntity.create();

        final long sequence = databaseSequenceService.generateSequence(
                context,
                SequenceConstants.RESTAURANT_BRANCH_SEQUENCE);

        branch.setId(
                String.format(
                        SequenceConstants.RESTAURANT_BRANCH_DB_ID_PATTERN,
                        sequence));

        branch.setBranchNumber(
                String.format(
                        SequenceConstants.RESTAURANT_BRANCH_NUMBER_PATTERN,
                        sequence));

        branch.setRestaurantId(restaurantId);
        branch.setBranchName(request.getBranchName());
        branch.setAddress(request.getAddress());
        branch.setOperatingHours(request.getOperatingHours());

        return branch;
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
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        return userProfile;
    }

    /**
     * Validates that the authenticated user has the restaurant-owner role.
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
    private void validateRestaurantOwnerRole(
            final UserProfile userProfile) {

        final boolean restaurantOwner = userProfile.getAuthorities()
                .stream()
                .anyMatch(authority -> ("ROLE_" + RoleType.RESTAURANT_OWNER.name())
                        .equals(authority.getAuthority()));

        if (!restaurantOwner) {
            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }
    }
}