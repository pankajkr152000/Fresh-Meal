package com.foodies.freshmeal.image.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.image.entity.ImageEntity;

@Repository
public interface IImageRepository extends IBaseRepository<ImageEntity, String> {

}
