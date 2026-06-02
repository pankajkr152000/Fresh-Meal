package com.foodies.freshmeal.food.dto;

import org.springframework.web.multipart.MultipartFile;

public class CreateFoodInputDTO {
    private FoodRequest foodRequest;
    private MultipartFile imageFile;

    public CreateFoodInputDTO() {
    }

    public CreateFoodInputDTO(FoodRequest foodRequest, MultipartFile imageFile) {
        this.foodRequest = foodRequest;
        this.imageFile = imageFile;
    }

    public FoodRequest getFoodRequest() {
        return foodRequest;
    }

    public void setFoodRequest(FoodRequest foodRequest) {
        this.foodRequest = foodRequest;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}
