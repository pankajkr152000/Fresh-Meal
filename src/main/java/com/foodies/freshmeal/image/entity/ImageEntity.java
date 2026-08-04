package com.foodies.freshmeal.image.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "fm_image")
public class ImageEntity extends ABaseEntity {
    private static final long serialVersionUID = -7738313744978922952L;

    private String imageName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String imageUrl;
    private String thumbnailUrl;
    private String storagePath;
    private String extension;
    private Integer width;
    private Integer height;
    private Boolean active;

    ImageEntity() {

    }

    public static IEntity create() {
        return new ImageEntity();
    }

}
