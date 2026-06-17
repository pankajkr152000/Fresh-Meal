package com.foodies.freshmeal.image.entity.impl;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.image.entity.IImageEntity;


@Document(collection = "fm_image")
public class ImageEntity extends ABaseEntity implements IImageEntity {
	private static final long serialVersionUID = -7738313744978922952L;
	@Id
	private String id;
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
	

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getImageName() {
		return this.imageName;
	}

	@Override
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String getOriginalFileName() {
		return this.originalFileName;
	}

	@Override
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
		
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public Long getFileSize() {
		return this.fileSize;
	}

	@Override
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;	
	}

	@Override
	public String getImageUrl() {
		return this.imageUrl;
	}

	@Override
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String getThumbnailUrl() {
		return this.thumbnailUrl;
	}

	@Override
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;		
	}

	@Override
	public String getStoragePath() {
		return this.storagePath;
	}

	@Override
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	@Override
	public String getExtension() {
		return this.extension;
	}

	@Override
	public void setExtension(String extension) {
		this.extension = extension;
		
	}

	@Override
	public Integer getWidth() {
		return this.width;
	}

	@Override
	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public Integer getHeight() {
		return this.height;
	}

	@Override
	public void setHeight(Integer height) {
		this.height = height;
		
	}

	@Override
	public Boolean getActive() {
		return this.active;
	}

	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

}
