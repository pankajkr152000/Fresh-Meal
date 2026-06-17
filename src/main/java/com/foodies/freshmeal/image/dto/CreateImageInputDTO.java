package com.foodies.freshmeal.image.dto;

import org.springframework.web.multipart.MultipartFile;

import com.foodies.freshmeal.image.entity.ImageEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateImageInputDTO {
    private String imageName;
    private String fileURL;
    private MultipartFile file;
    private ImageEntity imageEntity;
    
    
}
