package com.foodies.freshmeal.food.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.food.constants.CategoryGroup;
import com.foodies.freshmeal.food.constants.CuisineType;
import com.foodies.freshmeal.food.constants.DietCategory;
import com.foodies.freshmeal.food.constants.FoodCategory;
import com.foodies.freshmeal.food.dto.CreateFoodInputDTO;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.FoodResponse;
import com.foodies.freshmeal.food.entity.IFoodEntity;
import com.foodies.freshmeal.food.entity.impl.FoodEntity;
import com.foodies.freshmeal.food.repository.IFoodRepository;
import com.foodies.freshmeal.food.service.IFoodService;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.entity.IImageEntity;
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
        IServiceOutput<IFoodEntity> foodEntityOutput = createFoodEntity(input);
        IFoodEntity foodEntity = foodEntityOutput.getOutput();

        
        FoodResponse foodResponse = convertToFoodResponse(foodEntity, new FoodResponse());

        IServiceOutput<FoodResponse> output = new ServiceOutput<>();
        output.setOutput(foodResponse);
        return output;
    }

    private FoodResponse convertToFoodResponse(IFoodEntity foodEntity, FoodResponse foodResponse) {
        foodResponse.setId(foodEntity.getId());
        foodResponse.setImageName(foodEntity.getImageName());
        foodResponse.setDescription(foodEntity.getDescription());
        foodResponse.setPrice(foodEntity.getPrice());
        foodResponse.setFoodCategory(foodEntity.getFoodCategory());
        foodResponse.setImageUrl(foodEntity.getImageUrl());
        foodResponse.setDietCategory(foodEntity.getDietCategory());
        foodResponse.setCuisineType(foodEntity.getCuisineType());
        foodResponse.setCategoryGroup(foodEntity.getCategoryGroup());
        
        return foodResponse;
    }



    @Override
    public IServiceOutput<IFoodEntity> createFoodEntity(IServiceInput<CreateFoodInputDTO> input) {
        IFoodEntity foodEntity = (IFoodEntity) EntityFactory.createEntity(EntityName.FOOD_ENTITY);
        // Map the fields from foodRequest to foodEntity
        FoodRequest foodRequest = input.getInput().getFoodRequest();
        MultipartFile imageFile = input.getInput().getImageFile();
        IServiceInput<CreateFoodInputDTO> foodServiceInput = new ServiceInput<>();

        CreateFoodInputDTO createFoodInputDTO = new CreateFoodInputDTO(foodRequest, imageFile);
        foodServiceInput.setInput(createFoodInputDTO);
        
        String foodId = generateFoodId(foodServiceInput).getOutput();
        foodEntity.setId(foodId);

        IServiceInput<CreateImageInputDTO> imageServiceInput = new ServiceInput<>();
        CreateImageInputDTO createImageInputDTO = new CreateImageInputDTO();
        createImageInputDTO.setFile(imageFile);
        imageServiceInput.setInput(createImageInputDTO);
        
        IServiceOutput<IImageEntity> imageEntityOutput = imageService.uploadImageToS3(imageServiceInput);
        IImageEntity imageEntity = imageEntityOutput.getOutput();


        foodEntity.setImageName(imageEntity.getImageName());
        foodEntity.setFoodName(foodRequest.getFoodName());
        foodEntity.setDescription(foodRequest.getDescription());
        foodEntity.setPrice(foodRequest.getPrice());
        foodEntity.setFoodCategory(foodRequest.getFoodCategory());
        foodEntity.setImageUrl(imageEntity.getImageUrl());
        foodEntity.setDietCategory(foodRequest.getDietCategory());
        foodEntity.setCuisineType(foodRequest.getCuisineType());
        foodEntity.setCategoryGroup(foodRequest.getCategoryGroup());

        /*
         * Save the food entity to the database
         */
        foodRepository.save((FoodEntity)foodEntity);

        IServiceOutput<IFoodEntity> output = new ServiceOutput<>();
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
    public IServiceOutput<List<String>> getFoodCategories(IServiceInput<Void> input) {
        List<String> foodCategoriesList =  Arrays.stream(FoodCategory.values())
                .map(Enum::name)
                .toList();

        return new ServiceOutput<>(foodCategoriesList);
    }



    @Override
    public IServiceOutput<List<String>> getDietCategories(IServiceInput<Void> input) {
        List<String> dietCategoriesList =  Arrays.stream(DietCategory.values())
                .map(Enum::name)
                .toList();

        return new ServiceOutput<>(dietCategoriesList);
    }



    @Override
    public IServiceOutput<List<String>> getCuisineCategories(IServiceInput<Void> input) {
        List<String> cuisineCategoriesList =  Arrays.stream(CuisineType.values())
                .map(Enum::name)
                .toList();

        return new ServiceOutput<>(cuisineCategoriesList);
    }



    @Override
    public IServiceOutput<List<String>> getGroupCategories(IServiceInput<Void> input) {
        List<String> groupCategoriesList =  Arrays.stream(CategoryGroup.values())
                .map(Enum::name)
                .toList();

        return new ServiceOutput<>(groupCategoriesList);
    }

    

}
