package com.foodies.freshmeal.restaurant.service.impl;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
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
 * <li>Repository orchestration</li>
 * </ul>
 *
 * <p>
 * DTO transformation remains delegated to {@link RestaurantBranchMapper}.
 * Persistence operations remain delegated to the repository layer.
 * </p>
 *
 * <p>
 * Branch lifecycle and archival behavior are handled through the common
 * {@code ABaseEntity} infrastructure. Therefore, this service does not
 * duplicate common entity lifecycle fields or repository behavior.
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

	/**
	 * Creates RestaurantBranchServiceImpl.
	 *
	 * @param serviceContext             service execution context
	 * @param databaseSequenceService    database sequence service
	 * @param restaurantBranchRepository restaurant branch repository
	 * @param restaurantRepository       restaurant repository
	 * @param restaurantBranchMapper     restaurant branch mapper
	 */
	public RestaurantBranchServiceImpl(IServiceContext serviceContext, IDatabaseSequenceService databaseSequenceService,
			IRestaurantBranchRepository restaurantBranchRepository, IRestaurantRepository restaurantRepository,
			RestaurantBranchMapper restaurantBranchMapper) {

		this.serviceContext = serviceContext;
		this.databaseSequenceService = databaseSequenceService;
		this.restaurantBranchRepository = restaurantBranchRepository;
		this.restaurantRepository = restaurantRepository;
		this.restaurantBranchMapper = restaurantBranchMapper;
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
	 * <p>
	 * A dedicated Restaurant Branch sequence is used to generate both the internal
	 * database identifier and external business branch number.
	 * </p>
	 *
	 * @param input branch creation input
	 *
	 * @return created branch details
	 */
	@Override
	public IServiceOutput<RestaurantBranchDetailsResponse> create(IServiceInput<RestaurantBranchCreateRequest> input) {

		Objects.requireNonNull(input, "Restaurant branch service input must not be null.");

		RestaurantBranchCreateRequest request = input.getInput();

		Objects.requireNonNull(request, "Restaurant branch create request must not be null.");

		// ---------------------------------------------------------------------
		// Validate parent Restaurant
		// ---------------------------------------------------------------------

		validateParentRestaurant(request.getRestaurantId());

		// ---------------------------------------------------------------------
		// Validate duplicate branch
		// ---------------------------------------------------------------------

		validateBranchDoesNotExist(request.getRestaurantId(), request.getBranchName());

		// ---------------------------------------------------------------------
		// Generate branch sequence
		// ---------------------------------------------------------------------

		long sequence = databaseSequenceService.generateSequence(serviceContext,
				SequenceConstants.RESTAURANT_BRANCH_SEQUENCE);

		LOGGER.info("Generated Restaurant Branch sequence [{}]", sequence);

		// ---------------------------------------------------------------------
		// Map request → entity
		// ---------------------------------------------------------------------

		RestaurantBranchEntity branch = restaurantBranchMapper.toEntity(request);

		// ---------------------------------------------------------------------
		// Assign generated identifiers
		// ---------------------------------------------------------------------

		branch.setId(String.format(SequenceConstants.RESTAURANT_BRANCH_DB_ID_PATTERN, sequence));

		branch.setBranchNumber(String.format(SequenceConstants.RESTAURANT_BRANCH_NUMBER_PATTERN, sequence));

		// ---------------------------------------------------------------------
		// Persist
		// ---------------------------------------------------------------------

		branch = restaurantBranchRepository.save(branch);

		LOGGER.info("Restaurant branch created successfully. id=[{}], branchNumber=[{}], restaurantId=[{}]",
				branch.getId(), branch.getBranchNumber(), branch.getRestaurantId());

		// ---------------------------------------------------------------------
		// Map entity → response
		// ---------------------------------------------------------------------

		RestaurantBranchDetailsResponse response = restaurantBranchMapper.toDetailsResponse(branch);

		return new ServiceOutput<>(response);
	}

	// =========================================================================
	// Get By ID
	// =========================================================================

	/**
	 * Retrieves an active restaurant branch by identifier.
	 *
	 * @param input branch identifier request
	 *
	 * @return branch details
	 */
	@Override
	public IServiceOutput<RestaurantBranchDetailsResponse> getById(IServiceInput<RestaurantBranchIdRequest> input) {

		Objects.requireNonNull(input, "Restaurant branch service input must not be null.");

		RestaurantBranchIdRequest request = input.getInput();

		Objects.requireNonNull(request, "Restaurant branch id request must not be null.");

		RestaurantBranchEntity branch = restaurantBranchRepository.findActiveById(request.getBranchId())
				.orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND));

		RestaurantBranchDetailsResponse response = restaurantBranchMapper.toDetailsResponse(branch);

		return new ServiceOutput<>(response);
	}

	// =========================================================================
	// Get All
	// =========================================================================

	/**
	 * Retrieves all active restaurant branches.
	 *
	 * @param input service execution input
	 *
	 * @return active restaurant branches
	 */
	@Override
	public IServiceOutput<List<RestaurantBranchListResponse>> getAll(IServiceInput<Void> input) {

		Objects.requireNonNull(input, "Restaurant branch service input must not be null.");

		List<RestaurantBranchEntity> branches = restaurantBranchRepository.findAllActive();

		List<RestaurantBranchListResponse> responses = branches.stream().map(restaurantBranchMapper::toListResponse)
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
	 * The parent restaurant must itself be an active restaurant.
	 * </p>
	 *
	 * @param input restaurant identifier request
	 *
	 * @return active branches belonging to the restaurant
	 */
	@Override
	public IServiceOutput<List<RestaurantBranchListResponse>> getByRestaurantId(
			IServiceInput<RestaurantIdRequest> input) {

		Objects.requireNonNull(input, "Restaurant branch service input must not be null.");

		RestaurantIdRequest request = input.getInput();

		Objects.requireNonNull(request, "Restaurant id request must not be null.");

		validateParentRestaurant(request.getRestaurantId());

		Query query = Query.query(Criteria.where("restaurantId").is(request.getRestaurantId()));

		List<RestaurantBranchEntity> branches = restaurantBranchRepository.findAll(query);

		List<RestaurantBranchListResponse> responses = branches.stream().map(restaurantBranchMapper::toListResponse)
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
	 * Parent restaurant relationship and branch lifecycle information are not
	 * modified by this operation.
	 * </p>
	 *
	 * @param input branch update input
	 *
	 * @return updated branch details
	 */
	@Override
	public IServiceOutput<RestaurantBranchDetailsResponse> update(IServiceInput<RestaurantBranchUpdateRequest> input) {

		Objects.requireNonNull(input, "Restaurant branch service input must not be null.");

		RestaurantBranchUpdateRequest request = input.getInput();

		Objects.requireNonNull(request, "Restaurant branch update request must not be null.");

		// ---------------------------------------------------------------------
		// Retrieve active branch
		// ---------------------------------------------------------------------

		RestaurantBranchEntity branch = restaurantBranchRepository.findActiveById(request.getBranchId())
				.orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND));

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

		LOGGER.info("Restaurant branch updated successfully. id=[{}], branchNumber=[{}]", branch.getId(),
				branch.getBranchNumber());

		// ---------------------------------------------------------------------
		// Map entity → response
		// ---------------------------------------------------------------------

		RestaurantBranchDetailsResponse response = restaurantBranchMapper.toDetailsResponse(branch);

		return new ServiceOutput<>(response);
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
	private void validateParentRestaurant(String restaurantId) {

		if (restaurantId == null || restaurantId.isBlank()) {

			throw new IllegalArgumentException(
					RestaurantErrorConstants.RESTAURANT_BRANCH_REQUIRES_RESTAURANT.getErrorMessage());
		}

		restaurantRepository.findActiveById(restaurantId).orElseThrow(
				() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_BRANCH_REQUIRES_RESTAURANT));
	}

	// =========================================================================
	// Duplicate Branch Validation
	// =========================================================================

	/**
	 * Ensures that a restaurant does not contain another branch with the same
	 * branch name.
	 *
	 * <p>
	 * Branch numbers are generated by the sequence infrastructure and are therefore
	 * inherently unique. This validation protects against accidental duplicate
	 * business branch definitions within the same restaurant.
	 * </p>
	 *
	 * @param restaurantId parent restaurant identifier
	 * @param branchName   branch name
	 */
	private void validateBranchDoesNotExist(String restaurantId, String branchName) {

		Query query = Query.query(new Criteria().andOperator(Criteria.where("restaurantId").is(restaurantId),
				Criteria.where("branchName").is(branchName)));

		if (restaurantBranchRepository.exists(query)) {

			throw new IllegalStateException(
					RestaurantErrorConstants.RESTAURANT_BRANCH_ALREADY_EXISTS.getErrorMessage());
		}
	}
}
