package com.foodies.freshmeal.image.entity;



import com.foodies.freshmeal.common.entity.IEntity;

public interface IImageEntity extends IEntity {

    String getId();

    void setId(String id);

    String getImageName();

    void setImageName(String imageName);

    String getOriginalFileName();

    void setOriginalFileName( String originalFileName);

    String getContentType();

    void setContentType(String contentType);

    Long getFileSize();

    void setFileSize(Long fileSize);

    String getImageUrl();

    void setImageUrl(String imageUrl);

    String getThumbnailUrl();

    void setThumbnailUrl(String thumbnailUrl);

    String getStoragePath();

    void setStoragePath(String storagePath);

    String getExtension();

    void setExtension(String extension);

    Integer getWidth();

    void setWidth(Integer width);

    Integer getHeight();

    void setHeight(Integer height);

    Boolean getActive();

    void setActive(Boolean active);
}
