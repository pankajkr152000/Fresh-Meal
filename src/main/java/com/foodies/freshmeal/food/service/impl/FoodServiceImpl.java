package com.foodies.freshmeal.food.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.dto.DropdownOption;
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
import com.foodies.freshmeal.food.constants.CategoryGroup;
import com.foodies.freshmeal.food.constants.CuisineType;
import com.foodies.freshmeal.food.constants.DefaultFoodImageConstants;
import com.foodies.freshmeal.food.constants.DietCategory;
import com.foodies.freshmeal.food.constants.FoodCategory;
import com.foodies.freshmeal.food.constants.FoodStatus;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.dto.FoodStatusRequest;
import com.foodies.freshmeal.food.dto.FoodStatusResponse;
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
        foodResponse.setFoodCategory(foodEntity.getFoodCategory().getDisplayName());
        foodResponse.setImageUrl(foodEntity.getImageUrl());
        foodResponse.setDietCategory(foodEntity.getDietCategory().getDisplayName());
        foodResponse.setCuisineType(foodEntity.getCuisineType().getDisplayName());
        foodResponse.setCategoryGroup(foodEntity.getCategoryGroup().getDisplayName());
        foodResponse.setFoodStatus(foodEntity.getStatus().name());
        foodResponse.setAvailable(foodEntity.isAvailable());
        foodResponse.setAllowedStatuses(foodEntity.getStatus().getAllowedTransitions());
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
    public IServiceOutput<List<DropdownOption>> getFoodCategories(IServiceInput<Void> input) {
        List<DropdownOption> foodCategoriesList = Arrays.stream(FoodCategory.values())
                .sorted(Comparator.comparing(
                        (FoodCategory cg) -> cg.getDisplayName(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DropdownOption(
                        groupCategory.getDisplayName(),
                        groupCategory.name()))
                .toList();

        return new ServiceOutput<>(foodCategoriesList);
    }

    @Override
    public IServiceOutput<List<DropdownOption>> getDietCategories(IServiceInput<Void> input) {
        List<DropdownOption> dietCategoriesList = Arrays.stream(DietCategory.values())
                .sorted(Comparator.comparing(
                        (DietCategory cg) -> cg.getDisplayName(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DropdownOption(
                        groupCategory.getDisplayName(),
                        groupCategory.name()))
                .toList();

        return new ServiceOutput<>(dietCategoriesList);
    }

    @Override
    public IServiceOutput<List<DropdownOption>> getCuisineCategories(IServiceInput<Void> input) {
        List<DropdownOption> cuisineCategoriesList = Arrays.stream(CuisineType.values())
                .sorted(Comparator.comparing(
                        (CuisineType cg) -> cg.getDisplayName(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DropdownOption(
                        groupCategory.getDisplayName(),
                        groupCategory.name()))
                .toList();

        return new ServiceOutput<>(cuisineCategoriesList);
    }

    @Override
    public IServiceOutput<List<DropdownOption>> getGroupCategories(IServiceInput<Void> input) {
        List<DropdownOption> groupCategoriesList = Arrays.stream(CategoryGroup.values())
                .sorted(Comparator.comparing(
                        (CategoryGroup cg) -> cg.getDisplayName(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(groupCategory -> new DropdownOption(
                        groupCategory.getDisplayName(),
                        groupCategory.name()))
                .toList();

        return new ServiceOutput<>(groupCategoriesList);
    }

    @Override
    public IServiceOutput<Map<String, Object>> foodCategoryMetadata(IServiceInput<Void> input) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "foodCategories",
                Arrays.stream(FoodCategory.values())
                        .sorted(Comparator.comparing(
                                (FoodCategory groupCategory) -> groupCategory.getDisplayName(),
                                CommonUtils.alphabeticalWithOtherLast()))
                        .map(groupCategory -> Map.of(
                                "label", groupCategory.getDisplayName(),
                                "value", groupCategory.name()))
                        .toList());

        response.put(
                "dietCategories",
                Arrays.stream(DietCategory.values())
                        .sorted(Comparator.comparing(
                                (DietCategory groupCategory) -> groupCategory.getDisplayName(),
                                CommonUtils.alphabeticalWithOtherLast()))
                        .map(groupCategory -> Map.of(
                                "label", groupCategory.getDisplayName(),
                                "value", groupCategory.name()))
                        .toList());

        response.put(
                "cuisineCategories",
                Arrays.stream(CuisineType.values())
                        .sorted(Comparator.comparing(
                                (CuisineType groupCategory) -> groupCategory.getDisplayName(),
                                CommonUtils.alphabeticalWithOtherLast()))
                        .map(groupCategory -> Map.of(
                                "label", groupCategory.getDisplayName(),
                                "value", groupCategory.name()))
                        .toList());

        response.put(
                "groupCategories",
                Arrays.stream(CategoryGroup.values())
                        .sorted(Comparator.comparing(
                                (CategoryGroup groupCategory) -> groupCategory.getDisplayName(),
                                CommonUtils.alphabeticalWithOtherLast()))
                        .map(groupCategory -> Map.of(
                                "label", groupCategory.getDisplayName(),
                                "value", groupCategory.name()))
                        .toList());

        return new ServiceOutput<>(response);
    }

    @Override
    public IServiceOutput<FoodStatusResponse> updateFoodStatus(IServiceInput<FoodStatusRequest> input) {
        /*
         * ------------------------------------------------------------
         * Load Food
         * ------------------------------------------------------------
         */
        FoodStatusRequest request = input.getInput();
        FoodEntity food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Food not found with id : " + request.getFoodId()));

        FoodStatus currentStatus = food.getStatus();
        if (currentStatus == null) {
            throw new InvalidFoodStatusTransitionException(
                    "Food status is not set for food with ID: " + food.getId());
        }

        FoodStatus newStatus = request.getUpdateFoodStatusRequest().getStatus();

        /*
         * ------------------------------------------------------------
         * Same Status Validation
         * ------------------------------------------------------------
         */
        if (currentStatus == newStatus) {
            throw new InvalidFoodStatusTransitionException(
                    "Food is already in status : " + newStatus);
        }

        /*
         * ------------------------------------------------------------
         * Business Transition Validation
         * ------------------------------------------------------------
         */
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidFoodStatusTransitionException(
                    String.format(
                            "Food status cannot be changed from %s to %s.",
                            currentStatus,
                            newStatus));

        }

        /*
         * ------------------------------------------------------------
         * Update Status
         * ------------------------------------------------------------
         */
        food.setStatus(newStatus);
        food.setStatusUpdatedAt(LocalDateTime.now());

        /*
         *
         * Replace once Spring Security is integrated.
         */
        food.setStatusUpdatedBy("ADMIN");
        foodRepository.save(food);

        /*
         * ------------------------------------------------------------
         * Build Response
         * ------------------------------------------------------------
         */

        FoodStatusResponse response = FoodStatusResponse.builder()
                .foodId(food.getId())
                .previousStatus(currentStatus)
                .currentStatus(newStatus)
                .updatedAt(food.getStatusUpdatedAt())
                .updatedBy(food.getStatusUpdatedBy())
                .build();

        /*
         * return response
         */
        IServiceOutput<FoodStatusResponse> output = new ServiceOutput<>();
        output.setOutput(response);
        return output;

    }

}
