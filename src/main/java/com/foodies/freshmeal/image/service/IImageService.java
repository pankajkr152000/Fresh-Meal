package com.foodies.freshmeal.image.service;


import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.entity.ImageEntity;

public interface IImageService {

    public IServiceOutput<String> generateImageId(IServiceInput<CreateImageInputDTO> input);

    public IServiceOutput<ImageEntity> uploadImageToS3(IServiceInput<CreateImageInputDTO> input);

    public IServiceOutput<String> getImageURLByImageId(IServiceInput<String> input);

    public IServiceOutput<Boolean> deleteImageFromS3(IServiceInput<String> input);

    public IServiceOutput<ImageEntity> createImageEntity(IServiceInput<CreateImageInputDTO> input);

    public IServiceOutput<String> getImageName(IServiceInput<String> input);

    public IServiceOutput<ImageEntity> getImageEntityById(IServiceInput<String> input);



}
