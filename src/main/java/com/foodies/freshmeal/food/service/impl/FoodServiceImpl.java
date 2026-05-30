package com.foodies.freshmeal.food.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.food.service.IFoodService;

@Service
public class FoodServiceImpl implements IFoodService {

    @Override
    public String uploadImageFile(MultipartFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
