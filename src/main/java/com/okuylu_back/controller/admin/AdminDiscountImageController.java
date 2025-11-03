package com.okuylu_back.controller.admin;

import com.okuylu_back.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/discount-banners")
@Tag(name = "admin-discount-banner-controller", description = "Контроллер для управления баннерами скидок (админ)")
public class AdminDiscountImageController {

    private final ImageService imageService;

    public AdminDiscountImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(
            summary = "Загрузить баннер скидки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Баннер успешно загружен в Cloudinary",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "success_response",
                                            value = """
                        {
                          "asset_folder": "discount-banners",
                          "signature": "4a7060408f9f13c24509bd75477aac94516b2261",
                          "format": "png",
                          "resource_type": "image",
                          "secure_url": "https://res.cloudinary.com/example/upload/...",
                          "created_at": "2025-03-27T16:06:57Z",
                          "width": 1440,
                          "height": 500,
                          "public_id": "discount-banners/ajh2n8pocq5h8qnerujl",
                          "url": "http://res.cloudinary.com/example/upload/...",
                          "bytes": 443965
                        }
                    """
                                    ),
                                    schema = @Schema(implementation = Map.class) // Указываем, что это Map<String, Object>
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный размер изображения",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                        {
                            "success": false,
                            "message": "Размеры баннера должны быть 1440x500 пикселей"
                        }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                        {
                            "success": false,
                            "message": "Ошибка при загрузке изображения"
                        }
                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadDiscountBanner(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Читаем изображение
            BufferedImage image = ImageIO.read(file.getInputStream());
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // Проверяем размеры
            if (imageWidth != 1440 || imageHeight != 500) {
                response.put("success", false);
                response.put("message", "Размеры баннера должны быть 1440x500 пикселей");
                return ResponseEntity.badRequest().body(response);
            }

            // Загружаем изображение в сервис
            Map<String, Object> uploadResult = imageService.uploadImage(file, "discount-banners", 1440, 500);
            return ResponseEntity.ok(uploadResult);

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Ошибка при обработке изображения");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Удаление баннера скидки по publicId
    @Operation(summary = "Удалить баннер скидки")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDiscountBanner(@RequestParam("publicId") String publicId) {
        String result = imageService.deleteImage(publicId);
        return ResponseEntity.ok("Баннер скидки успешно удалён: " + publicId);
    }

    // Получить информацию о баннере скидки
    @Operation(
            summary = "Получить информацию о баннере скидки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о баннере",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "success_response",
                                            value = """
                        {
                          "publicId": "discount-banners/abc123",
                          "url": "https://res.cloudinary.com/example/upload/...",
                          "secureUrl": "https://res.cloudinary.com/example/upload/...",
                          "width": 1440,
                          "height": 500,
                          "format": "png",
                          "bytes": 443965,
                          "createdAt": "2025-03-27T16:06:57Z"
                        }
                    """
                                    ),
                                    schema = @Schema(implementation = Map.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Баннер не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                        {
                            "success": false,
                            "message": "Баннер с указанным publicId не найден"
                        }
                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getDiscountBannerInfo(@RequestParam("publicId") String publicId) {
        Map<String, Object> imageInfo = imageService.getImageInfo(publicId);
        return ResponseEntity.ok(imageInfo);
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
