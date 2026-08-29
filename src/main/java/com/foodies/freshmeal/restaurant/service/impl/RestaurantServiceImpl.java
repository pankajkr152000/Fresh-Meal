package com.foodies.freshmeal.restaurant.service.impl;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;
import com.foodies.freshmeal.food.constants.DefaultFoodImageConstants;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.image.service.IImageService;
import com.foodies.freshmeal.restaurant.constants.RestaurantErrorConstants;
import com.foodies.freshmeal.restaurant.constants.RestaurantStatusConstant;
import com.foodies.freshmeal.restaurant.dto.CreateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantAvailabilityUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantStatusUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.UpdateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.entity.RestaurantEntity;
import com.foodies.freshmeal.restaurant.mapper.RestaurantMapper;
import com.foodies.freshmeal.restaurant.repository.IRestaurantRepository;
import com.foodies.freshmeal.restaurant.service.IRestaurantService;

/**
 * ============================================================================
 * Service Implementation : Restaurant
 * ============================================================================
 *
 * <p>
 * Provides business operations for Restaurant management.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Restaurant creation</li>
 * <li>Restaurant retrieval</li>
 * <li>Restaurant business information update</li>
 * <li>Restaurant lifecycle status management</li>
 * <li>Restaurant operational availability management</li>
 * <li>Restaurant identifier generation</li>
 * <li>Restaurant image processing and validation</li>
 * <li>Repository orchestration</li>
 * </ul>
 *
 * <p>
 * DTO transformation remains delegated to {@link RestaurantMapper}. Business
 * validation and repository orchestration remain within this service.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
public class RestaurantServiceImpl implements IRestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final IImageService imageService;

    private final IServiceContext serviceContext;

    private final IDatabaseSequenceService databaseSequenceService;

    private final IRestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    /**
     * Creates RestaurantServiceImpl.
     *
     * @param imageService            image service
     * @param serviceContext          service execution context
     * @param databaseSequenceService database sequence service
     * @param restaurantRepository    restaurant repository
     * @param restaurantMapper        restaurant mapper
     */
    public RestaurantServiceImpl(IImageService imageService, IServiceContext serviceContext,
            IDatabaseSequenceService databaseSequenceService, IRestaurantRepository restaurantRepository,
            RestaurantMapper restaurantMapper) {

        this.imageService = imageService;
        this.serviceContext = serviceContext;
        this.databaseSequenceService = databaseSequenceService;
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    // =========================================================================
    // Create
    // =========================================================================

    /**
     * Creates a new restaurant.
     *
     * <p>
     * A single Restaurant sequence value is used to generate both the internal
     * database identifier and external business restaurant number.
     * </p>
     *
     * <p>
     * Restaurant images are processed through the existing ImageService.
     * </p>
     *
     * @param input restaurant creation input
     *
     * @return created restaurant details
     */
    @Override
    public IServiceOutput<RestaurantDetailsResponse> create(IServiceInput<CreateRestaurantInputDTO> input) {

        Objects.requireNonNull(input, "Restaurant service input must not be null.");

        CreateRestaurantInputDTO request = input.getInput();

        Objects.requireNonNull(request, "Restaurant create request must not be null.");

        // ---------------------------------------------------------------------
        // Process supplied images
        // ---------------------------------------------------------------------

        ImageEntity coverImageEntity = createImageForRestaurant(request.getCoverImageFile());

        validateImageReference(coverImageEntity.getId(), RestaurantErrorConstants.RESTAURANT_COVER_IMAGE_NOT_FOUND);

        ImageEntity logoImageEntity = createImageForRestaurant(request.getLogoImageFile());

        validateImageReference(logoImageEntity.getId(), RestaurantErrorConstants.RESTAURANT_LOGO_IMAGE_NOT_FOUND);

        // ---------------------------------------------------------------------
        // Generate Restaurant sequence
        // ---------------------------------------------------------------------

        long sequence = databaseSequenceService.generateSequence(serviceContext, SequenceConstants.RESTAURANT_SEQUENCE);

        LOGGER.info("Generated Restaurant sequence [{}]", sequence);

        // ---------------------------------------------------------------------
        // Map request → entity
        // ---------------------------------------------------------------------

        RestaurantEntity restaurant = restaurantMapper.toEntity(request);

        // ---------------------------------------------------------------------
        // Assign generated identifiers
        // ---------------------------------------------------------------------

        restaurant.setId(String.format(SequenceConstants.RESTAURANT_DB_ID_PATTERN, sequence));

        restaurant.setRestaurantNumber(String.format(SequenceConstants.RESTAURANT_NUMBER_PATTERN, sequence));

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        restaurant = restaurantRepository.save(restaurant);

        LOGGER.info("Restaurant created successfully. id=[{}], restaurantNumber=[{}]", restaurant.getId(),
                restaurant.getRestaurantNumber());

        // ---------------------------------------------------------------------
        // Map entity → response
        // ---------------------------------------------------------------------

        RestaurantDetailsResponse response = restaurantMapper.toDetailsResponse(restaurant);

        return new ServiceOutput<>(response);
    }

    // =========================================================================
    // Get By ID
    // =========================================================================

    /**
     * Retrieves an active restaurant by identifier.
     *
     * @param input restaurant identifier request
     *
     * @return restaurant details
     */
    @Override
    public IServiceOutput<RestaurantDetailsResponse> getById(IServiceInput<RestaurantIdRequest> input) {

        Objects.requireNonNull(input, "Restaurant service input must not be null.");

        RestaurantIdRequest request = input.getInput();

        Objects.requireNonNull(request, "Restaurant id request must not be null.");

        RestaurantEntity restaurant = restaurantRepository.findActiveById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_NOT_FOUND));

        RestaurantDetailsResponse response = restaurantMapper.toDetailsResponse(restaurant);

        return new ServiceOutput<>(response);
    }

    // =========================================================================
    // Get All
    // =========================================================================

    /**
     * Retrieves all active restaurants.
     *
     * @param input service execution input
     *
     * @return active restaurants
     */
    @Override
    public IServiceOutput<List<RestaurantListResponse>> getAll(IServiceInput<Void> input) {

        Objects.requireNonNull(input, "Restaurant service input must not be null.");

        List<RestaurantEntity> restaurants = restaurantRepository.findAllActive();

        List<RestaurantListResponse> responses = restaurants.stream().map(restaurantMapper::toListResponse).toList();

        return new ServiceOutput<>(responses);
    }

    // =========================================================================
    // Update
    // =========================================================================

    /**
     * Updates standard business information of an existing restaurant.
     *
     * <p>
     * Lifecycle status and operational availability are intentionally excluded from
     * this operation.
     * </p>
     *
     * <p>
     * When a replacement image is supplied, the image is processed and the
     * resulting snapshot supplied through the update input is applied to the
     * restaurant. When no replacement image is supplied, the existing image
     * reference remains unchanged.
     * </p>
     *
     * @param input restaurant update input
     *
     * @return updated restaurant details
     */
    @Override
    public IServiceOutput<RestaurantDetailsResponse> update(IServiceInput<UpdateRestaurantInputDTO> input) {

        Objects.requireNonNull(input, "Restaurant service input must not be null.");

        UpdateRestaurantInputDTO updateInput = input.getInput();

        Objects.requireNonNull(updateInput, "Restaurant update input must not be null.");

        RestaurantUpdateRequest request = updateInput.getRestaurantRequest();

        Objects.requireNonNull(request, "Restaurant update request must not be null.");

        // ---------------------------------------------------------------------
        // Retrieve active Restaurant
        // ---------------------------------------------------------------------

        RestaurantEntity restaurant = restaurantRepository.findActiveById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_NOT_FOUND));

        // ---------------------------------------------------------------------
        // Update business information
        // ---------------------------------------------------------------------

        restaurant.setRestaurantName(request.getRestaurantName());

        restaurant.setDescription(request.getDescription());

        restaurant.setPhoneNumber(PhoneNumber.toPhoneNumber(request.getPhoneNumber()));

        restaurant.setEmailAddress(EmailAddress.toEmailAddress(request.getEmailAddress()));

        restaurant.setWebsite(request.getWebsite());

        restaurant.setCuisineTypes(request.getCuisineTypes());

        // ---------------------------------------------------------------------
        // Process replacement logo
        // ---------------------------------------------------------------------

        if (updateInput.getLogoImageFile() != null && !updateInput.getLogoImageFile().isEmpty()) {

            ImageEntity logoImageEntity = createImageForRestaurant(updateInput.getLogoImageFile());

            validateImageReference(logoImageEntity.getId(), RestaurantErrorConstants.RESTAURANT_LOGO_IMAGE_NOT_FOUND);

            if (updateInput.getLogoImage() != null) {
                restaurant.setRestaurantLogoImage(updateInput.getLogoImage());
            }
        }

        // ---------------------------------------------------------------------
        // Process replacement cover image
        // ---------------------------------------------------------------------

        if (updateInput.getCoverImageFile() != null && !updateInput.getCoverImageFile().isEmpty()) {

            ImageEntity coverImageEntity = createImageForRestaurant(updateInput.getCoverImageFile());

            validateImageReference(coverImageEntity.getId(), RestaurantErrorConstants.RESTAURANT_COVER_IMAGE_NOT_FOUND);

            if (updateInput.getCoverImage() != null) {
                restaurant.setRestaurantCoverImage(updateInput.getCoverImage());
            }
        }

        // ---------------------------------------------------------------------
        // Persist
        // ---------------------------------------------------------------------

        restaurant = restaurantRepository.save(restaurant);

        LOGGER.info("Restaurant updated successfully. id=[{}], restaurantNumber=[{}]", restaurant.getId(),
                restaurant.getRestaurantNumber());

        RestaurantDetailsResponse response = restaurantMapper.toDetailsResponse(restaurant);

        return new ServiceOutput<>(response);
    }

    // =========================================================================
    // Update Status
    // =========================================================================

    /**
     * Updates the lifecycle status of a restaurant.
     *
     * <p>
     * Validates the requested transition against the Restaurant lifecycle rules.
     * </p>
     *
     * <p>
     * Any non-active lifecycle state automatically becomes operationally
     * unavailable.
     * </p>
     *
     * @param input restaurant status update input
     *
     * @return updated restaurant details
     */
    @Override
    public IServiceOutput<RestaurantDetailsResponse> updateStatus(IServiceInput<RestaurantStatusUpdateRequest> input) {

        Objects.requireNonNull(input, "Restaurant service input must not be null.");

        RestaurantStatusUpdateRequest request = input.getInput();

        Objects.requireNonNull(request, "Restaurant status update request must not be null.");

        RestaurantEntity restaurant = restaurantRepository.findActiveById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_NOT_FOUND));

        RestaurantStatusConstant currentStatus = restaurant.getStatus();

        RestaurantStatusConstant requestedStatus = request.getStatus();

        Objects.requireNonNull(currentStatus, "Current restaurant status must not be null.");

        Objects.requireNonNull(requestedStatus, "Requested restaurant status must not be null.");

        // ---------------------------------------------------------------------
        // Validate duplicate status
        // ---------------------------------------------------------------------

        if (currentStatus == requestedStatus) {

            throw new IllegalStateException(getAlreadyStatusError(currentStatus).getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Validate transition
        // ---------------------------------------------------------------------

        validateStatusTransition(currentStatus, requestedStatus);

        // ---------------------------------------------------------------------
        // Apply lifecycle status
        // ---------------------------------------------------------------------

        restaurant.setStatus(requestedStatus);

        // ---------------------------------------------------------------------
        // Non-active status cannot be available
        // ---------------------------------------------------------------------

        if (requestedStatus != RestaurantStatusConstant.ACTIVE) {
            restaurant.setAvailable(false);
        }

        restaurant = restaurantRepository.save(restaurant);

        LOGGER.info("Restaurant status updated. id=[{}], oldStatus=[{}], newStatus=[{}]", restaurant.getId(),
                currentStatus, requestedStatus);

        RestaurantDetailsResponse response = restaurantMapper.toDetailsResponse(restaurant);

        return new ServiceOutput<>(response);
    }

    // =========================================================================
    // Update Availability
    // =========================================================================

    /**
     * Updates the operational availability of a restaurant.
     *
     * <p>
     * Availability can only be enabled while the Restaurant lifecycle status is
     * {@link RestaurantStatusConstant#ACTIVE}.
     * </p>
     *
     * <p>
     * Lifecycle status and operational availability remain independent concerns.
     * </p>
     *
     * @param input restaurant availability update input
     *
     * @return updated restaurant details
     */
    @Override
    public IServiceOutput<RestaurantDetailsResponse> updateAvailability(
            IServiceInput<RestaurantAvailabilityUpdateRequest> input) {

        Objects.requireNonNull(input, "Restaurant service input must not be null.");

        RestaurantAvailabilityUpdateRequest request = input.getInput();

        Objects.requireNonNull(request, "Restaurant availability update request must not be null.");

        RestaurantEntity restaurant = restaurantRepository.findActiveById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_NOT_FOUND));

        // ---------------------------------------------------------------------
        // Validate lifecycle status before enabling availability
        // ---------------------------------------------------------------------

        if (request.isAvailable() && restaurant.getStatus() != RestaurantStatusConstant.ACTIVE) {

            throw new IllegalStateException(RestaurantErrorConstants.INVALID_RESTAURANT_STATUS.getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Validate duplicate availability
        // ---------------------------------------------------------------------

        if (restaurant.isAvailable() == request.isAvailable()) {

            if (request.isAvailable()) {

                throw new IllegalStateException(
                        RestaurantErrorConstants.RESTAURANT_ALREADY_AVAILABLE.getErrorMessage());

            }

            throw new IllegalStateException(RestaurantErrorConstants.RESTAURANT_ALREADY_UNAVAILABLE.getErrorMessage());
        }

        // ---------------------------------------------------------------------
        // Update availability
        // ---------------------------------------------------------------------

        restaurant.setAvailable(request.isAvailable());

        restaurant = restaurantRepository.save(restaurant);

        LOGGER.info("Restaurant availability updated. id=[{}], available=[{}]", restaurant.getId(),
                restaurant.isAvailable());

        RestaurantDetailsResponse response = restaurantMapper.toDetailsResponse(restaurant);

        return new ServiceOutput<>(response);
    }

    // =========================================================================
    // Status Transition Validation
    // =========================================================================

    /**
     * Validates whether a Restaurant lifecycle status transition is permitted.
     *
     * <p>
     * CLOSED is a terminal lifecycle state and cannot transition to another
     * lifecycle state.
     * </p>
     *
     * @param currentStatus   current lifecycle status
     * @param requestedStatus requested lifecycle status
     */
    private void validateStatusTransition(RestaurantStatusConstant currentStatus,
            RestaurantStatusConstant requestedStatus) {

        boolean validTransition = switch (currentStatus) {

            case ACTIVE -> requestedStatus == RestaurantStatusConstant.INACTIVE
                    || requestedStatus == RestaurantStatusConstant.SUSPENDED
                    || requestedStatus == RestaurantStatusConstant.CLOSED;

            case INACTIVE ->
                requestedStatus == RestaurantStatusConstant.ACTIVE
                        || requestedStatus == RestaurantStatusConstant.SUSPENDED
                        || requestedStatus == RestaurantStatusConstant.CLOSED;

            case SUSPENDED ->
                requestedStatus == RestaurantStatusConstant.ACTIVE
                        || requestedStatus == RestaurantStatusConstant.INACTIVE
                        || requestedStatus == RestaurantStatusConstant.CLOSED;

            case CLOSED -> false;
        };

        if (!validTransition) {

            throw new IllegalStateException(
                    RestaurantErrorConstants.INVALID_RESTAURANT_STATUS_TRANSITION.getErrorMessage());
        }
    }

    /**
     * Resolves the appropriate already-active status error.
     *
     * <p>
     * The existing error contract currently provides a dedicated
     * {@code RESTAURANT_ALREADY_ACTIVE} error. Other duplicate lifecycle states are
     * therefore represented by the generic invalid transition error until dedicated
     * errors are introduced.
     * </p>
     *
     * @param status current Restaurant status
     *
     * @return applicable business error
     */
    private RestaurantErrorConstants getAlreadyStatusError(RestaurantStatusConstant status) {

        if (status == RestaurantStatusConstant.ACTIVE) {
            return RestaurantErrorConstants.RESTAURANT_ALREADY_ACTIVE;
        }

        return RestaurantErrorConstants.INVALID_RESTAURANT_STATUS_TRANSITION;
    }

    // =========================================================================
    // Image Validation
    // =========================================================================

    /**
     * Validates an optional Restaurant image reference.
     *
     * @param imageId image identifier
     * @param error   error returned when the image cannot be resolved
     */
    private void validateImageReference(String imageId, com.foodies.freshmeal.common.exception.IBusinessError error) {

        if (imageId == null || imageId.isBlank()) {
            return;
        }

        IServiceInput<String> imageInput = new ServiceInput<>();

        imageInput.setInput(imageId);

        imageInput.setServiceContext(serviceContext);

        IServiceOutput<ImageEntity> imageOutput = imageService.getImageEntityById(imageInput);

        if (imageOutput == null || imageOutput.getOutput() == null) {

            throw new ResourceNotFoundException(error);
        }

        LOGGER.debug("Validated Restaurant image reference [{}]", imageId);
    }

    // =========================================================================
    // Restaurant Image Creation
    // =========================================================================

    /**
     * Creates an ImageEntity for a Restaurant image.
     *
     * <p>
     * When no file is supplied, the existing FreshMeal default image
     * configuration is used.
     * </p>
     *
     * @param imageFile uploaded image
     *
     * @return Restaurant image entity
     */
    private ImageEntity createImageForRestaurant(
            MultipartFile imageFile) {

        ImageEntity imageEntity = (ImageEntity) EntityFactory.createEntity(
                EntityName.IMAGE_ENTITY);

        if (imageFile == null
                || imageFile.isEmpty()) {

            imageEntity.setImageName(
                    DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE);

            imageEntity.setImageUrl(
                    DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE_URL);

            return imageEntity;
        }

        IServiceInput<CreateImageInputDTO> imageServiceInput = new ServiceInput<>();

        CreateImageInputDTO createImageInputDTO = new CreateImageInputDTO();

        createImageInputDTO.setFile(imageFile);

        imageServiceInput.setInput(
                createImageInputDTO);

        imageServiceInput.setServiceContext(
                serviceContext);

        IServiceOutput<ImageEntity> imageEntityOutput = imageService.uploadImageToS3(
                imageServiceInput);

        Objects.requireNonNull(
                imageEntityOutput,
                "Image service output must not be null.");

        imageEntity = imageEntityOutput.getOutput();

        Objects.requireNonNull(
                imageEntity,
                "Image service image entity must not be null.");

        return imageEntity;
    }
}
