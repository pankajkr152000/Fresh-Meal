package com.foodies.freshmeal.image.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageSnapshot {
	String imageId;
	String imageName;
	String imageURL;
	MultipartFile imageFile;
}
