package com.foodies.freshmeal.food.service.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.RepositoryConstants;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.exception.InvalidFoodStatusTransitionException;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.util.CommonUtils;
import com.foodies.freshmeal.common.util.DisplayOptionMapperUtil;
import com.foodies.freshmeal.food.constants.CategoryGroupConstant;
import com.foodies.freshmeal.food.constants.CuisineTypeConstant;
import com.foodies.freshmeal.food.constants.DefaultFoodImageConstants;
import com.foodies.freshmeal.food.constants.DietCategoryConstant;
import com.foodies.freshmeal.food.constants.FoodCategoryConstant;
import com.foodies.freshmeal.food.constants.FoodErrorConstants;
import com.foodies.freshmeal.food.constants.FoodStatusConstant;
import com.foodies.freshmeal.food.dto.ArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.BulkRestoreFoodRequest;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.EditFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodIdRequest;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.PermanentDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.RestoreFoodRequest;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.food.mapper.FoodMapper;
import com.foodies.freshmeal.food.repository.IFoodRepository;
import com.foodies.freshmeal.food.service.IFoodNavigationService;
import com.foodies.freshmeal.food.service.IFoodService;
import com.foodies.freshmeal.food.validation.FoodValidator;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.dto.ImageSnapshot;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.image.service.IImageService;
import com.foodies.freshmeal.image.service.impl.ImageServiceImpl;
import com.foodies.freshmeal.restaurant.constants.RestaurantBranchFieldConstants;
import com.foodies.freshmeal.restaurant.constants.RestaurantErrorConstants;
import com.foodies.freshmeal.restaurant.constants.RestaurantFieldConstants;
import com.foodies.freshmeal.restaurant.entity.RestaurantBranchEntity;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;
import com.foodies.freshmeal.restaurant.repository.IRestaurantBranchRepository;
import com.foodies.freshmeal.restaurant.repository.IRestaurantRepository;
import com.foodies.freshmeal.user.entity.UserEntity;
import com.foodies.freshmeal.user.entity.UserProfile;

@Service
public class FoodServiceImpl implements IFoodService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final IImageService imageService;
    private final IServiceContext serviceContext;
    private final IDatabaseSequenceService databaseSequenceService;
    private final IFoodRepository foodRepository;
    private final IFoodNavigationService foodNavigationService;
    private final FoodValidator foodValidator;
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantBranchRepository restaurantBranchRepository;
    private final FoodMapper foodMapper;

    public FoodServiceImpl(
            IImageService imageService,
            IServiceContext serviceContext,
            IDatabaseSequenceService databaseSequenceService,
            IFoodRepository foodRepository,
            IFoodNavigationService foodNavigationService,
            FoodValidator foodValidator,
            IRestaurantRepository restaurantRepository,
            IRestaurantBranchRepository restaurantBranchRepository,
            FoodMapper foodMapper) {

        this.imageService = imageService;
        this.serviceContext = serviceContext;
        this.databaseSequenceService = databaseSequenceService;
        this.foodRepository = foodRepository;
        this.foodNavigationService = foodNavigationService;
        this.foodValidator = foodValidator;
        this.restaurantRepository = restaurantRepository;
        this.restaurantBranchRepository = restaurantBranchRepository;
        this.foodMapper = foodMapper;
    }

    /**
     * ============================================================================
     * Load Active Food
     * ============================================================================
     *
     * Loads an active Food entity using its identifier.
     *
     * <p>
     * This method is the common active-Food loading path used by Food operations
     * such as editing, status updates, and archiving.
     * </p>
     *
     * <p>
     * The repository's active-record contract is used instead of the raw
     * Spring Data {@code findById(...)} operation so logically deleted Foods
     * are not returned through active operations.
     * </p>
     *
     * @param request service input containing the Food identifier
     *
     * @return service output containing the active Food entity
     *
     * @throws NullPointerException      when the request or request payload is null
     * @throws ResourceNotFoundException when the Food does not exist as an
     *                                   active record
     */
    @Override
    public IServiceOutput<FoodEntity> loadFood(
            final IServiceInput<FoodIdRequest> request) {

        Objects.requireNonNull(
                request,
                "Food load service input must not be null.");

        final FoodIdRequest foodRequest = Objects.requireNonNull(
                request.getInput(),
                "Food id request must not be null.");

        final FoodEntity foodEntity = foodRepository.findActiveById(
                foodRequest.getFoodId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                FoodErrorConstants.FOOD_NOT_FOUND));

        return new ServiceOutput<>(foodEntity);
    }

    @Override
    public IServiceOutput<String> generateFoodId(IServiceInput<CreateFoodInputDTO> input) {

        long seq = databaseSequenceService.generateSequence(serviceContext, SequenceConstants.FOOD_SEQUENCE);

        LOGGER.info("Generated food ID: FOD01_{}", seq);

        IServiceOutput<String> output = new ServiceOutput<>();

        String foodId = String.format(SequenceConstants.FOOD_DB_ID_PATTERN, seq);

        output.setOutput(foodId);
        return output;

    }

    @Override
    public IServiceOutput<String> generateFoodNumber(IServiceInput<CreateFoodInputDTO> input) {

        long seq = databaseSequenceService.getCurrentSequence(serviceContext, SequenceConstants.FOOD_SEQUENCE);

        LOGGER.info("Generated food Number: FOD01_{}", seq);

        IServiceOutput<String> output = new ServiceOutput<>();

        String foodId = String.format(SequenceConstants.FOOD_NUMBER_PATTERN, seq);

        output.setOutput(foodId);
        return output;

    }

    @Override
    public IServiceOutput<FoodResponse> addFood(IServiceInput<CreateFoodInputDTO> input) {
        /*
         * Create a new food entity using the food request and image file
         */
        IServiceOutput<FoodEntity> foodEntityOutput = createFoodEntity(input);
        FoodEntity foodEntity = foodEntityOutput.getOutput();

        FoodResponse foodResponse = convertToFoodResponse(foodEntity);

        IServiceOutput<FoodResponse> output = new ServiceOutput<>();
        output.setOutput(foodResponse);
        return output;
    }

    private FoodResponse convertToFoodResponse(FoodEntity foodEntity) {
        Objects.requireNonNull(
                foodEntity,
                "Food entity must not be null.");
        FoodResponse foodResponse = foodMapper.toResponse(foodEntity);

        return foodResponse;
    }

    /**
     * Creates and persists a new Food entity.
     *
     * <p>
     * The creation workflow resolves the target restaurant and branch,
     * validates ownership authorization, maps client-editable food data,
     * generates system identifiers, processes the food image, applies audit
     * information and persists the resulting entity.
     * </p>
     *
     * @param input food creation service input
     *
     * @return service output containing the persisted food entity
     */
    private IServiceOutput<FoodEntity> createFoodEntity(
            final IServiceInput<CreateFoodInputDTO> input) {

        Objects.requireNonNull(
                input,
                "Food service input must not be null.");

        final CreateFoodInputDTO request = Objects.requireNonNull(
                input.getInput(),
                "Create food request must not be null.");

        final FoodRequest foodRequest = Objects.requireNonNull(
                request.getFoodRequest(),
                "Food request must not be null.");

        // =========================================================================
        // Resolve Restaurant
        // =========================================================================

        final RestaurantEntity restaurant = resolveRestaurantForFoodCreation(
                request.getRestaurantNumber());

        // =========================================================================
        // Resolve Restaurant Branch
        // =========================================================================

        final RestaurantBranchEntity restaurantBranch = resolveRestaurantBranchForFoodCreation(
                restaurant,
                request.getRestaurantBranchNumber());

        // =========================================================================
        // Validate Authorization
        // =========================================================================

        validateFoodCreationAuthorization(restaurant);

        // =========================================================================
        // Map Client-Editable Fields
        // =========================================================================

        final FoodEntity foodEntity = foodMapper.toEntity(foodRequest);

        // =========================================================================
        // Generate Food Identifiers
        // =========================================================================

        final IServiceInput<CreateFoodInputDTO> foodServiceInput = new ServiceInput<>();

        foodServiceInput.setInput(request);
        foodServiceInput.setServiceContext(
                input.getServiceContext());

        final String foodId = generateFoodId(foodServiceInput).getOutput();

        final String foodNumber = generateFoodNumber(foodServiceInput).getOutput();

        foodEntity.setId(foodId);
        foodEntity.setFoodNumber(foodNumber);

        // =========================================================================
        // Assign Restaurant Ownership
        // =========================================================================

        foodEntity.setRestaurantNumber(
                restaurant.getRestaurantNumber());

        foodEntity.setRestaurantBranchNumber(
                restaurantBranch.getBranchNumber());

        // =========================================================================
        // Process Food Image
        // =========================================================================

        applyFoodImage(
                foodEntity,
                request.getImageFile());

        // =========================================================================
        // Audit Information
        // =========================================================================

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile != null
                && userProfile.getUserEntity() != null) {

            foodEntity.setCreatedBy(
                    userProfile.getUserEntity().getUserNumber());
        }

        foodEntity.setCreatedAt(
                AppCalendar.getBusinessLocalDateTime());

        // =========================================================================
        // Initial Availability
        // =========================================================================

        foodEntity.setAvailable(true);

        // =========================================================================
        // Persist
        // =========================================================================

        final FoodEntity savedFood = foodRepository.save(foodEntity);

        LOGGER.info(
                "Food created successfully. foodNumber={}, restaurantNumber={}, restaurantBranchNumber={}",
                savedFood.getFoodNumber(),
                savedFood.getRestaurantNumber(),
                savedFood.getRestaurantBranchNumber());

        return new ServiceOutput<>(savedFood);
    }

    /**
     * Resolves the active restaurant associated with the supplied restaurant
     * number.
     *
     * <p>
     * Restaurant numbers are business identifiers and therefore the lookup is
     * performed using the repository's query-based infrastructure rather than
     * relying on the MongoDB document identifier.
     * </p>
     *
     * @param restaurantNumber restaurant business number
     * @return active restaurant entity
     *
     * @throws ResourceNotFoundException when the restaurant does not exist
     */
    private RestaurantEntity resolveRestaurantForFoodCreation(
            final String restaurantNumber) {

        if (CommonUtils.isBlank(restaurantNumber)) {
            throw new ResourceNotFoundException(
                    RestaurantErrorConstants.RESTAURANT_NOT_FOUND);
        }

        final Query query = Query.query(
                Criteria.where("restaurantNumber").is(restaurantNumber));

        return restaurantRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RestaurantErrorConstants.RESTAURANT_NOT_FOUND));
    }

    /**
     * Resolves and validates the restaurant branch associated with a restaurant.
     *
     * <p>
     * The branch is resolved using its business branch number. The resolved branch
     * must belong to the supplied restaurant; otherwise the request is considered
     * invalid because a branch from another restaurant cannot be used to create
     * food for the selected restaurant.
     * </p>
     *
     * @param restaurant             resolved restaurant
     * @param restaurantBranchNumber restaurant branch business number
     * @return active restaurant branch
     *
     * @throws ResourceNotFoundException when the branch does not exist
     * @throws BusinessException         when the branch does not belong to the
     *                                   restaurant
     */
    private RestaurantBranchEntity resolveRestaurantBranchForFoodCreation(
            final RestaurantEntity restaurant,
            final String restaurantBranchNumber) {

        Objects.requireNonNull(
                restaurant,
                "Restaurant must not be null.");

        if (CommonUtils.isBlank(restaurantBranchNumber)) {
            throw new ResourceNotFoundException(
                    RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND);
        }

        final Query query = Query.query(
                Criteria.where("branchNumber").is(restaurantBranchNumber));

        final RestaurantBranchEntity restaurantBranch = restaurantBranchRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND));

        // =========================================================================
        // Validate Restaurant → Branch Relationship
        // =========================================================================

        if (!Objects.equals(
                restaurant.getId(),
                restaurantBranch.getRestaurantId())) {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_BELONG_TO_RESTAURANT);
        }

        return restaurantBranch;
    }

    /**
     * Validates whether the currently authenticated user is authorized to create
     * food for the supplied restaurant.
     *
     * <p>
     * Administrators can manage food for any valid restaurant. Restaurant owners
     * are restricted to restaurants owned by their authenticated FreshMeal user.
     * </p>
     *
     * @param restaurant target restaurant
     *
     * @throws IllegalStateException when the authenticated user context is missing
     * @throws BusinessException     when the authenticated user is not authorized
     */
    private void validateFoodCreationAuthorization(
            final RestaurantEntity restaurant) {

        Objects.requireNonNull(
                restaurant,
                "Restaurant must not be null.");

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile == null) {
            throw new IllegalStateException(
                    "Authenticated user profile is required.");
        }

        final UserEntity userEntity = userProfile.getUserEntity();

        Objects.requireNonNull(
                userEntity,
                "Authenticated user entity must not be null.");

        final List<RoleType> roles = userEntity.getRoles();

        if (roles == null || roles.isEmpty()) {
            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        // -------------------------------------------------------------------------
        // ADMIN
        // -------------------------------------------------------------------------

        if (roles.contains(RoleType.ADMIN)) {
            return;
        }

        // -------------------------------------------------------------------------
        // RESTAURANT OWNER
        // -------------------------------------------------------------------------

        if (roles.contains(RoleType.RESTAURANT_OWNER)
                && Objects.equals(
                        userEntity.getUserNumber(),
                        restaurant.getOwnerUserNumber())) {

            return;
        }

        throw new BusinessException(
                RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
    }

    /**
     * Applies the supplied food image to the food entity.
     *
     * <p>
     * When no image is supplied, the configured FreshMeal default food image is
     * assigned. When an image is provided, it is uploaded through the existing
     * image service and the resulting image information is assigned to the food
     * entity.
     * </p>
     *
     * @param foodEntity food entity receiving the image
     * @param imageFile  optional uploaded image
     *
     * @throws IllegalArgumentException when the food entity is null
     * @throws IllegalStateException    when the image service does not return an
     *                                  image entity
     */
    private void applyFoodImage(
            final FoodEntity foodEntity,
            final MultipartFile imageFile) {

        Objects.requireNonNull(
                foodEntity,
                "Food entity must not be null.");

        // =========================================================================
        // Default Image
        // =========================================================================

        if (imageFile == null || imageFile.isEmpty()) {

            final ImageSnapshot defaultImage = new ImageSnapshot();

            defaultImage.setImageName(
                    DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE);

            defaultImage.setImageURL(
                    DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE_URL);

            foodEntity.setFoodImage(defaultImage);

            return;
        }

        // =========================================================================
        // Upload Image
        // =========================================================================

        final CreateImageInputDTO imageInputDTO = new CreateImageInputDTO();

        imageInputDTO.setFile(imageFile);

        final IServiceInput<CreateImageInputDTO> imageServiceInput = new ServiceInput<>();

        imageServiceInput.setInput(imageInputDTO);
        imageServiceInput.setServiceContext(serviceContext);

        final IServiceOutput<ImageEntity> imageOutput = imageService.uploadImageToS3(imageServiceInput);

        Objects.requireNonNull(
                imageOutput,
                "Image service output must not be null.");

        final ImageEntity imageEntity = Objects.requireNonNull(
                imageOutput.getOutput(),
                "Image service image entity must not be null.");

        // =========================================================================
        // Assign Image Snapshot
        // =========================================================================

        final ImageSnapshot imageSnapshot = new ImageSnapshot();

        imageSnapshot.setImageId(
                imageEntity.getId());

        imageSnapshot.setImageName(
                imageEntity.getImageName());

        imageSnapshot.setImageURL(
                imageEntity.getImageUrl());

        foodEntity.setFoodImage(imageSnapshot);
    }

    /**
     * Retrieves active Food items within the authenticated user's authorized
     * restaurant scope.
     *
     * <p>
     * Administrators can access Food items across all active restaurants.
     * Restaurant owners are restricted to Food items belonging to restaurants
     * owned by the authenticated FreshMeal user.
     * </p>
     *
     * <p>
     * Restaurant and branch filtering will be applied through the dedicated
     * restaurant/branch scope contract once that read context is introduced.
     * Until then, the service enforces the highest-level role ownership scope
     * available from the current service input.
     * </p>
     *
     * @param input service input containing the current service context
     *
     * @return active Food responses within the caller's authorized scope
     *
     * @throws NullPointerException when the service input or service context
     *                              is null
     * @throws BusinessException    when the authenticated role is not permitted
     *                              to access Food management data
     */
    @Override
    public IServiceOutput<List<FoodResponse>> readFoodsForManagement(
            final IServiceInput<Void> input) {

        Objects.requireNonNull(
                input,
                "Food read service input must not be null.");

        final IServiceContext inputServiceContext = Objects.requireNonNull(
                input.getServiceContext(),
                "Food read service context must not be null.");

        // =========================================================================
        // Resolve Authorized Restaurant Scope
        // =========================================================================

        final List<String> restaurantNumbers = resolveFoodReadRestaurantNumbers(inputServiceContext);

        // =========================================================================
        // Load Active Foods
        // =========================================================================

        final List<FoodEntity> foodEntities;

        if (restaurantNumbers == null) {

            // ---------------------------------------------------------------------
            // ADMIN
            // ---------------------------------------------------------------------
            // Null scope means the authenticated administrator has unrestricted
            // restaurant scope for this Food management operation.

            foodEntities = foodRepository.findAllActive();

        } else if (restaurantNumbers.isEmpty()) {

            // ---------------------------------------------------------------------
            // RESTAURANT OWNER WITHOUT RESTAURANTS
            // ---------------------------------------------------------------------
            // The authenticated owner currently has no restaurant ownership scope.

            foodEntities = List.of();

        } else {

            // ---------------------------------------------------------------------
            // RESTAURANT OWNER
            // ---------------------------------------------------------------------

            final Query query = Query.query(
                    Criteria.where("restaurantNumber")
                            .in(restaurantNumbers));

            foodEntities = foodRepository.findAll(query);
        }

        // =========================================================================
        // Map Entity → Response
        // =========================================================================

        final List<FoodResponse> responses = foodEntities.stream()
                .filter(Objects::nonNull)
                .map(foodMapper::toResponse)
                .toList();

        LOGGER.info(
                "Active Food records retrieved successfully. count={}, restaurantScope={}",
                responses.size(),
                restaurantNumbers == null
                        ? "ALL"
                        : restaurantNumbers);

        return new ServiceOutput<>(responses);
    }

    /**
     * Resolves the restaurant numbers that the authenticated user is authorized
     * to access for Food management reads.
     *
     * <p>
     * The method deliberately resolves authorization from the authenticated
     * {@link UserProfile} rather than trusting restaurant identifiers supplied
     * by the client.
     * </p>
     *
     * <p>
     * The returned value has three possible meanings:
     * </p>
     *
     * <ul>
     * <li>{@code null} — unrestricted restaurant scope, currently ADMIN.</li>
     * <li>empty list — authenticated owner has no restaurants.</li>
     * <li>non-empty list — explicitly authorized restaurant numbers.</li>
     * </ul>
     *
     * @param inputServiceContext current service execution context
     *
     * @return authorized restaurant numbers, or {@code null} for unrestricted
     *         administrator scope
     *
     * @throws BusinessException when the authenticated user does not have a
     *                           permitted Food management role
     */
    private List<String> resolveFoodReadRestaurantNumbers(
            final IServiceContext inputServiceContext) {

        final UserProfile userProfile = Objects.requireNonNull(
                inputServiceContext.getUserProfile(),
                "Authenticated user profile is required.");

        final UserEntity userEntity = Objects.requireNonNull(
                userProfile.getUserEntity(),
                "Authenticated user entity is required.");

        final List<RoleType> roles = userEntity.getRoles();

        if (roles == null || roles.isEmpty()) {
            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        // =========================================================================
        // ADMIN
        // =========================================================================

        if (roles.contains(RoleType.ADMIN)) {
            return null;
        }

        // =========================================================================
        // RESTAURANT OWNER
        // =========================================================================

        if (roles.contains(RoleType.RESTAURANT_OWNER)) {

            final Query query = Query.query(
                    Criteria.where("ownerUserNumber")
                            .is(userEntity.getUserNumber()));

            return restaurantRepository.findAll(query)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(RestaurantEntity::getRestaurantNumber)
                    .filter(Objects::nonNull)
                    .toList();
        }

        // =========================================================================
        // Unsupported Food Management Role
        // =========================================================================

        throw new BusinessException(
                RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
    }

    @Override
    public IServiceOutput<List<DisplayOptionResponse>> getFoodCategories(IServiceInput<Void> input) {
        List<DisplayOptionResponse> foodCategoriesList = Arrays.stream(FoodCategoryConstant.values())
                .sorted(Comparator.comparing((FoodCategoryConstant cg) -> cg.getLabel(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DisplayOptionResponse(groupCategory.getLabel(), groupCategory.name()))
                .toList();

        return new ServiceOutput<>(foodCategoriesList);
    }

    @Override
    public IServiceOutput<List<DisplayOptionResponse>> getDietCategories(IServiceInput<Void> input) {
        List<DisplayOptionResponse> dietCategoriesList = Arrays.stream(DietCategoryConstant.values())
                .sorted(Comparator.comparing((DietCategoryConstant cg) -> cg.getLabel(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DisplayOptionResponse(groupCategory.getLabel(), groupCategory.name()))
                .toList();

        return new ServiceOutput<>(dietCategoriesList);
    }

    @Override
    public IServiceOutput<List<DisplayOptionResponse>> getCuisineCategories(IServiceInput<Void> input) {
        List<DisplayOptionResponse> cuisineCategoriesList = Arrays.stream(CuisineTypeConstant.values())
                .sorted(Comparator.comparing((CuisineTypeConstant cg) -> cg.getLabel(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DisplayOptionResponse(groupCategory.getLabel(), groupCategory.name()))
                .toList();

        return new ServiceOutput<>(cuisineCategoriesList);
    }

    @Override
    public IServiceOutput<List<DisplayOptionResponse>> getGroupCategories(IServiceInput<Void> input) {
        List<DisplayOptionResponse> groupCategoriesList = Arrays.stream(CategoryGroupConstant.values())
                .sorted(Comparator.comparing((CategoryGroupConstant cg) -> cg.getLabel(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DisplayOptionResponse(groupCategory.getLabel(), groupCategory.name()))
                .toList();

        return new ServiceOutput<>(groupCategoriesList);
    }

    @Override
    public IServiceOutput<FoodMetadataResponse> foodCategoryMetadata(IServiceInput<Void> input) {

        FoodMetadataResponse foodMetadataResponse = FoodMetadataResponse.builder()
                .foodCategories(DisplayOptionMapperUtil.toDisplayOptions(FoodCategoryConstant.class))
                .dietCategories(DisplayOptionMapperUtil.toDisplayOptions(DietCategoryConstant.class))
                .cuisineCategories(DisplayOptionMapperUtil.toDisplayOptions(CuisineTypeConstant.class))
                .groupCategories(DisplayOptionMapperUtil.toDisplayOptions(CategoryGroupConstant.class))
                .foodStatuses(DisplayOptionMapperUtil.toDisplayOptions(FoodStatusConstant.class))
                .build();

        IServiceOutput<FoodMetadataResponse> output = new ServiceOutput<>();
        output.setOutput(foodMetadataResponse);

        return output;
    }

    /**
     * ============================================================================
     * Read Food By ID
     * ============================================================================
     *
     * Reads an active Food by its identifier after validating that the
     * authenticated user is authorized to access the Food.
     *
     * <p>
     * Food identifiers are resource identifiers only and must never be treated
     * as authorization credentials. The Food ownership hierarchy is therefore
     * resolved before the Food response is returned.
     * </p>
     *
     * <p>
     * The authorization flow is:
     * </p>
     *
     * <pre>
     * Food
     *   -> Restaurant
     *   -> Restaurant Branch
     *   -> Authenticated User / Role
     *   -> Authorization
     * </pre>
     *
     * @param input service input containing the Food identifier
     *
     * @return service output containing the authorized Food response
     *
     * @throws NullPointerException      when the input or request is null
     * @throws ResourceNotFoundException when the Food, Restaurant, or Branch
     *                                   cannot be found
     * @throws BusinessException         when the authenticated user is not
     *                                   authorized
     *                                   to access the Food
     */
    @Override
    public IServiceOutput<FoodResponse> readFoodByFoodId(
            final IServiceInput<FoodStatusRequest> input) {

        Objects.requireNonNull(
                input,
                "Food read service input must not be null.");

        final FoodStatusRequest foodRequest = Objects.requireNonNull(
                input.getInput(),
                "Food read request must not be null.");

        final FoodEntity foodEntity = loadFoodById(foodRequest.getFoodId());

        /*
         * Resolve and validate the complete Food ownership hierarchy.
         *
         * Food
         * -> Restaurant
         * -> Restaurant Branch
         */
        final RestaurantEntity restaurant = resolveRestaurantForFood(foodEntity);

        resolveRestaurantBranchForFood(
                restaurant,
                foodEntity);

        /*
         * Validate that the authenticated actor is allowed to access
         * this Food within the resolved Restaurant scope.
         */
        validateFoodAccessAuthorization(restaurant);

        final FoodResponse foodResponse = foodMapper.toResponse(foodEntity);

        final IServiceOutput<FoodResponse> output = new ServiceOutput<>();

        output.setOutput(foodResponse);

        return output;
    }

    /**
     * ============================================================================
     * Resolve Restaurant For Food
     * ============================================================================
     *
     * Resolves the active Restaurant associated with the supplied Food.
     *
     * <p>
     * The Restaurant is resolved using the Restaurant business identifier
     * persisted on the Food entity. The repository's active-query contract
     * ensures that a logically deleted Restaurant is not returned.
     * </p>
     *
     * <p>
     * This method is intentionally limited to relationship resolution. It does
     * not perform role or ownership authorization.
     * </p>
     *
     * @param foodEntity Food entity containing the Restaurant identifier
     *
     * @return active Restaurant associated with the Food
     *
     * @throws NullPointerException      when the Food entity is null
     * @throws ResourceNotFoundException when the Restaurant cannot be found
     */
    private RestaurantEntity resolveRestaurantForFood(
            final FoodEntity foodEntity) {

        Objects.requireNonNull(
                foodEntity,
                "Food entity must not be null.");

        final Query query = Query.query(
                Criteria.where(
                        RestaurantFieldConstants.RESTAURANT_NUMBER)
                        .is(foodEntity.getRestaurantNumber()));

        return restaurantRepository.findOne(query)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                RestaurantErrorConstants.RESTAURANT_NOT_FOUND));
    }

    /**
     * ============================================================================
     * Resolve Restaurant Branch For Food
     * ============================================================================
     *
     * Resolves the active Restaurant Branch associated with the supplied Food.
     *
     * <p>
     * The branch is resolved using the branch identifier persisted on the Food
     * entity. After resolving the branch, its Restaurant relationship is verified
     * against the supplied Restaurant.
     * </p>
     *
     * <p>
     * This relationship validation is important because the Food stores both
     * Restaurant and Restaurant Branch identifiers. We must never assume that
     * those identifiers belong to the same ownership hierarchy merely because
     * they were persisted together.
     * </p>
     *
     * <p>
     * This method is intentionally limited to relationship resolution and
     * validation. It does not perform role or ownership authorization.
     * </p>
     *
     * @param restaurant Restaurant associated with the Food
     * @param foodEntity Food entity containing the branch identifier
     *
     * @return active Restaurant Branch associated with the Food
     *
     * @throws NullPointerException      when the Restaurant or Food is null
     * @throws ResourceNotFoundException when the Restaurant Branch cannot
     *                                   be found
     * @throws BusinessException         when the branch does not belong to the
     *                                   supplied Restaurant
     */
    private RestaurantBranchEntity resolveRestaurantBranchForFood(
            final RestaurantEntity restaurant,
            final FoodEntity foodEntity) {

        Objects.requireNonNull(
                restaurant,
                "Restaurant entity must not be null.");

        Objects.requireNonNull(
                foodEntity,
                "Food entity must not be null.");

        final Query query = Query.query(
                Criteria.where(
                        RestaurantBranchFieldConstants.BRANCH_NUMBER)
                        .is(foodEntity.getRestaurantBranchNumber()));

        final RestaurantBranchEntity restaurantBranch = restaurantBranchRepository.findOne(query)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                RestaurantErrorConstants.RESTAURANT_BRANCH_NOT_FOUND));

        if (!restaurant.getId().equals(
                restaurantBranch.getRestaurantId())) {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        return restaurantBranch;
    }

    /**
     * ============================================================================
     * Validate Food Access Authorization
     * ============================================================================
     *
     * Validates whether the authenticated user is authorized to access a Food
     * belonging to the supplied Restaurant.
     *
     * <p>
     * Administrative users can access Foods across all Restaurants. A
     * Restaurant Owner can access a Food only when the Food's Restaurant is
     * owned by the authenticated user.
     * </p>
     *
     * <p>
     * The Restaurant ownership relationship is resolved from persisted domain
     * data. No Restaurant identifier supplied by the client is trusted for
     * authorization.
     * </p>
     *
     * @param restaurant Restaurant associated with the Food
     *
     * @throws NullPointerException when the Restaurant is null
     * @throws BusinessException    when the authenticated user is not authorized
     *                              to access the Restaurant
     */
    private void validateFoodAccessAuthorization(
            final RestaurantEntity restaurant) {

        Objects.requireNonNull(
                restaurant,
                "Restaurant entity must not be null.");

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile == null
                || userProfile.getUserEntity() == null) {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        final UserEntity userEntity = userProfile.getUserEntity();

        final List<RoleType> roles = userEntity.getRoles();

        if (roles == null || roles.isEmpty()) {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        /*
         * ADMIN has unrestricted Food access.
         */
        if (roles.contains(RoleType.ADMIN)) {
            return;
        }

        /*
         * RESTAURANT_OWNER can access Food only when the authenticated
         * user owns the Restaurant associated with that Food.
         */
        if (roles.contains(RoleType.RESTAURANT_OWNER)
                && userEntity.getUserNumber() != null
                && userEntity.getUserNumber().equals(
                        restaurant.getOwnerUserNumber())) {

            return;
        }

        throw new BusinessException(
                RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
    }

    @Override
    public IServiceOutput<FoodResponse> updateFoodStatus(
            final IServiceInput<FoodStatusRequest> input) {

        Objects.requireNonNull(
                input,
                "Food status service input must not be null.");

        final FoodStatusRequest request = Objects.requireNonNull(
                input.getInput(),
                "Food status request must not be null.");

        // =========================================================================
        // Load Existing Food
        // =========================================================================

        final IServiceInput<FoodIdRequest> foodInput = new ServiceInput<>();

        final FoodIdRequest foodIdRequest = new FoodIdRequest();

        foodIdRequest.setFoodId(request.getFoodId());

        foodInput.setInput(foodIdRequest);
        foodInput.setServiceContext(input.getServiceContext());

        final FoodEntity foodEntity = loadFood(foodInput).getOutput();

        // =========================================================================
        // Resolve Existing Ownership
        // =========================================================================

        /*
         * Food ownership is persisted on the Food entity and cannot be changed
         * through a status operation.
         *
         * The persisted Restaurant and Restaurant Branch therefore remain the
         * source of truth for authorization.
         */
        final RestaurantEntity restaurant = resolveRestaurantForFood(foodEntity);

        resolveRestaurantBranchForFood(
                restaurant,
                foodEntity);

        validateFoodAccessAuthorization(restaurant);

        // =========================================================================
        // Resolve Requested Status
        // =========================================================================

        final FoodStatusConstant requestedStatus;

        if (request.getUpdateFoodStatusRequest() == null
                || request.getUpdateFoodStatusRequest().getStatus() == null) {

            throw new InvalidFoodStatusTransitionException(
                    "Food status is required.");
        }

        final String requestedFoodStatus = request.getUpdateFoodStatusRequest()
                .getStatus()
                .getValue() != null
                        ? request.getUpdateFoodStatusRequest()
                                .getStatus()
                                .getValue()
                        : request.getUpdateFoodStatusRequest()
                                .getStatus()
                                .getLabel();

        if (CommonUtils.isBlank(requestedFoodStatus)) {
            throw new InvalidFoodStatusTransitionException(
                    "Food status is required.");
        }

        requestedStatus = DisplayOptionMapperUtil.fromValue(
                FoodStatusConstant.class,
                requestedFoodStatus);

        // =========================================================================
        // Validate Status Transition
        // =========================================================================

        final FoodStatusConstant currentStatus = foodEntity.getStatus();

        validateStatusTransition(
                currentStatus,
                requestedStatus,
                input.getServiceContext());

        // =========================================================================
        // Apply Status
        // =========================================================================

        applyStatus(
                foodEntity,
                requestedStatus);

        // =========================================================================
        // Update Audit Information
        // =========================================================================

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile != null
                && userProfile.getUserEntity() != null) {

            foodEntity.setUpdatedBy(
                    userProfile
                            .getUserEntity()
                            .getUserNumber());
        }

        foodEntity.setUpdatedAt(
                AppCalendar.getBusinessLocalDateTime());

        // =========================================================================
        // Persist
        // =========================================================================

        final FoodEntity savedFood = foodRepository.save(foodEntity);

        LOGGER.info(
                "Food status updated successfully. foodNumber={}, restaurantNumber={}, restaurantBranchNumber={}, previousStatus={}, newStatus={}",
                savedFood.getFoodNumber(),
                savedFood.getRestaurantNumber(),
                savedFood.getRestaurantBranchNumber(),
                currentStatus,
                requestedStatus);

        // =========================================================================
        // Build Response
        // =========================================================================

        final FoodResponse foodResponse = foodMapper.toResponse(savedFood);

        foodResponse.setPreviousStatus(
                DisplayOptionMapperUtil.from(currentStatus));

        return new ServiceOutput<>(foodResponse);
    }

    private void validateStatusTransition(FoodStatusConstant currentStatus, FoodStatusConstant requestedStatus,
            IServiceContext serviceContext1) {

        if (currentStatus == null) {
            throw new InvalidFoodStatusTransitionException("Food status is not set.");
        }

        if (currentStatus == requestedStatus && !MethodType.UPDATE.equals(serviceContext1.getMethodType())) {
            throw new InvalidFoodStatusTransitionException("Food is already in status : " + requestedStatus);
        }

        if (!currentStatus.canTransitionTo(requestedStatus)
                && !MethodType.UPDATE.equals(serviceContext1.getMethodType())) {

            throw new InvalidFoodStatusTransitionException(
                    String.format("Food status cannot be changed from %s to %s.", currentStatus, requestedStatus));

        }

    }

    private void applyStatus(
            final FoodEntity food,
            final FoodStatusConstant newStatus) {

        Objects.requireNonNull(
                food,
                "Food entity must not be null.");

        Objects.requireNonNull(
                newStatus,
                "New food status must not be null.");

        food.setStatus(newStatus);

        food.setStatusUpdatedAt(
                AppCalendar.getBusinessLocalDateTime());

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile != null
                && userProfile.getUserEntity() != null) {

            food.setStatusUpdatedBy(
                    userProfile
                            .getUserEntity()
                            .getUserNumber());
        }
    }

    @Override
    public IServiceOutput<EntityViewResponse<FoodResponse>> getFoodByFoodId(IServiceInput<FoodStatusRequest> request) {

        // =========================================================================
        // Request
        // =========================================================================

        FoodStatusRequest foodRequest = request.getInput();

        LOGGER.info("Fetching food details for Food Id : {}", foodRequest.getFoodId());

        // =========================================================================
        // Retrieve Food
        // =========================================================================

        FoodEntity foodEntity = foodRepository.findById(foodRequest.getFoodId()).orElseThrow(() -> {
            LOGGER.error("Food not found with Food Id : {}", foodRequest.getFoodId());

            return new ResourceNotFoundException(FoodErrorConstants.FOOD_NOT_FOUND);
        });

        LOGGER.debug("Food found successfully : {}", foodEntity.getId());

        // =========================================================================
        // Convert Entity to Response
        // =========================================================================

        FoodResponse foodResponse = convertToFoodResponse(foodEntity);

        // =========================================================================
        // Build Entity View Response
        // =========================================================================

        EntityViewResponse<FoodResponse> entityViewResponse = new EntityViewResponse<>();

        entityViewResponse.setData(foodResponse);

        entityViewResponse.setNavigation(foodNavigationService.getNavigation(foodEntity.getId()));

        LOGGER.debug("Navigation generated successfully for Food Id : {}", foodEntity.getId());

        // =========================================================================
        // Build Service Output
        // =========================================================================

        IServiceOutput<EntityViewResponse<FoodResponse>> output = new ServiceOutput<>();

        output.setOutput(entityViewResponse);

        LOGGER.info("Food details retrieved successfully for Food Id : {}", foodEntity.getId());

        return output;
    }

    @Override
    public IServiceOutput<FoodResponse> editFood(
            final IServiceInput<EditFoodInputDTO> input) {

        Objects.requireNonNull(
                input,
                "Food edit service input must not be null.");

        final EditFoodInputDTO editRequest = Objects.requireNonNull(
                input.getInput(),
                "Food edit request must not be null.");

        final FoodRequest foodRequest = Objects.requireNonNull(
                editRequest.getFoodRequest(),
                "Food request must not be null.");

        // =========================================================================
        // Load Existing Food
        // =========================================================================

        final IServiceInput<FoodIdRequest> foodInput = new ServiceInput<>();

        final FoodIdRequest foodIdRequest = new FoodIdRequest();

        foodIdRequest.setFoodId(editRequest.getFoodId());

        foodInput.setInput(foodIdRequest);
        foodInput.setServiceContext(input.getServiceContext());

        final FoodEntity foodEntity = loadFood(foodInput).getOutput();

        // =========================================================================
        // Resolve Existing Ownership
        // =========================================================================

        /*
         * Food ownership cannot be changed during a normal edit.
         *
         * The persisted Restaurant and Restaurant Branch therefore remain
         * the source of truth for authorization.
         */
        final RestaurantEntity restaurant = resolveRestaurantForFood(foodEntity);

        resolveRestaurantBranchForFood(restaurant, foodEntity);

        validateFoodAccessAuthorization(restaurant);

        // =========================================================================
        // Update Editable Food Fields
        // =========================================================================

        foodMapper.updateEntity(
                foodRequest,
                foodEntity);

        // =========================================================================
        // Update Derived Category Groups
        // =========================================================================

        foodEntity.setCategoryGroups(
                foodRequest.getFoodCategories()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(fc -> Objects.requireNonNull(fc).getGroup())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()));

        // =========================================================================
        // Update Food Image
        // =========================================================================
        // Image is replaced only when a new image is supplied.
        // Otherwise the existing image remains unchanged.

        final MultipartFile imageFile = editRequest.getImageFile();

        if (imageFile != null && !imageFile.isEmpty()) {
            applyFoodImage(
                    foodEntity,
                    imageFile);
        }

        // =========================================================================
        // Audit Information
        // =========================================================================

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile != null
                && userProfile.getUserEntity() != null) {

            foodEntity.setUpdatedBy(
                    userProfile
                            .getUserEntity()
                            .getUserNumber());
        }

        foodEntity.setUpdatedAt(
                AppCalendar.getBusinessLocalDateTime());

        // =========================================================================
        // Persist Updated Food
        // =========================================================================

        final FoodEntity savedFood = foodRepository.save(foodEntity);

        LOGGER.info(
                "Food updated successfully. foodNumber={}, restaurantNumber={}, restaurantBranchNumber={}",
                savedFood.getFoodNumber(),
                savedFood.getRestaurantNumber(),
                savedFood.getRestaurantBranchNumber());

        // =========================================================================
        // Build Response
        // =========================================================================

        final FoodResponse response = foodMapper.toResponse(savedFood);

        return new ServiceOutput<>(response);
    }

    // ============================================================================
    // Archive Operations
    // ============================================================================

    /**
     * Archives an existing Food item using the application's soft-delete
     * mechanism.
     *
     * <p>
     * Before archiving, the method validates that:
     * <ul>
     * <li>the request is valid,</li>
     * <li>the Food exists,</li>
     * <li>its restaurant exists,</li>
     * <li>its restaurant branch exists and belongs to that restaurant,</li>
     * <li>the authenticated actor is authorized to manage that restaurant,</li>
     * <li>the Food is eligible for archiving.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Food ownership is derived from the persisted
     * {@code restaurantNumber} and {@code restaurantBranchNumber}.
     * These values are never accepted from the archive request itself.
     * </p>
     *
     * @param input service input containing the Food identifier
     *              and request context
     * @return archived Food response
     */
    @Override
    public IServiceOutput<FoodResponse> archiveFood(
            final IServiceInput<ArchiveFoodRequest> input) {

        Objects.requireNonNull(
                input,
                "Food archive service input must not be null.");

        // =========================================================================
        // Request Validation
        // =========================================================================

        foodValidator.validateArchiveFood(input);

        final ArchiveFoodRequest request = Objects.requireNonNull(
                input.getInput(),
                "Archive food request must not be null.");

        // =========================================================================
        // Load Food
        // =========================================================================

        final FoodEntity foodEntity = loadFoodById(request.getFoodId());

        // =========================================================================
        // Resolve Existing Ownership
        // =========================================================================

        /*
         * Food ownership is persisted on the Food entity and remains the
         * source of truth for archive authorization.
         */
        final RestaurantEntity restaurant = resolveRestaurantForFood(foodEntity);

        resolveRestaurantBranchForFood(
                restaurant,
                foodEntity);

        // =========================================================================
        // Authorization
        // =========================================================================
        validateFoodAccessAuthorization(restaurant);

        // =========================================================================
        // Business Validation
        // =========================================================================

        validateFoodCanBeArchived(
                foodEntity,
                input.getServiceContext());

        // =========================================================================
        // Archive Food
        // =========================================================================

        foodRepository.softDelete(
                foodEntity.getId(),
                createRepositoryContext(
                        input.getServiceContext()));

        // =========================================================================
        // Response
        // =========================================================================

        final FoodResponse response = foodMapper.toResponse(foodEntity);

        LOGGER.info(
                "Food archived successfully. foodNumber={}, restaurantNumber={}, restaurantBranchNumber={}",
                foodEntity.getFoodNumber(),
                foodEntity.getRestaurantNumber(),
                foodEntity.getRestaurantBranchNumber());

        return new ServiceOutput<>(response);
    }

    // ============================================================================
    // Common Helper Methods
    // ============================================================================

    /**
     * ============================================================================
     * Load Active Food By ID
     * ============================================================================
     *
     * Loads an active Food entity using the supplied Food identifier.
     *
     * <p>
     * This method is an internal entity-loading operation. It is intentionally
     * not responsible for role-based or ownership authorization. The calling
     * business operation must perform the appropriate authorization after the
     * Food has been loaded.
     * </p>
     *
     * <p>
     * The active repository contract is used so logically deleted Foods are not
     * returned through active Food operations.
     * </p>
     *
     * @param foodId unique Food identifier
     *
     * @return active Food entity
     *
     * @throws ResourceNotFoundException when the Food does not exist as an
     *                                   active record
     */
    private FoodEntity loadFoodById(final String foodId) {

        return foodRepository.findActiveById(foodId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                FoodErrorConstants.FOOD_NOT_FOUND));
    }

    /**
     * Creates repository context.
     *
     * @param serviceContext Service context.
     *
     * @return Repository context.
     */
    private RepositoryContext createRepositoryContext(
            final IServiceContext serviceContext) {

        RepositoryContext repositoryContext;
        if (serviceContext.getUserProfile() != null) {
            repositoryContext = RepositoryContext.of(
                    serviceContext.getUserProfile().getUsername(),
                    AppCalendar.getBusinessLocalDateTime());
        } else {
            repositoryContext = RepositoryContext.of(
                    RepositoryConstants.SYSTEM_USER,
                    AppCalendar.getBusinessLocalDateTime());
        }
        return repositoryContext;
    }

    /**
     * Validates whether a food can be archived.
     *
     * @param food           Food entity.
     * @param serviceContext Service context.
     */
    private void validateFoodCanBeArchived(
            final FoodEntity food,
            @SuppressWarnings("unused") final IServiceContext serviceContext) {

        if (food.isDeleted()) {

            throw new BusinessException(FoodErrorConstants.FOOD_CANNOT_BE_ARCHIVED);
        }

        /*
         * Future Rule
         *
         * Check whether active orders reference this food.
         *
         * if(orderService.hasActiveOrders(food.getId()))
         * throw new BusinessException(...);
         */
    }

    /**
     * Validates whether a food can be restored.
     *
     * @param food Food entity.
     */
    private void validateFoodCanBeRestored(
            final FoodEntity food) {

        if (!food.isDeleted()) {

            throw new BusinessException(FoodErrorConstants.FOOD_ALREADY_ACTIVE);
        }
    }

    /**
     * Validates whether a food can be permanently deleted.
     *
     * @param food Food entity.
     */
    private void validateFoodCanBeDeleted(
            final FoodEntity food) {

        if (!food.isDeleted()) {

            throw new BusinessException(FoodErrorConstants.FOOD_CANNOT_BE_DELETED);
        }

        /*
         * Future Rule
         *
         * Prevent deletion when
         * active orders reference this food.
         */
    }

    // ============================================================================
    // Restore Operations
    // ============================================================================

    /**
     * Restores an archived Food item.
     *
     * <p>
     * The Food's persisted restaurant and restaurant branch identifiers are used
     * to resolve its ownership context. The authenticated actor must be
     * authorized to manage that restaurant before the Food can be restored.
     * </p>
     *
     * <p>
     * The restore operation only changes the persistence lifecycle state.
     * Food status, availability, restaurant ownership, and branch ownership are
     * not modified by this operation.
     * </p>
     *
     * @param input service input containing the Food identifier
     *              and request context
     * @return restored Food response
     */
    @Override
    public IServiceOutput<FoodResponse> restoreFood(
            final IServiceInput<RestoreFoodRequest> input) {

        Objects.requireNonNull(
                input,
                "Food restore service input must not be null.");

        // =========================================================================
        // Request Validation
        // =========================================================================

        foodValidator.validateRestoreFood(input);

        final RestoreFoodRequest request = Objects.requireNonNull(
                input.getInput(),
                "Restore food request must not be null.");

        // =========================================================================
        // Load Archived Food
        // =========================================================================

        final FoodEntity foodEntity = loadArchivedFoodById(
                request.getFoodId());

        // =========================================================================
        // Resolve Existing Ownership
        // =========================================================================

        /*
         * The Food is currently archived, but its persisted Restaurant and
         * Restaurant Branch remain the source of truth for authorization.
         */
        final RestaurantEntity restaurant = resolveRestaurantForFood(foodEntity);

        resolveRestaurantBranchForFood(
                restaurant,
                foodEntity);

        // =========================================================================
        // Authorization
        // =========================================================================
        validateFoodAccessAuthorization(restaurant);

        // =========================================================================
        // Business Validation
        // =========================================================================

        validateFoodCanBeRestored(foodEntity);

        // =========================================================================
        // Restore Food
        // =========================================================================

        foodRepository.restore(
                foodEntity.getId(),
                createRepositoryContext(
                        input.getServiceContext()));

        // =========================================================================
        // Response
        // =========================================================================

        final FoodResponse response = foodMapper.toResponse(foodEntity);

        LOGGER.info(
                "Food restored successfully. foodNumber={}, restaurantNumber={}, restaurantBranchNumber={}",
                foodEntity.getFoodNumber(),
                foodEntity.getRestaurantNumber(),
                foodEntity.getRestaurantBranchNumber());

        return new ServiceOutput<>(response);
    }

    /**
     * Loads an archived food by its identifier.
     *
     * @param foodId Food identifier.
     *
     * @return Archived food entity.
     */
    private FoodEntity loadArchivedFoodById(
            final String foodId) {

        return foodRepository.findDeletedById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(FoodErrorConstants.FOOD_ALREADY_ARCHIVED));
    }

    // ============================================================================
    // Permanent Delete Operations
    // ============================================================================

    /**
     * Permanently deletes an archived Food item.
     *
     * <p>
     * Permanent deletion is only allowed for Food items that have already been
     * archived. The Food's restaurant and branch ownership are resolved from the
     * persisted entity before authorization and deletion are performed.
     * </p>
     *
     * <p>
     * This operation is intentionally destructive and is currently exposed as an
     * administrator-only operation at the controller/security layer.
     * </p>
     *
     * @param input service input containing the archived Food identifier
     *              and request context
     * @return deleted Food response
     */
    @Override
    public IServiceOutput<FoodResponse> permanentDeleteFood(
            final IServiceInput<PermanentDeleteFoodRequest> input) {

        Objects.requireNonNull(
                input,
                "Permanent delete service input must not be null.");

        // =========================================================================
        // Request Validation
        // =========================================================================

        foodValidator.validatePermanentDeleteFood(input);

        final PermanentDeleteFoodRequest request = Objects.requireNonNull(
                input.getInput(),
                "Permanent delete food request must not be null.");

        // =========================================================================
        // Load Archived Food
        // =========================================================================

        final FoodEntity foodEntity = loadArchivedFoodById(
                request.getFoodId());

        // =========================================================================
        // Resolve Existing Ownership
        // =========================================================================

        /*
         * The Food is archived at this stage, but its persisted Restaurant and
         * Restaurant Branch remain the source of truth for authorization.
         */
        final RestaurantEntity restaurant = resolveRestaurantForFood(foodEntity);

        resolveRestaurantBranchForFood(
                restaurant,
                foodEntity);

        // =========================================================================
        // Authorization
        // =========================================================================
        validateFoodAccessAuthorization(restaurant);

        // =========================================================================
        // Business Validation
        // =========================================================================

        validateFoodCanBeDeleted(foodEntity);

        // =========================================================================
        // Delete Food Image
        // =========================================================================

        deleteFoodImage(foodEntity);

        // =========================================================================
        // Permanently Delete Food
        // =========================================================================

        foodRepository.deletePermanently(
                foodEntity.getId());

        // =========================================================================
        // Response
        // =========================================================================

        final FoodResponse response = foodMapper.toResponse(foodEntity);

        LOGGER.info(
                "Food permanently deleted. foodNumber={}, restaurantNumber={}, restaurantBranchNumber={}",
                foodEntity.getFoodNumber(),
                foodEntity.getRestaurantNumber(),
                foodEntity.getRestaurantBranchNumber());

        return new ServiceOutput<>(response);
    }

    /**
     * Deletes food image.
     *
     * @param food Food entity.
     */
    private void deleteFoodImage(final FoodEntity food) {

        if (food == null || food.getFoodImage() == null) {
            return;
        }

        final String imageId = food.getFoodImage().getImageId();

        if (imageId == null || imageId.isBlank()) {
            return;
        }

        final IServiceInput<String> input = new ServiceInput<>();
        input.setInput(imageId);

        imageService.deleteImageFromS3(input);
    }

    // ============================================================================
    // Bulk Archive Operations
    // ============================================================================

    /**
     * Archives multiple Food items.
     *
     * <p>
     * Each Food is processed through the standard
     * {@link #archiveFood(IServiceInput)}
     * operation so that all individual validation, restaurant/branch ownership
     * checks, authorization rules, and archive business rules are consistently
     * applied.
     * </p>
     *
     * <p>
     * This is intentionally implemented as an orchestration method rather than
     * directly performing repository bulk deletion. A bulk request may contain
     * Foods belonging to different restaurants or branches, so each Food must be
     * independently authorized.
     * </p>
     *
     * @param input service input containing the Food identifiers to archive
     * @return empty service output after successful processing
     */
    @Override
    public IServiceOutput<Void> bulkArchiveFoods(
            final IServiceInput<BulkArchiveFoodRequest> input) {

        Objects.requireNonNull(
                input,
                "Bulk archive service input must not be null.");

        // =========================================================================
        // Request Validation
        // =========================================================================

        foodValidator.validateBulkArchiveFoods(input);

        final BulkArchiveFoodRequest request = Objects.requireNonNull(
                input.getInput(),
                "Bulk archive request must not be null.");

        // =========================================================================
        // Archive Foods
        // =========================================================================

        for (final String foodId : request.getFoodIds()) {

            final ArchiveFoodRequest archiveRequest = new ArchiveFoodRequest();

            archiveRequest.setFoodId(foodId);

            final IServiceInput<ArchiveFoodRequest> archiveInput = new ServiceInput<>();

            archiveInput.setInput(archiveRequest);
            archiveInput.setServiceContext(
                    input.getServiceContext());

            archiveFood(archiveInput);
        }

        // =========================================================================
        // Response
        // =========================================================================

        LOGGER.info(
                "Bulk food archive completed successfully. foodCount={}",
                request.getFoodIds().size());

        return new ServiceOutput<>();
    }

    // ============================================================================
    // Bulk Restore Operations
    // ============================================================================

    /**
     * Restores multiple archived Food items.
     *
     * <p>
     * Each Food is processed through the standard
     * {@link #restoreFood(IServiceInput)} operation so that restaurant/branch
     * ownership, authorization, lifecycle validation, and repository behavior
     * remain consistent with single Food restoration.
     * </p>
     *
     * <p>
     * Individual authorization is intentional because a bulk request may contain
     * Food items belonging to different restaurants or branches.
     * </p>
     *
     * @param input service input containing the Food identifiers to restore
     * @return empty service output after successful processing
     */
    @Override
    public IServiceOutput<Void> bulkRestoreFoods(
            final IServiceInput<BulkRestoreFoodRequest> input) {

        Objects.requireNonNull(
                input,
                "Bulk restore service input must not be null.");

        // =========================================================================
        // Request Validation
        // =========================================================================

        foodValidator.validateBulkRestoreFoods(input);

        final BulkRestoreFoodRequest request = Objects.requireNonNull(
                input.getInput(),
                "Bulk restore request must not be null.");

        // =========================================================================
        // Restore Foods
        // =========================================================================

        for (final String foodId : request.getFoodIds()) {

            final RestoreFoodRequest restoreRequest = new RestoreFoodRequest();

            restoreRequest.setFoodId(foodId);

            final IServiceInput<RestoreFoodRequest> restoreInput = new ServiceInput<>();

            restoreInput.setInput(restoreRequest);
            restoreInput.setServiceContext(
                    input.getServiceContext());

            restoreFood(restoreInput);
        }

        // =========================================================================
        // Response
        // =========================================================================

        LOGGER.info(
                "Bulk food restore completed successfully. foodCount={}",
                request.getFoodIds().size());

        return new ServiceOutput<>();
    }

    // ============================================================================
    // Bulk Permanent Delete Operations
    // ============================================================================

    /**
     * Permanently deletes multiple archived Food items.
     *
     * <p>
     * Each Food is processed individually through
     * {@link #permanentDeleteFood(IServiceInput)} so that the same ownership,
     * restaurant-branch validation, authorization, and permanent deletion rules
     * are consistently applied to every Food item.
     * </p>
     *
     * @param input service input containing Food identifiers and request context
     * @return empty service output after successful deletion
     */
    @Override
    public IServiceOutput<Void> bulkPermanentDeleteFoods(
            final IServiceInput<BulkDeleteFoodRequest> input) {

        Objects.requireNonNull(
                input,
                "Bulk permanent delete service input must not be null.");

        // =========================================================================
        // Request Validation
        // =========================================================================

        foodValidator.validateBulkPermanentDeleteFoods(input);

        final BulkDeleteFoodRequest request = Objects.requireNonNull(
                input.getInput(),
                "Bulk permanent delete request must not be null.");

        // =========================================================================
        // Permanent Delete
        // =========================================================================

        for (final String foodId : request.getFoodIds()) {

            final PermanentDeleteFoodRequest deleteRequest = new PermanentDeleteFoodRequest();

            deleteRequest.setFoodId(foodId);

            final IServiceInput<PermanentDeleteFoodRequest> deleteInput = new ServiceInput<>();

            deleteInput.setInput(deleteRequest);
            deleteInput.setServiceContext(
                    input.getServiceContext());

            permanentDeleteFood(deleteInput);
        }

        // =========================================================================
        // Logging
        // =========================================================================

        LOGGER.info(
                "Bulk food permanent deletion completed successfully. foodCount={}",
                request.getFoodIds().size());

        return new ServiceOutput<>();
    }

    /**
     * Creates a service input.
     *
     * @param request        Request object.
     * @param serviceContext Service context.
     *
     * @return Service input.
     */
    // private <T> IServiceInput<T> createServiceInput(
    // final T request,
    // final IServiceContext serviceContext) {

    // IServiceInput<T> serviceInput = new ServiceInput<>();

    // serviceInput.setInput(request);
    // serviceInput.setServiceContext(serviceContext);

    // return serviceInput;
    // }

    // ============================================================================
    // Archived Food Operations
    // ============================================================================

    /**
     * {@inheritDoc}
     *
     * <p>
     * Reads archived Foods according to the authenticated user's Restaurant
     * scope.
     * </p>
     *
     * <ul>
     * <li>ADMIN can read archived Foods across all Restaurants.</li>
     * <li>RESTAURANT_OWNER can read archived Foods only from Restaurants
     * owned by the authenticated user.</li>
     * </ul>
     *
     * <p>
     * Archived state is enforced by the repository's
     * {@code findAllDeleted(...)} contract.
     * </p>
     *
     * @return service output containing authorized archived Foods
     */
    @Override
    public IServiceOutput<List<FoodResponse>> readArchivedFoods() {

        // ---------------------------------------------------------------------
        // Resolve authenticated user's Restaurant scope
        // ---------------------------------------------------------------------

        final UserProfile userProfile = serviceContext.getUserProfile();

        if (userProfile == null
                || userProfile.getUserEntity() == null) {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        final UserEntity userEntity = userProfile.getUserEntity();

        final List<RoleType> roles = userEntity.getRoles();

        if (roles == null || roles.isEmpty()) {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        // ---------------------------------------------------------------------
        // Load Archived Foods
        // ---------------------------------------------------------------------

        final List<FoodEntity> archivedFoods;

        if (roles.contains(RoleType.ADMIN)) {

            /*
             * ADMIN has unrestricted archived-Food visibility.
             */
            archivedFoods = foodRepository.findAllDeleted();

        } else if (roles.contains(RoleType.RESTAURANT_OWNER)) {

            /*
             * Resolve Restaurants owned by the authenticated user.
             */
            final Query restaurantQuery = Query.query(
                    Criteria.where(
                            RestaurantFieldConstants.OWNER_USER_NUMBER)
                            .is(userEntity.getUserNumber()));

            final List<String> restaurantNumbers = restaurantRepository.findAll(restaurantQuery)
                    .stream()
                    .map(RestaurantEntity::getRestaurantNumber)
                    .filter(Objects::nonNull)
                    .toList();

            /*
             * The owner has no Restaurants, therefore there can be no
             * archived Foods within the owner's scope.
             */
            if (restaurantNumbers.isEmpty()) {

                archivedFoods = List.of();

            } else {

                final Query foodQuery = Query.query(
                        Criteria.where(
                                RestaurantFieldConstants.RESTAURANT_NUMBER)
                                .in(restaurantNumbers));

                archivedFoods = foodRepository.findAllDeleted(foodQuery);
            }

        } else {

            throw new BusinessException(
                    RestaurantErrorConstants.RESTAURANT_OPERATION_NOT_ALLOWED);
        }

        // ---------------------------------------------------------------------
        // Build Response
        // ---------------------------------------------------------------------

        final List<FoodResponse> response = archivedFoods.stream()
                .map(foodMapper::toResponse)
                .toList();

        final IServiceOutput<List<FoodResponse>> output = new ServiceOutput<>();

        output.setOutput(response);

        return output;
    }

}
