package com.foodies.freshmeal.food.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.food.entity.impl.FoodEntity;

@Repository
public interface IFoodRepository extends IBaseRepository<FoodEntity, String> {

}
