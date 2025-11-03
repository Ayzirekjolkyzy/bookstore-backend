package com.okuylu_back.controller.admin;

import com.okuylu_back.dto.request.GenreRequest;
import com.okuylu_back.dto.responses.GenreResponse;
import com.okuylu_back.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/genres")
public class AdminGenreController {

    private final GenreService genreService;

    public AdminGenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Создать жанр")
    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody GenreRequest genreRequest) {
        GenreResponse createdGenre = genreService.createGenre(genreRequest);
        return ResponseEntity.ok(createdGenre);
    }

    @Operation(summary = "Получить все жанры")
    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAllGenres() {
        List<GenreResponse> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @Operation(summary = "Получить жанр по ID")
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id) {
        GenreResponse genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @Operation(summary = "Обновить жанр по ID")
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> updateGenre(@PathVariable Long id,@Valid @RequestBody GenreRequest updatedGenreRequest) {
        GenreResponse updatedGenre = genreService.updateGenre(id, updatedGenreRequest);
        return ResponseEntity.ok(updatedGenre);
    }

    @Operation(summary = "Удалить жанр по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}