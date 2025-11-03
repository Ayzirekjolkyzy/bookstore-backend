package com.okuylu_back.controller.admin;

import com.okuylu_back.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/images")
@Tag(name = "admin-image-controller", description = "API для работы с изображениями")
public class AdminImageController {
    private final ImageService imageService;

    public AdminImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Загрузить изображение в Cloudinary")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("Ошибка: Файл не выбран!");
        }
        try {
            Map<String, Object> result = imageService.uploadImage(image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при загрузке изображения: " + e.getMessage());
        }
    }

    @Operation(summary = "Удалить изображение из Cloudinary")
    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<?> deleteImage(@PathVariable String publicId) {
        try {
            String result = imageService.deleteImage(publicId);
            if ("ok".equals(result)) {
                return ResponseEntity.ok("Изображение удалено успешно.");
            } else {
                return ResponseEntity.badRequest().body("Ошибка при удалении изображения.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при удалении: " + e.getMessage());
        }
    }

    @Operation(summary = "Получить информацию об изображении")
    @GetMapping("/info/{publicId}")
    public ResponseEntity<?> getImageInfo(@PathVariable String publicId) {
        try {
            Map<String, Object> result = imageService.getImageInfo(publicId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении информации: " + e.getMessage());
        }
    }

    @Operation(summary = "Получить список всех изображений")
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages() {
        try {
            List<Map<String, Object>> images = imageService.getAllImages();
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при получении списка изображений: " + e.getMessage());
        }
    }
}

