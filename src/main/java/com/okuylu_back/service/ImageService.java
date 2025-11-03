package com.okuylu_back.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageService {
    Map<String, Object> uploadImage(MultipartFile file);
    Map<String, Object> uploadImage(MultipartFile file, String folder, int width, int height);
    String deleteImage(String publicId);
    List<Map<String, Object>> getAllImages();
    Map<String, Object> getImageInfo(String publicId);

}