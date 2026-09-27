package com.foodies.freshmeal.restaurant.service.impl;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.restaurant.constants.RestaurantErrorConstants;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;
import com.foodies.freshmeal.restaurant.mapper.RestaurantBranchMapper;
import com.foodies.freshmeal.restaurant.repository.IRestaurantBranchRepository;
import com.foodies.freshmeal.restaurant.repository.IRestaurantRepository;
import com.foodies.freshmeal.restaurant.service.IRestaurantBranchService;
import com.foodies.freshmeal.restaurant.service.IRestaurantLifecycleService;

/**
 * ============================================================================
 * Service Implementation : Restaurant Branch
 * ============================================================================
 *
 * <p>
 * Provides business operations for Restaurant Branch management.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Restaurant branch creation</li>
 * <li>Restaurant branch retrieval</li>
 * <li>Restaurant branch update</li>
 * <li>Parent restaurant validation</li>
 * <li>Branch business identifier generation</li>
 * <li>Branch persistence lifecycle management</li>
 * <li>Repository orchestration</li>
 * </ul>
 *
 * <p>
 * DTO transformation remains delegated to {@link RestaurantBranchMapper}.
 * Persistence lifecycle operations are delegated to the common repository
 * infrastructure.
 * </p>
 *
 * <p>
 * Food cascade handling is intentionally not performed directly by this
 * service. Restaurant → Branch → Food lifecycle orchestration will be handled
 * separately once all participating domain lifecycle contracts are established.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class RestaurantBranchServiceImpl implements IRestaurantBranchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantBranchServiceImpl.class);

    private final IServiceContext serviceContext;

    private final IDatabaseSequenceService databaseSequenceService;

    private final IRestaurantBranchRepository restaurantBranchRepository;

    private final IRestaurantRepository restaurantRepository;

    private final RestaurantBranchMapper restaurantBranchMapper;

    private final IRestaurantLifecycleService restaurantLifecycleService;

    /**
     * Creates RestaurantBranchServiceImpl.
     *
     * @param serviceContext             service execution context
     * @param databaseSequenceService    database sequence service
     * @param restaurantBranchRepository restaurant branch repository
     * @param restaurantRepository       restaurant repository
     * @param restaurantBranchMapper     restaurant branch mapper
     * @param restaurantLifecycleService restaurant lifecycle coordinator
     */
    public RestaurantBranchServiceImpl(
            IServiceContext serviceContext,
            IDatabaseSequenceService databaseSequenceService,
            IRestaurantBranchRepository restaurantBranchRepository,
            IRestaurantRepository restaurantRepository,
            RestaurantBranchMapper restaurantBranchMapper,
            IRestaurantLifecycleService restaurantLifecycleService) {

        this.serviceContext = serviceContext;
        this.databaseSequenceService = databaseSequenceService;
        this.restaurantBranchRepository = restaurantBranchRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantBranchMapper = restaurantBranchMapper;
        this.restaurantLifecycleService = restaurantLifecycleService;
    }

    // =========================================================================
    // Create
    // =========================================================================

    /**
     * Creates a new restaurant branch.
     *
     * <p>
     * The parent restaurant must exist and be active before a branch can be
     * created.
     * </p>
     *
     * @param input branch creation input
     * @return created branch details
     */
    @Override
    public IServiceOutput<RestaurantBranchDetailsResponse> create(
            IServiceInput<RestaurantBranchCreateRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch service input must not be null.");

        RestaurantBranchCreateRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant branch create request must not be null.");

        // ---------------------------------------------------------------------
        // Validate parent Restaurant
        // ---------------------------------------------------------------------

        validateParentRestaurant(request.getRestaurantId());

        // ---------------------------------------------------------------------
        // Validate duplicate branch
        // ---------------------------------------------------------------------

        validateBranchDoesNotExist(
                request.getRestaurantId(),
                request.getBranchName());

        // ---------------------------------------------------------------------
        // Generate branch sequence
        // ---------------------------------------------------------------------

        long sequence = databaseSequenceService.generateSequence(
                serviceContext,
                SequenceConstants.RESTAURANT_BRANCH_SEQUENCE);

        LOGGER.info(
                "Generated Restaurant Branch sequence [{}]",
                sequence);

        // ---------------------------------------------------------------------
        // Map request → entity
        // ---------------------------------------------------------------------

        RestaurantBranchEntity branch = restaurantBranchMapper.toEntity(request);

        // ---------------------------------------------------------------------
        // Assign generated identifiers
        // ---------------------------------------------------------------------

        branch.setId(
                String.format(
                        SequenceConstants.RESTAURANT_BRANCH_DB_ID_PATTERN,
                        sequence));

        branch.setBranchNumber(
                String.format(
                        SequenceConstants.RESTAURANT_BRANCH_NUMBER_PATTERN,
                        sequence));

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        branch = restaurantBranchRepository.save(branch);

        LOGGER.info(
                "Restaurant branch created successfully. id=[{}], branchNumber=[{}], restaurantId=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                branch.getRestaurantId());

        return new ServiceOutput<>(
                restaurantBranchMapper.toDetailsResponse(branch));
    }

    // =========================================================================
    // Get By ID
    // =========================================================================

    /**
     * Retrieves an active restaurant branch by identifier.
     *
     * @param input branch identifier request
     * @return branch details
     */
    @Override
    public IServiceOutput<RestaurantBranchDetailsResponse> getById(
            IServiceInput<RestaurantBranchIdRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch service input must not be null.");

        RestaurantBranchIdRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant branch id request must not be null.");

        RestaurantBranchEntity branch = loadActiveBranch(request.getBranchId());

        return new ServiceOutput<>(
                restaurantBranchMapper.toDetailsResponse(branch));
    }

    // =========================================================================
    // Get All
    // =========================================================================

    /**
     * Retrieves all active restaurant branches.
     *
     * @param input service execution input
     * @return active restaurant branches
     */
    @Override
    public IServiceOutput<List<RestaurantBranchListResponse>> getAll(
            IServiceInput<Void> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch service input must not be null.");

        List<RestaurantBranchListResponse> responses = restaurantBranchRepository.findAllActive()
                .stream()
                .map(restaurantBranchMapper::toListResponse)
                .toList();

        return new ServiceOutput<>(responses);
    }

    // =========================================================================
    // Get By Restaurant ID
    // =========================================================================

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * <p>
     * The parent restaurant must itself be active.
     * </p>
     *
     * @param input restaurant identifier request
     * @return active branches belonging to the restaurant
     */
    @Override
    public IServiceOutput<List<RestaurantBranchListResponse>> getByRestaurantId(
            IServiceInput<RestaurantIdRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch service input must not be null.");

        RestaurantIdRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant id request must not be null.");

        validateParentRestaurant(request.getRestaurantId());

        Query query = Query.query(
                Criteria.where("restaurantId")
                        .is(request.getRestaurantId()));

        List<RestaurantBranchListResponse> responses = restaurantBranchRepository.findAll(query)
                .stream()
                .map(restaurantBranchMapper::toListResponse)
                .toList();

        return new ServiceOutput<>(responses);
    }

    // =========================================================================
    // Get Archived
    // =========================================================================

    /**
     * Retrieves all archived restaurant branches.
     *
     * @param input service execution input
     * @return archived restaurant branches
     */
    @Override
    public IServiceOutput<List<RestaurantBranchListResponse>> getArchived(
            IServiceInput<Void> input) {

        Objects.requireNonNull(
                input,
                "Restaurant archived branch input must not be null.");

        List<RestaurantBranchListResponse> responses = restaurantBranchRepository.findAllDeleted()
                .stream()
                .map(restaurantBranchMapper::toListResponse)
                .toList();

        return new ServiceOutput<>(responses);
    }

    // =========================================================================
    // Update
    // =========================================================================

    /**
     * Updates standard business information of an existing restaurant branch.
     *
     * <p>
     * Parent restaurant relationship and persistence lifecycle information are
     * intentionally excluded from this operation.
     * </p>
     *
     * @param input branch update input
     * @return updated branch details
     */
    @Override
    public IServiceOutput<RestaurantBranchDetailsResponse> update(
            IServiceInput<RestaurantBranchUpdateRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch service input must not be null.");

        RestaurantBranchUpdateRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant branch update request must not be null.");

        // ---------------------------------------------------------------------
        // Retrieve active branch
        // ---------------------------------------------------------------------

        RestaurantBranchEntity branch = loadActiveBranch(request.getBranchId());

        // ---------------------------------------------------------------------
        // Validate parent Restaurant
        // ---------------------------------------------------------------------

        validateParentRestaurant(branch.getRestaurantId());

        // ---------------------------------------------------------------------
        // Update business information
        // ---------------------------------------------------------------------

        branch.setBranchName(request.getBranchName());
        branch.setAddress(request.getAddress());
        branch.setOperatingHours(request.getOperatingHours());

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        branch = restaurantBranchRepository.save(branch);

        LOGGER.info(
                "Restaurant branch updated successfully. id=[{}], branchNumber=[{}]",
                branch.getId(),
                branch.getBranchNumber());

        return new ServiceOutput<>(
                restaurantBranchMapper.toDetailsResponse(branch));
    }

    // =========================================================================
    // Archive
    // =========================================================================

    /**
     * Archives an active restaurant branch and its active food hierarchy.
     *
     * <p>
     * Archival is implemented as a soft-delete operation. The lifecycle coordinator
     * first archives all currently active foods belonging to the branch and then
     * archives the branch itself.
     * </p>
     *
     * <p>
     * Independently archived foods are not modified because the lifecycle
     * coordinator operates only on currently active child records.
     * </p>
     *
     * @param input branch identifier request
     * @return archived branch details
     */
    @Override
    public IServiceOutput<RestaurantBranchDetailsResponse> archive(
            IServiceInput<RestaurantBranchIdRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch archive input must not be null.");

        RestaurantBranchIdRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant branch archive request must not be null.");

        RestaurantBranchEntity branch = loadActiveBranch(request.getBranchId());

        // ---------------------------------------------------------------------
        // Archive branch hierarchy
        // ---------------------------------------------------------------------
        // The lifecycle coordinator archives:
        //
        // Branch
        // └── all active Foods belonging to the branch
        //
        // This keeps lifecycle cascade logic outside the domain service itself.
        // ---------------------------------------------------------------------

        restaurantLifecycleService.archiveBranchHierarchy(
                branch,
                input.getServiceContext());

        LOGGER.info(
                "Restaurant branch archived successfully. id=[{}], branchNumber=[{}], restaurantId=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                branch.getRestaurantId());

        return new ServiceOutput<>(
                restaurantBranchMapper.toDetailsResponse(branch));
    }

    // =========================================================================
    // Restore
    // =========================================================================

    /**
     * Restores an archived restaurant branch.
     *
     * <p>
     * The parent restaurant must be active before the branch can be restored.
     * This prevents an active branch from being restored underneath an archived
     * parent restaurant.
     * </p>
     *
     * @param input branch identifier request
     * @return restored branch details
     */
    @Override
    public IServiceOutput<RestaurantBranchDetailsResponse> restore(
            IServiceInput<RestaurantBranchIdRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch restore input must not be null.");

        RestaurantBranchIdRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant branch restore request must not be null.");

        RestaurantBranchEntity branch = loadArchivedBranch(request.getBranchId());

        // ---------------------------------------------------------------------
        // Validate parent Restaurant
        // ---------------------------------------------------------------------

        validateParentRestaurant(branch.getRestaurantId());

        // ---------------------------------------------------------------------
        // Restore branch
        // ---------------------------------------------------------------------

        restaurantBranchRepository.restore(
                branch.getId(),
                createRepositoryContext(input.getServiceContext()));

        LOGGER.info(
                "Restaurant branch restored successfully. id=[{}], branchNumber=[{}], restaurantId=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                branch.getRestaurantId());

        return new ServiceOutput<>(
                restaurantBranchMapper.toDetailsResponse(branch));
    }

    // =========================================================================
    // Permanent Delete
    // =========================================================================

    /**
     * Permanently deletes an archived restaurant branch and its archived food
     * hierarchy.
     *
     * <p>
     * Permanent deletion is intentionally restricted to an already archived
     * branch.
     * </p>
     *
     * <p>
     * The lifecycle coordinator permanently deletes archived foods first and then
     * the archived branch. Active child records are protected by the lifecycle
     * coordinator and prevent an unsafe permanent deletion.
     * </p>
     *
     * @param input branch identifier request
     */
    @Override
    public void deletePermanently(
            IServiceInput<RestaurantBranchIdRequest> input) {

        Objects.requireNonNull(
                input,
                "Restaurant branch permanent-delete input must not be null.");

        RestaurantBranchIdRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restaurant branch permanent-delete request must not be null.");

        RestaurantBranchEntity branch = loadArchivedBranch(request.getBranchId());

        // ---------------------------------------------------------------------
        // Permanently delete branch hierarchy
        // ---------------------------------------------------------------------
        // The lifecycle coordinator permanently deletes:
        //
        // archived Foods
        // ↓
        // archived Branch
        //
        // The coordinator also prevents deletion when active child records
        // still exist.
        // ---------------------------------------------------------------------

        restaurantLifecycleService.deleteBranchHierarchyPermanently(
                branch,
                input.getServiceContext());

        LOGGER.info(
                "Restaurant branch permanently deleted. id=[{}], branchNumber=[{}], restaurantId=[{}]",
                branch.getId(),
                branch.getBranchNumber(),
                branch.getRestaurantId());
    }

    // =========================================================================
    // Branch Loading
    // =========================================================================

    /**
     * Loads an active restaurant branch.
     *
     * @param branchId branch identifier
     * @return active branch
     */
    private RestaurantBranchEntity loadActiveBranch(
            String branchId) {

        return restaurantBranchRepository.findActiveById(branchId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND));
    }

    /**
     * Loads an archived restaurant branch.
     *
     * @param branchId branch identifier
     * @return archived branch
     */
    private RestaurantBranchEntity loadArchivedBranch(
            String branchId) {

        return restaurantBranchRepository.findDeletedById(branchId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND));
    }

    // =========================================================================
    // Parent Restaurant Validation
    // =========================================================================

    /**
     * Validates that the supplied parent restaurant exists and is active.
     *
     * <p>
     * A Restaurant Branch cannot exist independently from its parent Restaurant.
     * </p>
     *
     * @param restaurantId parent restaurant identifier
     */
    private void validateParentRestaurant(
            String restaurantId) {

        if (restaurantId == null || restaurantId.isBlank()) {

            throw new IllegalArgumentException(
                    RestaurantErrorConstants.RESTAURANT_BRANCH_REQUIRES_RESTAURANT
                            .getErrorMessage());
        }

        restaurantRepository.findActiveById(restaurantId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                RestaurantErrorConstants.RESTAURANT_BRANCH_REQUIRES_RESTAURANT));
    }

    // =========================================================================
    // Duplicate Branch Validation
    // =========================================================================

    /**
     * Ensures that a restaurant does not contain another branch with the same
     * branch name.
     *
     * <p>
     * Branch numbers are generated by the sequence infrastructure and are
     * therefore inherently unique. This validation protects against accidental
     * duplicate business branch definitions within the same restaurant.
     * </p>
     *
     * @param restaurantId parent restaurant identifier
     * @param branchName   branch name
     */
    private void validateBranchDoesNotExist(
            String restaurantId,
            String branchName) {

        Query query = Query.query(
                new Criteria().andOperator(
                        Criteria.where("restaurantId")
                                .is(restaurantId),
                        Criteria.where("branchName")
                                .is(branchName)));

        if (restaurantBranchRepository.exists(query)) {

            throw new IllegalStateException(
                    RestaurantErrorConstants.RESTAURANT_BRANCH_ALREADY_EXISTS
                            .getErrorMessage());
        }
    }

    // =========================================================================
    // Repository Context
    // =========================================================================

    /**
     * Creates the repository context used by soft-delete and restore operations.
     *
     * @param context current service context
     * @return repository context
     */
    private RepositoryContext createRepositoryContext(
            IServiceContext context) {

        if (context != null
                && context.getUserProfile() != null) {

            return RepositoryContext.of(
                    context.getUserProfile().getUsername(),
                    AppCalendar.getBusinessLocalDateTime());
        }

        return RepositoryContext.of(
                RepositoryConstants.SYSTEM_USER,
                AppCalendar.getBusinessLocalDateTime());
    }
}