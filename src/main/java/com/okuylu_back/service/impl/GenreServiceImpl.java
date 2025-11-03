package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.GenreRequest;
import com.okuylu_back.dto.responses.GenreResponse;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import com.okuylu_back.model.Genre;
import com.okuylu_back.repository.GenreRepository;
import com.okuylu_back.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreResponse createGenre(GenreRequest genreRequest) {
        if (genreRepository.existsByName(genreRequest.getName())) {
            throw new IllegalArgumentException("A genre with this name already exists: " + genreRequest.getName());
        }

        Genre genre = new Genre();
        genre.setName(genreRequest.getName());

        genreRepository.save(genre);

        return new GenreResponse(genre.getGenreId(), genre.getName());
    }

    @Override
    public List<GenreResponse> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genre -> new GenreResponse(genre.getGenreId(),
                        genre.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public GenreResponse getGenreById(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " not found."));
        return new GenreResponse(genre.getGenreId(), genre.getName());
    }

    @Override
    public GenreResponse updateGenre(Long id, GenreRequest updatedGenreRequest) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + id + " not found."));

        existingGenre.setName(updatedGenreRequest.getName());
        genreRepository.save(existingGenre);
        return new GenreResponse(existingGenre.getGenreId(), existingGenre.getName());
    }

    @Override
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre with ID " + id + " not found.");
        }
        genreRepository.deleteById(id);
    }


}