package com.foodies.freshmeal.food.entity;

import com.foodies.freshmeal.common.entity.IEntity;

public interface IFoodEntity extends IEntity {
    String getId();
    String getImageName();
    String getFoodName();
    String getDescription();
    double getPrice();
    String getCategory();
    String getImageUrl();

    public void setId(String name);
    public void setImageName(String imageName);
    public void setFoodName(String foodName);
    public void setDescription(String description);
    public void setPrice(double price);
    public void setCategory(String category);
    public void setImageUrl(String imageUrl);

}
