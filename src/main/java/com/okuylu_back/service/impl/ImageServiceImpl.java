package com.okuylu_back.service.impl;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.okuylu_back.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
        System.out.println("Cloudinary: " + (cloudinary != null));
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Ошибка: Файл не выбран!");
            throw new RuntimeException("Ошибка: Файл не выбран!");
        }

        try {
            logger.info("Загрузка изображения: {}", file.getOriginalFilename());
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            if (uploadResult == null || !uploadResult.containsKey("secure_url")) {
                logger.error("Ошибка: Cloudinary не вернул URL!");
                throw new RuntimeException("Ошибка: Cloudinary не вернул URL!");
            }

            String imageUrl = (String) uploadResult.get("secure_url");
            logger.info("Файл успешно загружен. URL: {}", imageUrl);
            return uploadResult;
        } catch (IOException e) {
            logger.error("Ошибка при загрузке изображения: {}", e.getMessage());
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folder, int width, int height) {
        if (file.isEmpty()) {
            throw new RuntimeException("Ошибка: Файл не выбран!");
        }

        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", folder,
                    "width", width,
                    "height", height,
                    "crop", "fill" //  Масштабирует и обрезает до точных размеров
            );

            return cloudinary.uploader().upload(file.getBytes(), uploadParams);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки изображения: " + e.getMessage());
        }
    }

    @Override
    public String deleteImage(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            logger.error("Ошибка: publicId не может быть пустым!");
            throw new IllegalArgumentException("Ошибка: publicId не может быть пустым!");
        }

        try {
            logger.info("Удаление изображения с public_id: {}", publicId);
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            if ("ok".equals(result.get("result"))) {
                logger.info("Изображение с public_id {} успешно удалено", publicId);
                return "ok";
            } else {
                logger.warn("Ошибка при удалении изображения с public_id: {}", publicId);
                return "error";
            }
        } catch (IOException e) {
            logger.error("Ошибка при удалении файла: {}", e.getMessage());
            throw new RuntimeException("Ошибка при удалении файла: " + e.getMessage());
        }
    }
    @Override
    public Map<String, Object> getImageInfo(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            logger.error("Ошибка: publicId не может быть пустым!");
            throw new IllegalArgumentException("Ошибка: publicId не может быть пустым!");
        }

        try {
            logger.info("Получение информации об изображении с public_id: {}", publicId);
            Map<String, Object> result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            logger.info("Информация об изображении: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Ошибка при получении информации об изображении: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении информации об изображении: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllImages() {
        try {
            logger.info("Получение списка всех изображений");
            Map<String, Object> result = cloudinary.api().resources(ObjectUtils.emptyMap());
            List<Map<String, Object>> images = (List<Map<String, Object>>) result.get("resources");
            logger.info("Найдено {} изображений", images.size());
            return images;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка изображений: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении списка изображений: " + e.getMessage());
        }
    }
}