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
import com.foodies.freshmeal.food.constants.DefaultFoodImageConstants;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.image.service.IImageService;
import com.foodies.freshmeal.restaurant.constants.RestaurantErrorConstants;
import com.foodies.freshmeal.restaurant.dto.CreateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
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
 * Provides business operations for Restaurant management.
 *
 * Responsibilities:
 *
 * • Restaurant creation • Restaurant retrieval • Restaurant update • Restaurant
 * identifier generation • Restaurant image reference validation • Repository
 * orchestration
 *
 * DTO transformation is delegated to RestaurantMapper.
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
	 * A single Restaurant sequence value is used to generate both:
	 *
	 * <ul>
	 * <li>Internal database identifier</li>
	 * <li>External business restaurant number</li>
	 * </ul>
	 *
	 * <p>
	 * Supplied logo and cover image identifiers are validated through the existing
	 * ImageService.
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
		// Validate supplied image references
		// ---------------------------------------------------------------------
		ImageEntity coverImageEntity = createImageForRestaurant(input.getInput().getCoverImage().getImageFile());
		validateImageReference(coverImageEntity.getId());

		ImageEntity logoImageEntity = createImageForRestaurant(input.getInput().getLogoImage().getImageFile());
		validateImageReference(logoImageEntity.getId());

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

		List<RestaurantEntity> restaurants = restaurantRepository.findAllActive();

		List<RestaurantListResponse> responses = restaurants.stream().map(restaurantMapper::toListResponse).toList();

		return new ServiceOutput<>(responses);
	}

	// =========================================================================
	// Update
	// =========================================================================

	/**
	 * Updates an existing restaurant.
	 *
	 * <p>
	 * The exact update operation will be implemented according to the finalized
	 * RestaurantUpdateRequest contract.
	 * </p>
	 *
	 * @param input restaurant update input
	 *
	 * @return updated restaurant details
	 */
	@Override
	public IServiceOutput<RestaurantDetailsResponse> update(IServiceInput<UpdateRestaurantInputDTO> input) {

		throw new UnsupportedOperationException(
				"Restaurant update implementation requires the finalized " + "RestaurantUpdateRequest contract.");
	}

	// =========================================================================
	// Image Validation
	// =========================================================================

	/**
	 * Validates an optional Restaurant image reference.
	 *
	 * <p>
	 * Restaurant images are optional. When an image identifier is supplied, the
	 * existing ImageService is used to verify that the image exists.
	 * </p>
	 *
	 * @param imageId image identifier
	 */
	private void validateImageReference(String imageId) {

		if (imageId == null || imageId.isBlank()) {
			return;
		}

		IServiceInput<String> imageInput = new ServiceInput<>();

		imageInput.setInput(imageId);

		IServiceOutput<ImageEntity> imageOutput = imageService.getImageEntityById(imageInput);

		if (imageOutput == null || imageOutput.getOutput() == null) {

			throw new ResourceNotFoundException(RestaurantErrorConstants.RESTAURANT_LOGO_IMAGE_NOT_FOUND);
		}

		LOGGER.debug("Validated Restaurant image reference [{}]", imageId);
	}
	
	private ImageEntity createImageForRestaurant(MultipartFile imageFile) {
		/*
         * if image is not provided by the user, then add a default image to the food
         * entity
         */
		ImageEntity imageEntity =  (ImageEntity)EntityFactory.createEntity(EntityName.IMAGE_ENTITY);
        if (imageFile == null || imageFile.isEmpty()) {
        	imageEntity.setImageName(DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE);
        	imageEntity.setImageUrl(DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE_URL);
        } else {

            IServiceInput<CreateImageInputDTO> imageServiceInput = new ServiceInput<>();
            CreateImageInputDTO createImageInputDTO = new CreateImageInputDTO();
            createImageInputDTO.setFile(imageFile);
            imageServiceInput.setInput(createImageInputDTO);

            IServiceOutput<ImageEntity> imageEntityOutput = imageService.uploadImageToS3(imageServiceInput);
            imageEntity = imageEntityOutput.getOutput();

        }
        return imageEntity;
	}

}