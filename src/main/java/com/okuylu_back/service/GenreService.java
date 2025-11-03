package com.okuylu_back.service;

import com.okuylu_back.dto.request.GenreRequest;
import com.okuylu_back.dto.responses.GenreResponse;

import java.util.List;

public interface GenreService {
    List<GenreResponse> getAllGenres();
    GenreResponse getGenreById(Long id);
    GenreResponse createGenre(GenreRequest genre);
    GenreResponse updateGenre(Long id, GenreRequest genreDetails);
    void deleteGenre(Long id);
}