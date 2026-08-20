package com.foodies.freshmeal.restaurant.controller.impl;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.builder.ApiResponseBuilder;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ApiMessageConstants;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.dto.ApiResponse;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.restaurant.constants.RestaurantApiConstants;
import com.foodies.freshmeal.restaurant.controller.IRestaurantController;
import com.foodies.freshmeal.restaurant.dto.CreateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.dto.RestaurantCreateRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantListResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantUpdateRequest;
import com.foodies.freshmeal.restaurant.dto.UpdateRestaurantInputDTO;
import com.foodies.freshmeal.restaurant.service.IRestaurantService;

/**
 * ============================================================================
 * Restaurant Controller
 * ============================================================================
 *
 * Responsibilities ----------------
 *
 * • Receive HTTP requests. • Deserialize Restaurant JSON requests. • Receive
 * optional Restaurant images. • Build service input objects. • Delegate
 * business operations to RestaurantService. • Return standardized ApiResponse.
 *
 * <p>
 * The controller must not contain Restaurant business logic.
 * </p>
 *
 * ============================================================================
 *
 * Current Operations ------------------
 *
 * • Create Restaurant • Read Restaurant • Read All Restaurants • Update
 * Restaurant
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@RestController
@RequestMapping(RestaurantApiConstants.BASE_URL)
public class RestaurantController implements IRestaurantController {

	private final IRestaurantService restaurantService;

	private final ObjectMapper objectMapper;

	private final IServiceContext serviceContext;

	/**
	 * Creates RestaurantController.
	 *
	 * @param restaurantService Restaurant service.
	 * @param objectMapper      Jackson object mapper.
	 * @param serviceContext    Service execution context.
	 */
	public RestaurantController(IRestaurantService restaurantService, ObjectMapper objectMapper,
			IServiceContext serviceContext) {

		this.restaurantService = restaurantService;
		this.objectMapper = objectMapper;
		this.serviceContext = serviceContext;
	}

	// =========================================================================
	// Create Restaurant
	// =========================================================================

	/**
	 * Creates a new restaurant.
	 *
	 * <p>
	 * Endpoint:
	 * </p>
	 *
	 * <pre>
	 * POST / api / restaurants / create
	 * </pre>
	 *
	 * <p>
	 * Request content type:
	 * </p>
	 *
	 * <pre>
	 * multipart / form - data
	 * </pre>
	 *
	 * <p>
	 * Supported request parts:
	 * </p>
	 *
	 * <ul>
	 * <li>restaurant - required JSON</li>
	 * <li>logoImage - optional image</li>
	 * <li>coverImage - optional image</li>
	 * </ul>
	 *
	 * @param restaurantJson restaurant JSON
	 * @param logoImage      optional restaurant logo
	 * @param coverImage     optional restaurant cover image
	 *
	 * @return created restaurant
	 *
	 * @throws JsonProcessingException if JSON parsing fails
	 */
	@Override
	@AuditApi(action = ActionType.CREATE_RESTAURANT, module = ModuleType.RESTAURANT, method = MethodType.CREATE)
	@PostMapping(value = RestaurantApiConstants.CREATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<RestaurantDetailsResponse>> createRestaurant(
			@RequestPart("restaurant") String restaurantJson,
			@RequestPart(value = "logoImage", required = false) MultipartFile logoImage,
			@RequestPart(value = "coverImage", required = false) MultipartFile coverImage)
			throws JsonProcessingException {

		RestaurantCreateRequest restaurantRequest = objectMapper.readValue(restaurantJson,
				RestaurantCreateRequest.class);

		serviceContext.setAttribute("restaurantRequest", restaurantJson);

		CreateRestaurantInputDTO createInputDTO = new CreateRestaurantInputDTO();

		createInputDTO.setRestaurantRequest(restaurantRequest);

		createInputDTO.setLogoImageFile(logoImage);

		createInputDTO.setCoverImageFile(coverImage);

		IServiceInput<CreateRestaurantInputDTO> serviceInput = new ServiceInput<>();

		serviceInput.setInput(createInputDTO);

		serviceInput.setServiceContext(serviceContext);

		IServiceOutput<RestaurantDetailsResponse> serviceOutput = restaurantService.create(serviceInput);

		return ApiResponseBuilder.created(ApiMessageConstants.RESTAURANT_CREATED, serviceOutput.getOutput());
	}

	// =========================================================================
	// Read All Restaurants
	// =========================================================================

	/**
	 * Retrieves all active restaurants.
	 *
	 * <p>
	 * Endpoint:
	 * </p>
	 *
	 * <pre>
	 * GET / api / restaurants / readAllRestaurants
	 * </pre>
	 *
	 * @return active restaurant list
	 */
	@Override
	@AuditApi(action = ActionType.READ_ALL_RESTAURANTS, module = ModuleType.RESTAURANT, method = MethodType.READ)
	@GetMapping(RestaurantApiConstants.READ_ALL_RESTAURANTS)
	public ResponseEntity<ApiResponse<List<RestaurantListResponse>>> readRestaurants() {

		IServiceInput<Void> serviceInput = new ServiceInput<>();

		serviceInput.setServiceContext(serviceContext);

		IServiceOutput<List<RestaurantListResponse>> serviceOutput = restaurantService.getAll(serviceInput);

		return ApiResponseBuilder.success(ApiMessageConstants.RESTAURANT_LIST_FOUND, serviceOutput.getOutput());
	}

	// =========================================================================
	// Get Restaurant By ID
	// =========================================================================

	/**
	 * Retrieves a restaurant by its identifier.
	 *
	 * <p>
	 * Endpoint:
	 * </p>
	 *
	 * <pre>
	 * POST / api / restaurants / view
	 * </pre>
	 *
	 * @param input restaurant identifier request
	 *
	 * @return restaurant details
	 */
	@Override
	@AuditApi(action = ActionType.VIEW_RESTAURANT, module = ModuleType.RESTAURANT, method = MethodType.READ)
	@PostMapping(RestaurantApiConstants.GET_RESTAURANT_BY_ID)
	public ResponseEntity<ApiResponse<RestaurantDetailsResponse>> getRestaurantById(
			@RequestBody RestaurantIdRequest input) {

		IServiceInput<RestaurantIdRequest> serviceInput = new ServiceInput<>();

		serviceInput.setInput(input);

		serviceInput.setServiceContext(serviceContext);

		IServiceOutput<RestaurantDetailsResponse> serviceOutput = restaurantService.getById(serviceInput);

		return ApiResponseBuilder.success(ApiMessageConstants.RESTAURANT_FOUND, serviceOutput.getOutput());
	}

	// =========================================================================
	// Update Restaurant
	// =========================================================================

	/**
	 * Updates an existing restaurant.
	 *
	 * <p>
	 * Endpoint:
	 * </p>
	 *
	 * <pre>
	 * PUT / api / restaurants / update
	 * </pre>
	 *
	 * <p>
	 * Request content type:
	 * </p>
	 *
	 * <pre>
	 * multipart / form - data
	 * </pre>
	 *
	 * <p>
	 * Supported request parts:
	 * </p>
	 *
	 * <ul>
	 * <li>restaurant - required JSON</li>
	 * <li>logoImage - optional replacement image</li>
	 * <li>coverImage - optional replacement image</li>
	 * </ul>
	 *
	 * <p>
	 * If an image is not supplied, the service layer decides whether the existing
	 * image reference remains unchanged.
	 * </p>
	 *
	 * @param restaurantJson restaurant update JSON
	 * @param logoImage      optional replacement logo
	 * @param coverImage     optional replacement cover image
	 *
	 * @return updated restaurant
	 *
	 * @throws JsonProcessingException if JSON parsing fails
	 */
	@Override
	@AuditApi(action = ActionType.UPDATE_RESTAURANT, module = ModuleType.RESTAURANT, method = MethodType.UPDATE)
	@PutMapping(value = RestaurantApiConstants.UPDATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<RestaurantDetailsResponse>> updateRestaurant(
			@RequestPart("restaurant") String restaurantJson,
			@RequestPart(value = "logoImage", required = false) MultipartFile logoImage,
			@RequestPart(value = "coverImage", required = false) MultipartFile coverImage)
			throws JsonProcessingException {

		RestaurantUpdateRequest restaurantRequest = objectMapper.readValue(restaurantJson,
				RestaurantUpdateRequest.class);

		serviceContext.setAttribute("restaurantRequest", restaurantJson);

		UpdateRestaurantInputDTO updateInputDTO = new UpdateRestaurantInputDTO();

		updateInputDTO.setRestaurantRequest(restaurantRequest);

		updateInputDTO.setLogoImageFile(logoImage);

		updateInputDTO.setCoverImageFile(coverImage);

		IServiceInput<UpdateRestaurantInputDTO> serviceInput = new ServiceInput<>();

		serviceInput.setInput(updateInputDTO);

		serviceInput.setServiceContext(serviceContext);

		IServiceOutput<RestaurantDetailsResponse> serviceOutput = restaurantService.update(serviceInput);

		return ApiResponseBuilder.success(ApiMessageConstants.RESTAURANT_UPDATED, serviceOutput.getOutput());
	}
}