package com.foodies.freshmeal.food.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.InvalidFoodStatusTransitionException;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
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
import com.foodies.freshmeal.food.constants.FoodStatusConstant;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodIdRequest;
import com.foodies.freshmeal.food.dto.FoodMetadataResponse;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.food.repository.IFoodRepository;
import com.foodies.freshmeal.food.service.IFoodService;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.image.service.IImageService;
import com.foodies.freshmeal.image.service.impl.ImageServiceImpl;

@Service
public class FoodServiceImpl implements IFoodService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final IImageService imageService;
    private final IServiceContext serviceContext;
    private final IDatabaseSequenceService databaseSequenceService;
    private final IFoodRepository foodRepository;

    public FoodServiceImpl(
            IImageService imageService,
            IServiceContext serviceContext,
            IDatabaseSequenceService databaseSequenceService,
            IFoodRepository foodRepository) {
        this.imageService = imageService;
        this.serviceContext = serviceContext;
        this.databaseSequenceService = databaseSequenceService;
        this.foodRepository = foodRepository;
    }

    /*
     * get the food entity using food id
     */
    @Override
    public IServiceOutput<FoodEntity> loadFood(IServiceInput<FoodIdRequest> request) {

        FoodIdRequest foodRequest = request.getInput();
        FoodEntity output = foodRepository
                .findById(foodRequest.getFoodId()).orElseThrow(
                        () -> new ResourceNotFoundException("Food not found with id : " + foodRequest.getFoodId()));

        return new ServiceOutput<>(output);

    }

    @Override
    public IServiceOutput<String> generateFoodId(IServiceInput<CreateFoodInputDTO> input) {

        long seq = databaseSequenceService.generateSequence(serviceContext, SequenceConstants.FOOD_SEQUENCE);

        LOGGER.info("Generated food ID: FOD01_{}", seq);

        IServiceOutput<String> output = new ServiceOutput<>();

        String foodId = String.format(SequenceConstants.FOOD_ID_SEQUENCE, seq);

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

        FoodResponse foodResponse = convertToFoodResponse(foodEntity, new FoodResponse());

        IServiceOutput<FoodResponse> output = new ServiceOutput<>();
        output.setOutput(foodResponse);
        return output;
    }

    private FoodResponse convertToFoodResponse(FoodEntity foodEntity, FoodResponse foodResponse) {
        foodResponse.setId(foodEntity.getId());
        foodResponse.setImageName(foodEntity.getImageName());
        foodResponse.setFoodName(foodEntity.getFoodName());
        foodResponse.setDescription(foodEntity.getDescription());
        foodResponse.setPrice(foodEntity.getPrice());
        foodResponse.setFoodCategory(foodEntity.getFoodCategory().getLabel());
        foodResponse.setImageUrl(foodEntity.getImageUrl());
        foodResponse.setDietCategory(foodEntity.getDietCategory().getLabel());
        foodResponse.setCuisineType(foodEntity.getCuisineType().getLabel());
        foodResponse.setCategoryGroup(foodEntity.getCategoryGroup().getLabel());
        foodResponse.setFoodStatus(foodEntity.getStatus().name());
        foodResponse.setAvailable(foodEntity.isAvailable());
        foodResponse.setAllowedStatuses(foodEntity.getStatus().getAllowedTransitionOptions());
        return foodResponse;
    }

    @Override
    public IServiceOutput<FoodEntity> createFoodEntity(IServiceInput<CreateFoodInputDTO> input) {
        FoodEntity foodEntity = (FoodEntity) EntityFactory.createEntity(EntityName.FOOD_ENTITY);
        // Map the fields from foodRequest to foodEntity
        FoodRequest foodRequest = input.getInput().getFoodRequest();
        MultipartFile imageFile = input.getInput().getImageFile();
        IServiceInput<CreateFoodInputDTO> foodServiceInput = new ServiceInput<>();

        CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO(foodRequest, imageFile);
        foodServiceInput.setInput(createFoodInputDTO);

        String foodId = generateFoodId(foodServiceInput).getOutput();
        foodEntity.setId(foodId);

        /*
         * if image is not provided by the user, then add a default image to the food
         * entity
         */
        if (imageFile == null || imageFile.isEmpty()) {
            foodEntity.setImageName(DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE);
            foodEntity.setImageUrl(DefaultFoodImageConstants.DEFAULT_FOOD_IMAGE_URL);
        } else {

            IServiceInput<CreateImageInputDTO> imageServiceInput = new ServiceInput<>();
            CreateImageInputDTO createImageInputDTO = new CreateImageInputDTO();
            createImageInputDTO.setFile(imageFile);
            imageServiceInput.setInput(createImageInputDTO);

            IServiceOutput<ImageEntity> imageEntityOutput = imageService.uploadImageToS3(imageServiceInput);
            ImageEntity imageEntity = imageEntityOutput.getOutput();
            foodEntity.setImageName(imageEntity.getImageName());
            foodEntity.setImageUrl(imageEntity.getImageUrl());

        }
        foodEntity.setFoodName(foodRequest.getFoodName());
        foodEntity.setDescription(foodRequest.getDescription());
        foodEntity.setPrice(foodRequest.getPrice());
        foodEntity.setFoodCategory(foodRequest.getFoodCategory());
        foodEntity.setDietCategory(foodRequest.getDietCategory());
        foodEntity.setCuisineType(foodRequest.getCuisineType());
        foodEntity.setCategoryGroup(foodRequest.getFoodCategory().getGroup());

        /*
         * Save the food entity to the database
         */
        foodRepository.save((FoodEntity) foodEntity);

        IServiceOutput<FoodEntity> output = new ServiceOutput<>();
        output.setOutput(foodEntity);
        return output;
    }

    @Override
    public IServiceOutput<List<FoodResponse>> readFoods(IServiceInput<Void> input) {
        List<FoodResponse> foodResponses = new ArrayList<>();

        foodRepository.findAll().stream().forEach(foodEntity -> {
            LOGGER.info("Food ID: {}, Food Name: {}, Description: {}, Price: {}, Category: {}, Image URL: {}",
                    foodEntity.getId(),
                    foodEntity.getFoodName(),
                    foodEntity.getDescription(),
                    foodEntity.getPrice(),
                    foodEntity.getFoodCategory(),
                    foodEntity.getImageUrl());
            FoodResponse foodResponse = convertToFoodResponse(foodEntity, new FoodResponse());
            foodResponses.add(foodResponse);
        });

        return new ServiceOutput<>(foodResponses);
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

    @Override
    public IServiceOutput<FoodResponse> readFoodByFoodId(IServiceInput<FoodStatusRequest> input) {

        FoodStatusRequest foodRequest = input.getInput();

        FoodEntity foodEntity = foodRepository.findById(foodRequest.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No food found with this food id :" + " " + input.getInput()));

        /*
         * convert to foodresponse
         */

        // FoodStatusConstant currentFoodStatus = null;
        // if (foodRequest.getUpdateFoodStatusRequest() != null
        // && foodRequest.getUpdateFoodStatusRequest().getStatus() != null) {
        // currentFoodStatus = FoodStatusConstant
        // .valueOf(foodRequest.getUpdateFoodStatusRequest().getStatus().label());
        // }
        FoodResponse foodResponse = buildFoodResponse(foodEntity, foodEntity.getStatus());

        IServiceOutput<FoodResponse> output = new ServiceOutput<>();
        output.setOutput(foodResponse);
        return output;
    }

    @Override
    public IServiceOutput<FoodResponse> updateFoodStatus(IServiceInput<FoodStatusRequest> input) {
        FoodStatusRequest request = input.getInput();

        IServiceInput<FoodIdRequest> inputFoodId = new ServiceInput<>();
        FoodIdRequest foodIdRequest = new FoodIdRequest();
        foodIdRequest.setFoodId(request.getFoodId());
        inputFoodId.setInput(foodIdRequest);
        IServiceOutput<FoodEntity> output = loadFood(inputFoodId);

        FoodEntity food = output.getOutput();

        FoodStatusConstant currentStatus = food.getStatus();
        FoodStatusConstant requestedStatus = null;
        if (request.getUpdateFoodStatusRequest() != null
                && request.getUpdateFoodStatusRequest().getStatus() != null
                && (request.getUpdateFoodStatusRequest().getStatus().value() != null
                        || request.getUpdateFoodStatusRequest().getStatus().label() != null)) {

            String requestedFoodStatus = request.getUpdateFoodStatusRequest().getStatus().value() != null
                    ? request.getUpdateFoodStatusRequest().getStatus().value()
                    : request.getUpdateFoodStatusRequest().getStatus().label();

            requestedStatus = DisplayOptionMapperUtil.fromValue(FoodStatusConstant.class, requestedFoodStatus);
        }
        validateStatusTransition(currentStatus, requestedStatus);

        applyStatus(food, requestedStatus);

        FoodResponse foodResponse = buildFoodResponse(food, currentStatus);

        foodRepository.save(food);

        IServiceOutput<FoodResponse> foodResponseOutput = new ServiceOutput<>();

        foodResponseOutput.setOutput(foodResponse);

        return foodResponseOutput;
    }

    private FoodResponse buildFoodResponse(FoodEntity food, FoodStatusConstant previousStatus) {

        FoodResponse response = FoodResponse.builder()
                .id(food.getId())
                .imageName(food.getImageName())
                .foodName(food.getFoodName())
                .description(food.getDescription())
                .price(food.getPrice())
                .imageUrl(food.getImageUrl())
                .foodCategory(food.getFoodCategory().getLabel())
                .dietCategory(food.getDietCategory().getLabel())
                .cuisineType(food.getCuisineType().getLabel())
                .categoryGroup(food.getCategoryGroup().getLabel())
                .foodStatus(food.getStatus().getLabel())
                .isAvailable(food.getStatus() == FoodStatusConstant.AVAILABLE)
                .allowedStatuses(food.getStatus().getAllowedTransitionOptions())
                .updatedAt(food.getStatusUpdatedAt().toString())
                .updatedBy(food.getStatusUpdatedBy())
                .build();

        response.setPreviousStatus(
                previousStatus == null ? food.getStatus().getLabel() : previousStatus.getLabel());

        return response;

    }

    private void validateStatusTransition(FoodStatusConstant currentStatus, FoodStatusConstant requestedStatus) {

        if (currentStatus == null) {
            throw new InvalidFoodStatusTransitionException("Food status is not set.");
        }

        if (currentStatus == requestedStatus) {
            throw new InvalidFoodStatusTransitionException("Food is already in status : " + requestedStatus);
        }

        if (!currentStatus.canTransitionTo(requestedStatus)) {

            throw new InvalidFoodStatusTransitionException(
                    String.format("Food status cannot be changed from %s to %s.", currentStatus, requestedStatus));

        }

    }

    private void applyStatus(FoodEntity food, FoodStatusConstant newStatus) {

        food.setStatus(newStatus);
        food.setStatusUpdatedAt(AppCalendar.getBusinessLocalDateTime());

        /*
         * Replace once Spring Security is integrated.
         */
        food.setStatusUpdatedBy("ADMIN");

    }

}
