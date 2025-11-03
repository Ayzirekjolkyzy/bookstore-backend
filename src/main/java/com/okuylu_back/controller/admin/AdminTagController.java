package com.okuylu_back.controller.admin;

import com.okuylu_back.dto.request.TagRequest;
import com.okuylu_back.dto.responses.TagResponse;
import com.okuylu_back.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tags")
public class AdminTagController {

    private final TagService tagService;

    public AdminTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Создать тег")
    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest tagRequest) {
        TagResponse createdTag = tagService.createTag(tagRequest);
        return ResponseEntity.ok(createdTag);
    }

    @Operation(summary = "Получить все теги")
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Получить тег по ID")
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @Operation(summary = "Обновить тег по ID")
    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long id,@Valid @RequestBody TagRequest updatedTagRequest) {
        TagResponse tagResponse = tagService.updateTag(id, updatedTagRequest);
        return ResponseEntity.ok(tagResponse);
    }

    @Operation(summary = "Удалить тег по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}