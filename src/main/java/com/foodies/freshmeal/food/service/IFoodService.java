package com.foodies.freshmeal.food.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFoodService {

    String uploadImageFile(MultipartFile file);

}
