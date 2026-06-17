package com.foodies.freshmeal.image.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.common.config.AWSS3Config;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.image.dto.CreateImageInputDTO;
import com.foodies.freshmeal.image.entity.ImageEntity;
import com.foodies.freshmeal.image.repository.IImageRepository;
import com.foodies.freshmeal.image.service.IImageService;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class ImageServiceImpl implements IImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final IDatabaseSequenceService databaseSequenceService;
    private final AWSS3Config awss3Config;
    private final IImageRepository imageRepository;

    public ImageServiceImpl(IDatabaseSequenceService databaseSequenceService,
            AWSS3Config awss3Config, IImageRepository imageRepository) {
        this.databaseSequenceService = databaseSequenceService;
        this.awss3Config = awss3Config;
        this.imageRepository = imageRepository;
    }

    @Value("${aws.s3.bucket.name}")
    private String BUCKET_NAME;

    @Value("${aws.region}")
    private String REGION;

    @Override
    public IServiceOutput<String> generateImageId(IServiceInput<CreateImageInputDTO> input) {
        long seq = databaseSequenceService.generateSequence(input.getServiceContext(),
                SequenceConstants.IMAGE_SEQUENCE);

        LOGGER.info("Generated image ID: IMG07_{}", seq);

        String imageId = String.format(SequenceConstants.IMAGE_ID_SEQUENCE, seq);

        IServiceOutput<String> output = new ServiceOutput<>();
        output.setOutput(imageId);
        return output;
    }

    @Override
    public IServiceOutput<ImageEntity> uploadImageToS3(IServiceInput<CreateImageInputDTO> input) {

        CreateImageInputDTO dto = input.getInput();
        MultipartFile file = dto.getFile();
        /*
         * Generate a unique image name using the image service
         */
        String imageName = generateImageName(input.getServiceContext(), file);
        ImageEntity imageEntity = (ImageEntity) EntityFactory.createEntity(EntityName.IMAGE_ENTITY);
        imageEntity.setImageName(imageName);
        /*
         * Upload the image to AWS S3 using the AWSS3Config
         */
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(imageName)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            /*
             * Upload the file to S3 and get the response
             */
            PutObjectResponse putObjectResponse = awss3Config.s3Client().putObject(putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));

            /*
             * Return the URL of the uploaded image
             */
            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                System.out.println("Image uploaded successfully: " + imageName);

                /*
                 * save image entity to the database
                 */
                IServiceInput<CreateImageInputDTO> serviceinput = new ServiceInput<>();
                CreateImageInputDTO createImageInputDTO = new CreateImageInputDTO();
                createImageInputDTO.setFile(file);
                createImageInputDTO.setImageEntity(imageEntity);
                serviceinput.setInput(createImageInputDTO);

                imageEntity = createImageEntity(serviceinput).getOutput();

            } else {
                System.err.println("Failed to upload image: " + imageName);
                throw new RuntimeException("Failed to upload image to S3");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to upload image to S3", ex);
        }

        IServiceOutput<ImageEntity> output = new ServiceOutput<>();
        output.setOutput(imageEntity);
        return output;
    }

    @Override
    public IServiceOutput<String> getImageURLByImageId(IServiceInput<String> input) {
        String imgeId = input.getInput();
        ImageEntity imageEntity = imageRepository.findById(imgeId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imgeId));
        String fileURL = imageEntity.getImageUrl();
        IServiceOutput<String> output = new ServiceOutput<>();
        output.setOutput(fileURL);
        return output;
    }

    @Override
    public IServiceOutput<String> getImageName(IServiceInput<String> input) {
        String imgeId = input.getInput();
        ImageEntity imageEntity = imageRepository.findById(imgeId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imgeId));
        String imageName = imageEntity.getImageName();
        IServiceOutput<String> output = new ServiceOutput<>();
        output.setOutput(imageName);
        return output;
    }

    @Override
    public IServiceOutput<Boolean> deleteImageFromS3(IServiceInput<String> input) {
        String imageId = input.getInput();
        ImageEntity imageEntity = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
        String imageName = imageEntity.getImageName();

        awss3Config.s3Client().deleteObject(builder -> builder.bucket(BUCKET_NAME).key(imageName).build());

        imageRepository.deleteById(imageId);

        IServiceOutput<Boolean> output = new ServiceOutput<>();
        output.setOutput(true);
        return output;
    }

    @Override
    public IServiceOutput<ImageEntity> createImageEntity(IServiceInput<CreateImageInputDTO> input) {
        CreateImageInputDTO dto = input.getInput();
        MultipartFile file = dto.getFile();
        IServiceContext serviceContext = input.getServiceContext();

        ImageEntity imageEntity;
        String imageName;
        if (dto.getImageEntity() == null) {
            imageEntity = (ImageEntity) EntityFactory.createEntity(EntityName.IMAGE_ENTITY);
            imageName = generateImageName(serviceContext, file);
            imageEntity.setImageName(imageName);
        } else {
            imageEntity = dto.getImageEntity();
            imageName = imageEntity.getImageName();
        }
        
        String fileURL = String.format("https://%s.s3.%s.amazonaws.com/%s",
                BUCKET_NAME,
                REGION,
                imageName);
        imageEntity.setId(imageName);
        imageEntity.setImageName(imageName);
        imageEntity.setImageUrl(fileURL);
        imageEntity.setOriginalFileName(file.getOriginalFilename());
        imageEntity.setContentType(file.getContentType());
        imageEntity.setFileSize(file.getSize());
        imageEntity.setStoragePath(imageName);
        imageEntity.setActive(true);
        imageEntity.setCreatedAt(AppCalendar.getBusinessLocalDateTime());
        if (serviceContext.getUserProfile() != null) {
            imageEntity.setCreatedBy(serviceContext.getUserProfile().getId());
        }
        imageEntity.setFileSize(file.getSize());
        imageEntity.setThumbnailUrl(fileURL);
        imageEntity.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        imageEntity.setWidth(0); // You can set the actual width if you have it
        imageEntity.setHeight(0); // You can set the actual height if you have it

        imageRepository.save((ImageEntity) imageEntity);

        IServiceOutput<ImageEntity> output = new ServiceOutput<>();
        output.setOutput(imageEntity);
        return output;
    }

    /*
     * Generate a unique image name using the image service
     */
    public String generateImageName(IServiceContext serviceContext, MultipartFile file) {

        StringBuilder imageNameBuilder = new StringBuilder();

        IServiceInput<CreateImageInputDTO> input = new ServiceInput<>();
        CreateImageInputDTO dto = new CreateImageInputDTO();
        dto.setFile(file);
        input.setInput(dto);

        // 1. Get the ServiceOutput object
        var serviceOutput = generateImageId(input);

        // 2. Extract the actual ID string from the response object
        // (Adjust .getData() or .getId() to match your actual ServiceOutput class
        // methods)
        String uniqueId = serviceOutput.getOutput();

        // 3. Append the clean ID string and the file extension
        imageNameBuilder.append(uniqueId);
        imageNameBuilder.append(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));

        String imageName = imageNameBuilder.toString();
        LOGGER.info("Generated image name: {}", imageName);
        return imageName;
    }

    @Override
    public IServiceOutput<ImageEntity> getImageEntityById(IServiceInput<String> input) {
        String imageId = input.getInput();
        ImageEntity imageEntity = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
        IServiceOutput<ImageEntity> output = new ServiceOutput<>();
        output.setOutput(imageEntity);
        return output;
    }

}
